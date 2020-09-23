package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.*;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.tableCount.TableCount;
import com.assetManager.server.domain.tableCount.TableCountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class TableCountService {

    private final TableCountRepository tableCountRepository;
    private final BusinessRepository businessRepository;

    protected AddTableCountResponseDto addTableCount(AddTableCountRequestDto request) {

        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (business.isEmpty())
            return AddTableCountResponseDto.makeFailureResponse("상호명(닉네임)이 존재하지 않습니다.");

        Optional<TableCount> tableInfo = tableCountRepository.findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());
        if (tableInfo.isPresent()) {
            return AddTableCountResponseDto.makeFailureResponse("테이블 정보가 이미 존재합니다.");
        }

        // 등록
        tableCountRepository.save(request.toTableCountEntity());

        return AddTableCountResponseDto.makeSuccessResponse();
    }

    protected UpdateTableCountResponseDto updateTableCount(UpdateTableCountRequestDto request) {

        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (business.isEmpty())
            return UpdateTableCountResponseDto.makeFailureResponse("상호명(닉네임)이 존재하지 않습니다.");

        Optional<TableCount> tableCount = tableCountRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (tableCount.isEmpty()) {
            return UpdateTableCountResponseDto.makeFailureResponse("테이블 정보가 존재하지 않습니다.");
        }

        // 수정
        tableCount.get().updateTableCount(request.getTableCount());

        businessRepository.flush();
        return UpdateTableCountResponseDto.makeSuccessResponse();
    }

    protected ReadTableCountResponseDto readTableCount(ReadTableCountRequestDto request) {
        // 불러온다
        Optional<TableCount> tableCount = tableCountRepository.findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        // 아직 미등록이면 tableCount '0'로 반환
        if (tableCount.isEmpty()) {
            return ReadTableCountResponseDto.makeSuccessResponse(
                    TableCount.builder()
                            .userId(request.getUserId())
                            .businessName(request.getBusinessName())
                            .build());
        }

        return ReadTableCountResponseDto.makeSuccessResponse(tableCount.get());
    }
}
