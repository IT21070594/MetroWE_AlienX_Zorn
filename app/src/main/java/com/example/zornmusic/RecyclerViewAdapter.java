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

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.RVViewHolderClass> implements Filterable
{
    ArrayList<Songs> objectSongClassList;
    ArrayList<Songs> objectSongClassListFull;
    Context context;


    //Generate constructor
    public RecyclerViewAdapter(Context context, ArrayList<Songs> objectSongClassList) {

        this.objectSongClassList = objectSongClassList;
        this.context = context;

        //A copy of the Songs list to be used independently
        objectSongClassListFull = new ArrayList<>(objectSongClassList);
    }

    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        return new RVViewHolderClass(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass holder, int position)
    {
        Songs objectSongClass = objectSongClassList.get(position);

        holder.songNameTV.setText(objectSongClass.getSongName());
        holder.objectImageView.setImageBitmap(objectSongClass.getImage());

        if(MyMediaPlayer.currentIndex==position)
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

                //Resetting the media player when any item view is clicked
                MyMediaPlayer.getInstance().reset();
                //Setting the current index at that position
                MyMediaPlayer.currentIndex = position;

                //Navigate to new activity passing the song id
                Intent intent = new Intent(context,ViewSong.class);
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

                //Resetting the media player when any item view is clicked
                MyMediaPlayer.getInstance().reset();
                //Setting the current index at that position
                MyMediaPlayer.currentIndex = position;

                Intent intent = new Intent(context,ViewSong.class);
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

                //Resetting the media player when any item view is clicked
                MyMediaPlayer.getInstance().reset();
                //Setting the current index at that position
                MyMediaPlayer.currentIndex = position;

                Intent intent = new Intent(context,ViewSong.class);
                intent.putExtra("songData", bundle);
                context.startActivity(intent);
            }
        });

        //flow menu
        holder.flowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.flowMenu);
                popupMenu.inflate(R.menu.flow_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.edit_menu:
                                //Edit operation
                                Bundle bundle = new Bundle();
                                bundle.putInt("song_id", objectSongClass.getSongID());

                                //To convert Bitmap image to byte array.
                                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                                Bitmap imageToStore = objectSongClass.getImage();
                                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                                bundle.putByteArray("song_image", imageInBytes);

                                bundle.putString("song_name", objectSongClass.getSongName());
                                bundle.putString("song_lang", objectSongClass.getLang());
                                bundle.putString("song_genre", objectSongClass.getGenre());

                                Intent intent = new Intent(context,UpdateSong.class);
                                intent.putExtra("songData", bundle);
                                context.startActivity(intent);
                                break;

                            case R.id.delete_menu:
                                //Delete operation
                                DatabaseHandler dbhandler = new DatabaseHandler(context);
                                dbhandler.deleteSong(objectSongClass.getSongID());

                                //Remove position after deleting the record
                                objectSongClassList.remove(position);

                                //update data
                                notifyDataSetChanged();
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

    public static class RVViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView songNameTV;
        ImageView objectImageView;
        ImageButton flowMenu;
        public RVViewHolderClass(@NonNull View itemView) {
            super(itemView);
            songNameTV = itemView.findViewById(R.id.singleRowTV);
            objectImageView = itemView.findViewById(R.id.singleImageView);
            flowMenu = itemView.findViewById(R.id.flowmenu);
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
