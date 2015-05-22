package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.graphics.Palette;
import android.text.format.Time;
import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.broadcastreceiver.TimeChangedReceiver;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.ShortcutAdapter;
import com.energysistem.energylauncher.tvboxlauncher.util.Clock;
import com.energysistem.energylauncher.tvboxlauncher.util.ConnectionIndicator;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
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
    //sadadas



    //Propiedades elementos gridview para el scroll
    int mGridViewHeight = 0;
    int mDesktopIconHeight;
    int mColumnsDesktop;
    int mMarginDesktopIcons;
    private boolean hasFocus;
    private Time actualTime = new Time();
    private boolean primeraVez;

    private ImageView lateralIzq;
    private ImageView lateralDer;
    private Animation outAnimationIzq;
    private Animation outAnimationDer;
    private float luminosidad;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        //Para detectar el primer boot del sistema
        primeraVez = true;
        //Margins
        mMarginDesktopIcons = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.desktop_grid_margins), getResources().getDisplayMetrics());
        mDesktopIconHeight = (int)TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, getResources().getDimension(R.dimen.altura_desktop_icon), getResources().getDisplayMetrics());
        mColumnsDesktop = getResources().getInteger(R.integer.num_columns_desktop);

        //GridWebShortcut desktop icons
/*
        gridWebShortcutAdapter = new WebShortcutAdapter(getActivity());
        mWebShortCutGrid = (GridView) view.findViewById(R.id.webcut_grid);
        mWebShortCutGrid.setAdapter(gridWebShortcutAdapter);
        mWebShortCutGrid.setOnItemClickListener(this);*/

        //GridApp desktop icons
        gridAdapter = new ShortcutAdapter(getActivity());
        //gridAdapter = ((LauncherActivity) getActivity()).getGridDesktop();
        mFavoritesGrid = (GridView) view.findViewById(R.id.app_grid);
        mFavoritesGrid.setAdapter(gridAdapter);
        mFavoritesGrid.setSmoothScrollbarEnabled(true);
        mFavoritesGrid.setOnItemClickListener(this);
        mFavoritesGrid.setOnItemSelectedListener(itemSelected);
        mFavoritesGrid.setOnFocusChangeListener(onAppgridSelecctionchange);


        /*appButton = (ImageView) view.findViewById(R.id.icon_drawer);
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
            }
        });*/

        //notificationButton = (ImageView) view.findViewById(R.id.icon_notification);
        /*notificationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getNotificationLayout());
            }
        });*/

        currentLocale = getResources().getConfiguration().locale;
        timeTextView = (TextView) view.findViewById(R.id.clock);
        dateTextView = (TextView) view.findViewById(R.id.date);

        lateralDer = (ImageView) view.findViewById(R.id.menu_der);
        lateralIzq = (ImageView) view.findViewById(R.id.menu_izq);

        Clock clock = new Clock(getActivity());
        clock.AddClockTickListner(new Clock.OnClockTickListner() {


            @Override
            public void OnSecondTick(Time currentTime) {
                ///Log.e("OnSecondTick",android.text.format.DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(currentTime.toMillis(true)).toString());
                //Log.e("Prueba","secondTick");

            }

            @Override
            public void OnMinuteTick(Time currentTime) {
                actualTime.setToNow();
                updateClockWidget(actualTime);
            }
        });

        wifiIcon = (ImageView) view.findViewById(R.id.wifi_icon);
        ethernetIcon = (ImageView) view.findViewById(R.id.ethernet_icon);
        connectionIndicator = new ConnectionIndicator(getActivity(), wifiIcon, ethernetIcon);


        lateralIzq.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getNotificationLayout());
            }
        });

        lateralDer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
            }
        });


        TextView devText = (TextView) view.findViewById(R.id.versionDev);
        try {
            devText.setText("Dev Version "+getActivity().getPackageManager().getPackageInfo(getActivity().getPackageName(), 0).versionCode);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        outAnimationIzq = AnimationUtils.loadAnimation(getActivity(),
                R.anim.left_end_animation);
        outAnimationIzq.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lateralIzq.setAlpha(0.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        outAnimationDer = AnimationUtils.loadAnimation(getActivity(),
                R.anim.right_end_animation);
        outAnimationDer.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                lateralDer.setAlpha(0.0f);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        lateralIzq.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if( event.getActionMasked()== MotionEvent.ACTION_HOVER_ENTER) {
                    v.setAlpha(1f);
                    v.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.left_start_animation));
                }
                else if(event.getActionMasked()== MotionEvent.ACTION_HOVER_EXIT && gridAdapter.getCount()!=0){
                    lateralIzq.startAnimation(outAnimationIzq);
                }

                return false;
            }
        });
        //asdas

        lateralDer.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                if( event.getActionMasked()== MotionEvent.ACTION_HOVER_ENTER) {
                    lateralDer.setAlpha(1f);
                    lateralDer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.right_start_animation));
                }
                else if(event.getActionMasked()== MotionEvent.ACTION_HOVER_EXIT && gridAdapter.getCount()!=0){
                    lateralDer.startAnimation(outAnimationDer);
                }

                return false;
            }
        });
        Bitmap bitmapWallpaper = ((BitmapDrawable)getActivity().getWallpaper()).getBitmap();

        Palette.generateAsync(bitmapWallpaper, 1, new Palette.PaletteAsyncListener() {
            public void onGenerated(Palette palette) {
                luminosidad = palette.getSwatches().get(0).getHsl()[2];

                if(luminosidad<0.5f) {
                    lateralIzq.setImageResource(R.drawable.lateral_bar_settings_white);
                    lateralDer.setImageResource(R.drawable.lateral_bar_menu_white);
                }
                else {
                    lateralIzq.setImageResource(R.drawable.lateral_bar_settings_dark);
                    lateralDer.setImageResource(R.drawable.lateral_bar_menu_dark);
                }

            }
        });

        Log.e("DesktopFragment","OnCreate()");
        mFavoritesGrid.requestFocus();




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

            if (posView < mDesktopIconHeight) {
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
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);



    }

    public BroadcastReceiver BR_TimeChangedreceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            actualTime.setToNow();
            updateClockWidget(actualTime);
        }
    };

    BroadcastReceiver connectivityReceiver = new BroadcastReceiver() {

        @Override
        public void onReceive(Context arg0, Intent arg1) {
            Time time = new Time();
            time.setToNow();
            updateClockWidget(time);
            connectionIndicator.update();
        }
    };

        @Override
        public void onResume() {


            actualTime.setToNow();
        updateClockWidget(actualTime);
        connectionIndicator.update();

            IntentFilter filterClockWidget = new IntentFilter();
            filterClockWidget.setPriority(2147483647);
            filterClockWidget.addAction(TimeChangedReceiver.INTENT);
        getActivity().registerReceiver(BR_TimeChangedreceiver, filterClockWidget);

            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
            intentFilter.addAction(WifiManager.RSSI_CHANGED_ACTION);
            getActivity().registerReceiver(connectivityReceiver, intentFilter);
            super.onResume();
    }




    @Override
    public void onPause() {


        actualTime.setToNow();
        updateClockWidget(actualTime);
        getActivity().unregisterReceiver(BR_TimeChangedreceiver);
        getActivity().unregisterReceiver(connectivityReceiver);
        super.onPause();
    }



    /*
        Metodos OnClik de un ShortCutFavorito
     */
    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ShortcutInfo shortcut = (ShortcutInfo) gridAdapter.getItem(i);
        startActivity(shortcut.getIntent());
    }
    private boolean izqActiva = false;
    private boolean derActiva = false;


    /*
        Metodos OnFocus de un ShortCutFavorito
    */
    private View vistaAnterior;
    private int posicionAnterior = -1;
    private boolean recuperamosFoco;
    AdapterView.OnItemSelectedListener itemSelected = new AdapterView.OnItemSelectedListener() {
        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
            if (mGridViewHeight == 0) {
                mGridViewHeight = mFavoritesGrid.getHeight();
            }




            if(position%(4)==0) {
                if(!izqActiva) {
                    lateralIzq.setAlpha(1f);
                    lateralIzq.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.left_start_animation));
                    izqActiva = true;
                }
            }
            else {
                if(izqActiva) {
                    izqActiva=false;
                    lateralIzq.startAnimation(outAnimationIzq);
                }
            }

            if(((position+1)%4==0) || (position == gridAdapter.getCount()-1 && position<4)) {
                if(!derActiva) {
                    derActiva = true;
                    lateralDer.setAlpha(1f);
                    lateralDer.startAnimation(AnimationUtils.loadAnimation(getActivity(),
                            R.anim.right_start_animation));
                }
            } else {
                if(derActiva) {
                    derActiva=false;

                    lateralDer.startAnimation(outAnimationDer);
                }
            }





            deseleccionarView(vistaAnterior);

            vistaAnterior = view;
            seleccionarView(view);

            if(posicionAnterior !=  position-1 && posicionAnterior != position+1)
                //scrolleaGridView(position, view);

            posicionAnterior = position;
        }
        @Override
        public void onNothingSelected(AdapterView<?> parent) {
        }
    };

    private void deseleccionarView(View v)
    {
        if(v == null)
            return;
        /*TransitionDrawable transition = (TransitionDrawable) vista.getBackground();
        transition.reverseTransition(500);*/
        ImageView iv = (ImageView) v.findViewById(R.id.backgroundCellSelected);
        iv.setVisibility(View.GONE);
        v.setScaleX(1);
        v.setScaleY(1);



    }

    private void seleccionarView(View v)
    {
        if(v == null)
            return;
        /*TransitionDrawable transition = (TransitionDrawable) vista.getBackground();
        transition.startTransition(150);*/
        ImageView iv = (ImageView) v.findViewById(R.id.backgroundCellSelected);
        iv.setVisibility(View.VISIBLE);
        v.setScaleX(1.07f);
        v.setScaleY(1.07f);
        //zoomCell(v);

    }


    /*
    Maneja accesos directos AÑADIR-QUITAR
     */
    public void addShortcut(final ShortcutInfo shortcutInfo) throws MalformedURLException {
        Log.i("Add on desktop -->", shortcutInfo.getTitle());

        if (shortcutInfo instanceof WebPageInfo) {

            URL url = new URL("http://www.google.com/s2/favicons?domain="+((WebPageInfo) shortcutInfo).getPageUrl());
            Picasso.with(getActivity()).load(url.toString()).into(new Target() {

                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    shortcutInfo.setBitmap(bitmap);



                }

                @Override
                public void onBitmapFailed(final Drawable errorDrawable) {
                    Log.d("TAG", "FAILED");
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                }
            });

                gridAdapter.addItem(shortcutInfo, getActivity());
                gridAdapter.notifyDataSetChanged();

            //((LauncherActivity) getActivity()).preferencesListadoApps.ActualizaListaApps(gridAdapter.getListInfo());
            //setGridAdapter(((LauncherActivity) getActivity()).getGridDesktop());
        } else {

            try {
                gridAdapter.addItem(shortcutInfo,getActivity());
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
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
            mFavoritesGrid.requestFocus();
            mFavoritesGrid.setFocusable(true);
        }
        View v = mFavoritesGrid.getSelectedView();
        if (v != null) v.requestFocus();
    }

    public boolean onKeyRightAndLeft( int key){

        int itemSelected = mFavoritesGrid.getSelectedItemPosition();
        int columns = mFavoritesGrid.getNumColumns();
        switch (key){
            case KeyEvent.KEYCODE_DPAD_RIGHT:
                Log.e("DPAD_RIGHT","Right "+mFavoritesGrid.getSelectedItemPosition());
                if (((itemSelected + 1 ) % columns) == 0 || (gridAdapter.getCount()==1 || gridAdapter.getCount()==0)){
                    ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
                } else if (itemSelected == gridAdapter.getCount()-1 ) {
                    if(itemSelected+1>4) {
                        int newPosition = itemSelected - ((itemSelected+1)%4);
                        mFavoritesGrid.setSelection(newPosition);
                       // mFavoritesGrid.getSelectedView().requestFocus();
                    }
                    else
                        ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
                }
                break;
            case KeyEvent.KEYCODE_DPAD_LEFT:
                if (((itemSelected) % columns) == 0 || itemSelected==-1){

                    ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getNotificationLayout());
                    ((LauncherActivity) getActivity()).getNotificationLayout().requestFocus();
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
    }

   /*
    Wichet reloj
     */
   public void updateClockWidget(Time dateTime) {
        if (timeTextView != null) {
            //timeTextView.setText(android.text.format.DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(dateTime.toMillis(true)).toString());
            int hour = Integer.parseInt(dateTime.format("%H"));
            String minutes = dateTime.format("%M");
            //Este código no me representa. Negaré su existencia
            if (hour <= 9)
                timeTextView.setText(" " + hour + ":" + minutes);

            else
                timeTextView.setText(hour+":"+minutes);
        }

        if (dateTextView != null) {
            dateTextView.setText(android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext()).format(dateTime.toMillis(true)).toString());
        }
    }

    public void setHasFocus(boolean hasFocus) {
        this.hasFocus = hasFocus;
    }
}
