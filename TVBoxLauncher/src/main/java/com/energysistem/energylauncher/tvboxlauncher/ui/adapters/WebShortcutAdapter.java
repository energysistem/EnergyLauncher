package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 29/04/2014.
 */
//public class WebShortcutAdapter extends {
public class WebShortcutAdapter extends ArrayAdapter<WebPageInfo>{

    private LayoutInflater mLayoutInflater;
    private Resources mResources;
    private View.OnClickListener onCkeckBoxClickListener;
    private boolean checkBoxSelected;

    /*
    private final Context context;
    private List<WebPageInfo> list;*/

    /* CFG */

    public WebShortcutAdapter(Context context, List<WebPageInfo> objects) {
        super(context, 0, objects);
       /* this.list = new ArrayList<WebPageInfo>();
        this.context = context;*/
        this.mLayoutInflater = LayoutInflater.from(context);
        mResources = context.getResources();
    }

/*
    public WebShortcutAdapter(Context context, int resource, List<WebPageInfo> lista)  {


        mResources = context.getResources();
        this.mLayoutInflater = LayoutInflater.from(context);
        this.list = lista;
        this.context= context;
    }
*/

    /*  ----------

    public WebShortcutAdapter(Context context, int resource,  ArrayList<WebPageInfo> lista ) {
       // super(context, resource, lista);

        mLayoutInflater = LayoutInflater.from(context);
        mResources = context.getResources();
        mLayoutResourceId = resource;
        list = lista;
    }


    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }
*/
    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.row_web_shortcut, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.webImage);
            holder.title = (TextView) view.findViewById(R.id.webTitulo);
            holder.url = (TextView) view.findViewById(R.id.webUrl);
            holder.frameLayout = (FrameLayout) view.findViewById(R.id.frame_checkboxWeb);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final WebPageInfo info = getItem(position);

        if (info.getBitmap() != null)
            holder.image.setImageDrawable(new BitmapDrawable(mResources, info.getBitmap()));
        holder.title.setText(info.getTitle());
        holder.url.setText(info.getPageUrl().toString());

        if (checkBoxSelected)
            holder.frameLayout.setVisibility(View.VISIBLE);
        else
            holder.frameLayout.setVisibility(View.INVISIBLE);

        Log.e("-------------AppAdapter", "----holder Time-----");
        holder.frameLayout.setBackgroundColor(getFrameCheckBoxView(info.checked));


        holder.frameLayout.setOnClickListener(this.onCkeckBoxClickListener);

        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclickListener", "framelayout " + position);
               info.checked = !info.checked;

               v.setBackgroundColor(getFrameCheckBoxView(info.checked));

                v.setId(position);
                onCkeckBoxClickListener.onClick(v);
            }
        });

        return view;
    }


    public int getFrameCheckBoxView(boolean checked){

        if (checked)
            return getContext().getResources().getColor(R.color.blue);
        else
            return getContext().getResources().getColor(R.color.black_overlay);
    }


    private int lastSelectedItem = 0;
    private int selectedItem = 0;
    public void setSelectedItem(int position) {
        if (selectedItem != position) {
            lastSelectedItem = selectedItem;
            selectedItem = position;
        }
    }


    public int getLastSelectedItem(){
        return lastSelectedItem;
    }

    public int getSelectedItem(){
        return selectedItem;
    }

    public void setSelectedCheckBoxMode(boolean valor){
        Log.d("onclickListener", "Seleccionados checkBoxes WEBSHORTCUT");
        checkBoxSelected = valor;
    }

    public boolean getModeCheckBoxSelection(){
        return checkBoxSelected;
    }

    public void setOnCheckBoxClickListener(final View.OnClickListener onClickListener) {
        this.onCkeckBoxClickListener = onClickListener;
    }


    class ViewHolder {
        ImageView image;
        TextView title;
        TextView url;

        FrameLayout frameLayout;
    }



}
