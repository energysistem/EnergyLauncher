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
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.StableArrayAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.views.DynamicDraggingListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 02/05/2014.
 */

public class AppArrangeFragment extends Fragment {


    private List<DraggableItemApp> mFavInfosList;

    private List<DraggableItemApp> mListAppsDragablesOrdenada;
    private DynamicDraggingListView mListView;
    private Button mBtnGuardar;
    private StableArrayAdapter mAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_app_arrange_list, container, false);


        //mFavInfosList = ((LauncherActivity) getActivity()).getListFavDraggables();
        if(mFavInfosList!=null)Log.e("------AppArrangeFragment--recibe---mFavInfosList.size()--->", Integer.toString(mFavInfosList.size()));


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
                if (cambiado) {
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


    public void setFocus() {
        mListView.requestFocus();
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


        ArrayList<String> listaApps;
        try {
            listaApps = ((LauncherActivity) getActivity()).getAppsNamePreferences();
        } catch (NullPointerException e) {
            listaApps = new ArrayList<String>();
        }

        /*for (int i = 0; i < listaApps.size(); i++) {
            for (int j = 0; j < mFavInfosList.size(); j++) {
                if (SaveLoadAppsPreferences.ComparaNombreFav(mFavInfosList.get(j),
                        ((LauncherActivity) getActivity()).getAppsNamePreferences().get(i))) {
                    //if (mFavInfosList instanceof AppInfo){//para que solo recorra los favoritos que sean Apps
                            //Creamos el item que contendrá la pos, el nombre y el icono y lo añadimos a la lista
                    Log.e("------Rellenando mListAppsDragablesOrdenada--->", "Elemento nº"+mListAppsDragablesOrdenada.size());
                            DraggableItemApp item = new DraggableItemApp(
                                    mListAppsDragablesOrdenada.size(),//ultima posicion de la lista de favoritos draggables
                                    mFavInfosList.get(j).getTitle(),                                    //hacen lo mismo? getTitle de ShortcutInfo ------------->Titulo del objeto info
                                    //((LauncherActivity) getActivity()).getAppsNamePreferences().get(i), //Hacen lo mismo? getString de SavedLoadAppsPreferences ->Titulo de la lista de favoritos de solo titulos
                                    mFavInfosList.get(j).getIcono());
                            mListAppsDragablesOrdenada.add(item);
            }
            }
        }*/

        mListAppsDragablesOrdenada=getDraggableListActualizada();
        Log.d("----CreaListaFiltradaOrdenada() AppArrangeFragment: ",Integer.toString(((LauncherActivity) getActivity()).getListFavDraggables().size()));

    }


    private void aplicaCambios() {
        assert (getActivity()) != null;

        ((LauncherActivity) getActivity()).actualizaOrdenApps(mListAppsDragablesOrdenada);
        //((LauncherActivity) getActivity()).setsavedGridFav(mListAppsDragablesOrdenada);

        resetFragment();
        mListView.requestFocus();
    }



    public List<DraggableItemApp> getDraggableListActualizada(){
        Log.d("----getDraggableListActualizada() AppArrangeFragment: ",Integer.toString(((LauncherActivity) getActivity()).getListFavDraggables().size()));

        return ((LauncherActivity) getActivity()).getListFavDraggables();
        //Log.d("----getDraggableListActualizada() AppArrangeFragment: ",Integer.toString(mListAppsDragablesOrdenada.size()));

    }

    public void resetFragment() {
        if (mListView != null) {
            CreaListaFiltradaOrdenada();
            mListView.setAppsList(mListAppsDragablesOrdenada);
            mListView.setListaReseteada();

            mAdapter.notifyDataSetChanged();

            mBtnGuardar.setEnabled(false);
            mBtnGuardar.setFocusable(false);
        }
    }

    /*
    @Override
    public void onStop() {
        super.onStop();
        ((LauncherActivity) getActivity()).resetTab3();
    }*/


}
