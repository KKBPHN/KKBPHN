package miui.hybrid.feature;

import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import java.util.Map;
import miui.hybrid.Callback;
import miui.hybrid.HybridFeature;
import miui.hybrid.HybridFeature.Mode;
import miui.hybrid.Request;
import miui.hybrid.Response;
import org.json.JSONException;
import org.json.JSONObject;

public class Geolocation implements HybridFeature {
    private static final String ACTION_DISABLE = "disableListener";
    private static final String ACTION_ENABLE = "enableListener";
    private static final String ACTION_GET = "get";
    private static final String PARAM_TYPE = "type";
    private static final String TAG = "HybridGeolocation";
    /* access modifiers changed from: private */
    public Callback mCallback;
    private LocationListener mListener;
    private String mProvider = "network";

    class NetworkLocationListener implements LocationListener {
        private NetworkLocationListener() {
        }

        public void onLocationChanged(Location location) {
            if (Geolocation.this.mCallback != null) {
                Geolocation.this.mCallback.callback(Geolocation.this.response(location));
            }
        }

        public void onProviderDisabled(String str) {
        }

        public void onProviderEnabled(String str) {
        }

        public void onStatusChanged(String str, int i, Bundle bundle) {
        }
    }

    private Response invokeGetLocation(LocationManager locationManager, Request request) {
        return response(locationManager.getLastKnownLocation(this.mProvider));
    }

    private Response invokeRemove(LocationManager locationManager, Request request) {
        LocationListener locationListener = this.mListener;
        if (locationListener != null) {
            locationManager.removeUpdates(locationListener);
            this.mListener = null;
        }
        this.mCallback = null;
        request.getCallback().callback(new Response(0, "remove"));
        return null;
    }

    private Response invokeUpdateLocation(LocationManager locationManager, Request request) {
        this.mCallback = request.getCallback();
        if (this.mListener == null) {
            this.mListener = new NetworkLocationListener();
            Looper.prepare();
            locationManager.requestLocationUpdates(this.mProvider, 0, 0.0f, this.mListener);
            Looper.loop();
        }
        this.mCallback.callback(response(locationManager.getLastKnownLocation(this.mProvider)));
        return null;
    }

    /* access modifiers changed from: private */
    public Response response(Location location) {
        String str = TAG;
        if (location != null) {
            Log.i(str, "response with valid location.");
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put("latitude", location.getLatitude());
                jSONObject.put("longitude", location.getLongitude());
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return new Response(3, jSONObject.toString());
        }
        Log.i(str, "error: response location with null.");
        return new Response(200, "no location");
    }

    public Mode getInvocationMode(Request request) {
        return ACTION_GET.equals(request.getAction()) ? Mode.SYNC : Mode.CALLBACK;
    }

    public Response invoke(Request request) {
        String action = request.getAction();
        StringBuilder sb = new StringBuilder();
        sb.append("invoke with action: ");
        sb.append(action);
        Log.i(TAG, sb.toString());
        LocationManager locationManager = (LocationManager) request.getNativeInterface().getActivity().getSystemService("location");
        return ACTION_ENABLE.equals(action) ? invokeUpdateLocation(locationManager, request) : ACTION_GET.equals(action) ? invokeGetLocation(locationManager, request) : ACTION_DISABLE.equals(action) ? invokeRemove(locationManager, request) : new Response(204, "no such action");
    }

    public void setParams(Map map) {
        String str = "gps";
        if (str.equals((String) map.get("type"))) {
            this.mProvider = str;
        }
    }
}
