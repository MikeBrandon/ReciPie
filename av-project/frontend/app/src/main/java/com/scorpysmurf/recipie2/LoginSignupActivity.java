package com.scorpysmurf.recipie2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.scorpysmurf.recipie2.loginsignup.LoginAdapter;

public class LoginSignupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Button skipButton;
    float v=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_signup);

        tabLayout = findViewById(R.id.tab_layout);
        viewPager = findViewById(R.id.view_pager);
        skipButton = findViewById(R.id.skip_button);

        tabLayout.addTab(tabLayout.newTab().setText(LoginSignupActivity.this.getString(R.string.login)));
        tabLayout.addTab(tabLayout.newTab().setText(LoginSignupActivity.this.getString(R.string.signup)));
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);

        final LoginAdapter adapter = new LoginAdapter(getSupportFragmentManager(),this,tabLayout.getTabCount());
        viewPager.setAdapter(adapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                viewPager.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        skipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getApplicationContext(), MainActivity.class));
            }
        });

        skipButton.setTranslationY(300);
        skipButton.setAlpha(v);
        skipButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

        tabLayout.setAlpha(v);
        tabLayout.animate().alpha(1).setDuration(1000).setStartDelay(150).start();

    }
}