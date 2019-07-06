package com.eurakan.withmee.Models;

import com.eurakan.withmee.Preferences.Utilities;

/**
 * Created by Admin on 1/26/2019.
 */

public class UserModel {

    private String  email="", gender="", mobile="", fullname="", fullAddress="", aadharNumber="", work="", status="", studyIn="", city="", dateOfBirth ="";
    private int id;
    private String profileImageUrl = "";

    public String getProfileCoverImageUrl() {
        return profileCoverImageUrl;
    }

    public void setProfileCoverImageUrl(String profileCoverImageUrl) {
        this.profileCoverImageUrl = profileCoverImageUrl;
    }

    private String profileCoverImageUrl = "";


    public UserModel(int id, String mobileNumber, String email, String name, String aadharNumber) {
        this.id = id;
        this.mobile = mobileNumber;
        this.email = email;
        this.fullname = name;
        this.aadharNumber = aadharNumber;
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

    public void setId(int id) {
         this.id = id;
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

    public void setProfileImageUrl(String profileImageUrl){this.profileImageUrl = profileImageUrl;}

    public UserModel(int id, String mobile,String email,String username,String aadhar, String gender,   String address,  String work, String status, String city, String studyIn, String dateOfBirth) {
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

    public UserModel(int id){
        this.id = id;
    }
}
