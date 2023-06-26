package com.trintien2212.music_library_management_system.rest.track;

import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/tracks")
public class TrackController {

    private TrackService trackService;

    @Autowired
    public TrackController(TrackService trackService) {
        this.trackService = trackService;
    }

    @PostMapping
    public ResponseEntity save( @Valid @RequestBody TrackDTO trackDTO){
        return ResponseEntity.status(201).body(trackService.save(trackDTO));
    }

    @GetMapping
    public ResponseEntity<List<TrackDTO>> getAllTracks(){
        return ResponseEntity.ok(trackService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<TrackDTO> findById(@PathVariable int id){
        return ResponseEntity.ok(trackService.findById(id));
    }

    @GetMapping("/find-by-title")
    public ResponseEntity<List<TrackDTO>> findByTrackTitle(@RequestParam(name="track-title") @NotBlank String trackTitle){
        return ResponseEntity.ok(trackService.findByTrackTitle(trackTitle));
    }

    @GetMapping("/find-by-artist")
    public ResponseEntity<List<TrackDTO>> findByArtist(@RequestParam(name="artist-name") @NotBlank String artistName){
        return ResponseEntity.ok(trackService.findByArtistName(artistName));
    }

    @GetMapping("/find-by-album")
    public ResponseEntity<List<TrackDTO>> findByAlbum(@RequestParam(name="album-name") @NotBlank String artistName){
        return ResponseEntity.ok(trackService.findByAlbumName(artistName));
    }

    @GetMapping("/find")
    public ResponseEntity<List<TrackDTO>> findByKeyword(@RequestParam(name="keyword") @NotBlank String keyword){
        return ResponseEntity.ok(trackService.findByKeyword(keyword));
    }

    @PutMapping("/{id}")
    public ResponseEntity<TrackDTO> update(@PathVariable("id") long id, @Valid @RequestBody TrackDTO trackDTO){
        trackDTO.setTrackId(id);
        return ResponseEntity.ok(trackService.update(trackDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        trackService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
