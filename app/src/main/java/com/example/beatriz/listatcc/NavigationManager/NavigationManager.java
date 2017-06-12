package com.example.beatriz.listatcc.NavigationManager;

import android.support.v4.app.FragmentManager;

import com.example.beatriz.listatcc.MainMenuFragment;
import com.example.beatriz.listatcc.R;

/**
 * Created by moblee on 6/5/17.
 */

public class NavigationManager {

    public static void openHome(FragmentManager fragmentManager){
        fragmentManager.popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
        fragmentManager
                .beginTransaction()
                .replace(R.id.content_frame, new MainMenuFragment())
                .commit();
    }
}
