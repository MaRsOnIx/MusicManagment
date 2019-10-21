package me.marsonix.spotifyapitest.services;

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
    SpotifyAPI spotifyAPI;
    @Autowired
    UserRepository userRepository;

    public List<Track>getFarvoritesTracks(String id){
        return userRepository.findFarvoriteTracksById(id);
    }
    public boolean addNewTrack(String trackId, String userId){
        try {
            User user = userRepository.findById(userId);
            Track track = spotifyAPI.getTrack(trackId);
            return user.getFarvoriteTracks().contains(track)
                    ? false : user.getFarvoriteTracks().add(track);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteTrack(String trackId, String userId){
        try {
            User user = userRepository.findById(userId);
            Track track = spotifyAPI.getTrack(trackId);
            return user.getFarvoriteTracks().contains(track)
                    ?  user.getFarvoriteTracks().remove(track) : false;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
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
