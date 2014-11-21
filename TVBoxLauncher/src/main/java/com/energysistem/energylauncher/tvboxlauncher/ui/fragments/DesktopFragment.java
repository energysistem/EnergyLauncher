package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.BitmapFactory;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.ShortcutAdapter;
import com.energysistem.energylauncher.tvboxlauncher.util.Clock;
import com.energysistem.energylauncher.tvboxlauncher.util.ConnectionIndicator;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Locale;

/**
 * Created by Vicente Giner Tendero on 11/04/2014.
 */
public class DesktopFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mFavoritesGrid;
    private ShortcutAdapter gridAdapter;
    private ImageView appButton;
    private ImageView notificationButton;
    private TextView timeTextView;
    private TextView dateTextView;
    private Locale currentLocale;
    private ImageView wifiIcon;
    private ImageView ethernetIcon;
    private ConnectionIndicator connectionIndicator;


    //Propiedades elementos gridview para el scroll
    int mGridViewHeight = 0;
    int mDesktopIconHeight;
    int mColumnsDesktop;
    int mMarginDesktopIcons;
    private boolean hasFocus;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        //Margins
        mMarginDesktopIcons = (int)getResources().getDimension(R.dimen.desktop_grid_margins);
        mDesktopIconHeight = (int)getResources().getDimension(R.dimen.altura_desktop_icon);
        mColumnsDesktop = getResources().getInteger(R.integer.num_columns_desktop);

        //GridWebShortcut desktop icons
/*
        gridWebShortcutAdapter = new WebShortcutAdapter(getActivity());
        mWebShortCutGrid = (GridView) view.findViewById(R.id.webcut_grid);
        mWebShortCutGrid.setAdapter(gridWebShortcutAdapter);
        mWebShortCutGrid.setOnItemClickListener(this);*/

        //GridApp desktop icons
        gridAdapter = new ShortcutAdapter(getActivity());
        gridAdapter = ((LauncherActivity) getActivity()).getGridDesktop();
        mFavoritesGrid = (GridView) view.findViewById(R.id.app_grid);
        mFavoritesGrid.setAdapter(gridAdapter);
        mFavoritesGrid.setOnItemClickListener(this);
        mFavoritesGrid.setOnItemSelectedListener(itemSelected);
        mFavoritesGrid.setOnFocusChangeListener(onAppgridSelecctionchange);

        appButton = (ImageView) view.findViewById(R.id.icon_drawer);
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
            }
        });

        notificationButton = (ImageView) view.findViewById(R.id.icon_notification);
        notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getNotificationLayout());
            }
        });

        currentLocale = getResources().getConfiguration().locale;
        timeTextView = (TextView) view.findViewById(R.id.clock);
        dateTextView = (TextView) view.findViewById(R.id.date);
        Clock clock = new Clock(getActivity());
        clock.AddClockTickListner(new Clock.OnClockTickListner() {

            @Override
            public void OnSecondTick(Time currentTime) {

            }

            @Override
            public void OnMinuteTick(Time currentTime) {
                updateClockWidget(currentTime);
            }
        });

        wifiIcon = (ImageView) view.findViewById(R.id.wifi_icon);
        ethernetIcon = (ImageView) view.findViewById(R.id.ethernet_icon);
        connectionIndicator = new ConnectionIndicator(getActivity(), wifiIcon, ethernetIcon);

        Log.d("-------------onCreateView() Desktop Fragment", Integer.toString((gridAdapter.getCount())));
        return view;

    }



    View.OnFocusChangeListener onAppgridSelecctionchange = new View.OnFocusChangeListener() {
        @Override
        public void onFocusChange(View v, boolean hasFocus) {
            if (hasFocus){
                //recuperamosFoco = true;
//                if (vistaAnterior != null && (vistaAnterior.isSelected()))
//                    seleccionarView(vistaAnterior);
//                seleccionarView(mAppsGrid.getSelectedView());
//                mAppsGrid.requestFocusFromTouch();
//                mAppsGrid.setSelection(1);
//                mAppsGrid.setItemChecked(1,true);


                seleccionarView(vistaAnterior);
            }
            else {
                deseleccionarView(vistaAnterior);
            }
        }
    };


    /*
    Fin metodos OnFocus
     */


    /*
    Scroll del grid
     */
        private void scrolleaGridView(int posicion, View v) {
            float posView = v.getY();

            if (posView < (mDesktopIconHeight)) {
                mFavoritesGrid.smoothScrollBy(-mDesktopIconHeight, 500);
            }

            if (posView > (mGridViewHeight - (mDesktopIconHeight * 1.5))) {
                mFavoritesGrid.smoothScrollBy(mDesktopIconHeight, 500);
            }
        }
        /***********************/

