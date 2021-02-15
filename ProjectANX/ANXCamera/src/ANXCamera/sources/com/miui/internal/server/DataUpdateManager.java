package com.miui.internal.server;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.content.res.TypedArray;
import android.content.res.XmlResourceParser;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;
import android.preference.PreferenceManager;
import android.provider.Settings.System;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.util.Xml;
import com.google.android.apps.photos.api.PhotosOemApi;
import com.miui.internal.net.KeyValuePair;
import com.miui.internal.net.URLConnectionPostBuilder;
import com.miui.internal.util.DataUpdateUtils;
import com.miui.internal.util.DeviceHelper;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Locale;
import java.util.Map;
import miui.R;
import miui.util.AppConstants;
import miui.util.IOUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class DataUpdateManager {
    public static final String ATTRIBUTES_NAME = "data-update";
    private static final String BASE_URL = (DeviceHelper.IS_ALPHA_BUILD ? STAGING_BASE_URL : FORMAL_BASE_URL);
    private static final long CHECK_UPDATE_INTERVAL = 604800000;
    public static final String CHECK_UPDATE_ONLY_WIFI_AVAILABLE = "check_update_only_wifi_available";
    public static final int CHECK_UPDATE_ONLY_WIFI_AVAILABLE_DEFAULT = 1;
    public static final String DATA_UPDATE_RECEIVE = "com.xiaomi.dataUpdate.RECEIVE";
    public static final String DATA_UPDATE_REGISTRATION = "com.xiaomi.dataUpdate.REGISTRATION";
    private static final boolean DBG = true;
    public static final String EXTRA_SERVICE_NAME = "service_name";
    public static final String EXTRA_WATER_MARK = "water_mark";
    private static final String FORMAL_BASE_URL = "aHR0cHM6Ly9hcGkuY29tbS5taXVpLmNvbQ==";
    private static final int HTTP_REQUEST_TIMEOUT_MS = 30000;
    public static final String LAST_UPDATE_TIME = "last_update_time";
    public static final String RECEIVER_META_DATA = "com.xiaomi.dataUpdate";
    private static final String SECRET = "f47c473853236172bf1a709714bda7a9";
    private static final String STAGING_BASE_URL = "aHR0cDovL3RyaWFsLmFwaS5jb21tLm1pdWkuY29t";
    private static final String TAG = "DataUpdateManager";
    public static final String URL;
    private Context mContext;
    private Map mDataUpdateServices;
    private String mUserAgent;

    class DataUpdateResponse {
        public int mCode;
        public JSONObject mData;
        public String mDescription;
        public String mReason;
        public String mResult;

        public DataUpdateResponse(String str) {
            JSONObject jSONObject = new JSONObject(str);
            this.mResult = jSONObject.getString("result");
            this.mDescription = jSONObject.getString("description");
            this.mCode = jSONObject.getInt("code");
            if (this.mCode == 0) {
                this.mData = jSONObject.getJSONObject(PhotosOemApi.PATH_SPECIAL_TYPE_DATA);
                return;
            }
            String str2 = "reason";
            if (jSONObject.has(str2)) {
                this.mReason = jSONObject.getString(str2);
            }
        }

        public String toString() {
            StringBuilder sb;
            String str;
            String str2 = "}";
            String str3 = ", description:";
            String str4 = ", result:";
            String str5 = "{code:";
            if (this.mCode == 0) {
                sb = new StringBuilder();
                sb.append(str5);
                sb.append(this.mCode);
                sb.append(str4);
                sb.append(this.mResult);
                sb.append(str3);
                sb.append(this.mDescription);
                sb.append(", data:");
                str = this.mData.toString();
            } else {
                sb = new StringBuilder();
                sb.append(str5);
                sb.append(this.mCode);
                sb.append(str4);
                sb.append(this.mResult);
                sb.append(", reason:");
                sb.append(this.mReason);
                sb.append(str3);
                str = this.mDescription;
            }
            sb.append(str);
            sb.append(str2);
            return sb.toString();
        }
    }

    class Holder {
        static final DataUpdateManager INSTANCE = new DataUpdateManager(AppConstants.getCurrentApplication());

        private Holder() {
        }
    }

    static {
        StringBuilder sb = new StringBuilder();
        sb.append(getBaseUrl());
        sb.append("/cspmisc/service/version");
        URL = sb.toString();
    }

    private DataUpdateManager(Context context) {
        this.mContext = context;
    }

    public static String getBaseUrl() {
        return DataUpdateUtils.decodeBase64(BASE_URL);
    }

    private String getContent() {
        JSONObject jSONObject = new JSONObject();
        jSONObject.put("version", VERSION.INCREMENTAL);
        String deviceId = ((TelephonyManager) this.mContext.getSystemService("phone")).getDeviceId();
        if (TextUtils.isEmpty(deviceId)) {
            deviceId = getOAID(this.mContext);
        }
        jSONObject.put("imei", deviceId.hashCode());
        JSONArray jSONArray = new JSONArray();
        for (String put : this.mDataUpdateServices.keySet()) {
            jSONArray.put(put);
        }
        jSONObject.put(PhotosOemApi.PATH_SPECIAL_TYPE_DATA, jSONArray);
        return jSONObject.toString();
    }

    public static DataUpdateManager getInstance() {
        return Holder.INSTANCE;
    }

    private String getOAID(Context context) {
        String str = "";
        if (context == null) {
            return str;
        }
        try {
            Class cls = Class.forName("com.android.id.IdentifierManager");
            Object invoke = cls.getMethod("getOAID", new Class[]{Context.class}).invoke(cls.newInstance(), new Object[]{context});
            if (invoke instanceof String) {
                str = (String) invoke;
            }
        } catch (Exception e) {
            Log.e(TAG, "invoke IdentifierManager for OAID error. ", e);
        }
        return str;
    }

    private String getUserAgent() {
        if (this.mUserAgent == null) {
            StringBuilder sb = new StringBuilder();
            sb.append(Build.MODEL);
            sb.append("; MIUI/");
            sb.append(VERSION.INCREMENTAL);
            if (DeviceHelper.IS_ALPHA_BUILD) {
                sb.append(' ');
                sb.append("ALPHA");
            }
            this.mUserAgent = sb.toString();
        }
        return this.mUserAgent;
    }

    /* JADX WARNING: Removed duplicated region for block: B:40:0x0116 A[SYNTHETIC, Splitter:B:40:0x0116] */
    /* JADX WARNING: Removed duplicated region for block: B:45:0x0124  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private String httpPost() {
        Throwable th;
        InputStream inputStream;
        HttpURLConnection httpURLConnection;
        String str = "result";
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        linkedHashMap.put("http.useragent", getUserAgent());
        linkedHashMap.put("Accept-Language", Locale.getDefault().toString());
        String content = getContent();
        String str2 = TAG;
        Log.d(str2, content);
        LinkedList linkedList = new LinkedList();
        HashMap hashMap = new HashMap();
        String str3 = "content";
        hashMap.put(str3, content);
        linkedList.add(new KeyValuePair(str3, content));
        String genUrlSign = DataUpdateUtils.genUrlSign(hashMap, this.mContext.getPackageName(), SECRET);
        linkedList.add(new KeyValuePair("appkey", this.mContext.getPackageName()));
        linkedList.add(new KeyValuePair("sign", genUrlSign));
        try {
            URLConnectionPostBuilder uRLConnectionPostBuilder = new URLConnectionPostBuilder(URL);
            httpURLConnection = uRLConnectionPostBuilder.setTimeout(30000).setHeadParams(linkedHashMap).setDoInputOutput(true).setRequestMethod("POST").build();
            try {
                uRLConnectionPostBuilder.post(linkedList);
                httpURLConnection.connect();
                if (httpURLConnection.getResponseCode() == 200) {
                    inputStream = httpURLConnection.getInputStream();
                    try {
                        String iOUtils = IOUtils.toString(inputStream);
                        if (iOUtils != null) {
                            JSONObject jSONObject = new JSONObject(iOUtils);
                            if (jSONObject.getString(str).equals("ok")) {
                                if (inputStream != null) {
                                    try {
                                        inputStream.close();
                                    } catch (IOException e) {
                                        Log.e(str2, e.getMessage());
                                    }
                                }
                                if (httpURLConnection != null) {
                                    httpURLConnection.disconnect();
                                }
                                return iOUtils;
                            } else if (jSONObject.getString(str).equals(OnNativeInvokeListener.ARG_ERROR)) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("Failed to send to server, ");
                                sb.append(jSONObject.getString("description"));
                                sb.append(",");
                                sb.append(jSONObject.getInt("code"));
                                Log.i(str2, sb.toString());
                            }
                        }
                    } catch (Throwable th2) {
                        th = th2;
                        if (inputStream != null) {
                        }
                        if (httpURLConnection != null) {
                        }
                        throw th;
                    }
                } else {
                    inputStream = null;
                }
                if (inputStream != null) {
                    try {
                        inputStream.close();
                    } catch (IOException e2) {
                        Log.e(str2, e2.getMessage());
                    }
                }
                if (httpURLConnection != null) {
                    httpURLConnection.disconnect();
                }
                return null;
            } catch (Throwable th3) {
                th = th3;
                inputStream = null;
                th = th;
                if (inputStream != null) {
                }
                if (httpURLConnection != null) {
                }
                throw th;
            }
        } catch (Throwable th4) {
            th = th4;
            httpURLConnection = null;
            inputStream = null;
            th = th;
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e3) {
                    Log.e(str2, e3.getMessage());
                }
            }
            if (httpURLConnection != null) {
                httpURLConnection.disconnect();
            }
            throw th;
        }
    }

    private boolean isCheckUpdateOnlyWifiAvailable() {
        return System.getInt(this.mContext.getContentResolver(), CHECK_UPDATE_ONLY_WIFI_AVAILABLE, 1) == 1;
    }

    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) this.mContext.getSystemService("connectivity");
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isAvailable() && (!isCheckUpdateOnlyWifiAvailable() || !connectivityManager.isActiveNetworkMetered());
    }

    private boolean isUpdateExpired() {
        return Math.abs(System.currentTimeMillis() - PreferenceManager.getDefaultSharedPreferences(this.mContext).getLong(LAST_UPDATE_TIME, 0)) > CHECK_UPDATE_INTERVAL;
    }

    private synchronized void queryDataUpdateService() {
        TypedArray obtainAttributes;
        if (this.mDataUpdateServices == null) {
            this.mDataUpdateServices = new HashMap();
            PackageManager packageManager = this.mContext.getPackageManager();
            for (ResolveInfo resolveInfo : packageManager.queryBroadcastReceivers(new Intent(DATA_UPDATE_REGISTRATION), 128)) {
                try {
                    ActivityInfo activityInfo = resolveInfo.activityInfo;
                    XmlResourceParser loadXmlMetaData = activityInfo.loadXmlMetaData(packageManager, RECEIVER_META_DATA);
                    AttributeSet asAttributeSet = Xml.asAttributeSet(loadXmlMetaData);
                    while (true) {
                        int next = loadXmlMetaData.next();
                        if (next == 1 || next == 2) {
                        }
                    }
                    if (!ATTRIBUTES_NAME.equals(loadXmlMetaData.getName())) {
                        Log.d(TAG, "Meta-data does not start with data-update tag");
                    }
                    obtainAttributes = packageManager.getResourcesForApplication(activityInfo.applicationInfo).obtainAttributes(asAttributeSet, R.styleable.DataUpdate);
                    try {
                        for (String trim : obtainAttributes.getString(R.styleable.DataUpdate_serviceName).split(",")) {
                            this.mDataUpdateServices.put(trim.trim(), activityInfo.packageName);
                        }
                    } catch (IllegalArgumentException unused) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append(activityInfo.packageName);
                        sb.append(" micloud-push attrs error");
                        Log.d(str, sb.toString());
                    }
                    obtainAttributes.recycle();
                } catch (Exception e) {
                    e.printStackTrace();
                } catch (Throwable th) {
                    obtainAttributes.recycle();
                    throw th;
                }
            }
            for (String str2 : this.mDataUpdateServices.keySet()) {
                String str3 = TAG;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("{serviceName:");
                sb2.append(str2);
                sb2.append(", packageName:");
                sb2.append((String) this.mDataUpdateServices.get(str2));
                sb2.append("}");
                Log.d(str3, sb2.toString());
            }
        }
    }

    private void tryUpdate() {
        String str = LAST_UPDATE_TIME;
        String str2 = TAG;
        queryDataUpdateService();
        if (this.mDataUpdateServices.size() != 0) {
            String str3 = null;
            try {
                str3 = httpPost();
            } catch (Exception e) {
                e.printStackTrace();
            }
            if (str3 != null) {
                try {
                    DataUpdateResponse dataUpdateResponse = new DataUpdateResponse(str3);
                    Log.d(str2, dataUpdateResponse.toString());
                    if (dataUpdateResponse.mCode == 0) {
                        for (String str4 : this.mDataUpdateServices.keySet()) {
                            if (dataUpdateResponse.mData.has(str4)) {
                                long j = dataUpdateResponse.mData.getLong(str4);
                                String str5 = (String) this.mDataUpdateServices.get(str4);
                                Intent intent = new Intent(DATA_UPDATE_RECEIVE);
                                intent.setPackage(str5);
                                intent.putExtra(EXTRA_WATER_MARK, j);
                                intent.putExtra(EXTRA_SERVICE_NAME, str4);
                                this.mContext.sendBroadcast(intent);
                                Log.d(str2, "tryUpdate success -> Send DATA_UPDATE_RECEIVE");
                            }
                        }
                        SharedPreferences defaultSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this.mContext);
                        long j2 = defaultSharedPreferences.getLong(str, 0);
                        if (j2 > 0) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("The distance last update time:");
                            sb.append(System.currentTimeMillis() - j2);
                            Log.d(str2, sb.toString());
                        }
                        defaultSharedPreferences.edit().putLong(str, System.currentTimeMillis()).apply();
                    }
                } catch (JSONException e2) {
                    e2.printStackTrace();
                }
            }
        }
    }

    public void update() {
        if (DeviceHelper.isDeviceProvisioned(this.mContext) && isUpdateExpired() && isNetworkAvailable()) {
            tryUpdate();
        }
    }
}
