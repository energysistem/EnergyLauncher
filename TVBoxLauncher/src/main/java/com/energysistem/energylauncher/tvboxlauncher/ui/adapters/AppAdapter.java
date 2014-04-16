package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Message;
import android.util.Log;
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

import java.util.EventListener;
import java.util.List;

/**
 * Created by Esteban on 13/04/14.
 */
public class AppAdapter extends ArrayAdapter<AppInfo> {

    private LayoutInflater mLayoutInflater;
    private Resources mResources;
    private View.OnClickListener onCkeckBoxClickListener;


    public AppAdapter(Context context, List<AppInfo> objects) {
        super(context, 0, objects);
        mLayoutInflater = LayoutInflater.from(context);
        mResources = context.getResources();
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.row_app, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.image_app);
            holder.title = (TextView) view.findViewById(R.id.title_app);
            holder.frameLayout = (FrameLayout) view.findViewById(R.id.frame_checkbox);
            //holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxApp);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppInfo info = getItem(position);

        holder.image.setImageDrawable(new BitmapDrawable(mResources, info.iconBitmap));
        holder.title.setText(info.title);

        if (info.checked)
            holder.frameLayout.setBackgroundColor(Color.RED);
        else
            holder.frameLayout.setBackgroundColor(Color.BLACK);

        holder.frameLayout.setOnClickListener(this.onCkeckBoxClickListener);

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclickListener", "framelayout " + position);
                info.checked = !info.checked;

                if (info.checked)
                    v.setBackgroundColor(Color.RED);
                else
                    v.setBackgroundColor(Color.BLACK);

                v.setId(position);
                onCkeckBoxClickListener.onClick(v);
            }
        });
        return view;
    }




    public void setOnCheckBoxClickListener(final View.OnClickListener onClickListener) {
        this.onCkeckBoxClickListener = onClickListener;
    }


    class ViewHolder {
        ImageView image;
        TextView title;
        FrameLayout frameLayout;

    }
}