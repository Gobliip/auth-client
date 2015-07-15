package com.gobliip.auht.client;

import com.gobliip.auth.client.model.AuthenticationResponse;
import com.gobliip.auth.client.retrofit.AuthenticationServiceResource;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.matchers.Times;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

/**
 * Created by lsamayoa on 12/07/15.
 */
public class AuthenticationServiceResourceTest {

    private static final String GET_TOKEN_RESPONSE =
            "{\"access_token\":\"ACCESS_TOKEN\"," +
            "\"token_type\":\"bearer\"," +
            "\"refresh_token\":\"REFRESH_TOKEN\"," +
            "\"expires_in\":43199," +
            "\"scope\":\"openid\"," +
            "\"jti\":\"d4b6b281-e745-4346-aa26-d97de3d8a585\"}";

    private ClientAndServer mockServer;

    @Before
    public void startProxy() {
        mockServer = startClientAndServer(1080);
    }

    @After
    public void stopProxy() {
        mockServer.stop();
    }

    @Test
    public void test_getToken_passwordGrant() throws InterruptedException {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://localhost:1080/")
                .setConverter(new GsonConverter(gson))
                .build();

        AuthenticationServiceResource serviceClient = restAdapter.create(AuthenticationServiceResource.class);

        HttpRequest postToOAuthToken = request()
                .withMethod("POST")
                .withPath("/oauth/token");


        mockServer
                .when(postToOAuthToken, Times.once())
                .respond(response().withBody(GET_TOKEN_RESPONSE));


        Map<String, String> params = new HashMap<String, String>();
        params.put("username", "admin");
        params.put("password", "r00t");
        params.put("grant_type", "password");

        AuthenticationResponse response = serviceClient.getToken(params);
        assertEquals("ACCESS_TOKEN", response.getAccessToken());
        assertEquals("REFRESH_TOKEN", response.getRefreshToken());
        assertEquals(43199, response.getExpiresIn());
        assertEquals("bearer", response.getTokenType());

        mockServer.verify(postToOAuthToken.withBody("password=r00t&grant_type=password&username=admin"), VerificationTimes.exactly(1));
    }

    @Test
    public void test_getToken_refreshTokenGrant() throws InterruptedException {
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();

        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://localhost:1080/")
                .setConverter(new GsonConverter(gson))
                .build();

        AuthenticationServiceResource serviceClient = restAdapter.create(AuthenticationServiceResource.class);

        HttpRequest postToOAuthToken = request()
                .withMethod("POST")
                .withPath("/oauth/token");
        mockServer
                .when(postToOAuthToken, Times.once())
                .respond(response().withBody(GET_TOKEN_RESPONSE));

        // We let mockserver breath
        Thread.sleep(5000);

        Map<String, String> params = new HashMap<String, String>();
        params.put("refresh_token", "REFRESH_TOKEN");
        params.put("grant_type", "refresh_token");

        serviceClient.getToken(params);

        mockServer.verify(postToOAuthToken.withBody("refresh_token=REFRESH_TOKEN&grant_type=refresh_token"), VerificationTimes.exactly(1));
    }
}
