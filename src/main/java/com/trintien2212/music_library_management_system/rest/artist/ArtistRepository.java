package com.trintien2212.music_library_management_system.rest.artist;

import com.trintien2212.music_library_management_system.rest.model.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ArtistRepository extends JpaRepository<Artist, Long> {
    List<Artist> findArtistsByArtistName(String name);
}
