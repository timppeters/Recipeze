package com.group2.recipeze.data.model;

public class Comment {

    private int commentId;
    private String author;
    private String body;

    /**
     * Forum post comment
     * @param commentId
     * @param author
     * @param body
     */
    public Comment(int commentId, String author, String body) {
        this.commentId = commentId;
        this.author = author;
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public int getCommentId() {
        return commentId;
    }

    public void setCommentId(int commentId) {
        this.commentId = commentId;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
