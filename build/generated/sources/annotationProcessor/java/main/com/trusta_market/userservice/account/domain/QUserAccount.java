package com.trusta_market.userservice.account.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QUserAccount is a Querydsl query type for UserAccount
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QUserAccount extends EntityPathBase<UserAccount> {

    private static final long serialVersionUID = 132730470L;

    public static final QUserAccount userAccount = new QUserAccount("userAccount");

    public final com.trustamarket.common.domain.QBaseUserEntity _super = new com.trustamarket.common.domain.QBaseUserEntity(this);

    public final SimplePath<com.trusta_market.userservice.account.domain.vo.AccountHolder> accountHolder = createSimple("accountHolder", com.trusta_market.userservice.account.domain.vo.AccountHolder.class);

    public final ComparablePath<java.util.UUID> accountId = createComparable("accountId", java.util.UUID.class);

    public final SimplePath<com.trusta_market.userservice.account.domain.vo.AccountNumber> accountNumber = createSimple("accountNumber", com.trusta_market.userservice.account.domain.vo.AccountNumber.class);

    public final EnumPath<com.trusta_market.userservice.account.domain.vo.AccountType> accountType = createEnum("accountType", com.trusta_market.userservice.account.domain.vo.AccountType.class);

    public final DateTimePath<java.time.LocalDateTime> approvedAt = createDateTime("approvedAt", java.time.LocalDateTime.class);

    public final ComparablePath<java.util.UUID> approvedBy = createComparable("approvedBy", java.util.UUID.class);

    public final SimplePath<com.trusta_market.userservice.account.domain.vo.BankCode> bankCode = createSimple("bankCode", com.trusta_market.userservice.account.domain.vo.BankCode.class);

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

    public final BooleanPath isVerified = createBoolean("isVerified");

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    //inherited
    public final ComparablePath<java.util.UUID> updatedBy = _super.updatedBy;

    public final ComparablePath<java.util.UUID> userId = createComparable("userId", java.util.UUID.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QUserAccount(String variable) {
        super(UserAccount.class, forVariable(variable));
    }

    public QUserAccount(Path<? extends UserAccount> path) {
        super(path.getType(), path.getMetadata());
    }

    public QUserAccount(PathMetadata metadata) {
        super(UserAccount.class, metadata);
    }

}

