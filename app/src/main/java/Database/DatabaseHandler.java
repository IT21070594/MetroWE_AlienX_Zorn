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

    //Rashida's
    public static final String TABLE_NAME1 = "downloads";

    private static String createSongTableQuery = "CREATE TABLE " + TABLE_NAME + " ( songID INTEGER PRIMARY KEY, " +
                                                                        "songName TEXT, " +
                                                                        "lang TEXT, " +
                                                                        "genre TEXT, " +
                                                                        "image BLOB, " +
                                                                        "audio BLOB," +
                                                                        "artistName TEXT )";

    private static String createAlbumTableQuery = "CREATE TABLE " + TABLE_NAME_2 + " ( albumID INTEGER PRIMARY KEY, " +
                                                                                      "albumName TEXT, " +
                                                                                      "image BLOB , " +
                                                                                      "artistName TEXT )";

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


            //Nuha
            String CREATE_TABLE_STATEMENT = "CREATE TABLE "+ UserMasters.Users.TABLE_NAME + "(" +
                    UserMasters.Users.COLUMN1_UN + " TEXT PRIMARY KEY,"+UserMasters.Users.COLUMN2_EM+" TEXT,"+UserMasters.Users.COLUMN3_PW+" TEXT,"+UserMasters.Users.COLUMN4_AT+" TEXT)";

            String CREATE_TABLE_STATEMENT2="CREATE TABLE "+UserMasters.UserPlans.TABLE_NAME1 + "(" +
                    UserMasters.UserPlans.COLUMN1_PN + " TEXT PRIMARY KEY,"+UserMasters.UserPlans.COLUMN2_IM+" BLOB,"+ UserMasters.UserPlans.COLUMN3_AM+" INTEGER,"+UserMasters.UserPlans.COLUMN4_DL+" INTEGER)";

            String CREATE_TABLE_STATEMENT3="CREATE TABLE "+UserMasters.ArtistPlans.TABLE_NAME2 + "(" +
                    UserMasters.ArtistPlans.COLUMN1_PNA + " TEXT PRIMARY KEY,"+UserMasters.ArtistPlans.COLUMN2_IMA+" BLOB,"+ UserMasters.ArtistPlans.COLUMN3_AMA+" INTEGER,"+UserMasters.ArtistPlans.COLUMN4_DLA+" INTEGER,"+UserMasters.ArtistPlans.COLUMN5_ULA+" INTEGER)";

            String CREATE_TABLE_STATEMENT4="CREATE TABLE "+UserMasters.PremiumUsers.TABLE_NAME3 + "(" +UserMasters.PremiumUsers._ID+" INTEGER PRIMARY KEY,"+
                    UserMasters.PremiumUsers.COLUMN1_UN + " TEXT NOT NULL,"+UserMasters.PremiumUsers.COLUMN2_PT + " TEXT NOT NULL )";

            String CREATE_TABLE_STATEMENT5="CREATE TABLE "+UserMasters.ArtistUsers.TABLE_NAME4 + "(" +UserMasters.ArtistUsers._ID+" INTEGER PRIMARY KEY,"+
                    UserMasters.ArtistUsers.COLUMN1_UN + " TEXT NOT NULL,"+UserMasters.ArtistUsers.COLUMN2_PT + " TEXT NOT NULL )";

            String CREATE_TABLE_STATEMENT6="CREATE TABLE "+UserMasters.Payments.TABLE_NAME5 + "(" +UserMasters.Payments._ID+" INTEGER PRIMARY KEY,"+
                    UserMasters.Payments.COLUMN1_UN + " TEXT NOT NULL,"+UserMasters.Payments.COLUMN2_AM + " INTEGER NOT NULL,"+UserMasters.Payments.COLUMN3_DT+ " TEXT,"+UserMasters.Payments.COLUMN4_PN+ " TEXT )";
            db.execSQL(CREATE_TABLE_STATEMENT);
            db.execSQL(CREATE_TABLE_STATEMENT2);
            db.execSQL(CREATE_TABLE_STATEMENT3);
            db.execSQL(CREATE_TABLE_STATEMENT4);
            db.execSQL(CREATE_TABLE_STATEMENT5);
            db.execSQL(CREATE_TABLE_STATEMENT6);

            //Rashida's
            String query2 = "create table "+TABLE_NAME1+ "(id integer primary key, image blob,song text,username text)";
            db.execSQL(query2);

            Toast.makeText(context, "Songs and albums table created successfully inside the DB!", Toast.LENGTH_SHORT).show();
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        String query = "DROP TABLE IF EXISTS " + TABLE_NAME;
        db.execSQL(query);

        String query_2 = "DROP TABLE IF EXISTS " + TABLE_NAME_2;
        db.execSQL(query_2);

        //Nuha
        db.execSQL("drop Table if exists "+UserMasters.Users.TABLE_NAME);
        db.execSQL("drop Table if exists "+UserMasters.UserPlans.TABLE_NAME1);
        db.execSQL("drop Table if exists "+UserMasters.ArtistPlans.TABLE_NAME2);
        db.execSQL("drop Table if exists "+UserMasters.PremiumUsers.TABLE_NAME3);
        db.execSQL("drop Table if exists "+UserMasters.ArtistUsers.TABLE_NAME4);

        //Rashida's
        String query2 = "drop table if exists "+TABLE_NAME1+"";
        db.execSQL(query2);

    }

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
            objectContentValues.put("artistName", songClass.getArtistName());

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
                    String artistName = objectCursor.getString(6);


                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    objectSongsClassList.add(new Songs(songId,songName, lang, genre, objectBitMap, audioBytes, artistName));
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

    public ArrayList<Songs> getArtistSongsData(String artistName)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            ArrayList<Songs> objectSongsClassList = new ArrayList<>();

