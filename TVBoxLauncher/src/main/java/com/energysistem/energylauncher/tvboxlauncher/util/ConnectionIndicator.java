package com.energysistem.energylauncher.tvboxlauncher.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.view.View;
import android.widget.ImageView;

import com.energysistem.energylauncher.tvboxlauncher.R;

/**
 * Created by Vicente Giner Tendero on 11/04/2014.
 */
public class ConnectionIndicator {

    private ConnectivityManager connManager;
    private WifiManager wifiManager;
    private ImageView wifiIcon;
    private ImageView ethernetIcon;

    public ConnectionIndicator(Context context, ImageView wifiIcon, ImageView ethernetIcon) {
        connManager = (ConnectivityManager) context.getSystemService(context.CONNECTIVITY_SERVICE);
        wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        this.wifiIcon = wifiIcon;
        this.ethernetIcon = ethernetIcon;
    }

    public void update() {
        // Get the Wifi info service
        NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

        if (mWifi != null && mWifi.isConnected()) {

            wifiIcon.setVisibility(View.VISIBLE);
            int wifiLevel = normalizeRssi(wifiManager.getConnectionInfo().getRssi());

            if(wifiLevel >= 100) {
                wifiIcon.setImageResource(R.drawable.stat_sys_tether_wifi_4);
            } else if(wifiLevel < 100 && wifiLevel >= 80) {
                wifiIcon.setImageResource(R.drawable.stat_sys_tether_wifi_3);
            } else if(wifiLevel < 80 && wifiLevel >= 60) {
                wifiIcon.setImageResource(R.drawable.stat_sys_tether_wifi_2);
            } else if(wifiLevel < 60 && wifiLevel >= 20) {
                wifiIcon.setImageResource(R.drawable.stat_sys_tether_wifi_1);
            } else {
                wifiIcon.setImageResource(R.drawable.stat_sys_tether_wifi_0);
            }
        } else {
            wifiIcon.setVisibility(View.GONE);
        }

        NetworkInfo mEtertnet = connManager.getNetworkInfo(ConnectivityManager.TYPE_ETHERNET);
        if (mEtertnet != null && mEtertnet.isConnected()) {
            ethernetIcon.setVisibility(View.VISIBLE);
        } else {
            ethernetIcon.setVisibility(View.GONE);
        }
    }


    private int normalizeRssi(int rssi){
        // Anything worse than or equal to this will show 0 bars
        final int MIN_RSSI = -100;
        // Anything better than or equal to this will show the max bars.
        final int MAX_RSSI = -55;

        int range = MAX_RSSI - MIN_RSSI;
        return 100 - ((MAX_RSSI - rssi) * 100 / range);
    }
}
