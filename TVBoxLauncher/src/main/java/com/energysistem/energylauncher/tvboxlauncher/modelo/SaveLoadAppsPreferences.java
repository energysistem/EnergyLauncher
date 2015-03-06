package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.energysistem.energylauncher.tvboxlauncher.database.BookmarkDAO;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.ShortcutAdapter;

import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

import static android.app.PendingIntent.getActivities;
import static android.app.PendingIntent.getActivity;

/**
 * Created by emg on 15/04/2014.
 */


public class SaveLoadAppsPreferences {
    /*
     * LISTA DE APLICACIONES INICIALES (En su orden, no meter aquí los bookmarks, solo app)
     */
    private static final String[] LISTA_APP_INICIO = {"com.amlogic.miracast","com.amlogic.PicturePlayer", "com.farcore.videoplayer", "org.geometerplus.zlibrary.ui.android",
            "com.facebook.katana", "com.twitter.android", "com.android.vending", "com.google.android.youtube.googletv", "com.android.browser", "com.fb.FileBrower"};


    private static final String TAG = "SaveLoadPreferencias";
    private static final String FAVS_LIST_SIZE = "FavsListSize";
    private static final String HEADLISTFAVS = "ListFav_";
    private Context mContext;
    private final String PREFS_LIST_APPS = "SelectedDesktopApps";
    //private static final String PREFS_LIST_SIZE = "ListSize";
    //private static final String HEADLISTAPPS = "List_";

    //private static final String WEBPAGES_LIST_SIZE = "WebListSize";
    //private static final String HEADLISTWEBS = "ListW_";

    //private ArrayList<String> ListaFavoritos;
    //private ArrayList<WebPageItem> ListaWebs;
    public ArrayList<String> listaFavoritos;

    private SharedPreferences mSharedPrefs;

    private ArrayList<DraggableItemApp> mListAppsDragablesOrdenadaAUX;
    private ArrayList<WebPageInfo> listaWebF;
    private ArrayList<WebPageInfo> listaWeb;
    public ArrayList<AppInfo> listaApp;
    private static ShortcutAdapter listaDesktop;
    private BookmarkDAO datasource;


