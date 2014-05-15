package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import java.text.DateFormat;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.ShortcutAdapter;
import com.energysistem.energylauncher.tvboxlauncher.util.Clock;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Locale;

/**
 * Created by Vicente Giner Tendero on 11/04/2014.
 */
public class DesktopFragment extends Fragment implements AdapterView.OnItemClickListener{

    private GridView mAppsGrid;
    private ShortcutAdapter gridAdapter;
    private GridView mWebShortCutGrid;
    private ShortcutAdapter gridWebShortcutAdapter;
    private ImageButton appButton;
    private TextView timeTextView;
    private TextView dateTextView;
    private Locale currentLocale;

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
                ((LauncherActivity)getActivity()).toggleDrawer(((LauncherActivity)getActivity()).getAppLayout());
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
                timeTextView.setText(android.text.format.DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(currentTime.toMillis(true)).toString());
                dateTextView.setText(android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext()).format(currentTime.toMillis(true)).toString());
            }
        });

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
        timeTextView.setText(android.text.format.DateFormat.getTimeFormat(getActivity().getApplicationContext()).format(time.toMillis(true)).toString());
        dateTextView.setText(android.text.format.DateFormat.getDateFormat(getActivity().getApplicationContext()).format(time.toMillis(true)).toString());
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ShortcutInfo shortcut = (ShortcutInfo) gridAdapter.getItem(i);
        startActivity(shortcut.getIntent());
    }

    public void addShortcut(final ShortcutInfo shortcutInfo) {
        if(shortcutInfo instanceof WebPageInfo) {
            Thread thread = new Thread(new Runnable() {
                public void run() {
                    try {
                        URL url = new URL(((WebPageInfo) shortcutInfo).getPageUrl().toString()+"/favicon.ico");
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
        }
        else {
            gridAdapter.addItem(shortcutInfo);
            gridAdapter.notifyDataSetChanged();
        }

    }

    public void removeShortcut(ShortcutInfo shortcutInfo) {
        gridAdapter.removeItem(shortcutInfo);
        gridAdapter.notifyDataSetChanged();
    }

    public void removeShortcut(int webShortcutPos) {
        gridWebShortcutAdapter.removeItemPos(webShortcutPos);
        gridWebShortcutAdapter.notifyDataSetChanged();
    }

    public void clearShortcutsApps(){
        if (gridAdapter != null){
            gridAdapter.clearItems();
            gridAdapter.notifyDataSetChanged();
        }
    }

    public ImageButton getAppButton() {
        return appButton;
    }

    public void FocusAppListButton(){
        appButton.requestFocus();
    }


}
