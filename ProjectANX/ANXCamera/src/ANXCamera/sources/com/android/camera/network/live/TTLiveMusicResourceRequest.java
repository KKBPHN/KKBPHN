package com.android.camera.network.live;

import androidx.core.app.NotificationCompat;
import com.android.camera.CameraSettings;
import com.android.camera.fragment.music.LiveMusicInfo;
import com.android.camera.log.Log;
import com.android.camera.network.net.base.ErrorCode;
import com.ss.android.ugc.effectmanager.EffectConfiguration;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class TTLiveMusicResourceRequest extends BaseJsonRequest {
    private static final String BASE_URL = "https://third-api.amemv.com/aweme/v1/third/hot/music/";

    public TTLiveMusicResourceRequest() {
        super(BASE_URL);
        long currentTimeMillis = System.currentTimeMillis();
        int nextInt = new Random().nextInt(100);
        String md5 = Util.md5((long) nextInt, currentTimeMillis);
        addParam(EffectConfiguration.KEY_APP_ID, "2");
        addParam("randnum", Integer.toString(nextInt));
        addParam("time", Long.toString(currentTimeMillis));
        addParam("generate", md5);
    }

    public List loadFromCache() {
        String tTLiveMusicJsonCache = CameraSettings.getTTLiveMusicJsonCache();
        StringBuilder sb = new StringBuilder();
        sb.append("loadFromCache = ");
        sb.append(tTLiveMusicJsonCache);
        Log.e("BaseRequest", sb.toString());
        try {
            return processJson(new JSONObject(tTLiveMusicJsonCache));
        } catch (JSONException e) {
            throw new BaseRequestException(ErrorCode.PARSE_ERROR, e.getMessage(), e);
        }
    }

    public List processJson(JSONObject jSONObject) {
        if (jSONObject != null) {
            String str = "status_code";
            if (jSONObject.has(str)) {
                if (jSONObject.getInt(str) == 0) {
                    String str2 = "music_list";
                    if (!jSONObject.isNull(str2)) {
                        CameraSettings.setTTLiveMusicJsonCache(jSONObject.toString());
                        ArrayList arrayList = new ArrayList();
                        JSONArray jSONArray = new JSONArray(jSONObject.optString(str2));
                        for (int i = 0; i < jSONArray.length(); i++) {
                            LiveMusicInfo liveMusicInfo = new LiveMusicInfo();
                            JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                            liveMusicInfo.mDurationText = jSONObject2.getString("duration");
                            String str3 = "uri";
                            liveMusicInfo.mPlayUrl = jSONObject2.getJSONObject("play_url").getString(str3);
                            liveMusicInfo.mThumbnailUrl = jSONObject2.getJSONObject("cover_thumb").getString(str3);
                            liveMusicInfo.mAuthor = jSONObject2.getString("author");
                            liveMusicInfo.mTitle = jSONObject2.getString("title").replace('/', '|');
                            arrayList.add(liveMusicInfo);
                        }
                        return arrayList;
                    }
                    throw new BaseRequestException(ErrorCode.BODY_EMPTY, "response empty data");
                }
                throw new BaseRequestException(ErrorCode.SERVER_ERROR, jSONObject.optString(NotificationCompat.CATEGORY_MESSAGE));
            }
        }
        throw new BaseRequestException(ErrorCode.PARSE_ERROR, "response has no status_code");
    }
}
