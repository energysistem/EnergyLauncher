package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;

import java.net.MalformedURLException;

/**
 * Created by MFC on 14/04/2014.
 */
public class MenuBookMarkFragment  extends Fragment {

        @Override
        public View onCreateView (LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState){
            View view = inflater.inflate(R.layout.fragment_menu_bookmark, container, false);
            final TextView txtName = (TextView) view.findViewById(R.id.menuBookmarkTxtName);
            final TextView txtUri = (TextView) view.findViewById(R.id.menuBookmarkTxtUri);
            //Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
            Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
            btnSave.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
                    WebPageInfo info = null;
                    try {
                        info = new WebPageInfo(txtUri.getText().toString());
                        info.title = txtName.getText().toString();
                        ((LauncherActivity) getActivity()).addShortcut(info);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                        //TODO
                    }
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
