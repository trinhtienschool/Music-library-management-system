package com.trintien2212.music_library_management_system.rest.playlist;

import com.trintien2212.music_library_management_system.rest.model.dto.PlaylistDTO;
import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;

import java.util.List;

public interface PlaylistService {
    PlaylistDTO save(PlaylistDTO playlistDTO);
    List<PlaylistDTO> findAll();
    PlaylistDTO findById(long id);
    
    List<PlaylistDTO> findPlaylistsByPlaylistName(String name);

    List<PlaylistDTO> searchPlaylistsByKeyword(String keyword);
    List<PlaylistDTO> searchPlaylistsByMultipleCriteria(String trackTitle, String artistName, String albumName, String genre);

    List<TrackDTO> getTracksByPlaylistId(Long id);

    List<TrackDTO> getTracksByPlaylistName(String playlistName);

    List<TrackDTO> getTracksByPlaylistIdAndKeyword(Long playlistId, String keyword);
    List<TrackDTO> getTracksByPlaylistIdAndMultipleCriteria(Long playlistId, String trackTitle, String artistName, String albumName, String genre);
    PlaylistDTO update(PlaylistDTO playlistDTO);
    void delete(long id);
    PlaylistDTO addTracksToPlaylist(long playlistId, PlaylistDTO playlistDTO);
    PlaylistDTO removeTracksFromPlaylist(long playlistId, PlaylistDTO playlistDTO);
}
