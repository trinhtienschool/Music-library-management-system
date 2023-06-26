package com.trintien2212.music_library_management_system.rest.album;

import com.trintien2212.music_library_management_system.rest.model.dto.AlbumDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@RestController
@RequestMapping("/api/albums")
public class AlbumController {

    private AlbumService albumService;

    @Autowired
    public AlbumController(AlbumService albumService) {
        this.albumService = albumService;
    }

    @PostMapping
    public ResponseEntity save( @Valid @RequestBody AlbumDTO albumDTO){
        return ResponseEntity.status(201).body(albumService.save(albumDTO));
    }

    @GetMapping
    public ResponseEntity<List<AlbumDTO>> getAllAlbums(){
        return ResponseEntity.ok(albumService.findAll());
    }
    @GetMapping("/{id}")
    public ResponseEntity<AlbumDTO> findById(@PathVariable int id){
        return ResponseEntity.ok(albumService.findById(id));
    }

    @GetMapping("/find-by-name")
    public ResponseEntity<AlbumDTO> findAlbumByAlbumName(@RequestParam(name="album-name") @NotBlank String albumName){
        return ResponseEntity.ok(albumService.findAlbumByAlbumName(albumName));
    }

    @GetMapping("/find-by-artist-name")
    public ResponseEntity<List<AlbumDTO>> findAlbumsByAlbumArtistName(@RequestParam(name="artist-name") @NotBlank String artistName){
        return ResponseEntity.ok(albumService.findAlbumsByArtistName(artistName));
    }

    @PutMapping("/{id}")
    public ResponseEntity<AlbumDTO> update(@PathVariable("id") long id, @Valid @RequestBody AlbumDTO albumDTO){
        albumDTO.setAlbumId(id);
        return ResponseEntity.ok(albumService.update(albumDTO));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable int id){
        albumService.delete(id);
        return ResponseEntity.noContent().build();
    }

}
