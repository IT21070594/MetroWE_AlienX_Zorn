package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class viewPlans extends AppCompatActivity {
Intent viewPlans ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewPlans = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plans);
        Button home = findViewById(R.id.homeBtn);
        Button BtnLibrary = findViewById(R.id.myLibraryBtn);
        Button BtnUpload = findViewById(R.id.uploadBtn);
        BtnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent upload = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(upload);
            }
        });

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
    }
}