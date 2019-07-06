package com.eurakan.withmee.Models;

/**
 * Created by Admin on 1/22/2019.
 */

public class PostsModel {

    public String userImagePath, postImagePath, comment;
    public int total_like;
    public int postUserId;
    public int postId;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String userName;
    public String getUserImagePath() {
        return userImagePath;
    }

    public void setUserImagePath(String userImagePath) {
        this.userImagePath = userImagePath;
    }

    public String getPostImagePath() {
        return postImagePath;
    }

    public void setPostImagePath(String postImagePath) {
        this.postImagePath = postImagePath;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public int getTotal_like() {
        return total_like;
    }

    public void setTotal_like(int total_like) {
        this.total_like = total_like;
    }

    public int getPostUserId() {
        return postUserId;
    }

    public void setPostUserId(int postUserId) {
        this.postUserId = postUserId;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public PostsModel(int postId, String userImagePath, String postImagePath, String comment, int total_like, int postUserId, String userName) {
        this.postUserId = postUserId;
        this.postId = postId;
        this.userImagePath = userImagePath;
        this.postImagePath = postImagePath;
        this.comment = comment;
        this.total_like = total_like;
        this.userName = userName;
    }
}
