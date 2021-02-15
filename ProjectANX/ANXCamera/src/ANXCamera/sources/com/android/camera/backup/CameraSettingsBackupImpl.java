package com.android.camera.backup;

import android.content.Context;
import android.content.SharedPreferences;
import com.android.camera.data.data.config.DataItemConfig;
import com.android.camera.log.Log;
import com.android.camera.module.BaseModule;
import java.util.ArrayList;
import java.util.List;
import miui.cloud.backup.ICloudBackup;
import miui.cloud.backup.data.DataPackage;
import miui.cloud.backup.data.PrefsBackupHelper;
import miui.cloud.backup.data.PrefsBackupHelper.PrefEntry;

public class CameraSettingsBackupImpl implements ICloudBackup {
    private static final int FRONT_CLOUD_CAMERA_ID = 1;
    private static final PrefEntry[] PREF_ENTRIES = CameraBackupSettings.PREF_ENTRIES;
    private static final int REAR_CLOUD_CAMERA_ID = 0;
    private static final String TAG = "CameraSettingsBackup";

    interface BackupRestoreHandler {
        void handle(SharedPreferences sharedPreferences, DataPackage dataPackage, PrefEntry[] prefEntryArr);
    }

    private static PrefEntry[] addPrefixToEntries(PrefEntry[] prefEntryArr, String str) {
        PrefEntry[] prefEntryArr2 = new PrefEntry[prefEntryArr.length];
        for (int i = 0; i < prefEntryArr.length; i++) {
            PrefEntry prefEntry = prefEntryArr[i];
            Class valueClass = prefEntry.getValueClass();
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append("::");
            sb.append(prefEntry.getCloudKey());
            String sb2 = sb.toString();
            String localKey = prefEntry.getLocalKey();
            Object defaultValue = prefEntry.getDefaultValue();
            PrefEntry prefEntry2 = null;
            if (valueClass.equals(Integer.class)) {
                prefEntry2 = defaultValue == null ? PrefEntry.createIntEntry(sb2, localKey) : PrefEntry.createIntEntry(sb2, localKey, ((Integer) defaultValue).intValue());
            } else if (valueClass.equals(Boolean.class)) {
                prefEntry2 = defaultValue == null ? PrefEntry.createBoolEntry(sb2, localKey) : PrefEntry.createBoolEntry(sb2, localKey, ((Boolean) defaultValue).booleanValue());
            } else if (valueClass.equals(String.class)) {
                prefEntry2 = defaultValue == null ? PrefEntry.createStringEntry(sb2, localKey) : PrefEntry.createStringEntry(sb2, localKey, (String) defaultValue);
            } else if (valueClass.equals(Long.class)) {
                prefEntry2 = defaultValue == null ? PrefEntry.createLongEntry(sb2, localKey) : PrefEntry.createLongEntry(sb2, localKey, ((Long) defaultValue).longValue());
            }
            prefEntryArr2[i] = prefEntry2;
        }
        return prefEntryArr2;
    }

