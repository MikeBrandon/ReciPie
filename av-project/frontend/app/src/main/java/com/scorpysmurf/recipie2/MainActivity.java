package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.scorpysmurf.recipie2.mainactivity.DownloaderFragment;
import com.scorpysmurf.recipie2.mainactivity.MealsFragment;
import com.scorpysmurf.recipie2.mainactivity.RecipesFragment;
import com.scorpysmurf.recipie2.mainactivity.SettingsFragment;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    public static ArrayList<String> urls;
    public static ArrayList<String> urlTitles;
    public static ArrayAdapter titlesArrayAdapter;
    public static ArrayAdapter urlsArrayAdapter;
    ConnectivityBroadcastReceiver connectivityBroadcastReceiver = new ConnectivityBroadcastReceiver();
    FirebaseUser user;
    SharedPreferences sharedPreferences;
    ShapeableImageView profileBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottom_navigation_bar);
        profileBtn = findViewById(R.id.profile_pic);

        user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {
            StorageReference storageReference = FirebaseStorage.getInstance().getReference();
            StorageReference profileRef = storageReference.child("users/" + user.getUid() + "profile.jpg");
            profileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                @Override
                public void onSuccess(Uri uri) {
                    Picasso.get().load(uri).into(profileBtn);
                }
            });
        }

        profileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (FirebaseAuth.getInstance().getCurrentUser() == null) {
                    Toast.makeText(MainActivity.this, getString(R.string.logged_in), Toast.LENGTH_SHORT).show();
                } else {
                    Intent i = new Intent(MainActivity.this, ProfileActivity.class);
                    startActivity(i);
                }

            }
        });

        sharedPreferences = getSharedPreferences("com.scorpysmurf.recipie2",MODE_PRIVATE);

        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.fragment_container,new RecipesFragment())
                .commit();

        chipNavigationBar.setItemSelected(R.id.bottom_nav_recipes,true);
        bottomMenu();
        titlesArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,urlTitles);
        urlsArrayAdapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1,urls);

        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences("com.scorpysmurf.recipie2.mainactivity", Context.MODE_PRIVATE);
        HashSet<String> urlSet = (HashSet<String>) sharedPreferences.getStringSet("urls",null);
        HashSet<String> titleSet = (HashSet<String>) sharedPreferences.getStringSet("titles",null);

        if (urlSet == null) {
            urls = new ArrayList<>();
            urlTitles = new ArrayList<>();
        } else {
            urls = new ArrayList<String>(urlSet);
            urlTitles = new ArrayList<String>(titleSet);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        IntentFilter intentFilter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(connectivityBroadcastReceiver,intentFilter);

    }

    @Override
    protected void onStop() {
        super.onStop();

        unregisterReceiver(connectivityBroadcastReceiver);

    }

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i) {
                    case R.id.bottom_nav_recipes:
                        fragment = new RecipesFragment();
                        profileBtn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bottom_nav_downloader:
                        fragment = new DownloaderFragment();
                        profileBtn.setVisibility(View.INVISIBLE);
                        break;
                    case R.id.bottom_nav_mealPlanner:
                        fragment = new MealsFragment();
                        profileBtn.setVisibility(View.VISIBLE);
                        break;
                    case R.id.bottom_nav_settings:
                        fragment = new SettingsFragment();
                        profileBtn.setVisibility(View.VISIBLE);
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

    }
}