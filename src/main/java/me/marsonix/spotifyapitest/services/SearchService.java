package me.marsonix.spotifyapitest.services;

import lombok.AllArgsConstructor;
import me.marsonix.spotifyapitest.models.spotify.Artist;
import me.marsonix.spotifyapitest.models.spotify.Container;
import me.marsonix.spotifyapitest.models.spotify.Search;
import me.marsonix.spotifyapitest.models.spotify.Track;
import me.marsonix.spotifyapitest.models.dtos.ArtistDTO;
import me.marsonix.spotifyapitest.models.dtos.ContainerArtistDTO;
import me.marsonix.spotifyapitest.models.dtos.ContainerTrackDTO;
import me.marsonix.spotifyapitest.models.dtos.TrackDTO;
import me.marsonix.spotifyapitest.utilis.SpotifyManager;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.stream.Collectors;


@Service
@AllArgsConstructor
public class SearchService {

    private SpotifyManager spotifyManager;
    private UserService userService;


    public ContainerTrackDTO getTracks(Search search) throws IOException {
        return getTracks(spotifyManager.getItem(search));
    }


    private ContainerTrackDTO getTracks(Container container){


        return ContainerTrackDTO.builder()
                .content(container.getContent())
                .total(container.getTotal())
                .nextUrl(container.getNextUrl())
                .previousUrl(container.getPreviousUrl())
                .items(
                        container.getItems().stream().map(v ->
                                trackToTransfer((Track) v))
                                .collect(Collectors.toList()))

                .build();
    }

    public TrackDTO trackToTransfer(Track v){
        boolean saved = userService.containsTrack(v);
        return TrackDTO.builder()
                .name(v.getName())
                .id(v.getId())
                .popularity(v.getPopularity()/10)
                .image(v.getImage())
                .link(v.getLink())
                .saved(saved)
                .artists(v.getArtists().stream()
                        .map(Artist::getName)
                        .collect(Collectors.joining(", "))
                ).build();
    }


    public ContainerArtistDTO getArtists(Search search) throws IOException {
        Container container = spotifyManager.getItem(search);

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
                                        .id(v.getId())
                                        .followers(v.getFollowers())

                                        .build()).collect(Collectors.toList()))

                .build();
    }

    public ContainerTrackDTO getTopTracks(String artistId) {
        return getTracks(spotifyManager.getTopTracksOfArtist(artistId));

    }




}
