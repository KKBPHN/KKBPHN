package com.android.camera.external.mivi;

import android.annotation.SuppressLint;
import android.database.ContentObserver;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.text.TextUtils;
import com.android.camera.CameraAppImpl;
import com.android.camera.data.cloud.DataCloudItemMIVI;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.FileUtils;
import com.android.camera.resource.conf.ConfMIVIRequest;
import com.android.camera.storage.Storage;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import io.reactivex.schedulers.Schedulers;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.concurrent.TimeUnit;

public class MIVIHelper {
    private static final String EMPTY_DATA = "{}";
    public static final String KEY_PLATFORM_INFO = "platformInfo";
    private static String LOCAL_MIVI_INFO_FILE_NAME = "mivi_info.json";
    private static final String TAG = "MIVIHelper";
    private static boolean mDataChanged;

    class SettingDataChangeObserver extends ContentObserver {
        public SettingDataChangeObserver(Handler handler) {
            super(handler);
        }

        public void onChange(boolean z) {
            super.onChange(z);
            Log.d(MIVIHelper.TAG, "SettingDataChangeObserver onChange: ");
            CameraAppImpl.getAndroidContext().getContentResolver().unregisterContentObserver(this);
            MIVIHelper.setDataChanged(true);
            MIVIHelper.requestCloudDataAsync();
        }
    }

    private MIVIHelper() {
    }

    static /* synthetic */ void O000000o(DataCloudItemMIVI dataCloudItemMIVI) {
        String str;
        String stringFromLocalFile = getStringFromLocalFile();
        boolean isEmpty = TextUtils.isEmpty(stringFromLocalFile);
        String str2 = TAG;
        if (!isEmpty) {
            Log.d(str2, "requestCloudDataAsync: config with local miviinfo");
            setMiViInfo(stringFromLocalFile);
            return;
        }
        if (!TextUtils.isEmpty(dataCloudItemMIVI.getData())) {
            str = dataCloudItemMIVI.getData();
        } else if (!mDataChanged) {
            listenSettingDataChange();
            return;
        } else {
            Log.d(str2, "requestCloudDataAsync: set null info");
            str = null;
        }
        setMiViInfo(str);
    }

    static /* synthetic */ void O0000Oo0(Throwable th) {
        StringBuilder sb = new StringBuilder();
        sb.append("requestCloudData: occurs ");
        sb.append(th.getLocalizedMessage());
        Log.w(TAG, sb.toString());
        setMiViInfo(EMPTY_DATA);
    }

    public static String getCloudDataFilterByPackageName(String str) {
        StringBuilder sb = new StringBuilder();
        sb.append("getCloudDataFilterByPackageName: start > ");
        sb.append(str);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        String stringFromLocalFile = getStringFromLocalFile();
        if (TextUtils.isEmpty(stringFromLocalFile)) {
            stringFromLocalFile = ConfMIVIRequest.getCloudDataString();
        } else {
            Log.d(str2, "getCloudDataFilterByPackageName: replace with local miviinfo ");
        }
        JsonObject jsonObject = (JsonObject) new Gson().fromJson(stringFromLocalFile, JsonObject.class);
        if (jsonObject == null) {
            return null;
        }
        JsonObject jsonObject2 = new JsonObject();
        String str3 = KEY_PLATFORM_INFO;
        jsonObject2.add(str3, jsonObject.getAsJsonObject(str3));
        JsonArray asJsonArray = jsonObject.getAsJsonArray("AppList");
        if (asJsonArray != null) {
            Iterator it = asJsonArray.iterator();
            while (true) {
                if (!it.hasNext()) {
                    break;
                }
                JsonObject asJsonObject = ((JsonElement) it.next()).getAsJsonObject();
                if (TextUtils.equals(str, asJsonObject.get("PackageName").getAsString())) {
                    jsonObject2.add("appInfo", asJsonObject);
                    break;
                }
            }
        }
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getCloudDataFilterByPackageName: ");
        sb3.append(jsonObject2.toString());
        Log.d(str2, sb3.toString());
        return jsonObject2.toString();
    }

    private static String getStringFromLocalFile() {
        File file = new File(Storage.generatePrimaryDirectoryPath(), LOCAL_MIVI_INFO_FILE_NAME);
        StringBuilder sb = new StringBuilder();
        sb.append("getStringFromLocalFile E >> ");
        sb.append(file.getAbsolutePath());
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (!file.exists()) {
            Log.d(str, "getStringFromLocalFile: X return null");
            return null;
        }
        String readStringFromFile = FileUtils.readStringFromFile(file);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("getStringFromLocalFile X >> ");
        sb3.append(readStringFromFile);
        Log.d(str, sb3.toString());
        return readStringFromFile;
    }

    private static void listenSettingDataChange() {
        Log.d(TAG, "listenSettingDataChange: ");
        CameraAppImpl.getAndroidContext().getContentResolver().registerContentObserver(Uri.parse("content://com.android.settings.cloud.CloudSettings/cloud_all_data/notify"), true, new SettingDataChangeObserver(new Handler(Looper.getMainLooper())));
    }

    @SuppressLint({"CheckResult"})
    public static void requestCloudDataAsync() {
        Log.d(TAG, "requestCloudDataAsync: ");
        new ConfMIVIRequest().startObservable(DataCloudItemMIVI.class).delaySubscription(1, TimeUnit.SECONDS).subscribeOn(Schedulers.io()).subscribe(O00000Oo.INSTANCE, O000000o.INSTANCE);
    }

    public static void setDataChanged(boolean z) {
        mDataChanged = z;
    }

    private static void setMiViInfo(String str) {
        String str2;
        StringBuilder sb;
        String str3 = TAG;
        if (TextUtils.isEmpty(str)) {
            str = EMPTY_DATA;
        }
        try {
            JsonObject jsonObject = (JsonObject) new Gson().fromJson(str, JsonObject.class);
            if (jsonObject != null) {
                jsonObject.remove(KEY_PLATFORM_INFO);
                str = jsonObject.toString();
            }
            StringBuilder sb2 = new StringBuilder();
            sb2.append("begin to setMiViInfo info: ");
            sb2.append(str);
            Log.d(str3, sb2.toString());
            Class cls = Class.forName("vendor.xiaomi.hardware.campostproc.V1_0.IMiPostProcService");
            Object invoke = cls.getDeclaredMethod("getService", new Class[]{Boolean.TYPE}).invoke(cls, new Object[]{Boolean.valueOf(true)});
            cls.getDeclaredMethod("setCapabilities", new Class[]{String.class}).invoke(invoke, new Object[]{str});
            Log.d(str3, "iMiPostProcService: setMiViInfo success ");
            return;
        } catch (ClassNotFoundException e) {
            sb = new StringBuilder();
            sb.append("setMiViInfo: but can not find IMiPostProcService： ");
            str2 = e.getLocalizedMessage();
        } catch (NoSuchMethodException e2) {
            sb = new StringBuilder();
            sb.append("setMiViInfo: but can not find method : ");
            str2 = e2.getLocalizedMessage();
        } catch (IllegalAccessException e3) {
            sb = new StringBuilder();
            sb.append("setMiViInfo: but can not access method : ");
            str2 = e3.getLocalizedMessage();
        } catch (InvocationTargetException e4) {
            sb = new StringBuilder();
            sb.append("setMiViInfo: but can not invoke method : ");
            str2 = e4.getLocalizedMessage();
        }
        sb.append(str2);
        Log.w(str3, sb.toString());
    }
}
