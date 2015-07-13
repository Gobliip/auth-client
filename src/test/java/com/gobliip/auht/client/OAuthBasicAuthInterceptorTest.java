package com.gobliip.auht.client;

import com.gobliip.auth.client.retrofit.OAuthBasicAuthInterceptor;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import retrofit.RequestInterceptor;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.*;

/**
 * Created by lsamayoa on 12/07/15.
 */
@RunWith(MockitoJUnitRunner.class)
public class OAuthBasicAuthInterceptorTest {

    private static final String OAUTH_BASIC = "Basic YWNtZTphY21lc2VjcmV0";

    @Mock
    RequestInterceptor.RequestFacade requestFacade;

    @Test
    public void test(){
        OAuthBasicAuthInterceptor interceptor = new OAuthBasicAuthInterceptor("acme", "acmesecret");
        interceptor.intercept(requestFacade);
        verify(requestFacade, times(1)).addHeader("Authorization", OAUTH_BASIC);
    }

}
