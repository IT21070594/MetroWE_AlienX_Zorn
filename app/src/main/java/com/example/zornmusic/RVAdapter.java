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


public class RVAdapter extends RecyclerView.Adapter<RVAdapter.RVViewHolderClass> implements Filterable
{
    ArrayList<ModelClass> objectModelClassList;
    ArrayList<ModelClass> objectModelClassListFull;
    Context context;


    //Generate constructor
    public RVAdapter(Context context, ArrayList<ModelClass> objectModelClassList) {

        this.objectModelClassList = objectModelClassList;
        this.context = context;

        //A copy of the Songs list to be used independently
        objectModelClassListFull = new ArrayList<>(objectModelClassList);
    }

    @NonNull
    @Override
    public RVViewHolderClass onCreateViewHolder(@NonNull ViewGroup viewGroup, int viewType)
    {
        return new RVViewHolderClass(LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.plrecycleview_row, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull RVViewHolderClass holder, int position)
    {
        ModelClass objectModelClass = objectModelClassList.get(position);

        holder.plName.setText(objectModelClass.getPlName());
        holder.plImage.setImageBitmap(objectModelClass.getImage());

        holder.itemView.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("playlist_id", objectModelClass.getPid());
                bundle.putString("playlist_Name", objectModelClass.getPlName());

                //To convert Bitmap image to byte array.
                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap imageToStore = objectModelClass.getImage();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                bundle.putByteArray("playlist_image", imageInBytes);



                //Navigate to new activity passing the song id
                Intent intent = new Intent(context,Moredetailspl.class);
                intent.putExtra("playlistData", bundle);
                context.startActivity(intent);
            }
        });

        holder.plName.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("playlist_id", objectModelClass.getPid());
                bundle.putString("playlist_Name", objectModelClass.getPlName());

                //To convert Bitmap image to byte array.
                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap imageToStore = objectModelClass.getImage();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                bundle.putByteArray("playlist_image", imageInBytes);



                //Navigate to new activity passing the song id
                Intent intent = new Intent(context,Moredetailspl.class);
                intent.putExtra("playlistData", bundle);
                context.startActivity(intent);
            }
        });

        holder.plImage.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View view)
            {
                Bundle bundle = new Bundle();
                bundle.putInt("playlist_id", objectModelClass.getPid());
                bundle.putString("playlist_Name", objectModelClass.getPlName());

                //To convert Bitmap image to byte array.
                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                Bitmap imageToStore = objectModelClass.getImage();
                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                bundle.putByteArray("playlist_image", imageInBytes);



                //Navigate to new activity passing the song id
                Intent intent = new Intent(context,Moredetailspl.class);
                intent.putExtra("playlistData", bundle);
                context.startActivity(intent);
            }
        });

        //flow menu
        holder.flowMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context,holder.flowMenu);
                popupMenu.inflate(R.menu.pl_menu);
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId())
                        {
                            case R.id.edit_menu:
                                //Edit operation
                                Bundle bundle = new Bundle();
                                bundle.putInt("playlist_id", objectModelClass.getPid());
                                bundle.putString("playlist_Name", objectModelClass.getPlName());

                                //To convert Bitmap image to byte array.
                                ByteArrayOutputStream objectByteArrayOutputStream = new ByteArrayOutputStream();
                                Bitmap imageToStore = objectModelClass.getImage();
                                imageToStore.compress(Bitmap.CompressFormat.JPEG, 100, objectByteArrayOutputStream);
                                byte[] imageInBytes = objectByteArrayOutputStream.toByteArray();
                                bundle.putByteArray("playlist_image", imageInBytes);



                                //Navigate to new activity passing the song id
                                Intent intent = new Intent(context,EditPlaylistName.class);
                                intent.putExtra("playlistData", bundle);
                                context.startActivity(intent);
                                break;

                            case R.id.delete_menu:
                                //Delete operation
                                DatabaseHandler dbhandler = new DatabaseHandler(context);
                                dbhandler.deletePlaylist(objectModelClass.getPid());

                                //Remove position after deleting the record
                                objectModelClassList.remove(position);

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
        return objectModelClassList.size();
    }

    public static class RVViewHolderClass extends RecyclerView.ViewHolder
    {
        TextView plName;
        ImageView plImage;
        ImageButton flowMenu;
        public RVViewHolderClass(@NonNull View itemView) {
            super(itemView);
            plName = itemView.findViewById(R.id.PlaylistText3);
            plImage = itemView.findViewById(R.id.PLimage);
            flowMenu = itemView.findViewById(R.id.moreOption);
        }
    }

    public void filterList(ArrayList<ModelClass> filteredList)
    {
        objectModelClassList = filteredList;
        notifyDataSetChanged();
    }

    public Filter getFilter()
    {
        return playlistFilter;
    }

    private Filter playlistFilter = new Filter() {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {
            ArrayList<ModelClass> filteredList = new ArrayList<>();

            if(constraint == null || constraint.length() == 0)
            {
                filteredList.addAll(objectModelClassListFull);
            }
            else
            {
                String filterPattern = constraint.toString().toLowerCase().trim();

                for(ModelClass item : objectModelClassListFull)
                {
                    if(item.getPlName().toLowerCase().contains(filterPattern))
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
            objectModelClassList.clear();
            objectModelClassList.addAll((ArrayList)filterResults.values);
            notifyDataSetChanged();
        }
    };
}
