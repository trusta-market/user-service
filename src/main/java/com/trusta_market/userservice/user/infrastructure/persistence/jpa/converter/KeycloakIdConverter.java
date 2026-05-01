package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.KeycloakId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class KeycloakIdConverter implements AttributeConverter<KeycloakId, String> {
    @Override
    public String convertToDatabaseColumn(KeycloakId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public KeycloakId convertToEntityAttribute(String dbData) {
        return dbData != null ? KeycloakId.of(dbData) : null;
    }
}
