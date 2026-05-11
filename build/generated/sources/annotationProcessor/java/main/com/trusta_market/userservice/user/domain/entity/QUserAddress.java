package com.trusta_market.userservice.user.domain.entity;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAddress is a Querydsl query type for UserAddress
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAddress extends EntityPathBase<UserAddress> {

    private static final long serialVersionUID = -1265072654L;

    public static final QUserAddress userAddress = new QUserAddress("userAddress");

    public final com.trustamarket.common.domain.QBaseUserEntity _super = new com.trustamarket.common.domain.QBaseUserEntity(this);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.AddressInfo> address = createSimple("address", com.trusta_market.userservice.user.domain.vo.AddressInfo.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.AddressDetail> addressDetail = createSimple("addressDetail", com.trusta_market.userservice.user.domain.vo.AddressDetail.class);

    public final ComparablePath<java.util.UUID> addressId = createComparable("addressId", java.util.UUID.class);

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

    public final BooleanPath isDefault = createBoolean("isDefault");

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.Name> recipientName = createSimple("recipientName", com.trusta_market.userservice.user.domain.vo.Name.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.PhoneNumber> recipientPhone = createSimple("recipientPhone", com.trusta_market.userservice.user.domain.vo.PhoneNumber.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.UserId> userId = createSimple("userId", com.trusta_market.userservice.user.domain.vo.UserId.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.ZipCode> zipCode = createSimple("zipCode", com.trusta_market.userservice.user.domain.vo.ZipCode.class);

    public QUserAddress(String variable) {
        super(UserAddress.class, forVariable(variable));
    }

    public QUserAddress(Path<? extends UserAddress> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAddress(PathMetadata metadata) {
        super(UserAddress.class, metadata);
    }

}

