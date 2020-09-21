package com.assetManager.server.controller.setting.menu;

import com.assetManager.server.controller.setting.menu.dto.AddMenuRequestDto;
import com.assetManager.server.controller.setting.menu.dto.CommonMenuResponseDto;
import com.assetManager.server.controller.setting.menu.dto.UpdateMenuRequestDto;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Transactional
@RequiredArgsConstructor
@Service
public class MenuService {

    private final MenuRepository menuRepository;

    protected CommonMenuResponseDto addMenu(AddMenuRequestDto request) {

        // 이미 등록되어있는지 확인
        Optional<Menu> menu = menuRepository
                .findByUserIdAndBusinessNameAndMenu(request.getUserId(), request.getBusinessName(), request.getMenu());

        if (menu.isPresent()) {
            return CommonMenuResponseDto.makeFailureResponse("이미 존재하는 메뉴입니다.");
        }

        menuRepository.save(request.toMenuEntity());
        return CommonMenuResponseDto.makeSuccessResponse();
    }

    protected CommonMenuResponseDto updateMenu(UpdateMenuRequestDto request) {

        // 기존 데이터 확인
        Optional<Menu> menu = menuRepository
                .findByUserIdAndBusinessNameAndMenu(request.getUserId(), request.getBusinessName(), request.getMenu());

        // 기존 데이터가 없으면 에러
        if (menu.isEmpty()) {
            return CommonMenuResponseDto.makeFailureResponse("변경할 대상 데이터가 존재하지 않습니다.");
        }

        // 업데이트
        menu.get().updateMenu(request.toUpdatedMenuEntity());

        menuRepository.flush();
        return CommonMenuResponseDto.makeSuccessResponse();
    }
}
