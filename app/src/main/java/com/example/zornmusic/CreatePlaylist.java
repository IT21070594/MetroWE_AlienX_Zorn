package com.example.zornmusic;

import android.content.Intent;
import android.graphics.Bitmap;
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
import androidx.recyclerview.widget.RecyclerView;


public class CreatePlaylist extends AppCompatActivity {
    private RecyclerView objectRV;

//    private RVAdapter objectRVAdapter;
        EditText etPN;
        ImageView plPhoto;
        int pid=0;

        Button btnCreatePL;

        private static  final int PICK_IMAGE_REQUEST=100;
          private Uri imageFilePath;
       private Bitmap imageToStore;
        DatabaseHandler objectDatabaseHandler;




        @Override
        protected void onCreate(Bundle savedInstanceState) {







            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_create_playlist);

            try {

                etPN=findViewById(R.id.EnterHere);
                plPhoto=findViewById(R.id.plimage);
                objectDatabaseHandler= new DatabaseHandler(this);


            }catch ( Exception e){
                Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }

            Button BtnCancel = findViewById(R.id.CancelCreatePlaylistBtn);

            BtnCancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent playlistHome = new Intent(getApplicationContext(), PlaylistHome.class);
                    startActivity(playlistHome);
                }
            });
        }



      public void chooseImage( View imageView){

          try {

              Intent objectIntend = new Intent();
              objectIntend.setType("image/*");
              objectIntend.setAction(Intent.ACTION_GET_CONTENT);
              startActivityForResult(objectIntend,PICK_IMAGE_REQUEST);


          }catch ( Exception e){
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

                plPhoto.setImageBitmap(imageToStore);


            }
        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    public void storePlaylist(View view)
    {
        try
        {
            if(!etPN.getText().toString().isEmpty() && plPhoto.getDrawable()!=null && imageToStore!=null )
            {
                objectDatabaseHandler.insertPlaylist(new ModelClass(pid, etPN.getText().toString(), imageToStore));

                Intent intent = new Intent(CreatePlaylist.this, PlaylistHome.class);
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