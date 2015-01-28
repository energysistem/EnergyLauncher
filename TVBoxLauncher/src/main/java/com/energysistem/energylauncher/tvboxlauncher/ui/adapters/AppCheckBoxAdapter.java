package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;

import java.util.List;

/**
 * Created by emg on 16/04/2014.
 */
public class AppCheckBoxAdapter extends ArrayAdapter {

    private LayoutInflater mLayoutInflater;


    public AppCheckBoxAdapter(Context context, List<AppInfo> listaApps) {
        super(context, 0, listaApps);

        mLayoutInflater = LayoutInflater.from(context);

    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            v = mLayoutInflater.inflate(R.layout.row_app_checkbox, parent, false);
            holder = new ViewHolder();

            //holder.frame = (FrameLayout) v.findViewById(R.id.frame_checkbox);
            holder.checkBox = (CheckBox) v.findViewById(R.id.checkBoxApp);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final AppInfo info = (AppInfo) getItem(position);

        //holder.frame.setBackground(colorcolor);
        holder.checkBox.setChecked(info.checked);

        return v;
    }


    class ViewHolder {
        FrameLayout frame;
        CheckBox checkBox;
    }

}
