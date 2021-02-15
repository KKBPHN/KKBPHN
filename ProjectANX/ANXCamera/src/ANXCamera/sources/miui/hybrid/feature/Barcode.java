package miui.hybrid.feature;

import android.app.Activity;
import android.content.Intent;
import android.text.TextUtils;
import java.util.Map;
import miui.hybrid.HybridFeature;
import miui.hybrid.HybridFeature.Mode;
import miui.hybrid.LifecycleListener;
import miui.hybrid.NativeInterface;
import miui.hybrid.Request;
import miui.hybrid.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class Barcode implements HybridFeature {
    private static final String ACTION_SCAN_BARCODE = "scan";
    private static final String INTENT_ACTION_SCAN_BARCODE = "android.intent.action.scanbarcode";
    private static final String INTENT_CATEGORY_SYSAPP_TOOL = "miui.intent.category.SYSAPP_TOOL";
    private static final String INTENT_EXTRA_IS_BACKTO_THIRDAPP = "isBackToThirdApp";
    private static final String INTENT_EXTRA_RESULT = "result";
    private static final String INTENT_EXTRA_TYPE = "type";
    private static final String KEY_RESULT = "result";
    private static final String KEY_TYPE = "type";
    /* access modifiers changed from: private */
    public static final int REQUEST_SCAN_BARCODE = "REQUEST_SCAN_BARCODE".hashCode();

    /* access modifiers changed from: private */
    public JSONObject makeResult(Intent intent) {
        if (intent == null) {
            return null;
        }
        String str = "type";
        int intExtra = intent.getIntExtra(str, -1);
        String str2 = "result";
        String stringExtra = intent.getStringExtra(str2);
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(str, intExtra);
            jSONObject.put(str2, stringExtra);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return jSONObject;
    }

    public Mode getInvocationMode(Request request) {
        if (TextUtils.equals(request.getAction(), ACTION_SCAN_BARCODE)) {
            return Mode.CALLBACK;
        }
        return null;
    }

    public Response invoke(final Request request) {
        if (!TextUtils.equals(request.getAction(), ACTION_SCAN_BARCODE)) {
            return new Response(204, "no such action");
        }
        final NativeInterface nativeInterface = request.getNativeInterface();
        Activity activity = nativeInterface.getActivity();
        Intent intent = new Intent(INTENT_ACTION_SCAN_BARCODE);
        intent.addCategory(INTENT_CATEGORY_SYSAPP_TOOL);
        intent.putExtra(INTENT_EXTRA_IS_BACKTO_THIRDAPP, true);
        if (activity.getPackageManager().resolveActivity(intent, 0) == null) {
            request.getCallback().callback(new Response(204, "can't find barcode scanner activity"));
            return null;
        }
        nativeInterface.addLifecycleListener(new LifecycleListener() {
            public void onActivityResult(int i, int i2, Intent intent) {
                if (i == Barcode.REQUEST_SCAN_BARCODE) {
                    nativeInterface.removeLifecycleListener(this);
                    Response response = i2 == -1 ? new Response(0, Barcode.this.makeResult(intent)) : i2 == 0 ? new Response(100) : new Response(200);
                    request.getCallback().callback(response);
                }
            }
        });
        activity.startActivityForResult(intent, REQUEST_SCAN_BARCODE);
        return null;
    }

    public void setParams(Map map) {
    }
}
