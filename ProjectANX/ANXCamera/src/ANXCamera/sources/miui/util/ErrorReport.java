package miui.util;

import android.app.ActivityManager.ProcessErrorStateInfo;
import android.app.ApplicationErrorReport.CrashInfo;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.util.DeviceHelper;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import miui.net.ConnectivityHelper;
import miui.os.DropBoxManager;
import miui.os.SystemProperties;
import miui.provider.ExtraSettings.Secure;
import miui.security.DigestUtils;
import miui.telephony.TelephonyHelper;
import miui.text.ExtraTextUtils;
import org.json.JSONObject;

public class ErrorReport {
    private static final int ANR_MAX_LINE_NUMBER = 300;
    public static final String DROPBOX_TAG = "fc_anr";
    private static final String ERROR_TYPE_ANR = "anr";
    private static final String ERROR_TYPE_FC = "fc";
    public static final int FLAG_SEND_DIRECTLY = 2;
    public static final int FLAG_UNMETERED_NETWORK_ONLY = 1;
    private static final String JSON_ANR_ACTIVITY = "anr_activity";
    private static final String JSON_ANR_CAUSE = "anr_cause";
    private static final String JSON_APP_VESION = "app_version";
    private static final String JSON_BUILD_VERSION = "build_version";
    private static final String JSON_DEVICE = "device";
    private static final String JSON_ERROR_TYPE = "error_type";
    private static final String JSON_EXCEPTION_CLASS = "exception_class";
    private static final String JSON_EXCEPTION_SOURCE_METHOD = "exception_source_method";
    private static final String JSON_IMEI = "imei";
    private static final String JSON_MAC_ADDRESS = "mac_address";
    private static final String JSON_MODEL = "model";
    private static final String JSON_NETWORK = "network";
    private static final String JSON_PACKAGE_NAME = "package_name";
    private static final String JSON_PLATFORM = "platform";
    private static final String JSON_STACK_TRACK = "stack_track";
    private static final String JSON_USER_ALLOWED = "user_allowed";
    private static final String JSON_WIFI_STATE = "wifi_state";
    private static final String TAG = "ErrorReport";

    class AnrDataBuilder implements DataBuilder {
        private Context iContext;
        private String iPackageName;
        private ProcessErrorStateInfo iState;

        public AnrDataBuilder(Context context, String str, ProcessErrorStateInfo processErrorStateInfo) {
            this.iContext = context;
            this.iPackageName = str;
            this.iState = processErrorStateInfo;
        }

        public JSONObject getData() {
            return ErrorReport.getAnrData(this.iContext, this.iPackageName, this.iState);
        }
    }

    interface DataBuilder {
        JSONObject getData();
    }

    class ExceptionDataBuilder implements DataBuilder {
        private Context iContext;
        private CrashInfo iCrashInfo;
        private String iPackageName;

        public ExceptionDataBuilder(Context context, String str, CrashInfo crashInfo) {
            this.iContext = context;
            this.iPackageName = str;
            this.iCrashInfo = crashInfo;
        }

        public JSONObject getData() {
            return ErrorReport.getExceptionData(this.iContext, this.iPackageName, this.iCrashInfo);
        }
    }

    protected ErrorReport() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static JSONObject getAnrData(Context context, String str, ProcessErrorStateInfo processErrorStateInfo) {
        if (processErrorStateInfo == null) {
            return null;
        }
        JSONObject commonData = getCommonData(context, str);
        try {
            commonData.put(JSON_ERROR_TYPE, ERROR_TYPE_ANR);
            commonData.put(JSON_ANR_CAUSE, processErrorStateInfo.shortMsg);
            commonData.put(JSON_ANR_ACTIVITY, processErrorStateInfo.tag == null ? "" : processErrorStateInfo.tag);
            commonData.put(JSON_STACK_TRACK, getAnrStackTrack());
        } catch (Exception e) {
            Log.w(TAG, "Fail to getAnrData", e);
        }
        return commonData;
    }

