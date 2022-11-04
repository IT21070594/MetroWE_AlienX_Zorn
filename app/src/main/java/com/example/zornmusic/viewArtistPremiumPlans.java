package com.example.zornmusic;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import Database.DatabaseHandler;
import Database.UserMasters;

public class viewArtistPremiumPlans extends AppCompatActivity {
    Intent receiveIntent2;
    ImageButton back;
    DatabaseHandler dbHandler;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    ArtistPlanAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_artist_premium_plans);
        receiveIntent2 = getIntent();
        String name=receiveIntent2.getStringExtra("user");
        System.out.println(name);
        back=findViewById(R.id.BackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbHandler=new DatabaseHandler(getApplicationContext());
        findID();
        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
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



    private void displayData() {
        sqLiteDatabase=dbHandler.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+UserMasters.ArtistPlans.TABLE_NAME2+"",null);
        ArrayList<ArtistPlanModel>models=new ArrayList<>();
        while(cursor.moveToNext()){
            String planName = cursor.getString(0);
            byte[]imagePlan = cursor.getBlob(1);
            int amount = cursor.getInt(2);
            int downloadLimit = cursor.getInt(3);
            int uploadLimit = cursor.getInt(4);
            String name=receiveIntent2.getStringExtra("user");
            models.add(new ArtistPlanModel(planName,imagePlan,amount,downloadLimit,uploadLimit,name));
        }
        cursor.close();
        myAdapter= new ArtistPlanAdapter(this,models,sqLiteDatabase,R.layout.artistplansingledata);
        recyclerView.setAdapter(myAdapter);
    }

    private void findID() {
        recyclerView=findViewById(R.id.recyclerview);

    }  public void uploadButtonGo(){
        receiveIntent2=getIntent();
        String name=receiveIntent2.getStringExtra("user");
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