//            String selection = UserMasters.Users.COLUMN1_UN + "=?";
//
//            String []selectionArgs ={username};
//            Cursor cursor = db.rawQuery("Select * from "+UserMasters.Users.TABLE_NAME+" where "+selection,selectionArgs);
            String selection = "artistName =?";
            String []selectionArgs = {artistName};
            Cursor objectCursor = objectSQLiteDB.rawQuery("SELECT * FROM Songs WHERE "+selection, selectionArgs);

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
                    String name = objectCursor.getString(6);


                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    objectSongsClassList.add(new Songs(songId,songName, lang, genre, objectBitMap, audioBytes, name));
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
                    String artistName = objectCursor.getString(6);

                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    singleSong = new Songs(songId,songName, lang, genre, objectBitMap, audioBytes, artistName);
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
            objectContentValues.put("artistName", albumClass.getArtistName());

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
                    String artistName = objectCursor.getString(3);

                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    objectAlbumsClassList.add(new Albums(albumId, albumName, objectBitMap, artistName ));
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

    public ArrayList<Albums> getArtistAlbumsData(String artistName)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();
            ArrayList<Albums> objectAlbumsClassList = new ArrayList<>();
            String selection = "artistName =?";
            String []selectionArgs = {artistName};
            Cursor objectCursor = objectSQLiteDB.rawQuery("SELECT * FROM Albums WHERE "+selection, selectionArgs);

