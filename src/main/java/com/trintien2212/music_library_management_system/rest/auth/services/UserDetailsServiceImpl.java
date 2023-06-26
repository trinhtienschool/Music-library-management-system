package com.trintien2212.music_library_management_system.rest.auth.services;


import com.trintien2212.music_library_management_system.rest.auth.repository.UserDetailsRepository;
import com.trintien2212.music_library_management_system.rest.auth.models.UserDetailsImpl;
import com.trintien2212.music_library_management_system.rest.model.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    private UserDetailsRepository UserDetailsRepository;

    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

        Optional<User> user = UserDetailsRepository.findUserByUsername(username);

        user.orElseThrow(() -> new UsernameNotFoundException(username + " not found."));

        return user.map(UserDetailsImpl::new).get();
    }




}
