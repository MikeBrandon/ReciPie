package com.scorpysmurf.recipie2.loginsignup;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

public class LoginAdapter extends FragmentPagerAdapter {

    private Context context;
    int totalTabs;

    public LoginAdapter(@NonNull FragmentManager fm, Context context, int totalTabs) {
        super(fm);
        this.context = context;
        this.totalTabs = totalTabs;
    }

    @Override
    public int getCount() {
        return totalTabs;
    }

    @NonNull
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new LoginTabFragment();
            case 1:
                return new SignupTabFragment();
            default:
                return new Fragment();
        }
    }
}
