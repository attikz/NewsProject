package com.susankya.yubahunkar.model;

public class SavePostModel {

    public String postImagePath, postTitle, postDate, postContent, postId;

    public SavePostModel(String postImagePath, String postTitle, String postDate, String postContent, String postId) {
        this.postImagePath = postImagePath;
        this.postTitle = postTitle;
        this.postDate = postDate;
        this.postContent = postContent;
        this.postId = postId;
    }
}
