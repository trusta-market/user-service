package com.trusta_market.userservice.user.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.user.domain.vo.AddressInfo;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AddressInfoConverter implements AttributeConverter<AddressInfo, String> {

    @Override
    public String convertToDatabaseColumn(AddressInfo attribute) {
        return attribute == null ? null : attribute.value();
    }

    @Override
    public AddressInfo convertToEntityAttribute(String dbData) {
        return dbData == null ? null : AddressInfo.of(dbData);
    }
}
