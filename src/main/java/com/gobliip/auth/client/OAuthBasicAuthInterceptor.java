package com.gobliip.auth.client;

import retrofit.RequestInterceptor;

import java.util.Base64;

/**
 * Created by lsamayoa on 12/07/15.
 */
public class OAuthBasicAuthInterceptor implements RequestInterceptor {

    private String clientId;
    private String clientSecret;

    public OAuthBasicAuthInterceptor(String clientId, String clientSecret) {
        this.clientId = clientId;
        this.clientSecret = clientSecret;
    }

    public void intercept(RequestFacade request) {
        final StringBuilder builder = new StringBuilder("Basic ");
        final String authString = clientId + ":" + clientSecret;
        builder.append(Base64.getEncoder().encodeToString(authString.getBytes()));
        request.addHeader("Authorization", builder.toString());
    }
}
