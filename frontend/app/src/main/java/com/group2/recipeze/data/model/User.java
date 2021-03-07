package com.group2.recipeze.data.model;

import java.util.ArrayList;

public class User {

    private String username;
    private String bio;
    private ArrayList<Recipe> recipes;

    /**
     * External user (not logged in) with username
     * @param username
     */
    public User(String username) {
        this.username = username;
    }

    /**
     * External user (not logged in) with username, bio, and recipes they have authored
     * @param username
     * @param bio
     * @param recipes
     */
    public User(String username, String bio, ArrayList<Recipe> recipes) {
        this.username = username;
        this.bio = bio;
        this.recipes = recipes;
    }

    public String getUsername() {
        return username;
    }

    public String getBio() {
        return bio;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
