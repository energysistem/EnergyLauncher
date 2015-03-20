package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.graphics.Bitmap;
import android.net.Uri;

/**
 * Created by emg on 02/05/2014.
 */
public class DraggableItemApp{

    int pos;
    //String name;
    String title;
    Bitmap icono;
    private Uri pageUrl;

    public void setPos(int pos) {
        this.pos = pos;
    }

    /*
    public void setPackageName(String packageName) {
        this.name = name;
    }*/

    public void setIcono(Bitmap icono) {
        this.icono = icono;
    }

    public int getPos() {
        return pos;
    }

    /*
    public String getPackageName() {
        return name;
    }*/

    public Bitmap getIcono() {
        return icono;
    }

    public String getTitle() {
        return title;
    }

    //public DraggableItemApp(int pos, String title, String packageName, Bitmap icono) {
    public DraggableItemApp(int pos, String title, Bitmap icono) {
        this.pos = pos;
        this.title = title;
        //this.name = packageName;
        this.icono = icono;
    }

    public Uri getPageUrl() {
        return pageUrl;
    }

    public void setPageUrl(Uri pageUrl) {
        this.pageUrl = pageUrl;
    }
}
