package com.group2.recipeze.data.model;

public class ForumPost {

    private int postId;
    private String username;
    private String title;
    private String body;
    private Comment[] comments;

    /**
     * Forum post
     * @param postId
     * @param username
     * @param title
     * @param body
     * @param comments
     */
    public ForumPost(int postId, String username, String title, String body, Comment[] comments) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.body = body;
        this.comments = comments;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public Comment[] getComments() {
        return comments;
    }

    public void setComments(Comment[] comments) {
        this.comments = comments;
    }
}
