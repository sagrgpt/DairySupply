package com.example.sagar.dairysupply;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.sagar.dairysupply.R.id.fab;

public class OrderActivity extends AppCompatActivity {

    TextView quantity;
    FloatingActionButton done;
    ImageButton addQuantity, decreaseQuantity;
    RadioButton slot1, slot2;
    String qty, slot;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order);

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
                Boolean isvalid = validate(v);
                if(isvalid){
                    Intent i = new Intent(OrderActivity.this,DeliveryInfo.class);
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
}
