package me.marsonix.spotifyapitest.repo;

import me.marsonix.spotifyapitest.models.spotify.Track;
import me.marsonix.spotifyapitest.models.user.User;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public class UserRepository {

    private List<User> users = new ArrayList<>();


    public List<Track> findFarvoriteTracksById(String id){
        try {
        return users.stream().filter(u -> u.getId().equals(id)).findAny().orElseThrow(RuntimeException::new).getFarvoriteTracks();
        }catch(RuntimeException e){
            return new ArrayList<>();
        }
    }

    public User findById(String id){

        return users.stream().filter(v -> v.getId().equals(id)).findAny().orElse(createNew(id));

    }
    private User createNew(String id){
        User u = new User(id, new ArrayList<>());
        users.add(u);
        return u;
    }


}
