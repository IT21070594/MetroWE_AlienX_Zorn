package Database;

import android.graphics.Bitmap;

import java.io.Serializable;

public class Albums implements Serializable
{
    private int albumID;
    private String albumName;
    private Bitmap albumImage;
    private String artistName;

    public Albums(int albumID, String albumName, Bitmap albumImage,String artistName)
    {
        this.albumID = albumID;
        this.albumName = albumName;
        this.albumImage = albumImage;
        this.artistName = artistName;
    }

    public int getAlbumID() {
        return albumID;
    }

    public void setAlbumID(int albumID) {
        this.albumID = albumID;
    }

    public String getAlbumName() {
        return albumName;
    }

    public void setAlbumName(String albumName) {
        this.albumName = albumName;
    }

    public Bitmap getAlbumImage() {
        return albumImage;
    }

    public void setAlbumImage(Bitmap albumImage) {
        this.albumImage = albumImage;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setArtistName(String artistName) {
        this.artistName = artistName;
    }
}
