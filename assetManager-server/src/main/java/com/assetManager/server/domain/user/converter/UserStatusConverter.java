package com.assetManager.server.domain.user.converter;

import com.assetManager.server.domain.user.User;

import javax.persistence.AttributeConverter;
import javax.persistence.Converter;

@Converter
public class UserStatusConverter implements AttributeConverter<User.UserStatus, String> {
    @Override
    public String convertToDatabaseColumn(User.UserStatus attribute) {
        return attribute.getStatus();
    }

    @Override
    public User.UserStatus convertToEntityAttribute(String dbData) {
        return User.UserStatus.of(dbData);
    }
}
