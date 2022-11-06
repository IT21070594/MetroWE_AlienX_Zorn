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

public class EditAlbum extends AppCompatActivity {

    EditText editAlbumName;
    ImageView editAlbumPhoto;
    int id;

    //Any number other than 0 can be assigned.
    private static final int PICK_IMAGE_REQUEST = 100;

    private Uri imageFilePath;

    private Bitmap imageToStore;
    private byte[] imageInBytes;
    DatabaseHandler objectDatabaseHandler;
    String artistName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_album);

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

        Button cancelEditBtn = findViewById(R.id.cancelEditAlbumBtn);
        cancelEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        try
        {
            editAlbumName = findViewById(R.id.changeAlbumName);
            editAlbumPhoto = findViewById(R.id.editAlbumImg);

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

            editAlbumName.setText(bundle.getString("album_name"));
            editAlbumPhoto.setImageBitmap(bitmap);

            artistName=bundle.getString("user");
            System.out.println(artistName);
        }
    }

    public void chooseAlbumEditImage(View imageView)
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

                editAlbumPhoto.setImageBitmap(imageToStore);

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void editAlbum(View view)
    {
        try
        {
            if(!editAlbumName.getText().toString().isEmpty() && editAlbumPhoto.getDrawable()!=null)
            {
                objectDatabaseHandler.editAlbum(id, editAlbumName.getText().toString(),imageInBytes);

                Intent intent1 = new Intent(getApplicationContext(),Album.class);
                intent1.putExtra("user",artistName);
                startActivity(intent1);
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