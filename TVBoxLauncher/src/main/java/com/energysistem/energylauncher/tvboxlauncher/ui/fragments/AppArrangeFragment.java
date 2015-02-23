package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.DraggableItemApp;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.ShortcutInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.StableArrayAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.views.DynamicDraggingListView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by emg on 02/05/2014.
 */

public class AppArrangeFragment extends Fragment {


    private ArrayList<DraggableItemApp> mFavInfosList;

    private ArrayList<DraggableItemApp> mListAppsDragablesOrdenada;
    private ArrayList<DraggableItemApp> mListAppsDragablesOrdenadaAUX;

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
                R.layout.row_menu, mListAppsDragablesOrdenada);
        mListView = (DynamicDraggingListView) view.findViewById(R.id.draggable_listview);
        mBtnGuardar = (Button) view.findViewById(R.id.btnGuardaCambios);


        mListView.setAppsList(mListAppsDragablesOrdenada);
        mListView.setAdapter(mAdapter);
        mListView.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
        mListView.setClase(this);


        mListView.setOnListChangeListener(new DynamicDraggingListView.OnListChangeListener() {
            @Override
            public void onListChanged(View v, boolean cambiado) {
                if (cambiado) {
                    Log.e("Cambiado","ArrangeFragment");
                    mBtnGuardar.setEnabled(true);
                    mBtnGuardar.setFocusable(true);
                    //mBtnGuardar.setFocusableInTouchMode(true);
                    mBtnGuardar.setText(getActivity().getString(R.string.arrange_list_mensajeFinal));

                }
            }
        });

        mBtnGuardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // aplicaCambios();
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

       /* if(mListAppsDragablesOrdenadaAUX==null){
        mListAppsDragablesOrdenada=getDraggableListActualizada();}//Inicia el fragment
        else{
            mListAppsDragablesOrdenada=mListAppsDragablesOrdenadaAUX;
        }*/

        mListAppsDragablesOrdenada=getDraggableListActualizada();

        Log.d("---------LISTA DRAGABLES---------",mListAppsDragablesOrdenada.size()+"");

        //Log.d("----CreaListaFiltradaOrdenada() AppArrangeFragment: ",Integer.toString(mListAppsDragablesOrdenada.size()));

    }


    public void aplicaCambios() {
        assert (getActivity()) != null;


        ((LauncherActivity) getActivity()).actualizaOrdenApps(mListAppsDragablesOrdenada);

        mListAppsDragablesOrdenada=getDraggableListActualizada();
        resetFragment();
        mListView.requestFocus();
        mBtnGuardar.setText(getActivity().getString(R.string.arrange_list_mensajeInicial));
    }



    public ArrayList<DraggableItemApp> getDraggableListActualizada(){
       // Log.d("----CreaListaFiltradaOrdenada() AppArrangeFragment: ",Integer.toString(mListAppsDragablesOrdenada.size()));
        return ((LauncherActivity) getActivity()).getListFavDraggables();

    }

    public void resetFragment() {
        if (mListView != null) {
            //CreaListaFiltradaOrdenada();
            mListView.setAppsList(mListAppsDragablesOrdenada);
            mListView.setListaReseteada();

            mAdapter.notifyDataSetChanged();
            //((LauncherActivity) getActivity()).reloadDesktop(mListAppsDragablesOrdenada);

            mBtnGuardar.setEnabled(false);
            mBtnGuardar.setFocusable(false);
        }
    }

    public void setmListAppsDragablesOrdenada(ArrayList<DraggableItemApp> neo){
        mListAppsDragablesOrdenada=neo;
    }

    @Override
    public void onStop() {
        TextView text = (TextView) getActivity().findViewById(R.id.title_left);
        text.setText(getResources().getString(R.string.settings_panel_title));
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onResume() {
        TextView text = (TextView) getActivity().findViewById(R.id.title_left);
        text.setText(getResources().getString(R.string.reordena_button));
        super.onResume();
    }

    /*
    @Override
    public void onStop() {
        super.onStop();
        ((LauncherActivity) getActivity()).resetTab3();
    }*/


}
