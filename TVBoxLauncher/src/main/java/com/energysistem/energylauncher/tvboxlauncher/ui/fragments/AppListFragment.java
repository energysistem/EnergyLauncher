package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.Intent;
import android.content.Loader;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.Loader.AppLoader;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppCheckBoxAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.EventListener;
import java.util.List;

/**
 * Created by emg on 09/04/2014.
 */
public class AppListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<AppInfo>>, AdapterView.OnItemClickListener {

    static List<String> favorites = Arrays.asList("Play Movies & TV", "Netflix", "Plex", "YouTube", "Chrome");

    private ListView mAppsInfoListView;
   // private ListView mAppsChecboxListView;

    private Callbacks mCallbacks = sDummyCallbacks;

    private List<AppInfo> mAppInfosList;

    private AppAdapter mAppAdapter;
    //private AppCheckBoxAdapter mCheckBoxAdapter;
    private LinearLayout mFavorites;

    public interface Callbacks {
        void onExpandButtonClick();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override public void onExpandButtonClick() {}
    };

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

        mAppsInfoListView = (ListView) v.findViewById(R.id.app_grid);
        //mAppsChecboxListView = (ListView) v.findViewById(R.id.app_checkboxes);
//        mAppsGrid.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int pos, long l) {
//
//                CheckBox cb = (CheckBox) view.findViewById(R.id.checkBoxApp);
//                TextView tv = (TextView) view.findViewById(R.id.title_app);
//
//
//                final AppInfo info = mAdapter.getItem(pos);
//                startActivity(info.getIntent());
//
////
//            }
//
//        });

        mAppsInfoListView.setOnItemClickListener(this);



        //mFavorites = (LinearLayout) v.findViewById(R.id.favorite_bar);

        return v;
    }





    @Override
    public void onItemClick(AdapterView adapter, View v, int position, long arg3) {
        // TODO Auto-generated method stub
        final AppInfo info = mAppAdapter.getItem(position);
        startActivity(info.getIntent());

    }




    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        if (!(activity instanceof Callbacks)) {
            throw new IllegalArgumentException("Parent activity must implement Callbacks.");
        }
        mCallbacks = (Callbacks) activity;
    }

    @Override
    public void onDetach() {
        mCallbacks = sDummyCallbacks;
        super.onDetach();
    }

    @Override
    public Loader<List<AppInfo>> onCreateLoader(int i, Bundle bundle) {
        return new AppLoader(getActivity(), LauncherAppState.getInstance().getIconCache());
    }

    @Override
    public void onLoadFinished(Loader<List<AppInfo>> listLoader, List<AppInfo> appInfos) {
        //Take out the favorites
        //appInfos = extractFavorites(appInfos);

        mAppInfosList = appInfos;
        mAppAdapter = new AppAdapter(getActivity(), appInfos);
        mAppsInfoListView.setAdapter(mAppAdapter);

        mAppAdapter.setOnCheckBoxClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout f =(FrameLayout) v.findViewById(R.id.frame_checkbox);
                Log.e("Onclicklistener", "OnXXXClickListener "+ v.getId() );

                AppInfo info = mAppInfosList.get(v.getId());
                ((LauncherActivity)getActivity()).addShortcut(info);
            }
        });

    }

    @Override
    public void onLoaderReset(Loader<List<AppInfo>> listLoader) {
        mAppsInfoListView.setAdapter(null);
    }

    private List<AppInfo> extractFavorites(List<AppInfo> infos) {
        List<AppInfo> favs = new ArrayList<AppInfo>(favorites.size());
        for (String name : favorites) {
            for (AppInfo info : infos) {
                if (name.equals(info.title)) {
                    favs.add(info);
                    infos.remove(info);
                    break;
                }
            }
        }

        mFavorites.removeAllViews();
        LayoutInflater inflater = LayoutInflater.from(getActivity());
        for (AppInfo info : favs) {
            View v = inflater.inflate(R.layout.row_app, mFavorites, false);
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.WRAP_CONTENT, 1);
            v.setLayoutParams(params);

            ImageView image = (ImageView) v.findViewById(R.id.image);
            TextView title = (TextView) v.findViewById(R.id.title);
            image.setImageDrawable(new BitmapDrawable(getResources(), info.iconBitmap));
            title.setText(info.title);

            final Intent appIntent = info.getIntent();
            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    startActivity(appIntent);
                }
            });

            mFavorites.addView(v);
        }

        return infos;
    }


    public List<AppInfo> getAppsInfos(){
        if (mAppInfosList != null){
            return mAppInfosList;
        }
        else
        {
            return new ArrayList<AppInfo>(0);
        }
    }

}
