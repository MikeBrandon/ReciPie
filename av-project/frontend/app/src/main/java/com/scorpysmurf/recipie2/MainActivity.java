package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.WindowManager;

import com.ismaeldivita.chipnavigation.ChipNavigationBar;
import com.scorpysmurf.recipie2.mainactivity.DownloaderFragment;
import com.scorpysmurf.recipie2.mainactivity.MealsFragment;
import com.scorpysmurf.recipie2.mainactivity.RecipesFragment;
import com.scorpysmurf.recipie2.mainactivity.SettingsFragment;

public class MainActivity extends AppCompatActivity {

    ChipNavigationBar chipNavigationBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        chipNavigationBar = findViewById(R.id.bottom_navigation_bar);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,new RecipesFragment()).commit();
        chipNavigationBar.setItemSelected(R.id.bottom_nav_recipes,true);
        bottomMenu();
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