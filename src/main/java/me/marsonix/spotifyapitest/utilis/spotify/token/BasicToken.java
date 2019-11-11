package me.marsonix.spotifyapitest.utilis.spotify.token;

import java.util.Base64;
import java.util.Optional;

public class BasicToken extends AbstractTokenBody {

    private char[] clientId;
    private char[] secredId;

    public BasicToken(char[] clientId, char[] secredId) {
        this.clientId = clientId;
        this.secredId = secredId;
        creatingToken();
    }

    @Override
    protected void creatingToken() {
        super.data=Optional.of(Base64.getEncoder()
                .encodeToString(String.join("",
                        String.valueOf(clientId)
                        ,":",
                        String.valueOf(secredId)
                ).getBytes()).toCharArray());
    }

}
