package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class addSongs extends AppCompatActivity {
    Intent intentAddSong;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intentAddSong = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_songs);
        ImageButton BtnDone = findViewById(R.id.donebtn);


        BtnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent playlistHome = new Intent(getApplicationContext(), PlaylistHome.class);
                startActivity(playlistHome);
            }
        });


    }
}