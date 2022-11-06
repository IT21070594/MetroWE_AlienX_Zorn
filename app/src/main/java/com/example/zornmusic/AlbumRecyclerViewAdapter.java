package com.example.zornmusic;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
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

import Database.Albums;
import Database.DatabaseHandler;

public class AlbumRecyclerViewAdapter extends RecyclerView.Adapter<AlbumRecyclerViewAdapter.AlbumRVViewHolderClass> implements Filterable
{
    ArrayList<Albums> objectAlbumClassList;
    ArrayList<Albums> objectAlbumClassListFull;
    Context context;

    //Generate constructor
    public AlbumRecyclerViewAdapter(Context context, ArrayList<Albums> objectAlbumClassList) {

        this.objectAlbumClassList = objectAlbumClassList;
        this.context = context;

        //A copy of the Albums list to be used independently
        objectAlbumClassListFull = new ArrayList<>(objectAlbumClassList);
    }

    @NonNull
    @Override
    public AlbumRecyclerViewAdapter.AlbumRVViewHolderClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        return new AlbumRecyclerViewAdapter.AlbumRVViewHolderClass(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.single_row_album, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumRecyclerViewAdapter.AlbumRVViewHolderClass holder, int position)
    {
        Albums objectAlbumClass = objectAlbumClassList.get(position);

        holder.albumNameTV.setText(objectAlbumClass.getAlbumName());
        holder.albumImageView.setImageBitmap(objectAlbumClass.getAlbumImage());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("album_id", objectAlbumClass.getAlbumID());
                bundle.putString("album_name", objectAlbumClass.getAlbumName());
                bundle.putString("user",objectAlbumClass.getArtistName());


                //To convert Bitmap image to byte array.
                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap imageToStore = objectAlbumClass.getAlbumImage();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                bundle.putByteArray("album_image", imageInBytes);

                //Navigate to new activity passing the album id
                Intent intent = new Intent(context,ViewAlbumSongs.class);
                intent.putExtra("albumData", bundle);
                context.startActivity(intent);
            }
        });

        holder.albumNameTV.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("album_id", objectAlbumClass.getAlbumID());
                bundle.putString("album_name", objectAlbumClass.getAlbumName());
                bundle.putString("user",objectAlbumClass.getArtistName());

                //To convert Bitmap image to byte array.
                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap imageToStore = objectAlbumClass.getAlbumImage();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                bundle.putByteArray("album_image", imageInBytes);

                //Navigate to new activity passing the album id
                Intent intent = new Intent(context,ViewAlbumSongs.class);
                intent.putExtra("albumData", bundle);
                context.startActivity(intent);
            }
        });

        holder.albumImageView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("album_id", objectAlbumClass.getAlbumID());
                bundle.putString("album_name", objectAlbumClass.getAlbumName());
                bundle.putString("user",objectAlbumClass.getArtistName());

                //To convert Bitmap image to byte array.
                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap imageToStore = objectAlbumClass.getAlbumImage();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                bundle.putByteArray("album_image", imageInBytes);

                //Navigate to new activity passing the album id
                Intent intent = new Intent(context,ViewAlbumSongs.class);
                intent.putExtra("albumData", bundle);
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
                                bundle.putInt("album_id", objectAlbumClass.getAlbumID());
                                bundle.putString("album_name", objectAlbumClass.getAlbumName());
                                bundle.putString("user",objectAlbumClass.getArtistName());

                                //To convert Bitmap image to byte array.
                                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                                Bitmap imageToStore = objectAlbumClass.getAlbumImage();
                                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                                bundle.putByteArray("album_image", imageInBytes);

                                Intent intent = new Intent(context,EditAlbum.class);
                                intent.putExtra("albumData", bundle);
                                context.startActivity(intent);
                                break;

                            case R.id.delete_menu:
                                //Delete operation
                                DatabaseHandler dbhandler = new DatabaseHandler(context);
                                dbhandler.deleteAlbum(objectAlbumClass.getAlbumID());

                                //Remove position after deleting the record
                                objectAlbumClassList.remove(position);

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
        return objectAlbumClassList.size();
    }

    public static class AlbumRVViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView albumNameTV;
        ImageView albumImageView;
        ImageButton flowMenu;
        public AlbumRVViewHolderClass(@NonNull View itemView) {
            super(itemView);
            albumNameTV = itemView.findViewById(R.id.singleAlbumRowTV);
            albumImageView = itemView.findViewById(R.id.singleAlbumImageView);
            flowMenu = itemView.findViewById(R.id.albumFlowmenu);
        }
    }

    public void filterList(ArrayList<Albums> filteredList)
    {
        objectAlbumClassList = filteredList;
        notifyDataSetChanged();
    }

    public Filter getFilter()
    {
        return albumsFilter;
    }

    private Filter albumsFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<Albums> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(objectAlbumClassListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                //For each album in objectAlbumClassListFull
                for(Albums item : objectAlbumClassListFull)
                {
                    if(item.getAlbumName().toLowerCase().contains(filterPattern))
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
            objectAlbumClassList.clear();
            objectAlbumClassList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
