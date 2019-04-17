package com.android.newsfeedapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.newsfeedapp.fragment.NewsFeedFragment;
import com.android.newsfeedapp.fragment.SavedNewsFeedFragment;
import com.android.newsfeedapp.fragment.SavedWebsterFragment;
import com.android.newsfeedapp.fragment.WebsterFragment;

public class WebsterPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public WebsterPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                WebsterFragment websterFragment = new WebsterFragment();
                return websterFragment;
            case 1:
                SavedWebsterFragment savedWebsterFragment = new SavedWebsterFragment();
                return savedWebsterFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}