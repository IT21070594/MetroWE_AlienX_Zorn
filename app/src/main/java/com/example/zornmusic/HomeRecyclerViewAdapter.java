package com.example.zornmusic;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

import Database.DatabaseHandler;
import Database.Songs;

public class HomeRecyclerViewAdapter extends RecyclerView.Adapter<HomeRecyclerViewAdapter.HomeRVViewHolderClass> implements Filterable
{
    ArrayList<Songs> objectSongClassList;
    ArrayList<Songs> objectSongClassListFull;
    Context context;
    String user;


    //Generate constructor
    public HomeRecyclerViewAdapter(Context context, ArrayList<Songs> objectSongClassList, String user) {

        this.objectSongClassList = objectSongClassList;
        this.context = context;

        //A copy of the Songs list to be used independently
        objectSongClassListFull = new ArrayList<>(objectSongClassList);

        this.user=user;
    }

    @NonNull
    @Override
    public HomeRVViewHolderClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        return new HomeRVViewHolderClass(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_row_home, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull HomeRVViewHolderClass holder, int position)
    {
        Songs objectSongClass = objectSongClassList.get(position);

        holder.songNameTV.setText(objectSongClass.getSongName());
        holder.objectImageView.setImageBitmap(objectSongClass.getImage());

        if(MyMediaPlayer2.currentIndex==position)
        {
            holder.songNameTV.setTextColor(Color.parseColor("#F44336"));
        }

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("song_id", objectSongClass.getSongID());
                bundle.putString("user",user);

                //Resetting the media player when any item view is clicked
                MyMediaPlayer2.getInstance().reset();
                //Setting the current index at that position
                MyMediaPlayer2.currentIndex = position;

                //Navigate to new activity passing the song id
                Intent intent = new Intent(context,ViewSongHome.class);
                intent.putExtra("songData", bundle);
                context.startActivity(intent);
            }
        });

        holder.songNameTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("song_id", objectSongClass.getSongID());
                bundle.putString("user",user);
                //Resetting the med2ia player when any item view is clicked
                MyMediaPlayer2.getInstance().reset();
                //Setting the current index at that position
                MyMediaPlayer2.currentIndex = position;

                Intent intent = new Intent(context,ViewSongHome.class);
                intent.putExtra("songData", bundle);
                context.startActivity(intent);
            }
        });

        holder.objectImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("song_id", objectSongClass.getSongID());
                bundle.putString("user",user);
                //Resetting the media player when any item view is clicked
                MyMediaPlayer2.getInstance().reset();
                //Setting the current index at that position
                MyMediaPlayer2.currentIndex = position;

                Intent intent = new Intent(context,ViewSongHome.class);
                intent.putExtra("songData", bundle);
                context.startActivity(intent);
            }
        });

        //flow menu
        holder.flowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.flowMenu);
                popupMenu.inflate(R.menu.flow_menu_home);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.addToPlaylist_menu:

                                break;

                            case R.id.download_menu:
                                Bundle bundle = new Bundle();
                                bundle.putInt("song_id", objectSongClass.getSongID());
                                bundle.putString("user",user);
                                Intent intent = new Intent(context,DownloadSong.class);
                                intent.putExtra("userDetails",bundle);
                                context.startActivity(intent);
                                break;

                            default:
                                return false;
                        }
                        return false;
                    }
                });
                //Display menu
                popupMenu.show();
            }
        });
    }

    @Override
    public int getItemCount()
    {
        return objectSongClassList.size();
    }

    public static class HomeRVViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView songNameTV;
        ImageView objectImageView;
        ImageButton flowMenu;

        public HomeRVViewHolderClass(@NonNull View itemView) {
            super(itemView);
            songNameTV = itemView.findViewById(R.id.singleHomeRowTV);
            objectImageView = itemView.findViewById(R.id.singleHomeImageView);
            flowMenu = itemView.findViewById(R.id.homeFlowmenu);
        }
    }

    public void filterList(ArrayList<Songs> filteredList)
    {
        objectSongClassList = filteredList;
        notifyDataSetChanged();
    }

    public Filter getFilter()
    {
        return songsFilter;
    }

    private Filter songsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Songs> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(objectSongClassListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                //For each song in objectSongClassListFull
                for(Songs item : objectSongClassListFull)
                {
                    if(item.getSongName().toLowerCase().contains(filterPattern))
                    {
                        filteredList.add(item);
                    }
                }
            }

            FilterResults results = new FilterResults();
            results.values = filteredList;

            return results; //Return FilterResults to publishResults method
        }

        @Override
        protected void publishResults(CharSequence charSequence, FilterResults filterResults)
        {
            objectSongClassList.clear();
            objectSongClassList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };
}

