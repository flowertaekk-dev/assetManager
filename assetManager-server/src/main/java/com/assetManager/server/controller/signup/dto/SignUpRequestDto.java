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
    private String salt;
    private String password;
    private String email;
    private String emailAuthCode;

    @Builder
    public SignUpRequestDto(String id, String salt, String password, String email, String emailAuthCode) {
        this.id = id;
        this.salt = salt;
        this.password = password;
        this.email = email;
        this.emailAuthCode = emailAuthCode;
    }

    public User toUserEntity() {
        return User.builder()
                .id(this.id)
                .salt(this.salt)
                .password(this.password)
                .email(this.email)
                .status(User.UserStatus.USING)
                .build();
    }
}
