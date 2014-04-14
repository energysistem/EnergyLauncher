package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppAdapter;

/**
 * Created by emg on 14/04/2014.
 */
public class SelectedAppsListFragment extends Fragment {

    private ListView mAppsList;
    private AppAdapter mAdapter;

    private LinearLayout mFavorites;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_app_list, container, false);

//        mAllAppsButton = (ImageButton) v.findViewById(R.id.expand_button);
//        mAllAppsButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                mCallbacks.onExpandButtonClick();
//            }
//        });

        mAppsList = (ListView) v.findViewById(R.id.app_grid);


        mAppsList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                final AppInfo info = mAdapter.getItem(i);
//              startActivity(info.getIntent());

                ((LauncherActivity)getActivity()).addShortcut(info);
            }
        });

        //mFavorites = (LinearLayout) v.findViewById(R.id.favorite_bar);

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        mAdapter = new AppAdapter(getActivity(), ((LauncherActivity) getActivity()).getAppList(), false);
        mAppsList.setAdapter(mAdapter);

    }
}





