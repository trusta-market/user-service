package com.trusta_market.userservice.user.infrastructure.keycloak;

import com.trusta_market.userservice.user.application.port.out.IdentityProviderPort;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.vo.Role;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import jakarta.ws.rs.core.Response;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.RoleRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import lombok.extern.slf4j.Slf4j;

import java.util.Collections;
import java.util.List;

@Slf4j
@Component
public class KeycloakAdapter implements IdentityProviderPort {

    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String username;
    private final String password;

    // 관리자가 변경할 수 있는 역할 목록 (assignRole 시 먼저 모두 제거 후 새 역할 부여)
    private static final List<Role> MANAGED_ROLES = List.of(Role.MEMBER, Role.INSPECTOR);

    public KeycloakAdapter(
            @Value("${trusta.keycloak.server-url}") String serverUrl,
            @Value("${trusta.keycloak.realm}") String realm,
            @Value("${trusta.keycloak.client-id}") String clientId,
            @Value("${trusta.keycloak.username}") String username,
            @Value("${trusta.keycloak.password}") String password) {
        this.serverUrl = serverUrl;
        this.realm = realm;
        this.clientId = clientId;
        this.username = username;
        this.password = password;
    }

    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // 인증용 리얼름 (보통 master 사용)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();
    }

    @Override
    public KeycloakId createIdentity(Email email, String password, Name name) {
        log.info("Attempting to create Keycloak user: {}", email.value());
        Keycloak keycloak = getKeycloakInstance();
        
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(email.value());
        user.setEmail(email.value());
        user.setFirstName(name.value());
        user.setLastName("-"); // Keycloak 기본 필수값 충족을 위해 더미 데이터 삽입
        user.setEmailVerified(true);

        // 비밀번호 설정
        CredentialRepresentation credential = new CredentialRepresentation();
        credential.setType(CredentialRepresentation.PASSWORD);
        credential.setValue(password);
        credential.setTemporary(false);
        user.setCredentials(Collections.singletonList(credential));

        try (Response response = keycloak.realm(realm).users().create(user)) {
            log.info("Keycloak response status: {}", response.getStatus());
            
            if (response.getStatus() == 409) {
                throw new UserException(UserErrorCode.DUPLICATE_EMAIL);
            }
            
            if (response.getStatus() != 201) {
                String errorEntity = response.hasEntity() ? response.readEntity(String.class) : "no entity";
                log.error("Failed to create Keycloak user. Details: {}", errorEntity);
                throw new UserException(UserErrorCode.KEYCLOAK_ERROR);
            }

            if (response.getLocation() == null) {
                throw new UserException(UserErrorCode.KEYCLOAK_ERROR);
            }

            // 생성된 유저의 ID 추출 (Location 헤더에서 가져옴)
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("Successfully created Keycloak user. ID: {}", userId);
            return KeycloakId.of(userId);
        } catch (Exception e) {
            log.error("Exception occurred during Keycloak integration", e);
            throw e;
        } finally {
            keycloak.close();
        }
    }

    @Override
    public void deleteIdentity(KeycloakId keycloakId) {
        Keycloak keycloak = getKeycloakInstance();
        try {
            keycloak.realm(realm).users().get(keycloakId.value()).remove();
        } finally {
            keycloak.close();
        }
    }

    // Keycloak Realm 역할 변경: 기존 역할 제거 → 새 역할 부여 (Gateway JWT 동기화)
    @Override
    public void assignRole(KeycloakId keycloakId, Role role) {
        Keycloak keycloak = getKeycloakInstance();
        try {
            UserResource userResource = keycloak.realm(realm).users().get(keycloakId.value());

            // 1) 기존 관리 대상 역할 제거
            List<RoleRepresentation> rolesToRemove = MANAGED_ROLES.stream()
                    .map(r -> findRealmRole(keycloak, r))
                    .toList();
            userResource.roles().realmLevel().remove(rolesToRemove);

            // 2) 새 역할 부여
            RoleRepresentation newRole = findRealmRole(keycloak, role);
            userResource.roles().realmLevel().add(List.of(newRole));
        } catch (UserException e) {
            throw e;
        } catch (Exception e) {
            log.error("Exception occurred while changing Keycloak role: {}", e.getMessage(), e);
            throw new UserException(UserErrorCode.KEYCLOAK_ERROR);
        } finally {
            keycloak.close();
        }
    }

    // Keycloak Realm에서 역할 이름으로 RoleRepresentation 조회 (없으면 KEYCLOAK_ERROR)
    private RoleRepresentation findRealmRole(Keycloak keycloak, Role role) {
        try {
            return keycloak.realm(realm).roles().get(role.name()).toRepresentation();
        } catch (Exception e) {
            log.error("Keycloak Realm role not found: {}", role.name(), e);
            throw new UserException(UserErrorCode.KEYCLOAK_ERROR);
        }
    }
}

