package com.assetManager.server.controller.updateUser.dto;

import com.assetManager.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class UpdateUserRequestDto {

    private String id;
    private String email;
    private String updatingPassword;

    @Builder
    public UpdateUserRequestDto(String id, String email, String updatingPassword) {
        this.id = id;
        this.email = email;
        this.updatingPassword = updatingPassword;
    }

    public User toUserEntity() {
        return User.builder()
                .id(id)
                .password(updatingPassword)
                .email(email)
                .build();
    }
}
