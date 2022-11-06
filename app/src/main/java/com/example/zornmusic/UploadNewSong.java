package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import Database.DatabaseHandler;
import Database.Songs;

public class UploadNewSong extends AppCompatActivity
{
    String artistName;
    Intent intent;

    EditText songName;
    EditText lang;
    EditText genre;

    int id = 0;

    ImageView songPhoto;
    //Any number other than 0 can be assigned.
    private static final int PICK_IMAGE_REQUEST = 100;

    private static final int CAMERA_REQUEST = 101;
    public static final int STORAGE_REQUEST= 102;

    String[] cameraPermission;
    String[] storagePermission;

    private Uri imageFilePath;
    private Bitmap imageToStore;

    TextView selectAudio;
    private static final int PICK_AUDIO = 1;
    private Uri audioFile;
    private byte[] audioToStore;
    Intent receiveIntent;
    DatabaseHandler objectDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_new_song);

        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        receiveIntent=getIntent();
        String name=receiveIntent.getStringExtra("user");
        System.out.println(receiveIntent.getStringExtra("user"));
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Uploads Selected
        bottomNavigationView.setSelectedItemId(R.id.uploads);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent = new Intent(getApplicationContext(),Home.class);
                        intent.putExtra("user",artistName);
                        startActivity(intent);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myLibrary:
                        Intent intent1 = new Intent(getApplicationContext(),Library.class);
                        intent1.putExtra("user",artistName);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:
                        return true;
                }
                return false;
            }
        });
        Button cancelBtn = findViewById(R.id.cancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UploadNewSong.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        try
        {
            songName = findViewById(R.id.songNameInput);
            lang = findViewById(R.id.langInput);
            genre = findViewById(R.id.genreInput);
            songPhoto = findViewById(R.id.photoInput);
            selectAudio = findViewById(R.id.audioInput);

            intent = getIntent();
            artistName = intent.getStringExtra("user");

            objectDatabaseHandler = new DatabaseHandler(this);

            imagePick();

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseImage(View imageView)
    {
        try
        {
            Intent objectIntent = new Intent();
            objectIntent.setType("image/*");
            objectIntent.setAction(Intent.ACTION_GET_CONTENT);
            startActivityForResult(objectIntent,PICK_IMAGE_REQUEST);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseAudio(View view)
    {
        try
        {
            Intent audio = new Intent();
            audio.setType("audio/*");
            audio.setAction(Intent.ACTION_OPEN_DOCUMENT);
            startActivityForResult(audio, PICK_AUDIO);
            //startActivityForResult(Intent.createChooser(audio, "Select Audio"), PICK_AUDIO);
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
//    {
//        try
//        {
//            super.onActivityResult(requestCode, resultCode, data);
//            if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
//            {
//                imageFilePath = data.getData();
//                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);
//
//                songPhoto.setImageBitmap(imageToStore);
//
//            }
//            if(requestCode == PICK_AUDIO && resultCode==RESULT_OK && data!=null && data.getData()!=null)
//            {
//                try
//                {
//                    audioFile = data.getData();
//                    InputStream inputStream = getContentResolver().openInputStream(audioFile);
//                    audioToStore = new byte[inputStream.available()];
//
//                    audioToStore = getBytesFromInputStream(inputStream);
//
//                    selectAudio.setText("Audio selected");
//
//                }
//                catch (IOException e)
//                {
//                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
//                }
//
//            }
//        }
//        catch (Exception e)
//        {
//            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
//        }
//    }

    public void storeSong(View view)
    {
        try
        {
            if(!songName.getText().toString().isEmpty() && songPhoto.getDrawable()!=null && imageToStore!=null && audioToStore!=null )
            {
                objectDatabaseHandler.insertSong(new Songs(id, songName.getText().toString(), lang.getText().toString(), genre.getText().toString(),imageToStore, audioToStore, artistName));
                    //finish();
                Intent intent = new Intent(UploadNewSong.this, MainActivity.class);
                intent.putExtra("user",artistName);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else
            {
                Toast.makeText(this, "Please enter all the details", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public static byte[] getBytesFromInputStream(InputStream is) throws IOException
    {
        ByteArrayOutputStream os = new ByteArrayOutputStream();
        byte[] buffer = new byte[0xFFFF];
        for(int len = is.read(buffer); len!=-1; len= is.read(buffer))
        {
            os.write(buffer, 0, len);
        }
        return os.toByteArray();
    }

    private void imagePick() {
        songPhoto.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int avatar = 0;
                if(avatar == 0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromGallery();
                    }
                }else if (avatar == 1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
//        CropImage.activity()
//                .start(this);
        Intent objectIntent = new Intent();
        objectIntent.setType("image/*");
        objectIntent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(objectIntent,CAMERA_REQUEST);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission,CAMERA_REQUEST);
    }

    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    //OVERRIDE METHOD ONREQUESTPERMISSIONRESULT

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch(requestCode){
            case CAMERA_REQUEST:{
                if(grantResults.length>0){
                    boolean camera_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept=grantResults[1]==PackageManager.PERMISSION_GRANTED;
                    if(camera_accept&&storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "enable camera and storage",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if(grantResults.length>0){
                    boolean storage_accept=grantResults[0]==PackageManager.PERMISSION_GRANTED;

                    if(storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "please enable storage permission",Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
//            CropImage.ActivityResult result=CropImage.getActivityResult(data);
//            if(resultCode==RESULT_OK){
//                Uri resultUri = result.getUri();
//                Picasso.with(this).load(resultUri).into(avatar);
//            }
//        }
        try
        {
            if(requestCode == CAMERA_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
            {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);

                songPhoto.setImageBitmap(imageToStore);

            }
            if(requestCode == PICK_AUDIO && resultCode==RESULT_OK && data!=null && data.getData()!=null)
            {
                try
                {
                    audioFile = data.getData();
                    InputStream inputStream = getContentResolver().openInputStream(audioFile);
                    audioToStore = new byte[inputStream.available()];

                    audioToStore = getBytesFromInputStream(inputStream);

                    selectAudio.setText("Audio selected");

                }
                catch (IOException e)
                {
                    Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
                }

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

    }

}