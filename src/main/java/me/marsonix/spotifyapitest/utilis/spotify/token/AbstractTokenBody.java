package me.marsonix.spotifyapitest.utilis.spotify.token;

import java.util.Optional;

abstract public class AbstractTokenBody implements Token {

    protected Optional<char[]>data;
    protected abstract void creatingToken();

    @Override
    public Optional<char[]>get(){
        return this.data;
    }

}
