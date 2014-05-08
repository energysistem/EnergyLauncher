package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.graphics.Bitmap;

/**
 * Created by emg on 02/05/2014.
 */
public class DraggableItemApp {

    int pos;
    String packageName;


    String title;
    Bitmap icono;

    public void setPos(int pos) {
        this.pos = pos;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    public void setIcono(Bitmap icono) {
        this.icono = icono;
    }

    public int getPos() {
        return pos;
    }

    public String getPackageName() {
        return packageName;
    }

    public Bitmap getIcono() {
        return icono;
    }

    public String getTitle() {
        return title;
    }


    public DraggableItemApp(int pos, String titlee, String packageName, Bitmap icono) {
        this.pos = pos;
        this.title = titlee;
        this.packageName = packageName;
        this.icono = icono;
    }
}
