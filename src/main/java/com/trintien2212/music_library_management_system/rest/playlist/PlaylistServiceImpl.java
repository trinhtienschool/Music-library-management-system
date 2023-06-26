package com.trintien2212.music_library_management_system.rest.playlist;

import com.trintien2212.music_library_management_system.rest.album.AlbumRepository;
import com.trintien2212.music_library_management_system.rest.artist.ArtistRepository;
import com.trintien2212.music_library_management_system.rest.config.ConverterConfig;
import com.trintien2212.music_library_management_system.rest.exception.BadRequestException;
import com.trintien2212.music_library_management_system.rest.exception.ResourceExistedException;
import com.trintien2212.music_library_management_system.rest.exception.ResourceNotFoundException;
import com.trintien2212.music_library_management_system.rest.exception.UnAuthorizedException;
import com.trintien2212.music_library_management_system.rest.model.dto.PlaylistDTO;
import com.trintien2212.music_library_management_system.rest.model.dto.TrackDTO;
import com.trintien2212.music_library_management_system.rest.model.entity.*;
import com.trintien2212.music_library_management_system.rest.track.TrackRepository;
import com.trintien2212.music_library_management_system.rest.auth.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
public class PlaylistServiceImpl implements PlaylistService {

    private PlaylistRepository playlistRepository;
    private ArtistRepository artistRepository;
    private AlbumRepository albumRepository;
    private ModelMapper modelMapper;
    private ConverterConfig converter;
    private TrackRepository trackRepository;
    private UserRepository userRepository;

    @Autowired
    public PlaylistServiceImpl(PlaylistRepository playlistRepository, ModelMapper modelMapper,
                               ArtistRepository artistRepository, AlbumRepository albumRepository,
                               ConverterConfig converter, TrackRepository trackRepository, UserRepository userRepository){
        this.playlistRepository = playlistRepository;
        this.artistRepository = artistRepository;
        this.albumRepository = albumRepository;
        this.modelMapper = modelMapper;
        this.converter = converter;
        this.trackRepository = trackRepository;
        this.userRepository = userRepository;
    }

    @Override
    @Transactional
    public PlaylistDTO save(PlaylistDTO playlistDTO) {
        if(playlistDTO.getPlaylistId() !=null){
            playlistRepository.findById(playlistDTO.getPlaylistId())
                       .ifPresent(playlist -> {
                           throw
                                   new ResourceExistedException("Playlist has id: "+playlistDTO.getPlaylistId()+" has already existed!");});
        }

            playlistRepository.findPlaylistByPlaylistNameAndUser_UserId(playlistDTO.getPlaylistName(), getCurrentUser().getUserId())
                    .ifPresent(playlist -> {
                        throw
                                new ResourceExistedException("Playlist has name: "+playlistDTO.getPlaylistName()+" has already existed!");});
        //check tracks not null in playlistDTO and check trackId in tracks not null
        //then check if trackId in tracks existed not in database throw exception
        checkTracksExisted(playlistDTO);
        return convertPlaylistEntityToDTO(playlistRepository.save(convertPlaylistDTOToEntity(playlistDTO)));
    }
    private User getCurrentUser(){
        if(converter.getLoginUser() !=null){
            return userRepository.findUserByUsername(converter.getLoginUser().getUsername());
        }else throw new UnAuthorizedException("You must login to perform this action!");
    }
    @Override
    public List<PlaylistDTO> findAll() {
        return playlistRepository.findAll().stream()
                          .map(playlist -> convertPlaylistEntityToDTO(playlist))
                          .collect(Collectors.toList());
    }

    @Override
    public PlaylistDTO findById(long id) {
        return playlistRepository.findById(id).map(playlist -> convertPlaylistEntityToDTO(playlist))
                .orElseThrow(() -> new ResourceNotFoundException("Not found Playlist has id: "+id));
    }


    //before
    @Override
    public List<PlaylistDTO> findPlaylistsByPlaylistName(String name) {
        return playlistRepository.findPlaylistsByPlaylistName(name).stream()
                .map(playlist -> convertPlaylistEntityToDTO(playlist))
                .collect(Collectors.toList());
    }