    private static boolean checkCameraId(int i) {
        if (i < 0) {
            return false;
        }
        if (i < 2) {
            return true;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("cameraId is invalid: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    private static List getAvailableCameraIds() {
        ArrayList arrayList = new ArrayList();
        if (checkCameraId(0)) {
            arrayList.add(Integer.valueOf(0));
        }
        if (checkCameraId(1)) {
            arrayList.add(Integer.valueOf(1));
        }
        return arrayList;
    }

    private static String getCloudPrefix(int i, int i2) {
        if (checkCameraId(i)) {
            if (i == 0) {
                i = 0;
            } else if (i == 1) {
                i = 1;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("camera_settings_simple_mode_local_");
        sb.append(DataItemConfig.provideLocalId(i, i2));
        return sb.toString();
    }

    private static String getCloudPrefixByCameraIdAndMode(int i, int i2) {
        if (checkCameraId(i)) {
            if (i == 0) {
                i = 0;
            } else if (i == 1) {
                i = 1;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("camera_settings_simple_mode_local_");
        sb.append(BaseModule.getPreferencesLocalId(i, i2));
        return sb.toString();
    }

    private static String getSharedPreferencesName(int i, int i2) {
        StringBuilder sb = new StringBuilder();
        sb.append("camera_settings_simple_mode_local_");
        sb.append(DataItemConfig.provideLocalId(i, i2));
        return sb.toString();
    }

    private void handleBackupOrRestore(Context context, DataPackage dataPackage, BackupRestoreHandler backupRestoreHandler) {
        int[] iArr = {0, 1};
        List<Integer> availableCameraIds = getAvailableCameraIds();
        for (int i : iArr) {
            for (Integer intValue : availableCameraIds) {
                int intValue2 = intValue.intValue();
                SharedPreferences sharedPreferences = context.getSharedPreferences(getSharedPreferencesName(intValue2, i), 0);
                if (sharedPreferences != null) {
                    backupRestoreHandler.handle(sharedPreferences, dataPackage, addPrefixToEntries(PREF_ENTRIES, getCloudPrefix(intValue2, i)));
                }
            }
        }
        String str = "camera_settings_global";
        backupRestoreHandler.handle(context.getSharedPreferences(str, 0), dataPackage, addPrefixToEntries(PREF_ENTRIES, str));
    }

    private void restoreFromVersion1(Context context, DataPackage dataPackage) {
        Context context2 = context;
        DataPackage dataPackage2 = dataPackage;
        String str = "camera_settings_global";
        SharedPreferences sharedPreferences = context2.getSharedPreferences(str, 0);
        int i = 2;
        int[] iArr = {0, 1};
        List<Integer> availableCameraIds = getAvailableCameraIds();
        int length = iArr.length;
        int i2 = 0;
        while (i2 < length) {
            int i3 = iArr[i2];
            for (Integer intValue : availableCameraIds) {
                int intValue2 = intValue.intValue();
                SharedPreferences sharedPreferences2 = context2.getSharedPreferences(getSharedPreferencesName(intValue2, i3), 0);
                if (sharedPreferences2 != null) {
                    PrefEntry[] addPrefixToEntries = addPrefixToEntries(PREF_ENTRIES, getCloudPrefixByCameraIdAndMode(intValue2, i3 == 0 ? 0 : i));
                    CameraBackupHelper.restoreSettings(sharedPreferences2, dataPackage2, addPrefixToEntries, false);
                    if (i3 == 0 && intValue2 == 0) {
                        CameraBackupHelper.restoreSettings(sharedPreferences, dataPackage2, addPrefixToEntries, true);
                    }
                }
                i = 2;
            }
            i2++;
            i = 2;
        }
        CameraBackupHelper.restoreSettings(sharedPreferences, dataPackage2, addPrefixToEntries(PREF_ENTRIES, str), true);
    }

    private void restoreFromVersion3(Context context, DataPackage dataPackage) {
        int[] iArr = {0, 1};
        List<Integer> availableCameraIds = getAvailableCameraIds();
        for (int i : iArr) {
            for (Integer intValue : availableCameraIds) {
                int intValue2 = intValue.intValue();
                SharedPreferences sharedPreferences = context.getSharedPreferences(getSharedPreferencesName(intValue2, i), 0);
                if (sharedPreferences != null) {
                    CameraBackupHelper.restoreSettings(sharedPreferences, dataPackage, addPrefixToEntries(PREF_ENTRIES, getCloudPrefix(intValue2, i)), false);
                }
            }
        }
        String str = "camera_settings_global";
        CameraBackupHelper.restoreSettings(context.getSharedPreferences(str, 0), dataPackage, addPrefixToEntries(PREF_ENTRIES, str), true);
    }

    public int getCurrentVersion(Context context) {
        return 4;
    }

    public void onBackupSettings(Context context, DataPackage dataPackage) {
        StringBuilder sb = new StringBuilder();
        sb.append("backup version ");
        sb.append(getCurrentVersion(context));
        Log.d(TAG, sb.toString());
        handleBackupOrRestore(context, dataPackage, new BackupRestoreHandler() {
            public void handle(SharedPreferences sharedPreferences, DataPackage dataPackage, PrefEntry[] prefEntryArr) {
                PrefsBackupHelper.backup(sharedPreferences, dataPackage, prefEntryArr);
            }
        });
    }

    public void onRestoreSettings(Context context, DataPackage dataPackage, int i) {
        int currentVersion = getCurrentVersion(context);
        String str = TAG;
        if (i > currentVersion) {
            StringBuilder sb = new StringBuilder();
            sb.append("skip restore due to cloud version ");
            sb.append(i);
            sb.append(" is higher than local version ");
            sb.append(currentVersion);
            Log.w(str, sb.toString());
            return;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("restore version ");
        sb2.append(i);
        Log.d(str, sb2.toString());
        if (4 <= i) {
            handleBackupOrRestore(context, dataPackage, new BackupRestoreHandler() {
                public void handle(SharedPreferences sharedPreferences, DataPackage dataPackage, PrefEntry[] prefEntryArr) {
                    PrefsBackupHelper.restore(sharedPreferences, dataPackage, prefEntryArr);
                }
            });
        } else if (3 == i) {
            restoreFromVersion3(context, dataPackage);
        } else if (1 == i) {
            restoreFromVersion1(context, dataPackage);
        }
    }
}
