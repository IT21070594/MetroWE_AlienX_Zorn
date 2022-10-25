package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class viewPlans extends AppCompatActivity {
Intent viewPlans ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        viewPlans = getIntent();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plans);
    }
}