package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.Loader.AppLoader;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by emg on 09/04/2014.
 */
public class AppListFragment extends Fragment implements LoaderManager.LoaderCallbacks<List<AppInfo>>, AdapterView.OnItemClickListener {

    private static final String TAG = "AppListFragment";
    static List<String> favorites = Arrays.asList("Play Movies & TV", "Netflix", "Plex", "YouTube", "Chrome");

    private ListView mAppsInfoListView;
    TabHost mTabHost;

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


        mTabHost=(TabHost)v.findViewById(R.id.tabHost);
        mTabHost.setup();

        TabHost.TabSpec spec1=mTabHost.newTabSpec("APPS");
        spec1.setContent(R.id.tab1);
        spec1.setIndicator("APPS");


        TabHost.TabSpec spec2=mTabHost.newTabSpec("BOOKMARKS");
        spec2.setIndicator("BOOKMARKS");
        spec2.setContent(R.id.tab2);

        getFragmentManager().beginTransaction().replace(R.id.tab2, new MenuBookMarkFragment()).commit();


        TabHost.TabSpec spec3=mTabHost.newTabSpec("TAB 3");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator("TAB 3");
        mTabHost.addTab(spec1);
        mTabHost.addTab(spec2);
        mTabHost.addTab(spec3);

        return v;

    }





    @Override
    public void onItemClick(AdapterView adapter, View v, int position, long arg3) {

        final AppInfo info = mAppAdapter.getItem(position);

        if (mAppAdapter.getModeCheckBoxSelection()) {
            if (info.checked) {
                assert (getActivity()) != null;
                ((LauncherActivity) getActivity()).removeShortcut(info);
                info.checked = false;
            } else {
                assert (getActivity()) != null;
                ((LauncherActivity) getActivity()).addShortcut(info);
                info.checked = true;
            }
        } else {

            startActivity(info.getIntent());
        }
    }


    /*@Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("Key Pressed", KeyEvent.keyCodeToString(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAPTIONS:
                //toggleDrawer(appLayout);
                return true;
            case KeyEvent.KEYCODE_SETTINGS:
                //toggleDrawer(optionLayout);
                return true;
            case KeyEvent.KEYCODE_TV:
                Intent i;
                //PackageManager manager = getPackageManager();
                *//*try {
                    i = manager.getLaunchIntentForPackage("com.amlogic.DTVPlayer");
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (NullPointerException e) {
                    Log.d("Key Shortcut","App not foumd");
                }*//*
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }*/


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



    public boolean onKeyRight(){
        Log.d(TAG, "Recibido: ON_KEY_RIGHT" );
        if (mAppsInfoListView.hasFocus()){
            Log.d(TAG, "Estamos en app_grid" );
            mAppAdapter.setSelectedCheckBox(true);
            mAppAdapter.setSelectedItem(mAppsInfoListView.getSelectedItemPosition());
            updateView();
            //mAppAdapter.notifyDataSetChanged();
        }
        return false;
    }

    public boolean onKeyUpDown(){
        Log.d(TAG, "Recibido: ON_KEY_DOWN" );

        if (mAppsInfoListView.hasFocus()) {
            mAppAdapter.setSelectedItem(mAppsInfoListView.getSelectedItemPosition());
            updateView();
            return true;
        }
        //mAppAdapter.notifyDataSetChanged();
        return false;
    }

    public boolean onKeyLeft() {
        Log.d(TAG, "Recibido: ON_KEY_LEFT");
        if (mAppsInfoListView.hasFocus()) {
            Log.d(TAG, "Estamos en app_grid");
            if (mAppAdapter.getModeCheckBoxSelection()) {
                mAppAdapter.setSelectedCheckBox(false);
                mAppAdapter.setSelectedItem(mAppsInfoListView.getSelectedItemPosition());
                updateView();
                //mAppAdapter.notifyDataSetChanged();
                return true;
            } else {
                return false;
            }
        } else if (mTabHost.hasFocus()) {
            if (mTabHost.getCurrentTab() != 0) {
                mTabHost.setCurrentTab(mTabHost.getCurrentTab() - 1);
                return true;
            } else {
                if (mAppAdapter.getModeCheckBoxSelection()) {
                    mAppAdapter.setSelectedCheckBox(false);
                    mAppAdapter.setSelectedItem(mAppsInfoListView.getSelectedItemPosition());
                    updateView();
                    //mAppAdapter.notifyDataSetChanged();
                }
                return false;
            }
        }
        return false;
    }
    /**
     * Funcion para actualizar el checkBox del ListItem en caso que esté el modo selección
     */
    private void updateView() {

        //Guardamos en el adapter la seleccion actual
        mAppAdapter.setSelectedItem(mAppsInfoListView.getSelectedItemPosition());

        //Colocamos el listItem anterior como estaba
        View viewOld = mAppsInfoListView.getChildAt(mAppAdapter.getLastSelectedItem()- mAppsInfoListView.getFirstVisiblePosition());
        FrameLayout frameOld = (FrameLayout) viewOld.findViewById(R.id.frame_checkbox);
        frameOld.setBackgroundColor(mAppAdapter.getFrameCheckBoxView(mAppInfosList.get(mAppAdapter.getLastSelectedItem()).checked, false));

        //En caso que dejemos el modo seleccion colocamos el check como estaba
        if (!mAppAdapter.getModeCheckBoxSelection()){
            View v = mAppsInfoListView.getChildAt(mAppsInfoListView.getSelectedItemPosition() - mAppsInfoListView.getFirstVisiblePosition());
            FrameLayout frame = (FrameLayout) v.findViewById(R.id.frame_checkbox);
            frame.setBackgroundColor(mAppAdapter.getFrameCheckBoxView(mAppInfosList.get(mAppAdapter.getSelectedItem()).checked, false));
            return;
        }

        //Colocamos el checkbox actual con el aspecto seleccionado
        View v = mAppsInfoListView.getChildAt(mAppsInfoListView.getSelectedItemPosition() - mAppsInfoListView.getFirstVisiblePosition());

        if (v == null)
            return;

        FrameLayout frame = (FrameLayout) v.findViewById(R.id.frame_checkbox);
        frame.setBackgroundColor(mAppAdapter.getFrameCheckBoxView(false, true));
    }




}
