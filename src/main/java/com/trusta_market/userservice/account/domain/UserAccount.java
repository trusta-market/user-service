package com.trusta_market.userservice.account.domain;

import com.trusta_market.userservice.account.domain.vo.AccountType;
import com.trustamarket.common.domain.BaseUserEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Version;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.UuidGenerator;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_accounts")
public class UserAccount extends BaseUserEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID accountId;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false, length = 10)
    private String bankCode;

    @Column(nullable = false, length = 100)
    private String accountNumber;

    @Column(nullable = false, length = 50)
    private String accountHolder;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private AccountType accountType;

    @Column(nullable = false)
    private boolean isVerified;

    @Column(nullable = false)
    private boolean isDefault;

    private UUID approvedBy;

    private LocalDateTime approvedAt;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public UserAccount(UUID accountId, UUID userId, String bankCode, String accountNumber, String accountHolder, AccountType accountType, boolean isVerified, boolean isDefault, UUID approvedBy, LocalDateTime approvedAt, Integer version) {
        this.accountId = accountId;
        this.userId = userId;
        this.bankCode = bankCode;
        this.accountNumber = accountNumber;
        this.accountHolder = accountHolder;
        this.accountType = accountType;
        this.isVerified = isVerified;
        this.isDefault = isDefault;
        this.approvedBy = approvedBy;
        this.approvedAt = approvedAt;
        this.version = version;
    }

    public static UserAccount create(UUID userId, String bankCode, String accountNumber, String accountHolder, AccountType accountType, boolean isVerified, boolean isDefault) {
        return UserAccount.builder()
                .userId(userId)
                .bankCode(bankCode)
                .accountNumber(accountNumber)
                .accountHolder(accountHolder)
                .accountType(accountType)
                .isVerified(isVerified)
                .isDefault(isDefault)
                .build();
    }

    public void setDefault(boolean value) {
        this.isDefault = value;
    }

    public void approve(UUID approvedBy) {
        this.isVerified = true;
        this.approvedBy = approvedBy;
        this.approvedAt = LocalDateTime.now();
    }
}
