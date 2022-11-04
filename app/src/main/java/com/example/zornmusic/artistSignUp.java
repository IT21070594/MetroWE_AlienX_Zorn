package com.example.zornmusic;

import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class artistSignUp extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_sign_up);

        Spinner mySpinner = (Spinner) findViewById(R.id.spinner2);
        Spinner mySpinner2 = (Spinner) findViewById(R.id.spinner3);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<>(artistSignUp.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.accTypes));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(listener);

        ArrayAdapter<String> myAdapter2 = new ArrayAdapter<String>(artistSignUp.this, android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.planTypes));
        myAdapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner2.setAdapter(myAdapter2);
        mySpinner2.setOnItemSelectedListener(listener);
    }
    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }};
}