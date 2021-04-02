package com.group2.recipeze.data.model;

import com.google.gson.JsonElement;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Data class that captures user information for logged in users retrieved from LoginRepository
 */
public class LoggedInUser {

    private String username;
    private String email;
    private String bio;
    private HashMap<String, JsonElement> settings;
    private String token;


    public LoggedInUser() {}

    public LoggedInUser(String username, String email, String bio, HashMap<String, JsonElement> settings) {
        this.username = username;
        this.email = email;
        this.bio = bio;
        this.settings = settings;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBio() {
        return bio;
    }

    public void setBio(String bio) {
        this.bio = bio;
    }

    public HashMap<String, JsonElement> getSettings() {
        return settings;
    }

    public void setSettings(HashMap<String, JsonElement> settings) {
        this.settings = settings;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}