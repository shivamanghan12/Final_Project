package com.android.newsfeedapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.newsfeedapp.fragment.NewsFeedFragment;
import com.android.newsfeedapp.fragment.SavedNewsFeedFragment;

public class NewsFeedPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public NewsFeedPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewsFeedFragment newsFeedFragment = new NewsFeedFragment();
                return newsFeedFragment;
            case 1:
                SavedNewsFeedFragment savedArticlesFragment = new SavedNewsFeedFragment();
                return savedArticlesFragment;
            default:
                return null;
        }
    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}