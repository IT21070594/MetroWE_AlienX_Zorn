package Database;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Songs implements Serializable
{
    private int songID;
    private String songName;
    private String lang;
    private String genre;
    private Bitmap image;
    private byte[] audio;

    public Songs(int songID, String songName, String lang, String genre, Bitmap image, byte[] audio)
    {
        this.songID = songID;
        this.songName = songName;
        this.lang = lang;
        this.genre = genre;
        this.image = image;
        this.audio = audio;
    }

    public int getSongID()
    {
        return songID;
    }

    public void setSongID(int songID)
    {
        this.songID = songID;
    }

    public String getSongName()
    {
        return songName;
    }

    public void setSongName(String songName)
    {
        this.songName = songName;
    }

    public String getLang()
    {
        return lang;
    }

    public void setLang(String lang)
    {
        this.lang = lang;
    }

    public String getGenre()
    {
        return genre;
    }

    public void setGenre(String genre)
    {
        this.genre = genre;
    }

    public Bitmap getImage()
    {
        return image;
    }

    public void setImage(Bitmap image)
    {
        this.image = image;
    }

    public byte[] getAudio() {
        return audio;
    }

    public void setAudio(byte[] audio) {
        this.audio = audio;
    }
}

