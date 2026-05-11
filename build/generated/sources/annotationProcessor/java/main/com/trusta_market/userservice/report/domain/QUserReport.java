package com.trusta_market.userservice.report.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserReport is a Querydsl query type for UserReport
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserReport extends EntityPathBase<UserReport> {

    private static final long serialVersionUID = -715459364L;

    public static final QUserReport userReport = new QUserReport("userReport");

    public final com.trustamarket.common.domain.QBaseCreatedEntity _super = new com.trustamarket.common.domain.QBaseCreatedEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final SimplePath<com.trusta_market.userservice.report.domain.vo.ReportReason> reason = createSimple("reason", com.trusta_market.userservice.report.domain.vo.ReportReason.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.UserId> reportedUserId = createSimple("reportedUserId", com.trusta_market.userservice.user.domain.vo.UserId.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.UserId> reporterUserId = createSimple("reporterUserId", com.trusta_market.userservice.user.domain.vo.UserId.class);

    public final ComparablePath<java.util.UUID> reportId = createComparable("reportId", java.util.UUID.class);

    public final DateTimePath<java.time.LocalDateTime> reviewedAt = createDateTime("reviewedAt", java.time.LocalDateTime.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.UserId> reviewedBy = createSimple("reviewedBy", com.trusta_market.userservice.user.domain.vo.UserId.class);

    public final EnumPath<com.trusta_market.userservice.report.domain.vo.ReportStatus> status = createEnum("status", com.trusta_market.userservice.report.domain.vo.ReportStatus.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QUserReport(String variable) {
        super(UserReport.class, forVariable(variable));
    }

    public QUserReport(Path<? extends UserReport> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserReport(PathMetadata metadata) {
        super(UserReport.class, metadata);
    }

}

