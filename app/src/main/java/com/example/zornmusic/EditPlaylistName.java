package com.example.zornmusic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import java.io.ByteArrayOutputStream;

public class EditPlaylistName extends AppCompatActivity {

    EditText editPlaylistName;
    ImageView editPlaylistPhoto;
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
        setContentView(R.layout.activity_edit_playlist_name);

        Button cancelEditBtn = findViewById(R.id.CancelEditPlaylistBtn);
        cancelEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(EditPlaylistName.this, PlaylistHome.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        try
        {
            editPlaylistName = findViewById(R.id.enternewname);
            editPlaylistPhoto = findViewById(R.id.plEditimage);
            objectDatabaseHandler = new DatabaseHandler(this);

        }catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        };

        if(getIntent().getBundleExtra("playlistData") != null)
        {
            Bundle bundle = getIntent().getBundleExtra("playlistData");
            id = bundle.getInt("playlist_id");

            //For image , to convert byteArray to Bitmap and set it to imageView.
            imageInBytes = bundle.getByteArray("playlist_image");
            Bitmap bitmap = BitmapFactory.decodeByteArray(imageInBytes,0, imageInBytes.length);

            //To set the name and image.
            editPlaylistName.setText(bundle.getString("playlist_name"));
            editPlaylistPhoto.setImageBitmap(bitmap);
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

                editPlaylistPhoto.setImageBitmap(imageToStore);

            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void editPlaylist(View view)
    {
        try
        {
            if(!editPlaylistName.getText().toString().isEmpty() && editPlaylistPhoto.getDrawable()!=null)
            {
                objectDatabaseHandler.editPlaylist(id, editPlaylistName.getText().toString(),imageInBytes);

                Intent intent = new Intent(EditPlaylistName.this,PlaylistHome.class);
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

