package Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.zornmusic.Album;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper
{
    Context context;
    private static final String DATABASE_NAME = "musicStore.db";
    private static final String TABLE_NAME = "Songs";
    private static final String TABLE_NAME_2 = "Albums";
    private static final int DATABASE_VERSION = 1;

    private static String createSongTableQuery = "CREATE TABLE " + TABLE_NAME + " ( songID INTEGER PRIMARY KEY, " +
                                                                        "songName TEXT, " +
                                                                        "lang TEXT, " +
                                                                        "genre TEXT, " +
                                                                        "image BLOB, " +
                                                                        "audio BLOB ) ";

    private static String createAlbumTableQuery = "CREATE TABLE " + TABLE_NAME_2 + " ( albumID INTEGER PRIMARY KEY, " +
                                                                                      "albumName TEXT, " +
                                                                                      "image BLOB )" ;

    //To convert Bitmap to byte array to store in DB.
    private ByteArrayOutputStream objectByteArrayOutputStream;
    private byte[] imageInBytes;

    private byte[] audioInBytes;

    private byte[] albumImageInBytes;

    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        try
        {
            db.execSQL(createSongTableQuery);
            db.execSQL(createAlbumTableQuery);
            Toast.makeText(context, "Songs and albums table created successfully inside the DB!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1)
    {

    }

//    @Override
//    public void onUpgrade(SQLiteDatabase db, int i, int i1)
//    {
//        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
//        db.execSQL(query);
//
//        String query_2 = "DROP TABLE IF EXISTS " + TABLE_NAME_2;
//        db.execSQL(query_2);
//    }

    //CRUD of Songs
    public void insertSong(Songs songClass)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getWritableDatabase();
            Bitmap imageToStore = songClass.getImage();

            //To convert Bitmap to byte array and store in DB.
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
            imageInBytes = objectByteArrayOutputStream.toByteArray();

            audioInBytes = songClass.getAudio();

            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("songName", songClass.getSongName());
            objectContentValues.put("lang", songClass.getLang());
            objectContentValues.put("genre", songClass.getGenre());
            objectContentValues.put("image", imageInBytes);
            objectContentValues.put("audio", audioInBytes);

            long recordInsert =  objectSQLiteDB.insert(TABLE_NAME, null, objectContentValues);
            if(recordInsert != -1 )
            {
                Toast.makeText(context, "Song uploaded successfully" , Toast.LENGTH_SHORT).show();
                //Close the object opened in line 69
                objectSQLiteDB.close();
            }
            else
            {
                Toast.makeText(context, "Failed to upload song" , Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Songs> getAllSongsData()
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            ArrayList<Songs> objectSongsClassList = new ArrayList<>();

            Cursor objectCursor = objectSQLiteDB.rawQuery("SELECT * FROM " + TABLE_NAME, null);

            //If there are entries in the DB.
            if(objectCursor.getCount()!=0)
            {
                while (objectCursor.moveToNext())
                {
                    int songId = objectCursor.getInt(0);
                    String songName = objectCursor.getString(1);
                    String lang = objectCursor.getString(2);
                    String genre = objectCursor.getString(3);
                    byte[] imageBytes = objectCursor.getBlob(4);
                    byte[] audioBytes = objectCursor.getBlob(5);

                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    objectSongsClassList.add(new Songs(songId,songName, lang, genre, objectBitMap, audioBytes));
                }

                objectSQLiteDB.close();
                objectCursor.close();
                return objectSongsClassList;
            }
            else
            {
                Toast.makeText(context, "No songs uploaded yet" , Toast.LENGTH_SHORT).show();
                return objectSongsClassList;
            }
        }
        catch(Exception e)
        {
            //Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }

    public Songs getOneSongData(int id)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            Songs singleSong = null;

            String selectSong = "SELECT * FROM Songs WHERE songID = " + id;


            Cursor objectCursor = objectSQLiteDB.rawQuery(selectSong, null);

            //If there are entries in the DB.
            if(objectCursor.getCount()!=0)
            {
                while (objectCursor.moveToNext())
                {
                    int songId = objectCursor.getInt(0);
                    String songName = objectCursor.getString(1);
                    String lang = objectCursor.getString(2);
                    String genre = objectCursor.getString(3);
                    byte[] imageBytes = objectCursor.getBlob(4);
                    byte[] audioBytes = objectCursor.getBlob(5);

                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    singleSong = new Songs(songId,songName, lang, genre, objectBitMap, audioBytes);
                }

                objectSQLiteDB.close();
                objectCursor.close();
                return singleSong;
            }
            else
            {
                Toast.makeText(context, "No such song exists" , Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @SuppressLint("Range")
    public byte[] getAudio(int id)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            byte[] audioBytes = null;
            String selectAudio = "SELECT audio FROM Songs WHERE songID = " + id;

            Cursor objectCursor = objectSQLiteDB.rawQuery(selectAudio, null);

            //If there are entries in the DB.
            if(objectCursor.getCount()!=0)
            {
                while (objectCursor.moveToNext())
                {
                    audioBytes = objectCursor.getBlob(objectCursor.getColumnIndex("audio"));
                }
                objectSQLiteDB.close();
                objectCursor.close();
                return audioBytes;
            }
            else
            {
                Toast.makeText(context, "No songs uploaded yet" , Toast.LENGTH_SHORT).show();
                return null;
            }
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void editSong(int id, String name, String lang, String genre, byte[] imageInBytes)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getWritableDatabase();

            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("songID", id);
            objectContentValues.put("songName", name);
            objectContentValues.put("lang", lang);
            objectContentValues.put("genre", genre);
            objectContentValues.put("image", imageInBytes);

            long recordUpdate =  objectSQLiteDB.update(TABLE_NAME,objectContentValues, "songID = " + id,null);
            if(recordUpdate != -1 )
            {
                Toast.makeText(context, "Song details updated" , Toast.LENGTH_SHORT).show();
                objectSQLiteDB.close();
            }
            else
            {
                Toast.makeText(context, "Failed to edit data" , Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteSong(int id)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            long recordDelete = objectSQLiteDB.delete(TABLE_NAME, "songID = "+id, null);
            if(recordDelete!= -1)
            {
                Toast.makeText(context, "Uploaded song deleted" , Toast.LENGTH_SHORT).show();
                objectSQLiteDB.close();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }


    //CRUD of Albums
    public void insertAlbum(Albums albumClass)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getWritableDatabase();
            Bitmap imageToStore = albumClass.getAlbumImage();

            //To convert Bitmap to byte array and store in DB.
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
            albumImageInBytes = objectByteArrayOutputStream.toByteArray();

            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("albumName", albumClass.getAlbumName());
            objectContentValues.put("image", albumImageInBytes);

            long recordInsert =  objectSQLiteDB.insert(TABLE_NAME_2, null, objectContentValues);
            if(recordInsert != -1 )
            {
                Toast.makeText(context, "Album created successfully" , Toast.LENGTH_SHORT).show();
                //Close the object opened in line 69
                objectSQLiteDB.close();
            }
            else
            {
                Toast.makeText(context, "Failed to create album" , Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<Albums> getAllAlbumsData()
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            ArrayList<Albums> objectAlbumsClassList = new ArrayList<>();

            Cursor objectCursor = objectSQLiteDB.rawQuery("SELECT * FROM " + TABLE_NAME_2, null);

            //If there are entries in the DB.
            if(objectCursor.getCount()!=0)
            {
                while (objectCursor.moveToNext())
                {
                    int albumId = objectCursor.getInt(0);
                    String albumName = objectCursor.getString(1);
                    byte[] imageBytes = objectCursor.getBlob(2);

                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    objectAlbumsClassList.add(new Albums(albumId, albumName, objectBitMap));
                }

                objectSQLiteDB.close();
                objectCursor.close();
                return objectAlbumsClassList;
            }
            else
            {
                Toast.makeText(context, "No albums uploaded yet" , Toast.LENGTH_SHORT).show();
                return objectAlbumsClassList;
            }
        }
        catch(Exception e)
        {
            //Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }

    public void editAlbum(int id, String name, byte[] imageInBytes)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getWritableDatabase();

            ContentValues objectContentValues = new ContentValues();
            objectContentValues.put("albumID", id);
            objectContentValues.put("albumName", name);
            objectContentValues.put("image", imageInBytes);

            long recordUpdate =  objectSQLiteDB.update(TABLE_NAME_2, objectContentValues, "albumID = " + id,null);
            if(recordUpdate != -1 )
            {
                Toast.makeText(context, "Album details updated" , Toast.LENGTH_SHORT).show();
                objectSQLiteDB.close();
            }
            else
            {
                Toast.makeText(context, "Failed to edit data" , Toast.LENGTH_SHORT).show();
            }
        }
        catch (Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteAlbum(int id)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            long recordDelete = objectSQLiteDB.delete(TABLE_NAME_2, "albumID = "+id, null);
            if(recordDelete!= -1)
            {
                Toast.makeText(context, "Uploaded album deleted" , Toast.LENGTH_SHORT).show();
                objectSQLiteDB.close();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

}
