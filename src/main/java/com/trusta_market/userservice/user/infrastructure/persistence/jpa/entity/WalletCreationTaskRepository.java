package com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity;

import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface WalletCreationTaskRepository extends JpaRepository<WalletCreationTask, Long> {
    
    // 재시도가 필요한 작업들 조회
    List<WalletCreationTask> findAllByStatus(WalletCreationTask.TaskStatus status);
    
    // 특정 유저의 작업이 이미 존재하는지 확인
    Optional<WalletCreationTask> findByUserId(UUID userId);
}
