package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;

/**
 * Created by emg on 15/04/2014.
 */
public class SaveLoadAppsPreferences {

    private static final String TAG = "SaveLoadPreferencias";
    private static final String FAVS_LIST_SIZE = "FavsListSize";
    private static final String HEADLISTFAVS = "ListFav_";
    private Context mContext;
    private static final String PREFS_LIST_APPS = "SelectedDesktopApps";
    //private static final String PREFS_LIST_SIZE = "ListSize";
    //private static final String HEADLISTAPPS = "List_";

    //private static final String WEBPAGES_LIST_SIZE = "WebListSize";
    //private static final String HEADLISTWEBS = "ListW_";

    //private ArrayList<String> ListaFavoritos;
    //private ArrayList<WebPageItem> ListaWebs;
    private ArrayList<String> listaFavoritos;

    private SharedPreferences mSharedPrefs;


    public SaveLoadAppsPreferences(Context context) {
        this.mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_LIST_APPS, 0);
        listaFavoritos = getListaFavsString();

    }


    /*
    Gestion de las apps guardadas en preferencias
    Al añadir se comprueba que no se haya guardado ya la app
    Se añade por el final.
    Al borrar se busca la app en concreto, actualizamos la lista global y se borra y se vuelve a crear el
    array con los nombres de la apps (para no andar mareando con los indices)
    */

    public void ActualizaListaApps(List<ShortcutInfo> listaFavInfos) {
        ArrayList<String> listFavsString = new ArrayList<String>();

        //Bucles para comprobar si ya están metidas las apps en preferencias y conservar el orden de introduccion
        for (int i = 0; i < listaFavoritos.size(); i++) {
            String nombreFav = listaFavoritos.get(i);
            for (int j = 0; j < listaFavInfos.size(); j++) {
                ShortcutInfo favTemp = listaFavInfos.get(j);
                if (ComparaNombreFavInfo(favTemp.getTitle(), nombreFav) && chek(favTemp)){
                    //Miramos si ya está metida en la lista
                    if (!listaFavoritos.contains(nombreFav)) {
                        listaFavoritos.add(nombreFav);
                    }
                    break;
                }
            }
        }

        //Bucles para introducir las nuevas apps
        for (int i = 0; i < listaFavInfos.size(); i++) {
            ShortcutInfo favTemp = listaFavInfos.get(i);
            if (chek(favTemp)) {
                String nombreAppinfo = getNombreFav(favTemp);
                if (!listFavsString.contains(nombreAppinfo)) {
                    listFavsString.add(nombreAppinfo);
                }
            }
        }

        removeAppsArray();
        guardaFavArray(listFavsString);
    }

    public void ActualizaOrdenListaApps(List<DraggableItemApp> listaDraggables) {
        //Borramos Array y creamos de nuevo.
        ArrayList<String> listAppsString = new ArrayList<String>();

        for (int i = 0; i < listaDraggables.size(); i++) {
            String nombreAppinfo = listaDraggables.get(i).getTitle();
            if (!listAppsString.contains(nombreAppinfo)) {
                listAppsString.add(nombreAppinfo);
            }
        }

        removeAppsArray();
        guardaFavArray(listAppsString);
    }

    public boolean addAppInfo(AppInfo app) {
        String nombre = getNombreFav(app);

        if (listaFavoritos.contains(nombre)) {
            //Ya está metida
            return false;
        } else {
            Log.v(TAG, "Añadida a las preferencias la app: " + getNombreFav(app));
            listaFavoritos.add(nombre);
            insertItemEnd(nombre);
            return true;
        }
    }

    public boolean removeFavInfo(ShortcutInfo favInfo){

        String nombre = getNombreFav(favInfo);
        return removeFavInfoByName(nombre);

    }

    public boolean removeFavInfoByName(String nombre){

        if (listaFavoritos.contains(nombre)) {
            //Pasando de gestionar los indices de la lista de las preferencias - jajajajaja

            //Está la app. Borramos la lista entera y la volvemos a crear.
            removeAppsArray();

            //La qutamos de la lista global
            listaFavoritos.remove(nombre);
            //Volvemos a alamacenar la lista
            guardaFavArray(listaFavoritos);
            return true;
        }
        return false;
    }


    public static boolean ComparaNombreFavInfo(String favTitle, String nombre) {
        return nombre.equalsIgnoreCase(favTitle);
    }
    public static boolean ComparaNombreFav(DraggableItemApp draggableItemApp, String nombre) {
        return nombre.equalsIgnoreCase(draggableItemApp.getTitle());
    }



    public static boolean ComparaNombreFavInfoInv(String favTitle, String nombre) {
        return favTitle.equalsIgnoreCase(nombre);
    }


    public ArrayList<String> getListaFavsString() {
        ArrayList<String> listaStrings = new ArrayList<String>();

        int size = mSharedPrefs.getInt(FAVS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            String appS = mSharedPrefs.getString(HEADLISTFAVS + i, "");
            listaStrings.add(appS);
        }
        return listaStrings;

    }


    private void insertItemEnd(String appName) {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(FAVS_LIST_SIZE, 0);
        //El ultimo indice es el tamaño nuevo menos 1
        editor.putString(HEADLISTFAVS + (size), appName);

        size = size + 1;
        editor.putInt(FAVS_LIST_SIZE, size);

        editor.commit();
    }

    private void removeAppsArray() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(FAVS_LIST_SIZE, 0);

        for (int i = 0; i < size; i++) {
            editor.remove(HEADLISTFAVS + i);
        }

        editor.putInt(FAVS_LIST_SIZE, 0);
        editor.commit();
    }


    private void guardaFavArray(ArrayList<String> listaFav){
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = listaFav.size();
        editor.putInt(FAVS_LIST_SIZE, size);

        for (int i = 0; i < size; i++) {
            editor.putString(HEADLISTFAVS + i, listaFav.get(i));
        }
        editor.commit();

    }


    private static String getNombreFav(ShortcutInfo fav) {
        if (fav instanceof AppInfo) {
            return ((AppInfo) fav).getPackageName();
        } else if (fav instanceof WebPageInfo) {
            return ((WebPageInfo) fav).getName();
        }
        else{
            return null;
        }
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
        listaFavoritos.add(getNombreFav(info));

        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(FAVS_LIST_SIZE, 0);
        //El ultimo indice es el tamaño nuevo menos 1
        editor.putString(HEADLISTFAVS + (size), itemString);
        size = size + 1;
        editor.putInt(FAVS_LIST_SIZE, size);

        editor.commit();

        Log.v(TAG, "Añadida a las preferencias la pagina: " + info.getTitle());

    }

    public void removeWebPageInfo(int indice) throws ArrayIndexOutOfBoundsException {
        if (indice >= listaFavoritos.size()) {
            throw new ArrayIndexOutOfBoundsException("Indice fuera del array");
        }
        removeWebPagesArray();

        listaFavoritos.remove(indice);
        guardaFavArray(listaFavoritos);
    }



    private void removeWebPagesArray() {
        SharedPreferences.Editor editor = mSharedPrefs.edit();

        int size = mSharedPrefs.getInt(PREFS_LIST_APPS, 0);

        for (int i = 0; i < size; i++) {
            editor.remove(HEADLISTFAVS + i);
        }

        editor.putInt(PREFS_LIST_APPS, 0);
        editor.commit();
    }



    private String getStringWebPagePreferencias(WebPageItem item) {
        return item.getTitle() + "," + item.getUri();
    }


    public boolean chek(ShortcutInfo favTemp){
    if(favTemp instanceof WebPageInfo){
        return ((WebPageInfo)favTemp).checked;}
    else{
        return ((AppInfo)favTemp).checked;}
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
