package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

import com.energysistem.energylauncher.tvboxlauncher.ui.views.Shortcut;

/**
 * Created by vgt on 12/04/2014.
 */
public abstract class ShortcutInfo {

    //The intent used to start the application.
    public String title;
    public Bitmap iconBitmap;

    public ShortcutInfo() {
        this.title = "Holaaaa";
        this.iconBitmap = null;
    }

    public abstract Intent getIntent();

    public int getBackgroundColor() {
        return Color.RED;
    }
}
