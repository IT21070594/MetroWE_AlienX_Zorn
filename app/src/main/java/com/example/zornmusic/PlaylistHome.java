package com.example.zornmusic;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class PlaylistHome extends AppCompatActivity {


    private RecyclerView objectRV;
    DatabaseHandler objectDatabaseHandler;
    private RVAdapter objectRVAdapter;
    ModelClass modelClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlisthome);



        //Initialize And Assign Variable
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottom_navigation);

        //Set LibraryZo Selected
        bottomNavigationView.setSelectedItemId(R.id.myLibrary);

        //Perform ItemSelectedListener
//        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
//            @Override
//            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
//                switch (item.getItemId()){
//                    case R.id.home:
//                        Intent intent1 = new Intent(getApplicationContext(),Home.class);
//                        intent1.putExtra("user",name);
//                        startActivity(intent1);
//                        overridePendingTransition(0,0);
//                        return true;
//                    case R.id.myLibrary:
//                        return true;
//                    case R.id.uploads:
//
//                        uploadButtonGo();
//                        return true;
//                }
//                return false;
//            }
//        });

        try {

            objectRV = findViewById(R.id.PLRecycleView);
            objectDatabaseHandler = new DatabaseHandler(this);

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }

        try {
            objectRVAdapter = new RVAdapter(this, objectDatabaseHandler.getAllPlaylistData());

            objectRV.setHasFixedSize(true);
            objectRV.setLayoutManager(new LinearLayoutManager(this));
            objectRV.setAdapter(objectRVAdapter);

        } catch (Exception e) {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }


        Button BtnCreatePlaylist = findViewById(R.id.CreatePlaylist);
        BtnCreatePlaylist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent createPlaylist = new Intent(getApplicationContext(), CreatePlaylist.class);
                startActivity(createPlaylist);
            }
        });


        SearchView searchView = findViewById(R.id.Search);
        if (objectRV != null && objectRVAdapter != null) {
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
        ArrayList<ModelClass> filteredList = new ArrayList<>();
        ArrayList<ModelClass> NotFilteredList = objectDatabaseHandler.getModelData(text);

        for(ModelClass item : NotFilteredList)
        {
            if(item.getPlName().toLowerCase().contains(text.toLowerCase()))
            {
                filteredList.add(item);
            }
        }

        objectRVAdapter.filterList(filteredList);
    }



}