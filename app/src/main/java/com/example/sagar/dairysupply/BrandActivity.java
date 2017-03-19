package com.example.sagar.dairysupply;

import android.app.Activity;
import android.content.Intent;
import android.os.Handler;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.Window;

import com.google.firebase.auth.FirebaseAuth;

public class BrandActivity extends Activity {

    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthStateListener;
    private final int SPLASH_DISPLAY_LENGTH = 3000;

    @Override
    protected void onStart() {
        super.onStart();
        mAuth.addAuthStateListener(mAuthStateListener);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_brand);



//
//        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Intent i = new Intent(BrandActivity.this, SignInActivity.class);
//                startActivity(i);
//            }
//        }, SPLASH_DISPLAY_LENGTH);

        mAuth = FirebaseAuth.getInstance();
        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                if(firebaseAuth.getCurrentUser()!=null){
                    startActivity(new Intent(BrandActivity.this,OrderActivity.class));
                }
                else
                    startActivity(new Intent(BrandActivity.this,SignInActivity.class));
            }
        };





    }


}
