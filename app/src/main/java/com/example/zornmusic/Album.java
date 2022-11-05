package com.example.zornmusic;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.CursorWindow;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.lang.reflect.Field;

import Database.DatabaseHandler;

public class Album extends AppCompatActivity {

    private DatabaseHandler objectDBHandler;
    private RecyclerView objectAlbumRV;
    private TextView noAlbums;
    private AlbumRecyclerViewAdapter objectAlbumRVAdapter;
    private String artistName;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_album);
       // intent=getIntent();
        //nam

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

        Button songsBtn = findViewById(R.id.songsBtn);
        songsBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Album.this, MainActivity.class);
                intent.putExtra("user", artistName );
                //artistintent.putExtra("user", intent.getStringExtra("user"));
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });



        ImageButton createImgBtn = findViewById(R.id.createImgBtn);
        createImgBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Album.this,CreateNewAlbum.class);
                startActivity(intent);
                overridePendingTransition(0,0);
            }
        });

        try
        {
            noAlbums = findViewById(R.id.noAlbumsText);
            objectAlbumRV= findViewById(R.id.albumsRV);

            intent = getIntent();
            artistName = intent.getStringExtra("user");

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
            field.set(null, 500*1024*1024); //500MB is the new size

            if(objectDBHandler.getArtistAlbumsData(artistName).size()==0)
            {
                noAlbums.setVisibility(View.VISIBLE);
                objectAlbumRV.setVisibility(View.GONE);
            }
            else
            {
                objectAlbumRVAdapter = new AlbumRecyclerViewAdapter(this,objectDBHandler.getArtistAlbumsData(artistName));

                objectAlbumRV.setHasFixedSize(true);
                objectAlbumRV.setLayoutManager(new LinearLayoutManager(this));
                objectAlbumRV.setAdapter(objectAlbumRVAdapter);
            }
        }
        catch (Exception e)
        {
            //Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }

        SearchView albumSearchView = findViewById(R.id.albumSearchBar);
        if(objectAlbumRV !=null && objectAlbumRVAdapter != null) {
            albumSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextSubmit(String query) {
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    objectAlbumRVAdapter.getFilter().filter(newText);
                    return false;
                }
            });
        }
        Button createNewBtn = findViewById(R.id.createNewBtn);
        createNewBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent1 = new Intent(Album.this,CreateNewAlbum.class);
                intent1.putExtra("user",artistName);
                startActivity(intent1);
                overridePendingTransition(0,0);
            }
        });
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        if(objectAlbumRV!=null && objectAlbumRVAdapter!=null)
        {
            objectAlbumRVAdapter = new AlbumRecyclerViewAdapter(this,objectDBHandler.getArtistAlbumsData(artistName));

            objectAlbumRV.setHasFixedSize(true);
            objectAlbumRV.setLayoutManager(new LinearLayoutManager(this));
            objectAlbumRV.setAdapter(objectAlbumRVAdapter);
        }
        else
        {
            noAlbums.setVisibility(View.VISIBLE);
            objectAlbumRV.setVisibility(View.GONE);
        }
    }

}