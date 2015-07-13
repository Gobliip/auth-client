package com.gobliip.auht.client;

import com.gobliip.auth.client.AuthenticationServiceClient;
import com.gobliip.auth.client.model.AuthenticationResponse;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.mockserver.integration.ClientAndServer;
import org.mockserver.model.Body;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

import static org.mockserver.integration.ClientAndServer.startClientAndServer;
import static org.mockserver.model.HttpRequest.request;
import static org.mockserver.model.HttpResponse.response;

import static org.junit.Assert.assertEquals;

/**
 * Created by lsamayoa on 12/07/15.
 */
public class AuthenticationServiceClientTest {

    private ClientAndServer mockServer;

    private static final String GET_TOKEN_RESPONSE =
            "{\"access_token\":\"ACCESS_TOKEN\"," +
            "\"token_type\":\"bearer\"," +
            "\"refresh_token\":\"REFRESH_TOKEN\"," +
            "\"expires_in\":43199," +
            "\"scope\":\"openid\"," +
            "\"jti\":\"d4b6b281-e745-4346-aa26-d97de3d8a585\"}";

    @Before
    public void startProxy() {
        mockServer = startClientAndServer(1080);
    }

    @After
    public void stopProxy() {
        mockServer.stop();
    }

    @Test
    public void test_getToken(){
        Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint("http://localhost:1080/")
                .setConverter(new GsonConverter(gson))
                .build();

        AuthenticationServiceClient serviceClient = restAdapter.create(AuthenticationServiceClient.class);

        HttpRequest postToOAuthToken = request()
                .withMethod("POST")
                .withPath("/oauth/token");

        mockServer
                .when(postToOAuthToken)
                .respond(response().withBody(GET_TOKEN_RESPONSE));

        AuthenticationResponse response = serviceClient.getToken("password", "admin", "root").toBlocking().first();
        assertEquals("ACCESS_TOKEN", response.getAccessToken());
        assertEquals("REFRESH_TOKEN", response.getRefreshToken());
        assertEquals(43199, response.getExpiresIn());
        assertEquals("bearer", response.getTokenType());

        mockServer.verify(postToOAuthToken.withBody("grant_type=password&username=admin&password=root"), VerificationTimes.exactly(1));
    }

}
