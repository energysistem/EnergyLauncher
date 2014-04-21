package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 15/04/2014.
 */
public class SaveLoadAppsPreferences {

    private static final String TAG = "SaveLoadPreferencias";
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
            AppInfo appTemp = listaApps.get(i);
            if (listaApps.get(i).checked)
                listAppsString.add(getNombreApp(appTemp));
        }
        removeArray();
        addArray(listAppsString);
    }


    public boolean addAppInfo(AppInfo app) {
        String nombre = getNombreApp(app);

        if (ListaApps.contains(nombre)) {
            //Ya está metida
            return false;
        } else {
            Log.v(TAG, "Añadida a las preferencias la app: "  + getNombreApp(app));
            ListaApps.add(nombre);
            insertItemEnd(nombre);
            return true;
        }
    }

    public boolean removeAppInfo(AppInfo app) {
        String nombre = getNombreApp(app);

        if (ListaApps.contains(nombre)) {
            //Pasando de gestionar los indices de la lista de las preferencias

            //Está la app. Borramos la lista entera y la volvemos a crear.
            removeArray();

            //La qutamos de la lista global
            ListaApps.remove(nombre);
            //Volvemos a alamacenar la lista
            addArray(ListaApps);
            return true;
        }
        return false;
    }

    /**
     *
     * @return Listado de apps introducidas en opciones. app.getComponentName().toString() + app.title
     */
    public ArrayList<String> loadStringList() {
        ArrayList<String> listaStrings = new ArrayList<String>();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            //Se guarda a partir del 1 por tanto i+1
            String appS = mSharedPrefs.getString("list_" + (i + 1), "");
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


    private static String getNombreApp(AppInfo app){
        //return  app.title;
        return app.getComponentName().toString() + app.title;
    }


    public static boolean ComparaNombreAppInfo(AppInfo appinfo, String nombre){
        return nombre.equalsIgnoreCase(getNombreApp(appinfo));



    }


}
