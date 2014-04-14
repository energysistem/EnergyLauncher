package com.energysistem.energylauncher.tvboxlauncher.modelo;

import android.content.Intent;
import android.net.Uri;

import java.net.MalformedURLException;

/**
 * Created by emg on 09/04/2014.
 */
public class WebPageInfo extends ShortcutInfo {

    Uri pageUrl;

    public WebPageInfo(Uri pageUrl) {
        this.pageUrl = pageUrl;
    }

    public WebPageInfo(String pageUrl) throws MalformedURLException {
        this(Uri.parse(pageUrl));
    }

    @Override
    public Intent getIntent() {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setData(pageUrl);
        return intent;
    }
}
