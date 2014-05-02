package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

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

        ImageView imageViewIcon = (ImageView) NotificationItem.findViewById(R.id.listImage);
        TextView textViewName = (TextView) NotificationItem.findViewById(R.id.listText);
        TextView subtextViewName = (TextView) NotificationItem.findViewById(R.id.listSubText);

        NotificationItem folder = data.get(position);


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.text);
        subtextViewName.setText(folder.subText);

        return NotificationItem;
    }

}