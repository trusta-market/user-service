package com.trusta_market.userservice.user.infrastructure.keycloak;

import com.trusta_market.userservice.user.application.port.out.IdentityProviderPort;
import com.trusta_market.userservice.user.domain.vo.Email;
import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import com.trusta_market.userservice.user.domain.vo.Name;
import com.trusta_market.userservice.user.domain.exception.UserErrorCode;
import com.trusta_market.userservice.user.domain.exception.UserException;
import jakarta.ws.rs.core.Response;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.List;

// IdentityProviderPort 인터페이스의 Keycloak 기반 구현체 (Adapter)
@Slf4j
@Component
public class KeycloakAdapter implements IdentityProviderPort {

    private final String serverUrl;
    private final String realm;
    private final String clientId;
    private final String username;
    private final String password;

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

    // Keycloak 관리자 API 호출을 위한 인증 객체 생성
    private Keycloak getKeycloakInstance() {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm("master") // 인증용 리얼름 (보통 master 사용)
                .clientId(clientId)
                .username(username)
                .password(password)
                .build();
    }

    // Keycloak 서버에 새로운 사용자 계정을 생성하고 고유 식별자(KeycloakId) 반환
    @Override
    public KeycloakId createIdentity(Email email, String password, Name name) {
        log.info("Attempting to create a new Keycloak user account...");
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
                log.error("Keycloak creation failed. Check server logs for details.");
                throw new UserException(UserErrorCode.KEYCLOAK_ERROR);
            }

            if (response.getLocation() == null) {
                throw new UserException(UserErrorCode.KEYCLOAK_ERROR);
            }

            // 생성된 유저의 ID 추출 (Location 헤더에서 가져옴)
            String userId = response.getLocation().getPath().replaceAll(".*/([^/]+)$", "$1");
            log.info("Successfully created Keycloak user account.");
            return KeycloakId.of(userId);
        } catch (Exception e) {
            log.error("Exception occurred during Keycloak integration!");
            throw e;
        } finally {
            keycloak.close();
        }
    }

    // Keycloak 서버에서 특정 사용자 계정 삭제
    @Override
    public void deleteIdentity(KeycloakId keycloakId) {
        Keycloak keycloak = getKeycloakInstance();
        try {
            keycloak.realm(realm).users().get(keycloakId.value()).remove();
        } finally {
            keycloak.close();
        }
    }
}
