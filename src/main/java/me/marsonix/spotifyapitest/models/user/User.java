package me.marsonix.spotifyapitest.models.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import me.marsonix.spotifyapitest.models.spotify.Track;
import java.util.List;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class User {


    private String id;
    private List<Track> farvoriteTracks;


}
