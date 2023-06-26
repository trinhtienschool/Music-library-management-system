package com.trintien2212.music_library_management_system.rest.artist;

import com.trintien2212.music_library_management_system.rest.exception.ResourceExistedException;
import com.trintien2212.music_library_management_system.rest.exception.ResourceNotFoundException;
import com.trintien2212.music_library_management_system.rest.model.dto.ArtistDTO;
import com.trintien2212.music_library_management_system.rest.model.entity.Artist;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ArtistServiceImpl implements ArtistService {

    private ArtistRepository artistRepository;
    private ModelMapper modelMapper;

    @Autowired
    public ArtistServiceImpl(ArtistRepository artistRepository, ModelMapper modelMapper) {
        this.artistRepository = artistRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public ArtistDTO save(ArtistDTO artistDTO) {
        if (artistDTO.getArtistId() != null) {
            artistRepository.findById(artistDTO.getArtistId())
                    .ifPresent(artist -> {
                        throw
                                new ResourceExistedException("Artist has id: " + artistDTO.getArtistId() + " has already existed!");
                    });
        }
        return convertDaoToDto(artistRepository.save(convertDtoToDao(artistDTO)));
    }

    @Override
    public List<ArtistDTO> findAll() {
        return artistRepository.findAll().stream()
                .map(artist -> convertDaoToDto(artist))
                .collect(Collectors.toList());
    }

    @Override
    public ArtistDTO findById(long id) {
        return artistRepository.findById(id).map(artist -> convertDaoToDto(artist))
                .orElseThrow(() -> new ResourceNotFoundException("Not found Artist has id: " + id));
    }

    @Override
    public List<ArtistDTO> findArtistByArtistName(String artistName) {
        return artistRepository.findArtistsByArtistName(artistName).stream()
                .map(artist -> convertDaoToDto(artist))
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ArtistDTO update(ArtistDTO artistDTO) {
        return artistRepository.findById(artistDTO.getArtistId())
                .map(artist -> convertDaoToDto(artistRepository.save(convertDtoToDao(artistDTO)))
                )
                .orElseThrow(() -> new ResourceNotFoundException("Not found Artist has id: " + artistDTO.getArtistId()));

    }

    @Override
    @Transactional
    public void delete(long id) {
        artistRepository.findById(id).ifPresentOrElse(
                (artist) -> {
                    artistRepository.delete(artist);
                },
                () -> {
                    throw new ResourceNotFoundException("Not found Artist has id: " + id);
                });
    }

    private Artist convertDtoToDao(ArtistDTO artistDTO) {
        modelMapper.typeMap(ArtistDTO.class, Artist.class)
                .addMappings(mapping -> mapping.skip(Artist::setAlbums))
                .addMappings(mapping -> mapping.skip(Artist::setTracks));
        return modelMapper.map(artistDTO, Artist.class);
    }

    private ArtistDTO convertDaoToDto(Artist artist) {
        return modelMapper.map(artist, ArtistDTO.class);
    }

}
