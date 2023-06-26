package com.trintien2212.music_library_management_system.rest.auth.controller;

import com.trintien2212.music_library_management_system.rest.auth.models.AuthRequestDTO;
import com.trintien2212.music_library_management_system.rest.auth.models.AuthResponseDTO;
import com.trintien2212.music_library_management_system.rest.auth.services.AuthService;
import com.trintien2212.music_library_management_system.rest.exception.BadRequestException;
import com.trintien2212.music_library_management_system.rest.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.SpringVersion;
import org.springframework.data.repository.query.Param;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.web.bind.annotation.*;
import javax.mail.MessagingException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.io.UnsupportedEncodingException;


@RestController
@Slf4j
@RequestMapping("/api/auth")
public class AuthController {

    AuthenticationManager authenticationManager;
    private AuthService authService;

    @Autowired
    public AuthController(AuthenticationManager authenticationManager, AuthService authService) {
        this.authenticationManager = authenticationManager;
        this.authService = authService;
    }

    @PostMapping("/register")
    public ResponseEntity<?> authRegister(@Valid @RequestBody @NotNull UserDTO regUser, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException, BadRequestException {
        return ResponseEntity.status(201).body(authService.register(regUser, request));
    }


    @GetMapping("/verify")
    public ResponseEntity<?> verifyUser(@Param("code") String code, HttpServletResponse res) throws ServletException, IOException {
        UserDTO userDTO = authService.verifyUser(code);

        if (userDTO == null) {
            return ResponseEntity.internalServerError().body("Verify failed");
        } else {
            return ResponseEntity.ok().body("Verify success! Please login to continue!");
        }
    }

    @PostMapping("/login")
    public ResponseEntity<?> authenticateAndGetJWT(@RequestBody @NotNull AuthRequestDTO authRequest) throws Exception {
        return ResponseEntity.ok(new AuthResponseDTO(authService.login(authRequest)));
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletRequest request, HttpServletResponse response) throws IOException {
        authService.logout(request);
        return ResponseEntity.ok("Logout success");
    }


}
