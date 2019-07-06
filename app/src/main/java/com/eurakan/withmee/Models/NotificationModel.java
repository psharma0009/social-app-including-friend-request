package com.eurakan.withmee.Models;

/**
 * Created by Admin on 2/5/2019.
 */

public class NotificationModel {

    public String id, notificationTitle, notiDetails, dateTime;

    public NotificationModel(String id, String notificationTitle, String notiDetails, String dateTime) {

        this.id = id;
        this.notificationTitle = notificationTitle;
        this.notiDetails = notiDetails;
        this.dateTime = dateTime;
    }

    public String getNotificationTitle(){
        return notificationTitle;
    }

    public String getNotificationId(){
        return id;
    }

    public String getNotiDetails(){
        return notiDetails;
    }

    public String getDateTime(){
        return dateTime;
    }
}
