package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.IOException;
import java.io.InputStream;

import Database.Albums;
import Database.DatabaseHandler;
import Database.Songs;

public class CreateNewAlbum extends AppCompatActivity {

    EditText albumName;
    int id = 0;

    ImageView albumPhoto;
    //Any number other than 0 can be assigned.
    private static final int PICK_IMAGE_REQUEST = 100;
    private Uri imageFilePath;
    private Bitmap imageToStore;

    DatabaseHandler objectDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_new_album);

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
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myLibrary:
                        startActivity(new Intent(getApplicationContext(), Library.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:
                        return true;
                }
                return false;
            }
        });

        Button cancelAlBtn = findViewById(R.id.cancelAlbumBtn);
        cancelAlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(CreateNewAlbum.this, Album.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        try
        {
            albumName = findViewById(R.id.enterAlbumName);;
            albumPhoto = findViewById(R.id.albumPhotoInput);

            objectDatabaseHandler = new DatabaseHandler(this);

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void chooseAlbumImage(View imageView)
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        try
        {
            super.onActivityResult(requestCode, resultCode, data);
            if(requestCode == PICK_IMAGE_REQUEST && resultCode==RESULT_OK && data!=null && data.getData()!=null)
            {
                imageFilePath = data.getData();
                imageToStore = MediaStore.Images.Media.getBitmap(getContentResolver(),imageFilePath);

                albumPhoto.setImageBitmap(imageToStore);

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void storeAlbum(View view)
    {
        try
        {
            if(!albumName.getText().toString().isEmpty() && albumPhoto.getDrawable()!=null && imageToStore!=null)
            {
                objectDatabaseHandler.insertAlbum(new Albums(id, albumName.getText().toString(),imageToStore));

                Intent intent = new Intent(CreateNewAlbum.this, Album.class);
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

}