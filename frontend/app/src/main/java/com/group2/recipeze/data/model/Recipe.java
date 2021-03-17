package com.group2.recipeze.data.model;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Recipe {

    private int recipeId;
    private float rating;
    private int likes;
    private String author;
    private String title;
    private String description;
    private ArrayList<String> ingredients;
    private ArrayList<String> ingredientsAmounts;
    private HashMap<Integer, String> instructions;
    private HashMap<Integer, String> images;
    private ArrayList<String> tags;
    private int prepTime;
    private int cookTime;
    private Boolean liked;


    /**
     * Recipe object
     * @param recipeId
     * @param rating
     * @param likes
     * @param author
     * @param title
     * @param description
     * @param ingredients
     * @param ingredientsAmounts
     * @param instructions
     * @param images
     * @param tags
     * @param prepTime
     * @param cookTime
     */
    public Recipe(int recipeId, float rating, int likes, String author, String title,
                  String description, ArrayList<String> ingredients, ArrayList<String> ingredientsAmounts,
                  HashMap<Integer, String> instructions, HashMap<Integer, String> images, ArrayList<String> tags, int prepTime, int cookTime, Boolean liked) {
        this.recipeId = recipeId;
        this.rating = rating;
        this.likes = likes;
        this.author = author;
        this.title = title;
        this.description = description;
        this.ingredients = ingredients;
        this.ingredientsAmounts = ingredientsAmounts;
        this.tags = tags;
        this.prepTime = prepTime;
        this.cookTime = cookTime;
        this.instructions = instructions;
        this.images = images;
        this.liked = liked;
    }

    /**
     * Get the recipe images as bitmap objects in a hashmap
     * https://stackoverflow.com/a/42295077
     *
     * @return Hashmap of bitmaps
     */
    public HashMap<Integer, Bitmap> getImagesAsBitmaps() {
        // Convert base64 image string into bitmap
        HashMap<Integer, Bitmap> imagesAsBitmaps = new HashMap<>();
        for (Map.Entry<Integer, String> entry : images.entrySet()) {
            String pureBase64Encoded = entry.getValue();//.substring(entry.getValue().indexOf(",") + 1);
            byte[] decodedBytes = Base64.decode(pureBase64Encoded, Base64.DEFAULT);
            Bitmap decodedBitmap = BitmapFactory.decodeByteArray(decodedBytes, 0, decodedBytes.length);
            imagesAsBitmaps.put(entry.getKey(), decodedBitmap);
        }
        return imagesAsBitmaps;
    }

    /**
     * Convert a hashmap of bitmaps into a hashmap of base64 strings so you can create/update a recipe.
     * https://stackoverflow.com/a/42295077
     *
     * @param bitmaps
     * @return Hashmap of base64 strings
     */
    public static HashMap<String, String> convertBitmapsToBase64(HashMap<String, Bitmap> bitmaps) {
        // Convert base64 image string into bitmap
        HashMap<String, String> imagesAsBase64 = new HashMap<>();
        for (Map.Entry<String, Bitmap> entry : bitmaps.entrySet()) {
            Bitmap bitmap = entry.getValue();
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap = getResizedBitmap(bitmap, 300);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, baos);
            byte[] imageBytes = baos.toByteArray();
            String imageString = Base64.encodeToString(imageBytes, Base64.DEFAULT);
            imagesAsBase64.put(entry.getKey(), imageString);
        }
        return imagesAsBase64;
    }

    /**
     * reduces the size of the image
     * https://stackoverflow.com/a/25136550
     *
     * @param image
     * @param maxSize
     * @return
     */
    public static Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float)width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }


    public int getRecipeId() {
        return recipeId;
    }

    public float getRating() {
        return rating;
    }

    public void setRating(float rating) {
        this.rating = rating;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList<String> getIngredients() {
        return ingredients;
    }

    public void setIngredients(ArrayList<String> ingredients) {
        this.ingredients = ingredients;
    }

    public ArrayList<String> getIngredientsAmounts() {
        return ingredientsAmounts;
    }

    public void setIngredientsAmounts(ArrayList<String> ingredientsAmounts) {
        this.ingredientsAmounts = ingredientsAmounts;
    }

    public HashMap<Integer, String> getInstructions() {
        return instructions;
    }

    public void setInstructions(HashMap<Integer, String> instructions) {
        this.instructions = instructions;
    }

    public HashMap<Integer, String> getImages() {
        return images;
    }

    public void setImages(HashMap<Integer, String> images) {
        this.images = images;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public int getPrepTime() {
        return prepTime;
    }

    public void setPrepTime(int prepTime) {
        this.prepTime = prepTime;
    }

    public int getCookTime() {
        return cookTime;
    }

    public void setCookTime(int cookTime) {
        this.cookTime = cookTime;
    }

    public boolean getLiked() {return this.liked; }

}
