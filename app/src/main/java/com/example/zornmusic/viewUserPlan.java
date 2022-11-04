package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class viewUserPlan extends AppCompatActivity {
Intent intentUpgrade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        intentUpgrade = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_plan);
    }
}