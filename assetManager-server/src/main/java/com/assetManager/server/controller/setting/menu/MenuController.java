package com.assetManager.server.controller.setting.menu;

import com.assetManager.server.controller.setting.menu.dto.*;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/menu")
public class MenuController {
    private static Logger logger = LoggerFactory.getLogger(MenuController.class);

    private final MenuService menuService;

    @PostMapping("/add")
    public ResponseEntity<CommonMenuResponseDto> addMenu(@RequestBody AddMenuRequestDto request) {
        logger.info(String.format(
                "addMenu -> userId: %s, businessId: %s, menu: %s, price: %d",
                request.getUserId(), request.getBusinessId(), request.getMenu(), request.getPrice()));
        return ResponseEntity.ok(menuService.addMenu(request));
    }

    @PostMapping("/update")
    public ResponseEntity<CommonMenuResponseDto> updateMenu(@RequestBody UpdateMenuRequestDto request) {
        logger.info(String.format(
                "updateMenu -> userId: %s, businessId: %s, menu: %s, price: %d",
                request.getUserId(), request.getBusinessId(), request.getExistingMenu(), request.getPrice()));
        return ResponseEntity.ok(menuService.updateMenu(request));
    }

    @PostMapping("/delete")
    public ResponseEntity<CommonMenuResponseDto> deleteMenu(@RequestBody DeleteMenuRequestDto request) {
        logger.info(String.format(
                "deleteMenu -> userId: %s, businessId: %s, menu: %s",
                request.getUserId(), request.getBusinessId(), request.getMenu()));
        return ResponseEntity.ok(menuService.deleteMenu(request));
    }

    @PostMapping("/readAll")
    public ResponseEntity<ReadAllMenuResponseDto> readAllMenu(@RequestBody ReadAllMenuRequestDto request) {
        logger.info(String.format(
                "readAllMenu -> userId: %s, businessId: %s",
                request.getUserId(), request.getBusinessId()));
        return ResponseEntity.ok(menuService.readAllMenu(request));
    }

}
