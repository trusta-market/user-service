package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.AddressDetail;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AddressDetailConverter implements AttributeConverter<AddressDetail, String> {

    @Override
    public String convertToDatabaseColumn(AddressDetail attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public AddressDetail convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AddressDetail.of(dbData);
    }
}
