package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHandler;
import Database.DatabaseHandler;
import Database.UserMasters;

public class editUserProfile extends AppCompatActivity {
    Intent receiveIntent;
    TextView name;
    EditText email,pw;
    Button btn1;
    ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
        receiveIntent = getIntent();
        name = findViewById(R.id.namE);
        email= findViewById(R.id.inputEmail);
        pw= findViewById(R.id.inputPassword);
        btn1= findViewById(R.id.updateBTN);
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        name.setText(String.valueOf(receiveIntent.getStringExtra("user")));
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean checkupdate;

                checkupdate = dbHandler.updateInfo(String.valueOf(receiveIntent.getStringExtra("user")),email.getText().toString(),pw.getText().toString());
                if(checkupdate==true) {
                    Toast.makeText(getApplicationContext(), "entry updated", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(), "Entry not updated", Toast.LENGTH_SHORT).show();
            }

        });

        back=findViewById(R.id.backbtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}