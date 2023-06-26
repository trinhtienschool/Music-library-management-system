package com.trintien2212.music_library_management_system.rest.auth.services;

import com.trintien2212.music_library_management_system.rest.auth.models.AuthRequestDTO;
import com.trintien2212.music_library_management_system.rest.model.dto.UserDTO;

import javax.mail.MessagingException;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

public interface AuthService {
    String login(AuthRequestDTO authRequest) throws Exception;
    String register(UserDTO userDTO, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException;
    void logout(HttpServletRequest request);
    UserDTO verifyUser(String code);
}
