package com.trusta_market.userservice.account.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.account.domain.vo.AccountNumber;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountNumberConverter implements AttributeConverter<AccountNumber, String> {
    @Override
    public String convertToDatabaseColumn(AccountNumber attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public AccountNumber convertToEntityAttribute(String dbData) {
        return dbData != null ? AccountNumber.of(dbData) : null;
    }
}
