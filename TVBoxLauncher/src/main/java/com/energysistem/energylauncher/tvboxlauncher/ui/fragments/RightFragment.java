package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TabHost;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.TabsAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
/**
 * Created by cfg on 15/09/2014.
 */
    public class RightFragment extends Fragment implements ActionBar.TabListener, AdapterView.OnItemClickListener, AdapterView.OnItemLongClickListener{
        //private static final String TAG = "AppListFragment";
        static List<String> favorites = Arrays.asList("Play Movies & TV", "Netflix", "Plex", "YouTube", "Chrome");

        private ListView mAppsInfoListView;
        private TabHost mTabHost;


        SaveLoadAppsPreferences preferencesListadoApps;

        private boolean menu=true;

        private boolean activo1=true;
        private boolean activo2=false;
        private boolean activo3=false;


        /*1º pestaña*/
        public AppListFragment mAppListFragment;
        /*2º pestaña*/
        private MenuBookMarkFragment mMenuBookMarkFragment;
        /*3º pestaña*/
        private MenuListFragment mMenuListViewFragment;


        private List<AppInfo> mAppInfosList;
        public List<ShortcutInfo>  mFavInfosList1;

        private LinearLayout mFavorites;
        //private Callbacks mCallbacks = sDummyCallbacks;

        private LinearLayout vPager;
        private TabsAdapter tAdapter;
       // private ActionBar aBar;

        private CharSequence mLabel;
    private boolean fueraDeBookmarks = true;

    public List<WebPageInfo> getmWebInfosListAx() {
             return mMenuBookMarkFragment.getListWebPage();
         }

    /*
    **************************************************************
    Creators
    **************************************************************
    */

    public interface Callbacks {
        void onExpandButtonClick();
    }

    private static Callbacks sDummyCallbacks = new Callbacks() {
        @Override public void onExpandButtonClick() {}
    };



public View onCreateView(LayoutInflater inflater, ViewGroup container, final Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View v = inflater.inflate(R.layout.fragment_right, container, false);
    //View v = inflater.inflate(R.layout.appthemeverde_tab_indicator_holo, container, false);
       /* vPager = (LinearLayout)findViewById(R.id.tabLL2);
        vPager.setAdapter(tAdapter);*/

        mTabHost = (TabHost) v.findViewById(R.id.tabHost_right);
        if(mTabHost==null)
            Log.e("mTabHost","Soy Null");
        mTabHost.setup();


        return v;
    }

    public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
        //estadoVariables();


        tAdapter = new TabsAdapter();

        preferencesListadoApps = new SaveLoadAppsPreferences(getActivity());

        //aBar = getActivity().getActionBar();

//        mTabHost = (TabHost) getActivity().findViewById(R.id.tabHost_right);
//        if(mTabHost==null)
//            Log.e("mTabHost","Soy Null");
//        mTabHost.setup();


        // *******Tab 1*******
        TabHost.TabSpec spec1 = mTabHost.newTabSpec(getActivity().getString(R.string.tab_aplicaciones));
        spec1.setContent(R.id.tab11);
        spec1.setIndicator(getResources().getText(R.string.tab_aplicaciones));

        /*
        TabHost.TabSpec calculatorTab = tabs.newTabSpec("calculator");
        calculatorTab.setContent(R.id.calculator);
        calculatorTab.setIndicator("Calculator");
        tabs.addTab(calculatorTab);*/

        mAppListFragment = new AppListFragment();
        getFragmentManager().beginTransaction().replace(R.id.tab11,
                mAppListFragment).commit();


        //********Tab 2*******
        TabHost.TabSpec spec2 = mTabHost.newTabSpec(getActivity().getString(R.string.tab_bookmarks));
        spec2.setContent(R.id.tab22);
        spec2.setIndicator(getResources().getText(R.string.tab_bookmarks));

        mMenuBookMarkFragment = new MenuBookMarkFragment();
        getFragmentManager().beginTransaction().replace(R.id.tab22,
                mMenuBookMarkFragment).commit();


        //********Tab 3*******
       /* TabHost.TabSpec spec3 = mTabHost.newTabSpec("SETTINGS");
        spec3.setContent(R.id.tab3);
        spec3.setIndicator(getResources().getText(R.string.tab3));

        mMenuListViewFragment = new MenuListFragment();
        getFragmentManager().beginTransaction().replace(R.id.tab3,
                mMenuListViewFragment).commit();*/


        //añadimos los tabs
        mTabHost.addTab(spec1);
        mTabHost.addTab(spec2);

        estadoVariables();
        /*mTabHost.setCurrentTab(0);
        menu=true;
*/
        //mTabHost.setOnTabChangedListener(tabChangeListener);
        mTabHost.setOnTabChangedListener(new TabHost.OnTabChangeListener(){
            public void onTabChanged(String tabID) {
                if(tabID.equals(getActivity().getString(R.string.tab_bookmarks))) {
                    mTabHost.setCurrentTab(1);
                }
                fueraDeBookmarks = true;
            }
        });

        mTabHost.addOnAttachStateChangeListener(new View.OnAttachStateChangeListener() {

            @Override
            public void onViewDetachedFromWindow(View v) {}

            @Override
            public void onViewAttachedToWindow(View v) {
                mTabHost.getViewTreeObserver().removeOnTouchModeChangeListener(mTabHost);
            }
        });


        mTabHost.setCurrentTab(0);





    }

    @Override
    public void onResume() {
        super.onResume();
        Log.e("onResume","RightFragment");
    }

    public void testFunction(Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);
    }

     /*
    **************************************************************
    Manejo de tabs
    **************************************************************
    */

    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub
        mTabHost.setCurrentTab(tab.getPosition());

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub


    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction ft) {
        // TODO Auto-generated method stub

    }


     /*
    **************************************************************
    Eventos de teclado
    **************************************************************
    */


    //********** KEY DOWN
   public void onKeyLeftD(){
        Log.e("CurrentTAB:",mTabHost.getCurrentTab()+"");
       switch (mTabHost.getCurrentTab())
       {
           case 0:      Log.d("TAG", "Estamos en app_grid");
                        if (mAppListFragment.appgetModeCheckBoxSelec()) {
                           mAppListFragment.desactivaModoCheckBox();

                            // return true;
                        } else {
                           ((LauncherActivity) getActivity()).exitRightFragment();
                            mAppListFragment.clearFocus();
                        }
                        break;

           case 1:      if (mMenuBookMarkFragment.appgetModeCheckBoxSelec()) {
                           mMenuBookMarkFragment.desactivaModoCheckBox();
                        }
                        else {
                           mTabHost.setCurrentTab(0);
                        }
                        break;

           case 2:      mTabHost.setCurrentTab(1);
                        Log.d("lansandooooIII", "mMenuBookMarkFragment.setFocus()");
                        resetTab3();
                        //mMenuBookMarkFragment.setFocus();
                        break;

           default:     break;
       }
   }


    public void onKeyRightD(){

        switch (mTabHost.getCurrentTab())
        {
            case 0:     if(!mAppListFragment.appgetModeCheckBoxSelec()){
                            mAppListFragment.activaModoCheckBox();
                        }
                        else{
                            Log.d("lansandooooDDD", "mMenuBookMarkFragment.setFocus()");
                            //mTabHost.setCurrentTab(1);
                            //mMenuBookMarkFragment.setFocus();
                        }
                            break;

            case 1:     //mMenuBookMarkFragment.setHasFocus(false);
                        if(!mMenuBookMarkFragment.appgetModeCheckBoxSelec()) {
                            mMenuBookMarkFragment.activaModoCheckBox();
                        }
                        else{
                            Log.e("-KeyRighD--", "---------CheckBoxMode Activado ya--");
                        }
                        break;

            case 2:     break;

            default:    break;


        }
    }


    public void onKeyDownD() {
        if(mTabHost.getCurrentTab()==1 ){
          //  mMenuBookMarkFragment.
        }

       /* if(!menu) {
            if (mAppListFragment.isVisible()) {
                mAppListFragment.onKeyDownD();
            } else if (activo2) {
                mMenuBookMarkFragment.onKeyDownD();
            } else if (activo3) {
                mMenuListViewFragment.onKeyDownD();
            }
        }
        else{//Entrar a contenido de pestaña
            if (mAppListFragment.isVisible()) {
               // menu=false;
                mAppListFragment.setFocus();
            } else if (mMenuBookMarkFragment.isVisible()) {
              //  menu=false;
                mMenuBookMarkFragment.setFocus();
            } else if (activo3) {
               // menu=false;
                mMenuListViewFragment.setFocus();
            }
        }*/
    }

    public void onKeyUpD() {
     /*  if(mTabHost.getCurrentTab()==1){
           Log.d("IF 1", "Estamos dentro");


            LinearLayout tab2 = (LinearLayout) (getActivity()).findViewById(R.id.tab2);
            TextView topWebShortcut = (TextView) (getActivity()).findViewById(R.id.menuBookmarkTxtUri);

            if(topWebShortcut.isFocused()){
                Log.d("IF 2", "Estamos dentro!!!!!!");
                tab2.requestFocus();
            }
        }*/
    }


    //********** KEY UP  --->return true; para manejarlas y que no haga nada

    public void onKeyLeftU(){
        Log.d("Current Tab-->",String.valueOf(mTabHost.getCurrentTab()));

    }

    public void onKeyRightU(){
        /*if(!menu) {
            if (activo1) {
                mAppListFragment.onKeyDownD();
            } else if (activo2) {
                mMenuBookMarkFragment.onKeyDownD();
            } else if (activo3) {
                mMenuListViewFragment.onKeyDownD();
            }
        }
        else{
            if (activo1) {
            //    menu=false;
                mAppListFragment.setFocus();
            } else if (activo2) {
            //    menu=false;
                mMenuBookMarkFragment.setFocus();
            } else if (activo3) {
            //    menu=false;
                mMenuListViewFragment.setFocus();
            }
        }
        Log.d("Current Tab-->",String.valueOf(mTabHost.getCurrentTab()));
*/
    }

    public void onKeyDownU() {

        switch (mTabHost.getCurrentTab())
        {
            case 0:     Log.d("Entramos en case 03131231213123","0");

                        break;

            case 1:

                        if(fueraDeBookmarks==true) {
                            mMenuBookMarkFragment.setFocus();
                            fueraDeBookmarks=false;
                        }
                        break;

            case 2:     Log.d("Entramos en case 2","2");
                        fueraDeBookmarks = true;
                        break;

            default:    fueraDeBookmarks = true;
                        break;
        }


    }

    public void onKeyUpU() {
        Log.d("Current Tab-->",String.valueOf(mTabHost.getCurrentTab()));

    }

    /*
    **************************************************************
    ITEM Click
    **************************************************************
    */

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

    }

    @Override
    public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
        return true;
    }



    public void setFocus(){
        mAppListFragment.setFocus();
    }



    /*
                **************************************************************
                Gestionar las apps añadidas/quitadas
                **************************************************************
                */

