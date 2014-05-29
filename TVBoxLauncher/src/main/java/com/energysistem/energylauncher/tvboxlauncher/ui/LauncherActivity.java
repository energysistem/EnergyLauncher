package com.energysistem.energylauncher.tvboxlauncher.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
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
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppArrangeFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.DesktopFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.NotificationsFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.OptionsLauncherFragment;

import java.util.ArrayList;
import java.util.List;


public class LauncherActivity extends Activity implements AppListFragment.Callbacks {

    private static final String TAG = "LauncherActivity";
    SaveLoadAppsPreferences preferencesListadoApps;
    private AppListFragment mAppListFragment;
    private DesktopFragment mDesktopFragment;
    private NotificationsFragment mNotificationFragent;
    private OptionsLauncherFragment mOptionsLauncherFragment;
    private AppArrangeFragment mAppArrangeFragment;

    private DrawerLayout desktopLayout;
    private FrameLayout appLayout;
    private FrameLayout notificationLayout;

    public final static String EXTRA_MESSAGE = "com.energysistem.energylauncher.MESSAGE";

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        desktopLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appLayout = (FrameLayout) findViewById(R.id.right_drawer);
        notificationLayout = (FrameLayout) findViewById(R.id.left_drawer);


        preferencesListadoApps = new SaveLoadAppsPreferences(this);

        drawerToggle = new ActionBarDrawerToggle(this,
                desktopLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.left_drawer, new MenuListFragment()).commit();
                mDesktopFragment.getAppButton().requestFocus();
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
            mDesktopFragment = new DesktopFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, mDesktopFragment)
                    .commit();

            //Drawer derecho
            mAppListFragment = new AppListFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.right_drawer, mAppListFragment)
                    .commit();

            //Drawer Izquierdo
            mNotificationFragent = new NotificationsFragment() ;
            getFragmentManager().beginTransaction()
                    .add(R.id.left_drawer, mNotificationFragent)
                    .commit();
        }

    }


    /*
        **************************************************************
        Gestionar las apps añadidas/quitadas
        **************************************************************
        */
    public void addShortcutApp(ShortcutInfo shortcutInfo) {
        if ( shortcutInfo instanceof  AppInfo){
            mDesktopFragment.addShortcut(shortcutInfo);
            preferencesListadoApps.addAppInfo((AppInfo) shortcutInfo);
            resetArrangeAppsFragment();
        }
        else if (shortcutInfo instanceof WebPageInfo){
            mDesktopFragment.addShortcut(shortcutInfo);
            preferencesListadoApps.addWebPageInfo((WebPageInfo)shortcutInfo);
        }
    }

    public void removeShortcutApp(ShortcutInfo shortcutInfo) {
        mDesktopFragment.removeShortcut(shortcutInfo);
        preferencesListadoApps.removeAppInfo((AppInfo) shortcutInfo);
        resetArrangeAppsFragment();
    }

    public ArrayList<String> getAppsNamePreferences(){
        return preferencesListadoApps.getListaAppsString();
    }

    public void actualizaArrayAppsPreferencias(){
        preferencesListadoApps.ActualizaListaApps(mAppListFragment.getAppsInfos());
    }

    public void actualizaOrdenApps(List<DraggableItemApp> listaDraggables){
        preferencesListadoApps.ActualizaOrdenListaApps(listaDraggables);
        mAppListFragment.cargaListaApps();
    }

    public void clearShortcutsApp(){
        mDesktopFragment.clearShortcutsApps();
    }


    /*
    **************************************************************
    Gestionar los accesos directos a paginas web añadidos/quitados
    **************************************************************
    */
    public ArrayList<WebPageInfo> cargaShortcutsEnDesktop(){
        ArrayList<WebPageInfo> lista = new ArrayList<WebPageInfo>();

        ArrayList<SaveLoadAppsPreferences.WebPageItem> listaWebShortCuts = preferencesListadoApps.getListaWebs();
        for (int i = 0; i < listaWebShortCuts.size(); i++) {
            SaveLoadAppsPreferences.WebPageItem item = listaWebShortCuts.get(i);
            WebPageInfo webInfo = new WebPageInfo(item.getUri(), item.getTitle());
            mDesktopFragment.addShortcut(webInfo);
            lista.add(webInfo);
        }
        return lista;
    }


    public void removeShortcut(int webShortcutPos){
        mDesktopFragment.removeShortcut(webShortcutPos);
        preferencesListadoApps.removeWebPageInfo(webShortcutPos);
    }


    public void focusAppGrid(){
        mDesktopFragment.focusAppGrid();
    }


    /*
     * Gestionar los fucking Fragments
     */
    public void ShowOptionsLauncherMenuFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mOptionsLauncherFragment =  new OptionsLauncherFragment();

        ft.replace(R.id.tab3, mOptionsLauncherFragment);
        ft.addToBackStack("OptionsLauncherFragment");
        ft.commit();
    }

    public void ShowReordenaDesktopAppsFragment(){

        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mAppArrangeFragment =  new AppArrangeFragment();

        ft.replace(R.id.tab3, mAppArrangeFragment);
        ft.addToBackStack("AppArrangeFragment");
        ft.commit();
    }

    public void ShowPickWallpaperFragment(){
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ImagePickerActivity wvf =  new ImagePickerActivity();
//
//        ft.replace(R.id.content_frame, wvf);
//        ft.addToBackStack("imagePicker");
//        toggleDrawer(appLayout);
//        ft.commit();

        String message = "";

        Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(intent, "Select Wallpaper"));

