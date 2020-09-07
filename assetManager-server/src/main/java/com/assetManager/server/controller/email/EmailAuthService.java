package com.assetManager.server.controller.email;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.email.dto.EmailAuthResponseDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;

@RequiredArgsConstructor
@Service
public class EmailAuthService {
    private static Logger logger = LoggerFactory.getLogger(EmailAuthService.class);

    private final JavaMailSender mailSender;
    private final EmailAuthRepository emailAuthRepository;

    @Transactional
    public EmailAuthResponseDto sendEmail(String emailToSend) {

        if (isAlreadyRegistered(emailToSend)) {
            return EmailAuthResponseDto.builder()
                    .resultStatus(CommonResponseResult.FAILURE)
                    .reason("이미 사용중인 이메일입니다.")
                    .build();
        }

        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(
                    mailSender.createMimeMessage(), true, "UTF-8");

            mimeMessageHelper.setTo(emailToSend);
            mimeMessageHelper.setSubject("AssetManager 인증코드입니다!");

            String html =
                    "<h1>AssetManager 인증코드: " + RandomString.make() + "</h1>"
                    + "<p>인증코드를 입력한 뒤 회원가입을 마무리해주세요!</p>";

            mimeMessageHelper.setText(html, true);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패!");
        }

        mailSender.send(mimeMessageHelper.getMimeMessage());

        saveEmailAuth(emailToSend);
        return EmailAuthResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    private boolean isAlreadyRegistered(String emailAddress) {
        return emailAuthRepository.findByEmail(emailAddress).isPresent();
    }

    private void saveEmailAuth(String emailAddress) {
        emailAuthRepository.save(
                EmailAuth.builder()
                    .email(emailAddress)
                    .status(EmailAuth.EmailAuthStatus.SENT)
                    .build());
    }
}
