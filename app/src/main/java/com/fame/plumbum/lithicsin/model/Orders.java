package com.fame.plumbum.lithicsin.model;

/**
 * Created by pankaj on 10/1/17.
 */

public class Orders {
    private String order_id;
    private int price;
    private int thumbnail;

    public Orders() {
    }

    public Orders(String order_id, int price, int thumbnail) {
        this.order_id = order_id;
        this.price = price;
        this.thumbnail = thumbnail;
    }

    public String getName() {
        return order_id;
    }

    public void setName(String order_id) {
        this.order_id = order_id;
    }

    public int getNumOfSongs()
    {
        return price;
    }

    public void setNumOfSongs(int price) {
        this.price = price;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }
}
