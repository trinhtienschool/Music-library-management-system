package com.trintien2212.music_library_management_system.rest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.Duration;
import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TrackDTO {
    @Positive(message = "Track ID must be a positive number")
    private Long trackId;
    @NotBlank(message = "Track title is required")
    private String trackTitle;
    @NotNull(message = "Duration is required")
    private Duration duration;
    @NotNull(message = "Artist is required")
    private ArtistDTO artist;
    @NotNull(message = "Album is required")
    private AlbumDTO album;
    private String lyrics;
    @NotBlank(message = "Genre is required")
    private String genre;
    @NotNull(message = "Release date is required")
    @PastOrPresent(message = "Release date must be in the past or present")
    private LocalDate releaseDate;

    @Override
    public String toString() {
        return "TrackDTO{" +
                "trackId=" + trackId +
                ", trackTitle='" + trackTitle + '\'' +
                ", duration=" + duration +
                ", artist=" + artist.getArtistName() +
                ", album=" + album.getAlbumName() +
                ", lyrics='" + lyrics + '\'' +
                ", genre='" + genre + '\'' +
                ", releaseDate=" + releaseDate +
                '}';
    }
}
