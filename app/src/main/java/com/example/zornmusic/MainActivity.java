package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Locale;

import Database.DatabaseHandler;
import Database.Songs;

public class MainActivity extends AppCompatActivity {

    private DatabaseHandler objectDBHandler;
    private RecyclerView objectRV;
    private TextView noSongs;
    private RecyclerViewAdapter objectRVAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set Uploads Selected
        bottomNavigationView.setSelectedItemId(R.id.uploads);

        //Perform ItemSelectedListener
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.home:
                        startActivity(new Intent(getApplicationContext(), Home.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.myLibrary:
                        startActivity(new Intent(getApplicationContext(), Library.class));
                        overridePendingTransition(0,0);
                        return true;
                    case R.id.uploads:
                        return true;
                }
                return false;
            }
        });

        Button albumsBtn = findViewById(R.id.albumsBtn);
        albumsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, Album.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        Button uploadNewSongBtn = findViewById(R.id.uploadNewBtn);
        uploadNewSongBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadNewSong.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        ImageButton uploadImgBtn = findViewById(R.id.uploadImgBtn);
        uploadImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, UploadNewSong.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        try
        {
            noSongs = findViewById(R.id.noSongsText);
            objectRV= findViewById(R.id.songsRV);
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
            field.set(null, 700*1024*1024); //500MB is the new size

            if(objectDBHandler.getAllSongsData().size()==0)
            {
                noSongs.setVisibility(View.VISIBLE);
                objectRV.setVisibility(View.GONE);
            }
            else
            {
                objectRVAdapter = new RecyclerViewAdapter(this,objectDBHandler.getAllSongsData());

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

//        EditText search = findViewById(R.id.searchBar);
//        search.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//                filter(editable.toString());
//            }
//        });

        SearchView searchView = findViewById(R.id.searchBar);
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
            objectRVAdapter = new RecyclerViewAdapter(this,objectDBHandler.getAllSongsData());

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

}