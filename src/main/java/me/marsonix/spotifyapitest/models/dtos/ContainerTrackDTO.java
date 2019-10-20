package me.marsonix.spotifyapitest.models.dtos;

import lombok.Builder;
import lombok.Data;


import java.util.List;

@Data
@Builder
public class ContainerTrackDTO {

    private String content;
    private String previousUrl;
    private String nextUrl;
    private int total;

    private List<TrackDTO> items;

}
