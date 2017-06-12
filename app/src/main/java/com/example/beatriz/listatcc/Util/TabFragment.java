package com.example.beatriz.listatcc.Util;


import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.beatriz.listatcc.R;

import java.util.ArrayList;

/**
 * Created by Beatriz on 24/07/2016.
 */
public class TabFragment extends Fragment {

    private ArrayList<Tab> mTabs;
    Context mContext;
    protected View view;
    protected ViewPager viewPager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.tab_layout, container, false);
        setHasOptionsMenu(true);
        configureTabs(view);
        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.mContext = context;
    }

    public void configureTabs(View view) {
        final TabPagerAdapter tabPagerAdapter = new TabPagerAdapter(getChildFragmentManager());
        tabPagerAdapter.setTabs(getTabs());

        viewPager = (ViewPager) view.findViewById(R.id.view_pager);
        viewPager.setAdapter(tabPagerAdapter);

        final TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        if (mTabs.size() <= 1) {
            tabLayout.setVisibility(View.GONE);
        }

        if (mTabs.size() > 2)
            tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);

        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {

            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    public ArrayList<Tab> getTabs() {
        return mTabs;
    }

    public void setTabs(ArrayList<Tab> mTabs) {
        this.mTabs = mTabs;
    }

    @Override
    public void onPause() {
        super.onPause();
    }
}
