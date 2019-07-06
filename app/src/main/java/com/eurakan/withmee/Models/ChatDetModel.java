package com.eurakan.withmee.Models;


import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 1/27/2019.
 */

public class ChatDetModel {

    public String  message;
    public Date createdAt;
    public int sender, id;
    public int chatroomid;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

    public ChatDetModel(int id, String message, String createdAt, int sender) throws ParseException {
        this.id = id;
        this.message = message;
        this.createdAt = format.parse(createdAt);
        this.sender = sender;
    }

}
