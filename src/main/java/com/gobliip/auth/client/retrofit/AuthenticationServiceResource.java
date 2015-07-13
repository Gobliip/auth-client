package com.gobliip.auth.client.retrofit;

import com.gobliip.auth.client.model.AuthenticationResponse;
import retrofit.http.FieldMap;
import retrofit.http.FormUrlEncoded;
import retrofit.http.Headers;
import retrofit.http.POST;

import java.util.Map;

/**
 * Created by lsamayoa on 12/07/15.
 */
public interface AuthenticationServiceResource {

    @POST("/oauth/token")
    @FormUrlEncoded
    @Headers("Accept: application/json")
    AuthenticationResponse getToken(@FieldMap Map<String, String> params);

}
