package com.trintien2212.music_library_management_system.rest.auth.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import java.io.Serializable;

@Getter @Setter
@AllArgsConstructor
public class AuthResponseDTO implements Serializable {
    private String jwt;
}
