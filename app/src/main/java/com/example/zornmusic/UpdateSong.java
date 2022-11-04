package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.io.ByteArrayOutputStream;

import Database.DatabaseHandler;
import Database.Songs;

public class UpdateSong extends AppCompatActivity
{
    EditText editsongName;
    EditText editlang;
    EditText editgenre;
    ImageView editsongPhoto;
    int id;

    //Any number other than 0 can be assigned.
    private static final int PICK_IMAGE_REQUEST = 100;

    private Uri imageFilePath;

    private Bitmap imageToStore;
    private byte[] imageInBytes;
    DatabaseHandler objectDatabaseHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_song);

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

        Button cancelBtn = findViewById(R.id.editCancelBtn);
        cancelBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(UpdateSong.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        try
        {
            editsongName = findViewById(R.id.editSongNameInput);
            editlang = findViewById(R.id.editLangInput);
            editgenre = findViewById(R.id.editGenreInput);
            editsongPhoto = findViewById(R.id.editPhotoInput);

            objectDatabaseHandler = new DatabaseHandler(this);

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        };

        if(getIntent().getBundleExtra("songData") != null)
        {
            Bundle bundle = getIntent().getBundleExtra("songData");
            id = bundle.getInt("song_id");

            //For image , to convert byteArray to Bitmap and set it to imageView.
            imageInBytes = bundle.getByteArray("song_image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes,0, imageInBytes.length);
            editsongPhoto.setImageBitmap(bitmap);

            //To set the name, language and genre.
            editsongName.setText(bundle.getString("song_name"));
            editlang.setText(bundle.getString("song_lang"));
            editgenre.setText(bundle.getString("song_genre"));
        }
    }

    public void chooseEditImage(View imageView)
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

                //To convert Bitmap to byte array and store in DB.
                 ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                imageInBytes = objectByteArrayOutputStream.toByteArray();

                editsongPhoto.setImageBitmap(imageToStore);

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void editSong(View view)
    {
        try
        {
            if(!editsongName.getText().toString().isEmpty() && editsongPhoto.getDrawable()!=null)
            {
                objectDatabaseHandler.editSong(id, editsongName.getText().toString(), editlang.getText().toString(), editgenre.getText().toString(),imageInBytes);

                Intent intent = new Intent(UpdateSong.this, MainActivity.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
            else
            {
                Toast.makeText(this, "Select the image", Toast.LENGTH_SHORT).show();
            }

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

}