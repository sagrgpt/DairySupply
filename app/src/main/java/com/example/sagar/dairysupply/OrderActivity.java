package com.example.sagar.dairysupply;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Map;

import static com.example.sagar.dairysupply.R.id.fab;

public class OrderActivity extends AppCompatActivity {

    Spinner quantity;
    FloatingActionButton done;
    TextView description;
    double cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);


        //Toolbar
        Toolbar mToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        setTitle("Order");

        //Getting info from previous activity
        final Bundle data = getIntent().getExtras();
//        Toast.makeText(OrderActivity.this,,Toast.LENGTH_SHORT).show();
        String productId = data.getString("ProductID");
        description = (TextView) findViewById(R.id.productDescription);
        //Connecting to database for productInfo
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("ProductTable");
        final DatabaseReference productRef = mDatabase.child(productId);
        productRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Map<String, String> map = (Map) dataSnapshot.getValue();
                description.setText(map.get("Description"));
                cost = Double.parseDouble(map.get("Cost"));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        quantity = (Spinner) findViewById(R.id.quantity);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.quantity, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        quantity.setAdapter(adapter);

        //Moving to next activity on clicking of the done button
        done = (FloatingActionButton) findViewById(fab);
        done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                checking if the user entry is valid before moving to the next activity.
                if(validate()){
                    Intent i = new Intent(OrderActivity.this,OrderDetails.class);
                    i.putExtra("Quantity",quantity.getSelectedItem().toString());
                    i.putExtra("Cost",cost);
                    i.putExtra("ProductID","1");
                    startActivity(i);
                }
                else{
                    Toast.makeText(OrderActivity.this,"Invalid Quantity",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

//    checking if the user entry is valid.

    private Boolean validate() {
//        if(slot1.isChecked()){
//            qty = quantity.getText().toString();
//            slot = "Morning";
//            return true;
//        }
//        else if(slot2.isChecked()){
//            qty = quantity.getText().toString();
//            slot = "Evening";
//            return true;
//        }
        if(quantity.getSelectedItem().toString().equals("Quantity in Litres"))
            return false;
        else
            return true;
    }

}
