package me.marsonix.spotifyapitest.exceptions;

import me.marsonix.spotifyapitest.logger.LogManager;
import org.springframework.beans.factory.annotation.Autowired;

public class TrackNotFoundException extends RuntimeException {

    @Autowired
    private LogManager logManager;

    public TrackNotFoundException(String s) {
        super(s);



    }
}
