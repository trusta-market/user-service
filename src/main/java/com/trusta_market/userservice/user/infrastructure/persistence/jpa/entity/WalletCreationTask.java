package com.trusta_market.userservice.user.infrastructure.persistence.jpa.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "wallet_creation_tasks")
@Getter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
public class WalletCreationTask {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private UUID userId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TaskStatus status;

    @Column
    private String lastErrorMessage;

    @Column
    private int retryCount;

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;

    public WalletCreationTask(UUID userId) {
        this.userId = userId;
        this.status = TaskStatus.PENDING;
        this.retryCount = 0;
    }

    public void complete() {
        this.status = TaskStatus.COMPLETED;
    }

    public void fail(String errorMessage) {
        this.status = TaskStatus.FAILED;
        this.lastErrorMessage = errorMessage;
        this.retryCount++;
    }

    public void retry() {
        this.status = TaskStatus.PENDING;
        this.retryCount++;
    }

    public enum TaskStatus {
        PENDING, COMPLETED, FAILED
    }
}
