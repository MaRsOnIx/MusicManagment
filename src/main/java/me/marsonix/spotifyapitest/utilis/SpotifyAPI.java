package me.marsonix.spotifyapitest.utilis;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.marsonix.spotifyapitest.exceptions.MissingPropertyException;
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

    private String generateNewToken() throws IOException, MissingPropertyException {

        String code = String.valueOf(getBasicEncodedKey().get());
        CloseableHttpClient client = HttpClients.createDefault();
        HttpPost request = new HttpPost(tokenUrl.orElseThrow(() ->
                new MissingPropertyException("Property {security.oauth2.client.access-token-uri} is missing.")));

        request.addHeader("Authorization", "Basic " + code);
        request.addHeader("Accept", "application/json");
        request.setEntity(new UrlEncodedFormEntity(Collections.singletonList(new BasicNameValuePair("grant_type", "client_credentials"))));

        HttpResponse response = client.execute(request);
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        JsonNode jsonNode = new ObjectMapper().readTree(rd.readLine());

        return jsonNode.get("access_token").asText();
    }

    public Container getItem(Search search) throws IOException, MissingPropertyException {
        return getItemsFroumUrl("https://api.spotify.com/v1/search?q=" +
                URLEncoder.encode(search.getContent(), "UTF-8") +
                "&type="+search.getType().name().toLowerCase()+"&limit=" +
                search.getLimit() +"&market=PL"+
                "&offset=" +
                search.getOffset(), search);
        }

   public Track getTrack(String id) throws IOException, TrackNotFoundException, MissingPropertyException {
        return getTrackFromJson(getRespondedJson("https://api.spotify.com/v1/tracks/"+id));
    }



    private JsonNode getRespondedJson(String url) throws IOException, MissingPropertyException {
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
                return getRespondedJson(url);
        }
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        return objectMapper.readTree(rd.lines().collect(Collectors.joining()));
    }

    private Container getItemsFroumUrl(String url, Search search) throws IOException, MissingPropertyException {


        if(search.getType()==Type.TRACK) return jsonTracksToContainer(getRespondedJson(url), search.getContent(), search.getType());

        return jsonArtistsToContainer(getRespondedJson(url), search.getContent(), search.getType());
    }


    private Container createStandardContainer(String content, JsonNode node, Type type){
        Container container = new Container();
        container.setContent(content);
        container.setItems(new ArrayList<>());
        container.setNextUrl(Optional.ofNullable(node.get("next").asText()).orElse("null"));
        container.setPreviousUrl(Optional.ofNullable(node.get("previous").asText()).orElse("null"));
        container.setTotal(node.get("total").asInt());
        container.setType(type);
        return container;
    }

    private ObjectMapper objectMapper = new ObjectMapper();

    private Container jsonTracksToContainer(JsonNode json, String content, Type type) {

        JsonNode tracks = json.get("tracks");
        Container container = createStandardContainer(content, tracks, type);
        int total = container.getTotal();
        List<Item> itemList = container.getItems();

        if(total>0){
            JsonNode items = tracks.get("items");

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
        String id = Optional.ofNullable(nextValue.get("id").asText()).orElseThrow( () -> new TrackNotFoundException("Track doesn't find.") );
        Optional<String> name = Optional.ofNullable(nextValue.get("name").asText());
        Optional<Integer> popularity = Optional.of(nextValue.get("popularity").asInt());
        Optional<String> link = Optional.ofNullable(nextValue.get("preview_url").asText());

        Optional<String> image = Optional.empty();
        List<Artist> artists = new ArrayList<>();

        if (nextValue.hasNonNull("artists") && nextValue.get("artists").isArray()) {
            for (JsonNode nextArt : nextValue.get("artists")) {
                Optional<String> idArt = Optional.ofNullable(nextArt.get("id").asText());
                Optional<String> nameArt = Optional.ofNullable(nextArt.get("name").asText());
                artists.add(Artist.builder().id(idArt.orElse("")).name(nameArt.orElse("")).build());
            }
        }

        if (nextValue.hasNonNull("album")) {
            JsonNode album = nextValue.get("album");
            image=getImageFromJson(album);
        }

        return Track.builder().id(id).image(image.orElse("")).name(name.orElse("")).artists(artists).link(link.orElse("")).popularity(popularity.orElse(0)).build();
    }



    private Container jsonArtistsToContainer(JsonNode json, String content, Type type)  {

        JsonNode artists = json.get("artists");
        Container container = createStandardContainer(content, artists, type);
        int total = container.getTotal();
        List<Item> itemList = container.getItems();

        if(total>0){
            JsonNode items = artists.get("items");

            for (JsonNode nextValue : items) {
                Optional<String> id = Optional.ofNullable(nextValue.get("id").asText());
                Optional<String> name = Optional.ofNullable(nextValue.get("name").asText());
                Optional<Integer> popularity = Optional.of(nextValue.get("popularity").asInt());
                Optional<Integer> followers = Optional.empty();
                Optional<String> image = getImageFromJson(nextValue);
                Optional<String> link = Optional.empty();
                if(nextValue.hasNonNull("external_urls") && nextValue.get("external_urls").hasNonNull("spotify")){
                    link=Optional.ofNullable(nextValue.get("external_urls").get("spotify").asText());
                }
                if(nextValue.hasNonNull("followers") && nextValue.get("followers").hasNonNull("total")){
                    followers = Optional.of(nextValue.get("followers").get("total").asInt());
                }

                itemList.add(Artist.builder().id(id.orElse("")).image(image.orElse("")).name(name.orElse("")).followers(followers.get()).link(link.orElse("")).popularity(popularity.orElse(0)).link(link.orElse("")).build());

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
