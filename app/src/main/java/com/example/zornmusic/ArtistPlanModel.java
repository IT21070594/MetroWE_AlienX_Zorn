package com.example.zornmusic;

public class ArtistPlanModel {
    private String planName;
    private byte[]image;
    private int amount;
    private int noOfDownloads;
    private int noOfUploads;
    private String username;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public ArtistPlanModel(String planName, byte[] image, int amount, int noOfDownloads, int noOfUploads, String username) {
        this.planName = planName;
        this.image = image;
        this.amount = amount;
        this.noOfDownloads = noOfDownloads;
        this.noOfUploads = noOfUploads;
        this.username=username;
    }

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

    public int getNoOfUploads() {
        return noOfUploads;
    }

    public void setNoOfUploads(int noOfUploads) {
        this.noOfUploads = noOfUploads;
    }
}
