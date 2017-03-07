package com.fame.plumbum.lithicsin.database;

/**
 * Created by pankaj on 25/8/16.
 */
public class NotifTable {
    private String message, timestamp;
    private int id;


    public NotifTable(String message, String timestamp){
        this.message = message;
        this.timestamp = timestamp;
    }

    public NotifTable(int id, String message, String timestamp){
        this.id = id;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}