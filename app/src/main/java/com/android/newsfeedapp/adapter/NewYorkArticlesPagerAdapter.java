package com.android.newsfeedapp.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.android.newsfeedapp.fragment.NewYorkArticlesFragment;
import com.android.newsfeedapp.fragment.SavedNewYorkArticlesFragment;
import com.android.newsfeedapp.fragment.SavedNewsFeedFragment;

public class NewYorkArticlesPagerAdapter extends FragmentStatePagerAdapter {
    int mNumOfTabs;

    public NewYorkArticlesPagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        switch (position) {
            case 0:
                NewYorkArticlesFragment newYorkArticlesFragment = new NewYorkArticlesFragment();
                return newYorkArticlesFragment;
            case 1:
                SavedNewYorkArticlesFragment savedArticlesFragment = new SavedNewYorkArticlesFragment();
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