package com.energysistem.energylauncher.tvboxlauncher.ui;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.DesktopFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.MenuListFragmernt;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.SelectedAppsListFragment;
import com.energysistem.energylauncher.tvboxlauncher.util.SystemUiHider;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.Toast;

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

    private AppListFragment appListFragment;
    private SelectedAppsListFragment selectedAppsListFragment;
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
                    ((ViewGroup)drawerView).getChildAt(0).requestFocus();
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

    public void cargaFragmentSelectAppListIzquierdo() {
        selectedAppsListFragment = new SelectedAppsListFragment();
        getFragmentManager().beginTransaction()
                .add(R.id.left_drawer, selectedAppsListFragment)
                .commit();
    }

    public void addShortcut(ShortcutInfo shortcutInfo) {
        desktopFragment.addShortcut(shortcutInfo);
    }

    public void removeShortcut(ShortcutInfo shortcutInfo) {
        desktopFragment.removeShortcut(shortcutInfo);
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
