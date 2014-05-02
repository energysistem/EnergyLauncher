package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.graphics.Bitmap;

/**
 * Created by emg on 02/05/2014.
 */
public class DraggableItemApp {

        int pos;
        String nombre;
        Bitmap icono;

    public void setPos(int pos) {
        this.pos = pos;
    }
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }
    public void setIcono(Bitmap icono) {
        this.icono = icono;
    }

    public int getPos() {
        return pos;
    }
    public String getNombre() {
        return nombre;
    }
    public Bitmap getIcono() {
        return icono;
    }


    public DraggableItemApp(int pos, String nombre, Bitmap icono) {
        this.pos = pos;
        this.nombre = nombre;
        this.icono = icono;
    }
}
