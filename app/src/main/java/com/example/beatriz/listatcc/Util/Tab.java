package com.example.beatriz.listatcc.Util;

import android.support.v4.app.Fragment;

/**
 * Created by Beatriz on 30/07/2016.
 */
public class Tab {
    String title;
    Fragment fragment;

    public Tab(String title, Fragment fragment) {
        this.title = title;
        this.fragment = fragment;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public void setFragment(Fragment fragment) {
        this.fragment = fragment;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
