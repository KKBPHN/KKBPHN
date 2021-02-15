package com.miui.internal.net;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Map;

public abstract class URLConnectionBuilder {
    protected static final String TAG = "URLConnectionBuilder";
    protected HttpURLConnection urlConnection;

    public URLConnectionBuilder(String str) {
        this(new URL(str));
    }

    private URLConnectionBuilder(URL url) {
        this.urlConnection = (HttpURLConnection) url.openConnection();
    }

    public HttpURLConnection build() {
        return this.urlConnection;
    }

    public URLConnectionBuilder setConnectTimeout(int i) {
        this.urlConnection.setConnectTimeout(i);
        return this;
    }

    public URLConnectionBuilder setDoInput(boolean z) {
        this.urlConnection.setDoInput(z);
        return this;
    }

    public URLConnectionBuilder setDoInputOutput(boolean z) {
        setDoInput(true);
        setDoOutput(true);
        return this;
    }

    public URLConnectionBuilder setDoOutput(boolean z) {
        this.urlConnection.setDoOutput(z);
        return this;
    }

    public URLConnectionBuilder setHeadParams(Map map) {
        for (String str : map.keySet()) {
            this.urlConnection.setRequestProperty(str, (String) map.get(str));
        }
        return this;
    }

    public URLConnectionBuilder setReadTimeout(int i) {
        this.urlConnection.setReadTimeout(i);
        return this;
    }

    public URLConnectionBuilder setRequestMethod(String str) {
        this.urlConnection.setRequestMethod(str);
        return this;
    }

    public URLConnectionBuilder setTimeout(int i) {
        setConnectTimeout(i);
        setReadTimeout(i);
        return this;
    }
}
