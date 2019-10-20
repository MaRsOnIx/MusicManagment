package me.marsonix.spotifyapitest.exceptions;

public class MissingPropertyException extends RuntimeException {

    public MissingPropertyException(String message) {
        super(message);
    }
}
