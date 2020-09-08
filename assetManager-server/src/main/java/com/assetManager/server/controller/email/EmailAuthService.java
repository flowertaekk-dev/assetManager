package com.assetManager.server.controller.email;

import com.assetManager.server.controller.CommonResponseResult;
import com.assetManager.server.controller.email.dto.EmailAuthResponseDto;
import com.assetManager.server.domain.emailAuth.EmailAuth;
import com.assetManager.server.domain.emailAuth.EmailAuthRepository;
import com.assetManager.server.domain.user.UserRepository;
import lombok.RequiredArgsConstructor;
import net.bytebuddy.utility.RandomString;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.mail.MessagingException;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class EmailAuthService {
    private static Logger logger = LoggerFactory.getLogger(EmailAuthService.class);

    private final JavaMailSender mailSender;
    private final EmailAuthRepository emailAuthRepository;
    private final UserRepository userRepository;

    @Transactional
    public EmailAuthResponseDto sendEmail(String emailToSend) {

        // 이미 등록된 유저인가?
        if (isAlreadyOurMember(emailToSend)) {
            return EmailAuthResponseDto.builder()
                    .resultStatus(CommonResponseResult.FAILURE)
                    .reason("이미 사용중인 이메일입니다.")
                    .build();
        }

        // 이메일 작성 및 전송
        String authCode = RandomString.make();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(
                    mailSender.createMimeMessage(), true, "UTF-8");

            mimeMessageHelper.setTo(emailToSend);
            mimeMessageHelper.setSubject("AssetManager 인증코드입니다!");

            String html =
                    "<h1>AssetManager 인증코드: " + authCode + "</h1>"
                    + "<p>인증코드를 입력한 뒤 회원가입을 마무리해주세요!</p>";

            mimeMessageHelper.setText(html, true);
        } catch (MessagingException e) {
            throw new RuntimeException("이메일 발송 실패!");
        }

        mailSender.send(mimeMessageHelper.getMimeMessage());

        // 이미 등록된 EmailAuth 데이터가 있으면 업데이트만 한다
        Optional<EmailAuth> emailAuth = findEmailAuthByEmail(emailToSend);
        if (emailAuth.isPresent()) {
            emailAuth.get().updateAuthCode(authCode);
        } else {
            saveEmailAuth(emailToSend, authCode);
        }

        return EmailAuthResponseDto.builder()
                .resultStatus(CommonResponseResult.SUCCESS)
                .build();
    }

    /**
     * 해당 이메일이 EmailAuth 테이블에 이미 등록된 이메일이면 해당 인스턴스를 반환한다 (Optional)
     */
    private Optional<EmailAuth> findEmailAuthByEmail(String emailAddress) {
        return emailAuthRepository.findByEmail(emailAddress);
    }

    /**
     * 해당 이메일이 User 테이블에 이미 등록된 이메일이면 true, 아니면 false
     */
    private boolean isAlreadyOurMember(String emailAddress) {
        return userRepository.findByEmail(emailAddress).isPresent();
    }

    /**
     * EmailAuth에 데이터를 등록한다
     */
    private void saveEmailAuth(String emailAddress, String authCode) {
        emailAuthRepository.save(
                EmailAuth.builder()
                    .email(emailAddress)
                    .authCode(authCode)
                    .status(EmailAuth.EmailAuthStatus.SENT)
                    .build());
    }
}
