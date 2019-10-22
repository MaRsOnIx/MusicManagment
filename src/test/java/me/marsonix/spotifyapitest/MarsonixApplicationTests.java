package me.marsonix.spotifyapitest;

import me.marsonix.spotifyapitest.models.spotify.Container;
import me.marsonix.spotifyapitest.models.spotify.Item;
import me.marsonix.spotifyapitest.models.spotify.Search;
import me.marsonix.spotifyapitest.models.spotify.Type;
import me.marsonix.spotifyapitest.utilis.SpotifyAPI;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;

@SpringBootTest
class MarsonixApplicationTests {

    @Autowired
    private SpotifyAPI spotifyAPI;

    private Search createAnySearch(){

        return new Search("adele", Type.TRACK,10, 0);
    }

    private String getAnyArtistId(){
        return "7dGJo4pcD2V6oG8kP0tJRR";
    }

    @Test
    void gettingContainerFromTopListOfArtistNotNull(){
        Container container = null;
        try {
            container = spotifyAPI.getTopTracksOfArtist(getAnyArtistId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.notNull(container, "Testing getting data not null from Spotify Web API");
    }

    @Test
    void gettingContainerFromSearchingItemNotNull(){
        Container container = null;
        try {
            container = spotifyAPI.getItem(createAnySearch());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.notNull(container, "Testing getting data not null from Spotify Web API");
    }
    @Test
    void checkContainerFromSearchingItemWorking(){
        Container container = null;
        try {
            container = spotifyAPI.getItem(createAnySearch());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Assert.isTrue(!container.getItems().isEmpty(), "Testing getting data from searching are true");
    }
    @Test
    void gettingContainerFromTopListOfArtistWroking(){
        Container container = null;
        try {
            container = spotifyAPI.getTopTracksOfArtist(getAnyArtistId());
        } catch (IOException e) {
            e.printStackTrace();
        }
        Assert.isTrue(!container.getItems().isEmpty(), "Testing getting data are true");
    }

    // more tests in the future...

}
