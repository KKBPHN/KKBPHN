package miui.hybrid.feature;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo.State;
import android.util.Log;
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

public class Network implements HybridFeature {
    private static final String ACTION_DISABLE_NOTIFICATION = "disableNotification";
    private static final String ACTION_ENABLE_NOTIFICATION = "enableNotification";
    private static final String ACTION_GET_TYPE = "getType";
    private static final String KEY_CONNECTED = "connected";
    private static final String KEY_METERED = "metered";
    private static final String LOG_TAG = "Network";
    /* access modifiers changed from: private */
    public Callback mCallback;
    private IntentFilter mFilter = new IntentFilter();
    private LifecycleListener mLifecycleListener;
    private NetworkStateReceiver mReceiver;

    class NetworkStateReceiver extends BroadcastReceiver {
        private NetworkStateReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            if (intent.getExtras() != null) {
                boolean z = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo().getState() == State.CONNECTED;
                JSONObject jSONObject = new JSONObject();
                try {
                    jSONObject.put(Network.KEY_CONNECTED, z);
                    Network.this.mCallback.callback(new Response(jSONObject));
                } catch (JSONException e) {
                    Log.e(Network.LOG_TAG, e.getMessage());
                }
            }
        }
    }

    public Network() {
        this.mFilter.addAction("android.net.conn.CONNECTIVITY_CHANGE");
    }

    private Response disableNotification(Request request) {
        unregisterNotification(request.getNativeInterface());
        return new Response(100);
    }

    private Response enableNotification(Request request) {
        final NativeInterface nativeInterface = request.getNativeInterface();
        unregisterNotification(nativeInterface);
        Activity activity = nativeInterface.getActivity();
        this.mCallback = request.getCallback();
        this.mReceiver = new NetworkStateReceiver();
        activity.registerReceiver(this.mReceiver, this.mFilter);
        this.mLifecycleListener = new LifecycleListener() {
            private void unregister() {
                Network.this.unregisterNotification(nativeInterface);
                Network.this.mCallback.callback(new Response(100));
            }

            public void onDestroy() {
                unregister();
            }

            public void onPageChange() {
                unregister();
            }
        };
        nativeInterface.addLifecycleListener(this.mLifecycleListener);
        return null;
    }

    private Response isMetered(Request request) {
        boolean isActiveNetworkMetered = ((ConnectivityManager) request.getNativeInterface().getActivity().getSystemService("connectivity")).isActiveNetworkMetered();
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(KEY_METERED, isActiveNetworkMetered);
        } catch (JSONException e) {
            Log.e(LOG_TAG, e.getMessage());
        }
        return new Response(jSONObject);
    }

    /* access modifiers changed from: private */
    public void unregisterNotification(NativeInterface nativeInterface) {
        if (this.mReceiver != null) {
            Activity activity = nativeInterface.getActivity();
            nativeInterface.removeLifecycleListener(this.mLifecycleListener);
            activity.unregisterReceiver(this.mReceiver);
            this.mReceiver = null;
        }
    }

    public Mode getInvocationMode(Request request) {
        String action = request.getAction();
        if (ACTION_GET_TYPE.equals(action)) {
            return Mode.SYNC;
        }
        if (ACTION_ENABLE_NOTIFICATION.equals(action)) {
            return Mode.CALLBACK;
        }
        if (ACTION_DISABLE_NOTIFICATION.equals(action)) {
            return Mode.SYNC;
        }
        return null;
    }

    public Response invoke(Request request) {
        String action = request.getAction();
        return ACTION_GET_TYPE.equals(action) ? isMetered(request) : ACTION_ENABLE_NOTIFICATION.equals(action) ? enableNotification(request) : ACTION_DISABLE_NOTIFICATION.equals(action) ? disableNotification(request) : new Response(204, "no such action");
    }

    public void setParams(Map map) {
    }
}
