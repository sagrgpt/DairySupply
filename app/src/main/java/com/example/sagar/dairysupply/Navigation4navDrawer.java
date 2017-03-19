package com.example.sagar.dairysupply;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;



public class Navigation4navDrawer {


    private static FirebaseAuth mAuth;
    private static FirebaseAuth.AuthStateListener mAuthListener;
    private static Intent i=null;

    public Intent navAction(MenuItem item, final Context context){
        switch (item.getItemId()){
            case R.id.nav_account:{
                i = new Intent(context,MyAccount.class);
                return i;

            }
            case R.id.nav_order:{
                i = new Intent(context,OrderActivity.class);
                return i;
            }

//            case R.id.nav_logout: {
//                mAuth = FirebaseAuth.getInstance();
//                mAuthListener = new FirebaseAuth.AuthStateListener() {
//                    @Override
//                    public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                        if (firebaseAuth.getCurrentUser()==null){
//                            i =new Intent(context,SignInActivity.class);
//                        }
//                        else
//                            Toast.makeText(context,"Connection error",Toast.LENGTH_SHORT).show();
//                    }
//                };
//                mAuth.signOut();
//                return i;
//            }
        }
        return (new Intent(context,BrandActivity.class));
    }

}
