package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.Nickname;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class NicknameConverter implements AttributeConverter<Nickname, String> {
    @Override
    public String convertToDatabaseColumn(Nickname attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public Nickname convertToEntityAttribute(String dbData) {
        return dbData != null ? Nickname.of(dbData) : null;
    }
}
