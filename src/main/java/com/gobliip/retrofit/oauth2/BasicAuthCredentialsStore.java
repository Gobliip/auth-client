package com.gobliip.retrofit.oauth2;

import java.util.Optional;

/**
 * Created by lsamayoa on 14/07/15.
 */
public interface BasicAuthCredentialsStore {
    Optional<String> getUsername();
    Optional<String> getPassword();
    void setCredentials(String username, String password);
}
