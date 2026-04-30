package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.Email;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class EmailConverter implements AttributeConverter<Email, String> {
    @Override
    public String convertToDatabaseColumn(Email attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public Email convertToEntityAttribute(String dbData) {
        return dbData != null ? Email.of(dbData) : null;
    }
}
