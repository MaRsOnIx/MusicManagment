package me.marsonix.spotifyapitest;

import me.marsonix.spotifyapitest.models.spotify.Container;
import me.marsonix.spotifyapitest.models.spotify.Search;
import me.marsonix.spotifyapitest.models.spotify.Type;
import me.marsonix.spotifyapitest.utilis.SpotifyManager;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.util.Assert;

import java.io.IOException;

@SpringBootTest
class MarsonixApplicationTests {

    @Autowired
    private SpotifyManager spotifyManager;

    private Search createAnySearch(){

        return new Search("adele", Type.TRACK,10, 0);
    }

    private String getAnyArtistId(){
        return "7dGJo4pcD2V6oG8kP0tJRR";
    }

    @Test
    void gettingContainerFromTopListOfArtistNotNull(){
        Container container = spotifyManager.getTopTracksOfArtist(getAnyArtistId());
        Assert.notNull(container, "Testing getting data not null from Spotify Web API");
    }

    @Test
    void gettingContainerFromSearchingItemNotNull() throws IOException {
        Container container = spotifyManager.getItem(createAnySearch());
        Assert.notNull(container, "Testing getting data not null from Spotify Web API");
    }
    @Test
    void checkContainerFromSearchingItemWorking() throws IOException{
        Container container = spotifyManager.getItem(createAnySearch());
        Assert.isTrue(!container.getItems().isEmpty(), "Testing getting data from searching are true");
    }
    @Test
    void gettingContainerFromTopListOfArtistWroking(){
        Container container = spotifyManager.getTopTracksOfArtist(getAnyArtistId());
        Assert.isTrue(!container.getItems().isEmpty(), "Testing getting data are true");
    }

    // more tests in the future...

}
