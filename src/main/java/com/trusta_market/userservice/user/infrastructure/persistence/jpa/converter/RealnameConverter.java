package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.Realname;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class RealnameConverter implements AttributeConverter<Realname, String> {
    @Override
    public String convertToDatabaseColumn(Realname attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public Realname convertToEntityAttribute(String dbData) {
        return dbData != null ? Realname.of(dbData) : null;
    }
}
