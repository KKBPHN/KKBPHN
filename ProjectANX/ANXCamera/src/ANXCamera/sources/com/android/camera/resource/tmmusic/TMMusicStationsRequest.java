package com.android.camera.resource.tmmusic;

import com.android.camera.CameraAppImpl;
import com.android.camera.resource.AESUtils;
import com.android.camera.resource.RequestHelper;
import com.android.camera.resource.SimpleNetworkJsonRequest;
import java.util.Random;
import org.json.JSONObject;

public class TMMusicStationsRequest extends SimpleNetworkJsonRequest {
    private static final String APP_ID = "RM";
    private static final String BASE_URL = "https://sapi.tingmall.com/SkymanWS/Category/Stations";

    public TMMusicStationsRequest(boolean z) {
        super(BASE_URL);
        String substring = RequestHelper.md5((long) new Random().nextInt(100), System.currentTimeMillis()).substring(0, 16);
        String tMMusicAccessKey = RequestHelper.getTMMusicAccessKey();
        String str = APP_ID;
        String encryString = AESUtils.getEncryString(str, substring, tMMusicAccessKey);
        StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append(encryString);
        addHeaders("oauth_token", sb.toString());
        addParam("categorycode", "RM_Genre_CA");
    }

    public static String getRandomString(int i) {
        Random random = new Random();
        StringBuffer stringBuffer = new StringBuffer();
        for (int i2 = 0; i2 < i; i2++) {
            stringBuffer.append("abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789".charAt(random.nextInt(62)));
        }
        return stringBuffer.toString();
    }

    /* access modifiers changed from: protected */
    public TMMusicList parseJson(JSONObject jSONObject, TMMusicList tMMusicList) {
        JSONObject optJSONObject = jSONObject.optJSONObject("response").optJSONObject("docs");
        writeToCache(TMMusicList.CACHE_INITIAL, CameraAppImpl.getAndroidContext(), optJSONObject.toString());
        tMMusicList.parseInitialData(optJSONObject);
        return tMMusicList;
    }
}
