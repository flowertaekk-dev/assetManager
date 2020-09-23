package com.assetManager.server.controller.setting.menu.dto;

import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddMenuRequestDto {
    private final String identityCode = "MN-";

    private String userId;
    private String businessName;
    private String menu;
    private int price;

    @Builder
    public AddMenuRequestDto(String userId, String businessName, String menu, int price) {
        this.userId = userId;
        this.businessName = businessName;
        this.menu = menu;
        this.price = price;
    }

    public Menu toMenuEntity() {
        return Menu.builder()
                .menuId(identityCode + RandomIdCreator.create())
                .userId(this.userId)
                .businessName(this.businessName)
                .menu(this.menu)
                .price(this.price)
                .build();
    }
}
