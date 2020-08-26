package com.assetManager.server.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;

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

    @Column(nullable = false)
    private String email;

    @Builder
    public User(String id, String password, String email) {
        this.id = id;
        this.password = password;
        this.email = email;
    }
}
