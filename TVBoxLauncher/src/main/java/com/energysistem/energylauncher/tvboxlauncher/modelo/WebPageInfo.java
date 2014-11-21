package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;

import com.energysistem.energylauncher.tvboxlauncher.R;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by emg on 09/04/2014.
 */
public class WebPageInfo extends ShortcutInfo {

    private static final String TAG = "EnergyLauncher.WebPageInfo";

    private Uri pageUrl;
    private int id;
    private int fav = 0;
    public Boolean checked = false;

    public WebPageInfo(int _id, Uri pageUrl) {
        this.id = _id;
        this.pageUrl = pageUrl;
        this.fav = 0;
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
        intent.setData(pageUrl);
        return intent;
    }

    public Uri getPageUrl() {
        return pageUrl;
    }


    public String getName() {
        return String.valueOf(pageUrl);
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

    @Override
    public String toString() {
        return "WebPageInfo{" +
                "pageUrl=" + pageUrl.toString() +
                ", id=" + id +
                ", fav=" + fav +
                ", checked=" + checked +
                '}';
    }
}
