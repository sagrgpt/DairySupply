package com.example.sagar.dairysupply;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;
public class OrderDetails extends AppCompatActivity {

    int qty;
    double price=20.00;
    double totalCost;
    TextView quantity, address, contact, slot, cost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_order_details);

        quantity = (TextView) findViewById(R.id.quantity);
        address = (TextView) findViewById(R.id.address);
//        contact = (TextView) findViewById(R.id.contact);
        slot = (TextView) findViewById(R.id.timingSlot);
        cost = (TextView) findViewById(R.id.cost);
        Bundle data = getIntent().getExtras();
        quantity.setText(data.getString("Quantity"));
        address.setText(data.getString("Address")+", "+data.getString("LandMark"));
        slot.setText(data.getString("Slot"));
        qty = Integer.parseInt(quantity.getText().toString());
        totalCost = qty*price;
        cost.setText(String.valueOf(totalCost));

    }
}