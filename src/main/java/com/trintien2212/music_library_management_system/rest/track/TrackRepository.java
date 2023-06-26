package com.trintien2212.music_library_management_system.rest.track;

import com.trintien2212.music_library_management_system.rest.model.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrackRepository extends JpaRepository<Track, Long> {
    List<Track> findTracksByTrackTitle(String title);
    List<Track> findTrackByArtist_ArtistNameContains(String artistName);
    List<Track> findTrackByAlbum_AlbumNameContains(String albumName);
    List<Track> findTracksByTrackTitleContainsOrArtist_ArtistNameContainsOrAlbum_AlbumNameContains(String title, String artistName, String albumName);

//    @Query("SELECT p FROM Track t JOIN t.playlists p WHERE t.trackId = :trackId")
//    List<Playlist> findPlaylistByTrackId(Long trackId);

}
