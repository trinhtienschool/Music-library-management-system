package com.trintien2212.music_library_management_system.rest.auth.services;

import com.trintien2212.music_library_management_system.rest.auth.repository.JWTTokenRepository;
import com.trintien2212.music_library_management_system.rest.auth.repository.UserDetailsRepository;
import com.trintien2212.music_library_management_system.rest.auth.models.AuthRequestDTO;
import com.trintien2212.music_library_management_system.rest.auth.models.JWTToken;
import com.trintien2212.music_library_management_system.rest.auth.utils.JwtUtil;
import com.trintien2212.music_library_management_system.rest.exception.BadRequestException;
import com.trintien2212.music_library_management_system.rest.model.dto.UserDTO;
import com.trintien2212.music_library_management_system.rest.model.entity.User;
import com.trintien2212.music_library_management_system.rest.auth.repository.UserRepository;
import net.bytebuddy.utility.RandomString;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;

@Service
public class AuthServiceImpl implements AuthService{
    
    private UserDetailsRepository userDetailsRepository;
    private PasswordEncoder passwordEncoder;
    private ModelMapper modelMapper;
    private JwtUtil jwtUtil;
    private UserRepository userRepository;
    private AuthenticationManager authenticationManager;
    private JWTTokenRepository jwtTokenRepository;
    private JavaMailSender mailSender;

    @Autowired
    public AuthServiceImpl(UserDetailsRepository userDetailsRepository,
                           PasswordEncoder passwordEncoder,
                           ModelMapper modelMapper,
                           JWTTokenRepository jwtTokenRepository,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil,
                           UserRepository userRepository,
                           JavaMailSender mailSender){
        this.userDetailsRepository = userDetailsRepository;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.jwtTokenRepository = jwtTokenRepository;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
        this.userRepository = userRepository;
        this.mailSender = mailSender;
    }
    @Override
    public String login(AuthRequestDTO authRequest) throws Exception {
        try {
            authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        } catch (BadCredentialsException e) {
            throw new Exception("Incorrect username or password", e);
        }
        return jwtUtil.generateJWT(userDetailsRepository.findUserByUsername(authRequest.getUsername()));
    }

    @Override
    public String register(UserDTO userDTO, HttpServletRequest request) throws MessagingException, UnsupportedEncodingException {
        userDetailsRepository.findUserByUsername(userDTO.getUsername()).ifPresent(user -> {throw new BadRequestException( "Username already existed!");});
        User user = convertDtoToDao(userDTO);
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        String randomCode = RandomString.make(8);
        user.setActive(false);
        user.setVerificationCode(randomCode);
        user.setRoles("ROLE_ADMIN");
        sendVerificationEmail(user, getSiteURL(request));
        userDetailsRepository.save(user);
        return "Check email to verify your account!";
    }

    @Override
    public void logout(HttpServletRequest request) {
        final String authorizationHeader = request.getHeader("Authorization");
        if(authorizationHeader != null && authorizationHeader.startsWith("Bearer ")){
            jwtTokenRepository.save(new JWTToken(authorizationHeader.substring(7)));
        }
    }

    @Override
    public UserDTO verifyUser(String code) {
        User user = userRepository.findByVerificationCode(code);
        if(user == null) return null;
        if (user !=null && user.isActive() && user.getVerificationCode() == null) {
            return convertDaoToDto(user);
        }
        else {
            user.setActive(true);
            user.setVerificationCode(null);
            return convertDaoToDto(userRepository.save(user));
        }
    }

    private User convertDtoToDao(UserDTO userDTO){
        return modelMapper.map(userDTO, User.class);
    }

    private UserDTO convertDaoToDto(User user){
        return modelMapper.map(user, UserDTO.class);
    }

    private void sendVerificationEmail(User user, String siteURL)
            throws  UnsupportedEncodingException, MessagingException {
        String toAddress = user.getEmail();
        String fromAddress = "trinhtien6236@gmail.com";
        String senderName = "Music Library Management System";
        String subject = "Please verify your registration";
        String content = "Dear [[name]],<br>"
                + "Please click the link below to verify your registration:<br>"
                + "<h3><a href=\"[[URL]]\" target=\"_self\">VERIFY</a></h3>"
                + "Thank you,<br>"
                + "Trinh Quang Tien";

        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message);

        helper.setFrom(fromAddress, senderName);
        helper.setTo(toAddress);
        helper.setSubject(subject);

        content = content.replace("[[name]]", user.getUsername());

        String verifyURL = siteURL + "/api/auth/verify?code=" + user.getVerificationCode();

        content = content.replace("[[URL]]", verifyURL);

        helper.setText(content, true);


        mailSender.send(message);

    }

    private String getSiteURL(HttpServletRequest request) {
        String siteURL = request.getRequestURL().toString();
        return siteURL.replace(request.getServletPath(), "");
    }
}
