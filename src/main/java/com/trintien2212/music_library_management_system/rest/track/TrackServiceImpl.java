package com.trintien2212.music_library_management_system.rest.track;

import com.trintien2212.music_library_management_system.rest.album.AlbumRepository;
import com.trintien2212.music_library_management_system.rest.artist.ArtistRepository;
import com.trintien2212.music_library_management_system.rest.config.ConverterConfig;
import com.trintien2212.music_library_management_system.rest.exception.BadRequestException;
import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;
import com.trintien2212.music_library_management_system.rest.model.entity.*;
import com.trintien2212.music_library_management_system.rest.model.entity.Track;
import com.trintien2212.music_library_management_system.rest.exception.ResourceExistedException;
import com.trintien2212.music_library_management_system.rest.exception.ResourceNotFoundException;
import com.trintien2212.music_library_management_system.rest.playlist.PlaylistRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class TrackServiceImpl implements TrackService {

    private TrackRepository trackRepository;
    private ArtistRepository artistRepository;
    private AlbumRepository albumRepository;
    private ModelMapper modelMapper;
    private ConverterConfig converter;

    private PlaylistRepository playlistRepository;

    @Autowired
    public TrackServiceImpl(TrackRepository trackRepository, ModelMapper modelMapper,
                            ArtistRepository artistRepository, AlbumRepository albumRepository, ConverterConfig converter, PlaylistRepository playlistRepository){
        this.trackRepository = trackRepository;
        this.modelMapper = modelMapper;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.converter = converter;
        this.playlistRepository = playlistRepository;
    }

    @Override
    @Transactional
    public TrackDTO save(TrackDTO trackDTO) {
        if(trackDTO.getTrackId() !=null){
            trackRepository.findById(trackDTO.getTrackId())
                       .ifPresent(track -> {
                           throw
                                   new ResourceExistedException("Track has id: "+trackDTO.getTrackId()+" has already existed!");});
        }
        return converter.convertTrackEntityToDTO(trackRepository.save(convertDtoToDao(trackDTO)));
    }

    @Override
    public List<TrackDTO> findAll() {
        return trackRepository.findAll().stream()
                          .map(track -> converter.convertTrackEntityToDTO(track))
                          .collect(Collectors.toList());
    }

    @Override
    public TrackDTO findById(long id) {
        return trackRepository.findById(id).map(track -> converter.convertTrackEntityToDTO(track))
                .orElseThrow(() -> new ResourceNotFoundException("Not found Track has id: "+id));
    }

    @Override
    public List<TrackDTO> findByTrackTitle(String title) {
        return trackRepository.findTracksByTrackTitle(title).stream()
                          .map(track -> converter.convertTrackEntityToDTO(track))
                          .collect(Collectors.toList());
    }

    @Override
    public List<TrackDTO> findByArtistName(String artistName) {
        return trackRepository.findTrackByArtist_ArtistNameContains(artistName).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackDTO> findByAlbumName(String albumName) {
        return trackRepository.findTrackByAlbum_AlbumNameContains(albumName).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }
    //find by keyword

    @Override
    public List<TrackDTO> findByKeyword(String keyword) {
        List<Track> tracks = trackRepository.findTracksByTrackTitleContainsOrArtist_ArtistNameContainsOrAlbum_AlbumNameContains(keyword, keyword, keyword);
        return trackRepository.findTracksByTrackTitleContainsOrArtist_ArtistNameContainsOrAlbum_AlbumNameContains(keyword, keyword, keyword).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }
    @Override
    @Transactional
    public TrackDTO update(TrackDTO trackDTO) {
        return trackRepository.findById(trackDTO.getTrackId())
                   .map(track -> converter.convertTrackEntityToDTO(trackRepository.save(convertDtoToDao(trackDTO)))
                   )
                   .orElseThrow(() -> new ResourceNotFoundException("Not found Track has id: "+trackDTO.getTrackId()));

    }
    @Override
    @Transactional
    public void delete(long id) {

        trackRepository.findById(id).ifPresentOrElse(
                (track) -> {
                    for(Playlist playlist: track.getPlaylists()){
                        playlist.getTracks().remove(track);
                        playlistRepository.save(playlist);
                    }
                    trackRepository.delete(track);},
                () -> {throw new ResourceNotFoundException("Not found Track has id: "+id);});
    }

    private Track convertDtoToDao(TrackDTO trackDTO){
        modelMapper.typeMap(TrackDTO.class, Track.class);
        Track track = modelMapper.map(trackDTO, Track.class);
        if( trackDTO.getArtist() != null && trackDTO.getArtist().getArtistId() != null) {
            //check if artist existed
            Artist artist = artistRepository.findById(trackDTO.getArtist().getArtistId())
                    .orElseThrow(() -> new ResourceNotFoundException("Artist has id "+trackDTO.getArtist().getArtistId()+" not found!"));
            track.setArtist(artist);
        }else throw new BadRequestException("Missing the Artist Id field");
        if(trackDTO.getAlbum()!= null && track.getAlbum().getAlbumId() !=null) {
            //check if album existed
            Album album = albumRepository.findById(trackDTO.getAlbum().getAlbumId())
                    .orElseThrow(() -> new ResourceNotFoundException("Album has id "+trackDTO.getAlbum().getAlbumId()+" not found!"));
            track.setAlbum(album);
        }else throw new BadRequestException("Missing the Album Id field");
        return track;
    }

//    public TrackDTO converter.convertTrackEntityToDTO(Track track){
//        modelMapper.typeMap(Track.class, TrackDTO.class);
//        TrackDTO trackDTO = modelMapper.map(track, TrackDTO.class);
//        trackDTO.setDuration(Duration.parse(track.getDuration()));
//        return trackDTO;
//    }

}
