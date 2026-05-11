package com.trusta_market.userservice.suspension.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserSuspension is a Querydsl query type for UserSuspension
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserSuspension extends EntityPathBase<UserSuspension> {

    private static final long serialVersionUID = -2098912530L;

    public static final QUserSuspension userSuspension = new QUserSuspension("userSuspension");

    public final com.trustamarket.common.domain.QBaseCreatedEntity _super = new com.trustamarket.common.domain.QBaseCreatedEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final DateTimePath<java.time.LocalDateTime> expiresAt = createDateTime("expiresAt", java.time.LocalDateTime.class);

    public final StringPath reason = createString("reason");

    public final DateTimePath<java.time.LocalDateTime> releasedAt = createDateTime("releasedAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> releasedBy = createComparable("releasedBy", java.util.UUID.class);

    public final StringPath releasedReason = createString("releasedReason");

    public final ComparablePath<java.util.UUID> suspensionId = createComparable("suspensionId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QUserSuspension(String variable) {
        super(UserSuspension.class, forVariable(variable));
    }

    public QUserSuspension(Path<? extends UserSuspension> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserSuspension(PathMetadata metadata) {
        super(UserSuspension.class, metadata);
    }

}

