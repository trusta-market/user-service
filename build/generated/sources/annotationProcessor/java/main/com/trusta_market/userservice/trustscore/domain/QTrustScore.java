package com.trusta_market.userservice.trustscore.domain;

import static com.querydsl.core.types.PathMetadataFactory.*;

import com.querydsl.core.types.dsl.*;

import com.querydsl.core.types.PathMetadata;
import javax.annotation.processing.Generated;
import com.querydsl.core.types.Path;


/**
 * QTrustScore is a Querydsl query type for TrustScore
 */
@Generated("com.querydsl.codegen.DefaultEntitySerializer")
public class QTrustScore extends EntityPathBase<TrustScore> {

    private static final long serialVersionUID = -536092387L;

    public static final QTrustScore trustScore = new QTrustScore("trustScore");

    public final com.trustamarket.common.domain.QBaseTimeEntity _super = new com.trustamarket.common.domain.QBaseTimeEntity(this);

    //inherited
    public final DateTimePath<java.time.Instant> createdAt = _super.createdAt;

    public final SimplePath<com.trusta_market.userservice.trustscore.domain.vo.Score> score = createSimple("score", com.trusta_market.userservice.trustscore.domain.vo.Score.class);

    public final ComparablePath<java.util.UUID> trustScoreId = createComparable("trustScoreId", java.util.UUID.class);

    //inherited
    public final DateTimePath<java.time.Instant> updatedAt = _super.updatedAt;

    public final SimplePath<com.trusta_market.userservice.user.domain.vo.UserId> userId = createSimple("userId", com.trusta_market.userservice.user.domain.vo.UserId.class);

    public final NumberPath<Integer> version = createNumber("version", Integer.class);

    public QTrustScore(String variable) {
        super(TrustScore.class, forVariable(variable));
    }

    public QTrustScore(Path<? extends TrustScore> path) {
        super(path.getType(), path.getMetadata());
    }

    public QTrustScore(PathMetadata metadata) {
        super(TrustScore.class, metadata);
    }

}

