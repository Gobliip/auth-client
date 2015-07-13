package com.gobliip.auth.client;

import com.gobliip.auth.client.model.AuthenticationResponse;
import retrofit.http.*;
import rx.Observable;

/**
 * Created by lsamayoa on 12/07/15.
 */
public interface AuthenticationServiceClient {

    @POST("/oauth/token")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    Observable<AuthenticationResponse> getToken(@Field("grant_type") String grantType, @Field("username") String username, @Field("password") String password);

}
