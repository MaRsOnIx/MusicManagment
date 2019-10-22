package me.marsonix.spotifyapitest.models.user;



import javax.persistence.*;
import java.util.List;


@Entity
public class User {

    @Id
    private String id;
    @ElementCollection(targetClass=String.class)
    private List<String> farvoriteTracks;

    public User() {
    }

    public User(String id, List<String> farvoriteTracks) {
        this.id = id;
        this.farvoriteTracks = farvoriteTracks;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<String> getFarvoriteTracks() {
        return farvoriteTracks;
    }

    public void setFarvoriteTracks(List<String> farvoriteTracks) {
        this.farvoriteTracks = farvoriteTracks;
    }

    @Override
    public String   toString() {
        return "User{" +
                "id='" + id + '\'' +
                ", farvoriteTracks=" + farvoriteTracks +
                '}';
    }
}
