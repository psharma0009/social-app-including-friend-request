package com.eurakan.withmee.Models;

/**
 * Created by Admin on 2/5/2019.
 */

public class EarningModel {

    public int id;
    public int likes_point;
    public int comments_point;
    public int chat_points;
    public int cashback_points;
    public int bonus_points;

    public int getBonus_points() {
        return bonus_points;
    }

    public void setBonus_points(int bonus_points) {
        this.bonus_points = bonus_points;
    }



    public EarningModel(int id, int likes_point, int comments_point, int chat_points, int cashback_points) {
        this.id = id;
        this.likes_point = likes_point;
        this.comments_point = comments_point;
        this.chat_points = chat_points;
        this.cashback_points = cashback_points;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getLikes_point() {
        return likes_point;
    }

    public void setLikes_point(int likes_point) {
        this.likes_point = likes_point;
    }

    public int getComments_point() {
        return comments_point;
    }

    public void setComments_point(int comments_point) {
        this.comments_point = comments_point;
    }

    public int getChat_points() {
        return chat_points;
    }

    public void setChat_points(int chat_points) {
        this.chat_points = chat_points;
    }

    public int getCashback_points() {
        return cashback_points;
    }

    public void setCashback_points(int cashback_points) {
        this.cashback_points = cashback_points;
    }

    public int getTotalPoints(){
        return  this.bonus_points + this.cashback_points + this.chat_points + this.comments_point + this.likes_point;
    }

}
