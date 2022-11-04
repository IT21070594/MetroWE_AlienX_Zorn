package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Database.DatabaseHandler;

public class Home extends AppCompatActivity {
    Intent receiveIntent;
    ImageButton avatar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        receiveIntent=getIntent();
        String name=receiveIntent.getStringExtra("user");
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(name);
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3)+"\n");

        }
//
        avatar=(ImageButton) findViewById(R.id.imageButton);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";
        avatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                gotonextview();

            }
        });
        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Home Selected
        bottomNavigationView.setSelectedItemId(R.id.home);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        return true;
                    case R.id.myLibrary:
                        startActivity(new Intent(getApplicationContext(), Library.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:

                        uploadButtonGo();
                        return true;
                }
                return false;
            }
        });
    }
    public void gotonextview(){
        receiveIntent=getIntent();
        String name=receiveIntent.getStringExtra("user");
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(name);
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        String accType = String.valueOf(buffer);
        //TextView t1 = findViewById(R.id.textView3);
        //t1.setText(accType);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";

       // TextView t2 = findViewById(R.id.textView4);
        System.out.println(accType);
        if (c1.compareTo(accType)==0){ Toast.makeText(this, "Hi Premium", Toast.LENGTH_SHORT).show();
            Intent intent1 = new Intent(getApplicationContext(),viewPremiumUserAcc.class);
            intent1.putExtra("user",receiveIntent.getStringExtra("user"));
            startActivity(intent1);}
        else if(c2.compareTo(accType)==0) {Toast.makeText(this, "Hi Hi", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(getApplicationContext(),viewArtistAcc.class);
            intent2.putExtra("user",receiveIntent.getStringExtra("user"));
            startActivity(intent2);
        }
        else if(c3.compareTo(accType)==0) {Toast.makeText(this, "Hellooo", Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(getApplicationContext(),UserViewAccount.class);
            intent3.putExtra("user",receiveIntent.getStringExtra("user"));
            startActivity(intent3);
        }
    }

    public void uploadButtonGo(){
        receiveIntent=getIntent();
        String name=receiveIntent.getStringExtra("user");
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(name);
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        String accType = String.valueOf(buffer);
        //TextView t1 = findViewById(R.id.textView3);
        //t1.setText(accType);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";

        if(c3.compareTo(accType)==0){
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), login.class));
            overridePendingTransition(0,0);
        }else if(c2.compareTo(accType)==0){
            startActivity(new Intent(getApplicationContext(), MainActivity.class));
            overridePendingTransition(0,0);
        }else{
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(getApplicationContext(), login.class));
            overridePendingTransition(0,0);
        }
    }
}