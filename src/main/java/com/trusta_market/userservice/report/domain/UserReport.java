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
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import org.springframework.data.domain.DomainEvents;
import org.springframework.data.domain.AfterDomainEventPublication;
import jakarta.persistence.Transient;
import com.trusta_market.userservice.report.domain.event.ReportReviewedEvent;
import com.trusta_market.userservice.report.domain.event.ReportDismissedEvent;
import com.trusta_market.userservice.report.domain.exception.ReportException;
import com.trusta_market.userservice.report.domain.exception.ReportErrorCode;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "p_reports")
public class UserReport extends BaseCreatedEntity {

    @Transient
    private List<Object> domainEvents = new ArrayList<>();

    @DomainEvents
    public Collection<Object> domainEvents() {
        return Collections.unmodifiableList(domainEvents);
    }

    @AfterDomainEventPublication
    public void clearEvents() {
        domainEvents.clear();
    }

    protected void registerEvent(Object event) {
        this.domainEvents.add(event);
    }

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
    public UserReport(UUID reportId, UserId reporterUserId, UserId reportedUserId, ReportReason reason,
            ReportStatus status, LocalDateTime reviewedAt, UUID reviewedBy, Integer version) {
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
        if (this.status != ReportStatus.PENDING) {
            throw new ReportException(ReportErrorCode.ALREADY_PROCESSED);
        }
        this.status = ReportStatus.REVIEWED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = LocalDateTime.now();

        // 타 바운디드 컨텍스트(신뢰 점수 하락, 알림 발송 등)에 상태 전이를 알리기 위해 도메인 이벤트를 발행합니다.
        registerEvent(new ReportReviewedEvent(this.reportedUserId.value()));
    }

    public void dismiss(UUID reviewedBy) {
        if (this.status != ReportStatus.PENDING) {
            throw new ReportException(ReportErrorCode.ALREADY_PROCESSED);
        }
        this.status = ReportStatus.DISMISSED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = LocalDateTime.now();

        registerEvent(new ReportDismissedEvent(this.reportedUserId.value()));
    }
}
