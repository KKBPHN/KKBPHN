package com.android.camera.resource;

import android.content.Context;
import android.text.TextUtils;
import androidx.annotation.NonNull;
import com.android.camera.CameraAppImpl;
import com.android.camera.Util;
import com.android.camera.module.impl.component.FileUtils;
import io.reactivex.Scheduler;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Scanner;
import org.json.JSONObject;

public class SimpleMultiJsonInfoRequest extends BaseObservableRequest {
    private static final String PREFIX_NATIVE_RESOURCE = "assets://";
    private LinkedHashMap mFileMap;
    private Class mModelClass;

    public SimpleMultiJsonInfoRequest(LinkedHashMap linkedHashMap, @NonNull Class cls) {
        this.mFileMap = linkedHashMap;
        this.mModelClass = cls;
    }

    private String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    private void decompressNativeResource(Context context, BaseResourceItem baseResourceItem, String str) {
        Util.verifyAssetZip(context, baseResourceItem.uri.split("assets://")[1], str, 32768);
        baseResourceItem.onDecompressFinished(str, true);
    }

    private String getAssetCache(String str, Context context) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            return convertStreamToString(context.getApplicationContext().getAssets().open(str));
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void decompressResource(Context context, BaseResourceList baseResourceList, String str) {
        for (BaseResourceItem baseResourceItem : baseResourceList.getResourceList()) {
            String str2 = baseResourceItem.uri;
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(baseResourceItem.id);
            sb.append(File.separator);
            String sb2 = sb.toString();
            if (baseResourceItem.simpleVerification(sb2)) {
                boolean versionVerification = baseResourceItem.versionVerification(sb2);
                baseResourceItem.onDecompressFinished(sb2, versionVerification);
                if (versionVerification) {
                }
            }
            if (str2.startsWith("assets://")) {
                decompressNativeResource(context, baseResourceItem, sb2);
            }
        }
    }

    /* access modifiers changed from: protected */
    public Scheduler getWorkThread() {
        return Schedulers.io();
    }

    /* access modifiers changed from: protected */
    public void scheduleRequest(ResponseListener responseListener, BaseResourceList baseResourceList) {
        if (baseResourceList == null) {
            responseListener.onResponseError(2, null, null);
            return;
        }
        Iterator it = this.mFileMap.entrySet().iterator();
        while (true) {
            if (it.hasNext()) {
                Entry entry = (Entry) it.next();
                BaseResourceList baseResourceList2 = (BaseResourceList) create(this.mModelClass);
                String str = (String) entry.getKey();
                String str2 = (String) entry.getValue();
                Context androidContext = CameraAppImpl.getAndroidContext();
                try {
                    JSONObject jSONObject = new JSONObject(getAssetCache(str, androidContext));
                    baseResourceList2.parseInitialData(jSONObject);
                    baseResourceList2.createResourcesList(jSONObject);
                    try {
                        FileUtils.makeNoMediaDir(str2);
                        decompressResource(androidContext, baseResourceList2, str2);
                        List resourceList = baseResourceList2.getResourceList();
                        for (int i = 0; i < resourceList.size(); i++) {
                            ((BaseResourceItem) resourceList.get(i)).index = baseResourceList.getResourceList().size() + i;
                            baseResourceList.getResourceList().add(resourceList.get(i));
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                        responseListener.onResponseError(3, e.getMessage(), null);
                        return;
                    }
                } catch (Exception e2) {
                    e2.printStackTrace();
                    responseListener.onResponseError(2, e2.getMessage(), null);
                    return;
                }
            } else {
                responseListener.onResponse(baseResourceList, false);
                return;
            }
        }
    }
}
