package com.group2.recipeze.data.model;

import java.util.ArrayList;

public class Tag {

    private String name;
    // private Forum forum;
    private ArrayList<Recipe> recipes;
    private int numberOfRecipes;

    /**
     * Tag
     * @param name
     */
    public Tag(String name) {
        this.name = name;
    }

    /**
     * Tag with numberOfRecipes
     * @param name
     * @param numberOfRecipes
     */
    public Tag(String name, int numberOfRecipes) {
        this.name = name;
        this.numberOfRecipes = numberOfRecipes;
    }

    /**
     * Tag with numberOfRecipes + list of recipes
     * @param name
     * @param numberOfRecipes
     * @param recipes
     */
    public Tag(String name, int numberOfRecipes, ArrayList<Recipe> recipes) {
        this.name = name;
        this.numberOfRecipes = numberOfRecipes;
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

    public int getNumberOfRecipes() {
        return numberOfRecipes;
    }

    public void setNumberOfRecipes(int numberOfRecipes) {
        this.numberOfRecipes = numberOfRecipes;
    }
}
