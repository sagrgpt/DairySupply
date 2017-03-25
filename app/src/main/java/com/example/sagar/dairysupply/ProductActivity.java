package com.example.sagar.dairysupply;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProductActivity extends AppCompatActivity {

    //Toolbar and navigation drawer
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView leftNavDrawer;
    private ImageView product1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product);

        //Getting user key and username
        SharedPreferences sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
//        KEY = sharedPreferences.getString("KEY", null);
        String uName = sharedPreferences.getString("NAME",null);
        String image_url = sharedPreferences.getString("IMAGE_URL",null);

        //Toolbar and navigation drawer
        mToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        setTitle("Our Products");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        leftNavDrawer = (NavigationView) findViewById(R.id.leftNavDrawer);
        //Accessing the navigation view header
        View hView = leftNavDrawer.getHeaderView(0);
        //Profile image
        CircleImageView mIvProfileImage;
        mIvProfileImage = (CircleImageView) hView.findViewById(R.id.profile_image);
        Picasso.with(getApplicationContext()).load(image_url)//download URL
                .placeholder(R.drawable.default_user_profile)//use default image
                .error(R.drawable.default_user_profile)//if failed
                .into(mIvProfileImage);
        //Username
        TextView nav_username = (TextView) hView.findViewById(R.id.nav_userName);
        nav_username.setText(uName);
        leftNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                startActivity(new Navigation4navDrawer().navAction(item, ProductActivity.this));
                return false;
            }
        });

        //Product Selection.
        product1 = (ImageView) findViewById(R.id.product1);
        product1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ProductActivity.this,OrderActivity.class);
                String product ="1";
                i.putExtra("ProductID",product);
                startActivity(i);
            }
        });

    }

    @Override
    public void onBackPressed() {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
