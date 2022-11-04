package com.example.zornmusic;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import Database.DatabaseHandler;
import Database.DatabaseHandler;
import Database.UserMasters;

public class SignUp extends AppCompatActivity {
    EditText name,email,password;
    CheckBox checkBox;
    Button btn;
    DatabaseHandler DB;
    Spinner mySpinner;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
         mySpinner =  findViewById(R.id.spinner1);
        ArrayAdapter<String> myAdapter = new ArrayAdapter<String>(SignUp.this, android.R.layout.simple_list_item_1, getResources().getStringArray(R.array.accTypes));
        myAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        mySpinner.setAdapter(myAdapter);
        mySpinner.setOnItemSelectedListener(listener);
        DB = new DatabaseHandler(this);

    }

    //
    private AdapterView.OnItemSelectedListener listener = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            // ((TextView) parent.getChildAt(0)).setTextColor(Color.WHITE);
            DB = new DatabaseHandler(getApplicationContext());

             btn = findViewById(R.id.btnContinue1);

            name=findViewById(R.id.nameInput);
            email=findViewById(R.id.emailInput);
            password=findViewById(R.id.inputPass);

             String user = name.getText().toString();
             String passwordValue= password.getText().toString();
             String emailValue=email.getText().toString();
             String spinnerValue=mySpinner.getSelectedItem().toString();
             if (position == 0){
                 btn.setOnClickListener(new View.OnClickListener() {
                     @Override
                     public void onClick(View view) {
                         Toast.makeText(SignUp.this, "Select a n acc type", Toast.LENGTH_SHORT).show();
                     }
                 });
             }
             else if (position == 1) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAllFieldsChecked = false;

                        isAllFieldsChecked = CheckAllFields();
                        if(isAllFieldsChecked){
                            Boolean checkuser = DB.checkUserName(user);
                            if (checkuser==false){
                                Boolean insert = DB.insertData(user,passwordValue,emailValue,spinnerValue);
                                if(insert==true){
                                    Toast.makeText(SignUp.this, "Registered Successfully..", Toast.LENGTH_SHORT).show();
                                    Intent intent1 = new Intent(getApplicationContext(), login.class);
                                    intent1.putExtra("user", name.getText().toString());
                                    startActivity(intent1);
                                }else {
                                    Toast.makeText(SignUp.this, "Register unsuccessfull..", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }
                });
            } else if (position == 2) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAllFieldsChecked = false;

                        isAllFieldsChecked = CheckAllFields();
                        if(isAllFieldsChecked){
                            Boolean checkuser = DB.checkUserName(user);
                            if(checkuser==false){
                                Boolean insert = DB.insertData(user,passwordValue,emailValue,spinnerValue);
                                if(insert==true){
                                    Toast.makeText(SignUp.this, "Registered Successfully..", Toast.LENGTH_SHORT).show();
                                    Intent intent2 = new Intent(getApplicationContext(), viewPlans.class);
                                    intent2.putExtra("user", name.getText().toString());
                                    startActivity(intent2);
                                }else {
                                    Toast.makeText(SignUp.this, "Register unsuccessfull..", Toast.LENGTH_SHORT).show();
                                }
                            }}
                    }
                });

            } else if (position == 3) {
                btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        boolean isAllFieldsChecked = false;

                        isAllFieldsChecked = CheckAllFields();
                        if(isAllFieldsChecked){
                            Boolean checkuser = DB.checkUserName(user);
                            if(checkuser==false){
                                Boolean insert=DB.insertData(user,passwordValue,emailValue,spinnerValue);
                                if(insert==true){
                                    Toast.makeText(SignUp.this, "Registered successfully", Toast.LENGTH_SHORT).show();
                                    Intent intent3 = new Intent(getApplicationContext(), viewArtistPremiumPlans.class);
                                    intent3.putExtra("user", name.getText().toString());
                                    startActivity(intent3);
                                }else{
                                    Toast.makeText(SignUp.this, "Register unsuccessfull..", Toast.LENGTH_SHORT).show();
                                }
                            }else{
                                Toast.makeText(SignUp.this, "User already exists.. Please Sign in..", Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                });
            }
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }


    };
    private boolean CheckAllFields() {
         name=findViewById(R.id.nameInput);
         email=findViewById(R.id.emailInput);
         password=findViewById(R.id.inputPass);
         checkBox = findViewById(R.id.checkBox1);

        if (name.length() == 0) {
            name.setError("This field is required");
            return false;
        }

        if (email.length() == 0) {
            email.setError("This field is required");
            return false;
        }


        if (password.length() == 0) {
            password.setError("Password is required");
            return false;
        } else if (password.length() < 8) {
            password.setError("Password must be minimum 8 characters");
            return false;
        }

        if(checkBox.isChecked()){
            // Do something
            Toast.makeText(getApplicationContext(),"Accepted", Toast.LENGTH_SHORT).show();
        } else {

            // change color_secondary to make the check box red.
            checkBox.setError("Please accept terms and conditions!");
            checkBox.setTextColor(Color.parseColor("#FF0000"));
            return false;

        }
        // after all validation return true.
        return true;
    }

}


