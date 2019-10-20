package me.marsonix.spotifyapitest.exceptions;

import me.marsonix.spotifyapitest.logger.Log;
import me.marsonix.spotifyapitest.logger.LogManager;
import me.marsonix.spotifyapitest.logger.Type;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.LocalDate;

public class SpotifyConnectionException extends RuntimeException {

    @Autowired
    private LogManager logManager;

    public SpotifyConnectionException(String s) {
        super(s);



    }
}
