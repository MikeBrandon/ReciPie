package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

public class IntroActivity extends AppCompatActivity {

    ImageView splashImg;
    TextView appName;
    LottieAnimationView logo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        splashImg = findViewById(R.id.bg);
        appName = findViewById(R.id.app_name);
        logo = findViewById(R.id.logo);

        //logo.setImageAssetsFolder("raw");

        splashImg.animate().translationY(-2500).setDuration(1000).setStartDelay(5000);
        appName.animate().translationY(1600).setDuration(1000).setStartDelay(5000);
        logo.animate().translationY(1600).setDuration(1000).setStartDelay(5000);


    }
}