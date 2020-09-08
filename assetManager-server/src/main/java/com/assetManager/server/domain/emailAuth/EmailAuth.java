package com.assetManager.server.domain.emailAuth;

import com.assetManager.server.domain.emailAuth.converter.EmailAuthStatusConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@NoArgsConstructor
@Entity
public class EmailAuth {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String authCode;

    @Convert(converter = EmailAuthStatusConverter.class)
    @Column(nullable = false)
    private EmailAuthStatus status;

    @Builder
    public EmailAuth(String email, String authCode, EmailAuthStatus status) {
        this.email = email;
        this.authCode = authCode;
        this.status = status;
    }

    @Getter
    public enum EmailAuthStatus {
        SENT("발송완료"),
        COMPLETED("인증완료"),
        ;

        private String status;
        EmailAuthStatus(String status) {
            this.status = status;
        }

        public static EmailAuthStatus of(String status) {
            return Arrays.stream(values())
                    .filter(v -> v.getStatus().equals(status))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 코드입니다 : %s", status)));
        }
    }

    // -----------------------------------------------------------------------------------
    // util method

    /**
     * Email AuthCode를 갱신한다
     */
    public void updateAuthCode(String newAuthCode) {
        this.authCode = newAuthCode;
    }

    /**
     * EmailAuthStatus를 갱신한다
     */
    public void updateStatus(EmailAuthStatus status) {
        this.status = status;
    }

}
