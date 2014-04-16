package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.text.format.Time;
import android.view.Gravity;
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
import com.energysistem.energylauncher.tvboxlauncher.ui.views.ExpandableHeightGridView;
import com.energysistem.energylauncher.tvboxlauncher.util.Clock;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Date;

/**
 * Created by vgt on 11/04/2014.
 */
public class DesktopFragment extends Fragment{

    private ExpandableHeightGridView mAppsGrid;
    private ShortcutAdapter gridAdapter;
    private ImageButton appButton;

    TextView clockTextView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        mAppsGrid = (ExpandableHeightGridView) view.findViewById(R.id.app_grid);
        gridAdapter = new ShortcutAdapter(getActivity());
        mAppsGrid.setAdapter(gridAdapter);
        //mAppsGrid.setExpanded(true);

        appButton = (ImageButton) view.findViewById(R.id.appButton);
        appButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((LauncherActivity)getActivity()).toggleDrawer(((LauncherActivity)getActivity()).getAppLayout());
            }
        });

        Time time = new Time();
        time.setToNow();
        clockTextView = (TextView) view.findViewById(R.id.clock);
        clockTextView.setText(DateFormat.format("kk:mm", time.toMillis(true)).toString());
        Clock clock = new Clock(getActivity());
        clock.AddClockTickListner(new Clock.OnClockTickListner() {

            @Override
            public void OnSecondTick(Time currentTime) {

            }

            @Override
            public void OnMinuteTick(Time currentTime) {
                clockTextView.setText(DateFormat.format("kk:mm", currentTime.toMillis(true)).toString());
            }
        });

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            //getLoaderManager().restartLoader(0, null, this);
        }
    }

//    @Override
//    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//        ShortcutInfo shortcut = (ShortcutInfo) gridAdapter.getItem(i);
//        startActivity(shortcut.getIntent());
//    }

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
                        shortcutInfo.iconBitmap = BitmapFactory.decodeStream(input);
                    } catch (IOException e) {
                        shortcutInfo.iconBitmap = null;
                    }
                }
            });
            thread.start();
        }
        gridAdapter.addItem(shortcutInfo);
        gridAdapter.notifyDataSetChanged();
    }

    public void removeShortcut(ShortcutInfo shortcutInfo) {
        gridAdapter.removeItem(shortcutInfo);
        gridAdapter.notifyDataSetChanged();
    }

    public void getShortcuts() {

    }

    public ImageButton getAppButton() {
        return appButton;
    }
}
