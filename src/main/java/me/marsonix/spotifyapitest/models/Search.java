package me.marsonix.spotifyapitest.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@AllArgsConstructor
@EqualsAndHashCode
public class Search {

    private String content;
    private Type type;
    private int limit;
    private int offset;

}
