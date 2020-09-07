package com.assetManager.server.domain.emailAuth.converter;

import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.user.User;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class EmailAuthStatusConverter implements AttributeConverter<EmailAuth.EmailAuthStatus, String> {
    @Override
    public String convertToDatabaseColumn(EmailAuth.EmailAuthStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public EmailAuth.EmailAuthStatus convertToEntityAttribute(String dbData) {
        return EmailAuth.EmailAuthStatus.of(dbData);
    }
}
