package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.view.LayoutInflater;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vicente on 12/04/2014.
 */
public class ShortcutAdapter extends BaseAdapter  {

    List<ShortcutInfo> data;
    Context context;
    LayoutInflater inflater;

    public ShortcutAdapter(Context context) {
        this.data = new ArrayList<ShortcutInfo>();
        this.context = context;
    //if(context==null){context=getApplicationContext()}
        this.inflater = LayoutInflater.from(context);
    }

    public ShortcutAdapter(Context context, List<ShortcutInfo> data) {
        this.data = data;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int i) {
        return data.get(i);
    }

    public int getItemPosition(ShortcutInfo shortcutInfo) {
        for(int i = 0; i < data.size(); i++) {
            if(data.get(i) == shortcutInfo) {
                return i;
            }
        }
        return -1;
    }

    public void addItem(ShortcutInfo i) {
        if (i instanceof WebPageInfo) {

            data.add(((WebPageInfo) i).getPosi()-1, i);
        }
        else if(i instanceof AppInfo) {
            Log.e("metemos",i.toString());
            data.add(i);
        }
    }

    public void addItemPos(ShortcutInfo i) {
        if (i instanceof WebPageInfo)
            data.set(((WebPageInfo) i).getPosi(),i);
    }

    public void removeItem(ShortcutInfo i) {
        data.remove(i);
    }

    public void clearItems(){
        data.clear();
    }


    public void removeItemPos(int i){
        data.remove(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {

        View view = convertView;
        final ShortcutInfo shortcut = data.get(i);
        final ViewHolder holder;
        if (view == null) {
            view = inflater.inflate(R.layout.cell_shortcut, null);
            holder = new ViewHolder();

            holder.icon = (ImageView) view.findViewById(R.id.icon);
            holder.title = (TextView) view.findViewById(R.id.title);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        holder.icon.setImageBitmap(shortcut.getBitmap());
        holder.title.setText(shortcut.getTitle());

        return view;
    }


    public List<ShortcutInfo> getListInfo(){
           return this.data;
       }

    public void setListInfo(List<ShortcutInfo> listaN){ this.data=listaN;}


    class ViewHolder {
        ImageView icon;
        TextView title;
    }

}
