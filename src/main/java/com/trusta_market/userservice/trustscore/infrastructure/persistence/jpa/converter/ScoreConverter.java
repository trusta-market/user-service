package com.trusta_market.userservice.trustscore.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.trustscore.domain.vo.Score;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

// Score VO ↔ Long DB 컬럼 자동 변환 컨버터
@Converter(autoApply = true)
public class ScoreConverter implements AttributeConverter<Score, Long> {

    @Override
    public Long convertToDatabaseColumn(Score attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public Score convertToEntityAttribute(Long dbData) {
        return dbData != null ? Score.of(dbData) : null;
    }
}
