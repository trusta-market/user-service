package com.trusta_market.userservice.user.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserMembershipHistory is a Querydsl query type for UserMembershipHistory
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserMembershipHistory extends EntityPathBase<UserMembershipHistory> {

    private static final long serialVersionUID = 792435804L;

    public static final QUserMembershipHistory userMembershipHistory = new QUserMembershipHistory("userMembershipHistory");

    public final com.trustamarket.common.domain.QBaseCreatedEntity _super = new com.trustamarket.common.domain.QBaseCreatedEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final ComparablePath<java.util.UUID> membershipHistoryId = createComparable("membershipHistoryId", java.util.UUID.class);

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.Membership> nextMembership = createEnum("nextMembership", com.trusta_market.userservice.user.domain.vo.Membership.class);

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.Membership> prevMembership = createEnum("prevMembership", com.trusta_market.userservice.user.domain.vo.Membership.class);

    public final StringPath reason = createString("reason");

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public QUserMembershipHistory(String variable) {
        super(UserMembershipHistory.class, forVariable(variable));
    }

    public QUserMembershipHistory(Path<? extends UserMembershipHistory> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserMembershipHistory(PathMetadata metadata) {
        super(UserMembershipHistory.class, metadata);
    }

}

