package com.example.zornmusic;

import android.graphics.Bitmap;

public class ModelClass {
  int pid;
    String plName;
     Bitmap image;

    public ModelClass(int pid, String plName, Bitmap image) {
        this.pid = pid;
        this.plName = plName;
        this.image = image;
    }

    public int getPid() {
        return pid;
    }

    public void setPid(int pid) {
        this.pid = pid;
    }

    public String getPlName() {
        return plName;
    }

    public void setPlName(String plName) {
        this.plName = plName;
    }

    public Bitmap getImage() {
        return image;
    }

    public void setImage(Bitmap image) {
        this.image = image;
    }
}