//            String selectSongs = "SELECT * FROM  WHERE artistName = " + artistName;
//            Cursor objectCursor = objectSQLiteDB.rawQuery(selectSongs, null);

            //If there are entries in the DB.
            if(objectCursor.getCount()!=0)
            {
                while (objectCursor.moveToNext())
                {
                    int albumId = objectCursor.getInt(0);
                    String albumName = objectCursor.getString(1);
                    byte[] imageBytes = objectCursor.getBlob(2);
                    String name = objectCursor.getString(3);

                    Bitmap objectBitMap = BitmapFactory.decodeByteArray(imageBytes,0, imageBytes.length);
                    objectAlbumsClassList.add(new Albums(albumId, albumName, objectBitMap, name));
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

    //Nuha's Crud

    public boolean insertData(String username,String password,String email,String accountType){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserMasters.Users.COLUMN1_UN,username);
        values.put(UserMasters.Users.COLUMN3_PW,password);
        values.put(UserMasters.Users.COLUMN2_EM,email);
        values.put(UserMasters.Users.COLUMN4_AT,accountType);

        long result = MyDB.insert(UserMasters.Users.TABLE_NAME,null,values);
        if (result==-1) {return false;}
        else
        {return true;}
    }
    public boolean insertUserPlan(String username,String plan){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserMasters.PremiumUsers.COLUMN1_UN,username);
        values.put(UserMasters.PremiumUsers.COLUMN2_PT,plan);

        long result = MyDB.insert(UserMasters.PremiumUsers.TABLE_NAME3,null,values);
        if(result==-1){return false;}
        else { return true;}
    }
    public boolean insertArtistPlan(String username,String plan){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserMasters.ArtistUsers.COLUMN1_UN,username);
        values.put(UserMasters.ArtistUsers.COLUMN2_PT,plan);

        long result = MyDB.insert(UserMasters.ArtistUsers.TABLE_NAME4,null,values);
        if(result==-1){return false;}
        else { return true;}
    }
    public boolean insertPayment(String username, int amount, String date, String planname){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(UserMasters.Payments.COLUMN1_UN,username);
        values.put(UserMasters.Payments.COLUMN2_AM,amount);
        values.put(UserMasters.Payments.COLUMN3_DT,String.valueOf(date));
        values.put(UserMasters.Payments.COLUMN4_PN,planname);

        long result = MyDB.insert(UserMasters.Payments.TABLE_NAME5,null,values);
        if ( result==-1){return false;}
        else {return true;}
    }

    public Boolean checkUserName(String username) {
        SQLiteDatabase MyDB = this.getWritableDatabase();
        String selection = UserMasters.Users.COLUMN1_UN + "=?";

        String [] selectionArgs ={username};
        // Cursor cursor = db.rawQuery("Select * from "+UserMaster.Users.TABLE_NAME+" where "+selection,selectionArgs);

        Cursor cursor = MyDB.rawQuery("select * from "+UserMasters.Users.TABLE_NAME+" where "+selection, selectionArgs);
        if (cursor.getCount() > 0)
            return true;
        else
            return false;
    }

    public Boolean checkUserNamePassword(String username, String password){
        SQLiteDatabase MyDB = this.getWritableDatabase();
        Cursor cursor =  MyDB.rawQuery("select * from "+UserMasters.Users.TABLE_NAME+" where "+UserMasters.Users.COLUMN1_UN+" =? and "+UserMasters.Users.COLUMN3_PW+"=?",new String[] {username,password});

        if(cursor.getCount()>0)
            return true;
        else
            return false;

    }
    public Boolean updateInfo(String username,String email, String password){
        SQLiteDatabase db= getWritableDatabase();
        String selection = UserMasters.Users.COLUMN1_UN + "=?";
        String [] selectionArgs ={username};
        ContentValues values = new ContentValues();
//    values.put(UserMaster.Users.COLUMN1_UN, username);
        values.put(UserMasters.Users.COLUMN3_PW, password);
        values.put(UserMasters.Users.COLUMN2_EM,email);


        Cursor cursor = db.rawQuery("Select * from "+UserMasters.Users.TABLE_NAME+" where "+selection,selectionArgs);
        if(cursor.getCount()>0) {


//        insert method returns an integer if successful long is used in tutu to capture the value no point anyway since add info returns void
            long result = db.update(UserMasters.Users.TABLE_NAME, values, selection,selectionArgs);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }
    public boolean updateUserAccType(String username,String acctype){
        SQLiteDatabase db= getWritableDatabase();
        String selection = UserMasters.Users.COLUMN1_UN + "=?";
        String [] selectionArgs ={username};
        ContentValues values = new ContentValues();
        values.put(UserMasters.Users.COLUMN4_AT,acctype);
        Cursor cursor = db.rawQuery("Select * from "+UserMasters.Users.TABLE_NAME+" where "+selection,selectionArgs);
        if(cursor.getCount()>0) {


//        insert method returns an integer if successful long is used in tutu to capture the value no point anyway since add info returns void
            long result = db.update(UserMasters.Users.TABLE_NAME, values, selection,selectionArgs);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }

    }
    public Boolean deleteInfo(String username){
        SQLiteDatabase db= getWritableDatabase();
        String selection = UserMasters.Users.COLUMN1_UN + "=?";

        String []selectionArgs ={username};
        Cursor cursor = db.rawQuery("Select * from "+UserMasters.Users.TABLE_NAME+" where "+selection,selectionArgs);
        if(cursor.getCount()>0) {


//        insert method returns an integer if successful long is used in tutu to capture the value no point anyway since add info returns void
            long result = db.delete(UserMasters.Users.TABLE_NAME, selection, selectionArgs);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }

    public Cursor getInfo(String username) {
        SQLiteDatabase db = getWritableDatabase();
        String selection = UserMasters.Users.COLUMN1_UN + "=?";

        String []selectionArgs ={username};
        Cursor cursor = db.rawQuery("Select * from "+UserMasters.Users.TABLE_NAME+" where "+selection,selectionArgs);
        return cursor;
    }
    public Cursor getPremiumPlanInfo(String username){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selection = UserMasters.PremiumUsers.COLUMN1_UN+"=?";
        String []selectionArgs = {username};
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+UserMasters.PremiumUsers.TABLE_NAME3+" where "+selection,selectionArgs);
        return cursor;
    }
    public Cursor getArtistPlanInfo(String username){
        SQLiteDatabase sqLiteDatabase = getWritableDatabase();
        String selection = UserMasters.ArtistUsers.COLUMN1_UN+"=?";
        String []selectionArgs = {username};
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+UserMasters.ArtistUsers.TABLE_NAME4+" where "+selection,selectionArgs);
        return cursor;
    }
    public Cursor getArtistPlanDetails(String planname){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String selection = UserMasters.ArtistPlans.COLUMN1_PNA+"=?";
        String []selectionArgs = {planname};
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+UserMasters.ArtistPlans.TABLE_NAME2+" where "+selection,selectionArgs);
        return cursor;
    }
    public Cursor getPremiumPlanDetails(String planname){
        SQLiteDatabase sqLiteDatabase=getWritableDatabase();
        String selection = UserMasters.UserPlans.COLUMN1_PN+"=?";
        String []selectionArgs = {planname};
        Cursor cursor = sqLiteDatabase.rawQuery("Select * from "+UserMasters.UserPlans.TABLE_NAME1+" where "+selection,selectionArgs);
        return cursor;
    }
    public Boolean deletePremiumUserInfo(String username){
        SQLiteDatabase db= getWritableDatabase();
        String selection = UserMasters.PremiumUsers.COLUMN1_UN + "=?";

        String []selectionArgs ={username};
        Cursor cursor = db.rawQuery("Select * from "+UserMasters.PremiumUsers.TABLE_NAME3+" where "+selection,selectionArgs);
        if(cursor.getCount()>0) {


//        insert method returns an integer if successful long is used in tutu to capture the value no point anyway since add info returns void
            long result = db.delete(UserMasters.PremiumUsers.TABLE_NAME3, selection, selectionArgs);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }
    public Boolean deleteArtistUserInfo(String username){
        SQLiteDatabase db= getWritableDatabase();
        String selection = UserMasters.ArtistUsers.COLUMN1_UN + "=?";

        String []selectionArgs ={username};
        Cursor cursor = db.rawQuery("Select * from "+UserMasters.ArtistUsers.TABLE_NAME4+" where "+selection,selectionArgs);
        if(cursor.getCount()>0) {


//        insert method returns an integer if successful long is used in tutu to capture the value no point anyway since add info returns void
            long result = db.delete(UserMasters.ArtistUsers.TABLE_NAME4, selection, selectionArgs);
            if (result == -1) {
                return false;
            } else {
                return true;
            }
        }else{
            return false;
        }
    }



}
