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

public class viewPremiumUserAcc extends AppCompatActivity {
    ImageButton back,edit;
    Intent receiveIntent;
    TextView accType;
    Button delete,viewPlan,logout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_premium_user_acc);
        receiveIntent=getIntent();
        //String name= receiveIntent.getStringExtra("user");
        //System.out.println(name);
        back=findViewById(R.id.backBUTTON);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        edit=findViewById(R.id.editPremiumUserAcc);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(getApplicationContext(),editUserProfile.class);
                intent1.putExtra("user",receiveIntent.getStringExtra("user"));
                startActivity(intent1);
            }
        });
        accType = findViewById(R.id.PremiumUserLabel);
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        String name=receiveIntent.getStringExtra("user");
        System.out.println(name);
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
        delete=findViewById(R.id.deletePremium);
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
         viewPlan = findViewById(R.id.viewplanprem);
         viewPlan.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View view) {
                 Intent intent2 = new Intent(getApplicationContext(),viewUserPlan.class);
                 intent2.putExtra("user",name);
                 startActivity(intent2);
             }
         });
        logout = findViewById(R.id.logoutprem);
        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent3 = new Intent(getApplicationContext(),login.class);
                startActivity(intent3);
                Toast.makeText(viewPremiumUserAcc.this, "Logged out successfully..", Toast.LENGTH_SHORT).show();
            }
        });


    }
}