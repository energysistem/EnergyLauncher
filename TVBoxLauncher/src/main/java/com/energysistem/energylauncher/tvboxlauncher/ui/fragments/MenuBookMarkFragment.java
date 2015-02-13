package com.energysistem.energylauncher.tvboxlauncher.ui.fragments;

import android.app.AlertDialog;
import android.app.Fragment;
import android.app.LoaderManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.energysistem.energylauncher.tvboxlauncher.R;
import com.energysistem.energylauncher.tvboxlauncher.database.BookmarkDAO;
import com.energysistem.energylauncher.tvboxlauncher.modelo.AppInfo;
import com.energysistem.energylauncher.tvboxlauncher.modelo.SaveLoadAppsPreferences;
import com.energysistem.energylauncher.tvboxlauncher.modelo.WebPageInfo;
import com.energysistem.energylauncher.tvboxlauncher.ui.LauncherActivity;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.AppAdapter;
import com.energysistem.energylauncher.tvboxlauncher.ui.adapters.WebShortcutAdapter;

import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.List;
import static android.widget.AdapterView.OnItemClickListener;

/**
 * Created by MFC on 14/04/2014.
 */
public class MenuBookMarkFragment  extends Fragment
        implements
        AdapterView.OnItemLongClickListener,
        AdapterView.OnItemClickListener,
        LoaderManager.LoaderCallbacks<List<WebPageInfo>> {

    private static final String TAG = "WebShortcutListFragment";

    private static ListView mlistViewWebshorts;
    private List<WebPageInfo> mListWebPage;
    private List<WebPageInfo> mSavedWebpages;
    private WebShortcutAdapter mAdapter;

    private TextView mTxtName;
    private TextView mTxtUri;
    private Button mBtnSave;
    private boolean checkMode=false;
    private EditText mEditText;
    private BookmarkDAO datasource;



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.fragment_menu_bookmark, container, false);

        mTxtName = (TextView) view.findViewById(R.id.menuBookmarkTxtName);

        mTxtUri = (TextView) view.findViewById(R.id.menuBookmarkTxtUri);
       // mEditText=(EditText) view.findViewById(R.id.menuBookmarkTxtName);
       // mEditText.requestFocus();
        mTxtName.requestFocus();

        //if(mSavedWebpages==null){mSavedWebpages = new ArrayList<WebPageInfo>();}
        mListWebPage = new ArrayList<WebPageInfo>();
        //mListWebPage = ((LauncherActivity) getActivity()).getlistaWeb();
        //Button btnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
        mBtnSave = (Button) view.findViewById(R.id.menuBookmarkBtn);
        mlistViewWebshorts = (ListView) view.findViewById(R.id.listViewLinks);
        mlistViewWebshorts.setOnItemClickListener(this);
        mlistViewWebshorts.setOnItemLongClickListener(this);
        datasource = new BookmarkDAO(getActivity());

        mListWebPage = ((LauncherActivity) getActivity()).listaWebsDB;




        mBtnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                WebPageInfo info = null;
                try {
                    if(mTxtName.getText().toString().length() == 0 ) {
                        mTxtName.requestFocus(); //Ponemos el marcador donde debe
                        mTxtName.setError(getActivity().getString(R.string.titulo_vacio));
                    }
                    else if (mTxtUri.getText().toString().length() == 0 ) {
                        mTxtUri.requestFocus(); //Ponemos el marcador donde debe
                        mTxtUri.setError(getActivity().getString(R.string.url_vacia));
                    }
                    else {
                        info = new WebPageInfo(((LauncherActivity) getActivity()).getlistaWeb().size(),mTxtUri.getText().toString());
                        info.setTitle(mTxtName.getText().toString());

                        //((LauncherActivity) getActivity()).addShortcutApp(info);
                        if(info!=null){
                           // if(mListWebPage!=null){Log.d("mListWebPage", "NO NULL");}else{Log.d("mListWebPage", "NULLACO");}
                            if(mListWebPage.contains((WebPageInfo)info)) {
                                mTxtName.requestFocus(); //Ponemos el marcador donde debe
                                mTxtName.setError(getActivity().getString(R.string.bookmark_duplicado));
                            }
                            else {
                                mListWebPage.add(mListWebPage.size(), info);
                                ((LauncherActivity) getActivity()).setlistaWeb(mListWebPage);
                                //guardamos en base de datos, por defecto fav false
                                datasource.open();
                                datasource.createBookmark(info);
                                datasource.close();
                                mTxtName.setText("");
                                mTxtUri.setText("");
                                //mListWebPage.add(info);
                            }
    
                            }
                        mAdapter.notifyDataSetChanged();

                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    //TODO
                }

                InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(
                        Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(mTxtName.getWindowToken(), 0);
            }
        });


       // mListWebPage = ((LauncherActivity) getActivity()).cargaShortcutsEnDesktop();
        if(mListWebPage!=null){

            mAdapter = new WebShortcutAdapter(getActivity(), mListWebPage);
            mlistViewWebshorts.setAdapter(mAdapter);}
        setmListWebPage((ArrayList<WebPageInfo>) mListWebPage);
        return view;
    }

    public void OnFocusChangeListener(View v, boolean b){

        View vi = v.findFocus();
        Log.d("FOCOOOOOOOOOOOOOOOO", Integer.toString(vi.getId()));

    }



    public void setFocus() {
       /* if (mTxtUri.hasFocus() || mlistViewWebshorts.hasFocus() || mBtnSave.hasFocus()) {
            return;
        }*/
        mTxtName.requestFocus();

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState == null) {
            getLoaderManager().restartLoader(0, null, this);
        }
        setFocus();
    }

    /*---

   ------------------ CFG

   ----*/
   public void activaModoCheckBox(){
        if (mlistViewWebshorts.hasFocus()){
            Log.d(TAG, "Activamos modo checkbox WEBSHORTCUT");
            mAdapter.setSelectedCheckBoxMode(true);
            mAdapter.setSelectedItem(mlistViewWebshorts.getSelectedItemPosition());
            updateView();
            mAdapter.notifyDataSetChanged();
        }//listViewLinks
    }


    public void desactivaModoCheckBox() {
        if (mAdapter.getModeCheckBoxSelection()) {
            mAdapter.setSelectedCheckBoxMode(false);
            mAdapter.setSelectedItem(mlistViewWebshorts.getSelectedItemPosition());
            updateView();
            mAdapter.notifyDataSetChanged();
        }
    }

    private void updateView() {
        //Guardamos en el adapter la seleccion actual

        mAdapter.setSelectedItem(mlistViewWebshorts.getSelectedItemPosition());

        //Colocamos el listItem anterior como estaba
        View viewOld = mlistViewWebshorts.getChildAt(mAdapter.getLastSelectedItem()- mlistViewWebshorts.getFirstVisiblePosition());
        CheckBox frameOld = (CheckBox) viewOld.findViewById(R.id.frame_checkboxWeb);
        Log.e("-------------updateView()", "-----------listItem anterior---");

        if(mListWebPage.get(mAdapter.getLastSelectedItem()).checked){Log.e("IF 1", "----------TRUE---");}else{Log.e("IF 1", "----------FALSE---");}

        Log.e("Color black", Integer.toString(R.color.black_overlay));
        Log.e("Color blue", Integer.toString(R.color.blue));
        Log.e("Color capturado",Integer.toString(mAdapter.getFrameCheckBoxView(mListWebPage.get(mAdapter.getLastSelectedItem()).checked)));
        if(mAdapter.getFrameCheckBoxView(mListWebPage.get(mAdapter.getLastSelectedItem()).checked)==R.color.black_overlay){Log.e("IF 2", "----------TRUE- colorblacK--");}else{Log.e("IF 1", "----------FALSE---"+mAdapter.getFrameCheckBoxView(mListWebPage.get(mAdapter.getLastSelectedItem()).checked));}

        frameOld.setChecked(mListWebPage.get(mAdapter.getLastSelectedItem()).checked);

        //En caso que dejemos el modo seleccion colocamos el check como estaba
        if (!mAdapter.getModeCheckBoxSelection()){
            View v = mlistViewWebshorts.getChildAt(mlistViewWebshorts.getSelectedItemPosition() - mlistViewWebshorts.getFirstVisiblePosition());
            CheckBox frame = (CheckBox) v.findViewById(R.id.frame_checkboxWeb);
            Log.e("-------------updateView()", "----------el check como estaba---");
            frame.setChecked(mListWebPage.get(mAdapter.getSelectedItem()).checked);
            return;
        }

        //Colocamos el checkbox actual con el aspecto seleccionado
        View v = mlistViewWebshorts.getChildAt(mlistViewWebshorts.getSelectedItemPosition() - mlistViewWebshorts.getFirstVisiblePosition());

        if (v == null)
            return;

        CheckBox frame = (CheckBox) v.findViewById(R.id.frame_checkboxWeb);
        Log.e("-------------updateView()", "-----------ChekBox ACTUAL---");
        frame.setChecked(false);

    }



    public boolean appgetModeCheckBoxSelec() {
        if(mAdapter!=null){
        return   mAdapter.getModeCheckBoxSelection();}
        else{
            return false;
        }
    }


    @Override
    public boolean onItemLongClick(AdapterView parent, View view, int position, long id) {
        return false;
    }

    @Override
    public void onItemClick(AdapterView adapter, View v, int position, long arg) {
          final WebPageInfo info = mAdapter.getItem(position);
        Log.e("Numero elementos en grid", ((LauncherActivity) getActivity()).getGridDesktop().getCount() +"");
        if (mAdapter.getModeCheckBoxSelection()) {
            datasource.open();
            if (info.checked) {
                assert (getActivity()) != null;
                ((LauncherActivity) getActivity()).removeShortcutApp(info);
                info.checked = false;
                info.setFav(0);
                info.setPosi(-1);


                datasource.updateBookmark(info);

            } else {
                assert (getActivity()) != null;

                info.checked = true;
                info.setFav(1);
                info.setPosi(((LauncherActivity) getActivity()).getGridDesktop().getCount()+1);
                datasource.updateBookmark(info);
                try {
                    ((LauncherActivity) getActivity()).addShortcutApp(info);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            }
            mAdapter.notifyDataSetChanged();
            datasource.close();
        } else {
            openAlert(info, position);
            //getActivity().startActivity(info.getIntent());
        }


    }




    @Override
    public Loader<List<WebPageInfo>> onCreateLoader(int id, Bundle bundle) {
        //return new WebPageLoader();
        return null;
    }

    @Override
    public void onLoadFinished(Loader<List<WebPageInfo>> listLoader, List<WebPageInfo> webPageInfos) {
        //Take out the favorites
        //appInfos = extractFavorites(appInfos);

        //mListWebPage = webPageInfos;
        mAdapter = new WebShortcutAdapter(getActivity(), mListWebPage);
        mlistViewWebshorts.setAdapter(mAdapter);

        //Creamos el listener para el checkbox de dentro del item
        mAdapter.setOnCheckBoxClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.i("Onclicklistener", "OncheckboxClickListener posicion: " + v.getId());
                //La posicion está en el id del view
                WebPageInfo info = mListWebPage.get(v.getId());
                //guardamos en base de datos, por defecto fav false

                Log.e("-------ANTES------",info.toString());

                if (info.getFav()==1) {
                    assert (getActivity()) != null;
                    Log.e("Entramos fav = 1","onLoadFinished");


                    try {
                        ((LauncherActivity) getActivity()).addShortcutApp(info);
                    } catch (MalformedURLException e) {
                        e.printStackTrace();
                    }

                }
                else
                {
                    assert (getActivity()) != null;
                    Log.e("Entramos fav = 0","onLoadFinished");
                    ((LauncherActivity) getActivity()).removeShortcutApp(info);

                }


            }
        });

        ((LauncherActivity) getActivity()).cargaListaWeb();

        assert (getActivity()) != null;


    }

    private void openAlert(final WebPageInfo info, final int position) {
        ContextThemeWrapper themedContext = new ContextThemeWrapper( getActivity(), android.R.style.Theme_Holo_Light );
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(themedContext);

        alertDialogBuilder.setTitle(info.getTitle());
        // set positive button: Yes message
        alertDialogBuilder.setPositiveButton(themedContext.getString(R.string.bookmark_button_delete),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                mListWebPage.remove(position);
                mAdapter.notifyDataSetChanged();
                datasource.open();
                datasource.deleteBookmark(info);
                datasource.close();
                
            }
        });
        // set negative button: No message
        alertDialogBuilder.setNegativeButton(themedContext.getString(R.string.bookmark_button_go),new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {
                getActivity().startActivity(info.getIntent());
                //


            }
        });
        /*// set neutral button: Exit the app message
        alertDialogBuilder.setNeutralButton("Editar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog,int id) {


            }
        });*/


        AlertDialog alertDialog = alertDialogBuilder.create();
        // show alert
        alertDialog.show();
    }



    @Override
    public void onLoaderReset(Loader<List<WebPageInfo>> listLoader) {
        mlistViewWebshorts.setAdapter(null);
    }


    /**
     * Teclado en BookMarks
     */

    public boolean onKeyLeftD() {

        return true;
    }

    public boolean onKeyDownD() {

        return true;
    }

    public boolean onKeyUpD() {

        return true;
    }


    public List<WebPageInfo> getmListWebPage(){
        return mListWebPage;
    }

    public void setmListWebPage(ArrayList<WebPageInfo> n){
        mListWebPage=n;
    }
    public void actualisa(){
        mAdapter.notifyDataSetChanged();
    }

    public ArrayList<WebPageInfo> getListWebPage(){
        return (ArrayList<WebPageInfo>) mListWebPage;
    }



}
