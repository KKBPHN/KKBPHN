package com.android.camera.resource.tmmusic;

import com.android.camera.CameraAppImpl;
import com.android.camera.resource.AESUtils;
import com.android.camera.resource.RequestHelper;
import com.android.camera.resource.SimpleNetworkJsonRequest;
import java.util.Random;
import miui.telephony.phonenumber.TelocationProvider.Contract.Params;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class TMMusicCatrgoryRequest extends SimpleNetworkJsonRequest {
    private static final String APP_ID = "RM";
    private static final String BASE_URL = "https://sapi.tingmall.com/SkymanWS/Category/Items";

    public TMMusicCatrgoryRequest(String str) {
        super(BASE_URL);
        String substring = RequestHelper.md5((long) new Random().nextInt(100), System.currentTimeMillis()).substring(0, 16);
        String tMMusicAccessKey = RequestHelper.getTMMusicAccessKey();
        String str2 = APP_ID;
        String encryString = AESUtils.getEncryString(str2, substring, tMMusicAccessKey);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(encryString);
        addHeaders("oauth_token", sb.toString());
        addParam(OnNativeInvokeListener.ARG_OFFSET, "0");
        addParam(Params.LENGTH, "20");
        addParam("categoryid", str);
    }

    /* access modifiers changed from: protected */
    public TMMusicList parseJson(JSONObject jSONObject, TMMusicList tMMusicList) {
        JSONObject optJSONObject = jSONObject.optJSONObject("response").optJSONObject("docs");
        writeToCache(TMMusicList.CACHE_LIST, CameraAppImpl.getAndroidContext(), optJSONObject.toString());
        tMMusicList.createResourcesList(optJSONObject);
        return tMMusicList;
    }
}
