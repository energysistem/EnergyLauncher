package com.energysistem.energylauncher.tvboxlauncher.ui;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.DesktopFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.MenuListFragmernt;
import com.energysistem.energylauncher.tvboxlauncher.util.SystemUiHider;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.view.View;
import android.widget.FrameLayout;

import java.util.ArrayList;
import java.util.List;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 *
 * @see SystemUiHider
 */
public class LauncherActivity extends Activity implements AppListFragment.Callbacks {

    /**
     * Whether or not the system UI should be auto-hidden after
     * {@link #AUTO_HIDE_DELAY_MILLIS} milliseconds.
     */
    private static final boolean AUTO_HIDE = true;

    /**
     * If {@link #AUTO_HIDE} is set, the number of milliseconds to wait after
     * user interaction before hiding the system UI.
     */
    private static final int AUTO_HIDE_DELAY_MILLIS = 3000;

    /**
     * If set, will toggle the system UI visibility upon interaction. Otherwise,
     * will show the system UI visibility upon interaction.
     */
    private static final boolean TOGGLE_ON_CLICK = true;

    /**
     * The flags to pass to {@link SystemUiHider#getInstance}.
     */
    private static final int HIDER_FLAGS = SystemUiHider.FLAG_HIDE_NAVIGATION;

    /**
     * The instance of the {@link SystemUiHider} for this activity.
     */
    private SystemUiHider mSystemUiHider;

    SaveLoadAppsPreferences preferencesListadoApps;

    private AppListFragment appListFragment;
    //private SelectedAppsListFragment selectedAppsListFragment;
    private DesktopFragment desktopFragment;

    private DrawerLayout desktopLayout;
    private FrameLayout appLayout;
    private FrameLayout optionLayout;

    private ActionBarDrawerToggle drawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        preferencesListadoApps = new SaveLoadAppsPreferences(this);
        setContentView(R.layout.activity_main);
        desktopLayout = (DrawerLayout) findViewById(R.id.drawer_layout);

        drawerToggle = new ActionBarDrawerToggle(this,
                desktopLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
                getFragmentManager().beginTransaction()
                        .replace(R.id.left_drawer, new MenuListFragmernt()).commit();
            }

            public void onDrawerOpened(View drawerView) {

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
                    .add(R.id.rigth_drawer, appListFragment)
                    .commit();

            //Drawer Izquierdo
            getFragmentManager().beginTransaction()
                    .add(R.id.left_drawer, new MenuListFragmernt()).commit();

        }
    }

//    public void cargaFragmentSelectAppListIzquierdo() {
//        selectedAppsListFragment = new SelectedAppsListFragment();
//        getFragmentManager().beginTransaction()
//                .add(R.id.left_drawer, selectedAppsListFragment)
//                .commit();
//    }


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
        return;
    }

    public void toggleDrawer(int gravity) {
        if(desktopLayout.isDrawerOpen(gravity)) {
            desktopLayout.closeDrawer(gravity);
        } else {
            desktopLayout.openDrawer(gravity);
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
