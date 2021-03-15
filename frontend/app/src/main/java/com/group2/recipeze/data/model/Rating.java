package com.group2.recipeze.data.model;

public class Rating {

    private String user;
    private int rating;
    private String review;

    /**
     * Review without review (Just rating)
     * @param user
     * @param rating
     */
    public Rating(String user, int rating) {
        this.user = user;
        this.rating = rating;
        this.review = "";
    }

    /**
     * Review with rating + review text
     * @param user
     * @param rating
     * @param review
     */
    public Rating(String user, int rating, String review) {
        this.user = user;
        this.rating = rating;
        this.review = review;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public int getRating() {
        return rating;
    }

    public void setRating(int rating) {
        this.rating = rating;
    }

    public String getReview() {
        return review;
    }

    public void setReview(String review) {
        this.review = review;
    }
}
