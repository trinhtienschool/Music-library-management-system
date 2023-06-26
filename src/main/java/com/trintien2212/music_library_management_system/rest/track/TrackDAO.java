package com.trintien2212.music_library_management_system.rest.track;


public interface TrackDAO {
    void deleteTrackByTrackId(Long Id);
    void deleteTrackIdInPlaylistTrackTable(Long Id);

}
