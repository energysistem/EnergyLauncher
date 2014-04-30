package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.WebShortcutAdapter;

import java.net.MalformedURLException;
import java.util.ArrayList;

/**
 * Created by MFC on 14/04/2014.
 */
public class MenuBookMarkFragment  extends Fragment {

    WebShortcutAdapter mAdapter;
    ArrayList<WebPageInfo> mListWebPage;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_bookmark, container, false);
        final TextView txtName = (TextView) view.findViewById(R.id.menuBookmarkTxtName);
        final TextView txtUri = (TextView) view.findViewById(R.id.menuBookmarkTxtUri);
        //Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
        Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
        ListView listViewWebshorts = (ListView) view.findViewById(R.id.listViewLinks);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebPageInfo info = null;
                try {
                    info = new WebPageInfo(txtUri.getText().toString());
                    info.setTitle(txtName.getText().toString());
                    ((LauncherActivity) getActivity()).addShortcutApp(info);
                    mListWebPage.add(info);
                    mAdapter.notifyDataSetChanged();
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    //TODO
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(txtName.getWindowToken(), 0);
            }
        });

        mListWebPage = ((LauncherActivity) getActivity()).cargaShortcutsEnDesktop();

        mAdapter = new WebShortcutAdapter(getActivity(), R.id.listViewLinks, mListWebPage);

        listViewWebshorts.setAdapter(mAdapter);

        listViewWebshorts.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ((LauncherActivity) getActivity()).removeShortcut(position);
                mListWebPage.remove(position);
                mAdapter.notifyDataSetChanged();
            }
        });

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
