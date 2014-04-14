package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;

import java.util.List;

/**
 * Created by Esteban on 13/04/14.
 */
public class AppAdapter extends ArrayAdapter<AppInfo> {

    private LayoutInflater mLayoutInflater;
    private Resources mResources;
    private Boolean mShowCheckBox = false;

    public AppAdapter(Context context, List<AppInfo> objects, Boolean showCheckBox) {
        super(context, 0, objects);
        mLayoutInflater = LayoutInflater.from(context);
        mResources = context.getResources();
        mShowCheckBox = showCheckBox;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View v = convertView;
        ViewHolder holder;
        if (v == null) {
            v = mLayoutInflater.inflate(R.layout.row_app, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) v.findViewById(R.id.image_app);
            holder.title = (TextView) v.findViewById(R.id.title_app);
            holder.checkBox = (CheckBox) v.findViewById(R.id.checkBoxApp);

            v.setTag(holder);
        } else {
            holder = (ViewHolder) v.getTag();
        }

        final AppInfo info = getItem(position);

        holder.image.setImageDrawable(new BitmapDrawable(mResources, info.iconBitmap));
        holder.title.setText(info.title);

        holder.checkBox.setChecked(info.checked);
        if (mShowCheckBox){
            holder.checkBox.setVisibility(View.VISIBLE);
        }
        else{
            holder.checkBox.setVisibility(View.INVISIBLE);
        }

        return v;
    }

    class ViewHolder {
        ImageView image;
        TextView title;
        CheckBox checkBox;
    }
}