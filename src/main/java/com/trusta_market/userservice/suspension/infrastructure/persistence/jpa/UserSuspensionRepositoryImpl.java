package com.trusta_market.userservice.suspension.infrastructure.persistence.jpa;

import com.trusta_market.userservice.suspension.domain.UserSuspension;
import com.trusta_market.userservice.suspension.domain.repository.UserSuspensionRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class UserSuspensionRepositoryImpl implements UserSuspensionRepository {

    private final UserSuspensionJpaRepository userSuspensionJpaRepository;

    public UserSuspensionRepositoryImpl(UserSuspensionJpaRepository userSuspensionJpaRepository) {
        this.userSuspensionJpaRepository = userSuspensionJpaRepository;
    }

    @Override
    public UserSuspension save(UserSuspension suspension) {
        return userSuspensionJpaRepository.save(suspension);
    }

    @Override
    public Optional<UserSuspension> findById(UUID suspensionId) {
        return userSuspensionJpaRepository.findById(suspensionId);
    }

    @Override
    public List<UserSuspension> findAllByUserId(UUID userId) {
        return userSuspensionJpaRepository.findAllByUserId(userId);
    }
}
