package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.support.v7.graphics.Palette;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
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
public class ShortcutAdapter extends BaseAdapter {

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
        for (int i = 0; i < data.size(); i++) {
            if (data.get(i) == shortcutInfo) {
                return i;
            }
        }
        return -1;
    }

    public void addItem(final ShortcutInfo app, final Context context) throws MalformedURLException {
        Log.e("Entramos en addItem", "ShortcutAdapter");
        //
        if (app instanceof WebPageInfo) {
            boolean contiene = false;
            for (int i = 0; i < data.size(); i++) {
                Log.i((data.get(i)).getTitle(), (app).getTitle());
                if ((data.get(i)).getTitle().equals((app).getTitle())) {
                    contiene = true;
                    Log.e("ContieneEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", "");
                }
            }

            if (!contiene) {
                int num;
                if (data.size() < ((WebPageInfo) app).getPosi() - 1) {
                    Log.e("TAG", "ENTRAMOS->" + ((WebPageInfo) app).getPageUrl());
                    num = data.size() + 1;
                    data.add(app);
                } else {
                    Log.e("TAG", "ENTRAMOS->" + ((WebPageInfo) app).getPageUrl());
                    num = data.size() + 1;
                    if (((WebPageInfo) app).getPosi() - 1 < 0)
                        ((WebPageInfo) app).setPosi(1);
                    data.add(((WebPageInfo) app).getPosi() - 1, app);
                }

            }
        } else if (app instanceof AppInfo) {
            boolean contiene = false;
            for (int i = 0; i < data.size(); i++) {
                if (data.get(i) instanceof AppInfo) {
                    Log.i((data.get(i)).getTitle(), (app).getTitle());
                    if (((AppInfo) data.get(i)).getPackageName().equals(((AppInfo) app).getPackageName())) {
                        contiene = true;
                        Log.e("ContieneEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEEE", "");
                    }
                }
            }
            Log.e("metemos", app.toString());
            if (!contiene)
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
        Bitmap favicon = Bitmap.createScaledBitmap(c, browserBmp.getWidth() / 2, browserBmp.getHeight() / 2, true);


        comboImage.drawBitmap(browserBmp, 0f, 0f, null);
        comboImage.drawBitmap(favicon, browserBmp.getWidth() / 2, browserBmp.getHeight() / 2, null);

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
            data.set(((WebPageInfo) i).getPosi(), i);
    }

    public void removeItem(ShortcutInfo i) {

        data.remove(i);
    }

    public void clearItems() {
        data.clear();
    }


    public void removeItemPos(int i) {
        data.remove(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int i, View convertView, final ViewGroup viewGroup) {


        View view = convertView;


        ShortcutInfo shortcut = data.get(i);
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
        final View finalView = view;
        holder.title.setVisibility(View.VISIBLE);
        holder.icon.setVisibility(View.VISIBLE);
        if (shortcut instanceof WebPageInfo) {

            final Target mTarget = new Target() {
                @Override
                public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                    Log.d("TAG", "LOADED");
                    Bitmap combi = combineImages(bitmap, BitmapFactory.decodeResource(context.getResources(), R.drawable.bookmark));
                    holder.icon.setImageBitmap(combi);

                    holder.icon.setVisibility(View.VISIBLE);
                    Palette.generateAsync(combi, 5, new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {

                            Palette.Swatch color = palette.getDarkMutedSwatch();

                            if (color == null) {
                                color = palette.getDarkVibrantSwatch();
                                if (color == null) {
                                    color = palette.getVibrantSwatch();
                                }
                                if (color == null) {
                                    color = palette.getMutedSwatch();
                                }
                                if (color == null) {
                                    color = palette.getLightVibrantSwatch();
                                }
                                if (color == null) {
                                    color = palette.getLightMutedSwatch();
                                }
                                if (color == null) {
                                    color = palette.getSwatches().get(0);
                                }
                            }
                            if (color != null) {
                                if (color.getRgb() >= 16777215) {//Si el color es transparente
                                    ImageView iv = (ImageView) finalView.findViewById(R.id.backgroundCell);

                                    iv.setBackgroundResource(R.color.verdeOSCURO);//Ponemos un azul neutro
                                } else {
                                    ImageView iv = (ImageView) finalView.findViewById(R.id.backgroundCell);
                                    iv.setBackgroundColor(color.getRgb());
                                }
                            }


                        }
                    });

                    //holder.notify();
                    notifyDataSetChanged();
                }

                @Override
                public void onBitmapFailed(final Drawable errorDrawable) {
                    holder.icon.setVisibility(View.GONE);
                    ImageView iv = (ImageView) finalView.findViewById(R.id.backgroundCell);
                    iv.setBackgroundResource(R.drawable.tile_explorer);
                    Log.d("TAG", "FAILED");
                }

                @Override
                public void onPrepareLoad(final Drawable placeHolderDrawable) {
                    Log.d("TAG", "Prepare Load");
                    holder.icon.setVisibility(View.GONE);
                    ImageView iv = (ImageView) finalView.findViewById(R.id.backgroundCell);
                    iv.setBackgroundResource(R.drawable.tile_explorer);

                    holder.icon.setImageResource(R.drawable.bookmark);
                }

            };
            holder.icon.setTag(mTarget);
            //2112312312123123123213
            Log.e("dsdas", "sadas");

            if (((WebPageInfo) shortcut).getPageUrl().toString().toLowerCase().contains("energysistem.com")) {
                Log.e("watdafka", "entramos");
                holder.icon.setVisibility(View.GONE);
                holder.title.setVisibility(View.GONE);

                ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                iv.setBackgroundResource(R.drawable.tile_web);

            } else {
                URL url = null;
                try {
                    url = new URL("http://www.google.com/s2/favicons?domain=" + ((WebPageInfo) shortcut).getPageUrl());

                    Picasso.with(context).load(url.toString()).into(mTarget);

                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }


        } else {

            holder.icon.setImageBitmap(shortcut.getBitmap());
            if (((AppInfo) shortcut).getPackageName().equals("com.facebook.katana")) {

                //view.setBackgroundResource(R.drawable.facebook_tile);
                ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                iv.setBackgroundResource(R.drawable.facebook_tile);
                holder.icon.setVisibility(View.GONE);
                holder.title.setVisibility(View.GONE);
            } else {
                if (((AppInfo) shortcut).getPackageName().equals("com.android.vending")) {

                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_googleplay);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.amlogic.miracast")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_miracast);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.amlogic.PicturePlayer")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_gallery);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.farcore.videoplayer")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_video);
                } else if (((AppInfo) shortcut).getPackageName().equals("org.geometerplus.zlibrary.ui.android")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_music);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.twitter.android")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    holder.title.setVisibility(View.GONE);
                    iv.setBackgroundResource(R.drawable.tile_twitter);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.google.android.youtube.googletv")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);
                    holder.title.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_youtube);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.google.android.youtube")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);
                    holder.title.setVisibility(View.GONE);
                    iv.setBackgroundResource(R.drawable.tile_youtube);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.android.browser")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);


                    iv.setBackgroundResource(R.drawable.tile_explorer);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.fb.FileBrower")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_browser);
                } else if (((AppInfo) shortcut).getPackageName().equals("com.amlogic.OOBE")) {
                    //view.setBackgroundResource(R.drawable.facebook_tile);
                    ImageView iv = (ImageView) view.findViewById(R.id.backgroundCell);
                    holder.icon.setVisibility(View.GONE);

                    iv.setBackgroundResource(R.drawable.tile_setupwizard);
                } else {
                    holder.icon.setVisibility(View.VISIBLE);
                    holder.title.setVisibility(View.VISIBLE);
                    Palette.generateAsync(shortcut.getBitmap(), 5, new Palette.PaletteAsyncListener() {
                        public void onGenerated(Palette palette) {

                            Palette.Swatch color = palette.getDarkMutedSwatch();

                            if (color == null) {
                                color = palette.getDarkVibrantSwatch();
                                if (color == null) {
                                    color = palette.getVibrantSwatch();
                                }
                                if (color == null)
                                    color = palette.getMutedSwatch();
                            }
                            if (color.getRgb() >= 16777215) {//Si el color es transparente
                                ImageView iv = (ImageView) finalView.findViewById(R.id.backgroundCell);

                                iv.setBackgroundResource(R.color.verdeOSCURO);//Ponemos un azul neutro
                            } else {
                                ImageView iv = (ImageView) finalView.findViewById(R.id.backgroundCell);
                                iv.setBackgroundColor(color.getRgb());
                            }


                        }

                    });

                }
            }

        }


        holder.title.setText(shortcut.getTitle());


        view.setOnHoverListener(new View.OnHoverListener() {
            @Override
            public boolean onHover(View v, MotionEvent event) {
                ImageView iv = (ImageView) v.findViewById(R.id.backgroundCellSelected);


                if (event.getActionMasked() == MotionEvent.ACTION_HOVER_ENTER) {
                    iv.setVisibility(View.VISIBLE);
                    v.setScaleX(1.07f);
                    v.setScaleY(1.07f);


                    /*if (luminosidad > 0.5f) {
                        v.setBackgroundResource(R.drawable.shortcut_unselect_shape_light);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setTextColor(context.getResources().getColorStateList(R.color.text_grid_selector_light));
                    } else {
                        v.setBackgroundResource(R.drawable.shortcut_unselect_shape);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setTextColor(context.getResources().getColorStateList(R.color.text_grid_selector));
                    }*/
                } else if (event.getActionMasked() == MotionEvent.ACTION_HOVER_EXIT) {


                    iv.setVisibility(View.GONE);
                    v.setScaleX(1);
                    v.setScaleY(1);
                    /*if (luminosidad > 0.5f) {
                        v.setBackgroundResource(R.drawable.shortcut_select_shape_light);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setTextColor(context.getResources().getColorStateList(R.color.text_grid_selector_light));
                    } else {
                        v.setBackgroundResource(R.drawable.shortcut_select_shape);
                        TextView tv = (TextView) v.findViewById(R.id.title);
                        tv.setTextColor(context.getResources().getColorStateList(R.color.text_grid_selector));

                    }*/
                }

                return false;
            }


        });


        return view;
    }


    public List<ShortcutInfo> getListInfo() {
        return this.data;
    }

    public void setListInfo(List<ShortcutInfo> listaN) {
        this.data = listaN;
    }


    class ViewHolder {
        ImageView icon;
        TextView title;
    }

}
