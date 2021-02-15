package com.android.camera.resource.tmmusic;

import com.android.camera.fragment.music.LiveMusicInfo;
import com.android.camera.resource.AESUtils;
import com.android.camera.resource.RequestHelper;
import com.android.camera.resource.SimpleNetworkJsonRequest;
import java.util.Random;
import org.json.JSONObject;

public class TMMusicItemRequest extends SimpleNetworkJsonRequest {
    private static final String APP_ID = "RM";
    private static final String BASE_URL = "https://sapi.tingmall.com/SkymanWS/Streaming/MusicURL";

    public TMMusicItemRequest(String str) {
        super(BASE_URL);
        String substring = RequestHelper.md5((long) new Random().nextInt(100), System.currentTimeMillis()).substring(0, 16);
        String tMMusicAccessKey = RequestHelper.getTMMusicAccessKey();
        String str2 = APP_ID;
        String encryString = AESUtils.getEncryString(str2, substring, tMMusicAccessKey);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(encryString);
        addHeaders("oauth_token", sb.toString());
        addParam("itemid", str);
        addParam("subitemtype", "MP3-64K-FTD-P");
        addParam("identityid", RequestHelper.getIdentityID());
    }

    /* access modifiers changed from: protected */
    public LiveMusicInfo parseJson(JSONObject jSONObject, LiveMusicInfo liveMusicInfo) {
        liveMusicInfo.mPlayUrl = jSONObject.optJSONObject("response").optJSONObject("docs").optString("url");
        return liveMusicInfo;
    }
}
