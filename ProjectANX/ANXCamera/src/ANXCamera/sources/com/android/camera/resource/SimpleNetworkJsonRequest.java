package com.android.camera.resource;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import org.json.JSONObject;

public abstract class SimpleNetworkJsonRequest extends SimpleNetworkBaseRequest {
    public SimpleNetworkJsonRequest(String str) {
        super(str);
    }

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
}