    public SaveLoadAppsPreferences(Context context) {
        this.mContext = context;
        mSharedPrefs = context.getSharedPreferences(PREFS_LIST_APPS, 0);

        listaFavoritos = getListaFavsString();


        if(mSharedPrefs.getBoolean("first_load",true)){
            Log.e("SaveLoadAppsPreferences","First Load");

            for(String app: LISTA_APP_INICIO) {
                insertItemEnd(app);
            }

            SharedPreferences.Editor editor = mSharedPrefs.edit();
            editor.putBoolean("first_load",false);
            editor.commit();
            listaFavoritos = getListaFavsString();
        }

        listaWebF = getListaWebF();

        datasource = new BookmarkDAO(context);
        datasource.open();
        ArrayList<WebPageInfo> lBookmarks = datasource.getAllBookmarks();
        datasource.close();

        for(int i = 0; i < lBookmarks.size(); i++) {
              Log.e("Bookmark "+ i,lBookmarks.get(i).toString());
        }

        listaWeb = lBookmarks;

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
        Log.e("----LISTA FAVORITOOOOOS---",listaFavoritos.size()+"");
        //Bucles para comprobar si ya están metidas las apps en preferencias y conservar el orden de introduccion
        for (int i = 0; i < listaFavoritos.size(); i++) {
            String nombreFav = listaFavoritos.get(i);
            Log.e("----FAVORITOOOO---",nombreFav);
            for (int j = 0; j < listaFavInfos.size(); j++) {
                ShortcutInfo favTemp = listaFavInfos.get(j);
                Log.e("Primer COMPARE",((AppInfo) favTemp).getPackageName());
                if (ComparaNombreFavInfo(((AppInfo) favTemp).getPackageName(), nombreFav) && chek(favTemp)){

                    //Miramos si ya está metida en la lista
                    if (!listaFavoritos.contains(nombreFav)) {
                        Log.e("----NOMBRE FAVORITO---",nombreFav);
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


    public void ActualizaOrdenListaApps(ArrayList<DraggableItemApp> listaDraggables, ShortcutAdapter shAdapter) {
        //Borramos Array y creamos de nuevo.
        ArrayList<String> listAppsString = new ArrayList<String>();

        datasource.open();

        for (int i = 0; i < shAdapter.getCount(); i++) {
            if(shAdapter.getItem(i) instanceof AppInfo) {
                String nombreAppinfo = ((AppInfo) shAdapter.getItem(i)).getPackageName();
                if (!listAppsString.contains(nombreAppinfo)) {
                    listAppsString.add(nombreAppinfo);
                }
            }
            else if(shAdapter.getItem(i) instanceof WebPageInfo)
            {
                
               //listAppsString.add(((WebPageInfo) shAdapter.getItem(i)).getPosi(),shAdapter.getItem(i));
            }
        }
        datasource.close();
        setmListAppsDragablesOrdenadaAUX(listaDraggables);

        removeAppsArray();
        guardaFavArray(listAppsString);
    }



    public boolean removeFavInfo(ShortcutInfo favInfo){

        String nombre = getNombreFav(favInfo);
        return removeFavInfoByName(nombre);

    }

    public boolean removeFavInfoByName(String nombre){
        Log.e("Lista INICIAL",listaFavoritos.toString());
        Log.i("Eliminamos AppInfo (Tamaño lista fav: "+listaFavoritos.size(),nombre);
        if (listaFavoritos.contains(nombre)) {
            //Pasando de gestionar los indices de la lista de las preferencias - jajajajaja
            Log.i("Encontrada, borramos","");
            //Está la app. Borramos la lista entera y la volvemos a crear.
            removeAppsArray();

            //La qutamos de la lista global
            listaFavoritos.remove(nombre);
            Log.e("Despues",listaFavoritos.toString());
            //Volvemos a alamacenar la listaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa
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
        Log.e("getListaFavsString",listaStrings.toString());
        return listaStrings;

    }


    public void insertItemEnd(String appName) {
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
            Log.e("entramos en appinfo","huhu");
            return ((AppInfo) fav).getPackageName();
        } else if (fav instanceof WebPageInfo) {
            Log.e("entramos en webinfo","huhu");
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
    int pos;
    public void addWebPageInfo(WebPageInfo info) {


    }

    public void removeWebPageInfo(WebPageInfo info){
        removeWebPagesArray();
        listaFavoritos.remove(info);
        guardaFavArray(listaFavoritos);
    }


    private void removeWebPagesArray() {

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

    public ArrayList<WebPageInfo> getListaWebF() {
        if(listaWebF==null){
            listaWebF = new ArrayList<WebPageInfo>();
        }
        return listaWebF;
    }

    public void removeFavList(ShortcutInfo shortcutInfo) {
        if(listaWebF.contains(shortcutInfo)){
            listaWebF.remove(shortcutInfo);
        }
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

    public void setmListAppsDragablesOrdenadaAUX(ArrayList<DraggableItemApp> li){
        mListAppsDragablesOrdenadaAUX=li;
    }

    public ArrayList<DraggableItemApp> getmListAppsDragablesOrdenadaAUX(){
        if(mListAppsDragablesOrdenadaAUX==null){
            mListAppsDragablesOrdenadaAUX = new ArrayList<DraggableItemApp>();
        }
        return mListAppsDragablesOrdenadaAUX;
    }


    /*
        LISTA WEBS
     */

    public void setListaWeb(ArrayList<WebPageInfo> lista){
      listaWeb= lista;
    }

    public ArrayList<WebPageInfo> getListaWeb(){
        if(listaWeb==null){ listaWeb = new ArrayList<WebPageInfo>(); }
        return listaWeb;
    }
 //------------------------------------

    /*
        LISTA APPS
     */
    public void addAppInfoLista(AppInfo appInfo){
        listaApp.add(appInfo);
    }

    public void setListaApp(ArrayList<AppInfo> lista){
        listaApp = lista;
    }

    public ArrayList<AppInfo> getListaApp(){
        if(listaApp==null){ listaApp = new ArrayList<AppInfo>(); }
        return listaApp;
    }
    //------------------------------

    /*
        LISTA DESKTOP
     */
    public void setListaDesktop(ShortcutAdapter lista){
        listaDesktop=lista;
        listaDesktop.notifyDataSetChanged();
        //((LauncherActivity) getActivity().reloadDesktop(lista);
    }

    public ShortcutAdapter getListaDesktop(){
        if(listaDesktop==null){listaDesktop =  new ShortcutAdapter(mContext);}
        listaDesktop.notifyDataSetChanged();
        return listaDesktop;
    }

    public void addInfoDesktop (ShortcutInfo info){
        if(listaDesktop==null){ listaDesktop = new ShortcutAdapter(mContext);}
        try {
            listaDesktop.addItem(info, mContext);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        listaDesktop.notifyDataSetChanged();
    }

    public boolean addAppInfo(AppInfo app) {
        String nombre = getNombreFav(app);
        Log.i("Tamaño ListaFavoritos",listaFavoritos.size()+"");
        if (listaFavoritos.contains(nombre)) {//
            Log.e("Ya está metida","");
            //Ya está metida
            return false;
        } else {
            Log.v(TAG, "Añadida a las preferencias la app: " + getNombreFav(app));
            listaFavoritos.add(nombre);
            insertItemEnd(nombre);
            if(listaApp==null){listaApp = new ArrayList<AppInfo>();}
            listaApp.add(app);
            return true;
        }
    }

    public void removeInfoDesktop(ShortcutInfo info){
        listaDesktop.removeItem(info);
        listaDesktop.notifyDataSetChanged();
    }



    //;ArrayList<ShortcutAdapter> listaDesktop;


}
