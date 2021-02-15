package com.android.camera.resource;

import android.os.Parcelable;
import com.android.camera.module.impl.component.FileUtils;
import java.io.File;
import org.json.JSONObject;

public abstract class BaseResourceItem implements Parcelable, BaseResourceDownloadable {
    public String baseArchivesFolder;
    private transient int currentLoadState = 0;
    public String id;
    public transient int index;
    public String mZipPath;
    public String uri;
    public int versionCode;

    public abstract void fillDetailData(JSONObject jSONObject);

    public int getCurrentState() {
        return this.currentLoadState;
    }

    public String getDownloadUrl() {
        return this.uri;
    }

    public abstract String getIDPrefix();

    public abstract void onDecompressFailed(String str, String str2);

    public abstract void onDecompressFinished(String str, boolean z);

    /* access modifiers changed from: protected */
    public void onUpToDate(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("v_");
        sb.append(this.versionCode);
        FileUtils.createNewFile(sb.toString());
    }

    public abstract void parseSummaryData(JSONObject jSONObject, int i);

    public void setState(int i) {
        this.currentLoadState = i;
    }

    public abstract boolean simpleVerification(String str);

    public boolean versionVerification(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("v_");
        sb.append(this.versionCode);
        return new File(str, sb.toString()).exists();
    }
}
