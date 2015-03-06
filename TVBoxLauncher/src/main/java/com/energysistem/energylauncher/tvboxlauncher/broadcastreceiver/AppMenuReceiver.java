package com.energysistem.energylauncher.tvboxlauncher.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;

public class AppMenuReceiver extends BroadcastReceiver {

    public final static String INTENT = "com.energysistem.energylauncher.tvboxlauncher.APPS";

    @Override
    public void onReceive(Context context, Intent intent) {
        String cancel = getResultData();

        Intent i = new Intent(context, LauncherActivity.class);
        context.startActivity(i);

        if (cancel == null) {

        }
    }
}
