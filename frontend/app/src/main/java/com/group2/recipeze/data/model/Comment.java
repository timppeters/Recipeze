package com.group2.recipeze.data.model;

public class Comment {

    private int commentId;
    private String author;
    private ForumPost post;
    private String body;

    /**
     * Forum post comment
     * @param commentId
     * @param author
     * @param post
     * @param body
     */
    public Comment(int commentId, String author, ForumPost post, String body) {
        this.commentId = commentId;
        this.author = author;
        this.post = post;
        this.body = body;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public ForumPost getPost() {
        return post;
    }

    public void setPost(ForumPost post) {
        this.post = post;
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
