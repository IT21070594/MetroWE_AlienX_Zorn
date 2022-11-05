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

import Database.DatabaseHandler;
import Database.DatabaseHandler;
import Database.UserMasters;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    Context context;
    ArrayList<Model>modelArrayList;
    SQLiteDatabase sqLiteDatabase;
    int singledata;

    //generate constructor

    public MyAdapter(Context context, ArrayList<Model> modelArrayList, SQLiteDatabase sqLiteDatabase, int singledata) {
        this.context = context;
        this.modelArrayList = modelArrayList;
        this.sqLiteDatabase = sqLiteDatabase;
        this.singledata = singledata;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view=inflater.inflate(R.layout.singledata,null);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        final Model model=modelArrayList.get(position);
        byte[]image= model.getImage();
            Bitmap bitmap= BitmapFactory.decodeByteArray(image,0,image.length);
            holder.imagePlan.setImageBitmap(bitmap);
            holder.getPlan.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                   // Intent in
                    DatabaseHandler dbHandler = new DatabaseHandler(context.getApplicationContext());
                    Boolean insertresult = dbHandler.insertUserPlan(model.getUsername(),model.getPlanName());
                    if (insertresult==true){
                        Toast.makeText(context, "Plan chosen successfully", Toast.LENGTH_SHORT).show();
                        Bundle bundle = new Bundle();
                        bundle.putString("user",model.getUsername());
                        bundle.putString("plan",model.getPlanName());
                        bundle.putInt("payamount",model.getAmount());
                        bundle.putInt("downlimit",model.getNoOfDownloads());
                        //bundle.putInt("uploadlimit",model.getNoOfUploads());
                        bundle.putByteArray("image", model.getImage());
                        Intent intent1 = new Intent(context,premiumMakePayment.class);
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
        ImageView imagePlan;
        Button getPlan;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imagePlan=(ImageView)itemView.findViewById(R.id.viewplanimage);
            getPlan=(Button)itemView.findViewById(R.id.chooseplanbtn);
        }
    }
}
