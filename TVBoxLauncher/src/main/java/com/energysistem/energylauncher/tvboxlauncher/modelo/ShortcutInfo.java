package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.ComponentName;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;

/**
 * Created by Vicente Giner Tendero on 12/04/2014.
 */
public abstract class ShortcutInfo {

    //The intent used to start the application.
    private String title;
    private Bitmap iconBitmap;

    public String getTitle(){
        return title;
    }
    public void setTitle(String tit){
        title = tit;
    }

    public Bitmap getBitmap(){
        return iconBitmap;
    }
    public void setBitmap(Bitmap bitmap){
        iconBitmap = bitmap;
    }

    public ShortcutInfo() {
        this.title = "";
        this.iconBitmap = null;
    }

    public abstract Intent getIntent();

    public int getBackgroundColor() {
        return Color.RED;
    }





}
