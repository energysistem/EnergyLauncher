/*
 * Copyright (C) 2013 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.energysistem.energylauncher.tvboxlauncher.ui.adapters;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

public class StableArrayAdapter extends ArrayAdapter<DraggableItemApp> {

    final int INVALID_ID = -1;

    List<DraggableItemApp>mListaApps;
    int layoutResourceId;
    Context mContext;

    public StableArrayAdapter(Context context, int viewResourceId, List<DraggableItemApp> objects) {
        super(context,viewResourceId,objects);
        mListaApps = objects;
        layoutResourceId = viewResourceId;
        mContext = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View listItem = convertView;

        LayoutInflater inflater = ((Activity) mContext).getLayoutInflater();
        listItem = inflater.inflate(layoutResourceId, parent, false);

        final ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.icon_image_view);
        TextView textViewName = (TextView) listItem.findViewById(R.id.title_text_view);

        DraggableItemApp item = mListaApps.get(position);



        if(item.getIcono()==null) {

            if (item.getPageUrl().toString().toLowerCase().contains("energysistem.com")) {
                Log.e("watdafka", "entramos");
                imageViewIcon.setImageResource(R.drawable.ic_launcher);
            } else {
                URL url = null;
                try {
                    url = new URL("http://www.google.com/s2/favicons?domain=" + item.getPageUrl());
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
                Picasso.with(getContext()).load(url.toString()).into(new Target() {

                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap combi = combineImages(bitmap, BitmapFactory.decodeResource(mContext.getResources(), R.drawable.browser));
                        imageViewIcon.setImageBitmap(combi);
                        notifyDataSetChanged();
                        //

                    }

                    @Override
                    public void onBitmapFailed(final Drawable errorDrawable) {
                        Log.d("TAG", "FAILED");
                    }

                    @Override
                    public void onPrepareLoad(final Drawable placeHolderDrawable) {
                        imageViewIcon.setImageResource(R.drawable.browser);
                    }
                });
            }
        }
        else
            imageViewIcon.setImageBitmap(item.getIcono());

        textViewName.setText(item.getTitle());

        return listItem;
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

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mListaApps.size()) {
            return INVALID_ID;
        }
        return mListaApps.get(position).getPos();
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}

