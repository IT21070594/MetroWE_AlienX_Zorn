package com.example.zornmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class UploadSong extends AppCompatActivity {
    Intent receive;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        receive = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_song);
        Button home = findViewById(R.id.HomeBtn);
        Button BtnUpgrade = findViewById(R.id.upgradeBtn);
        Button BtnLibrary = findViewById(R.id.MyLibraryBtn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intentHome);
            }
        });
        BtnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpgrade = new Intent(getApplicationContext(), viewPlans.class);
                startActivity(intentUpgrade);
            }
        });
        BtnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent library = new Intent(getApplicationContext(), myLibrary.class);
                startActivity(library);
            }
        });
    }

}