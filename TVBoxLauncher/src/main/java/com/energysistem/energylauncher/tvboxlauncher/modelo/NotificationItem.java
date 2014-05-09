package com.energysistem.energylauncher.tvboxlauncher.modelo;

import java.util.List;

/**
 * Created by MFC on 30/04/2014.
 */
public class NotificationItem {
    public int icon;
    public String title;;
    public String text;
    public String info;
    public int smallIcon;
    public String date;
    public static List<NotificationItem> drawerItem;

    // Constructor.
    public NotificationItem(int icon, String title, String text, String info, int smallIcon, String date) {

        this.icon = icon;
        this.title = title;
        this.text = text;
        this.info = info;
        this.smallIcon = smallIcon;
        this.date = date;
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

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public int getSmallIcon() {
        return smallIcon;
    }

    public void setSmallIcon(int smallIcon) {
        this.smallIcon = smallIcon;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
