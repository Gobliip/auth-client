package com.gobliip.auth.client;

import com.gobliip.auth.client.model.AuthenticationResponse;
import com.gobliip.auth.client.retrofit.AuthenticationServiceResource;
import org.springframework.util.Assert;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by lsamayoa on 12/07/15.
 */
public class AuthenticationClient {

    public static final String GRANT_TYPE_PARAM = "grant_type";
    public static final String USERNAME_PARAM = "username";
    public static final String PASSWORD_PARAM = "password";
    public static final String REFRESH_TOKEN_PARAM = "refresh_token";

    public static final String PASSWORD_GRANT_TYPE = "password";

    private AuthenticationServiceResource authenticationServiceResource;

    public AuthenticationClient(AuthenticationServiceResource authenticationServiceResource) {
        this.authenticationServiceResource = authenticationServiceResource;
    }

    public AuthenticationResponse getTokenWithPasswordGrant(String username, String password) {
        Assert.hasText(username);
        Assert.hasText(password);

        final Map<String, String> params = new HashMap<String, String>();
        params.put(GRANT_TYPE_PARAM, PASSWORD_GRANT_TYPE);
        params.put(USERNAME_PARAM, username);
        params.put(PASSWORD_PARAM, password);

        return authenticationServiceResource.getToken(params);
    }

    public AuthenticationResponse getTokenWithRefreshTokenGrant(String refreshToken) {
        Assert.hasText(refreshToken);

        final Map<String, String> params = new HashMap<String, String>();
        params.put(GRANT_TYPE_PARAM, PASSWORD_GRANT_TYPE);
        params.put(REFRESH_TOKEN_PARAM, refreshToken);

        return authenticationServiceResource.getToken(params);
    }
}
