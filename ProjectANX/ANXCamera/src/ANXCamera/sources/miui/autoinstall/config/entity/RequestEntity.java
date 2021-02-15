package miui.autoinstall.config.entity;

import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class RequestEntity {
    public List appInfo;
    public String nonceStr;
    public String sign;

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<miui.autoinstall.config.entity.RequestAppInfo>, for r5v0, types: [java.util.List<miui.autoinstall.config.entity.RequestAppInfo>, java.util.List] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static String list2Json(List<RequestAppInfo> list) {
        if (list == null) {
            return "";
        }
        JSONArray jSONArray = new JSONArray();
        for (RequestAppInfo requestAppInfo : list) {
            if (requestAppInfo != null) {
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put("pn", requestAppInfo.pn);
                    jSONObject.put("pvc", requestAppInfo.pvc);
                    jSONArray.put(jSONObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
        return jSONArray.toString();
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("appInfo=");
        sb.append(list2Json(this.appInfo));
        sb.append("&nonceStr=");
        sb.append(this.nonceStr);
        sb.append("&sign=");
        sb.append(this.sign);
        return sb.toString();
    }
}
