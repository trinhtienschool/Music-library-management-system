package com.trintien2212.music_library_management_system.rest.config;

import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;
import com.trintien2212.music_library_management_system.rest.model.entity.Track;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.Duration;


@Configuration
public class ConverterConfig {

    @Bean
    public ModelMapper modelMapper(){
        ModelMapper modelMapper = new ModelMapper();
        modelMapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        return modelMapper;
    }
    public TrackDTO convertTrackEntityToDTO(Track track){
        ModelMapper modelMapper = modelMapper();
        modelMapper.typeMap(Track.class, TrackDTO.class);
        TrackDTO trackDTO = modelMapper.map(track, TrackDTO.class);
        trackDTO.setDuration(Duration.parse(track.getDuration()));
        return trackDTO;
    }
    public UserDetails getLoginUser(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (!(auth instanceof AnonymousAuthenticationToken)) {
            return  (UserDetails)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        }
        return null;
    }




}
