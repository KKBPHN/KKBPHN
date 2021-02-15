package O00000Oo.O00000o0.O000000o;

import com.xiaomi.mi_connect_sdk.util.LogUtil;
import org.json.JSONException;
import org.json.JSONObject;

public class O000000o {
    public static final String O0Oo0 = "CMD_START_VIDEO";
    public static final String O0Oo00o = "CMD_START_VIDEOANDAUDIO";
    public static final String O0Oo0O = "CMD_STOP_VIDEO";
    public static final String O0Oo0O0 = "CMD_START_AUDIO";
    public static final String O0Oo0OO = "CMD_STOP_VIDEOANDAUDIO";
    public static final int O0Oo0Oo = 0;
    public static final int O0Oo0o = 1;
    public static final int O0Oo0oO = 2;
    public static final int O0Oo0oo = 3;
    public static final String O0Oooo = "CMD_STOP_AUDIO";
    /* access modifiers changed from: private */
    public int O0Oo00O;
    /* access modifiers changed from: private */
    public String command;
    final /* synthetic */ O00000Oo this$0;

    O000000o(O00000Oo o00000Oo, String str) {
        this.this$0 = o00000Oo;
        this.command = str;
        this.O0Oo00O = 0;
    }

    O000000o(O00000Oo o00000Oo, JSONObject jSONObject) {
        this.this$0 = o00000Oo;
        this.command = jSONObject.optString("command");
        this.O0Oo00O = jSONObject.optInt("videoQuality", 0);
    }

    public JSONObject toJson() {
        JSONObject jSONObject = new JSONObject();
        try {
            if (this.command == null) {
                return jSONObject;
            }
            jSONObject.put("command", this.command);
            jSONObject.put("videoQuality", this.O0Oo00O);
            return jSONObject;
        } catch (JSONException e) {
            LogUtil.e("CameraResource", "", (Throwable) e);
            return null;
        }
    }
}
