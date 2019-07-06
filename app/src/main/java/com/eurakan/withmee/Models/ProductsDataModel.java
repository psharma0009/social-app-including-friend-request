package com.eurakan.withmee.Models;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by Admin on 1/27/2019.
 */

public class ProductsDataModel {

    public int id, cat_id_brand, weight, price, quantity, sold, purchased_price,marchent_id,availeble_product, product_color_id,product_quantity_id;
    public String productName, avatar,weight_type, status, active,desc,product_image, category_name, category_image;
    public Date date_and_time;
    DateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);

    public ProductsDataModel(int id, int price, int purchased_price, String productName, String weight_type, String status, String active, String product_image, String category_name, String date_and_time) throws Exception{
        this.id = id;
        this.price = price;
        this.purchased_price = purchased_price;
        this.productName = productName;
        this.weight_type = weight_type;
        this.status = status;
        this.active = active;
        this.product_image = product_image;
        this.category_name = category_name;
        this.date_and_time = date_and_time != null && date_and_time != "" ? format.parse(date_and_time) : format.getCalendar().getTime();
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCat_id_brand() {
        return cat_id_brand;
    }

    public void setCat_id_brand(int cat_id_brand) {
        this.cat_id_brand = cat_id_brand;
    }

    public int getWeight() {
        return weight;
    }

    public void setWeight(int weight) {
        this.weight = weight;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getSold() {
        return sold;
    }

    public void setSold(int sold) {
        this.sold = sold;
    }

    public int getPurchased_price() {
        return purchased_price;
    }

    public void setPurchased_price(int purchased_price) {
        this.purchased_price = purchased_price;
    }

    public int getMarchent_id() {
        return marchent_id;
    }

    public void setMarchent_id(int marchent_id) {
        this.marchent_id = marchent_id;
    }

    public int getAvaileble_product() {
        return availeble_product;
    }

    public void setAvaileble_product(int availeble_product) {
        this.availeble_product = availeble_product;
    }

    public int getProduct_color_id() {
        return product_color_id;
    }

    public void setProduct_color_id(int product_color_id) {
        this.product_color_id = product_color_id;
    }

    public int getProduct_quantity_id() {
        return product_quantity_id;
    }

    public void setProduct_quantity_id(int product_quantity_id) {
        this.product_quantity_id = product_quantity_id;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getWeight_type() {
        return weight_type;
    }

    public void setWeight_type(String weight_type) {
        this.weight_type = weight_type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getActive() {
        return active;
    }

    public void setActive(String active) {
        this.active = active;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getProduct_image() {
        return product_image;
    }

    public void setProduct_image(String product_image) {
        this.product_image = product_image;
    }

    public String getCategory_name() {
        return category_name;
    }

    public void setCategory_name(String category_name) {
        this.category_name = category_name;
    }

    public String getCategory_image() {
        return category_image;
    }

    public void setCategory_image(String category_image) {
        this.category_image = category_image;
    }

    public Date getDate_and_time() {
        return date_and_time;
    }

    public void setDate_and_time(Date date_and_time) {
        this.date_and_time = date_and_time;
    }
}
