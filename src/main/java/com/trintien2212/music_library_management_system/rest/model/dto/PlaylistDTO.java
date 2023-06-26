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
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PlaylistDTO {
    @Positive(message = "Artist ID must be a positive number")
    private Long playlistId;
    @NotBlank(message = "Playlist name is required")
    private String playlistName;
    @NotNull(message = "Playlist description is required")
    @PastOrPresent(message = "Created date must be in the past or present")
    private LocalDate createdDate;
    private List<TrackDTO> tracks;

}