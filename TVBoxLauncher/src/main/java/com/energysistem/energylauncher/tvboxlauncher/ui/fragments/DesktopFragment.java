package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.ShortcutAdapter;

/**
 * Created by vgt on 11/04/2014.
 */
public class DesktopFragment extends Fragment implements AdapterView.OnItemClickListener{

    private GridView mAppsGrid;
    private ShortcutAdapter gridAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_desktop, container, false);

        mAppsGrid = (GridView) view.findViewById(R.id.app_grid);
        gridAdapter = new ShortcutAdapter(getActivity());
        mAppsGrid.setAdapter(gridAdapter);

        mAppsGrid.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            //getLoaderManager().restartLoader(0, null, this);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        ShortcutInfo shortcut = (ShortcutInfo) gridAdapter.getItem(i);
        startActivity(shortcut.getIntent());
    }

    public void addShortcut(ShortcutInfo shortcutInfo) {
        gridAdapter.addItem(shortcutInfo);
        gridAdapter.notifyDataSetChanged();
    }

    public void getShortcuts() {

    }
}
