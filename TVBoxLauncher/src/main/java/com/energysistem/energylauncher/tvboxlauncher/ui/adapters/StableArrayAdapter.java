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
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;

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

        ImageView imageViewIcon = (ImageView) listItem.findViewById(R.id.icon_image_view);
        TextView textViewName = (TextView) listItem.findViewById(R.id.title_text_view);

        DraggableItemApp item = mListaApps.get(position);

        imageViewIcon.setImageBitmap(item.getIcono());
        textViewName.setText(item.getTitle());

        return listItem;
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

