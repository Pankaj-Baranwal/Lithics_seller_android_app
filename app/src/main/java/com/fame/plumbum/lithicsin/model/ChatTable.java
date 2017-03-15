package com.fame.plumbum.lithicsin.model;

/**
 * Created by pankaj on 23/7/16.
 */
public class ChatTable {
    private int id;
    private int status; // 1 means received, 2 means sent
    private String remote_id, name, message, timestamp;

    public ChatTable(int status, String remote_id, String name, String message, String timestamp){
        this.status = status;
        this.remote_id = remote_id;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatTable(int id, int status, String remote_id, String name, String message, String timestamp){
        this.id = id;
        this.status = status;
        this.remote_id = remote_id;
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

    public String getremote_id() {
        return remote_id;
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

    public void setremote_id(String remote_id) {
        this.remote_id = remote_id;
    }
}
