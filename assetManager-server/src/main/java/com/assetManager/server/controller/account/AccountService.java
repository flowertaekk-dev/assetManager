package com.assetManager.server.controller.account;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.account.dto.SaveAccountRequestDto;
import com.assetManager.server.controller.account.dto.SaveAccountResponseDto;
import com.assetManager.server.domain.account.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@RequiredArgsConstructor
@Service
public class AccountService {

    private final AccountRepository accountRepository;

    /**
     * 장부 데이터를 DB에 저장한다
     * @param request
     * @return
     */
    protected SaveAccountResponseDto saveAccountInfo(SaveAccountRequestDto request) {
        return SaveAccountResponseDto.builder()
                .account(accountRepository.save(request.toAccountEntity()))
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

}
