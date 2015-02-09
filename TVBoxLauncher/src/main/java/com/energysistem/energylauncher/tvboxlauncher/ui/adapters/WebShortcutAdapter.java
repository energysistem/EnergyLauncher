package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
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
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URL;
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

        final WebPageInfo info = getItem(position);



        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.row_web_shortcut, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.icon_image_view);
            holder.title = (TextView) view.findViewById(R.id.webTitulo);
            holder.url = (TextView) view.findViewById(R.id.webUrl);
            holder.checkBox = (CheckBox) view.findViewById(R.id.frame_checkboxWeb);
            holder.arrow = (FrameLayout) view.findViewById(R.id.arrow_image_view);
            holder.arrow2 = (FrameLayout) view.findViewById(R.id.arrow2_image_view);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }




        if (info.getBitmap() != null) {
            holder.image.setImageDrawable(new BitmapDrawable(mResources, info.getBitmap()));
        }
        holder.title.setText(info.getTitle());
        holder.url.setText(info.getPageUrl().toString());

        if (checkBoxSelected) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.INVISIBLE);
            holder.arrow2.setVisibility(View.VISIBLE);
        } else {
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.arrow2.setVisibility(View.INVISIBLE);
        }

        Log.e("-------------AppAdapter", "----holder Time-----");
        holder.checkBox.setChecked(info.checked);


        holder.checkBox.setOnClickListener(this.onCkeckBoxClickListener);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i("onclickListener", "framelayout " + position);
               info.checked = !info.checked;

               v.setBackgroundColor(getFrameCheckBoxView(info.checked));

                v.setId(position);
                onCkeckBoxClickListener.onClick(v);
            }
        });

        URL url = null;
        try {
            url = new URL("http://www.google.com/s2/favicons?domain="+((WebPageInfo) info).getPageUrl());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        Picasso.with(getContext()).load(url.toString()).into(new Target() {

            @Override
            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {

                holder.image.setImageBitmap(bitmap);


            }

            @Override
            public void onBitmapFailed(final Drawable errorDrawable) {
                Log.d("TAG", "FAILED");
            }

            @Override
            public void onPrepareLoad(final Drawable placeHolderDrawable) {
                holder.image.setImageResource(R.drawable.ic_launcher);
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
        CheckBox checkBox;
        FrameLayout arrow;
        FrameLayout arrow2;
    }



}
