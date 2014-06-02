package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.app.PendingIntent;

import java.util.List;

/**
 * Created by MFC on 30/04/2014.
 */
public class NotificationItem {
    public int icon;
    public String title;;
    public String text;
    public PendingIntent intent;
    public static List<NotificationItem> drawerItem;

    // Constructor
    public NotificationItem(int icon, String title, String text, PendingIntent intent) {
        this.icon = icon;
        this.title = title;
        this.text = text;
        this.intent = intent;
    }

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

}
