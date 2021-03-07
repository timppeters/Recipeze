package com.group2.recipeze.ui.login;

import org.json.JSONObject;

/**
 * Class exposing authenticated user details to the UI.
 */
class LoggedInUserView {
    private String username;
    private String email;
    private String bio;
    private JSONObject settings;

    LoggedInUserView(String username, String email, String bio, JSONObject settings) {

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

    public JSONObject getSettings() {
        return settings;
    }

    public void setSettings(JSONObject settings) {
        this.settings = settings;
    }
}