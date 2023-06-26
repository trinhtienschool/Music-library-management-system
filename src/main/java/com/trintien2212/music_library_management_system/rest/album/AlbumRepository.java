package com.trintien2212.music_library_management_system.rest.album;

import com.trintien2212.music_library_management_system.rest.model.entity.Album;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.Optional;

public interface AlbumRepository extends JpaRepository<Album, Long> {
    Optional<Album> findAlbumByAlbumName(String name);
    List<Album> findAlbumsByArtist_ArtistName(String artistName);
}
