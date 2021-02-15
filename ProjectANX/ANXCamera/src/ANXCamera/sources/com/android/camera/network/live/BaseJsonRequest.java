package com.android.camera.network.live;

import com.android.camera.network.net.base.ErrorCode;
import org.json.JSONException;
import org.json.JSONObject;

public abstract class BaseJsonRequest extends BaseRequest {
    public BaseJsonRequest(String str) {
        super(str);
    }

    /* access modifiers changed from: protected */
    public Object process(String str) {
        try {
            return processJson(new JSONObject(str));
        } catch (JSONException e) {
            throw new BaseRequestException(ErrorCode.PARSE_ERROR, e.getMessage(), e);
        }
    }

    public abstract Object processJson(JSONObject jSONObject);
}
