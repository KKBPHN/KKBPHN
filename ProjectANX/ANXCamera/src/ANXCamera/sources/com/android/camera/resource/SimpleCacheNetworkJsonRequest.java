package com.android.camera.resource;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Scanner;
import org.json.JSONObject;

public abstract class SimpleCacheNetworkJsonRequest extends SimpleNetworkBaseRequest {
    public SimpleCacheNetworkJsonRequest(String str) {
        super(str);
    }

    private String convertStreamToString(InputStream inputStream) {
        Scanner useDelimiter = new Scanner(inputStream).useDelimiter("\\A");
        return useDelimiter.hasNext() ? useDelimiter.next() : "";
    }

    public abstract boolean enableCache();

    /* access modifiers changed from: protected */
    public File getCacheFile(String str, Context context) {
        return new File(context.getCacheDir(), str);
    }

    /* access modifiers changed from: protected */
    public String getCacheJsonString(String str, Context context) {
        InputStream inputStream;
        File cacheFile = getCacheFile(str, context);
        if (cacheFile.exists()) {
            try {
                inputStream = new FileInputStream(cacheFile);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                return null;
            }
        } else {
            inputStream = null;
        }
        return convertStreamToString(inputStream);
    }

    public abstract boolean isCacheValid(Object obj);

    public abstract Object parseJson(JSONObject jSONObject, @NonNull Object obj);

    /* access modifiers changed from: protected */
    public Object process(String str, @Nullable Object obj) {
        try {
            return parseJson(new JSONObject(str), obj);
        } catch (Exception e) {
            e.printStackTrace();
            throw new BaseRequestException(2, e.getMessage(), e);
        }
    }

    public abstract void processRestore(Object obj);

    /* access modifiers changed from: protected */
    public void scheduleRequest(ResponseListener responseListener, @NonNull Object obj) {
        if (enableCache() && isCacheValid(obj)) {
            boolean z = true;
            try {
                processRestore(obj);
            } catch (Exception e) {
                e.printStackTrace();
                z = false;
            }
            if (z) {
                responseListener.onResponse(obj, false);
                return;
            }
        }
        super.scheduleRequest(responseListener, obj);
    }
}
