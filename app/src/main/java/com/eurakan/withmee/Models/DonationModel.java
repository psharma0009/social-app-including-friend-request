package com.eurakan.withmee.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 1/25/2019.
 */

public class DonationModel {

    public String reasonForDonation, donationStatus, title, description;
    public int id,userId, requestAmount, totalBalance;
    public String donationImage;
    public String profileImage;

    public String getDonationImage() {
        return donationImage;
    }

    public void setDonationImage(String donationImage) {
        this.donationImage = donationImage;
    }

    public String getProfileImage() {
        return profileImage;
    }

    public void setProfileImage(String profileImage) {
        this.profileImage = profileImage;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String userName;
    public DonationModel(int id, String reasonForDonation, String donationStatus, String title, String description, int userId, int requestAmount, int totalBalance, String date) throws ParseException {
        this.id = id;
        this.reasonForDonation = reasonForDonation;
        this.donationStatus = donationStatus;
        this.title = title;
        this.description = description;
        this.userId = userId;
        this.requestAmount = requestAmount;
        this.totalBalance = totalBalance;
        this.date = format.parse(date);
    }

    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
    Date date;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getReasonForDonation() {
        return reasonForDonation;
    }

    public void setReasonForDonation(String reasonForDonation) {
        this.reasonForDonation = reasonForDonation;
    }

    public String getDonationStatus() {
        return donationStatus;
    }

    public void setDonationStatus(String donationStatus) {
        this.donationStatus = donationStatus;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(int requestAmount) {
        this.requestAmount = requestAmount;
    }

    public int getTotalBalance() {
        return totalBalance;
    }

    public void setTotalBalance(int totalBalance) {
        this.totalBalance = totalBalance;
    }

    public DateFormat getFormat() {
        return format;
    }

    public void setFormat(DateFormat format) {
        this.format = format;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }
}
