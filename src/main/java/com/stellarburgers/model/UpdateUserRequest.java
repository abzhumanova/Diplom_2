package com.stellarburgers.model;

public class UpdateUserRequest {
    private String email;
    private String name;
    private String password;

    public UpdateUserRequest() {}

    public UpdateUserRequest setEmail(String email) {
        this.email = email;
        return this;
    }
    public UpdateUserRequest setName(String name) {
        this.name = name;
        return this;
    }
    public UpdateUserRequest setPassword(String password) {
        this.password = password;
        return this;
    }

    public String getEmail() {
        return email;
    }
    public String getName() {
        return name;
    }
    public String getPassword() {
        return password;
    }
}