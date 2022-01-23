package com.alitaghiyev.alitaghiyevfinal.models;

public class Message {
    private String tarihsaat;
    private String kimden;
    private String mesaj;
    private String kime;


    public Message() {
    }

    public Message(String tarihsaat, String kimden, String mesaj, String kime) {
        this.mesaj = mesaj;
        this.kimden = kimden;
        this.kime = kime;
        this.tarihsaat = tarihsaat;
    }

    public String getMesaj() {
        return mesaj;
    }

    public void setMesaj(String mesaj) {
        this.mesaj = mesaj;
    }

    public String getKimden() {
        return kimden;
    }

    public void setKimden(String kimden) {
        this.kimden = kimden;
    }

    public String getKime() {
        return kime;
    }

    public void setKime(String kime) {
        this.kime = kime;
    }

    public String getTarihsaat() {
        return tarihsaat;
    }

    public void setTarihsaat(String tarihsaat) {
        this.tarihsaat = tarihsaat;
    }
}