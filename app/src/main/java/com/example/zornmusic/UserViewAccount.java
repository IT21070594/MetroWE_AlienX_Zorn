package com.example.zornmusic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Database.DatabaseHandler;
import Database.UserMasters;

public class UserViewAccount extends AppCompatActivity {
    Intent receiveIntent;
    TextView useracctype;
    Button deleteAcc,logout,getPlan;
    ImageButton edit,back;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view_account);
        receiveIntent = getIntent();
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        useracctype = findViewById(R.id.freeuserAccLabel);
        String name=receiveIntent.getStringExtra("user");
        Cursor res=dbHandler.getInfo(name);
//
//        useracctype.setText(String.valueOf(receiveIntent.getStringExtra("user")));
//        Cursor res=dbHandler.getOneInfo(etUn.getText().toString());
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
//
            useracctype.setText(buffer);

        back = findViewById(R.id.backbutton);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit = findViewById(R.id.editAccountBtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(),editUserProfile.class);
                intent4.putExtra("user",receiveIntent.getStringExtra("user"));
                startActivity(intent4);
            }
        });
        deleteAcc=findViewById(R.id.deleteBtn);
        deleteAcc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                        boolean checkdelete=dbHandler.deleteInfo(name);
                        if(checkdelete==true) {
                            Toast.makeText(getApplicationContext(), "entry deleted", Toast.LENGTH_SHORT).show();
                            Intent intent1 = new Intent(getApplicationContext(),SignUp.class);
                            intent1.putExtra("user",receiveIntent.getStringExtra("user"));
                            startActivity(intent1);
                        }else
                            Toast.makeText(getApplicationContext(), "Entry not deleted", Toast.LENGTH_SHORT).show();

            }
        });
        logout = findViewById(R.id.logoutBtn);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(),login.class);
                startActivity(intent2);
            }
        });

        getPlan = findViewById(R.id.upgradeusernormal);
        getPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3= new Intent(getApplicationContext(),viewPlans.class);
                intent3.putExtra("user",receiveIntent.getStringExtra("user"));
                startActivity(intent3);
            }
        });

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