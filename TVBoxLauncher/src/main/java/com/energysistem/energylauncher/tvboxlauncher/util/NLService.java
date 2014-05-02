package com.energysistem.energylauncher.tvboxlauncher.util;

import android.annotation.TargetApi;
import android.app.Notification;
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

public class NLService extends NotificationListenerService {

    private String TAG = this.getClass().getSimpleName();
    private NLServiceReceiver nlservicereciver;

    @Override
    public void onCreate() {
        super.onCreate();
        nlservicereciver = new NLServiceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("com.energysistem.energylauncher.tvboxlauncher.util.NOTIFICATION_LISTENER_SERVICE_EXAMPLE");
        registerReceiver(nlservicereciver,filter);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        unregisterReceiver(nlservicereciver);
    }


    @Override
    public void onNotificationPosted(StatusBarNotification sbn) {
        Log.i(TAG,"**********  onNotificationPosted");
        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText + "t" + sbn.getPackageName());
        Intent i = new  Intent("com.energysistem.energylauncher.tvboxlauncher.util.NOTIFICATION_LISTENER_EXAMPLE");

        //Al recibir una notifiación la tratamos
        i.putExtra("notification_event_Icon",sbn.getNotification().extras.getInt(Notification.EXTRA_SMALL_ICON));
        i.putExtra("notification_event_Title",sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE));
        i.putExtra("notification_event_Text",sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT));
        sendBroadcast(i);

    }

    @Override
    public void onNotificationRemoved(StatusBarNotification sbn) {
        Log.i(TAG,"********** onNOtificationRemoved");
        Log.i(TAG,"ID :" + sbn.getId() + "t" + sbn.getNotification().tickerText +"t" + sbn.getPackageName());

    }

    class NLServiceReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if(intent.getStringExtra("command").equals("clearall")){
                NLService.this.cancelAllNotifications();
            }
            else if(intent.getStringExtra("command").equals("list")){
                //limpiamos el ArrayList de las notificaciones
                NotificationItem.drawerItem.clear();

                //Tratamos todas las notificaciones de la statusBar
                for (StatusBarNotification sbn : NLService.this.getActiveNotifications()) {
                    Intent i = new  Intent("com.energysistem.energylauncher.tvboxlauncher.util.NOTIFICATION_LISTENER_EXAMPLE");
                    i.putExtra("notification_event_Icon",sbn.getNotification().extras.getInt(Notification.EXTRA_SMALL_ICON));
                    i.putExtra("notification_event_Title",sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TITLE));
                    i.putExtra("notification_event_Text",sbn.getNotification().extras.getCharSequence(Notification.EXTRA_TEXT));
                    sendBroadcast(i);}

            }

        }
    }

}