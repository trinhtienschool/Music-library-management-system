package com.trintien2212.music_library_management_system.rest.playlist;

import com.trintien2212.music_library_management_system.rest.model.dto.PlaylistDTO;
import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/playlists")
public class PlaylistController {

    private PlaylistService playlistService;

    @Autowired
    PlaylistController(PlaylistService playlistService){
        this.playlistService = playlistService;
    }

    @PostMapping
    public ResponseEntity<PlaylistDTO> save(@Valid @RequestBody PlaylistDTO playlistDTO) {
        PlaylistDTO savedPlaylist = playlistService.save(playlistDTO);
        return ResponseEntity.status(201).body(savedPlaylist);
    }

    @GetMapping
    public ResponseEntity<List<PlaylistDTO>> findAll() {
        List<PlaylistDTO> playlists = playlistService.findAll();
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{id}")
    public ResponseEntity<PlaylistDTO> findById(@PathVariable("id") long id) {
        PlaylistDTO playlist = playlistService.findById(id);
        return ResponseEntity.ok(playlist);
    }

    @GetMapping("/search-by-playlist-name")
    public ResponseEntity<List<PlaylistDTO>> searchByPlaylistName(@RequestParam("name") @NotBlank String name) {
        List<PlaylistDTO> playlists = playlistService.findPlaylistsByPlaylistName(name);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/search")
    public ResponseEntity<List<PlaylistDTO>> searchByMultipleCriteria(@RequestParam("genre") String genre,
                                                                       @RequestParam("albumName") String albumName,
                                                                       @RequestParam("artistName") String artistName,
                                                                       @RequestParam("trackTitle") String trackTitle) {
        List<PlaylistDTO> playlists = playlistService.searchPlaylistsByMultipleCriteria(trackTitle, artistName, albumName, genre);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/search-by-keyword")
    public ResponseEntity<List<PlaylistDTO>> searchByKeyword(@RequestParam("keyword") @NotNull String keyword) {
        List<PlaylistDTO> playlists = playlistService.searchPlaylistsByKeyword(keyword);
        return ResponseEntity.ok(playlists);
    }

    @GetMapping("/{playlistId}/tracks")
    public ResponseEntity<List<TrackDTO>> getTracksByPlaylistId(@PathVariable("playlistId") Long playlistId) {
        List<TrackDTO> tracks = playlistService.getTracksByPlaylistId(playlistId);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/tracks")
    public ResponseEntity<List<TrackDTO>> getTracksByPlaylistId(@RequestParam("playlistName") @NotBlank String playlistName) {
        List<TrackDTO> tracks = playlistService.getTracksByPlaylistName(playlistName);
        return ResponseEntity.ok(tracks);
    }


    @GetMapping("/{playlistId}/tracks/search-keyword")
    public ResponseEntity<List<TrackDTO>> getTracksByPlaylistIdAndKeyword(@PathVariable("playlistId") Long playlistId,
                                                                                     @RequestParam("keyword") @NotBlank String keyword) {
        List<TrackDTO> tracks = playlistService.getTracksByPlaylistIdAndKeyword(playlistId, keyword);
        return ResponseEntity.ok(tracks);
    }

    @GetMapping("/{playlistId}/tracks/search")
    public ResponseEntity<List<TrackDTO>> getTracksByPlaylistIdAndMultipleCriteria(@PathVariable("playlistId") Long playlistId,
                                                                                   @RequestParam("genre") String genre,
                                                                                   @RequestParam("albumName") String albumName,
                                                                                   @RequestParam("artistName") String artistName,
                                                                                   @RequestParam("trackTitle") String trackTitle) {
        List<TrackDTO> tracks = playlistService.getTracksByPlaylistIdAndMultipleCriteria(playlistId, trackTitle, artistName, albumName, genre);
        return ResponseEntity.ok(tracks);
    }

    @PutMapping("/{id}")
    public ResponseEntity<PlaylistDTO> update(@PathVariable("id") long id, @Valid @RequestBody PlaylistDTO playlistDTO) {
        playlistDTO.setPlaylistId(id);
        PlaylistDTO updatedPlaylist = playlistService.update(playlistDTO);
        return ResponseEntity.ok(updatedPlaylist);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable("id") long id) {
        playlistService.delete(id);
        return ResponseEntity.noContent().build();
    }

    //Add tracks to playlist
    @PostMapping("/{playlistId}/tracks/add")
    public ResponseEntity<PlaylistDTO> addTrackToPlaylist(@PathVariable("playlistId") long playlistId, @RequestBody PlaylistDTO playlistDTO) {
        return ResponseEntity.ok(playlistService.addTracksToPlaylist(playlistId, playlistDTO));
    }

    //Remove tracks from playlist
    @DeleteMapping("/{playlistId}/tracks/remove")
    public ResponseEntity<PlaylistDTO> removeTracksFromPlaylist(@PathVariable("playlistId") long playlistId, @RequestBody PlaylistDTO playlistDTO) {
        return ResponseEntity.ok(playlistService.removeTracksFromPlaylist(playlistId, playlistDTO));
    }
}
