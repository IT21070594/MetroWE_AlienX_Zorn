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
    private String artistName;

    public Songs(int songID, String songName, String lang, String genre, Bitmap image, byte[] audio, String artistName)
    {
        this.songID = songID;
        this.songName = songName;
        this.lang = lang;
        this.genre = genre;
        this.image = image;
        this.audio = audio;
        this.artistName = artistName;
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

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}

