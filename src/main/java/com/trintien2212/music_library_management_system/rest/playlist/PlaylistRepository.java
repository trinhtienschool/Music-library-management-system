package com.trintien2212.music_library_management_system.rest.playlist;

import com.trintien2212.music_library_management_system.rest.model.entity.Playlist;
import com.trintien2212.music_library_management_system.rest.model.entity.Track;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PlaylistRepository extends JpaRepository<Playlist, Long> {
    List<Playlist> findPlaylistsByPlaylistName(String name);
    List<Playlist> findPlaylistsByTracks_TrackTitleContainsOrTracks_Artist_ArtistNameContainsOrTracks_Album_AlbumNameContainsOrTracks_GenreContains(String trackTitle, String artistName, String albumName, String genre);
    List<Playlist> findPlaylistsByTracks_TrackTitleContainsAndTracks_Artist_ArtistNameContainsAndTracks_Album_AlbumNameContainsAndTracks_GenreContains(String trackTitle, String artistName, String albumName, String genre);

    //find all tracks of the playlist
    @Query("SELECT p.tracks FROM Playlist p WHERE p.playlistId = :playlistId")
    List<Track> findTracksByPlaylistId(Long playlistId);

    @Query("SELECT p.tracks FROM Playlist p WHERE p.playlistName = :playlistName")
    List<Track> findTracksByPlaylistName(String playlistName);

    //find track by track title or album name or genre or artist name in a specific playlistName
    @Query("SELECT p.tracks FROM Playlist p JOIN p.tracks t WHERE p.playlistId = :playlistId AND (t.trackTitle LIKE %:keyword% OR t.artist.artistName LIKE %:keyword% OR t.album.albumName LIKE %:keyword% OR t.genre LIKE %:keyword%)")
    List<Track> findTracksByPlaylistIdAndTracks_TrackTitleContainsOrTracks_Artist_ArtistNameContainsOrTracks_Album_AlbumNameContainsOrTracks_GenreContains(Long playlistId, String keyword);

    @Query("SELECT p.tracks FROM Playlist p JOIN p.tracks t WHERE p.playlistId = :playlistId AND (t.trackTitle LIKE %:trackTitle% AND t.artist.artistName LIKE %:artistName% AND t.album.albumName LIKE %:albumName% AND t.genre LIKE %:genre%)")
    List<Track> findTracksByPlaylistIdAndTracks_TrackTitleContainsAndTracks_Artist_ArtistNameContainsAndTracks_Album_AlbumNameContainsAndTracks_GenreContains(Long playlistId, String trackTitle, String artistName, String albumName, String genre);

    Optional<Playlist> findPlaylistByPlaylistNameAndUser_UserId(String playlistName, Long UserId);

}
