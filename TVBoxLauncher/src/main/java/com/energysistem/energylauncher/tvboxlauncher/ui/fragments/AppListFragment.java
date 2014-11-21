package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Activity;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Intent;
import android.content.Loader;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
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
public class AppListFragment extends Fragment
        implements
        LoaderManager.LoaderCallbacks<List<AppInfo>>,
        AdapterView.OnItemClickListener,
        AdapterView.OnItemLongClickListener {

    private static final String TAG = "AppListFragment";
    static List<String> favorites = Arrays.asList("Play Movies & TV", "Netflix", "Plex", "YouTube", "Chrome");

    private static ListView mListViewApps;
    private List<AppInfo> mAppInfosList;
    private AppAdapter mAppAdapter;

    private LinearLayout mFavorites;
    private Callbacks mCallbacks = sDummyCallbacks;
    private boolean checkMode=false;
    private boolean pestanya=true;


    public interface Callbacks {
        void onExpandButtonClick();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override public void onExpandButtonClick() {}
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_app_list, container, false);

        mListViewApps = (ListView) v.findViewById(R.id.app_grid_list);
        mListViewApps.setOnItemClickListener(this);
        mListViewApps.setOnItemLongClickListener(this);

        return v;
    }


    @Override
    public void onItemClick(AdapterView adapter, View v, int position, long arg3) {

        final AppInfo info = mAppAdapter.getItem(position);

        if (mAppAdapter.getModeCheckBoxSelection()) {
            if (info.checked) {
                assert (getActivity()) != null;
                ((LauncherActivity) getActivity()).removeShortcutApp(info);
                info.checked = false;
            } else {
                assert (getActivity()) != null;
                ((LauncherActivity) getActivity()).addShortcutApp(info);
                info.checked = true;
            }
            mAppAdapter.notifyDataSetChanged();
        } else {
            startActivity(info.getIntent());
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
        final AppInfo info = mAppAdapter.getItem(position);
        if (info.checked) {
            assert (getActivity()) != null;
            ((LauncherActivity) getActivity()).removeShortcutApp(info);
            info.checked = false;
        } else {
            assert (getActivity()) != null;
            ((LauncherActivity) getActivity()).addShortcutApp(info);
            info.checked = true;
        }
        mAppAdapter.notifyDataSetChanged();
        //((LauncherActivity) getActivity()).reloadDesktop();
        return true;
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
    public void onLoadFinished(Loader<List<AppInfo>> listLoader, List<AppInfo> appInfos) { //Esto ocurre cuando el sistema devuelve todo el listado de Apps instaldas
        //Take out the favorites
        //appInfos = extractFavorites(appInfos);

        mAppInfosList = appInfos;
        mAppAdapter = new AppAdapter(getActivity(), appInfos);
        mListViewApps.setAdapter(mAppAdapter);

        //Creamos el listener para el checkbox de dentro del item
        mAppAdapter.setOnCheckBoxClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Onclicklistener", "OncheckboxClickListener posicion: " + v.getId());
                //La posicion está en el id del view
                AppInfo info = mAppInfosList.get(v.getId());

                if (info.checked) {
                    assert (getActivity()) != null;
                    ((LauncherActivity) getActivity()).addShortcutApp(info);
                }

                else
                {
                    assert (getActivity()) != null;
                    ((LauncherActivity) getActivity()).removeShortcutApp(info);
                }
            }
        });

        //((LauncherActivity) getActivity()).reloadDesktop();

        assert (getActivity()) != null;
        //((LauncherActivity)getActivity()).actualizaArrayAppsPreferencias();
    }


    @Override
    public void onLoaderReset(Loader<List<AppInfo>> listLoader) {
        mListViewApps.setAdapter(null);
    }

    private List<AppInfo> extractFavorites(List<AppInfo> infos) {
        List<AppInfo> favs = new ArrayList<AppInfo>(favorites.size());
        for (String name : favorites) {
            for (AppInfo info : infos) {
                if (name.equals(info.getTitle())) {
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
            image.setImageDrawable(new BitmapDrawable(getResources(), info.getBitmap()));
            title.setText(info.getTitle());

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
            //((LauncherActivity) getActivity()).setAppList(mAppInfosList);
           return mAppInfosList;
        }
        else
        {
            //((LauncherActivity) getActivity()).setAppList(new ArrayList<AppInfo>(0));
            return new ArrayList<AppInfo>(0);
        }
    }



    public void activaModoCheckBox(){
        if (mListViewApps.hasFocus()){
            Log.d(TAG, "Activamos modo checkbox");
            mAppAdapter.setSelectedCheckBoxMode(true);
            mAppAdapter.setSelectedItem(mListViewApps.getSelectedItemPosition());
            updateView();
            mAppAdapter.notifyDataSetChanged();
        }
    }

    public void desactivaModoCheckBox() {
        if (mAppAdapter.getModeCheckBoxSelection()) {
            Log.d(TAG, "Desactivamos modo checkbox");
            mAppAdapter.setSelectedCheckBoxMode(false);
            mAppAdapter.setSelectedItem(mListViewApps.getSelectedItemPosition());
            updateView();
            mAppAdapter.notifyDataSetChanged();
        }
    }

    public void setFocus(){
        mListViewApps.requestFocus();
    }

    public boolean appgetModeCheckBoxSelec(){
      return   mAppAdapter.getModeCheckBoxSelection();
    }

    /**
     * Funcion para actualizar el checkBox del ListItem en caso que esté el modo selección
     */
    private void updateView() {

        //Guardamos en el adapter la seleccion actual
        mAppAdapter.setSelectedItem(mListViewApps.getSelectedItemPosition());

        //Colocamos el listItem anterior como estaba
        View viewOld = mListViewApps.getChildAt(mAppAdapter.getLastSelectedItem()- mListViewApps.getFirstVisiblePosition());
        FrameLayout frameOld = (FrameLayout) viewOld.findViewById(R.id.frame_checkbox);
        Log.e("-------------updateView()", "-----------listItem anterior---");
        frameOld.setBackgroundColor(mAppAdapter.getFrameCheckBoxView(mAppInfosList.get(mAppAdapter.getLastSelectedItem()).checked));

        //En caso que dejemos el modo seleccion colocamos el check como estaba
        if (!mAppAdapter.getModeCheckBoxSelection()){
            View v = mListViewApps.getChildAt(mListViewApps.getSelectedItemPosition() - mListViewApps.getFirstVisiblePosition());
            FrameLayout frame = (FrameLayout) v.findViewById(R.id.frame_checkbox);
            Log.e("-------------updateView()", "----------el check como estaba---");
            frame.setBackgroundColor(mAppAdapter.getFrameCheckBoxView(mAppInfosList.get(mAppAdapter.getSelectedItem()).checked));
            return;
        }

        //Colocamos el checkbox actual con el aspecto seleccionado
        View v = mListViewApps.getChildAt(mListViewApps.getSelectedItemPosition() - mListViewApps.getFirstVisiblePosition());

        if (v == null)
            return;

        FrameLayout frame = (FrameLayout) v.findViewById(R.id.frame_checkbox);
        Log.e("-------------updateView()", "-----------ChekBox ACTUAL---");
        frame.setBackgroundColor(mAppAdapter.getFrameCheckBoxView(false));
    }

    public void onKeyDownD() {
    }


    public void onKeyLeftD() {

    }



    public void  onKeyUpD() {


    }

    public List<AppInfo> getmAppInfosList() {

        //if(mAppInfosList==null){}
        return mAppInfosList;
    }



    /*

           public void cargaListaApps() {

             //Limpiamos los shortcuts primero
             ((LauncherActivity)getActivity()).clearShortcutsApp();

             //Recorremos la lista de aplicaciones en preferencias y añadimos.
             assert (getActivity()) != null;
             ArrayList<String> listaApps = ((LauncherActivity)getActivity()).getAppsNamePreferences();

             for (int i = 0; i< listaApps.size(); i++) {
                 String nombreApp = listaApps.get(i);
                 for (int j = 0; j < mAppInfosList.size(); j++) {
                     AppInfo appInfoTemp =  mAppInfosList.get(j);
                     if (SaveLoadAppsPreferences.ComparaNombreAppInfo(appInfoTemp, nombreApp)){
                         assert (getActivity()) != null;
                         ((LauncherActivity)getActivity()).addShortcutApp(appInfoTemp);
                         appInfoTemp.checked = true;
                         break;
                     }
                 }
             }

             ((LauncherActivity)getActivity()).focusAppGrid();
         }*/


}
