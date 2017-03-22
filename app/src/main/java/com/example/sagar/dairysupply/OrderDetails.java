package com.example.sagar.dairysupply;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    int qty;
    double price=20.00;
    double totalCost;
    private TextView username, quantity, address, contact, slot, cost;
    private Toolbar mToolbar;

    //Database Variables
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    private String KEY;

    //User data variables
    private String nameS, contactS, locationS, zipcodeS,quantityS, slotS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);



        //Getting user key
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        KEY = sharedPreferences.getString("KEY",null);

//      Adding toolbar to the layout
        mToolbar =(Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        setTitle("Your Order!");

        username = (TextView) findViewById(R.id.username);
        quantity = (TextView) findViewById(R.id.quantity);
        address = (TextView) findViewById(R.id.address);
        contact = (TextView) findViewById(R.id.phone_number);
        slot = (TextView) findViewById(R.id.timingSlot);
        cost = (TextView) findViewById(R.id.cost);
        final Bundle data = getIntent().getExtras();
        quantityS = data.getString("Quantity");
        quantity.setText(quantityS);
        slotS = data.getString("Slot");
        slot.setText(slotS);
        qty = Integer.parseInt(quantity.getText().toString());
        totalCost = qty*price;
        cost.setText(String.valueOf(totalCost));

//      getting reference to the UserTable of the database
        mDatabase = FirebaseDatabase.getInstance().getReference("UserTable");
//      getting reference to the current user data
        myRef = mDatabase.child(KEY);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map <String, String> map = (Map)dataSnapshot.getValue();
                nameS = map.get("Name");
                contactS = map.get("Contact");
                locationS = map.get("Location");
                zipcodeS = map.get("zipcode");

                username.setText(nameS);
                address.setText(locationS+" "+zipcodeS);
                contact.setText(contactS);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("TAG","value not fetched"+databaseError.getMessage());
            }
        });

    }


    public void onConfirmOrder(View view) {

        //getting reference to the UserTable of the database
        mDatabase = FirebaseDatabase.getInstance().getReference("OrderTable");
        myRef = mDatabase.child(KEY);
        myRef.child("Name").setValue(nameS);
        myRef.child("ProductID").setValue("1");
        myRef.child("Quantity").setValue(quantityS);
        myRef.child("Price").setValue(totalCost);
        myRef.child("Timing").setValue(slotS);

        startActivity(new Intent(OrderDetails.this,OrderActivity.class));
    }
}
