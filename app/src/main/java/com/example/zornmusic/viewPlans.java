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

public class viewPlans extends AppCompatActivity {
    Intent receiveIntent1;
    ImageButton back;
    DatabaseHandler dbHandler;
    SQLiteDatabase sqLiteDatabase;
    RecyclerView recyclerView;
    MyAdapter myAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_plans);
        receiveIntent1=getIntent();
        String name= receiveIntent1.getStringExtra("user");
        System.out.println(name);
        back=findViewById(R.id.BackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        dbHandler = new DatabaseHandler(getApplicationContext());
        findId();
        displayData();
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));



    }

    private void displayData() {
        sqLiteDatabase=dbHandler.getReadableDatabase();
        Cursor cursor=sqLiteDatabase.rawQuery("select * from "+UserMasters.UserPlans.TABLE_NAME1+"",null);
        ArrayList<Model>models=new ArrayList<>();
        while(cursor.moveToNext()){
            String planName = cursor.getString(0);
            byte[]imagePlan = cursor.getBlob(1);
            int amount = cursor.getInt(2);
            int downloadLimit = cursor.getInt(3);
            String name=receiveIntent1.getStringExtra("user");
            models.add(new Model(planName,imagePlan,amount,downloadLimit,name));
        }
        cursor.close();
        myAdapter= new MyAdapter(this,models,sqLiteDatabase,R.layout.singledata);
        recyclerView.setAdapter(myAdapter);
    }

    private void findId() {
        recyclerView = findViewById(R.id.rv) ;
    }

}