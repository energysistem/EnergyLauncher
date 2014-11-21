package com.energysistem.energylauncher.tvboxlauncher.ui;

import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;

import android.support.v4.app.ActionBarDrawerToggle;
import android.support.v4.widget.DrawerLayout;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.Toast;

import com.energysistem.energylauncher.tvboxlauncher.LauncherAppState;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.ShortcutAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppArrangeFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.AppListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.DesktopFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.MenuListFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.NotificationsFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.OptionsLauncherFragment;
import com.energysistem.energylauncher.tvboxlauncher.ui.fragments.RightFragment;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;


public class LauncherActivity extends Activity implements AppListFragment.Callbacks {

    private static final String TAG = "LauncherActivity";
    SaveLoadAppsPreferences preferencesListadoApps;
    private RightFragment mRightFragment;
    private DesktopFragment mDesktopFragment;
    private NotificationsFragment mNotificationFragent;
    private OptionsLauncherFragment mOptionsLauncherFragment;
    private AppArrangeFragment mAppArrangeFragment;
    private ArrayList<DraggableItemApp> mListFavDraggables;

    List<AppInfo> AppList;

    private final String TAGFFRAGMENTRIGHT = "FRight";
    private final String TAGFFRAGMENTNOTIFICATIONS = "FNotifications";
    private final String TAGFFRAGMENTDESKTOP = "FDEsktop";


    private DrawerLayout desktopLayout;
    private FrameLayout appLayout;
    private FrameLayout notificationLayout;

    public final static String EXTRA_MESSAGE = "com.energysistem.energylauncher.MESSAGE";


    private ActionBarDrawerToggle drawerToggle;



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        Log.e("-------------ONCREATED", "------------------");

        if (savedInstanceState == null) {
            //Drawer derecho
            mRightFragment = new RightFragment();
            getFragmentManager().beginTransaction()
                   .add(R.id.right_drawer, mRightFragment, TAGFFRAGMENTRIGHT)
                   .commit();

            mDesktopFragment = new DesktopFragment();
            getFragmentManager().beginTransaction()
                    .add(R.id.content_frame, mDesktopFragment, TAGFFRAGMENTDESKTOP)
                    .commit();


            //Drawer Izquierdo
            mNotificationFragent = new NotificationsFragment() ;
            getFragmentManager().beginTransaction()
                    .add(R.id.left_drawer, mNotificationFragent, TAGFFRAGMENTNOTIFICATIONS)
                    .commit();
        } else {
            Log.e("-------------ONCREATED", "recuperamos los fragments");
            mDesktopFragment = (DesktopFragment) getFragmentManager().findFragmentByTag(TAGFFRAGMENTDESKTOP);
            mRightFragment = (RightFragment) getFragmentManager().findFragmentByTag(TAGFFRAGMENTRIGHT);
            mNotificationFragent = (NotificationsFragment) getFragmentManager().findFragmentByTag(TAGFFRAGMENTNOTIFICATIONS);
            return;
        }

        setContentView(R.layout.activity_main);
        desktopLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        appLayout = (FrameLayout) findViewById(R.id.right_drawer);
        notificationLayout = (FrameLayout) findViewById(R.id.left_drawer);


        preferencesListadoApps = new SaveLoadAppsPreferences(this);

        drawerToggle = new ActionBarDrawerToggle(this,
                desktopLayout,
                R.drawable.ic_navigation_drawer,
                R.string.drawer_open,
                R.string.drawer_close) {

            public void onDrawerClosed(View view) {
//                getFragmentManager().beginTransaction()
//                        .replace(R.id.left_drawer, new MenuListFragment()).commit();
                //mDesktopFragment.getAppButton().requestFocus();
            }

            public void onDrawerOpened(View drawerView) {
                try {
                    ((ViewGroup) drawerView).getChildAt(0).requestFocus();
                } catch (NullPointerException e) {

                }
            }
        };
        desktopLayout.setDrawerListener(drawerToggle);
        LauncherAppState.setApplicationContext(getApplicationContext());


