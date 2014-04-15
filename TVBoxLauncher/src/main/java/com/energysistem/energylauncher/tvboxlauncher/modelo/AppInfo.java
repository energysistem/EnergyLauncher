package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.ComponentName;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.util.Log;

import com.energysistem.energylauncher.tvboxlauncher.util.IconCache;

import java.util.HashMap;

/**
 * Created by emg on 09/04/2014.
 *
 * Clase para representar una app en el launcher
 */
public class AppInfo extends ShortcutInfo {

    private static final String TAG = "EnergyLauncher.AppInfo";

    static final int DOWNLOADED_FLAG = 1;
    static final int UPDATED_SYSTEM_APP_FLAG = 2;

    private String packageName;
    private ComponentName componentName;

    public Boolean checked = false;

    private long firstInstallTime;
    private int flags = 0;

    public String getPackageName(){
        return packageName;
    }

    public AppInfo(PackageManager pm, ResolveInfo info, IconCache iconCache,
                   HashMap<Object, CharSequence> labelCache) {
        this.packageName = info.activityInfo.applicationInfo.packageName;
        this.componentName = new ComponentName(packageName, info.activityInfo.name);

        try {
            PackageInfo pi = pm.getPackageInfo(packageName, 0);
            flags = initFlags(pi);
            firstInstallTime = initFirstInstallTime(pi);
        } catch (PackageManager.NameNotFoundException e) {
            Log.d(TAG, "PackageManager.getApplicationInfo failed for " + packageName);
        }

        iconCache.getTitleAndIcon(this, info, labelCache);
    }

    public static int initFlags(PackageInfo pi) {
        int appFlags = pi.applicationInfo.flags;
        int flags = 0;
        if ((appFlags & android.content.pm.ApplicationInfo.FLAG_SYSTEM) == 0) {
            flags |= DOWNLOADED_FLAG;

            if ((appFlags & android.content.pm.ApplicationInfo.FLAG_UPDATED_SYSTEM_APP) != 0) {
                flags |= UPDATED_SYSTEM_APP_FLAG;
            }
        }
        return flags;
    }

    public static long initFirstInstallTime(PackageInfo pi) {
        return pi.firstInstallTime;
    }

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(Intent.ACTION_MAIN);
        intent.addCategory(Intent.CATEGORY_LAUNCHER);
        intent.setComponent(componentName);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_RESET_TASK_IF_NEEDED);
        return intent;
    }

    public ComponentName getComponentName() {
        return componentName;
    }
}