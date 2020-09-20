package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.ReadTableCountRequestDto;
import com.assetManager.server.controller.setting.table.dto.ReadTableCountResponseDto;
import com.assetManager.server.controller.setting.table.dto.UpsertTableCountRequestDto;
import com.assetManager.server.controller.setting.table.dto.UpsertTableCountResponseDto;
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

    protected UpsertTableCountResponseDto upsertTableCount(UpsertTableCountRequestDto request) {

        // 상호명이 존재하는지 확인
        Optional<Business> business = businessRepository.findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (business.isEmpty())
            return UpsertTableCountResponseDto.makeFailureResponse("상호명(닉네임)이 존재하지 않습니다.");

        Optional<TableCount> tableCount = tableCountRepository
                .findByUserIdAndBusinessName(request.getUserId(), request.getBusinessName());

        if (tableCount.isPresent()) {
            tableCount.get().updateTableCount(request.getTableCount());
        } else {
            // spring-data의 upsert를 이용하려면 id가 같아야하는데 id는 이 시점에서 특정 불가
            tableCountRepository.save(request.toTableCountEntity());
        }

        // 실패하는 케이스는 없을까?
        return UpsertTableCountResponseDto.makeSuccessResponse();
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
