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
import com.energysistem.energylauncher.tvboxlauncher.modelo.BasicImgText;

/**
 * Created by MFC on 14/04/2014.
 */
public class BasicITAdapter extends ArrayAdapter<BasicImgText> {
    Context mContext;
    int layoutResourceId;
    BasicImgText data[] = null;

    public BasicITAdapter(Context mContext, int layoutResourceId, BasicImgText[] data) {

        super(mContext, layoutResourceId, data);
        this.layoutResourceId = layoutResourceId;
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.icon_image_view);
        TextView textViewName = (TextView) listItem.findViewById(R.id.title_text_view);

        BasicImgText folder = data[position];


        imageViewIcon.setImageResource(folder.icon);
        textViewName.setText(folder.name);

        return listItem;
    }
}
