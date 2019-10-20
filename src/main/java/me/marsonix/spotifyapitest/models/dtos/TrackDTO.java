package me.marsonix.spotifyapitest.models.dtos;

import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode
@Builder

public class TrackDTO {


        private String name;
        private String image;
        private String link;
        private String artists;
        private int popularity;






}
