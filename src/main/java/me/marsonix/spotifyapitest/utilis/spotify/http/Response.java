package me.marsonix.spotifyapitest.utilis.spotify.http;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

@Data
public class Response {
    private JsonNode data;
}
