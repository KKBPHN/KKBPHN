package O00000Oo.O00000oO.O000000o;

import O00000o0.O00000Oo;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Size;
import com.android.camera.CameraSettings;
import com.android.camera.CameraSize;
import com.android.camera.Display;
import com.android.camera.HybridZoomingSystem;
import com.android.camera.Util;
import com.android.camera.constant.DurationConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentConfigRatio;
import com.android.camera.log.Log;
import com.android.camera.module.loader.camera2.Camera2DataContainer;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import com.android.camera2.CameraCapabilities;
import com.mi.device.Common;
import com.xiaomi.camera.util.SystemProperties;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Optional;
import java.util.regex.PatternSyntaxException;
import miui.os.Build;
import miui.telephony.phonenumber.CountryCodeConverter;
import miui.widget.AlphabetFastIndexer;

/* renamed from: O00000Oo.O00000oO.O000000o.O00000o reason: case insensitive filesystem */
public class C0122O00000o implements O0000OOo {
    private static final boolean O0Ooo0;
    private static final boolean O0Ooo0O = false;
    private static final int PARALLEL_PERFORMANCE_SETTING = 0;
    private static final int PARALLEL_QUALITY_SETTING = 1;
    private static final int PARALLEL_QUEUE_SIZE = 2;
    private static final String TAG = "O00000o";
    private String O0Ooo00;
    private Common mConfig;

    static {
        boolean z = false;
        if (Util.DEBUG && SystemProperties.getBoolean("camera.feature.clone", false)) {
            z = true;
        }
        O0Ooo0 = z;
    }

    private C0122O00000o() {
        this.mConfig = O00000Oo.O000Oo0(getClassName());
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("init proxy = ");
        sb.append(this.mConfig.getClass());
        Log.d(str, sb.toString());
    }

    private String O000000o(boolean z, String str, String str2) {
        String Oo0OOo0 = z ? Oo0OOo0() : Oo0OOoo();
        if (!Oo0OOo0.contains(str)) {
            return str2;
        }
        Iterator it = Arrays.asList(Oo0OOo0.toLowerCase(Locale.ENGLISH).split(";")).iterator();
        while (true) {
            if (!it.hasNext()) {
                break;
            }
            String str3 = (String) it.next();
            if (str3.contains(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(":");
                str2 = str3.toLowerCase(Locale.ENGLISH).trim().replace(sb.toString(), "");
                break;
            }
        }
        return str2;
    }

    private boolean O00000o0(CameraCapabilities cameraCapabilities) {
        if (cameraCapabilities == null) {
            return false;
        }
        Size OO00OoO = OO00OoO();
        if (OO00OoO == null) {
            return false;
        }
        List<CameraSize> supportedOutputSizeWithAssignedMode = cameraCapabilities.getSupportedOutputSizeWithAssignedMode(32, 33011);
        if (supportedOutputSizeWithAssignedMode != null && supportedOutputSizeWithAssignedMode.size() > 0) {
            for (CameraSize cameraSize : supportedOutputSizeWithAssignedMode) {
                if (cameraSize.width >= OO00OoO.getWidth() && cameraSize.height >= OO00OoO.getHeight()) {
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("isSupportUltraPixelRaw size:");
                    sb.append(cameraSize.toString());
                    Log.d(str, sb.toString());
                    return true;
                }
            }
        }
        return false;
    }

    private int O000OoOO(String str) {
        if (!TextUtils.isEmpty(str)) {
            char charAt = str.charAt(0);
            if (Character.isDigit(charAt)) {
                return Integer.parseInt(String.valueOf(charAt));
            }
        }
        return -1;
    }

    private Size O000OoOo(String str) {
        if (!TextUtils.isEmpty(str)) {
            String substring = str.substring(str.indexOf(58) + 1);
            if (!TextUtils.isEmpty(substring)) {
                String[] split = substring.replace(" ", "").split("x");
                if (split.length >= 2) {
                    return new Size(Integer.valueOf(split[0]).intValue(), Integer.valueOf(split[1]).intValue());
                }
            }
        }
        return null;
    }

    public static String O0ooOO() {
        return SystemProperties.getInt("ro.boot.camera.config", 1) != 0 ? "" : "_pro";
    }

    private String Oo0OOo0() {
        return this.mConfig.Oo0OOo0();
    }

    private String Oo0OOoo() {
        return this.mConfig.Oo0OOoo();
    }

    private int Oo0Oo00() {
        return this.mConfig.Oo0Oo00();
    }

    private boolean Oo0Oooo() {
        return this.mConfig.Oo0Oooo();
    }

    public static C0122O00000o instance() {
        return C0123O00000o0.INSTANCE;
    }

    public List O000000o(boolean z, boolean z2, String str) {
        String O000000o2 = O000000o(false, "capture_inner", str);
        if (z2) {
            O000000o2 = O000000o(false, "video_inner", O000000o2);
        }
        if (z) {
            O000000o2 = this.mConfig.Oo0OOOo()[0];
        }
        List asList = Arrays.asList(O000000o2.split(":"));
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < asList.size(); i++) {
            arrayList.add(Float.valueOf(Float.parseFloat((String) asList.get(i))));
        }
        return arrayList;
    }

    public boolean O000000o(CameraCapabilities cameraCapabilities) {
        return O0oo0O() && !O00000o0(cameraCapabilities);
    }

