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
import android.widget.TabHost;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.Loader.AppLoader;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.BasicImgText;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppCheckBoxAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.BasicITAdapter;

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
    TabHost tabHost;

    private Callbacks mCallbacks = sDummyCallbacks;

    private List<AppInfo> mAppInfosList;

    private ListView mMenuList;
    private AppAdapter mAppAdapter;
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
//        mAppsChecboxListView = (ListView) v.findViewById(R.id.app_checkboxes);
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
//            }
//        });

        mAppsInfoListView = (ListView) v.findViewById(R.id.app_grid);
        mAppsInfoListView.setOnItemClickListener(this);

        tabHost=(TabHost)v.findViewById(R.id.tabHost);
        tabHost.setup();

        TabHost.TabSpec spec1=tabHost.newTabSpec("APPS");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("APPS");


        TabHost.TabSpec spec2=tabHost.newTabSpec("BOOKMARKS");
        spec2.setIndicator("BOOKMARKS");
        spec2.setContent(R.id.tab2);

        getFragmentManager().beginTransaction().replace(R.id.tab2, new MenuBookMarkFragment()).commit();


        TabHost.TabSpec spec3=tabHost.newTabSpec("TAB 3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("TAB 3");
        tabHost.addTab(spec1);
        tabHost.addTab(spec2);
        tabHost.addTab(spec3);

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

        //Creamos el listener para el checkbox de dentro del item
        mAppAdapter.setOnCheckBoxClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FrameLayout f =(FrameLayout) v.findViewById(R.id.frame_checkbox);
                Log.i("Onclicklistener", "OncheckboxClickListener posicion: " + v.getId());
                //La posicion está en el id del view
                AppInfo info = mAppInfosList.get(v.getId());

                if (info.checked) {

                    assert (getActivity()) != null;
                    ((LauncherActivity) getActivity()).addShortcut(info);
                }
                else
                {
                    assert (getActivity()) != null;
                    ((LauncherActivity) getActivity()).removeShortcut(info);
                }
            }
        });

        //Recorremos la lista de aplicaciones en preferencias y añadimos.
        assert (getActivity()) != null;
        ArrayList<String> listaApps = ((LauncherActivity)getActivity()).getAppsNamePreferences();

        for (int i = 0; i< listaApps.size(); i++) {
            String nombreApp = listaApps.get(i);
            for (int j = 0; j < mAppInfosList.size(); j++) {
                AppInfo appInfoTemp =  mAppInfosList.get(j);
                if (SaveLoadAppsPreferences.ComparaNombreAppInfo(appInfoTemp, nombreApp)){
                    assert (getActivity()) != null;
                    ((LauncherActivity)getActivity()).addShortcut(appInfoTemp);
                    appInfoTemp.checked = true;
                    break;
                }
            }
        }

        assert (getActivity()) != null;
        ((LauncherActivity)getActivity()).actualizaArrayAppsPreferencias(mAppInfosList);



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
