package com.example.zornmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
Intent uploadIntent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        uploadIntent = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button BtnLibrary = findViewById(R.id.myLibraryBtn);
        Button BtnUpgrade = findViewById(R.id.upgradeBtn);
        Button home = findViewById(R.id.homeBtn);

        BtnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent library = new Intent(getApplicationContext(), myLibrary.class);
                startActivity(library);
            }
        });
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

    }
}