    /*  JADX ERROR: IF instruction can be used only in fallback mode
        jadx.core.utils.exceptions.CodegenException: IF instruction can be used only in fallback mode
        	at jadx.core.codegen.InsnGen.fallbackOnlyInsn(InsnGen.java:563)
        	at jadx.core.codegen.InsnGen.makeInsnBody(InsnGen.java:469)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:234)
        	at jadx.core.codegen.InsnGen.makeInsn(InsnGen.java:211)
        	at jadx.core.codegen.RegionGen.makeSimpleBlock(RegionGen.java:109)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:55)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
        	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:288)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:68)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
        	at jadx.core.codegen.RegionGen.makeLoop(RegionGen.java:182)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:66)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
        	at jadx.core.codegen.RegionGen.makeTryCatch(RegionGen.java:288)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:68)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
        	at jadx.core.codegen.RegionGen.makeRegionIndent(RegionGen.java:98)
        	at jadx.core.codegen.RegionGen.makeIf(RegionGen.java:127)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:62)
        	at jadx.core.codegen.RegionGen.makeSimpleRegion(RegionGen.java:92)
        	at jadx.core.codegen.RegionGen.makeRegion(RegionGen.java:58)
        	at jadx.core.codegen.MethodGen.addInstructions(MethodGen.java:203)
        	at jadx.core.codegen.ClassGen.addMethod(ClassGen.java:329)
        	at jadx.core.codegen.ClassGen.addMethods(ClassGen.java:263)
        	at jadx.core.codegen.ClassGen.addClassBody(ClassGen.java:226)
        	at jadx.core.codegen.ClassGen.addClassCode(ClassGen.java:111)
        	at jadx.core.codegen.ClassGen.makeClass(ClassGen.java:77)
        	at jadx.core.codegen.CodeGen.generate(CodeGen.java:17)
        	at jadx.core.ProcessClass.process(ProcessClass.java:36)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
        */
    /* JADX WARNING: Code restructure failed: missing block: B:33:0x005d, code lost:
        if (r2 != null) goto L_0x004e;
     */
    /* JADX WARNING: Removed duplicated region for block: B:27:0x0058 A[SYNTHETIC, Splitter:B:27:0x0058] */
    private static java.lang.String getAnrStackTrack() {
        /*
        java.lang.StringBuilder r0 = new java.lang.StringBuilder
        r0.<init>()
        r1 = 0
        java.lang.String r2 = "dalvik.vm.stack-trace-file"
        java.lang.String r2 = miui.os.SystemProperties.get(r2, r1)
        boolean r3 = android.text.TextUtils.isEmpty(r2)
        if (r3 != 0) goto L_0x0060
        java.io.File r3 = new java.io.File
        r3.<init>(r2)
        java.io.BufferedReader r2 = new java.io.BufferedReader     // Catch:{ IOException -> 0x005c, all -> 0x0054 }
        java.io.FileReader r4 = new java.io.FileReader     // Catch:{ IOException -> 0x005c, all -> 0x0054 }
        r4.<init>(r3)     // Catch:{ IOException -> 0x005c, all -> 0x0054 }
        r2.<init>(r4)     // Catch:{ IOException -> 0x005c, all -> 0x0054 }
        r1 = 0
        r3 = r1
    L_0x0023:
        java.lang.String r4 = r2.readLine()     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        if (r4 == 0) goto L_0x004e     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        java.lang.String r5 = "DALVIK THREADS:"     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        boolean r5 = r4.startsWith(r5)     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        if (r5 == 0) goto L_0x0033     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        r1 = 1     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        goto L_0x003e     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
    L_0x0033:
        if (r1 == 0) goto L_0x003e     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        java.lang.String r5 = "-----"     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        boolean r5 = r4.startsWith(r5)     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        if (r5 == 0) goto L_0x003e     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        goto L_0x004e     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
    L_0x003e:
        if (r1 == 0) goto L_0x004a     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        r0.append(r4)     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        r4 = 10     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        r0.append(r4)     // Catch:{ IOException -> 0x005d, all -> 0x0052 }
        int r3 = r3 + 1
    L_0x004a:
        r4 = 300(0x12c, float:4.2E-43)
        if (r3 <= r4) goto L_0x0023
    L_0x004e:
        r2.close()     // Catch:{ IOException -> 0x0060 }
        goto L_0x0060
    L_0x0052:
        r0 = move-exception
        goto L_0x0056
    L_0x0054:
        r0 = move-exception
        r2 = r1
    L_0x0056:
        if (r2 == 0) goto L_0x005b
        r2.close()     // Catch:{ IOException -> 0x005b }
    L_0x005b:
        throw r0
    L_0x005c:
        r2 = r1
    L_0x005d:
        if (r2 == 0) goto L_0x0060
        goto L_0x004e
    L_0x0060:
        java.lang.String r0 = r0.toString()
        return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: miui.util.ErrorReport.getAnrStackTrack():java.lang.String");
    }

    private static JSONObject getCommonData(Context context, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put(JSON_NETWORK, getNetworkName(context));
            jSONObject.put(JSON_DEVICE, getDeviceString());
            jSONObject.put("model", getModel());
            jSONObject.put(JSON_MAC_ADDRESS, getMacAddressMd5());
            jSONObject.put(JSON_IMEI, getIMEIMd5());
            jSONObject.put(JSON_PLATFORM, VERSION.RELEASE);
            jSONObject.put(JSON_BUILD_VERSION, VERSION.INCREMENTAL);
            jSONObject.put("package_name", str);
            jSONObject.put("app_version", getPackageVersion(context, str));
            int i = 1;
            jSONObject.put(JSON_WIFI_STATE, ConnectivityHelper.getInstance().isWifiConnected() ? 1 : 0);
            String str2 = JSON_USER_ALLOWED;
            if (!isUserAllowed(context)) {
                i = 0;
            }
            jSONObject.put(str2, i);
        } catch (Exception e) {
            Log.w(TAG, "Fail to getCommonData", e);
        }
        return jSONObject;
    }

    private static String getDeviceString() {
        String str = SystemProperties.get("ro.product.mod_device", null);
        return TextUtils.isEmpty(str) ? Build.DEVICE : str;
    }

    public static JSONObject getExceptionData(Context context, String str, CrashInfo crashInfo) {
        if (crashInfo == null) {
            return null;
        }
        JSONObject commonData = getCommonData(context, str);
        try {
            commonData.put(JSON_ERROR_TYPE, "fc");
            commonData.put(JSON_EXCEPTION_CLASS, crashInfo.exceptionClassName);
            String str2 = JSON_EXCEPTION_SOURCE_METHOD;
            StringBuilder sb = new StringBuilder();
            sb.append(crashInfo.throwClassName);
            sb.append(".");
            sb.append(crashInfo.throwMethodName);
            commonData.put(str2, sb.toString());
            commonData.put(JSON_STACK_TRACK, crashInfo.stackTrace);
        } catch (Exception e) {
            Log.w(TAG, "Fail to getExceptionData", e);
        }
        return commonData;
    }

    private static String getIMEIMd5() {
        String deviceId = TelephonyHelper.getInstance().getDeviceId();
        return !TextUtils.isEmpty(deviceId) ? ExtraTextUtils.toHexReadable(DigestUtils.get((CharSequence) deviceId, "MD5")) : "";
    }

    private static String getMacAddressMd5() {
        String macAddress = ConnectivityHelper.getInstance().getMacAddress();
        return !TextUtils.isEmpty(macAddress) ? ExtraTextUtils.toHexReadable(DigestUtils.get((CharSequence) macAddress, "MD5")) : "";
    }

    private static String getModel() {
        return Build.MODEL;
    }

    private static String getNetworkName(Context context) {
        return ((TelephonyManager) context.getSystemService("phone")).getNetworkOperatorName();
    }

    private static String getPackageVersion(Context context, String str) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(str, 0);
            int i = packageInfo.applicationInfo.flags;
            if ((i & 1) != 0 || (i & 128) != 0) {
                return VERSION.INCREMENTAL;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(packageInfo.versionName);
            sb.append(" (");
            sb.append(packageInfo.versionCode);
            sb.append(")");
            return sb.toString();
        } catch (NameNotFoundException e) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Fail to find package: ");
            sb2.append(str);
            Log.w(TAG, sb2.toString(), e);
            return "";
        }
    }

    public static boolean isUserAllowed(Context context) {
        return Secure.getInt(context.getContentResolver(), "upload_log_pref", DeviceHelper.IS_DEVELOPMENT_VERSION ? 1 : 0) != 0;
    }

    public static void sendAnrReport(Context context, ProcessErrorStateInfo processErrorStateInfo, int i) {
        if (processErrorStateInfo == null) {
            throw new IllegalArgumentException("state can't be null");
        } else if (isUserAllowed(context)) {
            try {
                sendReportRequestAsync(context, new AnrDataBuilder(context, context.getPackageName(), processErrorStateInfo), i);
            } catch (Exception e) {
                Log.w(TAG, "Fail to sendAnrReport", e);
            }
        }
    }

    private static void sendDropboxRequest(Context context, JSONObject jSONObject) {
        if ("1".equals(SystemProperties.get("sys.boot_completed"))) {
            DropBoxManager.getInstance().addText(DROPBOX_TAG, jSONObject.toString());
            return;
        }
        Log.w(TAG, "Can not request dropbox before boot completed!");
    }

    public static void sendExceptionReport(Context context, String str, CrashInfo crashInfo, int i) {
        if (crashInfo == null) {
            throw new IllegalArgumentException("crashInfo can't be null");
        } else if (isUserAllowed(context)) {
            try {
                sendReportRequestAsync(context, new ExceptionDataBuilder(context, str, crashInfo), i);
            } catch (Exception e) {
                Log.w(TAG, "Fail to sendExceptionReport", e);
            }
        }
    }

    public static void sendExceptionReport(Context context, Throwable th) {
        if (th != null) {
            sendExceptionReport(context, context.getPackageName(), new CrashInfo(th), 1);
            return;
        }
        throw new IllegalArgumentException("throwable can't be null");
    }

    public static boolean sendReportRequest(Context context, JSONObject jSONObject, int i) {
        if (jSONObject != null) {
            if ((i & 2) == 0) {
                sendDropboxRequest(context, jSONObject);
            }
            return false;
        }
        throw new IllegalArgumentException("data can't be null");
    }

    private static void sendReportRequestAsync(final Context context, final DataBuilder dataBuilder, final int i) {
        new AsyncTask() {
            /* access modifiers changed from: protected */
            public Void doInBackground(Void... voidArr) {
                ErrorReport.sendReportRequest(context, dataBuilder.getData(), i);
                return null;
            }
        }.execute(new Void[0]);
    }
}
