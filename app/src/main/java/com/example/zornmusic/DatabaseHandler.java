package com.example.zornmusic;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.widget.Toast;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class DatabaseHandler extends SQLiteOpenHelper
{

    Context context;
    int pid=0;
    public static final String DATABASE_NAME = "musicStore.db";
    public static  final int DATABASE_VERSION=1;
    public ByteArrayOutputStream objectByteArrayOutputStream;
    public byte[] imageInByte;
    public static final String TABLE1= "Playlist";


    public DatabaseHandler(Context context)
    {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
        SQLiteDatabase db = this.getWritableDatabase();
    }

    @Override

    public void onCreate(SQLiteDatabase db)
    {

      String Query = "create table " + TABLE1 + "(pid integer primary key, plName text, image blob)";
        try{

            db.execSQL(Query);
            Toast.makeText(context, "Table created successfully", Toast.LENGTH_SHORT).show();
        }catch ( Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }


    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1)
    {
        String query = "drop table if exists "+TABLE1+"";
        db.execSQL(query);
    }

    public void insertPlaylist(ModelClass objectModelClass){
        try{

            SQLiteDatabase objSQLiteDatabase =this.getWritableDatabase();
            Bitmap imageToStoreBitmap = objectModelClass.getImage();
            objectByteArrayOutputStream = new ByteArrayOutputStream();
            imageToStoreBitmap.compress(Bitmap.CompressFormat.JPEG,100,objectByteArrayOutputStream);
            imageInByte= objectByteArrayOutputStream.toByteArray();
            ContentValues objectContentValues= new ContentValues();
//            objectContentValues.put("pid" , objectModelClass.getPid());
            objectContentValues.put("plName",objectModelClass.getPlName());
            objectContentValues.put("image",imageInByte);

            Long checkIfQueryRuns=objSQLiteDatabase.insert(TABLE1,null,objectContentValues);
            if(checkIfQueryRuns != null){
                Toast.makeText(context,"Data Added",Toast.LENGTH_SHORT).show();
                objSQLiteDatabase.close();
            }
            else{
                Toast.makeText(context,"Data did NOT add",Toast.LENGTH_SHORT).show();

            }

        }catch ( Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public ArrayList<ModelClass> getAllPlaylistData(){
        try {

            SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> objectModelClassList = new ArrayList<>();
            Cursor objectCursor = objSQLiteDatabase.rawQuery("select *from "+TABLE1+"", null);
            if (objectCursor.getCount() != 0) {
                while (objectCursor.moveToNext()) {
                    int pid = objectCursor.getInt(0);
                    String plName = objectCursor.getString(1);
                    byte[] imageBytes = objectCursor.getBlob(2);
                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    objectModelClassList.add(new ModelClass(pid, plName, objectBitmap));
                }
                objSQLiteDatabase.close();
                objectCursor.close();
//                RecyclerView recyclerView = new RecyclerView(this,R.layout.plrecycleview_row,objectModelClass,objSQLiteDatabase);

                return objectModelClassList;

            } else {
                Toast.makeText(context, "Table created successfully", Toast.LENGTH_SHORT).show();
                return objectModelClassList;
            }
            }catch ( Exception e){
            Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    public void deletePlaylist(int id)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getReadableDatabase();

            long recordDelete = objectSQLiteDB.delete(TABLE1, "pid = "+id, null);
            if(recordDelete!= -1)
            {
                Toast.makeText(context, "Playlist deleted" , Toast.LENGTH_SHORT).show();
                objectSQLiteDB.close();
            }
        }
        catch(Exception e)
        {
            Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
        }
    }


    public ArrayList<ModelClass> getModelData(String pName)
    {
        try
        {
            SQLiteDatabase objSQLiteDatabase = this.getReadableDatabase();
            ArrayList<ModelClass> objectModelClassList = new ArrayList<>();


            String selectSongs = "SELECT * FROM Songs WHERE plName = " + pName;
            Cursor objectCursor = objSQLiteDatabase.rawQuery("select *from "+TABLE1+"", null);

            //If there are entries in the DB.
            if(objectCursor.getCount()!=0)
            {
                while (objectCursor.moveToNext())
                {

                    int pid = objectCursor.getInt(0);
                    String plName = objectCursor.getString(1);
                    byte[] imageBytes = objectCursor.getBlob(2);
                    Bitmap objectBitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    objectModelClassList.add(new ModelClass(pid, plName, objectBitmap));
                     }

                objSQLiteDatabase.close();
                objectCursor.close();
                return objectModelClassList;
            }
            else
            {
                Toast.makeText(context, "no Playlist Created" , Toast.LENGTH_SHORT).show();
                return objectModelClassList;


            }
        }
        catch(Exception e)
        {
            //Toast.makeText(context, e.getMessage() , Toast.LENGTH_SHORT).show();
            e.printStackTrace();
            return null;
        }
    }





    public void editPlaylist(int id, String name, byte[] imageInBytes)
    {
        try
        {
            SQLiteDatabase objectSQLiteDB = this.getWritableDatabase();

            ContentValues objectContentValues = new ContentValues();

            objectContentValues.put("pid" , id);
            objectContentValues.put("plName",name);
            objectContentValues.put("image",imageInBytes);


            long recordUpdate =  objectSQLiteDB.update(TABLE1, objectContentValues, "pid = " + id,null);
            if(recordUpdate != -1 )
            {
                Toast.makeText(context, "Playlist details updated" , Toast.LENGTH_SHORT).show();
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

}


