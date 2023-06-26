package com.trintien2212.music_library_management_system.rest.artist;

import com.trintien2212.music_library_management_system.rest.model.dto.ArtistDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;

@RestController
@RequestMapping("/api/artists")
@Slf4j
@Validated
public class ArtistController {

    private final ArtistService artistService;

    @Autowired
    public ArtistController(ArtistService artistService) {
        this.artistService = artistService;
    }


    @PostMapping
    public ResponseEntity save(@Valid @RequestBody ArtistDTO artistDTO){
        return ResponseEntity.status(201).body(artistService.save(artistDTO));
    }

    @GetMapping
    public ResponseEntity<List<ArtistDTO>> getAllArtists(){
        return ResponseEntity.ok(artistService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<ArtistDTO> findById(@PathVariable @NotNull int id){
        return ResponseEntity.ok(artistService.findById(id));
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<List<ArtistDTO>> findArtistByArtistName(@RequestParam(name="artist-name") @NotNull String artistName){
        return ResponseEntity.ok(artistService.findArtistByArtistName(artistName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ArtistDTO> update(@PathVariable("id") long id, @Valid @RequestBody ArtistDTO artistDTO){
        artistDTO.setArtistId(id);
        return ResponseEntity.ok(artistService.update(artistDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable @NotNull int id){
        artistService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
