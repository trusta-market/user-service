package com.trusta_market.userservice.report.domain;

import com.trusta_market.userservice.report.domain.vo.ReportReason;
import com.trusta_market.userservice.report.domain.vo.ReportStatus;
import com.trusta_market.userservice.report.infrastructure.persistence.jpa.converter.ReportReasonConverter;
import com.trusta_market.userservice.user.domain.vo.UserId;
import com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter.UserIdConverter;
import com.trustamarket.common.domain.BaseCreatedEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Convert;
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

// 유저 신고 엔티티 (불량 유저 신고 및 관리자 검토 내역 관리)
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
    private UUID reportId; // 신고 식별자

    @Convert(converter = UserIdConverter.class)
    @Column(name = "reporter_user_id", nullable = false)
    private UserId reporterUserId; // 신고자 유저 ID

    @Convert(converter = UserIdConverter.class)
    @Column(name = "reported_user_id", nullable = false)
    private UserId reportedUserId; // 피신고자 유저 ID

    @Convert(converter = ReportReasonConverter.class)
    @Column(nullable = false, length = 200)
    private ReportReason reason; // 신고 사유

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private ReportStatus status; // 신고 처리 상태 (대기, 승인, 반려 등)

    private LocalDateTime reviewedAt; // 검토 일시

    @Convert(converter = UserIdConverter.class)
    private UserId reviewedBy; // 검토 관리자 ID

    @Version
    @Column(nullable = false)
    private Integer version;

    @Builder
    public UserReport(UUID reportId, UserId reporterUserId, UserId reportedUserId, ReportReason reason,
            ReportStatus status, LocalDateTime reviewedAt, UserId reviewedBy, Integer version) {
        this.reportId = reportId;
        this.reporterUserId = reporterUserId;
        this.reportedUserId = reportedUserId;
        this.reason = reason;
        this.status = status != null ? status : ReportStatus.PENDING;
        this.reviewedAt = reviewedAt;
        this.reviewedBy = reviewedBy;
        this.version = version;
    }

    // 신규 신고 객체 생성 팩토리 메서드
    public static UserReport create(UserId reporterUserId, UserId reportedUserId, ReportReason reason) {
        return UserReport.builder()
                .reporterUserId(reporterUserId)
                .reportedUserId(reportedUserId)
                .reason(reason)
                .status(ReportStatus.PENDING)
                .build();
    }

    // 신고 승인 처리 (신뢰 점수 차감 이벤트 발행 트리거)
    public void review(UserId reviewedBy, LocalDateTime reviewedAt) {
        validateReviewInput(reviewedBy, reviewedAt);
        if (this.status != ReportStatus.PENDING) {
            throw new ReportException(ReportErrorCode.ALREADY_PROCESSED);
        }
        this.status = ReportStatus.REVIEWED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;

        // 타 바운디드 컨텍스트(신뢰 점수 하락, 알림 발송 등)에 상태 전이를 알리기 위해 도메인 이벤트를 발행합니다.
        registerEvent(new ReportReviewedEvent(this.reportedUserId));
    }

    // 신고 기각 처리
    public void dismiss(UserId reviewedBy, LocalDateTime reviewedAt) {
        validateReviewInput(reviewedBy, reviewedAt);
        if (this.status != ReportStatus.PENDING) {
            throw new ReportException(ReportErrorCode.ALREADY_PROCESSED);
        }
        this.status = ReportStatus.DISMISSED;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;

        registerEvent(new ReportDismissedEvent(this.reportedUserId));
    }

    private void validateReviewInput(UserId reviewedBy, LocalDateTime reviewedAt) {
        if (reviewedBy == null || reviewedAt == null) {
            throw new ReportException(ReportErrorCode.INVALID_INPUT);
        }
    }
}

