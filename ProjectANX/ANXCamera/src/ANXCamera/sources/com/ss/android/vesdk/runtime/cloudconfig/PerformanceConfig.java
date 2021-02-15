package com.ss.android.vesdk.runtime.cloudconfig;

import android.content.Context;
import android.content.SharedPreferences.Editor;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import com.ss.android.ttve.monitor.DeviceInfoDetector;
import com.ss.android.ttve.monitor.MonitorUtils;
import com.ss.android.vesdk.runtime.VERuntime;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import org.json.JSONException;
import org.json.JSONObject;

public class PerformanceConfig {
    public static final String BASE_URL_CHINA = "https://effect.snssdk.com/devicehub/getSettings?";
    public static final String BASE_URL_SINGAPORE = "https://sg-effect.byteoversea.com/devicehub/getSetting?";
    public static final String BASE_URL_TEST = "https://effect.snssdk.com/devicehub/getSettings/?";
    public static final String BASE_URL_US = "https://va-effect.byteoversea.com/devicehub/getSettings?";
    public static final int DISABLE = 2;
    public static final int ENABLE = 1;
    private static final String TAG = "PerfConfig";
    public static final int UNDEFINED = 0;
    @RestrictTo({Scope.LIBRARY})
    public static int gServerLocation = 0;
    private static final String sConfigs = "ShortVideoConfig";
    /* access modifiers changed from: private */
    public static IInjector sInjector = new AutoInjector();
    private static final String sPerfConfigPrefix = "PerfConfig_";
    public static final VECloudConfig sVECloudConfig = new VECloudConfig();

    class ConfigAsyncTask extends AsyncTask {
        private ConfigAsyncTask() {
        }

        /* access modifiers changed from: protected */
        public Void doInBackground(Void... voidArr) {
            String str = "Injector == null. VECloudConfig is not initialized!";
            String str2 = "Parse json result failed! ";
            String str3 = PerformanceConfig.TAG;
            try {
                Context context = VERuntime.getInstance().getContext();
                Map map = DeviceInfoDetector.toMap();
                map.put("package_name", context.getPackageName());
                map.put(MonitorUtils.KEY_MODEL, Build.MODEL);
                map.put("os_version", VERSION.RELEASE);
                Locale locale = Locale.getDefault();
                if (locale != null) {
                    map.put("locale", (locale.getCountry() == null ? "" : locale.getCountry()).toLowerCase());
                }
                map.put("platform", "android");
                String body = HttpRequest.get((CharSequence) PerformanceConfig.getServerUrl(), map, true).body();
                StringBuilder sb = new StringBuilder();
                sb.append("cloud config result = ");
                sb.append(body);
                Log.d(str3, sb.toString());
                try {
                    JSONObject jSONObject = new JSONObject(body);
                    if (PerformanceConfig.sInjector != null) {
                        Map parse = PerformanceConfig.sInjector.parse(jSONObject);
                        if (parse != null) {
                            PerformanceConfig.setPerformanceConfig(VERuntime.getInstance().getContext(), parse);
                        } else {
                            Log.e(str3, str2);
                        }
                        return null;
                    }
                    Log.e(str3, str);
                    throw new IllegalStateException(str);
                } catch (JSONException e) {
                    Log.e(str3, str2, e);
                    e.printStackTrace();
                }
            } catch (Exception e2) {
                Log.e(str3, "Fetch config failed! ", e2);
            }
        }

        /* access modifiers changed from: protected */
        public void onPostExecute(Void voidR) {
        }
    }

    public static void fetch() {
        new ConfigAsyncTask().execute(new Void[0]);
    }

    public static Map getPerformanceConfig(Context context) {
        HashMap hashMap = new HashMap();
        for (Entry entry : context.getSharedPreferences(sConfigs, 0).getAll().entrySet()) {
            if (((String) entry.getKey()).startsWith(sPerfConfigPrefix)) {
                hashMap.put(((String) entry.getKey()).substring(11), (String) entry.getValue());
            }
        }
        return hashMap;
    }

    /* access modifiers changed from: private */
    public static String getServerUrl() {
        int i = gServerLocation;
        String str = BASE_URL_CHINA;
        return i != 0 ? i != 2 ? i != 3 ? str : BASE_URL_SINGAPORE : BASE_URL_US : str;
    }

    @RestrictTo({Scope.LIBRARY})
    public static void restoreFromCache() {
        if (VERuntime.getInstance().getContext() != null) {
            setConfigsFromMap(getPerformanceConfig(VERuntime.getInstance().getContext()));
            return;
        }
        throw new IllegalStateException("Must call VideoSdkCore.init() before.");
    }

    private static void setConfigsFromMap(Map map) {
        IInjector iInjector = sInjector;
        if (iInjector != null) {
            iInjector.inject(map, sVECloudConfig);
        } else {
            Log.e(TAG, "Injector == null. VECloudConfig is not initialized!");
            throw new IllegalStateException("CompileTimeBUG: Injector == null. VECloudConfig won't be initialized!. Consider specify an IInjector instance before compile code.");
        }
    }

    public static void setPerformanceConfig(Context context, Map map) {
        if (map != null) {
            Set<Entry> entrySet = map.entrySet();
            Editor edit = context.getSharedPreferences(sConfigs, 0).edit();
            for (Entry entry : entrySet) {
                StringBuilder sb = new StringBuilder();
                sb.append(sPerfConfigPrefix);
                sb.append((String) entry.getKey());
                edit.putString(sb.toString(), (String) entry.getValue());
            }
            edit.apply();
        }
    }

    public static void setServerLocation(int i) {
        if (i == 0 || i == 2 || i == 3) {
            gServerLocation = i;
            return;
        }
        Log.e(TAG, "SetServerLocation error", new IllegalArgumentException("Only china, us, and sea server is acceptable"));
    }

    @RestrictTo({Scope.LIBRARY})
    public static void updateConfig(Map map) {
        setConfigsFromMap(map);
        setPerformanceConfig(VERuntime.getInstance().getContext(), map);
    }
}
