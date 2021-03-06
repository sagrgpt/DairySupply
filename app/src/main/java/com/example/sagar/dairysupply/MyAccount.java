package com.example.sagar.dairysupply;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;

public class MyAccount extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthStateListener;
    //Database references
    private DatabaseReference mDatabase;
    private DatabaseReference myRef;
    //Layout references
    private TextView username, contact, address, zipcode;

    private String KEY;
    private SharedPreferences sharedPreferences;

    //Toolbar and navigation drawer
    private Toolbar mToolbar;
    private DrawerLayout drawerLayout;
    private ActionBarDrawerToggle mToggle;
    private NavigationView leftNavDrawer;
    //Profile image
    private CircleImageView mIvProfileImage;
    private GoogleApiClient mGoogleApiClient;


//    @Override
//    protected void onStart() {
//        super.onStart();
//        mAuth.addAuthStateListener(mAuthStateListener);
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_account);

        //Getting user key and username
        sharedPreferences = getSharedPreferences("Settings", Context.MODE_PRIVATE);
        KEY = sharedPreferences.getString("KEY", null);
        String image_url = sharedPreferences.getString("IMAGE_URL",null);
        String uName = sharedPreferences.getString("NAME",null);

        //Toolbar and navigation drawer
        mToolbar = (Toolbar) findViewById(R.id.appbar);
        setSupportActionBar(mToolbar);
        setTitle("My Account");
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        mToggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        leftNavDrawer = (NavigationView) findViewById(R.id.leftNavDrawer);
        //Accessing the navigation view header
        View hView = leftNavDrawer.getHeaderView(0);
        mIvProfileImage = (CircleImageView) hView.findViewById(R.id.profile_image);
        Picasso.with(getApplicationContext()).load(image_url)//download URL
                .placeholder(R.drawable.default_user_profile)//use default image
                .error(R.drawable.default_user_profile)//if failed
                .into(mIvProfileImage);
        TextView nav_username = (TextView) hView.findViewById(R.id.nav_userName);
        nav_username.setText(uName);
        leftNavDrawer.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                startActivity(new Navigation4navDrawer().navAction(item, MyAccount.this));
                return false;
            }
        });

        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        //Building a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(MyAccount.this,"Connection Failed!!",Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .build();


        //Initiating progress dialog display
        final ProgressDialog progressDialog = new ProgressDialog(MyAccount.this);
        progressDialog.setTitle("Loading");
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Please Wait");
        progressDialog.show();

        //Profile image
        mIvProfileImage = (CircleImageView) findViewById(R.id.profile_image);
        Picasso.with(getApplicationContext()).load(image_url)//download URL
                .placeholder(R.drawable.default_user_profile)//use defaul image
                .error(R.drawable.default_user_profile)//if failed
                .into(mIvProfileImage);

        //Referencing layout variables
        contact = (TextView) findViewById(R.id.contact);
        address = (TextView) findViewById(R.id.address);
        zipcode = (TextView) findViewById(R.id.zipcode);
        username = (TextView) findViewById(R.id.username);
        //Changing font for username
        username.setTypeface(Typeface.createFromAsset(getAssets(),"fonts/IndieFlower.ttf"));

        //Connecting to userTable
        mDatabase = FirebaseDatabase.getInstance().getReference("UserTable");
        myRef = mDatabase.child(KEY);
        myRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                Map<String, String> map = (Map) dataSnapshot.getValue();
                username.setText(map.get("Name"));
                contact.setText(map.get("Contact"));
                address.setText(map.get("Location"));
                zipcode.setText(map.get("zipcode"));
                progressDialog.dismiss();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Log.v("TAG", "value not fetched" + databaseError.getMessage());
                onBackPressed();
            }
        });

//
//        //User Session changes
        mAuth = FirebaseAuth.getInstance();
//        mAuthStateListener = new FirebaseAuth.AuthStateListener() {
//            @Override
//            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
//                if (firebaseAuth.getCurrentUser() == null) {
////                    startActivity(new Intent(MyAccount.this, SignInActivity.class));
//                }
//            }
//        };

    }


    public void onLogout(View view) {
        //Firebase SignOut
        mAuth.signOut();
        // Google sign out
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(new ResultCallback<Status>() {
            @Override
            public void onResult(@NonNull Status status) {
                startActivity(new Intent(MyAccount.this, userCheckActivity.class));
            }
        });
//        FirebaseAuth.getInstance().signOut();
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}