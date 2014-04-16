package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.BasicImgText;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.BasicITAdapter;

/**
 * Created by vgt on 11/04/2014.
 */
public class MenuListFragmernt extends Fragment {

    private ListView mMenuList;
    private String[] sMnuList = {"App Favorite","Books Marks", "Settings"};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_list, container, false);

        mMenuList = (ListView) view.findViewById(R.id.menu_list);
        BasicImgText[] drawerItem = new BasicImgText[3];
        drawerItem[0] = new BasicImgText(R.drawable.ereader, "App Shortcut");
        drawerItem[1] = new BasicImgText(R.drawable.browser, "BookMarks");
        drawerItem[2] = new BasicImgText(R.drawable.settings, "Settings");
        mMenuList.setOnItemClickListener(new DrawerItemClickListener());


        BasicITAdapter adapter = new BasicITAdapter(view.getContext(), R.layout.row_menu, drawerItem);
        mMenuList.setAdapter(adapter);


        return view;
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
                    //getFragmentManager().beginTransaction().replace(R.id.left_drawer, new SelectedAppsListFragment()).commit();
                    break;
                case 1:
                    //getFragmentManager().beginTransaction().replace(R.id.left_drawer, new MenuBookMarkFragment())
                    //        .setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN).addToBackStack(null).commit();
                    getFragmentManager().beginTransaction().replace(R.id.left_drawer, new MenuBookMarkFragment()).commit();
                    break;
                case 2:
                    startActivity(new Intent(Settings.ACTION_SETTINGS));
                    break;

                default:
                    break;
            }
        }
    }
}
