package com.energysistem.energylauncher.tvboxlauncher.ui.views;

import android.os.Build;

import java.io.IOException;

/**
 * Created by Vicente Giner Tendero on 09/05/2014.
 */
public class StatusBarAdmin {

    public void ShowStatusBar() {
        Process proc = null;
        try {
            proc = Runtime.getRuntime().exec(new String[] { "su", "-c", "am startservice -n com.android.systemui/.SystemUIService" });
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (proc != null) {
                proc.waitFor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void HideStatusBar() {
        Process proc = null;

        String ProcID = "79"; //HONEYCOMB AND OLDER

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH) {
            ProcID = "42"; //ICS AND NEWER
        }

        try {
            proc = Runtime.getRuntime().exec(new String[]{"su", "-c", "service call activity " + ProcID + " s16 com.android.systemui"});
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            if (proc != null) {
                proc.waitFor();
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
