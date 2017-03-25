package com.example.sagar.dairysupply;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;

public class OrderDetails extends AppCompatActivity {

    int qty;
    double price;
    double totalCost;
    private TextView username, quantity, address, contact, slot, cost;


    //Database Variables
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    private String KEY;

    //User data variables
    private String nameS, contactS, locationS, zipcodeS,quantityS, productid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

//Initiating progress dialog display
        final ProgressDialog progressDialog = new ProgressDialog(OrderDetails.this);
        progressDialog.setTitle("Loading");
        progressDialog.setInverseBackgroundForced(true);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        //Getting user key
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        KEY = sharedPreferences.getString("KEY",null);

//      Adding toolbar to the layout
        Toolbar mToolbar =(Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        setTitle("Your Order!");

        username = (TextView) findViewById(R.id.username);
        //Changing font for username
        username.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/IndieFlower.ttf"));
        quantity = (TextView) findViewById(R.id.quantity);
        address = (TextView) findViewById(R.id.address);
        contact = (TextView) findViewById(R.id.phone_number);
        slot = (TextView) findViewById(R.id.timingSlot);
        cost = (TextView) findViewById(R.id.cost);
        //Getting info from previous activity
        final Bundle data = getIntent().getExtras();
        productid = data.getString("ProductID");
        quantityS = data.getString("Quantity");
        quantity.setText(quantityS);
        slot.setText(R.string.timing_note);
        qty = Integer.parseInt(quantity.getText().toString());
        price=data.getDouble("Cost");
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
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("TAG","value not fetched"+databaseError.getMessage());
            }
        });

        Button onConfirmOrder = (Button) findViewById(R.id.onConfirmOrder);
        onConfirmOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                //getting reference to the UserTable of the database
                mDatabase = FirebaseDatabase.getInstance().getReference("OrderTable");
                myRef = mDatabase.child(KEY);
                myRef.child("Name").setValue(nameS);
                myRef.child("ProductID").setValue(productid);
                myRef.child("Quantity").setValue(quantityS);
                myRef.child("Price").setValue(totalCost);
                Calendar calendar = Calendar.getInstance();
                myRef.child("Timing").setValue(calendar.get(Calendar.HOUR_OF_DAY)+":"+calendar.get(Calendar.MINUTE));

//        startActivity(new Intent(OrderDetails.this,ProductActivity.class));

                //Alert dialog as confirmation to order placed
                AlertDialog.Builder builder = new AlertDialog.Builder(OrderDetails.this);
                builder.setTitle("Confirmation")
                        .setMessage("Your order has been confir")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                startActivity(new Intent(OrderDetails.this,ProductActivity.class));

                            }
                        });
                        builder.show();
            }
        });

    }


//    public void onConfirmOrder(View view) {
//
//        //getting reference to the UserTable of the database
//        mDatabase = FirebaseDatabase.getInstance().getReference("OrderTable");
//        myRef = mDatabase.child(KEY);
//        myRef.child("Name").setValue(nameS);
//        myRef.child("ProductID").setValue(productid);
//        myRef.child("Quantity").setValue(quantityS);
//        myRef.child("Price").setValue(totalCost);
//        Calendar calendar = Calendar.getInstance();
//        myRef.child("Timing").setValue(calendar.get(Calendar.HOUR_OF_DAY));
//
////        startActivity(new Intent(OrderDetails.this,ProductActivity.class));
//
//        //Alert dialog as confirmation to order placed
//        AlertDialog.Builder builder = new AlertDialog.Builder(getApplicationContext());
//        builder.setTitle("Confirmation")
//                .setMessage("Your order has been confir")
//                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                        startActivity(new Intent(OrderDetails.this,ProductActivity.class));
//
//                    }
//                })
//                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//
//                    }
//                });
//        AlertDialog dialog = builder.create();
//        dialog.show();
//    }
}
