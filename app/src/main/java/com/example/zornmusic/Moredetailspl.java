package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;


public class Moredetailspl extends AppCompatActivity {
    Intent intentMoredetailspl;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intentMoredetailspl = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moredetailspl);
        Button BtnAddSongs = findViewById(R.id.AddSongsBtn);
       ImageButton ButBk = findViewById(R.id.bkBtn);

        ButBk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPlaylist = new Intent(getApplicationContext(), PlaylistHome.class);
                startActivity(createPlaylist);
            }
        });
    }
}