package com.example.zornmusic;

import android.media.MediaPlayer;

public class MyMediaPlayer
{
    //Using static variable and methods to  be accessed outside the class using the class without creating objects
    static MediaPlayer instance;

    //Media Player instance will be created only once
    public static MediaPlayer getInstance()
    {
        if(instance == null)
        {
            instance = new MediaPlayer();
        }
        return instance;
    }

    public static int currentIndex = -1;
}
