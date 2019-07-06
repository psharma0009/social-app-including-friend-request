package com.eurakan.withmee.Models;

public class CommentsModel {
    int user_id;
    String profileImageUrl;

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    String userName;
    public CommentsModel(int user_id, String profileImageUrl, String comment, String userName) {
        this.user_id = user_id;
        this.profileImageUrl = profileImageUrl;
        this.comment = comment;
        this.userName = userName;
    }

    String comment;
    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getProfileImageUrl() {
        return profileImageUrl;
    }

    public void setProfileImageUrl(String profileImageUrl) {
        this.profileImageUrl = profileImageUrl;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }



}
