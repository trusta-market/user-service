package com.trusta_market.userservice.user.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUser is a Querydsl query type for User
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUser extends EntityPathBase<User> {

    private static final long serialVersionUID = 743879586L;

    public static final QUser user = new QUser("user");

    public final com.trustamarket.common.domain.QBaseUserEntity _super = new com.trustamarket.common.domain.QBaseUserEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    //inherited
    public final ComparablePath<java.util.UUID> createdBy = _super.createdBy;

    //inherited
    public final BooleanPath deleted = _super.deleted;

    //inherited
    public final DateTimePath<java.time.Instant> deletedAt = _super.deletedAt;

    //inherited
    public final ComparablePath<java.util.UUID> deletedBy = _super.deletedBy;

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.Email> email = createSimple("email", com.trusta_market.userservice.user.domain.vo.Email.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.KeycloakId> keycloakId = createSimple("keycloakId", com.trusta_market.userservice.user.domain.vo.KeycloakId.class);

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.Membership> membership = createEnum("membership", com.trusta_market.userservice.user.domain.vo.Membership.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.Name> name = createSimple("name", com.trusta_market.userservice.user.domain.vo.Name.class);

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.Role> role = createEnum("role", com.trusta_market.userservice.user.domain.vo.Role.class);

    public final StringPath slackId = createString("slackId");

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public final EnumPath<com.trusta_market.userservice.user.domain.vo.UserStatus> userStatus = createEnum("userStatus", com.trusta_market.userservice.user.domain.vo.UserStatus.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QUser(String variable) {
        super(User.class, forVariable(variable));
    }

    public QUser(Path<? extends User> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUser(PathMetadata metadata) {
        super(User.class, metadata);
    }

}

