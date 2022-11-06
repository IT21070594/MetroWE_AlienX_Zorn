package com.example.zornmusic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import Database.DatabaseHandler;
import Database.DatabaseHandler;
import Database.UserMasters;

public class editUserProfile extends AppCompatActivity {
    Intent receiveIntent;
    TextView name;
    EditText email,pw;
    Button btn1;
    ImageButton back;
    String username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_profile);
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
                        //startActivity(new Intent(getApplicationContext(), Library.class));
                        Intent intent1 = new Intent(getApplicationContext(),Library.class);
                        intent1.putExtra("user",receiveIntent.getStringExtra("user"));
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:

                        uploadButtonGo();
                        return true;
                }
                return false;
            }
        });
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
    public void uploadButtonGo(){
        receiveIntent=getIntent();
         username=receiveIntent.getStringExtra("user");
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(username);
//        if(res.getCount()==0){
//            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
//            return ;
//        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        res.close();
        String accType = String.valueOf(buffer);
        //TextView t1 = findViewById(R.id.textView3);
        //t1.setText(accType);
        String c1 = "Premium User";
        String c2 = "Artist";
        String c3 = "Free User";

        if(c3.compareTo(accType)==0){
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else if(c2.compareTo(accType)==0){
            Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else{
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",username);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }
    }
}