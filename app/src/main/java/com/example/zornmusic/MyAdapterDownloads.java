package com.example.zornmusic;


import static com.example.zorn.DatabaseHandler.TABLE_NAME1;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapterDownloads extends RecyclerView.Adapter<MyAdapterDownloads.ViewHolder> {
    Context context;
    int singledata;
     ArrayList<DownloadModel>downloadModelArrayList;
    SQLiteDatabase sqLiteDatabase;


    //generate constructor


    public MyAdapterDownloads(Context context, int singledata, ArrayList<DownloadModel> downloadModelArrayList, SQLiteDatabase sqLiteDatabase) {
        this.context = context;
        this.singledata = singledata;
        this.downloadModelArrayList = downloadModelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater =  LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.singledownload_data,null);
        return new ViewHolder(view);

    }

    @Override

    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

    final DownloadModel model = downloadModelArrayList.get(position);
    byte[] image = model.getProAvatar();
    Bitmap bitmap = BitmapFactory.decodeByteArray(image, 0, image.length);
    holder.downloadAvatar.setImageBitmap(bitmap);
    holder.downloadTxtName.setText(model.getName());
        holder.downloadTxtName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Bundle bundle = new Bundle();
                bundle.putInt("songId",model.getId());
                bundle.putByteArray("avatar",model.getProAvatar());
                bundle.putString("songName",model.getName());
                Intent intent1 = new Intent(context,displayOneSong.class);
                intent1.putExtra("oneSong", bundle);
                context.startActivity(intent1);
            }
        });

    //downloads menu

    holder.downloadedMenu.setOnClickListener(new View.OnClickListener() {

        @Override
        public void onClick(View view) {
            PopupMenu popupMenu = new PopupMenu(context,holder.downloadedMenu);
            popupMenu.inflate(R.menu.downloadsongmenu);
            popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    switch (item.getItemId()) {
                        case R.id.edit_download_menu:
                            /////
                            //edit operation
                            Bundle bundle = new Bundle();
                            bundle.putInt("id",model.getId());
                            bundle.putByteArray("avatar",model.getProAvatar());
                            bundle.putString("songName",model.getName());
                            Intent intent = new Intent(context,DownloadSong.class);
                            intent.putExtra("songDetails",bundle);
                            context.startActivity(intent);
                            break;
                        case R.id.delete_download_menu:
                            ///delete operation
                            DatabaseHandler dBmain = new DatabaseHandler(context);
                            sqLiteDatabase=dBmain.getReadableDatabase();
                            long songDelete = sqLiteDatabase.delete(TABLE_NAME1,"id="+model.getId(),null);
                            if (songDelete!=-1){
                                Toast.makeText(context, "deleted successfully", Toast.LENGTH_SHORT).show();
                                //remove position after deleted
                                downloadModelArrayList.remove(position);
                                //update the changes made
                                notifyDataSetChanged();
                            }

                            break;
                        default:
                            return false;
                    }
                    return false;
                }
            });
            //display menu
            popupMenu.show();
        }
    });


    }

    @Override
    public int getItemCount() {
        return downloadModelArrayList.size();
    }





    public void setFilteredList(ArrayList<DownloadModel> filteredList) {
        this.downloadModelArrayList = filteredList;
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView downloadAvatar;
        TextView  downloadTxtName;
        ImageButton downloadedMenu;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            downloadAvatar  = (ImageView)itemView.findViewById(R.id.viewdownloadedavatar);
            downloadTxtName = (TextView)itemView.findViewById(R.id.txt_download_name);
            downloadedMenu = (ImageButton)itemView.findViewById(R.id.download_song_menu);
        }
    }
}
