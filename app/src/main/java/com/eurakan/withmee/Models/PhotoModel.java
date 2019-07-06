package com.eurakan.withmee.Models;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 1/26/2019.
 */

public class PhotoModel {

    private String pic;
    private int id;
    private Date createdOn;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getCreatedOn() {
        return createdOn;
    }

    public void setCreatedOn(Date createdOn) {
        this.createdOn = createdOn;
    }

    public PhotoModel(int id, String pic, String createdOn)  throws ParseException {
        this.id = id;
        this.pic = pic;
        this.createdOn = format.parse(createdOn);
    }

}
