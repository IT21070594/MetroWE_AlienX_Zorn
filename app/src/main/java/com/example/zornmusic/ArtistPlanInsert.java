package com.example.zornmusic;

import android.Manifest;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;

import Database.DatabaseHandler;
import Database.DatabaseHandler;
import Database.UserMasters;

public class ArtistPlanInsert extends AppCompatActivity {
    DatabaseHandler dbHandler;
    SQLiteDatabase sqLiteDatabase;
    EditText name,amount,downloadLimit,uploadLimit;
    Button submit,display;
    ImageView imagePlan;

    public static final int CAMERA_REQUEST=100;
    public static final int STORAGE_REQUEST=101;
    String[]cameraPermission;
    String[]storagePermission;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_artist_plan_insert);
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
        dbHandler = new DatabaseHandler(getApplicationContext());
        findid();
        insertData();
        imagePick();
    }
    private void imagePick() {
        imagePlan.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onClick(View view) {
                int imageVal =0;
                if(imageVal==0){
                    if(!checkCameraPermission()){
                        requestCameraPermission();
                    }else{
                        pickFromGallery();
                    }
                }else if(imageVal==1){
                    if(!checkStoragePermission()){
                        requestStoragePermission();
                    }else{
                        pickFromGallery();
                    }
                }
            }
        });
    }
    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestStoragePermission() {
        requestPermissions(storagePermission,STORAGE_REQUEST);
    }

    private boolean checkStoragePermission() {
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        return result;
    }

    private void pickFromGallery() {
        CropImage.activity().start(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    private void requestCameraPermission() {
        requestPermissions(cameraPermission,CAMERA_REQUEST);


    }
    private boolean checkCameraPermission() {
        boolean result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)==(PackageManager.PERMISSION_GRANTED);
        boolean result2 = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA)==(PackageManager.PERMISSION_GRANTED);
        return result && result2;
    }

    private void findid(){
        imagePlan=findViewById(R.id.imagePlan);
        name=findViewById(R.id.planName);
        amount=findViewById(R.id.payamount);
        downloadLimit=findViewById(R.id.downloadLimit);
        submit=findViewById(R.id.btn_submit);
        display=findViewById(R.id.btn_display);
        uploadLimit=findViewById(R.id.uploadLimit);

    }

    private void insertData(){
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ContentValues values = new ContentValues();
                values.put(UserMasters.ArtistPlans.COLUMN2_IMA,ImageViewToByte(imagePlan));
                values.put(UserMasters.ArtistPlans.COLUMN1_PNA,name.getText().toString());
                values.put(UserMasters.ArtistPlans.COLUMN3_AMA,amount.getText().toString());
                values.put(UserMasters.ArtistPlans.COLUMN4_DLA,downloadLimit.getText().toString());
                values.put(UserMasters.ArtistPlans.COLUMN5_ULA,uploadLimit.getText().toString());
                sqLiteDatabase=dbHandler.getWritableDatabase();
                Long recordInsert = sqLiteDatabase.insert(UserMasters.ArtistPlans.TABLE_NAME2,null,values);
                if(recordInsert==-1){
                    Toast.makeText(ArtistPlanInsert.this, "Not inserted successfully", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(ArtistPlanInsert.this, "Inserted successfully", Toast.LENGTH_SHORT).show();
                    //clear when submit is clicked
                    imagePlan.setImageResource(R.mipmap.ic_launcher);
                    name.setText("");
                    amount.setText("");
                    downloadLimit.setText("");
                    uploadLimit.setText("");

                }
            }
        });
        display=findViewById(R.id.btn_display);
        display.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(),viewArtistPremiumPlans.class));
            }
        });
    }
    private byte[] ImageViewToByte(ImageView imagePlan) {
        Bitmap bitmap =((BitmapDrawable)imagePlan.getDrawable()).getBitmap();
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,80,stream);
        byte[]bytes=stream.toByteArray();
        return bytes;
    }
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case CAMERA_REQUEST:{
                if (grantResults.length>0) {
                    boolean camera_accept = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storage_accept = grantResults[1] == PackageManager.PERMISSION_GRANTED;
                    if (camera_accept && storage_accept) {
                        pickFromGallery();
                    } else {
                        Toast.makeText(this, "enable camera and storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
            case STORAGE_REQUEST:{
                if(grantResults.length>0){
                    boolean storage_accept = grantResults[0]==PackageManager.PERMISSION_GRANTED;
                    if(storage_accept){
                        pickFromGallery();
                    }else{
                        Toast.makeText(this, "please enable storage permissions", Toast.LENGTH_SHORT).show();
                    }
                }
            }
            break;
        }
    }

    //override method

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode==CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE){
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode== RESULT_OK){
                Uri resultUri =result.getUri();
                Picasso.with(this).load(resultUri).into(imagePlan);
            }
        }
    }

}