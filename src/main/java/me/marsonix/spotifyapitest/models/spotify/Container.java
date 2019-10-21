package me.marsonix.spotifyapitest.models.spotify;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

@Data
@EqualsAndHashCode
public class Container {

    private String content;
    private String previousUrl;
    private String nextUrl;
    private int total;
    private Type type;

    private List<Item> items;



}
