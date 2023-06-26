package com.trintien2212.music_library_management_system.rest.artist;

import com.trintien2212.music_library_management_system.rest.model.dto.ArtistDTO;
import java.util.List;

public interface ArtistService {
    ArtistDTO save(ArtistDTO artistDTO);
    List<ArtistDTO> findAll();
    ArtistDTO findById(long id);
    List<ArtistDTO> findArtistByArtistName(String name);
    ArtistDTO update(ArtistDTO artistDTO);
    void delete(long id);
}
