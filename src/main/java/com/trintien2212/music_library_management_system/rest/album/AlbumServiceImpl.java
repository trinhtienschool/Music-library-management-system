package com.trintien2212.music_library_management_system.rest.album;

import com.trintien2212.music_library_management_system.rest.artist.ArtistRepository;
import com.trintien2212.music_library_management_system.rest.exception.BadRequestException;
import com.trintien2212.music_library_management_system.rest.exception.ResourceExistedException;
import com.trintien2212.music_library_management_system.rest.exception.ResourceNotFoundException;
import com.trintien2212.music_library_management_system.rest.model.dto.AlbumDTO;
import com.trintien2212.music_library_management_system.rest.model.entity.Album;
import com.trintien2212.music_library_management_system.rest.model.entity.Artist;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AlbumServiceImpl implements AlbumService {

    private AlbumRepository albumRepository;
    private ArtistRepository artistRepository;
    private ModelMapper modelMapper;

    @Autowired
    public AlbumServiceImpl(AlbumRepository albumRepository, ArtistRepository artistRepository, ModelMapper modelMapper){
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
        this.artistRepository = artistRepository;
    }

    @Override
    @Transactional
    public AlbumDTO save(AlbumDTO albumDTO) {
        if(albumDTO.getAlbumId() != null){
            albumRepository.findById(albumDTO.getAlbumId())
                       .ifPresent(album -> {
                           throw
                                   new ResourceExistedException("Album has id: "+albumDTO.getAlbumId()+" has already existed!");});
        }
        if(albumDTO.getAlbumName() !=null){
            albumRepository.findAlbumByAlbumName(albumDTO.getAlbumName())
                    .ifPresent(Album -> {
                        throw
                                new ResourceExistedException("Album has name: "+albumDTO.getAlbumName()+" has already existed!");});
        }

        return convertDaoToDto(albumRepository.save(convertDtoToDao(albumDTO)));
    }

    @Override
    public List<AlbumDTO> findAll() {
        return albumRepository.findAll().stream()
                          .map(album -> convertDaoToDto(album))
                          .collect(Collectors.toList());
    }

    @Override
    public AlbumDTO findById(long id) {
        return albumRepository.findById(id).map(album -> convertDaoToDto(album))
                .orElseThrow(() -> new ResourceNotFoundException("Not found Album has id: "+id));
    }

    @Override
    public AlbumDTO findAlbumByAlbumName(String albumName) {
        return albumRepository.findAlbumByAlbumName(albumName).map(album -> convertDaoToDto(album))
                .orElseThrow(() -> new ResourceNotFoundException("Not found Album has name: "+albumName));
    }

    @Override
    public List<AlbumDTO> findAlbumsByArtistName(String name) {
        return albumRepository.findAlbumsByArtist_ArtistName(name).stream()
                .map(album -> convertDaoToDto(album)).collect(Collectors.toList());
    }

    @Override
    @Transactional
    public AlbumDTO update(AlbumDTO albumDTO) {
        return albumRepository.findById(albumDTO.getAlbumId())
                   .map(album -> convertDaoToDto(albumRepository.save(convertDtoToDao(albumDTO)))
                   )
                   .orElseThrow(() -> new ResourceNotFoundException("Not found Album has id: "+albumDTO.getAlbumId()));

    }

    @Override
    @Transactional
    public void delete(long id) {
        albumRepository.findById(id).ifPresentOrElse(
                (album) -> {albumRepository.delete(album);},
                () -> {throw new ResourceNotFoundException("Not found Album has id: "+id);});
    }

    private Album convertDtoToDao(AlbumDTO albumDTO){
        modelMapper.typeMap(AlbumDTO.class, Album.class);
        Album album = modelMapper.map(albumDTO, Album.class);
        if(albumDTO.getArtist() != null && albumDTO.getArtist().getArtistId() != null) {
            //check if artist existed
            Artist artist = artistRepository.findById(albumDTO.getArtist().getArtistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artist has id "+albumDTO.getArtist().getArtistId()+" not found!"));
            album.setArtist(artist);
        }else throw new BadRequestException("Missing the Artist Id field");
        return album;
    }

    private AlbumDTO convertDaoToDto(Album album){
        modelMapper.typeMap(Album.class, AlbumDTO.class);
        AlbumDTO albumDTO = modelMapper.map(album, AlbumDTO.class);
        return albumDTO;
    }
}
