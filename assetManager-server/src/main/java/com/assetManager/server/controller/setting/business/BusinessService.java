package com.assetManager.server.controller.setting.business;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.setting.business.dto.*;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.menu.Menu;
import com.assetManager.server.domain.menu.MenuRepository;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class BusinessService {

    private final BusinessRepository businessRepository;
    private final TableInfoRepository tableInfoRepository;
    private final MenuRepository menuRepository;

    protected CommonBusinessResponseDto addNewBusinessName(AddBusinessRequestDto request) {

        // 이미 존재하면 등록 실패
        Optional<Business> searchResult = businessRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (searchResult.isPresent()) {
            return CommonBusinessResponseDto.makeFailureResponse("이미 등록된 상호명입니다.");
        }

        // 상호명 등록
        return CommonBusinessResponseDto.makeSuccessResponse(businessRepository.save(request.toBusinessEntity()));
    }

    protected CommonBusinessResponseDto updateBusinessName(UpdateBusinessRequestDto request) {

        // 해당 상호명 존재 확인
        Optional<Business> business = businessRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getExistingBusinessName());

        if (!business.isPresent()) {
            return CommonBusinessResponseDto.makeFailureResponse("존재하지 않는 상호명입니다.");
        }

        // 상호명 변경
        Business updatedBusiness = business.get().updateBusinessName(request.getNewBusinessName());
        businessRepository.flush();

        return CommonBusinessResponseDto.makeSuccessResponse(updatedBusiness);
    }

    protected CommonBusinessResponseDto deleteBusinessName(DeleteBusinessRequestDto request) {

        // 해당 상호명 존재 확인
        Optional<Business> business = businessRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (!business.isPresent()) {
            return CommonBusinessResponseDto.makeFailureResponse("존재하지 않는 상호명입니다.");
        }

        String businessId = business.get().getBusinessId();

        // 테이블 정보 삭제
        deleteTableInfo(request.getUserId(), businessId);

        // 메뉴 삭제 로직 구현
        deleteMenu(request.getUserId(), businessId);

        // 상호명 삭제
        businessRepository.delete(business.get());

        return CommonBusinessResponseDto.makeSuccessResponse(null);
    }

    protected ReadAllBusinessResponseDto readAll(ReadAllBusinessRequestDto request) {

        List<Business> businessNames = businessRepository.findByUserId(request.getUserId());

        return ReadAllBusinessResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .businessNames(businessNames)
                .build();
    }

    // ---------------------------------------------------------------
    // utils

    /**
     * 상호명을 삭제한다
     */
    private void deleteTableInfo(String userId, String businessId) {
        Optional<TableInfo> tableInfo = tableInfoRepository.findByUserIdAndBusinessId(userId, businessId);

        if (tableInfo.isEmpty()) {
            return; // 테이블 정보가 없다.
        }

        tableInfoRepository.delete(tableInfo.get());
    }

    /**
     * 상호명을 삭제한다
     */
    private void deleteMenu(String userId, String businessId) {
        List<Menu> menus = menuRepository.findByUserIdAndBusinessId(userId, businessId);

        if (Objects.isNull(menus) || menus.isEmpty()) {
            return; // 테이블 정보가 없다.
        }

        menuRepository.deleteAll(menus);
    }

}
