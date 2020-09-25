package com.assetManager.server.domain.menu;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Getter
@NoArgsConstructor
@Entity
public class Menu {

    @Id
    private String menuId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String businessId;

    @Column(nullable = false)
    private String menu;

    @Column(nullable = false)
    private int price;

    @Builder
    public Menu(String menuId, String userId, String businessId, String menu, int price) {
        this.menuId = menuId;
        this.userId = userId;
        this.businessId = businessId;
        this.menu = menu;
        this.price = price;
    }

    // ---------------------------------------------------------
    // utils

    /**
     * menu, price를 갱신한다. <br />
     * 이외의 항목들은 갱신 불가
     */
    public void updateMenu(Menu menu) {
        this.menu = menu.menu;
        this.price = menu.price;
    }
}
