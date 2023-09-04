package com.iris.springboot.model;

import java.util.List;

public class JwtResponse {
    private final String token;
    private List<String> roles;

    public JwtResponse(String token, List<String> roles) {
        this.token = token;
        this.roles = roles;
    }

    public String getToken() {
        return token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}
