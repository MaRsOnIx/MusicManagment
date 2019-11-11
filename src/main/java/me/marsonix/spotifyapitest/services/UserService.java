package me.marsonix.spotifyapitest.services;

import lombok.AllArgsConstructor;
import me.marsonix.spotifyapitest.exceptions.TrackNotFoundException;
import me.marsonix.spotifyapitest.models.spotify.Track;
import me.marsonix.spotifyapitest.models.user.User;
import me.marsonix.spotifyapitest.repo.UserRepository;
import me.marsonix.spotifyapitest.utilis.SpotifyManager;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.OAuth2Authentication;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class UserService {

    private SpotifyManager spotifyManager;
    private UserRepository userRepository;

    public List<Track>getFarvoritesTracks(String id){
        User user;
        if(userRepository.existsById(getUserId())){
            user=userRepository.findById(getUserId()).get();
        }else{
            user=createNewUser();
        }
        return user.getFarvoriteTracks().stream().map(this::stringToTrack).collect(Collectors.toList());

    }

    private Track stringToTrack(String stringToTrack) {
        try {
            return spotifyManager.getTrack(stringToTrack);
        } catch (TrackNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean addNewTrack(String trackId){
        User user;
        if(userRepository.existsById(getUserId())){
            user=userRepository.findById(getUserId()).get();
        }else{
            user=createNewUser();
        }
            if(user.getFarvoriteTracks().contains(trackId)) return false;

            user.getFarvoriteTracks().add(trackId);
            userRepository.save(user);
            return true;

    }

    public boolean deleteTrack(String trackId){
        User user;
        if(userRepository.existsById(getUserId())){
            user=userRepository.findById(getUserId()).get();
        }else{
            user=createNewUser();
        }
        if(!user.getFarvoriteTracks().contains(trackId)) return false;
        user.getFarvoriteTracks().remove(trackId);
        userRepository.save(user);
        return true;
    }

    private User createNewUser(){
        User user = new User(getUserId(), new ArrayList<>());
        userRepository.save(user);
        return user;
    }

    boolean containsTrack(Track track){
        User user;
        if(userRepository.existsById(getUserId())){
            user=userRepository.findById(getUserId()).get();
        }else{
            user=createNewUser();
        }
       return user.getFarvoriteTracks().contains(track.getId());
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
