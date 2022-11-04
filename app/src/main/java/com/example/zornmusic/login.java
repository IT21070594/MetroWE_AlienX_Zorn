package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHandler;
import Database.UserMasters;

public class login extends AppCompatActivity {
    Intent receiveIntent;
    EditText username,password;
    Button signin,signup;
    DatabaseHandler DB;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);
        receiveIntent=getIntent();

        username= findViewById(R.id.usernameInput);
        password=findViewById(R.id.pWord);
        signin = findViewById(R.id.btn1);
        DB = new DatabaseHandler(this);

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass= password.getText().toString();

                if(user.equals("") || pass.equals(""))
                    Toast.makeText(login.this, "Please enter all the fields..", Toast.LENGTH_SHORT).show();
                else{
                    Boolean checkuserpass = DB.checkUserNamePassword(user,pass);
                    if(checkuserpass==true){
                        Toast.makeText(login.this, "Sign In Successfull", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(),Home.class);
                        intent1.putExtra("user",user);
                        startActivity(intent1);
                    }else{
                        Toast.makeText(login.this, "invalid credentials..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        signup=findViewById(R.id.createAccount);
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),SignUp.class));
            }
        });
    }
}