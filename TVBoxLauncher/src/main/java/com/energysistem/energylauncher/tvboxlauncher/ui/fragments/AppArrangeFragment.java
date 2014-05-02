package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TabHost;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.BasicImgText;
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.BasicITAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.StableArrayAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.views.DynamicDraggingListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 02/05/2014.
 */

public class AppArrangeFragment extends Fragment  {


    private List<AppInfo> mAppInfosList;
    private List<String> mListaAppsPreferenciasStrings;

    private List<DraggableItemApp> mAppsInfoFiltradaOrdenada;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_app_arrange_list, container, false);

        mListaAppsPreferenciasStrings = ((LauncherActivity)getActivity()).getAppsNamePreferences();
        mAppInfosList = ((LauncherActivity)getActivity()).getAppList();

        CreaListaFiltradaOrdenada();

        StableArrayAdapter adapter = new StableArrayAdapter(view.getContext(),
                R.layout.row_app_draggable, mAppsInfoFiltradaOrdenada);
        DynamicDraggingListView listView = (DynamicDraggingListView) view.findViewById(R.id.draggable_listview);



        listView.setCheeseList(mAppsInfoFiltradaOrdenada);
        listView.setAdapter(adapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);


        return view;
    }

    private void CreaListaFiltradaOrdenada() {

        mAppsInfoFiltradaOrdenada = new ArrayList<DraggableItemApp>();

        for (int i = 0; i < mListaAppsPreferenciasStrings.size(); i++) {
            for (int j = 0; j < mAppInfosList.size(); j++) {
                if (SaveLoadAppsPreferences.ComparaNombreAppInfo(mAppInfosList.get(j),
                        mListaAppsPreferenciasStrings.get(i))) {
                    //Creamos el item que contendrá la pos, el nombre y el icono y lo añadimos a la lista
                    DraggableItemApp item = new DraggableItemApp(mAppsInfoFiltradaOrdenada.size(),
                            mListaAppsPreferenciasStrings.get(i), mAppInfosList.get(j).getBitmap());
                    mAppsInfoFiltradaOrdenada.add(item);
                }
            }
        }
    }



}
