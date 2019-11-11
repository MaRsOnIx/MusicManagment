package me.marsonix.spotifyapitest.utilis.spotify.http;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.*;
import me.marsonix.spotifyapitest.exceptions.SpotifyConnectionException;
import me.marsonix.spotifyapitest.utilis.spotify.token.Token;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.message.BasicNameValuePair;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.Collections;
import java.util.stream.Collectors;

import static org.springframework.http.HttpHeaders.ACCEPT;
import static org.springframework.http.HttpHeaders.AUTHORIZATION;

@AllArgsConstructor
public abstract class AbstractPreparedRequest {
    private HttpRequestBase httpRequestBase;
    private final Type type;
    private Token token;
    private ObjectMapper objectMapper;

    private final String json = "application/json";
    private final HttpClient client = HttpClientBuilder.create().build();

    public Response get() throws IOException, SpotifyConnectionException {
        addHeaders();
        HttpResponse response = client.execute(httpRequestBase);
        Response res = new Response();
        if(response.getStatusLine().getStatusCode()!=200)
            throw new SpotifyConnectionException("Problem with connection of WEB API Spotify.");
        BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        res.setData(objectMapper.readTree(rd.lines().collect(Collectors.joining())));
        return res;

    }

    private void addHeaders() throws UnsupportedEncodingException {
        httpRequestBase.addHeader(AUTHORIZATION, String.join(" ",
                type.toString(), String.valueOf(token.get().get())));
        httpRequestBase.addHeader(ACCEPT, json);
        if(type==Type.BASIC && httpRequestBase instanceof HttpPost){
            HttpPost post = (HttpPost) httpRequestBase;
            post.setEntity(new UrlEncodedFormEntity(Collections.singletonList(
                    new BasicNameValuePair("grant_type", "client_credentials"))));
        }
    }

    public enum Type{
        BASIC, BEARER;
        @Override
        public String toString() {
            String name = this.name().toLowerCase();
            char ch = Character.toUpperCase(name.charAt(0));
            return ch+name.substring(1);
        }
    }
}



