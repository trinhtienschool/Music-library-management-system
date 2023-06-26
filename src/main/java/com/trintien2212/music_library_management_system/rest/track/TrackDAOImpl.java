package com.trintien2212.music_library_management_system.rest.track;

import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.EntityManager;

//@Repository
public class TrackDAOImpl implements TrackDAO{
    private EntityManager entityManager;

    @Autowired
    public TrackDAOImpl(EntityManager entityManager){
        this.entityManager = entityManager;
    }
    @Override
    public void deleteTrackByTrackId(Long trackId) {
        //delete track by trackId
//        entityManager.createQuery("DELETE FROM Track t WHERE t.trackId = :trackId")
//                .setParameter("trackId", trackId)
//                .executeUpdate();
    }

    @Override
    public void deleteTrackIdInPlaylistTrackTable(Long Id) {
        //delete trackId in playlist_track table
//        entityManager.createQuery("DELETE FROM PlaylistTrack pt WHERE pt.track.trackId = :trackId")
//                .setParameter("trackId", Id)
//                .executeUpdate();
    }
}
