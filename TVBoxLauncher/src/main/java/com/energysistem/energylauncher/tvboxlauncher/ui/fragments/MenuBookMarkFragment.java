package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;

/**
 * Created by MFC on 14/04/2014.
 */
public class MenuBookMarkFragment  extends Fragment {

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_menu_bookmark, container, false);
            TextView txtName = (TextView) view.findViewById(R.id.menuBookmarkTxtName);
            TextView txtUri = (TextView) view.findViewById(R.id.menuBookmarkTxtUri);
            //Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
            Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    // Perform action on click
                }
            });


        return view;
    }

        @Override
        public void onActivityCreated (Bundle savedInstanceState){
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            //getLoaderManager().restartLoader(0, null, this);
        }
    }


}
