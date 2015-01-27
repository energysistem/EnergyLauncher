package com.energysistem.energylauncher.tvboxlauncher.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.provider.Settings;

public class SettingsMenuReceiver extends BroadcastReceiver {

    public final static String INTENT = "com.energysistem.energylauncher.tvboxlauncher.SETTINGS";
    public final static String CANCEL_EXTRA = "com.energysistem.energylauncher.tvboxlauncher.CANCEL_EXTRA";

    @Override
    public void onReceive(Context context, Intent intent) {
        String cancel = getResultData();

        if (cancel == null) {
            Intent newIntent = new Intent(Settings.ACTION_SETTINGS);
            newIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(newIntent);
        }
    }
}
