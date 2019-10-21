package me.marsonix.spotifyapitest.exceptions;

import me.marsonix.spotifyapitest.logger.LogManager;
import org.springframework.beans.factory.annotation.Autowired;


public class SpotifyConnectionException extends Exception {

    @Autowired
    private LogManager logManager;

    public SpotifyConnectionException(String s) {
        super(s);



    }
}
