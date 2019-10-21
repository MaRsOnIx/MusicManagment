package me.marsonix.spotifyapitest.models.spotify;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
@Builder
public class Track implements Item {

    private String id;
    private String name;
    private String image;
    private String link;
    private List<Artist> artists;
    private int popularity;



}
