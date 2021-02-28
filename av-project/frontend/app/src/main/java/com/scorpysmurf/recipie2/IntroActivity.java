package com.scorpysmurf.recipie2;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.viewpager2.widget.ViewPager2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.android.material.button.MaterialButton;
import com.scorpysmurf.recipie2.onboarding.OnboardingAdapter;
import com.scorpysmurf.recipie2.onboarding.OnboardingItem;

import java.util.ArrayList;
import java.util.List;

public class IntroActivity extends AppCompatActivity {

    ImageView splashImg;
    TextView appName;
    LottieAnimationView logo;
    private OnboardingAdapter onboardingAdapter;
    private LinearLayout indicatorsLayout;
    private MaterialButton buttonOnboardingAction;

    private static int SPLASH_TIME_OUT = 6250;
    SharedPreferences sharedPreferences;
    boolean isFirstTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_intro);

        splashImg = findViewById(R.id.bg);
        appName = findViewById(R.id.app_name);
        logo = findViewById(R.id.logo);
        ViewPager2 onBoardingViewPager = findViewById(R.id.onBoardingViewPager);
        indicatorsLayout = findViewById(R.id.onBoardingIndicators);
        buttonOnboardingAction = findViewById(R.id.onBoardingAction);

        sharedPreferences = getSharedPreferences("SharedPref",MODE_PRIVATE);
        isFirstTime = sharedPreferences.getBoolean("firstTime",true);

        setupOnboardingItems();
        onBoardingViewPager.setAdapter(onboardingAdapter);

        setupOnboardingIndicators();
        setupCurrentOnBoardingIndicator(0);

        onBoardingViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                setupCurrentOnBoardingIndicator(position);
            }
        });

        buttonOnboardingAction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(onBoardingViewPager.getCurrentItem() + 1 < onboardingAdapter.getItemCount()) {
                    onBoardingViewPager.setCurrentItem(onBoardingViewPager.getCurrentItem() + 1);
                } else {
                    startActivity(new Intent(getApplicationContext(),LoginSignupActivity.class));
                    finish();
                }
            }
        });

        splashImg.animate().translationY(-2500).setDuration(1000).setStartDelay(5000);
        appName.animate().translationY(1600).setDuration(1000).setStartDelay(5000);
        logo.animate().translationY(1600).setDuration(1000).setStartDelay(5000);

        if (!isFirstTime) {
            buttonOnboardingAction.setAlpha(0);
            onBoardingViewPager.setAlpha(0);
            indicatorsLayout.setAlpha(0);

        } else {
            onBoardingViewPager.animate().translationYBy(100).setDuration(0).setStartDelay(0);
            onBoardingViewPager.animate().translationYBy(-100).alpha(1).setDuration(1500).setStartDelay(5000);
            logo.animate().alpha(0).setDuration(1500).setStartDelay(6000);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if(isFirstTime) {
                    sharedPreferences.edit().putBoolean("firstTime",false).apply();
                } else {
                    Intent intent = new Intent(IntroActivity.this,LoginSignupActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        },SPLASH_TIME_OUT);
    }

    private void setupOnboardingItems () {
        List<OnboardingItem> onboardingItems = new ArrayList<>();

        OnboardingItem OBItem1 = new OnboardingItem();
        OBItem1.setTitle(IntroActivity.this.getString(R.string.obOneTitle));
        OBItem1.setDescription(IntroActivity.this.getString(R.string.obOneDescription));
        OBItem1.setImage(R.raw.ob1);

        OnboardingItem OBItem2 = new OnboardingItem();
        OBItem2.setTitle(IntroActivity.this.getString(R.string.obTwoTitle));
        OBItem2.setDescription(IntroActivity.this.getString(R.string.obTwoDescription));
        OBItem2.setImage(R.raw.ob2);

        OnboardingItem OBItem3 = new OnboardingItem();
        OBItem3.setTitle(IntroActivity.this.getString(R.string.obThreeTitle));
        OBItem3.setDescription(IntroActivity.this.getString(R.string.obThreeDescription));
        OBItem3.setImage(R.raw.ob3);

        onboardingItems.add(OBItem1);
        onboardingItems.add(OBItem2);
        onboardingItems.add(OBItem3);

        onboardingAdapter = new OnboardingAdapter(onboardingItems);
    }

    private void setupOnboardingIndicators() {
        ImageView[] indicators = new ImageView[onboardingAdapter.getItemCount()];
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT
        );
        layoutParams.setMargins(8,0,8,0);

        for (int i = 0;i<indicators.length;i++) {
            indicators[i] = new ImageView(getApplicationContext());
            indicators[i].setImageDrawable(ContextCompat.getDrawable(
                    getApplicationContext(),
                    R.drawable.onboarding_indicator_inactive
            ));
            indicators[i].setLayoutParams(layoutParams);
            indicatorsLayout.addView(indicators[i]);
        }
    }

    private void setupCurrentOnBoardingIndicator(int index) {
        int childcount = indicatorsLayout.getChildCount();

        for(int i=0;i<childcount;i++) {
            ImageView imageView = (ImageView)indicatorsLayout.getChildAt(i);

            if(i == index) {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(), R.drawable.onboarding_indicator_active
                ));
            } else {
                imageView.setImageDrawable(
                        ContextCompat.getDrawable(getApplicationContext(),R.drawable.onboarding_indicator_inactive)
                );
            }
        }

        if(index == onboardingAdapter.getItemCount()-1) {
            buttonOnboardingAction.setText(IntroActivity.this.getString(R.string.start));
        } else {
            buttonOnboardingAction.setText(IntroActivity.this.getString(R.string.next));
        }
    }
}