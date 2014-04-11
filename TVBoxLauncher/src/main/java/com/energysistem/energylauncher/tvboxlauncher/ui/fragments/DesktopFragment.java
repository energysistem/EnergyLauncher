package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;

/**
 * Created by vgt on 11/04/2014.
 */
public class DesktopFragment extends Fragment {

    private GridView mAppsGrid;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_launcher, container, false);

        mAppsGrid = (GridView) view.findViewById(R.id.app_grid);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1);
        mAppsGrid.setAdapter(adapter);

        for(int i = 0; i < 10; i++)
            adapter.add("Prueba "+i);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            //getLoaderManager().restartLoader(0, null, this);
        }
    }
}
