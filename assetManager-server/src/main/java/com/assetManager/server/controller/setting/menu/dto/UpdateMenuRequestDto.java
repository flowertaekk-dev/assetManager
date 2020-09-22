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
    private String businessName;
    private String existingMenu;
    private String newMenu;
    private int price;

    @Builder
    public UpdateMenuRequestDto(String userId, String businessName, String existingMenu, String newMenu, int price) {
        this.userId = userId;
        this.businessName = businessName;
        this.existingMenu = existingMenu;
        this.newMenu = newMenu;
        this.price = price;
    }

    public Menu toUpdatedMenuEntity() {
        return Menu.builder()
                .userId(this.userId)
                .businessName(this.businessName)
                .menu(
                        Objects.isNull(this.newMenu)
                                ? this.existingMenu
                                : this.newMenu)
                .price(this.price)
                .build();
    }

}
