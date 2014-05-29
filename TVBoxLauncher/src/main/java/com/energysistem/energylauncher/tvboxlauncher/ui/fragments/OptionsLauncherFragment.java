package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.annotation.TargetApi;

import android.app.Fragment;
import android.os.Build;
import android.os.Bundle;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.BasicImgText;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.BasicITAdapter;

/**
 * Created by emg on 22/04/2014.
 */
public class OptionsLauncherFragment extends Fragment {

    private ListView mMenuList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);

        mMenuList = (ListView) view.findViewById(R.id.menu_list);
        BasicImgText[] drawerItem = new BasicImgText[3];
        drawerItem[0] = new BasicImgText(R.drawable.ic_launcher, getResources().getString(R.string.wallpaper));
        drawerItem[1] = new BasicImgText(R.drawable.ic_launcher, getResources().getString(R.string.reordena_button));
        drawerItem[2] = new BasicImgText(R.drawable.ic_launcher, getResources().getString(R.string.back));

        mMenuList.setOnItemClickListener(new DrawerItemClickListener());

        BasicITAdapter adapter = new BasicITAdapter(view.getContext(), R.layout.row_menu, drawerItem);
        mMenuList.setAdapter(adapter);

        mMenuList.requestFocus();

        return view;
    }



    public void setFocus() {
        mMenuList.requestFocus();
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            //getLoaderManager().restartLoader(0, null, this);
        }
    }




    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            selectItem(position);
        }


        @TargetApi(Build.VERSION_CODES.HONEYCOMB)
        private void selectItem(int position) {

            android.app.Fragment fragment = null;

            switch (position) {
                case 0:
                    ((LauncherActivity)getActivity()).ShowPickWallpaperFragment();
                    break;
                case 1:
                    ((LauncherActivity)getActivity()).ShowReordenaDesktopAppsFragment();
                    break;
                default:
                    ((LauncherActivity)getActivity()).onBackPressed();
                    break;
            }
        }
    }


}
