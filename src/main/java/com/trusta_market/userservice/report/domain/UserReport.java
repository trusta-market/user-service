package com.trusta_market.userservice.report.domain;

import com.trusta_market.userservice.report.domain.vo.ReportReason;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trustamarket.common.domain.BaseCreatedEntity;
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
@Table(name = "p_reports")
public class UserReport extends BaseCreatedEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    @Column(nullable = false, updatable = false)
    private UUID reportId;

    @Column(name = "reporter_user_id", nullable = false)
    private UserId reporterUserId;

    @Column(name = "reported_user_id", nullable = false)
    private UserId reportedUserId;

    @Column(nullable = false, length = 200)
    private ReportReason reason;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status;

    private LocalDateTime reviewedAt;

    private UUID reviewedBy;

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public UserReport(UUID reportId, UserId reporterUserId, UserId reportedUserId, ReportReason reason, ReportStatus status, LocalDateTime reviewedAt, UUID reviewedBy, Integer version) {
        this.reportId = reportId;
        this.reporterUserId = reporterUserId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.status = status != null ? status : ReportStatus.PENDING;
        this.reviewedAt = reviewedAt;
        this.reviewedBy = reviewedBy;
        this.version = version;
    }

    public static UserReport create(UserId reporterUserId, UserId reportedUserId, ReportReason reason) {
        return UserReport.builder()
                .reporterUserId(reporterUserId)
                .reportedUserId(reportedUserId)
                .reason(reason)
                .status(ReportStatus.PENDING)
                .build();
    }

    public void review(UUID reviewedBy) {
        this.status = ReportStatus.REVIEWED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = LocalDateTime.now();
    }

    public void dismiss(UUID reviewedBy) {
        this.status = ReportStatus.DISMISSED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = LocalDateTime.now();
    }
}
