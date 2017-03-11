package com.fame.plumbum.lithicsin.model;

/**
 * Created by pankaj on 10/1/17.
 */

public class Orders {
    private String order_id;
    private double price;
    private String thumbnail;
    private String name;
    private String status;


    public Orders() {
    }

    public Orders(String order_id, double price, String thumbnail, String name, String status) {
        this.order_id = order_id;
        this.price = price;
        this.thumbnail = thumbnail;
        this.name = name;
        this.status = status;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getOrder_id() {
        return "#"+order_id;
    }

    public void setOrder_id(String order_id) {
        this.order_id = order_id;
    }

    public String getPrice() {
        return "Rs " + price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
