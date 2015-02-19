package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
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
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URL;
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

    public void addItem(final ShortcutInfo app, final Context context) throws MalformedURLException {
        Log.e("Entramos en addItem","ShortcutAdapter");
        //
        if (app instanceof WebPageInfo) {
            boolean contiene = false;
            for(int i = 0; i < data.size(); i++)
            {
                Log.i((data.get(i)).getTitle(),(app).getTitle());
                if( ( data.get(i)).getTitle().equals((app).getTitle()) )
                {
                    contiene = true;
                    Log.e("ContieneEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE","");
                }
            }

            if(!contiene) {
                int num;
                if (data.size() < ((WebPageInfo) app).getPosi() - 1) {
                    Log.e("TAG","ENTRAMOS->"+((WebPageInfo) app).getPageUrl());
                    num=data.size()+1;
                    data.add(app);
                } else {
                    Log.e("TAG","ENTRAMOS->"+((WebPageInfo) app).getPageUrl());
                    num=data.size()+1;
                    data.add(((WebPageInfo) app).getPosi() - 1, app);
                }

            }
        }
        else if(app instanceof AppInfo) {
            boolean contiene = false;
            for(int i = 0; i < data.size(); i++)
            {
                if(data.get(i) instanceof AppInfo) {
                    Log.i((data.get(i)).getTitle(), (app).getTitle());
                    if (((AppInfo) data.get(i)).getPackageName().equals(((AppInfo) app).getPackageName())) {
                        contiene = true;
                        Log.e("ContieneEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", "");
                    }
                }
            }
            Log.e("metemos",app.toString());
            if(!contiene)
                data.add(app);

            contiene = false;

        }

    }


    public Bitmap combineImages(Bitmap c, Bitmap browserBmp) { // can add a 3rd parameter 'String loc' if you want to save the new image - left some code to do that at the bottom
        Bitmap cs = null;

        int width, height = 0;

       /* if(c.getWidth() > browserBmp.getWidth()) {
            width = c.getWidth() + browserBmp.getWidth();
            height = c.getHeight();
        } else {
            width = browserBmp.getWidth() + browserBmp.getWidth();
            height = c.getHeight();
        }*/

        cs = Bitmap.createBitmap(browserBmp.getWidth(), browserBmp.getHeight(), Bitmap.Config.ARGB_8888);

        Canvas comboImage = new Canvas(cs);
        Bitmap favicon = Bitmap.createScaledBitmap(c,browserBmp.getWidth()/2, browserBmp.getHeight()/2, true);



        comboImage.drawBitmap(browserBmp, 0f, 0f, null);
        comboImage.drawBitmap(favicon, browserBmp.getWidth()/2, browserBmp.getHeight()/2, null);

        // this is an extra bit I added, just incase you want to save the new image somewhere and then return the location
    /*String tmpImg = String.valueOf(System.currentTimeMillis()) + ".png";

    OutputStream os = null;
    try {
      os = new FileOutputStream(loc + tmpImg);
      cs.compress(CompressFormat.PNG, 100, os);
    } catch(IOException e) {
      Log.e("combineImages", "problem combining images", e);
    }*/
        favicon.recycle();
        return cs;
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
        if(shortcut instanceof WebPageInfo) {

            URL url = null;
            try {
                url = new URL("http://www.google.com/s2/favicons?domain=" + ((WebPageInfo) shortcut).getPageUrl());

                

                Picasso.with(context).load(url.toString()).into(new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap combi = combineImages(bitmap, BitmapFactory.decodeResource(context.getResources(), R.drawable.browser));
                        holder.icon.setImageBitmap(combi);

                        //holder.notify();
                       notifyDataSetChanged();
                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        Log.d("TAG", "Prepare Load");
                    }

                });
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }

        }
        else
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
