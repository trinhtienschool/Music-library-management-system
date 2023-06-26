package com.trintien2212.music_library_management_system.rest.model.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "music_track")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Track {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long trackId;

    @Column(nullable = false)
    private String trackTitle;

    @ManyToOne
    @JoinColumn(name = "artist_id")
    private Artist artist;

    @ManyToOne
    @JoinColumn(name = "album_id")
    private Album album;

    @Column(nullable = false)
    private String genre;

    @Column(nullable = false)
    private LocalDate releaseDate;

    @Column(nullable = false)
    private String duration;
    private String lyrics;

    @ManyToMany(mappedBy = "tracks")
    private Set<Playlist> playlists;

    @Override
    public String toString() {
        return "Track{" +
                "trackId=" + trackId +
                ", trackTitle='" + trackTitle + '\'' +
                ", artist=" + artist.getArtistName() +
                ", album=" + album.getAlbumName() +
                ", genre='" + genre + '\'' +
                ", releaseDate=" + releaseDate +
                ", duration='" + duration + '\'' +
                ", lyrics='" + lyrics + '\'' +
                '}';
    }
}