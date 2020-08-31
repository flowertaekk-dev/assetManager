package com.assetManager.server.domain.user;

import com.assetManager.server.domain.user.converter.UserStatusConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@NoArgsConstructor
@Entity
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long seq;

    @Column(nullable = false)
    private String id;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Convert(converter = UserStatusConverter.class)
    @Column(nullable = false)
    private UserStatus status;

    @Builder
    public User(String id, String password, String email, UserStatus status) {
        this.id = id;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    @Getter
    public enum UserStatus {
        APPLIED("신청완료"),
        USING("사용중"),
        REMOVED("종료"),
        ;

        private String status;

        UserStatus(String status) {
            this.status = status;
        }

        public static UserStatus of(String status) {
            return Arrays.stream(values())
                    .filter(v -> v.getStatus().equals(status))
                    .findAny()
                    .orElseThrow(() -> new IllegalArgumentException(String.format("존재하지 않는 코드입니다 : %s", status)));
        }
    }
}
