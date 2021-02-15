package miui.autoinstall.config.entity;

import android.text.TextUtils;
import com.google.android.apps.photos.api.PhotosOemApi;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ResponseAppInfo {
    public int code;
    public List data;
    public String message;

    public class ResponseAppInfoItem {
        public String apkMd5;
        public String apkName;
        public String cdnPath;
        public String packageName;
        public String packageVersionCode;
    }

    public static ResponseAppInfo json2Entity(String str) {
        if (TextUtils.isEmpty(str)) {
            return null;
        }
        try {
            JSONObject jSONObject = new JSONObject(str);
            ResponseAppInfo responseAppInfo = new ResponseAppInfo();
            responseAppInfo.message = jSONObject.getString("message");
            responseAppInfo.code = jSONObject.getInt("code");
            ArrayList arrayList = new ArrayList();
            JSONArray jSONArray = jSONObject.getJSONArray(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
            int length = jSONArray.length();
            for (int i = 0; i < length; i++) {
                ResponseAppInfoItem responseAppInfoItem = new ResponseAppInfoItem();
                JSONObject jSONObject2 = jSONArray.getJSONObject(i);
                responseAppInfoItem.apkMd5 = jSONObject2.getString("apkMd5");
                responseAppInfoItem.packageName = jSONObject2.getString("packageName");
                responseAppInfoItem.packageVersionCode = jSONObject2.getString("packageVersionCode");
                responseAppInfoItem.apkName = jSONObject2.getString("apkName");
                responseAppInfoItem.cdnPath = jSONObject2.getString("cdnPath");
                arrayList.add(responseAppInfoItem);
            }
            responseAppInfo.data = arrayList;
            return responseAppInfo;
        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }
}
