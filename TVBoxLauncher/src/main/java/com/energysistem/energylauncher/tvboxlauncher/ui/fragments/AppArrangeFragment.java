package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.StableArrayAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.views.DynamicDraggingListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 02/05/2014.
 */

public class AppArrangeFragment extends Fragment  {


    private List<AppInfo> mAppInfosList;
    private List<DraggableItemApp> mListAppsDragablesOrdenada;
    private DynamicDraggingListView mListView;
       private Button mBtnGuardar;
    private StableArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_app_arrange_list, container, false);


        mAppInfosList = ((LauncherActivity)getActivity()).getAppList();


        CreaListaFiltradaOrdenada();

        mAdapter = new StableArrayAdapter(view.getContext(),
                R.layout.row_app_draggable, mListAppsDragablesOrdenada);
        mListView = (DynamicDraggingListView) view.findViewById(R.id.draggable_listview);
        mBtnGuardar = (Button) view.findViewById(R.id.btnGuardaCambios);


        mListView.setAppsList(mListAppsDragablesOrdenada);
        mListView.setAdapter(mAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);

        mListView.setOnListChangeListener(new DynamicDraggingListView.OnListChangeListener() {
            @Override
            public void onListChanged(View v, boolean cambiado) {
                if (cambiado){
                    mBtnGuardar.setEnabled(true);
                    mBtnGuardar.setFocusable(true);
                    mBtnGuardar.setFocusableInTouchMode(true);
                }
            }
        });

        mBtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                aplicaCambios();
            }
        });

        mListView.requestFocus();

        return view;
    }





    private void CreaListaFiltradaOrdenada() {

        /**
         * Si ya está creada la lista tenemos que actualizarla,
         * Recorremos la lista de apps en preferencias, luego la lista general de apps (para el caso que
         * se haya instalado o desinstalado alguna)
         */
        if (mListAppsDragablesOrdenada != null && mListAppsDragablesOrdenada.size() > 0) {
            mListAppsDragablesOrdenada.clear();
        } else {
            mListAppsDragablesOrdenada = new ArrayList<DraggableItemApp>();
        }


        for (int i = 0; i < ((LauncherActivity)getActivity()).getAppsNamePreferences().size(); i++) {
            for (int j = 0; j < mAppInfosList.size(); j++) {
                if (SaveLoadAppsPreferences.ComparaNombreAppInfo(mAppInfosList.get(j),
                        ((LauncherActivity)getActivity()).getAppsNamePreferences().get(i))) {
                    //Creamos el item que contendrá la pos, el nombre y el icono y lo añadimos a la lista
                    DraggableItemApp item = new DraggableItemApp(
                            mListAppsDragablesOrdenada.size(),
                            mAppInfosList.get(j).getTitle(),
                            ((LauncherActivity)getActivity()).getAppsNamePreferences().get(i),
                            mAppInfosList.get(j).getBitmap());
                    mListAppsDragablesOrdenada.add(item);
                }
            }
        }
    }


    private void aplicaCambios() {
        assert (getActivity()) != null;
        ((LauncherActivity) getActivity()).actualizaOrdenApps(mListAppsDragablesOrdenada);

        resetFragment();
        mListView.requestFocus();
    }



    public void resetFragment(){
        if (mListView != null ){
            CreaListaFiltradaOrdenada();
            mListView.setAppsList(mListAppsDragablesOrdenada);
            mListView.setListaReseteada();
            mAdapter.notifyDataSetChanged();

            mBtnGuardar.setEnabled(false);
            mBtnGuardar.setFocusable(false);

        }
    }


}
