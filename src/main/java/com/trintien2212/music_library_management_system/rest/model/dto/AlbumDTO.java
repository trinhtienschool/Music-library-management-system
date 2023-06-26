package com.trintien2212.music_library_management_system.rest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Positive;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AlbumDTO {
    @Positive(message = "Album ID must be a positive number")
    private Long albumId;
    @NotBlank(message = "Album name is required")
    private String albumName;
    @NotNull(message = "Release date is required")
    @PastOrPresent(message = "Release date must be in the past or present")
    private LocalDate releaseDate;
    @NotNull(message = "Artist is required")
    private ArtistDTO artist;
    @NotBlank(message = "Genre is required")
    private String genre;

}
