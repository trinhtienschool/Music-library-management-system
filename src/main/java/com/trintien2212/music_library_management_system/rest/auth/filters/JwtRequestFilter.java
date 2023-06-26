package com.trintien2212.music_library_management_system.rest.auth.filters;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.trintien2212.music_library_management_system.rest.auth.repository.JWTTokenRepository;
import com.trintien2212.music_library_management_system.rest.auth.utils.JwtUtil;
import com.trintien2212.music_library_management_system.rest.model.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;
import java.util.TreeMap;
import java.util.stream.Collectors;

@Component
@Slf4j
public class JwtRequestFilter extends OncePerRequestFilter {
    public static final String CURRENT_USER_REQUEST = "currentUser";
    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private JWTTokenRepository jwtTokenRepository;

    @Autowired
    private JwtUtil jwtUtil;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String authorizationHeader = request.getHeader("Authorization");


        String username = null;
        String jwt = null;
        Long userId = null;

        if (authorizationHeader != null && authorizationHeader.startsWith("Bearer ")) {
            jwt = authorizationHeader.substring(7);
            username = jwtUtil.extractUserName(jwt);
            userId = jwtUtil.extractUserId(jwt).longValue();
        }
        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

            UserDetails userDetails = this.userDetailsService.loadUserByUsername(username);

            if (!(jwtUtil.isJwtExpired(jwt))) {

                //check user has logout
                //check if jwt is in blacklist
                //Get all jwtTokens from the JWTToken object then check if jwt is in blacklist
                if (jwtTokenRepository.findAll().stream().map(jwtToken -> jwtToken.getToken()).collect(Collectors.toList()).contains(jwt)) {

                    //set Content-Type header for response
                    Map<String, String> returnObject = new TreeMap<>();
                    //put status and message and localdatetime to map
                    returnObject.put("status", String.valueOf(HttpStatus.UNAUTHORIZED.value()));
                    returnObject.put("message", "You have logged out");
                    returnObject.put("time", new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));

                    response.setContentType("application/json");
                    response.setStatus(HttpStatus.UNAUTHORIZED.value());
                    response.getWriter().write(convertObjectToJson(returnObject));
                    return;
                }

                UsernamePasswordAuthenticationToken usernamePasswordAuthenticationToken = new UsernamePasswordAuthenticationToken(
                        userDetails, null, userDetails.getAuthorities());
                usernamePasswordAuthenticationToken
                        .setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(usernamePasswordAuthenticationToken);
            }
        }
        if (userId != null) {
            User user = new User();
            user.setUserId(userId);
            request.setAttribute(CURRENT_USER_REQUEST, user);
        }
        chain.doFilter(request, response);

    }
    public String convertObjectToJson(Object object) throws JsonProcessingException {
        if (object == null) {
            return null;
        }
        ObjectMapper mapper = new ObjectMapper();
        return mapper.writeValueAsString(object);
    }
}
