package miui.hybrid.feature;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import java.util.Map;
import miui.hybrid.Callback;
import miui.hybrid.HybridFeature;
import miui.hybrid.HybridFeature.Mode;
import miui.hybrid.LifecycleListener;
import miui.hybrid.NativeInterface;
import miui.hybrid.Request;
import miui.hybrid.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class Share implements HybridFeature {
    private static final String ACTION_SEND = "send";
    private static final String PARAM_DATA = "data";
    private static final String PARAM_TYPE = "type";
    private static final String TAG = "HybridShare";

    private Response invokeShareTo(Request request) {
        final NativeInterface nativeInterface = request.getNativeInterface();
        Activity activity = nativeInterface.getActivity();
        final Callback callback = request.getCallback();
        nativeInterface.addLifecycleListener(new LifecycleListener() {
            public void onActivityResult(int i, int i2, Intent intent) {
                nativeInterface.removeLifecycleListener(this);
                Response response = i2 == -1 ? new Response(0, BaseEvent.VALUE_SUCCESS) : i2 == 0 ? new Response(100, BaseEvent.VALUE_CANCEL) : new Response(200);
                callback.callback(response);
            }
        });
        Intent intent = new Intent();
        intent.setAction("android.intent.action.SEND");
        String rawParams = request.getRawParams();
        try {
            JSONObject jSONObject = new JSONObject(rawParams);
            String string = jSONObject.getString("type");
            String string2 = jSONObject.getString("data");
            if (string != null) {
                if (string2 != null) {
                    intent.setType(string);
                    intent.putExtra(string.startsWith("text/") ? "android.intent.extra.TEXT" : "android.intent.extra.STREAM", string2);
                    activity.startActivityForResult(intent, 1);
                    return null;
                }
            }
            callback.callback(new Response(200, "no data to share"));
            return null;
        } catch (JSONException unused) {
            StringBuilder sb = new StringBuilder();
            sb.append("invalid JSON string:");
            sb.append(rawParams);
            Log.i(TAG, sb.toString());
            callback.callback(new Response(200, "invalid data to share"));
        }
    }

    public Mode getInvocationMode(Request request) {
        return Mode.CALLBACK;
    }

    public Response invoke(Request request) {
        return ACTION_SEND.equals(request.getAction()) ? invokeShareTo(request) : new Response(204, "no such action");
    }

    public void setParams(Map map) {
    }
}
