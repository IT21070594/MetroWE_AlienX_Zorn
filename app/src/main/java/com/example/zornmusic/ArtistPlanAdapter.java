package com.example.zornmusic;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import Databases.DBHandler;
import Databases.UserMasters;

public class ArtistPlanAdapter extends RecyclerView.Adapter<ArtistPlanAdapter.ViewHolder> {
    Context context;
    ArrayList<ArtistPlanModel> modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    int singledata;

    //constructor

    public ArtistPlanAdapter(Context context, ArrayList<ArtistPlanModel> modelArrayList, SQLiteDatabase sqLiteDatabase, int singledata) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.singledata = singledata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.artistplansingledata,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final ArtistPlanModel model=modelArrayList.get(position);
        byte[]image=model.getImage();
            Bitmap bitmap = BitmapFactory.decodeByteArray(image,0, image.length);
            holder.imageArtistPlan.setImageBitmap(bitmap);
            holder.purchasePlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    DBHandler dbHandler = new DBHandler(context.getApplicationContext(), UserMasters.ArtistUsers.TABLE_NAME4,null,1);
                    Boolean insertresult = dbHandler.insertArtistPlan(model.getUsername(),model.getPlanName());
                    if (insertresult==true){
                        Toast.makeText(context, "Plan chosen successfully", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("user",model.getUsername());
                        bundle.putString("plan",model.getPlanName());
                        bundle.putInt("payamount",model.getAmount());
                        bundle.putInt("downlimit",model.getNoOfDownloads());
                        bundle.putInt("uploadlimit",model.getNoOfUploads());
                        bundle.putByteArray("image", model.getImage());
                        Intent intent1 = new Intent(context,makePayment.class);
                        intent1.putExtra("userinfo",bundle);
                        context.startActivity(intent1);

                    }else
                        Toast.makeText(context, "not inserted..", Toast.LENGTH_SHORT).show();
                }
            });
    }

    @Override
    public int getItemCount() {
        return modelArrayList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageArtistPlan;
        Button purchasePlan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageArtistPlan=(ImageView)itemView.findViewById(R.id.viewartistplanimage);
            purchasePlan=(Button)itemView.findViewById(R.id.chooseartistplanbtn);
        }
    }
}
