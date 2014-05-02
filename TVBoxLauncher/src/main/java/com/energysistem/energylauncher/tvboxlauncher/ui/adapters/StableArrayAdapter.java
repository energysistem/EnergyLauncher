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

import android.content.Context;
import android.graphics.Bitmap;
import android.media.Image;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;

import java.util.HashMap;
import java.util.List;

public class StableArrayAdapter extends ArrayAdapter<DraggableItemApp> {

    final int INVALID_ID = -1;

    HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

    public StableArrayAdapter(Context context, int textViewResourceId, List<DraggableItemApp> objects) {
        super(context, textViewResourceId, objects);
        for (int i = 0; i < objects.size(); ++i) {
            mIdMap.put(objects.get(i).getNombre(), i);
        }
    }

    @Override
    public long getItemId(int position) {
        if (position < 0 || position >= mIdMap.size()) {
            return INVALID_ID;
        }
        String item = getItem(position).getNombre();
        return mIdMap.get(item);
    }

    @Override
    public boolean hasStableIds() {
        return true;
    }


}

