package com.assetManager.server.controller.setting.menu;

import com.assetManager.server.controller.setting.menu.dto.AddMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.CommonMenuResponseDto;
import com.assetManager.server.controller.setting.menu.dto.UpdateMenuRequestDto;
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
                "addMenu -> userId: %s, businessName: %s, menu: %s, price: %d",
                request.getUserId(), request.getBusinessName(), request.getMenu(), request.getPrice()));
        return ResponseEntity.ok(menuService.addMenu(request));
    }

    @PostMapping("/update")
    public ResponseEntity<CommonMenuResponseDto> updateMenu(@RequestBody UpdateMenuRequestDto request) {
        logger.info(String.format(
                "addMenu -> userId: %s, businessName: %s, menu: %s, price: %d",
                request.getUserId(), request.getBusinessName(), request.getMenu(), request.getPrice()));
        return ResponseEntity.ok(menuService.updateMenu(request));
    }

}
