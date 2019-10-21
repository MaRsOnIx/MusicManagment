package me.marsonix.spotifyapitest.services;

import me.marsonix.spotifyapitest.exceptions.MissingPropertyException;
import me.marsonix.spotifyapitest.exceptions.TrackNotFoundException;
import me.marsonix.spotifyapitest.models.spotify.Track;
import me.marsonix.spotifyapitest.models.user.User;
import me.marsonix.spotifyapitest.repo.UserRepository;
import me.marsonix.spotifyapitest.utilis.SpotifyAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;

@Service
public class UserService {

    @Autowired
    private SpotifyAPI spotifyAPI;
    @Autowired
    private UserRepository userRepository;

    public List<Track>getFarvoritesTracks(String id){
        return userRepository.findFarvoriteTracksById(id);
    }
    public boolean addNewTrack(String trackId){
        try {
            User user = userRepository.findById(getUserId());
            Track track = spotifyAPI.getTrack(trackId);
            return user.getFarvoriteTracks().contains(track)
                    ? false : user.getFarvoriteTracks().add(track);
        } catch (IOException | TrackNotFoundException | MissingPropertyException e) {
        }
        return false;
    }

    public boolean deleteTrack(String trackId){
        try {
            User user = userRepository.findById(getUserId());
            Track track = spotifyAPI.getTrack(trackId);
            return user.getFarvoriteTracks().contains(track)
                    ?  user.getFarvoriteTracks().remove(track) : false;
        } catch (IOException | TrackNotFoundException | MissingPropertyException e) {
        }
        return false;
    }
    public boolean containsTrack(Track track){
       return userRepository.findById(getUserId()).getFarvoriteTracks().contains(track);
    }

    public String getUserId(){
        LinkedHashMap details = (LinkedHashMap) (((OAuth2Authentication) (SecurityContextHolder
                .getContext())
                .getAuthentication())
                .getUserAuthentication())
                .getDetails();
        return details.values().toArray()[7].toString();
    }

}
