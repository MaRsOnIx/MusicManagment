package me.marsonix.spotifyapitest.utilis.spotify.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.marsonix.spotifyapitest.utilis.spotify.token.Token;
import org.apache.http.client.methods.HttpGet;

public class GetRequest extends AbstractPreparedRequest {

    public GetRequest(String url, Type type, Token token, ObjectMapper obj) {
        super(new HttpGet(url), type, token, obj);
    }

}
