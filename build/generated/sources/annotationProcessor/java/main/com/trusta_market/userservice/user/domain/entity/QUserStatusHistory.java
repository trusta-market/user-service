package com.trusta_market.userservice.user.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserStatusHistory is a Querydsl query type for UserStatusHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserStatusHistory extends EntityPathBase<UserStatusHistory> {

    private static final long serialVersionUID = 109915072L;

    public static final QUserStatusHistory userStatusHistory = new QUserStatusHistory("userStatusHistory");

    public final com.trustamarket.common.domain.QBaseCreatedEntity _super = new com.trustamarket.common.domain.QBaseCreatedEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.UserStatus> nextStatus = createEnum("nextStatus", com.trusta_market.userservice.user.domain.vo.UserStatus.class);

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.UserStatus> prevStatus = createEnum("prevStatus", com.trusta_market.userservice.user.domain.vo.UserStatus.class);

    public final StringPath reason = createString("reason");

    public final ComparablePath<java.util.UUID> statusHistoryId = createComparable("statusHistoryId", java.util.UUID.class);

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QUserStatusHistory(String variable) {
        super(UserStatusHistory.class, forVariable(variable));
    }

    public QUserStatusHistory(Path<? extends UserStatusHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserStatusHistory(PathMetadata metadata) {
        super(UserStatusHistory.class, metadata);
    }

}

