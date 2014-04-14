package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicente on 12/04/2014.
 */
public class ShortcutAdapter extends BaseAdapter {

    List<ShortcutInfo> data;
    Context context;
    LayoutInflater inflater;

    public ShortcutAdapter(Context context) {
        this.data = new ArrayList<ShortcutInfo>();
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    public ShortcutAdapter(Context context, List<ShortcutInfo> data) {
        this.data = data;
        this.context = context;
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    public void addItem(ShortcutInfo i) {
        data.add(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {

        ShortcutInfo shortcut = data.get(i);

        view = inflater.inflate(R.layout.cell_shortcut, null);

        RelativeLayout container = (RelativeLayout) view.findViewById(R.id.container);
        ImageView icon = (ImageView) view.findViewById(R.id.icon);
        TextView title = (TextView) view.findViewById(R.id.title);

        container.setBackgroundColor(shortcut.getBackgroundColor());
        icon.setImageBitmap(shortcut.iconBitmap);
        title.setText(shortcut.title);

        return view;
    }
}