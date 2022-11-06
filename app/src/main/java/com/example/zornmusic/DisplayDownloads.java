package com.example.zornmusic;


import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import Database.DatabaseHandler;

public class DisplayDownloads extends AppCompatActivity {
    DatabaseHandler dBmain;
    SQLiteDatabase sqLiteDatabase;
    Intent receiveIntent1;
    RecyclerView recyclerView;
    MyAdapterDownloads myAdapterDownloads;
    ArrayList<DownloadModel> downloadModels;
int id = 0;
    Intent intent1;
    String name;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_downloads);
         intent1=getIntent();
       // intent1.getStringExtra("user");
         name=intent1.getStringExtra("user");

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set library Selected
        bottomNavigationView.setSelectedItemId(R.id.myLibrary);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        Intent intent1 = new Intent(getApplicationContext(),Home.class);
                        intent1.putExtra("user",name);
                        startActivity(intent1);
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myLibrary:
                        return true;
                    case R.id.uploads:

                        uploadButtonGo();
                        return true;
                }
                return false;
            }
        });
        dBmain = new DatabaseHandler(this);
        findId();
        displayDownloads(intent1.getStringExtra("user"));
        recyclerView.setLayoutManager(new LinearLayoutManager(this,RecyclerView.VERTICAL,false));
        SearchView searchDownload = findViewById(R.id.searchdownloads);
       searchDownload.clearFocus();
       searchDownload.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
           @Override
           public boolean onQueryTextSubmit(String s) {
               return false;
           }

           @Override
           public boolean onQueryTextChange(String s) {
               filterList(s);
               return true;
           }
       });





    }

    private void filterList(String text) {
        ArrayList<DownloadModel> filteredList =  new ArrayList<>();
        for(DownloadModel item : downloadModels){
            if(item.getName().toLowerCase().contains(text.toLowerCase())) {
                filteredList.add(item);
            }
        }
        if(filteredList.isEmpty()){
            Toast.makeText(this,"no song found",Toast.LENGTH_SHORT).show();
        }else{
            myAdapterDownloads.setFilteredList(filteredList);
        }
    }

    private void displayDownloads(String Username) {
        sqLiteDatabase = dBmain.getReadableDatabase();

        String selection = "SELECT * FROM downloads WHERE username =" +Username;
        Cursor cursor = sqLiteDatabase.rawQuery(selection,null);
        downloadModels=new ArrayList<>();
        while(cursor.moveToNext()){
            int id = cursor.getInt(0);
            byte[]avatar = cursor.getBlob(1);
            String name = cursor.getString(2);
            downloadModels.add(new DownloadModel(id,avatar,name));


        }
        cursor.close();
        myAdapterDownloads = new MyAdapterDownloads(this, R.layout.singledownload_data,downloadModels,sqLiteDatabase);
        recyclerView.setAdapter(myAdapterDownloads);
    }

    private void findId() {
        recyclerView = findViewById(R.id.rvd);

    }
    public void uploadButtonGo(){
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
            intent2.putExtra("user",name);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else if(c2.compareTo(accType)==0){
            Intent intent2 = new Intent(getApplicationContext(),MainActivity.class);
            intent2.putExtra("user",name);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }else{
            Toast.makeText(this, "Please login with Artist Account Credentials!", Toast.LENGTH_SHORT).show();
            Intent intent2 = new Intent(getApplicationContext(),login.class);
            intent2.putExtra("user",name);
            startActivity(intent2);
            overridePendingTransition(0,0);
        }
    }
}