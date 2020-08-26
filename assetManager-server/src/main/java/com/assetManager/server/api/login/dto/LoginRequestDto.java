package com.assetManager.server.api.login.dto;

import com.assetManager.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class LoginRequestDto {

    private String id;
    private String password;
    private String email;

    @Builder
    public LoginRequestDto(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }

    public User toUserEntity() {
        return User.builder()
                .id(this.id)
                .password(this.password)
                .email(this.email)
                .build();
    }
}
