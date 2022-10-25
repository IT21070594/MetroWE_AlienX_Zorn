package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class HomePage extends AppCompatActivity {
    Intent intentHome;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intentHome = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        Button BtnLibrary = findViewById(R.id.myLibraryBtn);
Button BtnUpgrade = findViewById(R.id.upgradeBtn);
        BtnLibrary.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent library = new Intent(getApplicationContext(), myLibrary.class);
                startActivity(library);
            }
        });

        BtnUpgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upgrade = new Intent(getApplicationContext(),viewPlans.class);
                startActivity(upgrade);
            }
        });
    }
}