package com.android.camera.settings;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.ContentProvider;
import android.content.ContentValues;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.android.camera.CameraSettings;
import com.android.camera.ProximitySensorLock;
import com.android.camera.R;
import com.android.camera.log.Log;
import com.android.camera.settings.SearchContract.SearchResultColumn;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class CameraSettingsSearchProvider extends ContentProvider {
    private static final String TAG = "CameraSettingsSearchProvider";

    class RawData {
        String intentAction;
        String intentTargetClass;
        String intentTargetPackage;
        String title;

        public RawData(String str, String str2, String str3, String str4) {
            this.title = str;
            this.intentAction = str2;
            this.intentTargetPackage = str3;
            this.intentTargetClass = str4;
        }
    }

    public int delete(@NonNull Uri uri, @Nullable String str, @Nullable String[] strArr) {
        return 0;
    }

    @Nullable
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues contentValues) {
        return null;
    }

    public boolean onCreate() {
        return false;
    }

    public List prepareData() {
        Log.d(TAG, "prepare data.");
        ArrayList arrayList = new ArrayList(25);
        if (C0124O00000oO.Oo00OoO()) {
            arrayList.add(Integer.valueOf(R.string.pref_camera_recordlocation_title));
        }
        if (C0124O00000oO.Oo00o0O()) {
            arrayList.add(Integer.valueOf(R.string.pref_camera_sound_title));
        }
        if (ProximitySensorLock.supported()) {
            arrayList.add(Integer.valueOf(R.string.pref_camera_proximity_lock_title));
        }
        arrayList.add(Integer.valueOf(R.string.pref_retain_camera_mode_title));
        if (C0124O00000oO.Oo0O000()) {
            arrayList.add(Integer.valueOf(R.string.pref_camera_watermark_title));
        }
        if (CameraSettings.isSupportedDualCameraWaterMark()) {
            arrayList.add(Integer.valueOf(R.string.pref_camera_device_watermark_title));
        }
        arrayList.add(Integer.valueOf(R.string.pref_camera_referenceline_title));
        arrayList.add(Integer.valueOf(R.string.pref_camera_focus_shoot_title));
        if (!C0124O00000oO.OOo0oOo()) {
            arrayList.add(Integer.valueOf(R.string.pref_scan_qrcode_title));
        }
        if (C0124O00000oO.Oo00Ooo() || C0122O00000o.instance().OOO0oOo()) {
            arrayList.add(Integer.valueOf(R.string.pref_camera_long_press_shutter_feature_title));
        }
        arrayList.add(Integer.valueOf(R.string.pref_camera_jpegquality_title));
        arrayList.add(Integer.valueOf(R.string.pref_video_encoder_title));
        arrayList.add(Integer.valueOf(R.string.pref_video_time_lapse_frame_interval_title));
        if (C0124O00000oO.OOoo0o0()) {
            arrayList.add(Integer.valueOf(R.string.pref_fingerprint_capture_title));
        }
        arrayList.add(Integer.valueOf(R.string.pref_camera_volumekey_function_title));
        arrayList.add(Integer.valueOf(R.string.pref_camera_antibanding_title));
        arrayList.add(Integer.valueOf(R.string.confirm_restore_title));
        ArrayList arrayList2 = new ArrayList(arrayList.size());
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            String str = "miui.intent.action.CAMERA_SETTINGS";
            RawData rawData = new RawData(getContext().getString(((Integer) it.next()).intValue()), str, getContext().getPackageName(), "com.android.camera.CameraPreferenceActivity");
            arrayList2.add(rawData);
        }
        return arrayList2;
    }

    public Cursor query(@NonNull Uri uri, @Nullable String[] strArr, @Nullable String str, @Nullable String[] strArr2, @Nullable String str2) {
        MatrixCursor matrixCursor = new MatrixCursor(SearchContract.SEARCH_RESULT_COLUMNS);
        for (RawData rawData : prepareData()) {
            matrixCursor.newRow().add("title", rawData.title).add(SearchResultColumn.COLUMN_INTENT_ACTION, rawData.intentAction).add(SearchResultColumn.COLUMN_INTENT_TARGET_PACKAGE, rawData.intentTargetPackage).add(SearchResultColumn.COLUMN_INTENT_TARGET_CLASS, rawData.intentTargetClass);
        }
        return matrixCursor;
    }

    public int update(@NonNull Uri uri, @Nullable ContentValues contentValues, @Nullable String str, @Nullable String[] strArr) {
        return 0;
    }
}