/*

    public void cargaListaAppsAux() {
        mAppListFragment.cargaListaApps();
    }*/


/*
    **************************************************************
    Gestionar los accesos directos a paginas web añadidos/quitados
    **************************************************************
    */


    public void estadoVariables(){
        Log.d("Estado Inicial","VARIABLES-----------------------------------");
        Log.d("Tab Inicial",String.valueOf(mTabHost.getCurrentTab()));
        mTabHost.setCurrentTab(0);
        Log.d("Tab 0",String.valueOf(mTabHost.getCurrentTab()));
        mTabHost.setCurrentTab(1);
        Log.d("Tab 1",String.valueOf(mTabHost.getCurrentTab()));
        mTabHost.setCurrentTab(2);
        Log.d("Tab 2",String.valueOf(mTabHost.getCurrentTab()));
        mTabHost.setCurrentTabByTag(getActivity().getString(R.string.tab_aplicaciones));
        Log.d("Tab APPS (byTag)",String.valueOf(mTabHost.getCurrentTab()));
        mTabHost.setCurrentTabByTag(getActivity().getString(R.string.tab_bookmarks));
        Log.d("Tab BOOKMARKS (byTag)",String.valueOf(mTabHost.getCurrentTab()));
        mTabHost.setCurrentTabByTag(getActivity().getString(R.string.tab_settings));
        Log.d("Tab SETTINGS (byTag)",String.valueOf(mTabHost.getCurrentTab()));
        Log.d("Fin Pestañas","VARIABLES-----------------------------------");
        Log.d("Menu", String.valueOf(menu));

    }



    public void resetTab3() {
//        getFragmentManager().beginTransaction().replace(R.id.tab3,
//                mMenuListViewFragment).commit();
    }

    public List<AppInfo> getmAppInfosListAx(){
        return mAppListFragment.getmAppInfosList();
    }


    public void setListWebPage(ArrayList<WebPageInfo> lista){
        mMenuBookMarkFragment.setmListWebPage(lista);
        mMenuBookMarkFragment.actualisa();
    }

    public ArrayList<WebPageInfo> getListWebpage(){
        return mMenuBookMarkFragment.getListWebPage();
    }

/*
    public void tabChange(Fragment newFragment int simpleFragment){
        FragmentTransaction ft = getFragmentManager().beginTransaction();
        ft.replace(simpleFragment, newFragment);
        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        ft.addToBackStack(null);
        ft.commit();
        R.id.

    }*/
}