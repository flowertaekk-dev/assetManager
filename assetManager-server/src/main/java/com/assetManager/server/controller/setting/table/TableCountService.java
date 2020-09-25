package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.*;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.tableCount.TableCount;
import com.assetManager.server.domain.tableCount.TableCountRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class TableCountService {
    private static Logger logger = LoggerFactory.getLogger(TableCountService.class);

    private final TableCountRepository tableCountRepository;
    private final BusinessRepository businessRepository;

    protected AddTableCountResponseDto addTableCount(AddTableCountRequestDto request) {

        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        if (business.isEmpty()){
            logger.error(String.format(
                    "TableCountService > addTableCount -> userId : %s, businessId: %s : Business not found",
                    request.getUserId(), request.getBusinessId()));
            return AddTableCountResponseDto.makeFailureResponse("상호명(닉네임)이 존재하지 않습니다.");
        }


        Optional<TableCount> tableInfo = tableCountRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());
        if (tableInfo.isPresent()) {
            logger.error(String.format(
                    "TableCountService > addTableCount -> userId : %s, businessId: %s : TableInfo does already exists",
                    request.getUserId(), request.getBusinessId()));
            return AddTableCountResponseDto.makeFailureResponse("테이블 정보가 이미 존재합니다.");
        }

        return AddTableCountResponseDto.makeSuccessResponse(tableCountRepository.save(request.toTableCountEntity()));
    }

    protected UpdateTableCountResponseDto updateTableCount(UpdateTableCountRequestDto request) {
        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        if (business.isEmpty()) {
            logger.error(String.format(
                    "TableCountService > updateTableCount -> userId : %s, businessId: %s : Business not found",
                    request.getUserId(), request.getBusinessId()));
            return UpdateTableCountResponseDto.makeFailureResponse("상호명(닉네임)이 존재하지 않습니다.");
        }

        Optional<TableCount> tableCount = tableCountRepository
                .findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        if (tableCount.isEmpty()) {
            logger.error(String.format(
                    "TableCountService > updateTableCount -> userId : %s, businessId: %s : TableInfo not found",
                    request.getUserId(), request.getBusinessId()));
            return UpdateTableCountResponseDto.makeFailureResponse("테이블 정보가 존재하지 않습니다.");
        }

        // 수정
        TableCount updatedTableCount = tableCount.get().updateTableCount(request.getTableCount());

        businessRepository.flush();
        return UpdateTableCountResponseDto.makeSuccessResponse(updatedTableCount);
    }

    protected ReadTableCountResponseDto readTableCount(ReadTableCountRequestDto request) {
        // 불러온다
        Optional<TableCount> tableCount = tableCountRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        // 아직 미등록이면 tableCount '0'로 반환
        if (tableCount.isEmpty()) {
            return ReadTableCountResponseDto.makeSuccessResponse(
                    TableCount.builder()
                            .userId(request.getUserId())
                            .businessId(request.getBusinessId())
                            .build());
        }

        return ReadTableCountResponseDto.makeSuccessResponse(tableCount.get());
    }
}
