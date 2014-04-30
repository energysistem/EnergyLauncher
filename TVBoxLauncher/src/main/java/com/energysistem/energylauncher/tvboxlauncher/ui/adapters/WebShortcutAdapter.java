package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.BitmapDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;

import java.util.ArrayList;

/**
 * Created by emg on 29/04/2014.
 */
public class WebShortcutAdapter extends ArrayAdapter<WebPageInfo>{

    private LayoutInflater mLayoutInflater;
    private Resources mResources;
    private View.OnClickListener onListItemClickListener;
    private Context mContext;
    private int mLayoutResourceId;
    private ArrayList<WebPageInfo> list;


    public WebShortcutAdapter(Context context, int resource,  ArrayList<WebPageInfo> lista ) {
        super(context, resource, lista);

        mLayoutInflater = LayoutInflater.from(context);
        mResources = context.getResources();
        mLayoutResourceId = resource;
        list = lista;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;
        final ViewHolder holder;
        if (view == null) {
            view = mLayoutInflater.inflate(R.layout.row_web_shortcut, parent, false);
            holder = new ViewHolder();

            holder.image = (ImageView) view.findViewById(R.id.webImage);
            holder.title = (TextView) view.findViewById(R.id.webTitulo);
            holder.url = (TextView) view.findViewById(R.id.webUrl);

            view.setTag(holder);
        } else {
            holder = (ViewHolder) view.getTag();
        }

        final WebPageInfo info = getItem(position);

        if (info.getBitmap() != null)
            holder.image.setImageDrawable(new BitmapDrawable(mResources, info.getBitmap()));
        holder.title.setText(info.getTitle());
        holder.url.setText(info.getPageUrl().toString());


//        holder.frameLayout.setOnClickListener(this.onCkeckBoxClickListener);
//
//        holder.frameLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Log.i("onclickListener", "framelayout " + position);
//                info.checked = !info.checked;
//
//                v.setBackgroundColor(getFrameCheckBoxView(info.checked, false));
//
//                v.setId(position);
//                onCkeckBoxClickListener.onClick(v);
//            }
//        });

        return view;
    }


    class ViewHolder {
        ImageView image;
        TextView title;
        TextView url;

    }

}
