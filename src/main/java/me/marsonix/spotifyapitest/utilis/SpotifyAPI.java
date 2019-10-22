package me.marsonix.spotifyapitest.utilis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.marsonix.spotifyapitest.exceptions.MissingPropertyException;
import me.marsonix.spotifyapitest.exceptions.SpotifyConnectionException;
import me.marsonix.spotifyapitest.exceptions.TrackNotFoundException;
import me.marsonix.spotifyapitest.models.spotify.*;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URLEncoder;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.USER_AGENT;

@Repository
public class SpotifyAPI {


    @Value("${security.oauth2.client.client-id}")
    private Optional<char[]> clientId;
    @Value("${security.oauth2.client.client-secret}")
    private Optional<char[]> secretId;
    @Value("${security.oauth2.client.access-token-uri}")
    private Optional<String> tokenUrl;




    private Optional<char[]> getBasicEncodedKey() throws MissingPropertyException {

        return Optional.of(Base64.getEncoder()
                .encodeToString(String.join("",
                        String.valueOf(clientId.orElseThrow(() ->
                                        new MissingPropertyException("Property {security.oauth2.client.client-id} is missing.")))
                        ,":",
                        String.valueOf(secretId.orElseThrow(()->
                                        new MissingPropertyException("Property {security.oauth2.client.client-secret} is missing.")))
                ).getBytes()).toCharArray());
    }

    private Optional<String> token = Optional.empty();

    private String generateNewToken() throws IOException {

        String code = null;
        try {
            code = String.valueOf(getBasicEncodedKey().get());
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = null;
        try {
            request = new HttpPost(tokenUrl.orElseThrow(() ->
                    new MissingPropertyException("Property {security.oauth2.client.access-token-uri} is missing.")));
        } catch (MissingPropertyException e) {
            e.printStackTrace();
        }

        request.addHeader("Authorization", "Basic " + code);
        request.addHeader("Accept", "application/json");
        request.setEntity(new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair("grant_type", "client_credentials"))));

        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        JsonNode jsonNode = new ObjectMapper().readTree(rd.readLine());

        return jsonNode.get("access_token").asText();
    }

    public Container getItem(Search search) throws IOException{
        return getItemsFroumUrl("https://api.spotify.com/v1/search?q=" +
                URLEncoder.encode(search.getContent(), "UTF-8") +
                "&type="+search.getType().name().toLowerCase()+"&limit=" +
                search.getLimit() +"&market=PL"+
                "&offset=" +
                search.getOffset(), search);
        }

   public Track getTrack(String id) throws IOException, TrackNotFoundException {
        return getTrackFromJson(getRespondedJson("https://api.spotify.com/v1/tracks/"+id));
    }


    public Container getTopTracksOfArtist(String artistId) throws IOException {
        return jsonTracksToContainer(getRespondedJson("https://api.spotify.com/v1/artists/"+artistId+"/top-tracks?country=PL"),"Top");
    }


    private AtomicInteger ai = new AtomicInteger();

    private JsonNode getRespondedJson(String url) throws IOException{
        if(!token.isPresent())token=Optional.of(generateNewToken());
        HttpClient client = HttpClientBuilder.create().build();
        HttpGet request = new HttpGet(url);

        request.addHeader("User-Agent", USER_AGENT);
        request.addHeader("Authorization", "Bearer " + token.get());
        request.addHeader("Accept", "application/json");
        HttpResponse response = client.execute(request);

        switch(response.getStatusLine().getStatusCode()){
            case 400:
            case 401:
                generateNewToken();
                if(ai.getAndIncrement()>=5) {
                    try {
                        throw new SpotifyConnectionException("Problem with connection of WEB API Spotify.");
                    } catch (SpotifyConnectionException e) {
                        e.printStackTrace();
                    }
                    return null;
                }
                return getRespondedJson(url);
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));

        return objectMapper.readTree(rd.lines().collect(Collectors.joining()));
    }

    private Container getItemsFroumUrl(String url, Search search) throws IOException {

        JsonNode json = getRespondedJson(url);
        if(json==null)return createStandardContainer("", objectMapper.createObjectNode(), search.getType());
        if(search.getType()==Type.TRACK) return jsonTracksToContainer(json, search.getContent());

        return jsonArtistsToContainer(json, search.getContent(), search.getType());
    }


    private Container createStandardContainer(String content, JsonNode node, Type type){
        Container container = new Container();
        container.setContent(content);
        container.setItems(new ArrayList<>());
        container.setNextUrl(node.hasNonNull("next")?node.get("next").asText():"null");
        container.setPreviousUrl(node.hasNonNull("previous")?node.get("previous").asText():"null");
        container.setTotal(node.hasNonNull("next")?node.get("total").asInt():-1);
        container.setType(type);
        return container;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

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



    private Container jsonArtistsToContainer(JsonNode json, String content, Type type)  {

        JsonNode artists = json.get("artists");
        Container container = createStandardContainer(content, artists, type);
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

                itemList.add(Artist.builder().id(id).image(image.orElse("")).name(name).followers(followers).link(link.orElse("")).popularity(popularity).link(link.orElse("")).build());

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
    return Optional.of("https://img.icons8.com/color/64/000000/gender-neutral-user.png");
    }











}
