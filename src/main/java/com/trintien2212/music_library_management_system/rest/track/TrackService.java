package com.trintien2212.music_library_management_system.rest.track;

import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;

import java.util.List;

public interface TrackService {
    TrackDTO save(TrackDTO trackDTO);
    List<TrackDTO> findAll();
    TrackDTO findById(long id);
    List<TrackDTO> findByTrackTitle(String title);
    List<TrackDTO> findByArtistName(String artistName);
    List<TrackDTO> findByAlbumName(String albumName);
    List<TrackDTO> findByKeyword(String keyword);
    TrackDTO update(TrackDTO trackDTO);
    void delete(long id);
}
