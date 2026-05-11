package com.trusta_market.userservice.trustscore.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.trustscore.domain.vo.Score;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ScoreConverter implements AttributeConverter<Score, Long> {

    @Override
    public Long convertToDatabaseColumn(Score attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public Score convertToEntityAttribute(Long dbData) {
        return dbData == null ? null : Score.of(dbData);
    }
}
