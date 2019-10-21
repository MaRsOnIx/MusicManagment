package me.marsonix.spotifyapitest.models.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode
@Builder
public class ArtistDTO {

    private String id;
    private String name;
    private String image;
    private String link;
    private int followers;
    private int popularity;


}
