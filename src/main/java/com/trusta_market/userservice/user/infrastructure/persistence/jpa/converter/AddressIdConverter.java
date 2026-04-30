package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.AddressId;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

import java.util.UUID;

@Converter(autoApply = true)
public class AddressIdConverter implements AttributeConverter<AddressId, UUID> {
    @Override
    public UUID convertToDatabaseColumn(AddressId attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public AddressId convertToEntityAttribute(UUID dbData) {
        return dbData != null ? AddressId.of(dbData) : null;
    }
}
