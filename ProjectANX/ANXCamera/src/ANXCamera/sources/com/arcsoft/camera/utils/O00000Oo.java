package com.arcsoft.camera.utils;

import android.content.Context;
import android.database.sqlite.SQLiteFullException;
import android.media.MediaScannerConnection;
import android.media.MediaScannerConnection.MediaScannerConnectionClient;
import android.net.Uri;
import java.util.ArrayList;
import java.util.List;

public class O00000Oo implements MediaScannerConnectionClient {
    private static final int c = 100;
    private Context a;
    private MediaScannerConnection b;
    private List d = new ArrayList();
    private String e;

    public O00000Oo(Context context) {
        this.a = context;
        this.b = new MediaScannerConnection(this.a, this);
    }

    public void addPath(String str) {
        this.e = str;
        this.b.connect();
    }

    public void addUri(Uri uri) {
        if (uri != null) {
            this.d.add(uri);
        }
    }

    public void addUris(List list) {
        if (list != null && !list.isEmpty()) {
            this.d.addAll(list);
        }
    }

    public Uri getCurrentMediaUri() {
        if (this.d.isEmpty()) {
            return null;
        }
        return (Uri) this.d.get(0);
    }

    public List getUris() {
        return this.d;
    }

    public boolean isEmpty() {
        List list = this.d;
        return list == null || list.isEmpty();
    }

    public void onMediaScannerConnected() {
        try {
            this.b.scanFile(this.e, null);
        } catch (SQLiteFullException unused) {
        }
    }

    public void onScanCompleted(String str, Uri uri) {
        try {
            if (this.d.size() > 100) {
                this.d.remove(this.d.size() - 1);
            }
            this.d.add(0, uri);
        } finally {
            this.b.disconnect();
        }
    }

    public void release() {
        MediaScannerConnection mediaScannerConnection = this.b;
        if (mediaScannerConnection != null && mediaScannerConnection.isConnected()) {
            this.b.disconnect();
        }
    }
}
