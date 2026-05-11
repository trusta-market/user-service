package com.trusta_market.userservice.account.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.account.domain.vo.BankCode;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class BankCodeConverter implements AttributeConverter<BankCode, String> {
    @Override
    public String convertToDatabaseColumn(BankCode attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public BankCode convertToEntityAttribute(String dbData) {
        return dbData != null ? BankCode.of(dbData) : null;
    }
}
