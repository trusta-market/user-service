package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.ZipCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class ZipCodeConverter implements AttributeConverter<ZipCode, String> {

    @Override
    public String convertToDatabaseColumn(ZipCode attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public ZipCode convertToEntityAttribute(String dbData) {
        return dbData == null ? null : ZipCode.of(dbData);
    }
}
