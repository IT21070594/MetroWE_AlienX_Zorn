package com.example.zornmusic;




import static Database.DatabaseHandler.TABLE_NAME1;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

import Database.DatabaseHandler;
import Database.Songs;


public class DownloadSong extends AppCompatActivity {
    DatabaseHandler dBmain;
    SQLiteDatabase sqLiteDatabase;
    ImageView avatar,download_logo;
    TextView name;

    Button download,cancel,edit;
    AlertDialog dialog;
    EditText editText;
    Intent receiveIntent;
    int id = 0;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        receiveIntent=getIntent();
         username=receiveIntent.getStringExtra("user");
        setContentView(R.layout.activity_download_song);
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set library Selected
        bottomNavigationView.setSelectedItemId(R.id.myLibrary);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(getApplicationContext(),Home.class);
                        intent1.putExtra("user",username);
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
        findid();
downloadData();
insertData();
//editData();


    }public void uploadButtonGo(){
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(username);
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        res.close();
        String accType = String.valueOf(buffer);
        //TextView t1 = findViewById(R.id.textView3);
        //t1.setText(accType);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";

        if(c3.compareTo(accType)==0){
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else if(c2.compareTo(accType)==0){
            Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else{
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }
    }

////    private void editData() {
//////        if (getIntent().getBundleExtra("songDetails")!=null){
//////            Bundle bundle =getIntent().getBundleExtra("songDetails");
//////            id=bundle.getInt("song_id");
//////            //for image
//////            Databa
//////            byte[]bytes=bundle.getByteArray("avatar");
//////            Bitmap bitmap = BitmapFactory.decodeByteArray(bytes,0,bytes.length);
//////            avatar.setImageBitmap(bitmap);
//////            // for set song name
//////            name.setText(bundle.getString("songName"));
//////            //visible edit button and hide download button
//////            download.setVisibility(View.GONE);
//////            edit.setVisibility(View.VISIBLE);
//////            download_logo.setVisibility(View.GONE);
////        }
//    }


    private void downloadData() {
        if(getIntent().getBundleExtra("userDetails")!=null){
            Bundle bundle = getIntent().getBundleExtra("userDetails");
            id=bundle.getInt("song_id");
            DatabaseHandler db = new DatabaseHandler(this);
            Songs song=db.getOneSongData(id);
            //for image
            byte[]bytes = bundle.getByteArray("avatar");
            Bitmap bitmap = song.getImage();
            avatar.setImageBitmap(bitmap);
            //for set name
            name.setText(song.getSongName());

        }
    }




    private void insertData() {
        receiveIntent=getIntent();
        download.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put("image", ImageViewToByte(avatar));
                cv.put("song", name.getText().toString());
                cv.put("username",receiveIntent.getStringExtra("user"));
                sqLiteDatabase = dBmain.getWritableDatabase();
               Long result = sqLiteDatabase.insert(TABLE_NAME1,null,cv);
//
                if (result == -1) {
                    Toast.makeText(DownloadSong.this, " not downloaded successfully", Toast.LENGTH_SHORT).show();


                }
                else{
                    Toast.makeText(DownloadSong.this, "downloaded successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DownloadSong.this,DisplayDownloads.class));
                    download_logo.setVisibility(View.VISIBLE);
                    //clear when click on submit button
//                    avatar.setImageResource(R.mipmap.ic_launcher);
//                    name.setText("");
                }
            }
        });
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                startActivity(new Intent(DownloadSong.this,DisplayDownloads.class));
//            }
//        });
        //for storing the updated data
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues cv = new ContentValues();
                cv.put("image",ImageViewToByte(avatar));
                cv.put("song",name.getText().toString());
                sqLiteDatabase=dBmain.getWritableDatabase();
                long songEdit = sqLiteDatabase.update(TABLE_NAME1,cv,"id="+id,null);
                if(songEdit != -1){
                    Toast.makeText(DownloadSong.this,"updated successfully", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(DownloadSong.this,DisplayDownloads.class));
                    //clear data after editing
                    avatar.setImageResource(R.mipmap.ic_launcher);
                    name.setText("");
                    //edit hide and download button visible
                    edit.setVisibility(View.GONE);
                    download.setVisibility(View.VISIBLE);

                }
            }
        });
    }

    private byte[] ImageViewToByte(ImageView avatar) {
        Bitmap bitmap = ((BitmapDrawable)avatar.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        byte[] bytes =stream.toByteArray();
        return bytes;
    }

    private void findid(){
        avatar = (ImageView)findViewById(R.id.download_avatar);
        name=(TextView) findViewById(R.id.download_song_name);
        download_logo=(ImageView)findViewById(R.id.downloadLogo);
        dialog = new AlertDialog.Builder(this).create();
        editText = new EditText(this);

        dialog.setTitle("Edit song name");
        dialog.setView(editText);
        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "SAVE SONG NAME", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
name.setText(editText.getText());
            }
        });
        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editText.setText(name.getText());
                dialog.show();
            }
        });
        download = (Button)findViewById(R.id.btn_download);

        edit = (Button)findViewById(R.id.btn_download_edit);



    }


}