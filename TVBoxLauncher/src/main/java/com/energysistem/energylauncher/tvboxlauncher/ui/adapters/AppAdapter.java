package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
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

import java.util.List;

/**
 * Created by Esteban on 13/04/14.
 */
public class AppAdapter extends ArrayAdapter<AppInfo> {

    private LayoutInflater mLayoutInflater;
    private Resources mResources;
    private View.OnClickListener onCkeckBoxClickListener;
    private boolean checkBoxSelected;


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

            holder.image = (ImageView) view.findViewById(R.id.icon_image_view);
            holder.title = (TextView) view.findViewById(R.id.title_text_view);
            holder.checkBox = (CheckBox) view.findViewById(R.id.frame_checkbox);
            holder.arrow = (FrameLayout) view.findViewById(R.id.arrow_image_view);
            holder.arrow2 = (FrameLayout) view.findViewById(R.id.arrow2_image_view);
            //holder.checkBox = (CheckBox) view.findViewById(R.id.checkBoxApp);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final AppInfo info = getItem(position);

        holder.image.setImageDrawable(new BitmapDrawable(mResources, info.getBitmap()));
        holder.title.setText(info.getTitle());
        holder.arrow.setBackgroundResource(R.drawable.row_selector_app_arrow);
        holder.arrow2.setBackgroundResource(R.drawable.row_selector_app_arrow2);

        if (checkBoxSelected) {
            holder.checkBox.setVisibility(View.VISIBLE);
            holder.arrow.setVisibility(View.INVISIBLE);
            holder.arrow2.setVisibility(View.VISIBLE);
        }
        else {
            holder.checkBox.setVisibility(View.INVISIBLE);
            holder.arrow.setVisibility(View.VISIBLE);
            holder.arrow2.setVisibility(View.INVISIBLE);
        }

        //Log.e("-------------AppAdapter", "----holder Time-----");
        holder.checkBox.setChecked(info.checked);


       /* holder.checkBox.setOnClickListener(this.onCkeckBoxClickListener);

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // Log.i("onclickListener", "framelayout " + position);
                info.checked = !info.checked;
               // Log.e("-------------AppAdapter", "----ClikListener-----");
                v.setBackgroundColor(getFrameCheckBoxView(info.checked));
                v.setId(position);
                onCkeckBoxClickListener.onClick(v);
            }
        });*/

        return view;
    }


    public int getFrameCheckBoxView(boolean checked){

        if (checked){
            //Log.e("-------------updateView()", "-----AZULANDOO-----");
             return getContext().getResources().getColor(R.color.blue);}
        else{
            //Log.e("-------------updateView()", "-----NEGRANDOOO-----");
            return getContext().getResources().getColor(R.color.black_overlay);}
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
       // Log.d("onclickListener", "Seleccionados checkBoxes");
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
        CheckBox checkBox;
        FrameLayout arrow;
        FrameLayout arrow2;
    }
}