package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.NotificationItem;

import java.util.List;


/**  ListAdapter
 * Created by mfc on 30/04/14.
 */

public class NotificationAdapter extends ArrayAdapter<NotificationItem> {

    Context mContext;
    int layoutResourceId;
    List<NotificationItem> data = null;

    public NotificationAdapter(Context mContext, int layoutResourceId, List<NotificationItem> data) {
        super(mContext,layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View NotificationItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        NotificationItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView iconViewIcon = (ImageView) NotificationItem.findViewById(R.id.listIcon);
        TextView titleViewName = (TextView) NotificationItem.findViewById(R.id.listTitle);
        TextView textViewName = (TextView) NotificationItem.findViewById(R.id.listText);
        ImageView smallIconViewIcon = (ImageView) NotificationItem.findViewById(R.id.listSmallIcon);
        TextView infoViewName = (TextView) NotificationItem.findViewById(R.id.listInfo);
        TextView dateViewName = (TextView) NotificationItem.findViewById(R.id.listDate);

        NotificationItem folder = data.get(position);


        iconViewIcon.setImageResource(folder.icon);
        titleViewName.setText(folder.title);
        textViewName.setText(folder.text);
        smallIconViewIcon.setImageResource(folder.smallIcon);
        infoViewName.setText(folder.info);
        dateViewName.setText(folder.date);

        return NotificationItem;
    }

}