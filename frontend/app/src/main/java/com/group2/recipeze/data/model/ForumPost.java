package com.group2.recipeze.data.model;

public class ForumPost {

    private int postId;
    private String author;
    private String title;
    private String body;
    private Comment[] comments;
    private boolean liked;

    /**
     * Forum post
     * @param postId
     * @param author
     * @param title
     * @param body
     * @param comments
     */
    public ForumPost(int postId, String author, String title, String body, Comment[] comments) {
        this.postId = postId;
        this.author = author;
        this.title = title;
        this.body = body;
        this.comments = comments;
        this.liked = liked;
    }

    public int getPostId() {
        return postId;
    }

    public boolean getLiked() {return liked; }

    public void setPostId(int postId) {
        this.postId = postId;
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
