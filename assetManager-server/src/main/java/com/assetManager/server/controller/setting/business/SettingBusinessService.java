package com.assetManager.server.controller.setting.business;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.setting.business.dto.AddBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.CommonBusinessResponseDto;
import com.assetManager.server.controller.setting.business.dto.DeleteBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.UpdateBusinessRequestDto;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class SettingBusinessService {

    private final BusinessRepository businessRepository;

    protected CommonBusinessResponseDto addNewBusinessName(AddBusinessRequestDto request) {

        // 이미 존재하면 등록 실패
        Optional<Business> searchResult = businessRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (searchResult.isPresent()) {
            return makeFailureResponse("이미 등록된 상호명입니다.");
        }

        // 상호명 등록
        businessRepository.save(
                Business.builder()
                        .userId(request.getUserId())
                        .businessName(request.getBusinessName())
                        .build());

        return makeSuccessResponse();
    }

    protected CommonBusinessResponseDto updateBusinessName(UpdateBusinessRequestDto request) {

        // 해당 상호명 존재 확인
        Optional<Business> business = businessRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getExistingBusinessName());

        if (!business.isPresent()) {
            return makeFailureResponse("존재하지 않는 상호명입니다.");
        }

        // 상호명 변경
        business.get().updateBusinessName(request.getNewBusinessName());
        businessRepository.flush(); // 이거 필요해? JPA 영속성하고 관련된 듯 아닌듯..

        return makeSuccessResponse();
    }

    protected CommonBusinessResponseDto deleteBusinessName(DeleteBusinessRequestDto request) {

        // 해당 상호명 존재 확인
        Optional<Business> business = businessRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (!business.isPresent()) {
            return makeFailureResponse("존재하지 않는 상호명입니다.");
        }

        // 상호명 삭제
        businessRepository.delete(business.get());

        return makeSuccessResponse();
    }

    // -------------------------------------------------------------------
    // utils

    private CommonBusinessResponseDto makeSuccessResponse() {
        return CommonBusinessResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    private CommonBusinessResponseDto makeFailureResponse(String reason) {
        return CommonBusinessResponseDto.builder()
                .resultStatus(CommonResponseResult.FAILURE)
                .reason(reason)
                .build();
    }
}
