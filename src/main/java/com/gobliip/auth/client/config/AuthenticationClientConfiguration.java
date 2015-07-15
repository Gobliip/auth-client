package com.gobliip.auth.client.config;

import com.gobliip.auth.client.AuthenticationClient;
import com.gobliip.auth.client.AuthenticationClientImpl;
import com.gobliip.auth.client.retrofit.AuthenticationServiceResource;
import com.gobliip.retrofit.oauth2.BasicAuthCredentialsStore;
import com.gobliip.retrofit.oauth2.InMemoryBasicAuthCredentialsStore;
import com.gobliip.retrofit.oauth2.OAuthBasicAuthInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by lsamayoa on 12/07/15.
 */
@Configuration
public class AuthenticationClientConfiguration {

    @Value("${gobliip.oauth2.clientId}")
    private String clientId;

    @Value("${gobliip.oauth2.clientSecret}")
    private String clientSecret;

    @Value("${gobliip.oauth2.endpoint}")
    private String endpoint;

    @Autowired(required = false)
    private BasicAuthCredentialsStore credentialsStore;

    @Bean
    public AuthenticationClient authenticationClient(){
        return new AuthenticationClientImpl(authenticationServiceResource());
    }

    @Bean
    public AuthenticationServiceResource authenticationServiceResource(){
        return restAdapter().create(AuthenticationServiceResource.class);
    }

    @Bean
    public RestAdapter restAdapter(){
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson()))
                .setEndpoint(endpoint)
                .setRequestInterceptor(oAuthBasicInterceptor())
                .build();
        return restAdapter;
    }

    @Bean
    public Gson gson(){
        final Gson gson = new GsonBuilder()
                .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
                .create();
        return gson;
    }

    @Bean
    public OAuthBasicAuthInterceptor oAuthBasicInterceptor(){
        return new OAuthBasicAuthInterceptor(credentialsStore());
    }

    @Bean
    public BasicAuthCredentialsStore credentialsStore(){
        if(credentialsStore != null) return credentialsStore;
        return new InMemoryBasicAuthCredentialsStore(clientId, clientSecret);
    }

}