    public List O00000Oo(boolean z, boolean z2, String str) {
        String O000000o2 = O000000o(false, "capture_ruler", str);
        if (z2) {
            O000000o2 = O000000o(false, "video_ruler", O000000o2);
        }
        if (z) {
            O000000o2 = this.mConfig.Oo0OOOo()[1];
        }
        List asList = Arrays.asList(O000000o2.split(":"));
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < asList.size(); i++) {
            arrayList.add(Integer.valueOf(Integer.parseInt((String) asList.get(i))));
        }
        return arrayList;
    }

    public boolean O0000O0o() {
        return false;
    }

    public boolean O000O00o(int i) {
        if (i == 180 || i == 162 || i == 214) {
            return this.mConfig.Oo0o00O();
        }
        return false;
    }

    public boolean O000O0Oo(boolean z) {
        return z && this.mConfig.Oo0OO00();
    }

    public String O000O0o0(boolean z) {
        return z ? ComponentConfigRatio.RATIO_4X3 : this.mConfig.Oo0OO0();
    }

    public String O000OOoO(String str) {
        String Oo0OOOO = this.mConfig.Oo0OOOO();
        return TextUtils.isEmpty(Oo0OOOO) ? str : Oo0OOOO;
    }

    public boolean O000OOoo(String str) {
        String Oo0o00 = this.mConfig.Oo0o00();
        if (!TextUtils.isEmpty(Oo0o00) && !TextUtils.isEmpty(str)) {
            for (String split : Oo0o00.split(";")) {
                String[] split2 = split.split(",");
                if (split2.length >= 2 && split2[0].equals(str)) {
                    return Boolean.valueOf(split2[1]).booleanValue();
                }
            }
        }
        return false;
    }

    public String O00oOoOo(boolean z) {
        return z ? O0ooOoO() : O0ooO0();
    }

    public int O00oOooO(int i) {
        int Oo0OOoO = this.mConfig.Oo0OOoO();
        return Oo0OOoO < 0 ? i : Oo0OOoO;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:27:0x0053, code lost:
        if (r5.equals(r6) == false) goto L_0x0057;
     */
    /* JADX WARNING: Removed duplicated region for block: B:26:0x0046  */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x006d A[LOOP:0: B:30:0x0067->B:32:0x006d, LOOP_END] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public float[] O00oOooo(int i) {
        boolean z;
        String str;
        List asList;
        String str2 = C0124O00000oO.isSupportedOpticalZoom() ? "1.0:2.0" : "1.0";
        String str3 = BaseEvent.CAPTURE;
        if (!(i == 161 || i == 162 || i == 169 || i == 180 || i == 183)) {
            if (i != 188) {
                if (i != 204) {
                    switch (i) {
                        case 172:
                        case 174:
                            break;
                        case 173:
                            str3 = "supernight";
                            str2 = "0.6:1:2";
                            break;
                        case 175:
                            str3 = "pixel";
                            str2 = "1:2";
                            break;
                    }
                }
            } else {
                str3 = "supermoon";
                str2 = "5:60";
            }
            z = false;
            String O000000o2 = O000000o(true, str3, str2);
            if (z) {
                String str4 = "";
                str = O000000o(true, "video", str4);
            }
            str = O000000o2;
            asList = Arrays.asList(str.split(":"));
            float[] fArr = new float[asList.size()];
            for (int i2 = 0; i2 < asList.size(); i2++) {
                fArr[i2] = Float.parseFloat((String) asList.get(i2));
            }
            return fArr;
        }
        z = true;
        String O000000o22 = O000000o(true, str3, str2);
        if (z) {
        }
        str = O000000o22;
        asList = Arrays.asList(str.split(":"));
        float[] fArr2 = new float[asList.size()];
        while (i2 < asList.size()) {
        }
        return fArr2;
    }

    public boolean O0oOoo() {
        return this.mConfig.O0oOoo();
    }

    public boolean O0oOooO() {
        return this.mConfig.O0oOooO();
    }

    public String O0oOooo() {
        return this.mConfig.O0oOooo();
    }

    public int O0oo() {
        return this.mConfig.O0oo();
    }

    public boolean O0oo0() {
        return this.mConfig.O0oo0();
    }

    public boolean O0oo00() {
        return this.mConfig.O0oo00();
    }

    public boolean O0oo000() {
        return this.mConfig.O0oo000();
    }

    public String O0oo00O() {
        return this.mConfig.O0oo00O();
    }

    public boolean O0oo00o() {
        return this.mConfig.O0oo00o();
    }

    public boolean O0oo0O() {
        return this.mConfig.O0oo0O();
    }

    public String O0oo0O0() {
        return this.mConfig.O0oo0O0();
    }

    public boolean O0oo0OO() {
        return this.mConfig.O0oo0OO();
    }

    public String O0oo0Oo() {
        return this.mConfig.O0oo0Oo();
    }

    public long O0oo0o0() {
        return this.mConfig.O0oo0o0();
    }

    public long O0oo0oO() {
        return this.mConfig.O0oo0oO();
    }

    public int O0oo0oo() {
        return this.mConfig.O0oo0oo();
    }

    public int[] O0ooO() {
        String O0ooO = this.mConfig.O0ooO();
        if (TextUtils.isEmpty(O0ooO)) {
            return null;
        }
        try {
            String[] split = O0ooO.split(":");
            int[] iArr = new int[split.length];
            for (int i = 0; i < split.length; i++) {
                iArr[i] = Integer.parseInt(split[i].trim());
            }
            return iArr;
        } catch (NumberFormatException | PatternSyntaxException unused) {
            Log.w(TAG, "get default favorite modes fails.");
            return null;
        }
    }

    public String O0ooO0() {
        return this.mConfig.O0ooO0();
    }

    public boolean O0ooO00() {
        return this.mConfig.O0ooO00();
    }

    public float O0ooO0O() {
        float O0ooO0O = this.mConfig.O0ooO0O();
        return (Util.getDumpCropFrontZoomRatio().floatValue() == 0.0f || Util.getDumpCropFrontZoomRatio().floatValue() == O0ooO0O) ? O0ooO0O : Util.getDumpCropFrontZoomRatio().floatValue();
    }

    public String O0ooO0o() {
        return this.mConfig.O0ooO0o();
    }

    public int O0ooOO0() {
        return this.mConfig.O0ooOO0();
    }

    public String O0ooOOO() {
        return this.mConfig.O0ooOOO();
    }

    public String O0ooOOo() {
        return this.mConfig.O0ooOOo();
    }

    public int O0ooOo() {
        return this.mConfig.O0ooOo();
    }

    public int O0ooOo0() {
        if (!C0124O00000oO.O0o0OOO || !CameraSettings.isHighQualityPreferred()) {
            return this.mConfig.O0ooOo0();
        }
        return -1;
    }

    public String O0ooOoO() {
        return this.mConfig.O0ooOoO();
    }

    public String O0ooOoo() {
        return this.mConfig.O0ooOoo();
    }

    public int[] O0ooo() {
        return this.mConfig.O0ooo();
    }

    public int O0ooo0() {
        return this.mConfig.Oo0OO0O();
    }

    public int O0ooo00() {
        return O000OoOO(O0ooOoo());
    }

    public String O0ooo0O() {
        return this.mConfig.O0ooo0O();
    }

    public int O0ooo0o() {
        if (C0124O00000oO.OOooOoO() || C0124O00000oO.Oo000O()) {
            return 15;
        }
        return this.mConfig.O0ooo0o();
    }

    public Size O0oooO() {
        String O0oo0Oo = O0oo0Oo();
        if (!TextUtils.isEmpty(O0oo0Oo)) {
            String str = ":";
            if (O0oo0Oo.toLowerCase(Locale.ENGLISH).contains(str)) {
                String[] split = O0oo0Oo.toLowerCase(Locale.ENGLISH).split(str);
                if (split.length > 1) {
                    String[] split2 = split[1].toLowerCase(Locale.ENGLISH).split("x");
                    if (split2.length == 2) {
                        return new Size(Integer.parseInt(split2[0]), Integer.parseInt(split2[1]));
                    }
                }
            }
        }
        return null;
    }

    public float O0oooO0() {
        return this.mConfig.O0oooO0();
    }

    public int O0oooOO() {
        return this.mConfig.O0oooOO();
    }

    public int O0oooOo() {
        return this.mConfig.O0oooOo();
    }

    public int O0oooo() {
        return this.mConfig.O0oooo();
    }

    public int O0oooo0() {
        return this.mConfig.O0oooo0();
    }

    public String O0ooooO() {
        return this.mConfig.O0ooooO();
    }

    public float O0ooooo() {
        return this.mConfig.O0ooooo();
    }

    public int OO0000() {
        return this.mConfig.OO0000();
    }

    public int OO0000o() {
        return this.mConfig.OO0000o();
    }

    public Map OO000OO() {
        String OO000OO = this.mConfig.OO000OO();
        HashMap hashMap = new HashMap();
        Integer valueOf = Integer.valueOf(0);
        hashMap.put(valueOf, valueOf);
        Integer valueOf2 = Integer.valueOf(1);
        hashMap.put(valueOf2, valueOf);
        Integer valueOf3 = Integer.valueOf(2);
        hashMap.put(valueOf3, Integer.valueOf(3));
        if (OO000OO.length() == 0) {
            return hashMap;
        }
        String[] split = OO000OO.toLowerCase(Locale.ENGLISH).split(";");
        int length = split.length;
        int i = 0;
        while (true) {
            if (i >= length) {
                break;
            }
            String[] split2 = split[i].toLowerCase(Locale.ENGLISH).split(",");
            if (Util.TOTAL_MEMORY_GB < Long.parseLong(split2[0])) {
                String[] split3 = split2[1].toLowerCase(Locale.ENGLISH).split(AlphabetFastIndexer.STARRED_TITLE);
                hashMap.put(valueOf, Integer.valueOf(Integer.parseInt(split3[0])));
                String[] split4 = split3[1].toLowerCase(Locale.ENGLISH).split(":");
                hashMap.put(valueOf2, Integer.valueOf(Integer.parseInt(split4[0])));
                hashMap.put(valueOf3, Integer.valueOf(Integer.parseInt(split4[1])));
                break;
            }
            i++;
        }
        return hashMap;
    }

    public int OO000Oo() {
        return this.mConfig.OO000Oo();
    }

    public String OO000o() {
        return this.mConfig.OO000o();
    }

    public int OO000o0() {
        if (Util.TOTAL_MEMORY_GB < 6) {
            return this.mConfig.Oo0OO0o();
        }
        return 11;
    }

    public int OO000oO() {
        if (C0124O00000oO.OOooO0()) {
            return -1;
        }
        if (C0124O00000oO.OOooOOO()) {
            return 1;
        }
        return O000OoOO(OO000o());
    }

    public int OO000oo() {
        return this.mConfig.OO000oo();
    }

    public float[] OO00O() {
        return this.mConfig.OO00O();
    }

    public int OO00O0() {
        return this.mConfig.OO00O0();
    }

    public float[] OO00O0o() {
        return this.mConfig.OO00O0o();
    }

    public int OO00OO() {
        if (C0124O00000oO.Oo0000o()) {
            return 6;
        }
        if (CameraSettings.isUltraPixelOn() && instance().OOOoooO()) {
            return 5;
        }
        if (!CameraSettings.isHighQualityPreferred()) {
            return OO00O0();
        }
        int i = (Util.TOTAL_MEMORY_GB > 6 ? 1 : (Util.TOTAL_MEMORY_GB == 6 ? 0 : -1));
        Common common = this.mConfig;
        return i < 0 ? common.Oo0OOO0() : common.OO00OO();
    }

    public int OO00OOO() {
        return this.mConfig.OO00OOO();
    }

    public long OO00OOo() {
        return this.mConfig.OO00OOo();
    }

    public int OO00Oo() {
        return this.mConfig.OO00Oo();
    }

    public int OO00Oo0() {
        return this.mConfig.OO00Oo0();
    }

    public Size OO00OoO() {
        return C0124O00000oO.OOooOOO() ? new Size(8000, DurationConstant.DURATION_LANDSCAPE_HINT) : O000OoOo(OO000o());
    }

    public int OO00Ooo() {
        return this.mConfig.OO00Ooo();
    }

    public String OO00o00() {
        return this.mConfig.OO00o00();
    }

    public float[] OO00o0O() {
        return this.mConfig.OO00o0O();
    }

    public float[] OO00o0o() {
        return this.mConfig.OO00o0o();
    }

    public int OO00oO() {
        if (OOOoooo()) {
            if (OO0O0OO()) {
                return 1;
            }
            if (OO0OOoo()) {
                return 2;
            }
        }
        return this.mConfig.OO00oO();
    }

    public String OO00oO0() {
        String OO00oO0 = this.mConfig.OO00oO0();
        if (TextUtils.isEmpty(OO00oO0)) {
            return null;
        }
        return OO00oO0;
    }

    public int OO00oOO() {
        return this.mConfig.OO00oOO();
    }

    public int OO00oOo() {
        return this.mConfig.OO00oOo();
    }

    public boolean OO00oo() {
        return OO000oO() < 0 || this.mConfig.OO00oo();
    }

    public boolean OO00oo0() {
        return this.mConfig.OO00oo0();
    }

    public boolean OO00ooO() {
        return this.mConfig.OO00ooO();
    }

    public boolean OO00ooo() {
        return this.mConfig.OO00ooo();
    }

    public boolean OO0O00o() {
        return this.mConfig.OO0O00o();
    }

    public boolean OO0O0O() {
        return this.mConfig.OO0O0O();
    }

    public boolean OO0O0OO() {
        if (this.O0Ooo00 == null) {
            this.O0Ooo00 = SystemProperties.get("ro.boot.hwc");
        }
        return "cn".equalsIgnoreCase(this.O0Ooo00);
    }

    public boolean OO0O0Oo() {
        return Arrays.asList(OOo00O0().toUpperCase(Locale.ENGLISH).split(":")).contains("CAPTURE_INTENT");
    }

    public boolean OO0O0o() {
        return this.mConfig.OO0O0o();
    }

    public boolean OO0O0o0() {
        return this.mConfig.OO0O0o0();
    }

    public boolean OO0O0oO() {
        return this.mConfig.OO0O0oO();
    }

    public boolean OO0O0oo() {
        return this.mConfig.OO0O0oo();
    }

    public boolean OO0OO() {
        return this.mConfig.OO0OO();
    }

    public boolean OO0OO0o() {
        return this.mConfig.OO0OO0o();
    }

    public boolean OO0OOO() {
        return this.mConfig.OO0OOO();
    }

    public boolean OO0OOOO() {
        return this.mConfig.OO0OOOO();
    }

    public boolean OO0OOOo() {
        return this.mConfig.OO0OOOo();
    }

    public boolean OO0OOo() {
        return this.mConfig.OO0OOo();
    }

    public boolean OO0OOo0() {
        return this.mConfig.OO0OOo0();
    }

    public boolean OO0OOoO() {
        return this.mConfig.OO0OOoO();
    }

    public boolean OO0OOoo() {
        if (this.O0Ooo00 == null) {
            this.O0Ooo00 = SystemProperties.get("ro.boot.hwc");
        }
        if ("india".equalsIgnoreCase(this.O0Ooo00)) {
            return true;
        }
        return !TextUtils.isEmpty(this.O0Ooo00) && this.O0Ooo00.toLowerCase(Locale.ENGLISH).startsWith("india_");
    }

    public boolean OO0Oo0() {
        return this.mConfig.OO0Oo0();
    }

    public boolean OO0Oo00() {
        return Build.getRegion().endsWith("IN");
    }

    public boolean OO0Oo0O() {
        return this.mConfig.OO0Oo0O();
    }

    public boolean OO0Oo0o() {
        return this.mConfig.OO0Oo0o();
    }

    public boolean OO0OoO() {
        return this.mConfig.OO0OoO();
    }

    public boolean OO0OoO0() {
        return this.mConfig.OO0OoO0();
    }

    public boolean OO0OoOO() {
        return this.mConfig.OO0OoOO();
    }

    public boolean OO0OoOo() {
        return this.mConfig.OO0OoOo();
    }

    public boolean OO0Ooo() {
        if (!C0124O00000oO.O0o0O0 || !C0124O00000oO.O0o0ooO) {
            return Arrays.asList(OOo00O0().toUpperCase(Locale.ENGLISH).split(":")).contains("PRO");
        }
        return false;
    }

    public boolean OO0Ooo0() {
        return Arrays.asList(OOo00O0().toUpperCase(Locale.ENGLISH).split(":")).contains("NO_PIXEL");
    }

    public boolean OO0OooO() {
        return this.mConfig.OO0OooO();
    }

    public boolean OO0Oooo() {
        return this.mConfig.OO0Oooo();
    }

    public boolean OO0o() {
        return this.mConfig.OO0o();
    }

    public boolean OO0o0() {
        return this.mConfig.OO0o0();
    }

    public boolean OO0o00() {
        return this.mConfig.OO0o00();
    }

    public boolean OO0o000() {
        return C0124O00000oO.O0o0ooO && this.mConfig.OO0o000();
    }

    public boolean OO0o00O() {
        return this.mConfig.OO0o00O();
    }

    public boolean OO0o00o() {
        return this.mConfig.OO0o00o();
    }

    public boolean OO0o0O0() {
        return this.mConfig.OO0o0O0();
    }

    public boolean OO0o0OO() {
        return Arrays.asList(OOo00O0().toUpperCase(Locale.ENGLISH).split(":")).contains("MACRO");
    }

    public boolean OO0o0Oo() {
        return this.mConfig.OO0o0Oo();
    }

    public boolean OO0o0o() {
        return this.mConfig.OO0o0o();
    }

    public boolean OO0o0o0() {
        return this.mConfig.OO0o0o0();
    }

    public boolean OO0o0oO() {
        return this.mConfig.OO0o0oO();
    }

    public boolean OO0o0oo() {
        return this.mConfig.OO0o0oo();
    }

    public boolean OO0oO0() {
        return this.mConfig.OO0oO0();
    }

    public boolean OO0oO00() {
        return this.mConfig.OO0oO00();
    }

    public boolean OO0oO0O() {
        return this.mConfig.OO0oO0O();
    }

    public boolean OO0oO0o() {
        return this.mConfig.OO0oO0o();
    }

    public boolean OO0oOO() {
        return this.mConfig.OO0oOO();
    }

    public boolean OO0oOO0() {
        return this.mConfig.OO0oOO0();
    }

    public boolean OO0oOOO() {
        return this.mConfig.OO0oOOO();
    }

    public boolean OO0oOOo() {
        return this.mConfig.OO0oOOo();
    }

    public boolean OO0oOo() {
        if (VERSION.SDK_INT < 28) {
            return false;
        }
        return this.mConfig.OO0oOo();
    }

    public boolean OO0oOo0() {
        return this.mConfig.OO0oOo0();
    }

    public boolean OO0oOoO() {
        return this.mConfig.OO0oOoO();
    }

    public boolean OO0oOoo() {
        return this.mConfig.OO0oOoo();
    }

    public boolean OO0oo() {
        return this.mConfig.OO0oo();
    }

    public boolean OO0oo0() {
        return this.mConfig.OO0oo0();
    }

    public boolean OO0oo00() {
        return this.mConfig.OO0oo00() || O0Ooo0;
    }

    public boolean OO0oo0O() {
        return this.mConfig.OO0oo0O();
    }

    public boolean OO0oo0o() {
        return this.mConfig.OO0oo0o();
    }

    public String OO0ooO() {
        return this.mConfig.OO0ooO();
    }

    public boolean OO0ooO0() {
        return this.mConfig.OO0ooO0();
    }

    public boolean OO0ooOO() {
        return this.mConfig.OO0ooOO();
    }

    public boolean OO0ooOo() {
        return this.mConfig.OO0ooOo();
    }

    public boolean OO0ooo() {
        if (!C0124O00000oO.OOooOoo() && VERSION.SDK_INT != 28) {
            return this.mConfig.OO0ooo();
        }
        return false;
    }

    public boolean OO0ooo0() {
        if (VERSION.SDK_INT == 28) {
            return false;
        }
        return this.mConfig.OO0ooo0();
    }

    public boolean OO0oooO() {
        return this.mConfig.OO0oooO();
    }

    public boolean OO0oooo() {
        Camera2DataContainer instance = Camera2DataContainer.getInstance();
        boolean z = false;
        boolean booleanValue = ((Boolean) Optional.ofNullable(instance.getCapabilities(instance.getMainBackCameraId())).map(lambda.INSTANCE).orElse(Boolean.valueOf(false))).booleanValue();
        boolean z2 = SystemProperties.getBoolean("miuicamera.dualvideo.show", false);
        boolean OO0oooo = this.mConfig.OO0oooo();
        if (C0124O00000oO.isMTKPlatform()) {
            if (z2 || (booleanValue && OO0oooo)) {
                z = true;
            }
            return z;
        }
        if (z2 || OO0oooo) {
            z = true;
        }
        return z;
    }

    public boolean OOO() {
        return this.mConfig.OOO();
    }

    public boolean OOO00() {
        return !C0124O00000oO.O0o0ooO && this.mConfig.OOO00();
    }

    public boolean OOO000o() {
        return this.mConfig.O0ooOOO() != null;
    }

    public boolean OOO00O0() {
        return this.mConfig.OOO00O0();
    }

    public boolean OOO00Oo() {
        return this.mConfig.OOO00Oo();
    }

    public boolean OOO00o() {
        return this.mConfig.O00000oo();
    }

    public boolean OOO00o0() {
        return this.mConfig.OOO00o0();
    }

    public boolean OOO00oO() {
        return this.mConfig.OOO00oO();
    }

    public boolean OOO00oo() {
        return this.mConfig.OOO00oo();
    }

    public boolean OOO0O() {
        return this.mConfig.OOO0O();
    }

    public boolean OOO0O0O() {
        return this.mConfig.OOO0O0O();
    }

    public boolean OOO0O0o() {
        return this.mConfig.OOO0O0o();
    }

    public boolean OOO0OO0() {
        return this.mConfig.OOO0OO0();
    }

    public boolean OOO0OOO() {
        return this.mConfig.OOO0OOO();
    }

    public boolean OOO0OOo() {
        return this.mConfig.OOO0OOo();
    }

    public boolean OOO0Oo() {
        return this.mConfig.OOO0Oo();
    }

    public boolean OOO0Oo0() {
        return this.mConfig.OOO0Oo0() && !Build.IS_INTERNATIONAL_BUILD;
    }

    public boolean OOO0OoO() {
        if (Util.isGlobalVersion() || !DataRepository.dataItemGlobal().isNormalIntent()) {
            return false;
        }
        return this.mConfig.OOO0OoO();
    }

    public boolean OOO0Ooo() {
        return this.mConfig.OOO0Ooo();
    }

    public boolean OOO0o() {
        return OOO00o() && Oo0Oo00() == 1;
    }

    public boolean OOO0o0() {
        return this.mConfig.OOO0o0();
    }

    public boolean OOO0o00() {
        return this.mConfig.OOO0o00();
    }

    public boolean OOO0o0O() {
        return this.mConfig.OOO0o0O();
    }

    public boolean OOO0o0o() {
        return android.os.SystemProperties.get("ro.product.cpu.abi", "").contains(CountryCodeConverter.NZ) && !android.os.Build.DEVICE.equals("laurus") && !android.os.Build.DEVICE.equals("ginkgo") && !android.os.Build.DEVICE.equals("willow");
    }

    public boolean OOO0oO() {
        return this.mConfig.OOO0oO();
    }

    public boolean OOO0oO0() {
        return OOO00o() && Oo0Oo00() == 0;
    }

    public boolean OOO0oOO() {
        return this.mConfig.OOO0oOO();
    }

    public boolean OOO0oOo() {
        return this.mConfig.OOO0oOo();
    }

    public boolean OOO0oo() {
        return this.mConfig.OOO0oo();
    }

    public boolean OOO0oo0() {
        return this.mConfig.OOO0oo0();
    }

    public boolean OOO0ooO() {
        return this.mConfig.OOO0ooO();
    }

    public boolean OOO0ooo() {
        boolean z = false;
        if (SystemProperties.getBoolean("miuicamera.mimoji.show", false)) {
            return true;
        }
        if (this.mConfig.OOO0ooo() && !Build.IS_INTERNATIONAL_BUILD) {
            z = true;
        }
        return z;
    }

    public boolean OOOO0() {
        return this.mConfig.OOOO0();
    }

    public boolean OOOO00O() {
        return this.mConfig.OOOO00O();
    }

    public boolean OOOO00o() {
        return this.mConfig.OOOO00o();
    }

    public boolean OOOO0O() {
        return this.mConfig.OOOO0O();
    }

    public boolean OOOO0OO() {
        return this.mConfig.OOOO0OO();
    }

    public boolean OOOO0Oo() {
        return this.mConfig.OOOO0Oo();
    }

    public boolean OOOO0o0() {
        return this.mConfig.OOOO0o0();
    }

    public boolean OOOO0oO() {
        return this.mConfig.OOOO0oO();
    }

    public boolean OOOO0oo() {
        return this.mConfig.OOOO0oo();
    }

    public boolean OOOOO() {
        return this.mConfig.OOOOO();
    }

    public boolean OOOOO0() {
        return this.mConfig.OOOOO0();
    }

    public boolean OOOOO00() {
        return this.mConfig.OOOOO00();
    }

    public boolean OOOOO0o() {
        return this.mConfig.OOOOO0o();
    }

    public boolean OOOOOO() {
        return this.mConfig.OOOOOO();
    }

    public boolean OOOOOOO() {
        return this.mConfig.OOOOOOO();
    }

    public boolean OOOOOOo() {
        return this.mConfig.OOOOOOo();
    }

    public boolean OOOOOo() {
        if (Util.isGlobalVersion() || !Util.isLocaleChinese()) {
            return false;
        }
        return this.mConfig.OOOOOo();
    }

    public boolean OOOOOo0() {
        return this.mConfig.OOOOOo0();
    }

    public boolean OOOOOoO() {
        return this.mConfig.OOOOOoO();
    }

    public boolean OOOOOoo() {
        if (Util.isGlobalVersion()) {
            return false;
        }
        if (!C0124O00000oO.O0o00o || VERSION.SDK_INT != 28) {
            return this.mConfig.OOOOOoo();
        }
        return false;
    }

    public boolean OOOOo() {
        return this.mConfig.OOOOo();
    }

    public boolean OOOOo0() {
        return this.mConfig.OOOOo0();
    }

    public boolean OOOOo00() {
        return this.mConfig.OOOOo00();
    }

    public boolean OOOOo0O() {
        return this.mConfig.OOOOo0O();
    }

    public boolean OOOOo0o() {
        return this.mConfig.OOOOo0o();
    }

    public boolean OOOOoO() {
        return this.mConfig.OOOOoO();
    }

    public boolean OOOOoO0() {
        return this.mConfig.OOOOoO0();
    }

    public boolean OOOOoOO() {
        return this.mConfig.OOOOoOO();
    }

    public boolean OOOOoOo() {
        return this.mConfig.OOOOoOo();
    }

    public boolean OOOOoo() {
        return this.mConfig.OOOOoo();
    }

    public boolean OOOOoo0() {
        return this.mConfig.OOOOoo0();
    }

    public boolean OOOOooO() {
        return this.mConfig.OOOOooO();
    }

    public boolean OOOOooo() {
        return this.mConfig.OOOOooo();
    }

    public boolean OOOo() {
        return this.mConfig.OOOo();
    }

    public boolean OOOo0() {
        return this.mConfig.OOOo0();
    }

    public boolean OOOo00() {
        return this.mConfig.OOOo00();
    }

    public boolean OOOo000() {
        return this.mConfig.OOOo000();
    }

    public boolean OOOo00O() {
        return this.mConfig.OOOo00O();
    }

    public boolean OOOo00o() {
        return this.mConfig.OOOo00o();
    }

    public boolean OOOo0O() {
        return this.mConfig.OOOo0O();
    }

    public boolean OOOo0O0() {
        return this.mConfig.OOOo0O0();
    }

    public boolean OOOo0OO() {
        return this.mConfig.OOOo0OO();
    }

    public boolean OOOo0Oo() {
        return this.mConfig.OOOo0Oo();
    }

    public boolean OOOo0o() {
        if (C0124O00000oO.OOooO00()) {
            return false;
        }
        return this.mConfig.OOOo0o();
    }

    public boolean OOOo0o0() {
        return !OOOo0O0() && !ooOOo00() && this.mConfig.OOOo0o0();
    }

    public boolean OOOo0oO() {
        boolean z = false;
        if (VERSION.SDK_INT == 30 && (C0124O00000oO.O0o0oO0 || C0124O00000oO.O0o0oO)) {
            return false;
        }
        if (VERSION.SDK_INT >= 28 && this.mConfig.OOOo0oO()) {
            z = true;
        }
        return z;
    }

    public boolean OOOo0oo() {
        return this.mConfig.OOOo0oo();
    }

    public boolean OOOoO() {
        return this.mConfig.OOOoO();
    }

    public boolean OOOoO0() {
        return this.mConfig.OOOoO0();
    }

    public boolean OOOoO00() {
        if (!C0124O00000oO.O0o0O0 || !C0124O00000oO.O0o0ooO) {
            return Arrays.asList(OOo00O0().toUpperCase(Locale.ENGLISH).split(":")).contains("ULTRA_WIDE");
        }
        return false;
    }

    public boolean OOOoO0O() {
        return this.mConfig.OOOoO0O();
    }

    public boolean OOOoO0o() {
        return this.mConfig.OOOoO0o();
    }

    public String OOOoOO() {
        return this.mConfig.OOOoOO();
    }

    public String OOOoOO0() {
        return this.mConfig.OOOoOO0();
    }

    public String OOOoOOO() {
        return this.mConfig.OOOoOOO();
    }

    public boolean OOOoOOo() {
        return this.mConfig.OOOoOOo();
    }

    public boolean OOOoOo() {
        return this.mConfig.OOOoOo();
    }

    public boolean OOOoOo0() {
        return (OO0OOoo() && OOO0Ooo()) || OO0oo0();
    }

    public boolean OOOoOoO() {
        String OOoOoO0 = OOoOoO0();
        return TextUtils.isEmpty(OOoOoO0) ? !OOoOoOo() : Boolean.parseBoolean(OOoOoO0);
    }

    public boolean OOOoOoo() {
        return this.mConfig.OOoO();
    }

    public int OOOoo() {
        return this.mConfig.OOOoo();
    }

    public int OOOoo0() {
        return this.mConfig.OOOoo0();
    }

    public int OOOoo00() {
        return this.mConfig.OOOoo00();
    }

    public boolean OOOoo0O() {
        return this.mConfig.OOOoo0O();
    }

    public int OOOoo0o() {
        return this.mConfig.OOOoo0o();
    }

    public boolean OOOooO() {
        return this.mConfig.OOOooO();
    }

    public boolean OOOooO0() {
        return this.mConfig.OOOooO0();
    }

    public boolean OOOooOO() {
        return ((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth()) >= 2.1666667f && this.mConfig.OOOooOO();
    }

    public boolean OOOooOo() {
        return ((double) Math.abs((((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth())) - 2.1111112f)) <= 0.02d && this.mConfig.OOOooOo();
    }

    public boolean OOOooo() {
        return this.mConfig.OOOooo();
    }

    public boolean OOOooo0() {
        return ((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth()) >= 2.2222223f && this.mConfig.OOOooo0();
    }

    public boolean OOOoooO() {
        return this.mConfig.OOOoooO();
    }

    public boolean OOOoooo() {
        return this.mConfig.OOOoooo() && (OO0OOoo() || OO0O0OO()) && OOo00O();
    }

    public boolean OOo0() {
        return this.mConfig.OOo0();
    }

    public boolean OOo00() {
        return this.mConfig.OOo00();
    }

    public boolean OOo000() {
        return this.mConfig.OOo000();
    }

    public boolean OOo0000() {
        return this.mConfig.OOo0000();
    }

    public boolean OOo000O() {
        if (!OOo000o()) {
            return OOo00O();
        }
        if (!this.mConfig.OOo000O() || !OOo000o() || (!(163 == DataRepository.dataItemGlobal().getCurrentMode() || 165 == DataRepository.dataItemGlobal().getCurrentMode()) || CameraSettings.getCameraId() != 0 || CameraSettings.isUltraPixelOn() || ((double) CameraSettings.getRetainZoom(DataRepository.dataItemGlobal().getCurrentMode())) < 1.0d)) {
            Log.i(TAG, "Algo up disabled for mm-camera");
            return false;
        }
        Log.i(TAG, "Algo up enabled for mm-camera");
        return true;
    }

    public boolean OOo000o() {
        return this.mConfig.OOo000o();
    }

    public boolean OOo00O() {
        return VERSION.SDK_INT > 28 ? Oo0Oooo() : this.mConfig.OOo000O();
    }

    public String OOo00O0() {
        return this.mConfig.OOo00O0();
    }

    public boolean OOo00OO() {
        return this.mConfig.OOo00OO() && !SystemProperties.getBoolean("close.append.yuv", false);
    }

    public boolean OOo00Oo() {
        return this.mConfig.OOo00Oo();
    }

    public boolean OOo00o() {
        return this.mConfig.OOo00o();
    }

    public boolean OOo00o0() {
        return this.mConfig.OOo00o0();
    }

    public boolean OOo00oO() {
        return this.mConfig.OOo00oO();
    }

    public boolean OOo00oo() {
        return this.mConfig.OOo00oo();
    }

    public boolean OOo0O0() {
        return this.mConfig.OOo0O0() && !C0124O00000oO.isMTKPlatform();
    }

    public boolean OOo0O00() {
        return this.mConfig.OOo0O00();
    }

    public boolean OOo0O0O() {
        return this.mConfig.OOo0O0O();
    }

    public boolean OOo0O0o() {
        return this.mConfig.OOo0O0o();
    }

    public boolean OOo0OO() {
        if (C0124O00000oO.OOooo0O()) {
            return false;
        }
        return this.mConfig.OOo0OO();
    }

    public boolean OOo0OO0() {
        return this.mConfig.OOo0OO0();
    }

    public boolean OOo0OOO() {
        return this.mConfig.OOo0OOO();
    }

    public boolean OOo0OOo() {
        return this.mConfig.OOo0OOo();
    }

    public boolean OOo0Oo() {
        return this.mConfig.OOo0Oo();
    }

    public boolean OOo0Oo0() {
        return this.mConfig.OOo0Oo0() && OO0OOoo();
    }

    public boolean OOo0OoO() {
        return this.mConfig.OOo0OoO() && OO0OOoo();
    }

    public boolean OOo0Ooo() {
        return this.mConfig.Oo0o000();
    }

    public boolean OOo0o() {
        String O0oo0O0 = O0oo0O0();
        if (O0oo0O0 == null) {
            return false;
        }
        String str = "notelemfnr";
        if (!O0oo0O0.toLowerCase(Locale.ENGLISH).contains(str)) {
            return false;
        }
        String[] split = O0oo0O0.toLowerCase(Locale.ENGLISH).split(";");
        if (split.length > 0) {
            for (String lowerCase : split) {
                String[] split2 = lowerCase.toLowerCase(Locale.ENGLISH).split(":");
                if (split2[0].equals(str)) {
                    return split2[1].equals("1");
                }
            }
        }
        return false;
    }

    public boolean OOo0o0() {
        return this.mConfig.OOo0o0();
    }

    public boolean OOo0o00() {
        return this.mConfig.OOo0o00();
    }

    public boolean OOo0o0O() {
        return this.mConfig.OOo0o0O();
    }

    public boolean OOo0o0o() {
        String O0oo0O0 = O0oo0O0();
        if (O0oo0O0 == null) {
            return false;
        }
        String str = "mfnr";
        if (!O0oo0O0.toLowerCase(Locale.ENGLISH).contains(str)) {
            return false;
        }
        String[] split = O0oo0O0.toLowerCase(Locale.ENGLISH).split(";");
        if (split.length > 0) {
            for (String lowerCase : split) {
                String[] split2 = lowerCase.toLowerCase(Locale.ENGLISH).split(":");
                if (split2[0].equals(str)) {
                    return split2[1].equals("1");
                }
            }
        }
        return false;
    }

    public boolean OOo0oO() {
        return this.mConfig.OOo0oO();
    }

    public boolean OOo0oO0() {
        return this.mConfig.OOo0oO0();
    }

    public boolean OOo0oOO() {
        return this.mConfig.OOo0oOO();
    }

    public boolean OOo0oOo() {
        return false;
    }

    public boolean OOo0oo() {
        return this.mConfig.OOo0oo();
    }

    public boolean OOo0oo0() {
        return this.mConfig.OOo0oo0();
    }

    public boolean OOo0ooO() {
        return true;
    }

    public boolean OOo0ooo() {
        return this.mConfig.OOo0ooo();
    }

    public boolean OOoO() {
        return this.mConfig.OOoO();
    }

    public boolean OOoO0() {
        return this.mConfig.OOoO0();
    }

    public boolean OOoO00() {
        return this.mConfig.OOoO00();
    }

    public boolean OOoO000() {
        return this.mConfig.OOoO000();
    }

    public boolean OOoO00O() {
        return (OO0OOoo() || OO0Oo00()) && this.mConfig.OOoO00O();
    }

    public boolean OOoO00o() {
        return this.mConfig.OOoO00o();
    }

    public boolean OOoO0O() {
        return Util.TOTAL_MEMORY_GB > 6 && this.mConfig.OOoO0O();
    }

    public boolean OOoO0O0() {
        return this.mConfig.OOoO0O0();
    }

    public boolean OOoO0OO() {
        return this.mConfig.OOoO0OO();
    }

    public boolean OOoO0Oo() {
        if (DataRepository.dataItemGlobal().getIntentType() != 1 || !C0124O00000oO.isMTKPlatform()) {
            return this.mConfig.OOoO0Oo();
        }
        return false;
    }

    public boolean OOoO0o() {
        return this.mConfig.OOoO0o();
    }

    public boolean OOoO0o0() {
        return this.mConfig.OOoO0o0();
    }

    public boolean OOoO0oO() {
        return this.mConfig.OOoO0oO();
    }

    public boolean OOoO0oo() {
        String O0oo0O0 = O0oo0O0();
        if (O0oo0O0 == null) {
            return false;
        }
        String str = "telesr";
        if (!O0oo0O0.toLowerCase(Locale.ENGLISH).contains(str)) {
            return false;
        }
        String[] split = O0oo0O0.toLowerCase(Locale.ENGLISH).split(";");
        if (split.length > 0) {
            for (String lowerCase : split) {
                String[] split2 = lowerCase.toLowerCase(Locale.ENGLISH).split(":");
                if (split2[0].equals(str)) {
                    return split2[1].equals("1");
                }
            }
        }
        return false;
    }

    public boolean OOoOO() {
        return this.mConfig.OOoOO();
    }

    public boolean OOoOO00() {
        return this.mConfig.OOoOO00();
    }

    public boolean OOoOO0O() {
        return this.mConfig.OOoOO0O();
    }

    public boolean OOoOO0o() {
        return this.mConfig.OOoOO0o();
    }

    public boolean OOoOOO() {
        return this.mConfig.OOoOOO();
    }

    public boolean OOoOOO0() {
        int i = (Util.TOTAL_MEMORY_GB > 6 ? 1 : (Util.TOTAL_MEMORY_GB == 6 ? 0 : -1));
        Common common = this.mConfig;
        return i < 0 ? common.Oo0o00o() : common.OOoOOO0();
    }

    public boolean OOoOOOO() {
        return this.mConfig.OOoOOOO();
    }

    public boolean OOoOOOo() {
        return this.mConfig.OOoOOOo();
    }

    public boolean OOoOOo() {
        return this.mConfig.OOoOOo();
    }

    public boolean OOoOOo0() {
        return this.mConfig.OOoOOo0();
    }

    public boolean OOoOOoO() {
        return this.mConfig.OOoOOoO();
    }

    public boolean OOoOOoo() {
        return this.mConfig.OOoOOoo();
    }

    public boolean OOoOo() {
        boolean z = SystemProperties.getBoolean("miuicamera.sat.video", false);
        if (z) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("sat video debug prop:");
            sb.append(z);
            Log.d(str, sb.toString());
            return z;
        } else if (!C0124O00000oO.O0o0ooO || C0124O00000oO.O0o0O0O) {
            return this.mConfig.OOoOo();
        } else {
            return false;
        }
    }

    public boolean OOoOo0() {
        if (!HybridZoomingSystem.IS_3_OR_MORE_SAT) {
            return false;
        }
        return this.mConfig.OOoOo0();
    }

    public boolean OOoOo00() {
        return this.mConfig.OOoOo00();
    }

    public boolean OOoOo0O() {
        return this.mConfig.OOoOo0O();
    }

    public boolean OOoOo0o() {
        return this.mConfig.OOoOo0o();
    }

    public boolean OOoOoO() {
        return this.mConfig.OOoOoO();
    }

    public String OOoOoO0() {
        return this.mConfig.OOoOoO0();
    }

    public boolean OOoOoOO() {
        return this.mConfig.OOoOoOO();
    }

    public boolean OOoOoOo() {
        return this.mConfig.OOoOoOo();
    }

    public boolean OOoOoo0() {
        return OOOo0O0() || ooOOo00() || OOOo0o0() || OOOo0OO();
    }

    public boolean OOoo00() {
        return this.mConfig.OOoo00();
    }

    public boolean OOooOo() {
        return this.mConfig.OOooOo();
    }

    public boolean OOoooo() {
        return this.mConfig.OOoooo();
    }

    public boolean Oo0Oo0O() {
        return this.mConfig.Oo0Oo0O();
    }

    public boolean Oo0Ooo0() {
        return this.mConfig.Oo0Ooo0();
    }

    public int OoOOO() {
        return this.mConfig.OoOOO();
    }

    public boolean OoOOoOo() {
        return this.mConfig.OoOOoOo();
    }

    public String getClassName() {
        String str = android.os.Build.DEVICE;
        char c = (str.hashCode() == 3321813 && str.equals("lime")) ? (char) 0 : 65535;
        if (c == 0 && Util.isInternationalBuild()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("gl");
            str = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(O0ooOO());
        return sb2.toString();
    }

    public Common getConfig() {
        return this.mConfig;
    }

    public int getRawSuperNightImpl() {
        return this.mConfig.getRawSuperNightImpl();
    }

    public boolean is4K30FpsEISSupported() {
        return this.mConfig.is4K30FpsEISSupported();
    }

    public boolean isCinematicPhotoSupported() {
        if (!C0124O00000oO.O0o00o || VERSION.SDK_INT != 28) {
            return this.mConfig.isCinematicPhotoSupported();
        }
        return false;
    }

    public boolean isHighQualityPreferred() {
        return this.mConfig.isHighQualityPreferred();
    }

    public boolean isPad() {
        return this.mConfig.isPad();
    }

    public boolean isSRRequireReprocess() {
        return this.mConfig.isSRRequireReprocess();
    }

    public boolean isSupportBeautyBody() {
        return this.mConfig.isSupportBeautyBody();
    }

    public boolean isSupportBokehAdjust() {
        return this.mConfig.isSupportBokehAdjust();
    }

    public boolean isSupportMacroMode() {
        return this.mConfig.isSupportMacroMode();
    }

    public boolean isSupportNormalWideLDC() {
        return this.mConfig.isSupportNormalWideLDC();
    }

    public boolean isSupportShortVideoBeautyBody() {
        return this.mConfig.isSupportShortVideoBeautyBody();
    }

    public boolean isSupportSlowMotionVideoEditor() {
        return this.mConfig.isSupportSlowMotionVideoEditor();
    }

    public boolean isSupportUltraWide() {
        return this.mConfig.isSupportUltraWide();
    }

    public boolean isSupportUltraWideLDC() {
        return this.mConfig.isSupportUltraWideLDC();
    }

    public int o00O0oO0() {
        return this.mConfig.o00O0oO0();
    }

    public boolean o00OOOOO() {
        return this.mConfig.o00OOOOO();
    }

    public boolean o0OOoOoo() {
        return ((double) Math.abs((((float) Display.getWindowHeight()) / ((float) Display.getWindowWidth())) - 2.0833333f)) < 0.02d && this.mConfig.o0OOoOoo();
    }

    public Boolean o0ooo0OO() {
        return Boolean.valueOf(this.mConfig.o0ooo0OO());
    }

    public boolean oO0OO0() {
        String O0oo0Oo = O0oo0Oo();
        if (TextUtils.isEmpty(O0oo0Oo)) {
            return false;
        }
        String str = ":";
        if (!O0oo0Oo.toLowerCase(Locale.ENGLISH).contains(str)) {
            return Boolean.parseBoolean(O0oo0Oo);
        }
        String[] split = O0oo0Oo.toLowerCase(Locale.ENGLISH).split(str);
        if (split.length > 0) {
            return Boolean.parseBoolean(split[0]);
        }
        return false;
    }

    public boolean oOo00() {
        return this.mConfig.oOo00();
    }

    public int ooO00O0() {
        return this.mConfig.ooO00O0();
    }

    public boolean ooOOo00() {
        return !OOOo0O0() && this.mConfig.ooOOo00();
    }

    public boolean ooOo() {
        return this.mConfig.ooOo();
    }

    public boolean shouldCheckSatFallbackState() {
        return this.mConfig.shouldCheckSatFallbackState();
    }

    public boolean supportColorEnhance() {
        return this.mConfig.supportColorEnhance() && OO0OOoo();
    }
}
