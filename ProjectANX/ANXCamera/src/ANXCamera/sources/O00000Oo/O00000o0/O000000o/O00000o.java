package O00000Oo.O00000o0.O000000o;

import android.util.Log;
import com.ss.android.ttve.monitor.MonitorUtils;
import org.json.JSONException;
import org.json.JSONObject;

public class O00000o {
    private static final String TAG = "IotDeviceInfo";
    private String O0OoO;
    private String O0OoOO0;
    private int O0OoOOO;
    private int O0OoOOo;
    private String O0OoOo0;
    private int O0OoOoO;
    private String OoO0o;
    private String bssid;
    private boolean isOnline;
    private String latitude;
    private String longitude;
    private String mac;
    private String model;
    private String name;
    private int pid;
    private int rssi;
    private String ssid;
    private String token;
    private long uid;

    private O00000o() {
    }

    public static final O00000o buildFromJson(String str) {
        try {
            return buildFromJson(new JSONObject(str));
        } catch (JSONException e) {
            Log.e(TAG, "", e);
            return null;
        }
    }

    public static final O00000o buildFromJson(JSONObject jSONObject) {
        try {
            O00000o o00000o = new O00000o();
            o00000o.OoO0o = jSONObject.getString("did");
            o00000o.uid = jSONObject.getLong("uid");
            o00000o.token = jSONObject.getString("token");
            o00000o.name = jSONObject.getString("name");
            o00000o.pid = jSONObject.getInt("pid");
            o00000o.O0OoO = jSONObject.optString("localip");
            o00000o.mac = jSONObject.getString("mac");
            o00000o.ssid = jSONObject.optString("ssid");
            o00000o.bssid = jSONObject.getString("bssid");
            o00000o.rssi = jSONObject.optInt("rssi");
            o00000o.longitude = jSONObject.optString("longitude");
            o00000o.latitude = jSONObject.optString("latitude");
            o00000o.O0OoOO0 = jSONObject.optString("parent_id");
            o00000o.O0OoOOO = jSONObject.getInt("show_mode");
            o00000o.model = jSONObject.getString(MonitorUtils.KEY_MODEL);
            o00000o.O0OoOOo = jSONObject.getInt("permitLevel");
            o00000o.isOnline = jSONObject.getBoolean("isOnline");
            o00000o.O0OoOo0 = jSONObject.optString("spec_type");
            o00000o.O0OoOoO = jSONObject.optInt("voice_ctrl");
            return o00000o;
        } catch (JSONException e) {
            Log.e(TAG, "Error when create IotDeviceInfo", e);
            return null;
        }
    }

    private static void setJsonValue(JSONObject jSONObject, String str, Object obj) {
        if (obj != null) {
            jSONObject.put(str, obj);
        }
    }

    public String O0oOo() {
        return this.O0OoO;
    }

    public String O0oOo0o() {
        return this.OoO0o;
    }

    public int O0oOoO() {
        return this.O0OoOOo;
    }

    public String O0oOoO0() {
        return this.O0OoOO0;
    }

    public int O0oOoOO() {
        return this.O0OoOOO;
    }

    public String O0oOoOo() {
        return this.O0OoOo0;
    }

    public boolean O0oOoo0() {
        return this.isOnline;
    }

    public int OOoOoo() {
        return this.O0OoOoO;
    }

    public String getBssid() {
        return this.bssid;
    }

    public String getLatitude() {
        return this.latitude;
    }

    public String getLongitude() {
        return this.longitude;
    }

    public String getMac() {
        return this.mac;
    }

    public String getModel() {
        return this.model;
    }

    public String getName() {
        return this.name;
    }

    public int getPid() {
        return this.pid;
    }

    public int getRssi() {
        return this.rssi;
    }

    public String getSsid() {
        return this.ssid;
    }

    public String getToken() {
        return this.token;
    }

    public long getUid() {
        return this.uid;
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            setJsonValue(jSONObject, "did", this.OoO0o);
            setJsonValue(jSONObject, "uid", Long.valueOf(this.uid));
            setJsonValue(jSONObject, "token", this.token);
            setJsonValue(jSONObject, "name", this.name);
            setJsonValue(jSONObject, "pid", Integer.valueOf(this.pid));
            setJsonValue(jSONObject, "localip", this.O0OoO);
            setJsonValue(jSONObject, "mac", this.mac);
            setJsonValue(jSONObject, "ssid", this.ssid);
            setJsonValue(jSONObject, "bssid", this.bssid);
            setJsonValue(jSONObject, "rssi", Integer.valueOf(this.rssi));
            setJsonValue(jSONObject, "longitude", this.longitude);
            setJsonValue(jSONObject, "latitude", this.latitude);
            setJsonValue(jSONObject, "parent_id", this.O0OoOO0);
            setJsonValue(jSONObject, "show_mode", Integer.valueOf(this.O0OoOOO));
            setJsonValue(jSONObject, MonitorUtils.KEY_MODEL, this.model);
            setJsonValue(jSONObject, "permitLevel", Integer.valueOf(this.O0OoOOo));
            setJsonValue(jSONObject, "isOnline", Boolean.valueOf(this.isOnline));
            setJsonValue(jSONObject, "spec_type", this.O0OoOo0);
            setJsonValue(jSONObject, "voice_ctrl", Integer.valueOf(this.O0OoOoO));
        } catch (JSONException e) {
            Log.e(TAG, "", e);
        }
        return jSONObject;
    }

    public String toJsonString() {
        return toJson().toString();
    }
}
