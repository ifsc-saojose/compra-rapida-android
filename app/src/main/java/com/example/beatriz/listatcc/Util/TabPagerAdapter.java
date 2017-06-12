package com.example.beatriz.listatcc.Util;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Beatriz on 24/07/2016.
 */
public class TabPagerAdapter extends FragmentStatePagerAdapter {
    private List<Tab> mTabs;

    public TabPagerAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {

        return getTabs().get(position).getFragment();
    }

    @Override
    public int getCount() {

        return getTabs().size();
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return getTabs().get(position).getTitle();
    }

    public List<Tab> getTabs() {
        return mTabs;
    }

    public void setTabs(List<Tab> tabs) {
        this.mTabs = tabs;
    }
}