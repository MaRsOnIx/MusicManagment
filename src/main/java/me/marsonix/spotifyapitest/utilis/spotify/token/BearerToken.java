package me.marsonix.spotifyapitest.utilis.spotify.token;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import me.marsonix.spotifyapitest.exceptions.SpotifyConnectionException;
import me.marsonix.spotifyapitest.utilis.spotify.http.AbstractPreparedRequest;
import me.marsonix.spotifyapitest.utilis.spotify.http.PostRequest;
import java.io.IOException;
import java.util.Optional;

public class BearerToken extends AbstractTokenBody {

    private BasicToken basicToken;
    private String tokenUrl;
    private ObjectMapper objectMapper;

    public BearerToken(BasicToken basicToken, String tokenUrl, ObjectMapper objectMapper) {
        this.basicToken = basicToken;
        this.tokenUrl = tokenUrl;
        this.objectMapper = objectMapper;
        creatingToken();
    }

    @Override
    protected void creatingToken() {
        PostRequest postRequest =
                new PostRequest(tokenUrl, AbstractPreparedRequest.Type.BASIC, basicToken, objectMapper);
        JsonNode jsonNode = null;
        try {
            jsonNode = postRequest.get().getData();

        } catch (IOException | SpotifyConnectionException e) {
            e.printStackTrace();
        }

        assert jsonNode != null;
        super.data= jsonNode.hasNonNull("access_token")?
                Optional.of(jsonNode.get("access_token").asText().toCharArray()):Optional.empty();
    }

}
