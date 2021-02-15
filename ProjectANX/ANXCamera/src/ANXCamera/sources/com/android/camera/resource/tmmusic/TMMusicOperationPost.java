package com.android.camera.resource.tmmusic;

import com.android.camera.resource.AESUtils;
import com.android.camera.resource.RequestHelper;
import com.android.camera.resource.SimpleNetworkJsonRequest;
import java.util.Random;
import okhttp3.FormBody.Builder;
import okhttp3.RequestBody;
import org.json.JSONObject;

public class TMMusicOperationPost extends SimpleNetworkJsonRequest {
    private static final String APP_ID = "RM";
    private static final String BASE_URL = "https://statist.tingmall.com/tango-statist/report/wmReportSongs";

    public TMMusicOperationPost(String str) {
        super(BASE_URL);
        String substring = RequestHelper.md5((long) new Random().nextInt(100), System.currentTimeMillis()).substring(0, 16);
        String tMMusicAccessKey = RequestHelper.getTMMusicAccessKey();
        String str2 = APP_ID;
        String encryString = AESUtils.getEncryString(str2, substring, tMMusicAccessKey);
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(encryString);
        addHeaders("oauth_token", sb.toString());
        try {
            addParam("reportData", TangoCompressTests.toHexString(DataZipUtil.compress(str.getBytes())));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /* access modifiers changed from: protected */
    public RequestBody generatePostBody() {
        return new Builder().build();
    }

    /* access modifiers changed from: protected */
    public String parseJson(JSONObject jSONObject, String str) {
        return "";
    }
}
