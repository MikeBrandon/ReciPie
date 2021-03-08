package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.ArrayAdapter;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.scorpysmurf.recipie2.mainactivity.DownloaderFragment;
import com.scorpysmurf.recipie2.mainactivity.MealsFragment;
import com.scorpysmurf.recipie2.mainactivity.RecipesFragment;
import com.scorpysmurf.recipie2.mainactivity.SettingsFragment;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    public static ArrayList<String> urls;
    public static ArrayList<String> urlTitles;
    public static ArrayAdapter titlesArrayAdapter;
    public static ArrayAdapter urlsArrayAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottom_navigation_bar);

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

    private void bottomMenu() {

        chipNavigationBar.setOnItemSelectedListener(new ChipNavigationBar.OnItemSelectedListener() {
            @Override
            public void onItemSelected(int i) {
                Fragment fragment = null;

                switch (i) {
                    case R.id.bottom_nav_recipes:
                        fragment = new RecipesFragment();
                        break;
                    case R.id.bottom_nav_downloader:
                        fragment = new DownloaderFragment();
                        break;
                    case R.id.bottom_nav_mealPlanner:
                        fragment = new MealsFragment();
                        break;
                    case R.id.bottom_nav_settings:
                        fragment = new SettingsFragment();
                        break;
                }

                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,fragment).commit();
            }
        });

    }
}