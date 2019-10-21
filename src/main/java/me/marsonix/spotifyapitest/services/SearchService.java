package me.marsonix.spotifyapitest.services;

import me.marsonix.spotifyapitest.models.Artist;
import me.marsonix.spotifyapitest.models.Container;
import me.marsonix.spotifyapitest.models.Search;
import me.marsonix.spotifyapitest.models.Track;
import me.marsonix.spotifyapitest.models.dtos.ArtistDTO;
import me.marsonix.spotifyapitest.models.dtos.ContainerArtistDTO;
import me.marsonix.spotifyapitest.models.dtos.ContainerTrackDTO;
import me.marsonix.spotifyapitest.models.dtos.TrackDTO;
import me.marsonix.spotifyapitest.utilis.SpotifyAPI;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;


@Service
public class SearchService {

    @Autowired
    private SpotifyAPI spotifyAPI;

    // I see that it should be refactored but my time is limited, it will be done in the future

    public ContainerTrackDTO getTracks(Search search) throws IOException{
        Container container = spotifyAPI.getItem(search);
//        spotifyAPI.getTrack("3n3Ppam7vgaVa1iaRUc9Lp");
        return ContainerTrackDTO.builder()
                .content(container.getContent())
                .total(container.getTotal())
                .nextUrl(container.getNextUrl())
                .previousUrl(container.getPreviousUrl())
                .items(
                       container.getItems().stream().map(val -> (Track) val)
                               .map(v -> TrackDTO.builder()
                                       .name(v.getName())
                                       .popularity(v.getPopularity()/10)
                                       .image(v.getImage())
                                       .link(v.getLink())
                                       .artists(v.getArtists().stream()
                                               .map(Artist::getName)
                                               .collect(Collectors.joining(", "))
                                       )
                                       .build()).collect(Collectors.toList()))

                .build();
    }

    public ContainerArtistDTO getArtists(Search search) throws IOException{
        Container container = spotifyAPI.getItem(search);

        return ContainerArtistDTO.builder()
                .content(container.getContent())
                .total(container.getTotal())
                .nextUrl(container.getNextUrl())
                .previousUrl(container.getPreviousUrl())
                .items(
                        container.getItems().stream().map(val -> (Artist) val)
                                .map(v -> ArtistDTO.builder()
                                        .name(v.getName())
                                        .popularity(v.getPopularity()/10)
                                        .image(v.getImage())
                                        .link(v.getLink())
                                        .followers(v.getFollowers())

                                        .build()).collect(Collectors.toList()))

                .build();
    }




}
