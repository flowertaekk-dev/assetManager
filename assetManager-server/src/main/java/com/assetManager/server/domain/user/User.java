package com.assetManager.server.domain.user;

import com.assetManager.server.domain.BaseTimeEntity;
import com.assetManager.server.domain.user.converter.UserStatusConverter;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.Arrays;

@Getter
@NoArgsConstructor
@Entity
public class User extends BaseTimeEntity {

    @Id
    @Column(nullable = false, unique = true)
    private String id;

    @Column(nullable = false)
    private String salt;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false, unique = true)
    private String email;

    @Convert(converter = UserStatusConverter.class)
    @Column(nullable = false)
    private UserStatus status;

    @Builder
    public User(String id, String salt, String password, String email, UserStatus status) {
        this.id = id;
        this.salt = salt;
        this.password = password;
        this.email = email;
        this.status = status;
    }

    public void initPasswordAndSaltKey() {
        this.password = null;
        this.salt = null;
    }

    @Getter
    public enum UserStatus {
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
