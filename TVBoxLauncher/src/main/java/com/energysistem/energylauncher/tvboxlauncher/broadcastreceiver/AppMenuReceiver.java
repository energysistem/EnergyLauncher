package com.energysistem.energylauncher.tvboxlauncher.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.provider.Settings;

import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;

public class AppMenuReceiver extends BroadcastReceiver {

    public final static String INTENT = "com.energysistem.energylauncher.tvboxlauncher.APPS";


    @Override
    public void onReceive(Context context, Intent intent) {
        String cancel = getResultData();

        if (cancel == null) {
            Intent i = new Intent(context, LauncherActivity.class);

            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            i.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP);
            String name = "ABRIR_MENU_APP";
            String value = "ABRIR_APP";
            i.putExtra(name, value);
            context.startActivity(i);
        }
    }
}
