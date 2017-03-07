package com.fame.plumbum.lithicsin.database;

/**
 * Created by pankaj on 23/7/16.
 */
public class ChatTable {
    private int id;
    private int status; // 1 means received, 2 means sent
    private String remote_id, remote_name, message, timestamp;

    public ChatTable(int status, String remote_id, String remote_name, String message, String timestamp){
        this.status = status;
        this.remote_id = remote_id;
        this.remote_name = remote_name;
        this.message = message;
        this.timestamp = timestamp;
    }

    public ChatTable(int id, int status, String remote_id, String remote_name, String message, String timestamp){
        this.id = id;
        this.status = status;
        this.remote_id = remote_id;
        this.remote_name = remote_name;
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

    public String getMessage() {
        return message;
    }

    public String getRemote_id() {
        return remote_id;
    }

    public String getRemote_name() {
        return remote_name;
    }

    public void setRemote_name(String remote_name) {
        this.remote_name = remote_name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setRemote_id(String remote_id) {
        this.remote_id = remote_id;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
