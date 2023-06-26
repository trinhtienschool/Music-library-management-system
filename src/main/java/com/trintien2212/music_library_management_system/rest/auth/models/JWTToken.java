package com.trintien2212.music_library_management_system.rest.auth.models;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;

@Entity
@Table(name = "jwt_token")
@Getter
@Setter
public class JWTToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tokenId;
    private String token;

    public JWTToken(Long tokenId, String token) {
        this.tokenId = tokenId;
        this.token = token;
    }

    public JWTToken() {
    }

    public JWTToken(String token) {
        this.token = token;
    }
}
