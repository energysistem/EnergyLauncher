package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Intent;
import android.net.Uri;

import java.net.MalformedURLException;

/**
 * Created by emg on 09/04/2014.
 */
public class WebPageInfo extends ShortcutInfo {

    private static final String TAG = "EnergyLauncher.WebPageInfo";

    private Uri pageUrl;
    private int id;
    private int fav = 0;
    private int posi = -1;
    public Boolean checked = false;

    public WebPageInfo(int _id, Uri pageUrl) {
        this.id = _id;
        this.pageUrl = pageUrl;
        this.fav = 0;
    }

    @Override
    public boolean equals(Object o) {
        if(!(o instanceof WebPageInfo))
            return false;
        else
        {
            //Log.e("Nombre:"+this.getName(),((WebPageInfo) o).getName());

            //Compara ignorando mayus/minus
            return this.getName().trim().equalsIgnoreCase(((WebPageInfo) o).getName().trim());
        }

    }

    @Override
    public String toString() {
        return "WebPageInfo{" +
                "pageUrl=" + pageUrl +
                ", id=" + id +
                ", fav=" + fav +
                ", posi=" + posi +
                ", checked=" + checked +
                '}';
    }

    public WebPageInfo(int _id, String pageUrl) throws MalformedURLException {
        this(_id, Uri.parse(pageUrl));
    }

    public WebPageInfo(int _id, String uri, String tit, int _fav){
        setId(_id);
        pageUrl = Uri.parse(uri);
        setTitle(tit);
        setFav(_fav);
    }

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);

        String url = pageUrl.toString();
        if (!url.startsWith("http://") && !url.startsWith("https://"))
            url = "http://" + url;

        intent.setData(Uri.parse(url));
        return intent;
    }

    public Uri getPageUrl() {
        return pageUrl;
    }


    public String getName() {
        return String.valueOf(getTitle());
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFav() {
        return fav;
    }

    public void setFav(int fav) {
        this.fav = fav;
    }

    public int getPosi() {
        return posi;
    }

    public void setPosi(int posi) {
        this.posi = posi;
    }
}
