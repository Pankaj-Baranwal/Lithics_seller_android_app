package com.fame.plumbum.lithicsin.database;

/**
 * Created by pankaj on 23/7/16.
 */
public class ChatTable {
    private int id;
    private int status; // 1 means received, 2 means sent
    private String sender_id, name, message, timestamp;

    public ChatTable(int status, String sender_id, String name, String message, String timestamp){
        this.status = status;
        this.sender_id = sender_id;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatTable(int id, int status, String sender_id, String name, String message, String timestamp){
        this.id = id;
        this.status = status;
        this.sender_id = sender_id;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public String getMessage() {
        return message;
    }

    public String getName() {
        return name;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }
}
