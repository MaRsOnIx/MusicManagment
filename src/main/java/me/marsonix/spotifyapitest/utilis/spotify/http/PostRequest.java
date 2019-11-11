package me.marsonix.spotifyapitest.utilis.spotify.http;


import com.fasterxml.jackson.databind.ObjectMapper;
import me.marsonix.spotifyapitest.utilis.spotify.token.Token;
import org.apache.http.client.methods.HttpPost;

public class PostRequest extends AbstractPreparedRequest {

    public PostRequest(String url, Type type, Token token, ObjectMapper obj) {
        super(new HttpPost(url), type, token, obj);
    }

}
