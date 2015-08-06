package com.gobliip.auth.client.config;

import com.gobliip.auth.client.AuthenticationClient;
import com.gobliip.auth.client.retrofit.AuthenticationServiceResource;
import com.gobliip.retrofit.cloud.endpoint.RoundRobinEndpoint;
import com.gobliip.retrofit.cloud.oauth2.basic.BasicAuthCredentialsStore;
import com.gobliip.retrofit.cloud.oauth2.basic.InMemoryBasicAuthCredentialsStore;
import com.gobliip.retrofit.cloud.oauth2.basic.OAuthBasicAuthInterceptor;
import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import retrofit.Endpoint;
import retrofit.Endpoints;
import retrofit.RestAdapter;
import retrofit.converter.GsonConverter;

/**
 * Created by lsamayoa on 12/07/15.
 */
@Configuration
public class AuthenticationClientConfiguration {

    @Value("${gobliip.authentication.clientId}")
    private String clientId;

    @Value("${gobliip.authentication.clientSecret}")
    private String clientSecret;

    @Value("${gobliip.authentication.service-name:authentication")
    private String serviceName;

    @Value("${gobliip.authentication.endpoint:http://authentication.gobliip.:5021/}")
    private String endpoint;

    @Autowired(required = false)
    private OAuthBasicAuthInterceptor oAuthBasicInterceptor;

    @Autowired(required = false)
    private BasicAuthCredentialsStore basicAuthCredentialsStore;

    @Autowired(required = false)
    private DiscoveryClient discoveryClient;

    @Bean
    public AuthenticationClient authenticationClient(AuthenticationServiceResource authenticationServiceResource){
        return new AuthenticationClient(authenticationServiceResource);
    }

    @Bean
    public AuthenticationServiceResource authenticationServiceResource(RestAdapter restAdapter){
        return restAdapter.create(AuthenticationServiceResource.class);
    }

    @Bean
    public RestAdapter restAdapter(OAuthBasicAuthInterceptor oAuthBasicInterceptor, Gson gson){
        final RestAdapter restAdapter = new RestAdapter.Builder()
                .setConverter(new GsonConverter(gson))
                .setEndpoint(endpoint)
                .setRequestInterceptor(oAuthBasicInterceptor)
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
        if (oAuthBasicInterceptor != null) return oAuthBasicInterceptor;
        return new OAuthBasicAuthInterceptor(basicAuthCredentialsStore());
    }

    @Bean
    public BasicAuthCredentialsStore basicAuthCredentialsStore(){
        if(basicAuthCredentialsStore != null) return basicAuthCredentialsStore;
        return new InMemoryBasicAuthCredentialsStore(clientId,  clientSecret);
    }

    @Bean
    public Endpoint authenticationEndpoint(){
        if(discoveryClient != null) {
            return new RoundRobinEndpoint(discoveryClient, serviceName);
        }
        return Endpoints.newFixedEndpoint(endpoint);
    }

}
