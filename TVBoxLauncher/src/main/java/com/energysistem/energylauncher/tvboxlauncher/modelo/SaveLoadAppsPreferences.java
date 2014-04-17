package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 15/04/2014.
 */
public class SaveLoadAppsPreferences {

    private Context mContext;
    private static final String PREFS_LIST_APPS = "SelectedDesktopApps";
    private static final String PREFS_LIST_SIZE = "ListSize";

    private ArrayList<String> ListaApps;
    private SharedPreferences mSharedPrefs;


    public SaveLoadAppsPreferences(Context context) {
        this.mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_LIST_APPS, 0);
        ListaApps = loadStringList();

    }


    public void ActualizaListaApps(List<AppInfo> listaApps) {
        ArrayList<String> listAppsString = new ArrayList<String>(listaApps.size());
        for (int i = 0; i < listaApps.size(); i++) {
            if (listaApps.get(i).checked)
                listAppsString.add(listaApps.get(i).getComponentName().toString());
        }
        removeArray();
        addArray(listAppsString);
    }


    public boolean addAppInfo(AppInfo app) {
        String nombre = app.getComponentName().toString();

        if (ListaApps.contains(nombre)) {
            //Ya está metida
            return false;
        } else {
            ListaApps.add(nombre);
            insertItemEnd(nombre);
            return true;
        }
    }

    public boolean removeAppInfo(AppInfo app) {
        String nombre = app.getComponentName().toString();

        if (ListaApps.contains(nombre)) {
            //Pasando de gestionar los indices de la lista de las preferencias

            //Está la app. Borramos la lista y la volvemos a crear.
            removeArray();
            //La qutamos de la lista global
            ListaApps.remove(nombre);
            //Volvemos a alamacenar la lista
            addArray(ListaApps);
            return true;
        }
        return false;
    }


    public ArrayList<String> loadStringList() {
        ArrayList<String> listaStrings = new ArrayList<String>();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            String appS = mSharedPrefs.getString("list_" + i, "");
            listaStrings.add(appS);
        }
        return listaStrings;
    }


    private void insertItemEnd(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);
        size = size+1;
        editor.putString("list_" + size, appName);
        editor.putInt(PREFS_LIST_SIZE, size);

        editor.commit();
    }


    private void removeArray() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            editor.remove("list_" + i);
        }

        editor.putInt(PREFS_LIST_SIZE, 0);
        editor.commit();
    }

    private void addArray(ArrayList<String> list) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = list.size();
        editor.putInt(PREFS_LIST_SIZE, size);

        for (int i = 0; i < size; i++) {
            editor.putString("list_" + i, list.get(i));
        }
        editor.commit();
    }


}
