package com.assetManager.server.controller.setting.menu.dto;

import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.utils.RandomIdCreator;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AddMenuRequestDto {

    private String userId;
    private String businessId;
    private String menu;
    private int price;

    @Builder
    public AddMenuRequestDto(String userId, String businessId, String menu, int price) {
        this.userId = userId;
        this.businessId = businessId;
        this.menu = menu;
        this.price = price;
    }

    public Menu toMenuEntity() {
        return Menu.builder()
                .menuId(RandomIdCreator.createMenuId())
                .userId(this.userId)
                .businessId(this.businessId)
                .menu(this.menu)
                .price(this.price)
                .build();
    }
}
