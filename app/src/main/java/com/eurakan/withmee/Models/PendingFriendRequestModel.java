package com.eurakan.withmee.Models;

/**
 * Created by Admin on 1/26/2019.
 */

public class PendingFriendRequestModel {

    private String  email="", gender="", mobile="", fullname="", fullAddress="", aadharNumber="", work="", status="", studyIn="", city="", dateOfBirth ="";
    private int id;
    private String profileImageUrl;
    private String token ="";
    public Boolean isReceived = false;

    public PendingFriendRequestModel(int id, String name, String fullAddress, String work, String profileImageUrl) {
        this.id = id;
        this.fullname = name;
        this.fullAddress = fullAddress;
        this.work = work;
        this.profileImageUrl = profileImageUrl;
    }

    public String GetFullName() {
        return fullname;
    }

    public String getUsername() {
        return fullname;
    }

    public String getFullAddress() {
        return fullAddress;
    }

    public String getAadharNumber() {
        return aadharNumber;
    }

    public String getEmail() {
        return email;
    }

    public String getDateOfBirth() {
        return dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public String getMobile() {
        return mobile;
    }

    public int getId() {
        return id;
    }

    public void setAadharNumber(String aadhar){
        this.aadharNumber = aadhar;
    }

    public void setFullAddress(String fullAddress){
        this.fullAddress = fullAddress;
    }

    public String getWork() {
        return work;
    }

    public String getStatus() {
        return status;
    }

    public String getStudyIn() {
        return studyIn;
    }

    public String getCity() {
        return city;
    }

    public void setWorksAt(String worksAt){
        this.work = worksAt;
    }

    public void setStatus(String status){
        this.status = status;
    }

    public void setGender(String gender){
        this.gender = gender;
    }

    public void setDateOfBirth(String dateOfBirth){
        this.dateOfBirth = dateOfBirth;
    }

    public String getProfileImageUrl(){ return profileImageUrl;}

    public String getToken(){ return profileImageUrl;}

    public void setToken(String token){
        this.token = token;
    }

    public void setProfileImageUrl(String profileImageUrl){this.profileImageUrl = profileImageUrl;}

    public PendingFriendRequestModel(int id, String mobile, String email, String username, String aadhar, String gender, String address, String work, String status, String city, String studyIn, String dateOfBirth) {
        this.id = id;
        this.mobile = mobile;
        this.email = email;
        this.fullname = username;
        this.aadharNumber = aadhar;
        this.gender = gender;
        this.fullAddress = address;
        this.work = work;
        this.status = status;
        this.city = city;
        this.studyIn = studyIn;
        this.dateOfBirth = dateOfBirth;
    }

}
