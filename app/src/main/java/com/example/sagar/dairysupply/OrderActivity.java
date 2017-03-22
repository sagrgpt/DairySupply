package com.example.sagar.dairysupply;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import static com.example.sagar.dairysupply.R.id.fab;

public class OrderActivity extends AppCompatActivity {

    TextView quantity;
    FloatingActionButton done;
    ImageButton addQuantity, decreaseQuantity;
    RadioButton slot1, slot2;
    String qty, slot;
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView leftNavDrawer;
    private DatabaseReference mDatabase;
    private DatabaseReference mUserRef;
    private static String uName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);
//
//        //Initiating progress dialog display
//        final ProgressDialog progressDialog = new ProgressDialog(OrderActivity.this);
//        progressDialog.setTitle("Loading");
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Please Wait");
//        progressDialog.show();

        //Toolbar and navigation drawer
        mToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this,drawerLayout,R.string.open,R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        leftNavDrawer =(NavigationView) findViewById(R.id.leftNavDrawer);

        //Using sharedPreferences to get the user key
        final SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        String key = sharedPreferences.getString("KEY",null);

        //Accessing database to get username for navigation drawer
        mDatabase = FirebaseDatabase.getInstance().getReference("UserTable");
        mUserRef = mDatabase.child(key).child("Name");
        mUserRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                uName = dataSnapshot.getValue(String.class);
                if(uName!=null) {
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putString("NAME", uName);
                    editor.apply();
                    //Accessing the navigation view header
                    View hView = leftNavDrawer.getHeaderView(0);
                    TextView username = (TextView) hView.findViewById(R.id.nav_userName);
                    username.setText(uName);
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


//        new Thread() {
//            public void run() {
//                try {
//                    sleep(3000);
//                    progressDialog.dismiss();
//                }
//                catch (Exception e) { }
//
//            }
//        }.start();



        leftNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                startActivity(new Navigation4navDrawer().navAction(item,OrderActivity.this));
                return false;
            }
        });


        slot1 = (RadioButton) findViewById(R.id.slot1);
        slot2 = (RadioButton) findViewById(R.id.slot2);

        //Increasing and decreasing function of quantity using the image button
        quantity = (TextView) findViewById(R.id.quantity);
        addQuantity = (ImageButton) findViewById(R.id.increase);
        decreaseQuantity = (ImageButton) findViewById(R.id.decrease);
        addQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt(quantity.getText().toString());
                if(x==10)
                    Toast.makeText(getApplicationContext(),"Invalid Order!!",Toast.LENGTH_SHORT).show();
                else{
                    x = x+1;
                    quantity.setText(String.valueOf(x));
                }
            }
        });

        decreaseQuantity.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int x = Integer.parseInt(quantity.getText().toString());
                if(x==1)
                    Toast.makeText(getApplicationContext(),"Invalid Order!!",Toast.LENGTH_SHORT).show();
                else{
                    x = x-1;
                    quantity.setText(String.valueOf(x));
                }
            }
        });

        //Moving to next activity on clicking of the done button
        done = (FloatingActionButton) findViewById(fab);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //checking if the user entry is valid before moving to the next activity.
                Boolean isValid = validate(v);
                if(isValid){
                    Intent i = new Intent(OrderActivity.this,OrderDetails.class);
                    i.putExtra("Quantity",qty);
                    i.putExtra("Slot",slot);
                    startActivity(i);
                }
            }
        });

    }

    //checking if the user entry is valid.

    private Boolean validate(View v) {
        if(slot1.isChecked()){
            qty = quantity.getText().toString();
            slot = "Morning";
            return true;
        }
        else if(slot2.isChecked()){
            qty = quantity.getText().toString();
            slot = "Evening";
            return true;
        }
        else return false;
    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


}
