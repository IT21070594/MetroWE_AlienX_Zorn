package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class myLibrary extends AppCompatActivity {
    Intent intentLibrary;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intentLibrary = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_library);
        Button home = findViewById(R.id.homeBtn);
Button upgrade = findViewById(R.id.upgradeBtn);
        home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentHome = new Intent(getApplicationContext(), HomePage.class);
                startActivity(intentHome);
            }
        });

        upgrade.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intentUpgrade = new Intent(getApplicationContext(), viewPlans.class);
                startActivity(intentUpgrade);
            }
        });
    }
}