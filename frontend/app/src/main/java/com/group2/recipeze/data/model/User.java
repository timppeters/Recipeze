package com.group2.recipeze.data.model;

import java.util.ArrayList;

public class User {

    private String username;
    private String bio;

    /**
     * External user (not logged in) with username
     * @param username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * External user (not logged in) with username, bio
     * @param username
     * @param bio
     */
    public User(String username, String bio) {
        this.username = username;
        this.bio = bio;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

}
