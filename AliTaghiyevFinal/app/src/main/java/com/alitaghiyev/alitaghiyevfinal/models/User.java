package com.alitaghiyev.alitaghiyevfinal.models;

import java.io.Serializable;

public class User implements Serializable {
    private String uid;
    private String adsoyad;
    private String email;
    private String parola;
    private String fotograf;

    public User() {
    }

    public User(String uid, String fotograf, String adsoyad, String email, String parola) {
        this.uid = uid;
        this.fotograf = fotograf;
        this.adsoyad = adsoyad;
        this.email = email;
        this.parola = parola;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getFotograf() {
        return fotograf;
    }

    public void setFotograf(String fotograf) {
        this.fotograf = fotograf;
    }

    public String getAdsoyad() {
        return adsoyad;
    }

    public void setAdsoyad(String adsoyad) {
        this.adsoyad = adsoyad;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getParola() {
        return parola;
    }

    public void setParola(String parola) {
        this.parola = parola;
    }
}