    @Override
    public List<PlaylistDTO> searchPlaylistsByKeyword(String keyword) {
        return playlistRepository.findPlaylistsByTracks_TrackTitleContainsOrTracks_Artist_ArtistNameContainsOrTracks_Album_AlbumNameContainsOrTracks_GenreContains(keyword, keyword, keyword, keyword).stream()
                .map(playlist -> convertPlaylistEntityToDTO(playlist))
                .collect(Collectors.toList());
    }
    @Override
    public List<PlaylistDTO> searchPlaylistsByMultipleCriteria(String trackTitle, String artistName, String albumName, String genre) {
        return playlistRepository.findPlaylistsByTracks_TrackTitleContainsAndTracks_Artist_ArtistNameContainsAndTracks_Album_AlbumNameContainsAndTracks_GenreContains(trackTitle, artistName, albumName, genre).stream()
                .map(playlist -> convertPlaylistEntityToDTO(playlist))
                .collect(Collectors.toList());
    }
    @Override
    public List<TrackDTO> getTracksByPlaylistId(Long playlistId) {
        return playlistRepository.findTracksByPlaylistId(playlistId).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackDTO> getTracksByPlaylistName(String playlistName) {
        return playlistRepository.findTracksByPlaylistName(playlistName).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }


    @Override
    public List<TrackDTO> getTracksByPlaylistIdAndMultipleCriteria(Long playlistId, String trackTitle, String artistName, String albumName, String genre){
        return playlistRepository.findTracksByPlaylistIdAndTracks_TrackTitleContainsAndTracks_Artist_ArtistNameContainsAndTracks_Album_AlbumNameContainsAndTracks_GenreContains(playlistId, trackTitle, artistName, albumName, genre).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }

    @Override
    public List<TrackDTO> getTracksByPlaylistIdAndKeyword(Long playlistId, String keyword) {
        return playlistRepository.findTracksByPlaylistIdAndTracks_TrackTitleContainsOrTracks_Artist_ArtistNameContainsOrTracks_Album_AlbumNameContainsOrTracks_GenreContains(playlistId, keyword).stream()
                .map(track -> converter.convertTrackEntityToDTO(track))
                .collect(Collectors.toList());
    }


    @Override
    @Transactional
    public PlaylistDTO update(PlaylistDTO playlistDTO) {
        return playlistRepository.findById(playlistDTO.getPlaylistId())
                   .map(playlist -> {

                               checkOwner(playlist, "You are not authorized to update this playlist!");
                               //check tracks not null in playlistDTO and check trackId in tracks not null
                               //then check if trackId in tracks existed not in database throw exception else return List<Track>
                               checkTracksExisted(playlistDTO);

                               return convertPlaylistEntityToDTO(playlistRepository.save(convertPlaylistDTOToEntity(playlistDTO)));
                   }
                   )
                   .orElseThrow(() -> new ResourceNotFoundException("Not found Playlist has id: "+playlistDTO.getPlaylistId()));

    }

    //check tracks not null in playlistDTO and check trackId in tracks not null
    //then check if trackId in tracks existed not in database throw exception else return List<Track>
    private List<Track> checkTracksExisted(PlaylistDTO playlistDTO) {
        //if tracks null in playlistDTO return null
        if(playlistDTO.getTracks() == null) return null;

        //if tracks not null in playlistDTO and check trackId in tracks not null
        //then check if trackId in tracks existed not in database throw exception else return List<Track>
        return playlistDTO.getTracks().stream()
                .map(trackDTO -> {
                    if(trackDTO.getTrackId() == null) throw new BadRequestException("Not found TrackId in Tracks in Playlist in request");
                    return trackRepository.findById(trackDTO.getTrackId()).orElseThrow(() -> new ResourceNotFoundException("Not found Track has id: "+trackDTO.getTrackId()));
                })
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public void delete(long id) {
        playlistRepository.findById(id).ifPresentOrElse(
                (playlist) -> {
                    checkOwner(playlist, "You are not authorized to delete this playlist!");

                    playlistRepository.delete(playlist);},
                () -> {throw new ResourceNotFoundException("Not found Playlist has id: "+id);});
    }

    @Override
    public PlaylistDTO addTracksToPlaylist(long playlistId, PlaylistDTO playlistDTO) {

        playlistDTO.setPlaylistId(playlistId);
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResourceNotFoundException("Not found Playlist has id: "+playlistId));

        checkOwner(playlist, "You are not authorized to add tracks tp this playlist!");

        //get track entities from tracks list in playlistDTO from database
        List<Track> tracks = checkTracksExisted(playlistDTO);

        if(tracks ==null)  throw new BadRequestException("Not found Tracks in Playlist in request");

        //add tracks to playlist
        playlist.getTracks().addAll(tracks);

        //save playlist
        return convertPlaylistEntityToDTO(playlistRepository.save(playlist));

    }

    @Override
    public PlaylistDTO removeTracksFromPlaylist(long playlistId, PlaylistDTO playlistDTO) {
        Playlist playlist = playlistRepository.findById(playlistId).orElseThrow(() -> new ResourceNotFoundException("Not found Playlist has id: "+playlistId));

        checkOwner(playlist, "You are not authorized to add tracks tp this playlist!");
        //get track entities from tracks list in playlistDTO from database
        List<Track> tracks = checkTracksExisted(playlistDTO);

        if(tracks ==null) throw new BadRequestException("Not found Tracks in Playlist in request");

        //remove tracks from playlist
        playlist.getTracks().removeAll(tracks);

        //save playlist
        return convertPlaylistEntityToDTO(playlistRepository.save(playlist));

    }

    private void checkOwner(Playlist playlist, String message) {
        if (playlist.getUser().getUserId() != getCurrentUser().getUserId())
            throw new UnAuthorizedException(message);
    }


    private Playlist convertPlaylistDTOToEntity(PlaylistDTO playlistDTO){
        modelMapper.typeMap(PlaylistDTO.class, Playlist.class)
                .addMappings(mapping -> mapping.skip(Playlist::setUser));
        Playlist playlist = modelMapper.map(playlistDTO, Playlist.class);
        if( getCurrentUser().getUserId() != null) {
            User user = new User();
            user.setUserId(getCurrentUser().getUserId());
            playlist.setUser(user);
        }else throw new BadRequestException("Missing the User Id field");
        return playlist;
    }

    private PlaylistDTO convertPlaylistEntityToDTO(Playlist playlist){
        modelMapper.typeMap(Playlist.class, PlaylistDTO.class);
        PlaylistDTO playlistDTO = modelMapper.map(playlist, PlaylistDTO.class);
        return playlistDTO;
    }

}
