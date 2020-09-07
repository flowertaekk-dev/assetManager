package com.assetManager.server.controller.email.dto;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Getter
@ToString
@NoArgsConstructor
public class EmailAuthRequestDto {

    private String addressTo;

    @Builder
    public EmailAuthRequestDto(String addressTo) {
        this.addressTo = addressTo;
    }
}
