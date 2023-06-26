package com.trintien2212.music_library_management_system.rest.album;

import com.trintien2212.music_library_management_system.rest.model.dto.AlbumDTO;
import java.util.List;

public interface AlbumService {
    AlbumDTO save(AlbumDTO albumDTO);
    List<AlbumDTO> findAll();
    AlbumDTO findById(long id);
    AlbumDTO findAlbumByAlbumName(String name);
    List<AlbumDTO> findAlbumsByArtistName(String artistName);
    AlbumDTO update(AlbumDTO albumDTO);
    void delete(long id);
}
