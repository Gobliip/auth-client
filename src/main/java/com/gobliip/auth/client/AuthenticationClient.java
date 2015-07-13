package com.gobliip.auth.client;

import com.gobliip.auth.client.model.AuthenticationResponse;

/**
 * Created by lsamayoa on 12/07/15.
 */
public interface AuthenticationClient {

    AuthenticationResponse getTokenWithPasswordGrant(String username, String password);

    AuthenticationResponse getTokenWithRefreshTokenGrant(String refreshToken);

}
