package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.media.Image;
import android.os.Bundle;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.ImageView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.scorpysmurf.recipie2.onboarding.OnboardingAdapter;
import com.scorpysmurf.recipie2.onboarding.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    ImageView splashImg;
    TextView appName;
    LottieAnimationView logo;
    private OnboardingAdapter onboardingAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        splashImg = findViewById(R.id.bg);
        appName = findViewById(R.id.app_name);
        logo = findViewById(R.id.logo);
        ViewPager2 onBoardingViewPager = findViewById(R.id.onBoardingViewPager);

        setupOnboardingItems();
        onBoardingViewPager.setAdapter(onboardingAdapter);

        splashImg.animate().translationY(-2500).setDuration(1000).setStartDelay(5000);
        appName.animate().translationY(1600).setDuration(1000).setStartDelay(5000);
        logo.animate().translationY(1600).setDuration(1000).setStartDelay(5000);

        onBoardingViewPager.animate().translationYBy(100).setDuration(0).setStartDelay(0);
        onBoardingViewPager.animate().translationYBy(-100).setDuration(1500).setStartDelay(5000);
        onBoardingViewPager.animate().alpha(1).setDuration(500).setStartDelay(5500);
    }

    private void setupOnboardingItems () {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem OBItem1 = new OnboardingItem();
        OBItem1.setTitle(IntroActivity.this.getString(R.string.obOneTitle));
        OBItem1.setDescription(IntroActivity.this.getString(R.string.obOneDescription));
        OBItem1.setImage(R.raw.final_logo);

        OnboardingItem OBItem2 = new OnboardingItem();
        OBItem2.setTitle(IntroActivity.this.getString(R.string.obTwoTitle));
        OBItem2.setDescription(IntroActivity.this.getString(R.string.obTwoDescription));
        OBItem2.setImage(R.raw.food_anim);

        OnboardingItem OBItem3 = new OnboardingItem();
        OBItem3.setTitle(IntroActivity.this.getString(R.string.obThreeTitle));
        OBItem3.setDescription(IntroActivity.this.getString(R.string.obThreeDescription));
        OBItem3.setImage(R.raw.hotel_food);

        onboardingItems.add(OBItem1);
        onboardingItems.add(OBItem2);
        onboardingItems.add(OBItem3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }
}