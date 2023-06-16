package com.harismehmood.finalproject.activities.adapters;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.harismehmood.finalproject.activities.common.loginFragment;
import com.harismehmood.finalproject.activities.common.signupFragment;

public class LoginAdapter extends FragmentPagerAdapter {
    public Context context;
    int totalTabs;
    public LoginAdapter(@NonNull FragmentManager fm) {
        super(fm);
    }
@NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0:
                return new loginFragment();
            case 1:
                return new signupFragment();
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }
    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0:
                return "Login";
            case 1:
                return "Signup";
            default:
                return null;
        }
    }
}
