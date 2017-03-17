package com.example.sagar.dairysupply;

import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class DeliveryInfo extends AppCompatActivity {

    String ERROR="Cannot be left empty";
    String addr1, addr2, addr3, landmark;
    static private TextInputLayout addr1TextInputLayout, addr2TextInputLayout, addr3TextInputLayout,landMarkTextInputLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Your Location!!");
        setContentView(R.layout.activity_delivery_info);
        addr1TextInputLayout = (TextInputLayout) findViewById(R.id.addr1TextInputLayout);
        addr2TextInputLayout = (TextInputLayout) findViewById(R.id.addr2TextInputLayout);
        addr3TextInputLayout = (TextInputLayout) findViewById(R.id.addr3TextInputLayout);
        landMarkTextInputLayout = (TextInputLayout) findViewById(R.id.landMarkTextInputLayout);

    }

    public void onProceed(View view) {

        Boolean isValid = validate();
        if(isValid){
            Intent i = new Intent(DeliveryInfo.this,OrderDetails.class);
            String address = addr1+", "+addr2+", Near "+landmark+", "+addr3;
            i.putExtra("Address",address);
            i.putExtra("LandMark",landmark);
            Bundle data = getIntent().getExtras();
            i.putExtra("Quantity",data.getString("Quantity"));
            i.putExtra("Slot",data.getString("Slot"));
            startActivity(i);
        }

    }

    private Boolean validate() {
        addr1 = addr1TextInputLayout.getEditText().getText().toString();
        addr2 = addr2TextInputLayout.getEditText().getText().toString();
        addr3 = addr3TextInputLayout.getEditText().getText().toString();
        landmark = landMarkTextInputLayout.getEditText().getText().toString();
        if(addr1.length()==0){
            addr1TextInputLayout.setError(ERROR);
            return false;
        }
        else if(addr2.length()==0){
            addr2TextInputLayout.setError(ERROR);
            return false;
        }
        else if(addr3.length()==0){
            addr3TextInputLayout.setError(ERROR);
            return false;
        }
        else if(landmark.length()==0){
            landMarkTextInputLayout.setError(ERROR);
            return false;
        }
        return true;
    }
}
