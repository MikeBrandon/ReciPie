package com.scorpysmurf.recipie2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.viewpager.widget.ViewPager;

import android.app.ActivityOptions;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.scorpysmurf.recipie2.loginsignup.LoginAdapter;

public class LoginSignupActivity extends AppCompatActivity {

    TabLayout tabLayout;
    ViewPager viewPager;
    Button skipButton;
    float v=0;
    ConstraintLayout constraintLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login_signup);

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null) {

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(intent);
            finish();

        } else {

            tabLayout = findViewById(R.id.tab_layout);
            viewPager = findViewById(R.id.view_pager);
            skipButton = findViewById(R.id.skip_button);
            constraintLayout = findViewById(R.id.loginsignup_bg);

            constraintLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    InputMethodManager inputMethodManager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    inputMethodManager.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(),0);
                }
            });

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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                    //Add Transition
                    Pair[] pairs = new Pair[1];
                    pairs[0] = new Pair<View,String>(skipButton,"transition_skip_button");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(LoginSignupActivity.this,pairs);

                    startActivity(intent,options.toBundle());
                    finish();
                }
            });

            skipButton.setTranslationY(300);
            skipButton.setAlpha(v);
            skipButton.animate().translationY(0).alpha(1).setDuration(1000).setStartDelay(400).start();

            tabLayout.setAlpha(v);
            tabLayout.animate().alpha(1).setDuration(1000).setStartDelay(150).start();

        }
    }
}