/*
    Estado de la actividad
 */
        /*@Override
        public void onStart(){
            super.onStart();
            gridAdapter = ((LauncherActivity) getActivity()).getGridDesktop();
        }*/

        @Override
        public void onActivityCreated(Bundle savedInstanceState) {

            super.onActivityCreated(savedInstanceState);
        }

        @Override
        public void onResume() {
        super.onResume();
        Time time = new Time();
        time.setToNow();
        updateClockWidget(time);
        connectionIndicator.update();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
        BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context arg0, Intent arg1) {
                connectionIndicator.update();
            }
        };
        getActivity().registerReceiver(connectivityReceiver, intentFilter);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    /*******************************/


    /*
        Metodos OnClik de un ShortCutFavorito
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ShortcutInfo shortcut = (ShortcutInfo) gridAdapter.getItem(i);
        startActivity(shortcut.getIntent());
    }


    /*
        Metodos OnFocus de un ShortCutFavorito
    */
    private View vistaAnterior;
    private boolean recuperamosFoco;
    AdapterView.OnItemSelectedListener itemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mGridViewHeight == 0) {
                mGridViewHeight = mFavoritesGrid.getHeight();
            }

            deseleccionarView(vistaAnterior);

            vistaAnterior =view;
            seleccionarView(view);

            scrolleaGridView(position, view);
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void deseleccionarView(View vista)
    {
        if(vista!=null) {
            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(vista,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    getResources().getColor(R.color.desktop_icon_background_selected),
                    getResources().getColor(R.color.desktop_icon_background));
            backgroundColorAnimator.setDuration(500);
            backgroundColorAnimator.start();
        }
    }

    private void seleccionarView(View vista)
    {
        if(vista!=null)
        {
            final ObjectAnimator backgroundColorAnimator = ObjectAnimator.ofObject(vista,
                    "backgroundColor",
                    new ArgbEvaluator(),
                    getResources().getColor(R.color.desktop_icon_background),
                    getResources().getColor(R.color.desktop_icon_background_selected));
            backgroundColorAnimator.setDuration(500);
            backgroundColorAnimator.start();
        }
    }

    /***************************************/

    /*
    Maneja accesos directos AÑADIR-QUITAR
     */
    public void addShortcut(final ShortcutInfo shortcutInfo) {
        Log.i("Add on desktop -->", shortcutInfo.getTitle());

        if (shortcutInfo instanceof WebPageInfo) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL(((WebPageInfo) shortcutInfo).getPageUrl().toString() + "/favicon.ico");
                        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                        connection.setDoInput(true);
                        connection.connect();

                        InputStream input = connection.getInputStream();
                        shortcutInfo.setBitmap(BitmapFactory.decodeStream(input));

                    } catch (IOException e) {
                        shortcutInfo.setBitmap(null);
                    }
                }
            });
            thread.start();

            gridAdapter.addItem(shortcutInfo);
            gridAdapter.notifyDataSetChanged();
            //((LauncherActivity) getActivity()).preferencesListadoApps.ActualizaListaApps(gridAdapter.getListInfo());
            //setGridAdapter(((LauncherActivity) getActivity()).getGridDesktop());

        } else {

            gridAdapter.addItem(shortcutInfo);
            gridAdapter.notifyDataSetChanged();
            //((LauncherActivity) getActivity()).preferencesListadoApps.ActualizaListaApps(gridAdapter.getListInfo());
            //setGridAdapter(((LauncherActivity) getActivity()).getGridDesktop());

        }
    }


    public void removeShortcut(ShortcutInfo shortcutInfo) {
        gridAdapter.removeItem(shortcutInfo);
        gridAdapter.notifyDataSetChanged();
    }

    /*
    public void clearShortcutsApps() {
        if (gridAdapter != null) {
            gridAdapter.clearItems();
            gridAdapter.notifyDataSetChanged();
            ((LauncherActivity) getActivity()).setsavedGridFav(gridAdapter);
        }
    }*/

//    public ImageView getAppButton() {
//        return appButton;
//    }

    public void FocusAppListButton() {
        appButton.requestFocus();
    }


    public void focusAppGrid() {
        if(mFavoritesGrid!=null){
            mFavoritesGrid.requestFocus();}
        View v = mFavoritesGrid.getSelectedView();
        if (v != null) v.requestFocus();
    }

    public boolean onKeyRightAndLeft( int key){
        int itemSelected = mFavoritesGrid.getSelectedItemPosition();
        int columns = mFavoritesGrid.getNumColumns();

        switch (key){
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                if (((itemSelected + 1 ) % columns) == 0){
                    ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (((itemSelected) % columns) == 0){
                    ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getNotificationLayout());
                }
                break;
        }






        return false;
    }


    public List<ShortcutInfo> getFavInfoList() {

        return gridAdapter.getListInfo();

    }

    public void setGridAdapter(ShortcutAdapter gridAda){
       // gridAdapter = new ShortcutAdapter(getActivity());
        Log.d("----setGridAdapter() DESKTOP: ",Integer.toString(gridAda.getCount()));
        if(this.gridAdapter!=null){
            this.gridAdapter.clearItems();
            //gridAdapter.setListInfo(((LauncherActivity) getActivity()).getGridDesktop().getListInfo());
            this.gridAdapter.setListInfo(gridAda.getListInfo());
            this.gridAdapter.notifyDataSetChanged();
        }
        else{
            this.gridAdapter = new ShortcutAdapter(getActivity());
            this.gridAdapter.setListInfo(gridAda.getListInfo());
           // gridAdapter.getCount();
            this.gridAdapter.notifyDataSetChanged();
        }
        Log.d("-------------setGridAdapter() Desktop Fragment", Integer.toString((this.gridAdapter.getCount())));
    }

   /*
    Wichet reloj
     */
    private void updateClockWidget(Time dateTime) {
        if (timeTextView != null) {
            timeTextView.setText(android.text.format.DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(dateTime.toMillis(true)).toString());
        }

        if (dateTextView != null) {
            dateTextView.setText(android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext()).format(dateTime.toMillis(true)).toString());
        }
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
