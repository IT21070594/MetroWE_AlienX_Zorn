package com.example.zornmusic;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHandler;
import Database.UserMasters;

public class viewUserPlan extends AppCompatActivity {

    Intent receiveIntent1;
    ImageButton back;
    ImageView planImage;
    Button renew,cancel,Upgrade;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_plan);
        receiveIntent1=getIntent();
        //receiveIntent.getStringExtra("user");
        //String name= receiveIntent1.getStringExtra("user");
        //System.out.println(name);
        back=findViewById(R.id.imageButton3);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        receiveIntent1=getIntent();
        String name=receiveIntent1.getStringExtra("user");
        System.out.println(name);
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
       // String c3 = "Free User";

        //TextView t2 = findViewById(R.id.textView4);
        System.out.println(accType);

        if(accType.compareTo(c1)==0){
            DatabaseHandler db = new DatabaseHandler(this);
            Cursor result=db.getPremiumPlanInfo(name);
            if(result.getCount()==0){
                Toast.makeText(this, "No such user exists", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer sb =new StringBuffer();
            while(result.moveToNext()){
                sb.append(result.getString(2));
            }
            String planType = String.valueOf(sb);
            System.out.println(planType);

            DatabaseHandler dbs= new DatabaseHandler(this);
            Cursor result1=dbs.getPremiumPlanDetails(planType);
            if(result1.getCount()==0){
                Toast.makeText(this, "No such plan exists", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer sb1 = new StringBuffer();
            while(result1.moveToNext()){
                sb1.append("Plan Type: "+result1.getString(0)+"\n\n");
                sb1.append("Price: "+result1.getInt(2)+"\n\n");
                sb1.append("Download Limit: "+result1.getInt(3)+"\n");
//                buffer.append("Name :"+res.getString(1)+"\n");
//                buffer.append("Password :"+res.getString(2)+"\n\n");

            }
            TextView plandetails = findViewById(R.id.planDeets);
            plandetails.setText(sb1);
        }
            if(accType.compareTo(c2)==0){
            DatabaseHandler db = new DatabaseHandler(this);
            Cursor result=db.getArtistPlanInfo(name);
            if(result.getCount()==0){
                Toast.makeText(this, "No such entry exists", Toast.LENGTH_SHORT).show();
                return;
            }
            StringBuffer sb =new StringBuffer();
            while(result.moveToNext()){
                sb.append(result.getString(2));
            }
            String planType = String.valueOf(sb);
            System.out.println(planType);
        }



        cancel=findViewById(R.id.dnclbtn);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(getApplicationContext());
                boolean checkDelete = db.deletePremiumUserInfo(name);
                if(checkDelete==true) {
                    Toast.makeText(getApplicationContext(), "entry deleted", Toast.LENGTH_SHORT).show();
                }else {
                    Toast.makeText(getApplicationContext(), "Entry not deleted", Toast.LENGTH_SHORT).show();
                }
                DatabaseHandler dbn= new DatabaseHandler(getApplicationContext());
                String c3 = "Free User";
                boolean checkUpdate=dbn.updateUserAccType(name,c3);
                if(checkUpdate==true) {
                    Toast.makeText(getApplicationContext(), "entry updated", Toast.LENGTH_SHORT).show();
                }else
                    Toast.makeText(getApplicationContext(), "Entry not updated", Toast.LENGTH_SHORT).show();

                Intent i1=new Intent(getApplicationContext(),login.class);
                startActivity(i1);

            }

        });



   }
}