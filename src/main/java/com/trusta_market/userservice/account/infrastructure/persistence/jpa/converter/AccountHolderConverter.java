package com.trusta_market.userservice.account.infrastructure.persistence.jpa.converter;

import com.trusta_market.userservice.account.domain.vo.AccountHolder;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class AccountHolderConverter implements AttributeConverter<AccountHolder, String> {
    @Override
    public String convertToDatabaseColumn(AccountHolder attribute) {
        return attribute != null ? attribute.value() : null;
    }

    @Override
    public AccountHolder convertToEntityAttribute(String dbData) {
        return dbData != null ? AccountHolder.of(dbData) : null;
    }
}
