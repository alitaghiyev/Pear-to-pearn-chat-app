package com.alitaghiyev.alitaghiyevfinal.models;

public class Friend {
    private String key;
    private String from;
    private String to;
    private String controller;

    public Friend() {
    }

    public Friend(String key, String from, String to, String controller) {
        this.key = key;
        this.from = from;
        this.to = to;
        this.controller = controller;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public String getController() {
        return controller;
    }

    public void setController(String controller) {
        this.controller = controller;
    }
}
