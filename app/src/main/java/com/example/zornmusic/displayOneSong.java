package com.example.zornmusic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import Database.DatabaseHandler;


public class displayOneSong extends AppCompatActivity {
SQLiteDatabase sqLiteDatabase;
DatabaseHandler dBmain;

int id = 0;
ImageView songPic;
TextView songName;
ImageButton backBtn;
Intent receiveIntent1;
ArrayList<DownloadModel> downloadModels;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_one_song);

        receiveIntent1=getIntent();
        String name=receiveIntent1.getStringExtra("user");
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(getApplicationContext(),Home.class);
                        intent1.putExtra("user",name);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myLibrary:
                        return true;
                    case R.id.uploads:

                        uploadButtonGo();
                        return true;
                }
                return false;
            }
        });
        dBmain = new DatabaseHandler(this);
        songPic = findViewById(R.id.playDMusicImg);
        songName = findViewById(R.id.songDname);
        backBtn =  findViewById(R.id.backDBtn);
        Bundle bundle = getIntent().getBundleExtra("oneSong");
        id = bundle.getInt("songId");

        //DownloadModel downloadModel = getOneSong(id);
        byte[]bytes = bundle.getByteArray("avatar");

        //byte[]bytes = downloadModel.getProAvatar();
        Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
        songPic.setImageBitmap(bitmap);

        //songName.setText(downloadModel.getName());
        songName.setText(bundle.getString("songName"));
        getOneSong(id);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(displayOneSong.this,DisplayDownloads.class));
            }
        });


    }
    private void getOneSong(int id) {
        sqLiteDatabase=dBmain.getReadableDatabase();
        //DownloadModel model = null;
       // Cursor cursor=sqLiteDatabase.rawQuery("select *from "+TABLE_NAME1+"id="+id,null);
        Cursor cursor = sqLiteDatabase.rawQuery("SELECT * FROM downloads WHERE id='id'", null);

        // Cursor cursor=sqLiteDatabase.rawQuery("select *from "+TABLE_NAME1+"id="+id,null);
        downloadModels=new ArrayList<>();
        if(cursor.getCount()!=0) {

            while (cursor.moveToNext()) {
                int songId = cursor.getInt(0);
                byte[] avatar = cursor.getBlob(1);
                String name = cursor.getString(2);
                downloadModels.add(new DownloadModel(songId,avatar,name));
              // model=  new DownloadModel(songId,avatar,name);

            }
            cursor.close();

            //return model;
      }
//        else{
//            return model;
//        }
    }

    public void uploadButtonGo(){
        receiveIntent1=getIntent();
        String name=receiveIntent1.getStringExtra("user");
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(name);
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        String accType = String.valueOf(buffer);
        //TextView t1 = findViewById(R.id.textView3);
        //t1.setText(accType);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";

        if(c3.compareTo(accType)==0){
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",name);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else if(c2.compareTo(accType)==0){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0,0);
        }else{
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",name);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }
    }
}