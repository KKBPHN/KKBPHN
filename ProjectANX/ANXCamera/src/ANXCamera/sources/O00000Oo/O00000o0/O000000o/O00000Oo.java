package O00000Oo.O00000o0.O000000o;

import com.xiaomi.mi_connect_sdk.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class O00000Oo {
    private static final String O0Oo = "P2pStream";
    private static final String TAG = "CameraResource";
    O000000o o00o00O;

    public static O00000Oo buildFromJson(String str) {
        JSONObject jSONObject;
        try {
            jSONObject = new JSONObject(str);
        } catch (JSONException e) {
            LogUtil.e(TAG, "", (Throwable) e);
            jSONObject = null;
        }
        return buildFromJson(jSONObject);
    }

    public static O00000Oo buildFromJson(JSONObject jSONObject) {
        O00000Oo o00000Oo = new O00000Oo();
        JSONObject optJSONObject = jSONObject.optJSONObject("cameraControl");
        if (optJSONObject != null) {
            o00000Oo.o00o00O = new O000000o(o00000Oo, optJSONObject);
        }
        return o00000Oo;
    }

    public void O0000ooO(int i) {
        this.o00o00O = new O000000o(this, O000000o.O0Oo0);
        this.o00o00O.O0Oo00O = i;
    }

    public void O0000ooo(int i) {
        this.o00o00O = new O000000o(this, O000000o.O0Oo00o);
        this.o00o00O.O0Oo00O = i;
    }

    public void O0oOo0() {
        this.o00o00O = new O000000o(this, O000000o.O0Oo0O0);
    }

    public void O0oOo0O() {
        this.o00o00O = new O000000o(this, O000000o.O0Oo0OO);
    }

    public String getCommand() {
        O000000o o000000o = this.o00o00O;
        if (o000000o == null) {
            return null;
        }
        return o000000o.command;
    }

    public String getType() {
        return "P2pStream";
    }

    public int getVideoQuality() {
        O000000o o000000o = this.o00o00O;
        return (o000000o == null ? null : Integer.valueOf(o000000o.O0Oo00O)).intValue();
    }

    public void ooO0Ooo() {
        this.o00o00O = new O000000o(this, O000000o.O0Oo0O);
    }

    public void stopAudio() {
        this.o00o00O = new O000000o(this, O000000o.O0Oooo);
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.o00o00O == null) {
                return jSONObject;
            }
            JSONObject json = this.o00o00O.toJson();
            if (json == null) {
                return jSONObject;
            }
            jSONObject.put("cameraControl", json);
            return jSONObject;
        } catch (JSONException e) {
            LogUtil.e(TAG, "", (Throwable) e);
            return null;
        }
    }

    public String toJsonString() {
        JSONObject json = toJson();
        if (json == null) {
            return null;
        }
        return json.toString();
    }
}
