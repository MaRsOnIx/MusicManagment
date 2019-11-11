package me.marsonix.spotifyapitest.utilis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.marsonix.spotifyapitest.exceptions.SpotifyConnectionException;
import me.marsonix.spotifyapitest.exceptions.TrackNotFoundException;
import me.marsonix.spotifyapitest.models.spotify.*;
import me.marsonix.spotifyapitest.utilis.spotify.http.AbstractPreparedRequest;
import me.marsonix.spotifyapitest.utilis.spotify.http.GetRequest;
import me.marsonix.spotifyapitest.utilis.spotify.http.Response;
import me.marsonix.spotifyapitest.utilis.spotify.token.BasicToken;
import me.marsonix.spotifyapitest.utilis.spotify.token.BearerToken;
import me.marsonix.spotifyapitest.utilis.spotify.token.Token;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class SpotifyManager {


    @Value("${security.oauth2.client.client-id}")
    private char[] clientId;
    @Value("${security.oauth2.client.client-secret}")
    private char[] secretId;
    @Value("${security.oauth2.client.access-token-uri}")
    private String tokenUrl;

    private ObjectMapper objectMapper = new ObjectMapper();
    private Token basicToken;
    private Token bearerToken;


    private BasicToken getBasicToken() {
        if(basicToken==null)
            basicToken=new BasicToken(clientId, secretId);
        return (BasicToken) basicToken;
    }
    private void generateNewToken() {
        bearerToken=new BearerToken(getBasicToken(), tokenUrl, objectMapper);
    }

    public Container getItem(Search search) throws IOException {
        return getItemsFroumUrl("https://api.spotify.com/v1/search?q=" +
                URLEncoder.encode(search.getContent(), "UTF-8") +
                "&type="+search.getType().name().toLowerCase()+"&limit=" +
                search.getLimit() +"&market=PL"+
                "&offset=" +
                search.getOffset(), search);
        }

   public Track getTrack(String id) throws TrackNotFoundException {
        return getTrackFromJson(Objects.requireNonNull(
                getRespondedJson("https://api.spotify.com/v1/tracks/" + id)));
    }

    public Container getTopTracksOfArtist(String artistId) {
        return jsonTracksToContainer(Objects.requireNonNull(
                getRespondedJson("https://api.spotify.com/v1/artists/" + artistId + "/top-tracks?country=PL"))
                ,"Top");
    }

    private AtomicInteger ai = new AtomicInteger();

    private boolean checkSpotifyKeys(){
        if(!bearerToken.get().isPresent()) try {
            throw new SpotifyConnectionException("ClientId or SecredId in application.properties is wrong," +
                    " you have to check this data and correct them.");
        } catch (SpotifyConnectionException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private JsonNode getRespondedJson(String url) {
        if(bearerToken==null || !bearerToken.get().isPresent()) generateNewToken();
        if(!checkSpotifyKeys()) return null;
        GetRequest getRequest = new GetRequest(url,
                AbstractPreparedRequest.Type.BEARER, bearerToken, objectMapper);
        Response response;
        try {
            response = getRequest.get();
        } catch (IOException |SpotifyConnectionException e) {
            if(ai.getAndIncrement()>3){
                e.printStackTrace();
                return null;
            }
            generateNewToken();
            return getRespondedJson(url);
        }
        if(ai.get()!=0) ai.set(0);
        return response.getData();
    }

    private Container getItemsFroumUrl(String url, Search search) {

        JsonNode json = getRespondedJson(url);
        if(json==null)return createStandardContainer("", objectMapper.createObjectNode(), search.getType());
        if(search.getType()==Type.TRACK) return jsonTracksToContainer(json, search.getContent());

        return jsonArtistsToContainer(json, search.getContent());
    }

    private Container createStandardContainer(String content, JsonNode node, Type type){
        Container container = new Container();
        container.setContent(content);
        container.setItems(new ArrayList<>());
        container.setNextUrl(node.hasNonNull("next")?node.get("next").asText():"null");
        container.setPreviousUrl(node.hasNonNull("previous")?node.get("previous").asText():"null");
        container.setTotal(node.hasNonNull("total")?node.get("total").asInt():-1);
        container.setType(type);
        return container;
    }

    private Container jsonTracksToContainer(JsonNode json, String content) {

        JsonNode tracks = json.get("tracks");
        Container container = createStandardContainer(content, tracks, Type.TRACK);
        int total = container.getTotal();
        List<Item> itemList = container.getItems();

        if(total!=0){
            JsonNode items = total==-1?tracks:tracks.get("items");

            for (JsonNode nextValue : items) {
                try {
                    itemList.add(getTrackFromJson(nextValue));
                }catch (TrackNotFoundException ev){
                    ev.printStackTrace();
                }
            }
        }
        return container;

    }

    private Track getTrackFromJson(JsonNode nextValue) throws TrackNotFoundException {
        if(!nextValue.hasNonNull("id"))throw new TrackNotFoundException("Track doesn't find.");
        String id = nextValue.get("id").asText();
        String name = nextValue.get("name").asText();
        int popularity = nextValue.get("popularity").asInt();
        String link = nextValue.get("preview_url").asText();

        Optional<String> image = Optional.empty();
        List<Artist> artists = new ArrayList<>();

        if (nextValue.hasNonNull("artists") && nextValue.get("artists").isArray()) {
            for (JsonNode nextArt : nextValue.get("artists")) {
                String idArt = nextArt.get("id").asText();
                String nameArt = nextArt.get("name").asText();
                artists.add(Artist.builder().id(idArt).name(nameArt).build());
            }
        }
        if (nextValue.hasNonNull("album")) {
            JsonNode album = nextValue.get("album");
            image=getImageFromJson(album);
        }
        return Track.builder().id(id).image(image.orElse(""))
                .name(name).artists(artists).link(link)
                .popularity(popularity).build();
    }

    private Container jsonArtistsToContainer(JsonNode json, String content)  {

        JsonNode artists = json.get("artists");
        Container container = createStandardContainer(content, artists, Type.ARTIST);
        int total = container.getTotal();
        List<Item> itemList = container.getItems();

        if(total>0){
            JsonNode items = artists.get("items");

            for (JsonNode nextValue : items) {
                String id = nextValue.get("id").asText();
                String name = nextValue.get("name").asText();
                int popularity = nextValue.get("popularity").asInt();
                int followers = 0;
                Optional<String> image = getImageFromJson(nextValue);
                Optional<String> link = Optional.empty();
                if(nextValue.hasNonNull("external_urls") && nextValue.get("external_urls").hasNonNull("spotify")){
                    link=Optional.ofNullable(nextValue.get("external_urls").get("spotify").asText());
                }
                if(nextValue.hasNonNull("followers") && nextValue.get("followers").hasNonNull("total")){
                    followers = nextValue.get("followers").get("total").asInt();
                }

                itemList.add(Artist.builder().id(id).image(image.orElse(""))
                        .name(name).followers(followers).link(link.orElse(""))
                        .popularity(popularity).link(link.orElse("")).build());
            }
        }

        return container;
    }

    private Optional<String> getImageFromJson(JsonNode json){
        if (json.hasNonNull("images") && json.get("images").isArray()) {
            for (JsonNode nextImg : json.get("images")) {
                if (nextImg.get("height").intValue() < 400) {
                    return Optional.ofNullable(nextImg.get("url").asText());

                }
            }
        }
    return Optional.of("web/profile.png");
    }
}
