package com.example.zornmusic;

public class DownloadModel {
    private int id;
    private byte[] proAvatar;
    private String name;

    //constructor

    public DownloadModel(int id, byte[] proAvatar, String name) {
        this.id = id;
        this.proAvatar = proAvatar;
        this.name = name;
    }

    //getters and setters

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public byte[] getProAvatar() {
        return proAvatar;
    }

    public void setProAvatar(byte[] proAvatar) {
        this.proAvatar = proAvatar;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
