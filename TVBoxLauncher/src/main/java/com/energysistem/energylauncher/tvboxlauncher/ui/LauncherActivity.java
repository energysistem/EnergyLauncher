package com.energysistem.energylauncher.tvboxlauncher.ui;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.DesktopFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.MenuListFragmernt;

import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends Activity implements AppListFragment.Callbacks {

    SaveLoadAppsPreferences preferencesListadoApps;
    private AppListFragment appListFragment;
    private DesktopFragment desktopFragment;

    private DrawerLayout desktopLayout;
    private FrameLayout appLayout;
    private FrameLayout optionLayout;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        desktopLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appLayout = (FrameLayout) findViewById(R.id.right_drawer);
        optionLayout = (FrameLayout) findViewById(R.id.left_drawer);

        preferencesListadoApps = new SaveLoadAppsPreferences(this);

        drawerToggle = new ActionBarDrawerToggle(this,
                desktopLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.left_drawer, new MenuListFragmernt()).commit();
                desktopFragment.getAppButton().requestFocus();
            }

            public void onDrawerOpened(View drawerView) {
                try {
                    ((ViewGroup) drawerView).getChildAt(0).requestFocus();
                } catch (NullPointerException e) {

                }
            }
        };

        desktopLayout.setDrawerListener(drawerToggle);

        LauncherAppState.setApplicationContext(getApplicationContext());

        if (savedInstanceState == null) {
            desktopFragment = new DesktopFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, desktopFragment)
                    .commit();

            //Drawer derecho
            appListFragment = new AppListFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.right_drawer, appListFragment)
                    .commit();

            //Drawer Izquierdo
            getFragmentManager().beginTransaction()
                    .add(R.id.left_drawer, new MenuListFragmernt()).commit();

        }



    }
    /*
    **************************************************************
    Manejar las preferencias
    **************************************************************
    */

    public void addShortcut(ShortcutInfo shortcutInfo) {
        if ( shortcutInfo instanceof  AppInfo){
            desktopFragment.addShortcut(shortcutInfo);
            preferencesListadoApps.addAppInfo((AppInfo) shortcutInfo);
        }
    }

    public void removeShortcut(ShortcutInfo shortcutInfo) {
        desktopFragment.removeShortcut(shortcutInfo);
        preferencesListadoApps.removeAppInfo((AppInfo) shortcutInfo);
    }

    public ArrayList<String> getAppsNamePreferences(){
        return preferencesListadoApps.loadStringList();
    }

    public void actualizaArrayAppsPreferencias(List<AppInfo> listaApps){
        preferencesListadoApps.ActualizaListaApps(listaApps);
    }




    @Override
    public void onExpandButtonClick() {

    }

    public List<AppInfo> getAppList(){
        return  appListFragment.getAppsInfos();
    }

    @Override
    public void onBackPressed() {
        desktopLayout.closeDrawers();
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("Key Pressed", KeyEvent.keyCodeToString(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAPTIONS:
                toggleDrawer(appLayout);
                return true;
            case KeyEvent.KEYCODE_SETTINGS:
                toggleDrawer(optionLayout);
                return true;
            case KeyEvent.KEYCODE_TV:
                Intent i;
                PackageManager manager = getPackageManager();
                try {
                    i = manager.getLaunchIntentForPackage("com.amlogic.DTVPlayer");
                    i.addCategory(Intent.CATEGORY_LAUNCHER);
                    startActivity(i);
                } catch (NullPointerException e) {
                    Log.d("Key Shortcut","App not foumd");
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    public void toggleDrawer(FrameLayout drawerLaayout) {
        if(desktopLayout.isDrawerOpen(drawerLaayout)) {
            desktopLayout.closeDrawers();
        } else {
            desktopLayout.closeDrawers();
            desktopLayout.openDrawer(drawerLaayout);
        }
    }

    public FrameLayout getAppLayout() {
        return appLayout;
    }

    public FrameLayout getOptionLayout() {
        return optionLayout;
    }

    public DrawerLayout getDesktopLayout() {
        return desktopLayout;
    }
}
