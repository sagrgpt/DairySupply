package com.example.sagar.dairysupply;

import android.content.Context;
import android.content.Intent;
import android.view.MenuItem;

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
            case R.id.nav_make_order:{
                i = new Intent(context,OrderActivity.class);
                return i;
            }

            case R.id.nav_my_order:{
                i = new Intent(context,OrderActivity.class);
                return i;
            }
            case R.id.nav_about:{
                i = new Intent(context,OrderActivity.class);
                return i;
            }
            case R.id.nav_setting:{
                i = new Intent(context,OrderActivity.class);
                return i;
            }
        }
        //This is a dead code as it is never executed.
        return (new Intent(context,BrandActivity.class));
    }

}
