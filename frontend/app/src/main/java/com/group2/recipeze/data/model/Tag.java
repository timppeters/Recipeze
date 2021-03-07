package com.group2.recipeze.data.model;

import java.util.ArrayList;

public class Tag {

    private String name;
    // private Forum forum;
    private ArrayList<Recipe> recipes;

    /**
     * Tag
     * @param name
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Tag with list of recipes
     * @param name
     * @param recipes
     */
    public Tag(String name, ArrayList<Recipe> recipes) {
        this.name = name;
        this.recipes = recipes;
    }

    public String getName() {
        return name;
    }

    public ArrayList<Recipe> getRecipes() {
        return recipes;
    }

    public void setRecipes(ArrayList<Recipe> recipes) {
        this.recipes = recipes;
    }
}
