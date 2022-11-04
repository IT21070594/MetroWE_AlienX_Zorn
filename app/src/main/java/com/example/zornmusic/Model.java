package com.example.zornmusic;
//uPlan(PlanName,Image,amount,noOfSongsDownload)

public class Model {
    private String planName;
    private byte[]image;
    private int amount;
    private int noOfDownloads;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    private String username;
    //constructor

    public Model(String planName, byte[] image, int amount, int noOfDownloads, String username) {
        this.planName = planName;
        this.image = image;
        this.amount = amount;
        this.noOfDownloads = noOfDownloads;
        this.username = username;
    }
    //getters and setters

    public String getPlanName() {
        return planName;
    }

    public void setPlanName(String planName) {
        this.planName = planName;
    }

    public byte[] getImage() {
        return image;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public int getNoOfDownloads() {
        return noOfDownloads;
    }

    public void setNoOfDownloads(int noOfDownloads) {
        this.noOfDownloads = noOfDownloads;
    }
}
