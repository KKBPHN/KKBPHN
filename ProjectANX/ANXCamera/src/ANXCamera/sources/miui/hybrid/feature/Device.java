package miui.hybrid.feature;

import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.Locale;
import java.util.Map;
import miui.hybrid.HybridFeature;
import miui.hybrid.HybridFeature.Mode;
import miui.hybrid.Request;
import miui.hybrid.Response;
import miui.telephony.TelephonyHelper;
import org.json.JSONException;
import org.json.JSONObject;

public class Device implements HybridFeature {
    private static final String ACTION_GET_DEVICE_INFO = "getDeviceInfo";
    private static final String KEY_DEVICE_ID = "deviceId";
    private static final String KEY_LANGUAGE = "language";
    private static final String KEY_MODEL = "model";
    private static final String KEY_REGION = "region";
    private static final String KEY_ROM_VERSION = "romVersion";
    private static final String LOG_TAG = "Device";

    private Response getDeviceInfo() {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("model", Build.MODEL);
            jSONObject.put(KEY_ROM_VERSION, VERSION.RELEASE);
            jSONObject.put("language", Locale.getDefault().getLanguage());
            jSONObject.put("region", Locale.getDefault().getCountry());
            jSONObject.put(KEY_DEVICE_ID, TelephonyHelper.getInstance().getDeviceId());
            return new Response(jSONObject);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
            return new Response(200, e.getMessage());
        }
    }

    public Mode getInvocationMode(Request request) {
        if (ACTION_GET_DEVICE_INFO.equals(request.getAction())) {
            return Mode.SYNC;
        }
        return null;
    }

    public Response invoke(Request request) {
        return ACTION_GET_DEVICE_INFO.equals(request.getAction()) ? getDeviceInfo() : new Response(204, "no such action");
    }

    public void setParams(Map map) {
    }
}
