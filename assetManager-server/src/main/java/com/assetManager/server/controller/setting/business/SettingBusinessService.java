package com.assetManager.server.controller.setting.business;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.setting.business.dto.SettingBusinessRequestDto;
import com.assetManager.server.controller.setting.business.dto.SettingBusinessResponseDto;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class SettingBusinessService {

    private final BusinessRepository businessRepository;

    @Transactional
    protected SettingBusinessResponseDto addNewBusinessName(SettingBusinessRequestDto request) {

        // 이미 존재하면 등록 실패
        Optional<Business> searchResult = businessRepository.findByBusinessName(request.getBusinessName());
        if (searchResult.isPresent()) {
            return SettingBusinessResponseDto.builder()
                    .resultStatus(CommonResponseResult.FAILURE)
                    .reason("이미 등록된 상호명입니다.")
                    .build();
        }

        // 상호명 등록
        businessRepository.save(
                Business.builder()
                        .userId(request.getUserId())
                        .businessName(request.getBusinessName())
                        .build());

        return SettingBusinessResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }
}
