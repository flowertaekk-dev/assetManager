package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.*;
import com.assetManager.server.domain.business.Business;
import com.assetManager.server.domain.business.BusinessRepository;
import com.assetManager.server.domain.tableInfo.TableInfo;
import com.assetManager.server.domain.tableInfo.TableInfoRepository;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@RequiredArgsConstructor
@Transactional
@Service
public class TableInfoService {
    private static Logger logger = LoggerFactory.getLogger(TableInfoService.class);

    private final TableInfoRepository tableInfoRepository;
    private final BusinessRepository businessRepository;

    protected AddTableInfoResponseDto addTableInfo(AddTableInfoRequestDto request) {

        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        if (business.isEmpty()){
            logger.error(String.format(
                    "TableCountService > addTableCount -> userId : %s, businessId: %s : Business not found",
                    request.getUserId(), request.getBusinessId()));
            return AddTableInfoResponseDto.makeFailureResponse("Business not found.");
        }


        Optional<TableInfo> tableInfo = tableInfoRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());
        if (tableInfo.isPresent()) {
            logger.error(String.format(
                    "TableCountService > addTableCount -> userId : %s, businessId: %s : TableInfo does already exists",
                    request.getUserId(), request.getBusinessId()));
            return AddTableInfoResponseDto.makeFailureResponse("Table info has already been registered.");
        }

        return AddTableInfoResponseDto.makeSuccessResponse(tableInfoRepository.save(request.toTableInfoEntity()));
    }

    protected UpdateTableCountResponseDto updateTableCount(UpdateTableCountRequestDto request) {
        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        if (business.isEmpty()) {
            logger.error(String.format(
                    "TableCountService > updateTableCount -> userId : %s, businessId: %s : Business not found",
                    request.getUserId(), request.getBusinessId()));
            return UpdateTableCountResponseDto.makeFailureResponse("Business not found.");
        }

        Optional<TableInfo> tableCount = tableInfoRepository
                .findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        if (tableCount.isEmpty()) {
            logger.error(String.format(
                    "TableCountService > updateTableCount -> userId : %s, businessId: %s : TableInfo not found",
                    request.getUserId(), request.getBusinessId()));
            return UpdateTableCountResponseDto.makeFailureResponse("Table information not found.");
        }

        // 수정
        TableInfo updatedTableInfo = tableCount.get().updateTableCount(request.getTableCount());

        businessRepository.flush();
        return UpdateTableCountResponseDto.makeSuccessResponse(updatedTableInfo);
    }

    protected ReadTableCountResponseDto readTableCount(ReadTableCountRequestDto request) {
        // 불러온다
        Optional<TableInfo> tableCount = tableInfoRepository.findByUserIdAndBusinessId(request.getUserId(), request.getBusinessId());

        // 아직 미등록이면 tableCount '0'로 반환
        if (tableCount.isEmpty()) {
            return ReadTableCountResponseDto.makeSuccessResponse(
                    TableInfo.builder()
                            .userId(request.getUserId())
                            .businessId(request.getBusinessId())
                            .build());
        }

        return ReadTableCountResponseDto.makeSuccessResponse(tableCount.get());
    }

}
