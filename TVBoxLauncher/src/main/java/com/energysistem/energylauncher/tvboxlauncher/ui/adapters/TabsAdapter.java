package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.app.Fragment;
import android.app.FragmentManager;
import android.support.v4.view.PagerAdapter;
import android.view.View;
//import android.support.v4.app.FragmentPagerAdapter;

import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.MenuBookMarkFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.OptionsLauncherFragment;

/**
 * Created by cfg on 15/09/2014.
 */
public class TabsAdapter extends PagerAdapter {

/*
    public TabsAdapter(FragmentManager fm) {
        super(fm);
    }*/


    public String getItem(int index) {
        String title=null;
        if(index < 3) {
            switch(index) {
                case 0:
                    title = "Apps";
                    break;
                case 1:
                    title = "Bookmarks";
                    break;
                case 2:
                    title = "Settings";
                    break;

            }
            return title;
        }
        return null;
    }

    public int getCount() {
        return 3;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return false;
    }

}
