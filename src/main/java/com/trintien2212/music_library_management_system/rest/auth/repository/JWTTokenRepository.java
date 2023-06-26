package com.trintien2212.music_library_management_system.rest.auth.repository;

import com.trintien2212.music_library_management_system.rest.auth.models.JWTToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JWTTokenRepository extends JpaRepository<JWTToken, Long> {
}
