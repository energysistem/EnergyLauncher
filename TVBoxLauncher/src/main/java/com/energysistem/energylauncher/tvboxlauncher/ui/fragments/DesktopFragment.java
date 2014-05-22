package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

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
import java.util.Locale;

/**
 * Created by Vicente Giner Tendero on 11/04/2014.
 */
public class DesktopFragment extends Fragment implements AdapterView.OnItemClickListener {

    private GridView mAppsGrid;
    private ShortcutAdapter gridAdapter;
    private GridView mWebShortCutGrid;
    private ShortcutAdapter gridWebShortcutAdapter;
    private ImageButton appButton;
    private TextView timeTextView;
    private TextView dateTextView;
    private Locale currentLocale;
    private ImageView wifiIcon;
    private ImageView ethernetIcon;
    private ConnectionIndicator connectionIndicator;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        mAppsGrid = (GridView) view.findViewById(R.id.app_grid);
        mWebShortCutGrid = (GridView) view.findViewById(R.id.webcut_grid);

        gridAdapter = new ShortcutAdapter(getActivity());
        gridWebShortcutAdapter = new ShortcutAdapter(getActivity());

        mAppsGrid.setAdapter(gridAdapter);
        mAppsGrid.setOnItemClickListener(this);

        mWebShortCutGrid.setAdapter(gridWebShortcutAdapter);
        mWebShortCutGrid.setOnItemClickListener(this);

        appButton = (ImageButton) view.findViewById(R.id.appButton);
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LauncherActivity) getActivity()).toggleDrawer(((LauncherActivity) getActivity()).getAppLayout());
            }
        });
        appButton.requestFocus();

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
        return view;
    }

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

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ShortcutInfo shortcut = (ShortcutInfo) gridAdapter.getItem(i);
        startActivity(shortcut.getIntent());
    }

    /*
    Maneja accesos directos añadidos/quitados
     */
    public void addShortcut(final ShortcutInfo shortcutInfo) {
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

            gridWebShortcutAdapter.addItem(shortcutInfo);
            gridWebShortcutAdapter.notifyDataSetChanged();
        } else {
            gridAdapter.addItem(shortcutInfo);
            gridAdapter.notifyDataSetChanged();
        }

    }

    public void removeShortcut(ShortcutInfo shortcutInfo) {
        gridAdapter.removeItem(shortcutInfo);
        gridAdapter.notifyDataSetChanged();

//
//        final ShortcutInfo shortc = shortcutInfo;
//        View tile = mAppsGrid.getSelectedView();
//
//        final Animation animation = AnimationUtils.loadAnimation(tile.getContext(), R.anim.splashfadeout);
//        tile.startAnimation(animation);
//        Handler handle = new Handler();
//        handle.postDelayed(new Runnable() {
//
//            @Override
//            public void run() {
//                // TODO Auto-generated method stub
//                gridAdapter.removeItem(shortc);
//                gridAdapter.notifyDataSetChanged();
//                animation.cancel();
//            }
//        }, 1000);
    }

    public void removeShortcut(int webShortcutPos) {
        gridWebShortcutAdapter.removeItemPos(webShortcutPos);
        gridWebShortcutAdapter.notifyDataSetChanged();
    }

    public void clearShortcutsApps() {
        if (gridAdapter != null) {
            gridAdapter.clearItems();
            gridAdapter.notifyDataSetChanged();
        }
    }


    public ImageButton getAppButton() {
        return appButton;
    }

    public void FocusAppListButton() {
        appButton.requestFocus();
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

}