        //carga el desktop guardado


    }

    @Override
         protected void onResume() {
             super.onResume();
             Log.e("-------------ONRESUME", "------------------");
         }

    @Override
    protected void onStart() {
        super.onStart();
         Log.e("-------------ONSTART", "------------------");
    }

     /*
          **************************************************************
          Eventos de teclado
          **************************************************************
          */


    @Override
    public void onBackPressed() {
        FragmentManager fm = getFragmentManager();
        if (fm.getBackStackEntryCount() > 0) {
            Log.i("MainActivity", "popping backstack");
            fm.popBackStack();
            return;
        } else {
            Log.i("MainActivity", "nothing on backstack, calling super");
            //super.onBackPressed();
        }

        desktopLayout.closeDrawers();
    }


    @Override
         public boolean onKeyDown(int keyCode, KeyEvent event) {
             Log.d("onKeyDown Pressed", KeyEvent.keyCodeToString(keyCode));
             switch (keyCode) {
                 case KeyEvent.KEYCODE_DPAD_RIGHT:
                     if (appLayout.isShown()) {
                         desktopLayout.setFocusable(false);

                         mRightFragment.onKeyRightD();
                     } else if (notificationLayout.isShown()) {
                         //mOptionsLauncherFragment.onKeyRightAndLeft();

                         toggleDrawer(notificationLayout);
                     } else if (desktopLayout.isShown()) {
                         //desktopLayout.setFocusable(false);
                         focusProblems(false, 1);
                         toggleDrawer(appLayout);
                         mDesktopFragment.onKeyRightAndLeft(KeyEvent.KEYCODE_DPAD_RIGHT);
                     }
                     break;

                 case KeyEvent.KEYCODE_DPAD_LEFT:
                     if (appLayout.isShown()) {
                         mRightFragment.onKeyLeftD();
                     } else if (notificationLayout.isShown()) {

                     } else if (desktopLayout.isShown()) {
                         mDesktopFragment.onKeyRightAndLeft(KeyEvent.KEYCODE_DPAD_LEFT);
                     }
                     return true;

                 /*
                 case KeyEvent.KEYCODE_DPAD_UP:
                     if (appLayout.isShown()) {
                         mRightFragment.onKeyUpD();
                     }
                     return true;*/
             }
             return super.onKeyUp(keyCode, event);
         }



    @Override
         public boolean onKeyUp(int keyCode, KeyEvent event) {
             Log.d("onKeyUp Pressed", KeyEvent.keyCodeToString(keyCode));
             switch (keyCode) {
                 case KeyEvent.KEYCODE_CAPTIONS:
                     toggleDrawer(appLayout);
                     return true;
                 case KeyEvent.KEYCODE_SETTINGS:
                     toggleDrawer(notificationLayout);
                     return true;
                 case KeyEvent.KEYCODE_TV:
                     Intent i;
                     PackageManager manager = getPackageManager();
                     try {
                         i = manager.getLaunchIntentForPackage("com.amlogic.DTVPlayer");
                         i.addCategory(Intent.CATEGORY_LAUNCHER);
                         startActivity(i);
                     } catch (NullPointerException e) {
                         Log.d("Key Shortcut", "App not foumd");
                     }
                     return true;
                 case KeyEvent.KEYCODE_MEDIA_RECORD:


                     return true;

                 case KeyEvent.KEYCODE_DPAD_DOWN:
                     if (appLayout.isShown()) {
                         mRightFragment.onKeyDownU();
                     } else if (notificationLayout.isShown()) {
                         //mNotificationFragent.onKeyUpAndDown(KeyEvent.KEYCODE_DPAD_DOWN);
                     }
                     return true;
                 case KeyEvent.KEYCODE_DPAD_UP:
                     if (appLayout.isShown()) {
                         mRightFragment.onKeyDownU();
                     } else if (notificationLayout.isShown()) {
                     /*
                     TODO: REVISAR
                      */
                         //mNotificationFragent.onKeyUpAndDown(KeyEvent.KEYCODE_DPAD_UP);
                     }
                     return true;
                 case KeyEvent.KEYCODE_BACK:
                     Log.d("Presed", "KEYCODE_BACK");
                     onBackPressed();
                     return true;
                 case KeyEvent.KEYCODE_DPAD_RIGHT:

                     return false;

                 case KeyEvent.KEYCODE_DPAD_LEFT:

                     return false;
             }
             return super.onKeyUp(keyCode, event);
             //return true;
         }


    /*
     * Gestionar los fucking Fragments
     */

    public void ShowOptionsLauncherMenuFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mOptionsLauncherFragment =  new OptionsLauncherFragment();

        ft.replace(R.id.tab3, mOptionsLauncherFragment);
        ft.addToBackStack("OptionsLauncherFragment");
        ft.commit();
    }

    public void ShowReordenaDesktopAppsFragment(){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        mAppArrangeFragment =  new AppArrangeFragment();

        ft.replace(R.id.tab3, mAppArrangeFragment);
        ft.addToBackStack("AppArrangeFragment");
        ft.commit();
    }

    public void resetTab3(){
        mRightFragment.resetTab3();
    }

    public void ShowPickWallpaperFragment(){
//        FragmentTransaction ft = getFragmentManager().beginTransaction();
//        ImagePickerActivity wvf =  new ImagePickerActivity();
//
//        ft.replace(R.id.content_frame, wvf);
//        ft.addToBackStack("imagePicker");
//        toggleDrawer(appLayout);
//        ft.commit();

        String message = "";

        Intent intent = new Intent(Intent.ACTION_SET_WALLPAPER);
        startActivity(Intent.createChooser(intent, "Select Wallpaper"));

//        Intent intent = new Intent(this, ImagePickerActivity.class);
//        intent.putExtra(EXTRA_MESSAGE, message);
//        startActivity(intent);
    }


    public void setFocusFragmentDerecha() {
        if (mOptionsLauncherFragment.isVisible()) {
            mOptionsLauncherFragment.setFocus();
        } else if (mAppArrangeFragment.isVisible()) {
            mAppArrangeFragment.setFocus();
        }
    }



    @Override
    public void onExpandButtonClick() {

    }


    public List<ShortcutInfo> getAppList() {
        //mRightFragment.setinfoList();
        return mDesktopFragment.getFavInfoList();
        // return  mAppListFragment.getAppsInfos();
    }


    public void resetArrangeAppsFragment(){
        if (mAppArrangeFragment != null){
            mAppArrangeFragment.resetFragment();
        }
    }

    public void toggleDrawer(FrameLayout drawerLayout) {
        if (desktopLayout.isDrawerOpen(drawerLayout)) {
            if (mAppArrangeFragment != null) {
                mAppArrangeFragment.resetFragment();
            }
            desktopLayout.closeDrawers();
            mDesktopFragment.focusAppGrid();
        } else {
            desktopLayout.closeDrawers();
            desktopLayout.openDrawer(drawerLayout);
            if (drawerLayout.getLayerType() == appLayout.getLayerType()){
                mRightFragment.setFocus();
            }else{
                /*
                TODO: REVISAR
                 */
                //mNotificationFragent.setFocus();
            }
            drawerLayout.requestFocus();
        }
    }

    public FrameLayout getAppLayout() {
        return appLayout;
    }

    public FrameLayout getNotificationLayout() {
        return notificationLayout;
    }

    public DrawerLayout getDesktopLayout() {
        return desktopLayout;
    }


    public void focusAppGrid(){
        mDesktopFragment.focusAppGrid();
    }

    /**
     * ***********************************************************
     * Gestionar las apps añadidas/quitadas
     * ************************************************************
     */

    public void addShortcutApp(ShortcutInfo shortcutInfo) { //Añade Apps y Accesos directos Web
        Log.d("----ENTRAMOSSSS-------------------ADDD---------------------","dentro");
        //preferencesListadoApps.addInfoDesktop(shortcutInfo);
        if (shortcutInfo instanceof AppInfo) {
            mDesktopFragment.addShortcut(shortcutInfo);
            preferencesListadoApps.addAppInfo((AppInfo) shortcutInfo);
            fillDraggableList(shortcutInfo);
            resetArrangeAppsFragment();
           // reloadDesktop();
        } else if (shortcutInfo instanceof WebPageInfo) {
            mDesktopFragment.addShortcut(shortcutInfo);
            preferencesListadoApps.addWebPageInfo((WebPageInfo) shortcutInfo);
            fillDraggableList(shortcutInfo);
            resetArrangeAppsFragment();
           // reloadDesktop();
        }
    }


    public void removeShortcutApp(ShortcutInfo shortcutInfo) {
        Log.d("----ENTRAMOSSSS--------------------REMOVEEEE--------------------","dentro");
        //preferencesListadoApps.removeInfoDesktop(shortcutInfo);
        if (shortcutInfo instanceof AppInfo) {
            mDesktopFragment.removeShortcut(shortcutInfo);
           preferencesListadoApps.removeFavInfo(shortcutInfo);
            removeDraggableList(shortcutInfo);
            resetArrangeAppsFragment();
          //  reloadDesktop();
        }
        else if (shortcutInfo instanceof WebPageInfo) {
            mDesktopFragment.removeShortcut(shortcutInfo);
            preferencesListadoApps.removeFavInfo(shortcutInfo);
            removeDraggableList(shortcutInfo);
            resetArrangeAppsFragment();
          //  reloadDesktop();
        }
    }


    public ArrayList<String> getAppsNamePreferences() {
        return preferencesListadoApps.getListaFavsString();
    }

    public void actualizaOrdenApps(ArrayList<DraggableItemApp> listaDraggables) {
        Log.d("----Asctualiza Orden Apps ACTIVITY: ",Integer.toString(listaDraggables.size()));
       // mDesktopFragment.setGridAdapter(draggableTOshortcutAdapter(listaDraggables));
        preferencesListadoApps.setListaDesktop(draggableTOshortcutAdapter(listaDraggables));
        mDesktopFragment.setGridAdapter(draggableTOshortcutAdapter(listaDraggables));
        preferencesListadoApps.ActualizaOrdenListaApps(listaDraggables);
        setListFavDraggables(listaDraggables);
        //mRightFragment.cargaListaAppsAux();
    }

    private ShortcutAdapter draggableTOshortcutAdapter(List<DraggableItemApp> listaDraggables) {
        ShortcutAdapter shortcutAdapter;
        shortcutAdapter = new ShortcutAdapter(this);
        ShortcutInfo shortcutinfo;
        shortcutinfo = new ShortcutInfo() {
            @Override
            public Intent getIntent() {
                return null;
            }
        };


        List<AppInfo> appList;
        List<WebPageInfo> webList;
        appList=mRightFragment.getmAppInfosListAx();
        webList=mRightFragment.getmWebInfosListAx();

        String nomD;
        int pos=-1;
        int tip=0;
        //Rellenamos la lista ShortcutInfo
        for(int k=0;k<listaDraggables.size();k++) //convertimos la lista entera, uno a uno
        {   tip=0;

            nomD=listaDraggables.get(k).getTitle();
            //Esta  el draggable en las Apps?
            for(int a=0; a<appList.size();a++){
                if(nomD == appList.get(a).getTitle()){
                    pos=a;
                    tip=1;
                    break;
                }
            }
            if(tip==0){//Para que no mire en Webs si ya es una App
                //esta el draggable en las Webs?
                for(int a=0; a<webList.size();a++){
                    if(nomD == webList.get(a).getTitle()){
                      pos=a;
                      tip=2;
                      break;
                      }
                }
            }

            //Rellenamos el ShortcutInfo de la posicion k
            if(tip>0&&pos>-1){
                if(tip==1){
                shortcutinfo = appList.get(pos);}
                if(tip==2) {
                shortcutinfo = webList.get(pos);}
                shortcutAdapter.addItem(shortcutinfo);
            }
        }

        return shortcutAdapter;
    }


    /*
             Manejar-Rellenar-Vaciar la lista de FavoritosOrdenables
          */
    public void fillDraggableList(ShortcutInfo info){
        mListFavDraggables =  preferencesListadoApps.getmListAppsDragablesOrdenadaAUX();
            if(mListFavDraggables== null)
             {mListFavDraggables = new ArrayList<DraggableItemApp>();}

             //creamos un nuevo draggable
             DraggableItemApp item = new DraggableItemApp(
                      mListFavDraggables.size(),
                     info.getTitle(),
                     info.getBitmap());
             //añadimos a la lista maestra de draggables
             mListFavDraggables.add(item);
        preferencesListadoApps.setmListAppsDragablesOrdenadaAUX(mListFavDraggables);
         }

    public void removeDraggableList(ShortcutInfo info){
        mListFavDraggables =  preferencesListadoApps.getmListAppsDragablesOrdenadaAUX();
        if(mListFavDraggables.size()!=0) {
            for (int k = 0; k < mListFavDraggables.size(); k++) {
                if (mListFavDraggables.get(k).getTitle() == info.getTitle()) {
                    mListFavDraggables.remove(k);
                }
            }
        }
        preferencesListadoApps.setmListAppsDragablesOrdenadaAUX(mListFavDraggables);

    }


    public ArrayList<DraggableItemApp> getListFavDraggables(){
       // Log.d("----getmListFavDraggables ACTIVITY: ",Integer.toString(mListFavDraggables.size()));
       return preferencesListadoApps.getmListAppsDragablesOrdenadaAUX();
       // return this.mListFavDraggables;
    }

    public void setListFavDraggables(ArrayList<DraggableItemApp> listAppsString){
        //Log.d("----setmListFavDraggables ACTIVITY le meto: ",Integer.toString(listAppsString.size()));
       // mListFavDraggables=listAppsString;
        preferencesListadoApps.setmListAppsDragablesOrdenadaAUX(listAppsString);
        //mAppArrangeFragment.setmListAppsDragablesOrdenada(listAppsString);
        //Log.d("----setmListFavDraggables ACTIVITY: cambio a",Integer.toString(mListFavDraggables.size()));
    }

    /*
    public void reloadDesktop(ArrayList<DraggableItemApp> mDragablesOrdenada) {
        mDesktopFragment.setGridAdapter(draggableTOshortcutAdapter(mDragablesOrdenada));
    }*/

    public void reloadDesktop() {
        Log.e("------preferencesListadoApps.getListaDesktop()------->", Integer.toString(preferencesListadoApps.getListaDesktop().getCount()));

        mDesktopFragment.setGridAdapter(preferencesListadoApps.getListaDesktop());
    }

    public ShortcutAdapter getGridDesktop(){
        return preferencesListadoApps.getListaDesktop();
    }



    /*
        Cargar la lista de Apps Instaldas
     */
    public void cargaListaApps(List<AppInfo> mAppsInstaladas) {
        Log.e("-----Tamaño mAppsInstaladas cargaListaApps()-------->", Integer.toString(mAppsInstaladas.size()));

        //Limpiamos los shortcuts primero
        //clearShortcutsApp();

        //Recorremos la lista de aplicaciones en preferencias y añadimos.
        assert (this) != null;
        ArrayList<String> listaApps = getAppsNamePreferences();

        for (int i = 0; i< listaApps.size(); i++) {
            String nombreApp = listaApps.get(i);
            for (int j = 0; j < mAppsInstaladas.size(); j++) {
                    AppInfo appInfoTemp = mAppsInstaladas.get(j);
                    if (SaveLoadAppsPreferences.ComparaNombreFavInfo(appInfoTemp.getTitle(), nombreApp)) {//Esta la App instalada en Favoritos?
                        assert (this) != null;
                        //addShortcutApp(appInfoTemp);
                        addAppInfoInstaladas(appInfoTemp);

                        Log.e("-----cargaListaApps()----Cargo App nº", Integer.toString(j));
                        appInfoTemp.checked = true;
                        break;
                    }

            }
        }
        focusAppGrid();
    }

    void addAppInfoInstaladas(AppInfo appInfo){
        preferencesListadoApps.addAppInfoLista(appInfo);
    }


    /*
        Cargar la lista de Webs guardadas
     */
    public void cargaListaWeb() {
        mRightFragment.setListWebPage(getlistaWeb());
    }

    public ArrayList<WebPageInfo> getListaWebF(){
        return preferencesListadoApps.getListaWebF();
    }

    public void setlistaWeb(List<WebPageInfo> lista){
        preferencesListadoApps.setListaWeb((ArrayList<WebPageInfo>) lista);
    }

    public ArrayList<WebPageInfo> getlistaWeb(){
        if(preferencesListadoApps.getListaWeb()!=null) {
            return preferencesListadoApps.getListaWeb();
        }
        else{
            return new ArrayList<WebPageInfo>();
        }
    }



    /**
     * ***********************************************************
     * Gestionar Shortcuts del Desktop
     * ************************************************************
     */
    /*
    public void clearShortcutsApp() {
        mDesktopFragment.clearShortcutsApps();
    }*/

    public void exitRightFragment() {
        focusProblems(true, 1);
        mDesktopFragment.focusAppGrid();
        mDesktopFragment.setHasFocus(false);
        toggleDrawer(appLayout);
    }

    public void focusProblems(boolean focus, int n){
        if(n==1) {
            GridView gwDesktop = (GridView) findViewById(R.id.app_grid);
            gwDesktop.setFocusable(focus);
        }
    }




    /* PROBANDO

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getKeyCode() == KeyEvent.KEYCODE_ENTER) {
            if (event.getAction() == KeyEvent.ACTION_UP){

                //enter();

                return true;
            }}
        return super.dispatchKeyEvent(event);
    }

    @Override
    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == KeyEvent.ACTION_DOWN) {
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                   // mBackDown = true;
                    return true;
                case KeyEvent.KEYCODE_HOME:
                   // mHomeDown = true;
                    return true;
            }

        }
        else if (event.getAction() == KeyEvent.ACTION_UP){
            switch (event.getKeyCode()) {
                case KeyEvent.KEYCODE_BACK:
                    //mBackDown = true;
                    return true;
                case KeyEvent.KEYCODE_HOME:
                    //mHomeDown = true;
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }*/

}
