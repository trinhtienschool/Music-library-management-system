package com.trintien2212.music_library_management_system.rest.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.*;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ArtistDTO {

    @Positive(message = "Artist ID must be a positive number")
    private Long artistId;
    @NotBlank(message = "Artist name is required")
    @Size(max = 100, message = "Artist name must be up to 100 characters")
    private String artistName;
    @NotBlank(message = "Genre is required")
    @Size(max = 100, message = "Genre must be up to 100 characters")
    private String genre;

    @NotNull(message = "Date of birth is required")
    @Past(message = "Date of Birth must be in the past ")
    private LocalDate dateOfBirth;
    private String country;

    @Override
    public String toString() {
        return "ArtistDTO{" +
                "artistId=" + artistId +
                ", artistName='" + artistName + '\'' +
                ", genre='" + genre + '\'' +
                ", dateOfBirth=" + dateOfBirth +
                ", country='" + country + '\'' +
                '}';
    }
}
