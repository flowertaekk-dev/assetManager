package com.assetManager.server.controller.email.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class EmailRequestDto {

    private String addressTo;
    private String message;

    @Builder
    public EmailRequestDto(String addressTo, String message) {
        this.addressTo = addressTo;
        this.message = message;
    }
}
