package com.energysistem.energylauncher.tvboxlauncher.broadcastreceiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.text.format.Time;
import android.util.Log;

import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.DesktopFragment;

/**
 * Created by pgc on 16/02/2015.
 */
public class TimeChangedReceiver extends BroadcastReceiver {

    public final static String INTENT = "com.energysistem.energylauncher.tvboxlauncher.broadcastreceiver.TimeChangedReceiver";
    DesktopFragment main = null;
    public void setMainActivityHandler(DesktopFragment main){
        this.main=main;
    }


    @Override
    public void onReceive(Context context, Intent intent) {
        Log.e("onReceive","----------------------------------------------TimeChangedReceiver");
        Time time = new Time();
        time.setToNow();
        main.updateClockWidget(time);
    }
}
