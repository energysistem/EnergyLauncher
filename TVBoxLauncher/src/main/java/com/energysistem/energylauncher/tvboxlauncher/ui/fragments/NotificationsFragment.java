package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.NotificationItem;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.NotificationAdapter;

import java.util.ArrayList;

/**
 * Created by emg on 22/04/2014.
 */

public class NotificationsFragment extends Fragment {

    private ListView mDrawerList;
    private NotificationAdapter adapter;
    private NotificationReceiver nReceiver;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_notifications, container, false);
        super.onCreate(savedInstanceState);
        nReceiver = new NotificationReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.energysistem.energylauncher.tvboxlauncher.ui.fragments.NOTIFICATION_LISTENER_EXAMPLE");
        getActivity().registerReceiver(nReceiver,filter);

        //Lista
        mDrawerList = (ListView) view.findViewById(R.id.listViewNotif);
        NotificationItem.drawerItem = new ArrayList<NotificationItem>();

        /*for(int i = 1; i<11;i++){

            String title = "Prueba " + i;
            String text = "Estoy haciendo una prueba "+i;
            NotificationItem.drawerItem.add(new NotificationItem(0, title, text));
        }*/

        adapter = new NotificationAdapter(view.getContext(),R.layout.row_notification, NotificationItem.drawerItem);
        mDrawerList.setAdapter(adapter);
        listNotify();

        return view;
    }

    private void clearNotify(){

        Intent i = new Intent("com.energy.pruebanorificacion.app.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        i.putExtra("command", "clearall");
        getActivity().sendBroadcast(i);

        Intent i2 = new Intent("com.energy.pruebanorificacion.app.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        i2.putExtra("command", "list");
        getActivity().sendBroadcast(i2);
    }

    private void listNotify(){
        Intent i = new Intent("com.energy.pruebanorificacion.app.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        i.putExtra("command","list");
        getActivity().sendBroadcast(i);
    }
    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {

            int icon = intent.getIntExtra("notification_event_Icon", 0);
            String title = intent.getStringExtra("notification_event_Title");
            String text = intent.getStringExtra("notification_event_Text");
            String info =intent.getStringExtra("notification_event_Text");
            int smallIcon = intent.getIntExtra("notification_event_Icon", 0);
            String date =intent.getStringExtra("notification_event_Text");
            NotificationItem.drawerItem.add(new NotificationItem(icon,title,text,info,smallIcon,date));
            adapter.notifyDataSetChanged();

        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            //getLoaderManager().restartLoader(0, null, this);
        }
    }

    public void onKeyRightAndLeft(int key) {

    }

    public void onKeyUpAndDown(int key) {

    }

    public void setFocus() {
        mDrawerList.requestFocus();
    }

}