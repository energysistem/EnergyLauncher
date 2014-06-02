package com.energysistem.energylauncher.tvboxlauncher.util;

import android.annotation.TargetApi;
import android.app.Notification;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.service.notification.NotificationListenerService;
import android.service.notification.StatusBarNotification;
import android.util.Log;


import com.energysistem.energylauncher.tvboxlauncher.modelo.NotificationItem;


/**  NLService
 * Created by mfc on 22/04/14.
 */

@TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
public class NLService extends NotificationListenerService {
    private NLServiceReceiver nlservicereciver;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.energysistem.energylauncher.tvboxlauncher.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        updateNotif();
    }

    @TargetApi(Build.VERSION_CODES.KITKAT)
    private void postNotification(StatusBarNotification sbn){
        int icon = sbn.getNotification().extras.getInt(Notification.EXTRA_SMALL_ICON);
        String title = (String) sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE);
        String text = (String) sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT);
        PendingIntent intent = sbn.getNotification().contentIntent;

        NotificationItem.drawerItem.add(new NotificationItem(icon, title, text, intent));

    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN_MR2)
    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        updateNotif();
    }

    private void updateNotif(){
        //limpiamos el ArrayList de las notificaciones
        NotificationItem.drawerItem.clear();

        //Tratamos todas las notificaciones de la statusBar
        for (StatusBarNotification sbn : NLService.this.getActiveNotifications())
            postNotification(sbn);

        Intent i = new  Intent("com.energysistem.energylauncher.tvboxlauncher.ui.fragments.NOTIFICATION_LISTENER_EXAMPLE");
        i.putExtra("notification_event_app","New");
        sendBroadcast(i);
    }


    class NLServiceReceiver extends BroadcastReceiver{

        @TargetApi(Build.VERSION_CODES.KITKAT)
        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                NLService.this.updateNotif();
            }

        }
    }

}