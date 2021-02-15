package com.android.camera.data.cloud;

import android.app.Application;
import android.content.SharedPreferences.Editor;
import android.os.Build;
import android.provider.MiuiSettings.SettingsCloudData;
import android.text.TextUtils;
import android.util.SparseArray;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.aiwatermark.util.WatermarkConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.data.cloud.DataCloud.CloudFeature;
import com.android.camera.data.cloud.DataCloud.CloudItem;
import com.android.camera.data.cloud.DataCloud.CloudManager;
import com.android.camera.log.Log;
import com.android.camera.network.util.NetworkUtils;
import com.android.camera.sensitive.SensitiveFilter;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DataCloudMgr implements CloudManager {
    private static final String CLOUD_DATA_MODULE_NAME = "camera_settings_v3";
    private static final long CLOUD_DATA_UPDATE_INTERVAL = 43200000;
    private static final String FEA_SENSI_WORD = "pref_senstive_words";
    private static final String KEY_CLOUD_DATA_DEVICE_VERSION = "cloud_data_device_version";
    private static final String KEY_CLOUD_DATA_LAST_UPDATE = "cloud_data_last_update";
    private static final String KEY_CLOUD_DATA_VERSION = "cloud_data_version";
    private static final String TAG = "DataCloudMgr";
    private static final String VALUE = "value";
    private static final String VERSION = "version";
    private DataCloudFeatureController mDataCloudFeatureController;
    private DataCloudItemGlobal mDataCloudGlobal = new DataCloudItemGlobal();
    private SparseArray mDataCloudItemConfigs = new SparseArray(2);

    private static void checkLastUpdateTimeFormat() {
        String str = KEY_CLOUD_DATA_LAST_UPDATE;
        try {
            getCloudDataLastUpdateTime();
        } catch (Exception unused) {
            String str2 = TAG;
            Log.w(str2, "get cloud_data_last_update long value failed, try String type");
            try {
                String string = DataRepository.dataItemGlobal().getString(str, "0");
                Log.i(str2, "try to convert the string value type to long for field [cloud_data_last_update]");
                DataRepository.dataItemGlobal().editor().remove(str).putLong(str, TextUtils.isEmpty(string) ? 0 : Long.parseLong(string)).commit();
            } catch (Exception unused2) {
                Log.w(str2, "get cloud_data_last_update string value failed");
            }
        }
    }

    /* access modifiers changed from: private */
    public void fillPreference() {
        CloudItem provideDataCloudConfig = provideDataCloudConfig(1);
        CloudItem provideDataCloudConfig2 = provideDataCloudConfig(0);
        getCloudSensitiveWords();
        String str = "version";
        String str2 = CLOUD_DATA_MODULE_NAME;
        String cloudDataString = getCloudDataString(str2, str, null);
        String cloudDataCommonVersion = getCloudDataCommonVersion();
        StringBuilder sb = new StringBuilder();
        String str3 = "camera_settings_v3_";
        sb.append(str3);
        sb.append(Build.DEVICE);
        String cloudDataString2 = getCloudDataString(sb.toString(), str, null);
        String cloudDataDeviceVersion = getCloudDataDeviceVersion();
        this.mDataCloudGlobal.setReady(false);
        DataCloudFeature().setReady(false);
        provideDataCloudConfig.setReady(false);
        provideDataCloudConfig2.setReady(false);
        if ((cloudDataString != null && !cloudDataString.equals(cloudDataCommonVersion)) || (cloudDataString2 != null && !cloudDataString2.equals(cloudDataDeviceVersion))) {
            this.mDataCloudGlobal.editor().clear().apply();
            DataCloudFeature().editor().clear().apply();
            provideDataCloudConfig.editor().clear().apply();
            provideDataCloudConfig2.editor().clear().apply();
            StringBuilder sb2 = new StringBuilder();
            sb2.append("updateSettingsFromCloudData common version ");
            sb2.append(cloudDataCommonVersion);
            String str4 = " -> ";
            sb2.append(str4);
            sb2.append(cloudDataString);
            String sb3 = sb2.toString();
            String str5 = TAG;
            Log.d(str5, sb3);
            setCloudDataCommonVersion(cloudDataString);
            updateSettingsFromCloudData(str2);
            StringBuilder sb4 = new StringBuilder();
            sb4.append("updateSettingsFromCloudData device version ");
            sb4.append(cloudDataDeviceVersion);
            sb4.append(str4);
            sb4.append(cloudDataString2);
            Log.d(str5, sb4.toString());
            setCloudDataDeviceVersion(cloudDataString2);
            StringBuilder sb5 = new StringBuilder();
            sb5.append(str3);
            sb5.append(Build.DEVICE);
            updateSettingsFromCloudData(sb5.toString());
        }
        this.mDataCloudGlobal.setReady(true);
        DataCloudFeature().setReady(true);
        provideDataCloudConfig.setReady(true);
        provideDataCloudConfig2.setReady(true);
    }

    private static String getCloudDataCommonVersion() {
        return DataRepository.dataItemGlobal().getString(KEY_CLOUD_DATA_VERSION, null);
    }

    private static String getCloudDataDeviceVersion() {
        return DataRepository.dataItemGlobal().getString(KEY_CLOUD_DATA_DEVICE_VERSION, null);
    }

    private static final int getCloudDataInt(String str, String str2, int i) {
        return SettingsCloudData.getCloudDataInt(CameraAppImpl.getAndroidContext().getContentResolver(), str, str2, i);
    }

    private static long getCloudDataLastUpdateTime() {
        return DataRepository.dataItemGlobal().getLong(KEY_CLOUD_DATA_LAST_UPDATE, 0);
    }

    private static final String getCloudDataString(String str, String str2, String str3) {
        return SettingsCloudData.getCloudDataString(CameraAppImpl.getAndroidContext().getContentResolver(), str, str2, str3);
    }

    private void getCloudSensitiveWords() {
        String cloudDataString = getCloudDataString(CLOUD_DATA_MODULE_NAME, FEA_SENSI_WORD, "");
        if (!TextUtils.isEmpty(cloudDataString)) {
            try {
                JSONObject jSONObject = new JSONObject(cloudDataString);
                updateSensitiveWords(jSONObject.optInt("version"), jSONObject.optString("value"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    private static void setCloudDataCommonVersion(String str) {
        DataRepository.dataItemGlobal().editor().putString(KEY_CLOUD_DATA_VERSION, str).apply();
    }

    private static void setCloudDataDeviceVersion(String str) {
        DataRepository.dataItemGlobal().editor().putString(KEY_CLOUD_DATA_DEVICE_VERSION, str).apply();
    }

    private static void setCloudDataLastUpdateTime(long j) {
        DataRepository.dataItemGlobal().editor().putLong(KEY_CLOUD_DATA_LAST_UPDATE, j).apply();
    }

    private void updateSensitiveWords(int i, String str) {
        String keyCloudSenstiveWordsPath = CameraSettings.getKeyCloudSenstiveWordsPath(str);
        File file = new File(SensitiveFilter.CLOUD_FILE_PATH);
        if (i > CameraSettings.getLocalWordsVersion() || (!file.exists() && i != 0)) {
            NetworkUtils.bind((Application) CameraAppImpl.getAndroidContext());
            if (NetworkUtils.isNetworkConnected() && SensitiveFilter.loadSensitiveWords(keyCloudSenstiveWordsPath, SensitiveFilter.LOCAL_FILE_PATH)) {
                CameraSettings.setLocalWordsVersion(i);
            }
        }
    }

    private void updateSettingsFromCloudData(String str) {
        Object obj;
        String str2 = "type";
        String str3 = TAG;
        try {
            String cloudDataString = getCloudDataString(str, "content", null);
            boolean isEmpty = TextUtils.isEmpty(cloudDataString);
            String str4 = "value";
            String str5 = WatermarkConstant.ITEM_KEY;
            if (!isEmpty) {
                JSONArray jSONArray = new JSONArray(cloudDataString);
                for (int i = 0; i < jSONArray.length(); i++) {
                    DataCloudItemBase dataCloudItemBase = this.mDataCloudGlobal;
                    JSONObject jSONObject = jSONArray.getJSONObject(i);
                    if (jSONObject.has(str2)) {
                        String string = jSONObject.getString(str2);
                        if (string.equals("front")) {
                            obj = this.mDataCloudItemConfigs.get(1);
                        } else if (string.equals("back")) {
                            obj = this.mDataCloudItemConfigs.get(0);
                        }
                        dataCloudItemBase = (DataCloudItemBase) obj;
                    }
                    String string2 = jSONObject.getString(str5);
                    Editor editor = dataCloudItemBase.editor();
                    Object obj2 = jSONObject.get(str4);
                    if (obj2 instanceof Boolean) {
                        editor.putBoolean(string2, ((Boolean) obj2).booleanValue());
                    } else if (obj2 instanceof Integer) {
                        editor.putInt(string2, ((Integer) obj2).intValue());
                    } else if (obj2 instanceof Long) {
                        editor.putLong(string2, ((Long) obj2).longValue());
                    } else if (obj2 instanceof Float) {
                        editor.putFloat(string2, ((Float) obj2).floatValue());
                    } else if (obj2 instanceof String) {
                        editor.putString(string2, (String) obj2);
                    } else {
                        StringBuilder sb = new StringBuilder();
                        sb.append("Wrong camera setting type ");
                        sb.append(string2);
                        sb.append(": ");
                        sb.append(obj2);
                        Log.e(str3, sb.toString());
                    }
                    editor.apply();
                }
            }
            String cloudDataString2 = getCloudDataString(str, "feature", null);
            if (!TextUtils.isEmpty(cloudDataString2)) {
                Editor editor2 = DataCloudFeature().editor();
                JSONArray jSONArray2 = new JSONArray(cloudDataString2);
                for (int i2 = 0; i2 < jSONArray2.length(); i2++) {
                    JSONObject jSONObject2 = jSONArray2.getJSONObject(i2);
                    editor2.putBoolean(jSONObject2.getString(str5), jSONObject2.getBoolean(str4));
                }
                editor2.apply();
            }
        } catch (JSONException e) {
            Log.e(str3, "JSONException when get camera settings !", (Throwable) e);
        }
    }

    public CloudFeature DataCloudFeature() {
        if (this.mDataCloudFeatureController == null) {
            this.mDataCloudFeatureController = new DataCloudFeatureController();
        }
        return this.mDataCloudFeatureController;
    }

    public void fillCloudValues() {
        long currentTimeMillis = System.currentTimeMillis();
        checkLastUpdateTimeFormat();
        if (currentTimeMillis - getCloudDataLastUpdateTime() < CLOUD_DATA_UPDATE_INTERVAL) {
            this.mDataCloudGlobal.setReady(true);
            DataCloudFeature().setReady(true);
            provideDataCloudConfig(1).setReady(true);
            provideDataCloudConfig(0).setReady(true);
            return;
        }
        setCloudDataLastUpdateTime(currentTimeMillis);
        Completable.create(new CompletableOnSubscribe() {
            public void subscribe(@NonNull CompletableEmitter completableEmitter) {
                DataCloudMgr.this.fillPreference();
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe();
    }

    public CloudItem provideDataCloudConfig(int i) {
        DataCloudItemConfig dataCloudItemConfig = (DataCloudItemConfig) this.mDataCloudItemConfigs.get(i);
        if (dataCloudItemConfig != null) {
            return dataCloudItemConfig;
        }
        DataCloudItemConfig dataCloudItemConfig2 = new DataCloudItemConfig(i);
        this.mDataCloudItemConfigs.put(i, dataCloudItemConfig2);
        return dataCloudItemConfig2;
    }

    public CloudItem provideDataCloudGlobal() {
        if (this.mDataCloudGlobal == null) {
            this.mDataCloudGlobal = new DataCloudItemGlobal();
        }
        return this.mDataCloudGlobal;
    }
}
