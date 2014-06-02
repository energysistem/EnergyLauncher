package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.annotation.TargetApi;
import android.app.Fragment;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.NotificationItem;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.NotificationAdapter;

import java.util.ArrayList;

/**
 * Created by emg on 22/04/2014.
 */

@TargetApi(Build.VERSION_CODES.HONEYCOMB)
public class NotificationsFragment extends Fragment implements AdapterView.OnItemClickListener {
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

        adapter = new NotificationAdapter(view.getContext(),R.layout.row_notification, NotificationItem.drawerItem);
        mDrawerList.setAdapter(adapter);
        mDrawerList.setOnItemClickListener(this);


        listNotify();
        return view;
    }



    private void clearNotify(){

        Intent i = new Intent("com.energysistem.energylauncher.tvboxlauncher.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        i.putExtra("command", "clearall");
        getActivity().sendBroadcast(i);

        Intent i2 = new Intent("com.energysistem.energylauncher.tvboxlauncher.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        i2.putExtra("command", "list");
        getActivity().sendBroadcast(i2);
    }

    private void listNotify(){
        Intent i = new Intent("com.energysistem.energylauncher.tvboxlauncher.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        i.putExtra("command","list");
        getActivity().sendBroadcast(i);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        try {
            NotificationItem.drawerItem.get(position).intent.send();

        } catch (PendingIntent.CanceledException e) {
        }
    }

    class NotificationReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }
}