//        Intent intent = new Intent(this, ImagePickerActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
    }

    public void setFocusFragmentDerecha() {
        if (mOptionsLauncherFragment.isVisible()) {
            mOptionsLauncherFragment.setFocus();
        } else if (mAppArrangeFragment.isVisible()) {
            mAppArrangeFragment.setFocus();
        }
    }



    @Override
    public void onExpandButtonClick() {

    }

    public List<AppInfo> getAppList(){
        return  mAppListFragment.getAppsInfos();
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
            return;
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            //super.onBackPressed();
        }

        desktopLayout.closeDrawers();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        Log.d("onKeyDown Pressed", KeyEvent.keyCodeToString(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (appLayout.isShown()) {
                    mAppListFragment.onKeyRight();
                } else if (notificationLayout.isShown()) {
                    //mOptionsLauncherFragment.onKeyRightAndLeft();
                    toggleDrawer(notificationLayout);
                } else if (desktopLayout.isShown()) {
                    mDesktopFragment.onKeyRightAndLeft(KeyEvent.KEYCODE_DPAD_RIGHT);
                }
                break;

            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (appLayout.isShown()) {
                    if (mAppListFragment.onKeyLeft()) {
                        //true si estabamos en el seleccionable checkBox, ya se maneja en el fragment
                        return true;
                    } else {
                        //mDesktopFragment.FocusAppListButton();
                        toggleDrawer(appLayout);
                    }
                } else if (notificationLayout.isShown()) {

                } else if (desktopLayout.isShown()) {
                    mDesktopFragment.onKeyRightAndLeft(KeyEvent.KEYCODE_DPAD_LEFT);
                }
                return true;
        }
        return super.onKeyUp(keyCode, event);
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("onKeyUp Pressed", KeyEvent.keyCodeToString(keyCode));
        switch (keyCode) {
            case KeyEvent.KEYCODE_CAPTIONS:
                toggleDrawer(appLayout);
                return true;
            case KeyEvent.KEYCODE_SETTINGS:
                toggleDrawer(notificationLayout);
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
            case KeyEvent.KEYCODE_MEDIA_RECORD:

                return true;

            case KeyEvent.KEYCODE_DPAD_DOWN:
                if (appLayout.isShown()) {
                    mAppListFragment.onKeyUpDown(KeyEvent.KEYCODE_DPAD_DOWN);
                }else if (notificationLayout.isShown()){
                    mNotificationFragent.onKeyUpAndDown(KeyEvent.KEYCODE_DPAD_DOWN);
                }
                return true;
            case KeyEvent.KEYCODE_DPAD_UP:
                if (appLayout.isShown()) {
                    mAppListFragment.onKeyUpDown(KeyEvent.KEYCODE_DPAD_UP);
                }else if (notificationLayout.isShown()) {
                    mNotificationFragent.onKeyUpAndDown(KeyEvent.KEYCODE_DPAD_UP);
                }
                return true;
            case KeyEvent.KEYCODE_BACK:
                Log.d("Presed","KEYCODE_BACK");
                onBackPressed();
                return true;
        }
            return super.onKeyUp(keyCode, event);
            //return true;
    }



    public void resetArrangeAppsFragment(){
        if (mAppArrangeFragment != null){
            mAppArrangeFragment.resetFragment();
        }
    }

    public void toggleDrawer(FrameLayout drawerLayout) {
        if (desktopLayout.isDrawerOpen(drawerLayout)) {
            if (mAppArrangeFragment != null) {
                mAppArrangeFragment.resetFragment();
            }
            desktopLayout.closeDrawers();
            mDesktopFragment.focusAppGrid();
        } else {
            desktopLayout.closeDrawers();
            desktopLayout.openDrawer(drawerLayout);
            if (drawerLayout.getLayerType() == appLayout.getLayerType()){
                mAppListFragment.setFocus();
            }else{
                mNotificationFragent.setFocus();
            }
            drawerLayout.requestFocus();
        }
    }

    public FrameLayout getAppLayout() {
        return appLayout;
    }

    public FrameLayout getNotificationLayout() {
        return notificationLayout;
    }

    public DrawerLayout getDesktopLayout() {
        return desktopLayout;
    }
}
