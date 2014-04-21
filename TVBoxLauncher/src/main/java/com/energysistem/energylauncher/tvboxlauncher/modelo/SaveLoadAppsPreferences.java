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

    private ArrayList<String> ListaAppsString;
    private SharedPreferences mSharedPrefs;


    public SaveLoadAppsPreferences(Context context) {
        this.mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_LIST_APPS, 0);
        ListaAppsString = loadStringList();

    }


    public void ActualizaListaApps(List<AppInfo> listaAppInfos) {
        ArrayList<String> listAppsString = new ArrayList<String>();

        //Bucles para comprobar si ya están metidas las apps en preferencias y conservar el orden de introduccion
        for (int i = 0; i < ListaAppsString.size(); i++) {
            String nombreApp = ListaAppsString.get(i);
            for (int j = 0; j < listaAppInfos.size(); j++) {
                AppInfo appTemp = listaAppInfos.get(j);
                if (ComparaNombreAppInfo(appTemp, nombreApp) && appTemp.checked){
                    //Miramos si ya está metida en la lista
                    if (!listAppsString.contains(nombreApp)) {
                        listAppsString.add(nombreApp);
                    }
                    break;
                }
            }
        }

        //Bucles para introducir las nuevas apps
        for (int i = 0; i < listaAppInfos.size(); i++) {
            AppInfo appTemp = listaAppInfos.get(i);
            if (appTemp.checked) {
                String nombreAppinfo = getNombreApp(appTemp);
                if (!listAppsString.contains(nombreAppinfo)){
                    listAppsString.add(nombreAppinfo);
                }
            }
        }

        removeArray();
        addArray(listAppsString);

    }


    public boolean addAppInfo(AppInfo app) {
        String nombre = getNombreApp(app);

        if (ListaAppsString.contains(nombre)) {
            //Ya está metida
            return false;
        } else {
            Log.v(TAG, "Añadida a las preferencias la app: "  + getNombreApp(app));
            ListaAppsString.add(nombre);
            insertItemEnd(nombre);
            return true;
        }
    }

    public boolean removeAppInfo(AppInfo app) {
        String nombre = getNombreApp(app);

        if (ListaAppsString.contains(nombre)) {
            //Pasando de gestionar los indices de la lista de las preferencias

            //Está la app. Borramos la lista entera y la volvemos a crear.
            removeArray();

            //La qutamos de la lista global
            ListaAppsString.remove(nombre);
            //Volvemos a alamacenar la lista
            addArray(ListaAppsString);
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
            String appS = mSharedPrefs.getString("list_" + i, "");
            listaStrings.add(appS);
        }
        return listaStrings;
    }


    private void insertItemEnd(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);
        //El ultimo indice es el tamaño nuevo menos 1
        editor.putString("list_" + (size), appName);

        size = size+1;
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
