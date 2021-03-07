package com.group2.recipeze.data.model;

public class ForumPost {

    private int postId;
    private String username;
    private String title;
    private String body;
    private Tag tag;

    /**
     * Forum post
     * @param postId
     * @param username
     * @param title
     * @param body
     * @param tag
     */
    public ForumPost(int postId, String username, String title, String body, Tag tag) {
        this.postId = postId;
        this.username = username;
        this.title = title;
        this.body = body;
        this.tag = tag;
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

    public Tag getTag() {
        return tag;
    }

    public void setTag(Tag tag) {
        this.tag = tag;
    }
}
