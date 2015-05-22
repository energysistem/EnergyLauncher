package com.energysistem.energylauncher.tvboxlauncher;

import android.content.Context;

import com.energysistem.energylauncher.tvboxlauncher.util.IconCache;


/**
 * Created by rharter on 1/11/14.
 */
public class LauncherAppState {

    private IconCache mIconCache;

    private static Context sContext;

    private static LauncherAppState INSTANCE;

    public static LauncherAppState getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new LauncherAppState();
        }
        return INSTANCE;
    }

    public Context getContext() {
        return sContext;
    }

    public static void setApplicationContext(Context context) {
        if (sContext != null) {
        }
        sContext = context.getApplicationContext();
    }

    private LauncherAppState() {
        if (sContext == null) {
            throw new IllegalStateException(sContext.getString(R.string.launcherAppState_error));
        }

        mIconCache = new IconCache(sContext);
    }

    public IconCache getIconCache() {
        return mIconCache;
    }

    public void setIconCache(IconCache iconCache) {
        mIconCache = iconCache;
    }
}
