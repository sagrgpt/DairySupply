package com.example.sagar.dairysupply;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UserInfo extends AppCompatActivity {

    private TextView username;
    private EditText contact,addr1,addr2,addr3,zipcode,landmark;
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    private String location;
    private String KEY;
    private Toolbar mtoolbar;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);

        //Getting user key
        SharedPreferences sharedPreferences = getSharedPreferences("Settings",Context.MODE_PRIVATE);
        KEY = sharedPreferences.getString("KEY",null);

        //Adding toolbar to the layout
        mtoolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mtoolbar);



        //Creating reference to UserTable
        mDatabase = FirebaseDatabase.getInstance().getReference("UserTable");

        //Connecting all the data edit text
        contact = (EditText) findViewById(R.id.contact);
        addr1 = (EditText) findViewById(R.id.addr1);
        addr2 = (EditText) findViewById(R.id.addr2);
        addr3 = (EditText) findViewById(R.id.addr3);
        zipcode = (EditText) findViewById(R.id.zipcode);
        landmark = (EditText) findViewById(R.id.landmark);
        username = (TextView) findViewById(R.id.username);

        myRef = mDatabase.child(KEY).child("Name");
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String name = dataSnapshot.getValue(String.class);
                username.setText(name);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

                Toast.makeText(UserInfo.this,"Database Error "+databaseError,Toast.LENGTH_SHORT).show();
            }
        });

    }


    private Boolean validate() {
        if(contact.getText().toString().length()!=10){
            Toast.makeText(UserInfo.this,"Contact Empty!!",Toast.LENGTH_SHORT).show();
            return false;
        }
        if(addr1.getText().toString().length()==0){
            Toast.makeText(UserInfo.this,"House NO Empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addr2.getText().toString().length()==0){
            Toast.makeText(UserInfo.this,"Area Name Empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(addr3.getText().toString().length()==0){
            Toast.makeText(UserInfo.this,"City Name Empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(zipcode.getText().toString().length()!=6){
            Toast.makeText(UserInfo.this,"Zip Empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        else if(landmark.getText().toString().length()==0){
            Toast.makeText(UserInfo.this,"Landmark Empty!",Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    public void onSaveData(View view) {
        if(validate()){

            mDatabase = FirebaseDatabase.getInstance().getReference("UserTable");
            myRef = mDatabase.child(KEY);
            Toast.makeText(UserInfo.this,KEY,Toast.LENGTH_SHORT).show();
            location = addr1.getText().toString()+", "+addr2.getText().toString()+", "+addr3.getText().toString()+", Near "+landmark.getText().toString();
            myRef.child("Contact").setValue(contact.getText().toString());
            myRef.child("Location").setValue(location);
            myRef.child("zipcode").setValue(zipcode.getText().toString());

            sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean("NewUser",true);
            editor.apply();

            startActivity(new Intent(UserInfo.this,OrderActivity.class));

        }
    }
}
