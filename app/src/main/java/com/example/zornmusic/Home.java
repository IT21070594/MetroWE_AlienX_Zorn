package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;

import Database.DatabaseHandler;
import Database.Songs;
public class Home extends AppCompatActivity {
    String name;
    ImageButton avatar;
    Intent receiveIntent;
    private DatabaseHandler objectDBHandler;
    private RecyclerView objectRV;
    private TextView noSongs;
    private HomeRecyclerViewAdapter objectRVAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
         receiveIntent=getIntent();
         name=receiveIntent.getStringExtra("user");
      //  System.out.println(name);
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
                        intent1.putExtra("user",name);
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

        try
        {
            noSongs = findViewById(R.id.noSongsHomeText);
            objectRV= findViewById(R.id.homeRV);
            objectDBHandler = new DatabaseHandler(this);

        }
        catch (Exception e)
        {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try
        {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 700*1024*1024); //700MB is the new size

            if(objectDBHandler.getAllSongsData().size()==0)
            {
                noSongs.setVisibility(View.VISIBLE);
                objectRV.setVisibility(View.GONE);
            }
            else
            {
                objectRVAdapter = new HomeRecyclerViewAdapter(this,objectDBHandler.getAllSongsData(),name);

                objectRV.setHasFixedSize(true);
                objectRV.setLayoutManager(new LinearLayoutManager(this));
                objectRV.setAdapter(objectRVAdapter);
            }
        }
        catch (Exception e)
        {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        SearchView searchView = findViewById(R.id.searchHomeBar);
        if(objectRV!=null && objectRVAdapter != null) {
            searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {

                    objectRVAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
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
        res.close();
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
    }
    private void filter(String text)
    {
        ArrayList<Songs> filteredList = new ArrayList<>();
        ArrayList<Songs> NotFilteredList = objectDBHandler.getAllSongsData();

        for(Songs item : NotFilteredList)
        {
            if(item.getSongName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        objectRVAdapter.filterList(filteredList);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(objectRV!=null && objectRVAdapter!=null)
        {
            objectRVAdapter = new HomeRecyclerViewAdapter(this,objectDBHandler.getAllSongsData(),name);

            objectRV.setHasFixedSize(true);
            objectRV.setLayoutManager(new LinearLayoutManager(this));
            objectRV.setAdapter(objectRVAdapter);
        }
        else
        {
            noSongs.setVisibility(View.VISIBLE);
            objectRV.setVisibility(View.GONE);
        }
    }
    public void gotonextview(){
       Intent receiveIntent2=getIntent();
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
        res.close();
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
            intent1.putExtra("user",receiveIntent2.getStringExtra("user"));
            startActivity(intent1);}
        else if(c2.compareTo(accType)==0) {Toast.makeText(this, "Hi Hi", Toast.LENGTH_SHORT).show();

            Intent intent2 = new Intent(getApplicationContext(),viewArtistAcc.class);
            intent2.putExtra("user",receiveIntent2.getStringExtra("user"));
            startActivity(intent2);
        }
        else if(c3.compareTo(accType)==0) {Toast.makeText(this, "Hellooo", Toast.LENGTH_SHORT).show();
            Intent intent3 = new Intent(getApplicationContext(),UserViewAccount.class);
            intent3.putExtra("user",receiveIntent2.getStringExtra("user"));
            startActivity(intent3);
        }
    }

    public void uploadButtonGo(){
//        System.out.println(receiveIntent3.getStringExtra("user"));
        DatabaseHandler dbHandler=  new DatabaseHandler(this);
        Cursor res=dbHandler.getInfo(name);

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