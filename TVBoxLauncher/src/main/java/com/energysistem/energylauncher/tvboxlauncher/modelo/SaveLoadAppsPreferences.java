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
    private static final String HEADLISTAPPS = "List_";

    private static final String WEBPAGES_LIST_SIZE = "WebListSize";
    private static final String HEADLISTWEBS = "ListW_";

    private ArrayList<String> ListaAppsString;
    private ArrayList<WebPageItem> ListaWebs;

    private SharedPreferences mSharedPrefs;


    public SaveLoadAppsPreferences(Context context) {
        this.mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_LIST_APPS, 0);
        ListaAppsString = getListaAppsString();

    }


    /*
    Gestion de las apps guardadas en preferencias
    Al añadir se comprueba que no se haya guardado ya la app
    Se añade por el final.
    Al borrar se busca la app en concreto, actualizamos la lista gloval y se borra y se vuelve a crear el
    array con los nombres de la apps (para no andar mareando con los indices)
    */

    public void ActualizaListaApps(List<AppInfo> listaAppInfos) {
        ArrayList<String> listAppsString = new ArrayList<String>();

        //Bucles para comprobar si ya están metidas las apps en preferencias y conservar el orden de introduccion
        for (int i = 0; i < ListaAppsString.size(); i++) {
            String nombreApp = ListaAppsString.get(i);
            for (int j = 0; j < listaAppInfos.size(); j++) {
                AppInfo appTemp = listaAppInfos.get(j);
                if (ComparaNombreAppInfo(appTemp, nombreApp) && appTemp.checked) {
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
                if (!listAppsString.contains(nombreAppinfo)) {
                    listAppsString.add(nombreAppinfo);
                }
            }
        }

        removeAppsArray();
        guardaArrayApps(listAppsString);
    }

    public boolean addAppInfo(AppInfo app) {
        String nombre = getNombreApp(app);

        if (ListaAppsString.contains(nombre)) {
            //Ya está metida
            return false;
        } else {
            Log.v(TAG, "Añadida a las preferencias la app: " + getNombreApp(app));
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
            removeAppsArray();

            //La qutamos de la lista global
            ListaAppsString.remove(nombre);
            //Volvemos a alamacenar la lista
            guardaArrayApps(ListaAppsString);
            return true;
        }
        return false;
    }

    public static boolean ComparaNombreAppInfo(AppInfo appinfo, String nombre) {
        return nombre.equalsIgnoreCase(getNombreApp(appinfo));
    }

    public ArrayList<String> getListaAppsString() {
        ArrayList<String> listaStrings = new ArrayList<String>();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            String appS = mSharedPrefs.getString(HEADLISTAPPS + i, "");
            listaStrings.add(appS);
        }
        return listaStrings;
    }


    private void insertItemEnd(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);
        //El ultimo indice es el tamaño nuevo menos 1
        editor.putString(HEADLISTAPPS + (size), appName);

        size = size + 1;
        editor.putInt(PREFS_LIST_SIZE, size);

        editor.commit();
    }

    private void removeAppsArray() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(PREFS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            editor.remove(HEADLISTAPPS + i);
        }

        editor.putInt(PREFS_LIST_SIZE, 0);
        editor.commit();
    }

    private void guardaArrayApps(ArrayList<String> list) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = list.size();
        editor.putInt(PREFS_LIST_SIZE, size);

        for (int i = 0; i < size; i++) {
            editor.putString(HEADLISTAPPS + i, list.get(i));
        }
        editor.commit();
    }

    private static String getNombreApp(AppInfo app) {
        //return  app.title;
        //return app.getComponentName().toString() + app.title;
        return app.getPackageName();
    }


    /*
    Gestion de los enlaces guardados en preferencias
    No se comprueba si se repiten nombres o urls
    Se añaden por el final y que quitan dado un indice en concreto
    Para borrar se borra el array completo y se vuelve a crear (para no andar moviendo indices)
     */
    public void addWebPageInfo(WebPageInfo info) {
        WebPageItem item = new WebPageItem(info.getTitle(), info.getPageUrl().toString());
        String itemString = getStringWebPagePreferencias(item);
        ListaWebs.add(item);

        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(WEBPAGES_LIST_SIZE, 0);
        //El ultimo indice es el tamaño nuevo menos 1
        editor.putString(HEADLISTWEBS + (size), itemString);
        size = size + 1;
        editor.putInt(WEBPAGES_LIST_SIZE, size);

        editor.commit();

        Log.v(TAG, "Añadida a las preferencias la pagina: " + info.getTitle());

    }

    public void removeWebPageInfo(int indice) throws ArrayIndexOutOfBoundsException {
        if (indice >= ListaWebs.size()) {
            throw new ArrayIndexOutOfBoundsException("Indice fuera del array");
        }
        removeWebPagesArray();

        ListaWebs.remove(indice);
        guardaWebPagesArray(ListaWebs);
    }

    public ArrayList<WebPageItem> getListaWebs() {
        if (ListaWebs == null) {
            ListaWebs = new ArrayList<WebPageItem>();

            int size = mSharedPrefs.getInt(WEBPAGES_LIST_SIZE, 0);

            for (int i = 0; i < size; i++) {
                String texto = mSharedPrefs.getString(HEADLISTWEBS + i, "");
                String[] partes = texto.split(",");
                WebPageItem item = new WebPageItem(partes[0], partes[1]);
                ListaWebs.add(item);
            }
        }
        return ListaWebs;
    }


    private void removeWebPagesArray() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(WEBPAGES_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            editor.remove(HEADLISTWEBS + i);
        }

        editor.putInt(WEBPAGES_LIST_SIZE, 0);
        editor.commit();
    }

    private void guardaWebPagesArray(ArrayList<WebPageItem> listaWebs) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = listaWebs.size();
        editor.putInt(WEBPAGES_LIST_SIZE, size);

        for (int i = 0; i < size; i++) {
            String linea = getStringWebPagePreferencias(getListaWebs().get(i));
            editor.putString(HEADLISTWEBS + i, linea);
        }
        editor.commit();
    }

    private String getStringWebPagePreferencias(WebPageItem item) {
        return item.getTitle() + "," + item.getUri();
    }


    public class WebPageItem {
        private String title;
        private String uri;

        public String getTitle() {
            return title;
        }

        public String getUri() {
            return uri;
        }

        public WebPageItem(String tit, String uris) {
            title = tit;
            uri = uris;
        }


    }


}
