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

    private Uri pageUrl;

    public WebPageInfo(Uri pageUrl) {
        this.pageUrl = pageUrl;
    }

    public WebPageInfo(String pageUrl) throws MalformedURLException {
        this(Uri.parse(pageUrl));
    }

    public WebPageInfo(String uri, String tit){
        pageUrl = Uri.parse(uri);
        setTitle(tit);
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






}
