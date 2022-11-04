package com.example.zornmusic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHandler;
import Database.UserMasters;

public class viewArtistAcc extends AppCompatActivity {
    Intent receiveIntent;
    ImageButton edit,back;
    TextView accType;
    Button delete,viewUploads,viewPlan,logOut;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artist_acc);
        receiveIntent=getIntent();

        edit=findViewById(R.id.editBtn);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),editUserProfile.class);
                intent1.putExtra("user",receiveIntent.getStringExtra("user"));
                startActivity(intent1);
            }
        });

        back = findViewById(R.id.navBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        accType=findViewById(R.id.artistLabel);
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        String name=receiveIntent.getStringExtra("user");
        Cursor res=dbHandler.getInfo(name);
        if(res.getCount()==0){
            Toast.makeText(getApplicationContext(), "No such entry exists", Toast.LENGTH_SHORT).show();
            return ;
        }
        StringBuffer buffer = new StringBuffer();
        while(res.moveToNext()){
            buffer.append(res.getString(3));

        }
        accType.setText(buffer);
        System.out.println(buffer);

        delete=findViewById(R.id.deletebutton);
        delete.setOnClickListener(new View.OnClickListener() {
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

        viewUploads = findViewById(R.id.viewUploads);
        viewUploads.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
                intent2.putExtra("user",receiveIntent.getStringExtra("user"));
                startActivity(intent2);
            }
        });

        viewPlan = findViewById(R.id.viewPlan);
        viewPlan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(),viewArtistPlan.class);
                intent3.putExtra("user",receiveIntent.getStringExtra("user"));
                startActivity(intent3);
            }
        });

        logOut=findViewById(R.id.logOutArtist);
        logOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent4 = new Intent(getApplicationContext(),login.class);
                startActivity(intent4);
            }
        });




    }
}