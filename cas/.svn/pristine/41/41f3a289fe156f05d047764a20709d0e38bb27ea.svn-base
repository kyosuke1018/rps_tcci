/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.tcci.security;

import java.time.Instant;
import java.util.Optional;

/**
 *
 * @author Peter.pan
 */
public class OAuth2Result {
    private String accessToken;
    private Optional<String> scope;
    private Optional<String> refreshToken;
    private Optional<Integer> expiresIn;
    private Instant TimeSet;
    
    public OAuth2Result() {}

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Optional<String> getScope() {
        return scope;
    }

    public void setScope(Optional<String> scope) {
        this.scope = scope;
    }

    public Optional<String> getRefreshToken() {
        return refreshToken;
    }

    public Optional<Integer> getExpiresIn() {
        return expiresIn;
    }

    public Instant getTimeSet() {
        return TimeSet;
    }

    public void setTimeSet(Instant TimeSet) {
        this.TimeSet = TimeSet;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = (refreshToken==null)?Optional.empty():Optional.of(refreshToken);
    }

    public void setExpiresIn(Integer expiresIn) {
        this.expiresIn = (expiresIn==null)?Optional.empty():Optional.of(expiresIn);
    }

}
