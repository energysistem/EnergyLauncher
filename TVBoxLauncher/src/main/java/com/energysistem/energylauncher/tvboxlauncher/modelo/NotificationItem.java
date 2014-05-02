package com.energysistem.energylauncher.tvboxlauncher.modelo;

import java.util.List;

/**
 * Created by MFC on 30/04/2014.
 */
public class NotificationItem {
    public int icon;
    public String text;;
    public String subText;
    public static List<NotificationItem> drawerItem;

    // Constructor.
    public NotificationItem(int icon, String text, String subText) {

        this.icon = icon;
        this.text = text;
        this.subText = subText;
    }
}
