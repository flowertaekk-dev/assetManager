package com.assetManager.server.controller.setting.menu;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.setting.menu.dto.*;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    protected CommonMenuResponseDto addMenu(AddMenuRequestDto request) {

        // 이미 등록되어있는지 확인
        Optional<Menu> menu = menuRepository
                .findByUserIdAndBusinessIdAndMenu(request.getUserId(), request.getBusinessId(), request.getMenu());

        if (menu.isPresent()) {
            return CommonMenuResponseDto.makeFailureResponse("This menu has already been registered.");
        }

        return CommonMenuResponseDto.makeSuccessResponse(menuRepository.save(request.toMenuEntity()));
    }

    /**
     * 메뉴명 및 가격을 변경
     */
    protected CommonMenuResponseDto updateMenu(UpdateMenuRequestDto request) {

        // 기존 데이터 확인
        Optional<Menu> menu = menuRepository
                .findByUserIdAndBusinessIdAndMenu(request.getUserId(), request.getBusinessId(), request.getExistingMenu());

        // 기존 데이터가 없으면 에러
        if (menu.isEmpty()) {
            return CommonMenuResponseDto.makeFailureResponse("Menu not found.");
        }

        Menu targetMenu = menu.get();

        // 업데이트
        targetMenu.updateMenu(request.toUpdatedMenuEntity());

        menuRepository.flush();
        return CommonMenuResponseDto.makeSuccessResponse(targetMenu);
    }

    /**
     * 메뉴 삭제
     */
    protected CommonMenuResponseDto deleteMenu(DeleteMenuRequestDto request) {

        // 타켓 데이터가 존재하는지 확인
        Optional<Menu> menu = menuRepository
                .findByUserIdAndBusinessIdAndMenu(request.getUserId(), request.getBusinessId(), request.getMenu());

        // 타겟 데이터가 없으면 실패
        if (menu.isEmpty()) {
            return CommonMenuResponseDto.makeFailureResponse("Menu not found.");
        }

        Menu targetMenu = menu.get();

        // 삭제
        menuRepository.delete(targetMenu);

        return CommonMenuResponseDto.makeSuccessResponse(targetMenu);
    }

    /**
     * userId와 상호명으로 관련된 메뉴 전부를 쿼리
     */
    protected ReadAllMenuResponseDto readAllMenu(ReadAllMenuRequestDto request) {

        // 데이터 쿼리
        List<Menu> menus = menuRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        // 성공
        return ReadAllMenuResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .menus(menus)
                .build();
    }
}
