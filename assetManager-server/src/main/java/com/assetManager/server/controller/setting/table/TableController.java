package com.assetManager.server.controller.setting.table;

import com.assetManager.server.controller.setting.table.dto.*;
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
@RequestMapping("/api/v1/table")
public class TableController {
    private static Logger logger = LoggerFactory.getLogger(TableController.class);

    private final TableCountService tableCountService;

    @PostMapping("/add")
    public ResponseEntity<AddTableCountResponseDto> addTableCount(@RequestBody AddTableCountRequestDto request) {
        logger.info(String.format(
                "addTableCount -> userId: %s, businessName: %s, tableCount: %s",
                request.getUserId(), request.getBusinessName(), request.getTableCount()));
        return ResponseEntity.ok(tableCountService.addTableCount(request));
    }

    @PostMapping("/update")
    public ResponseEntity<UpdateTableCountResponseDto> updateTableCount(@RequestBody UpdateTableCountRequestDto request) {
        logger.info(String.format(
                "updateTableCount -> userId: %s, businessName: %s, tableCount: %s",
                request.getUserId(), request.getBusinessName(), request.getTableCount()));
        return ResponseEntity.ok(tableCountService.updateTableCount(request));
    }

    @PostMapping("/read")
    public ResponseEntity<ReadTableCountResponseDto> readTableCount(@RequestBody ReadTableCountRequestDto request) {
        logger.info(String.format(
                "readTableCount -> userId: %s, businessName: %s", request.getUserId(), request.getBusinessName()));
        return ResponseEntity.ok(tableCountService.readTableCount(request));
    }


}
