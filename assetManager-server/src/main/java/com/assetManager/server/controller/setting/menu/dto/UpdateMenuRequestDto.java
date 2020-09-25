package com.assetManager.server.controller.setting.menu.dto;

import com.assetManager.server.domain.menu.Menu;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Getter
@NoArgsConstructor
public class UpdateMenuRequestDto {

    private String userId;
    private String businessId;
    private String existingMenu;
    private String newMenu;
    private int price;

    @Builder
    public UpdateMenuRequestDto(String userId, String businessId, String existingMenu, String newMenu, int price) {
        this.userId = userId;
        this.businessId = businessId;
        this.existingMenu = existingMenu;
        this.newMenu = newMenu;
        this.price = price;
    }

    public Menu toUpdatedMenuEntity() {
        return Menu.builder()
                .userId(this.userId)
                .businessId(this.businessId)
                .menu(
                        Objects.isNull(this.newMenu)
                                ? this.existingMenu
                                : this.newMenu)
                .price(this.price)
                .build();
    }

}
