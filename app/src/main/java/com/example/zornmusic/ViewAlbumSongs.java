package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Database.DatabaseHandler;

public class ViewAlbumSongs extends AppCompatActivity {

    TextView albumName;
    ImageView albumPhoto;
    String name;
    int id;

    private byte[] imageInBytes;
    DatabaseHandler objectDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_album_songs);

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

        try
        {
            albumName = findViewById(R.id.albumName);
            albumPhoto = findViewById(R.id.albumImage);

            objectDatabaseHandler = new DatabaseHandler(this);

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        };

        if(getIntent().getBundleExtra("albumData") != null)
        {
            Bundle bundle = getIntent().getBundleExtra("albumData");
            id = bundle.getInt("album_id");

            //For image , to convert byteArray to Bitmap and set it to imageView.
            imageInBytes = bundle.getByteArray("album_image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes,0, imageInBytes.length);

            //To set the name and image.
            name = bundle.getString("album_name");
            albumName.setText(name);
            albumPhoto.setImageBitmap(bitmap);
        }

        ImageButton backBtn = findViewById(R.id.backAlBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAlbumSongs.this, Album.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        ImageButton editBtn = findViewById(R.id.editAlButton);
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putInt("album_id", id);
                bundle.putString("album_name", name);
                bundle.putByteArray("album_image", imageInBytes);

                Intent intent = new Intent(ViewAlbumSongs.this, EditAlbum.class);
                intent.putExtra("albumData", bundle);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        ImageButton deleteBtn = findViewById(R.id.deleteAlButton);
        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                objectDatabaseHandler.deleteAlbum(id);

                Intent intent = new Intent(ViewAlbumSongs.this, Album.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        Button uploadNewSongBtn = findViewById(R.id.uploadNewBtnAl);
        uploadNewSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAlbumSongs.this, UploadNewSong.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        ImageButton uploadImgBtn = findViewById(R.id.uploadImgBtnAl);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ViewAlbumSongs.this, UploadNewSong.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });
    }
}