package com.assetManager.server.controller.signup.dto;

import com.assetManager.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class SignUpRequestDto {

    private String id;
    private String password;
    private String email;

    @Builder
    public SignUpRequestDto(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public User toUserEntity() {
        return User.builder()
                .id(this.id)
                .password(this.password)
                .email(this.email)
                .status(User.UserStatus.APPLIED)
                .build();
    }
}
