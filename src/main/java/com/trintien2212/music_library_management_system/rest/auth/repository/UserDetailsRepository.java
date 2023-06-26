package com.trintien2212.music_library_management_system.rest.auth.repository;

import com.trintien2212.music_library_management_system.rest.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDetailsRepository extends JpaRepository<User, Long> {
    Optional<User> findUserByUsername(String username);





}
