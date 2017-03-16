package com.fame.plumbum.lithicsin.model;

/**
 * Created by pankaj on 23/7/16.
 */
public class ChatTable {
    private int id;
    private int status; // 1 means received, 2 means sent
    private String name, message, timestamp, chat_id;

    public ChatTable(int status, String chat_id, String name, String message, String timestamp){
        this.status = status;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
        this.chat_id = chat_id;
    }

    public ChatTable(int id, int status, String chat_id, String name, String message, String timestamp){
        this.id = id;
        this.status = status;
        this.name = name;
        this.message = message;
        this.timestamp = timestamp;
        this.chat_id = chat_id;
    }

    public int getStatus() {
        return status;
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

    public String getChat_id() {
        return chat_id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setStatus(int status) {
        this.status = status;
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

    public void setChat_id(String chat_id) {
        this.chat_id = chat_id;
    }
}
