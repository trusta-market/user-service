package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.UserId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter(autoApply = true)
public class UserIdConverter implements AttributeConverter<UserId, UUID> {
    @Override
    public UUID convertToDatabaseColumn(UserId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public UserId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? UserId.of(dbData) : null;
    }
}
