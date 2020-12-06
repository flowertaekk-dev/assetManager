package com.assetManager.server.controller.user.dto;

import com.assetManager.server.domain.user.User;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class DeleteUserRequestDto {

    private String id;
    private String email;

    @Builder
    public DeleteUserRequestDto(String id, String email) {
        this.id = id;
        this.email = email;
    }

    public User toUserEntity() {
        return User.builder()
                .id(this.id)
                .email(this.email)
                .build();
    }
}
