package com.example.zornmusic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import Database.DatabaseHandler;
import Database.DatabaseHandler;
import Database.UserMasters;

public class premiumMakePayment extends AppCompatActivity {
    ImageButton back;
    TextView planName,amount;
    EditText card,exp,sec;
    Button buy;
    CheckBox check;
    Intent receiveIntent;
    String plan,username;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_premium_make_payment);
        planName=findViewById(R.id.planname);
        amount=findViewById(R.id.payValue);
        receiveIntent=getIntent();
        Bundle bundle=getIntent().getBundleExtra("userinfo");
         plan =String.valueOf(bundle.getString("plan"));
         username =String.valueOf(bundle.getString("user"));
        int amountpay = bundle.getInt("payamount");
        amount.setText(String.valueOf(bundle.getInt("payamount")));
        planName.setText(String.valueOf(bundle.getString("plan")));
        back=findViewById(R.id.imageButton2);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        buy = findViewById(R.id.btnMakePay);
        buy.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                boolean isAllfieldsChecked= false;
                DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");
                //ofPattern("yyyy/MM/dd HH:mm:ss");
                LocalDateTime now = LocalDateTime.now();
                String datetime= String.valueOf(dtf.format(now));
                isAllfieldsChecked =CheckAllFields();
                if(isAllfieldsChecked){
                    //1234123412341234
                    DatabaseHandler DB = new DatabaseHandler(getApplicationContext());
                    Boolean insert = DB.insertPayment(username,amountpay, datetime,plan);
                    if(insert==true){
                        Toast.makeText(getApplicationContext(), "Payment successfull..", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(getApplicationContext(), Home.class);
                        intent1.putExtra("user",username);
                        startActivity(intent1);
                    }else {
                        Toast.makeText(getApplicationContext(), "Payment unsuccessfull..", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }
    private boolean CheckAllFields() {
        card=findViewById(R.id.cardNo);
        exp=findViewById(R.id.expiry);
        sec = findViewById(R.id.security);
        check = findViewById(R.id.terms);

        if (exp.length() == 0) {
            exp.setError("This field is required");
            return false;
        } else if(exp.length()!=4){
            exp.setError("Invalid exp date 4 digits required");
        }


        if (card.length() == 0) {
            card.setError("Card No Required");
            return false;
        }else if (card.length()!=16){
            card.setError("Invalid card number enter 16 digits");
        }
        if (sec.length() == 0) {
            sec.setError("Code required");
            return false;
        } else if (sec.length() != 3) {
            sec.setError("Invalide code");
            return false;
        }

        if(check.isChecked()){
            // Do something
            Toast.makeText(getApplicationContext(),"Accepted", Toast.LENGTH_SHORT).show();
        } else {

            // change color_secondary to make the check box red.
            check.setError("Please accept terms and conditions!");
            check.setTextColor(Color.parseColor("#FF0000"));
            return false;

        }
        // after all validation return true.
        return true;
    }
}