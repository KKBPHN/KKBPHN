package com.android.camera.data.backup;

import android.util.SparseArray;
import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.data.provider.DataProvider.ProviderEvent;

public class DataActionBackUpImpl implements DataBackUp {
    private SparseArray mBackupArrays;

    public void backupRunning(DataItemRunning dataItemRunning, int i) {
        if (this.mBackupArrays == null) {
            this.mBackupArrays = new SparseArray();
        }
        if (this.mBackupArrays.get(i) == null) {
            this.mBackupArrays.put(i, dataItemRunning.cloneValues());
        }
    }

    public void clearBackUp() {
        SparseArray sparseArray = this.mBackupArrays;
        if (sparseArray != null) {
            sparseArray.clear();
        }
    }

    @Nullable
    public SimpleArrayMap getBackupRunning(int i) {
        SparseArray sparseArray = this.mBackupArrays;
        if (sparseArray == null) {
            return null;
        }
        return (SimpleArrayMap) sparseArray.get(i);
    }

    public void revertOrCreateRunning(DataItemRunning dataItemRunning, int i) {
        SimpleArrayMap simpleArrayMap;
        SparseArray sparseArray = this.mBackupArrays;
        if (sparseArray == null) {
            this.mBackupArrays = new SparseArray();
            simpleArrayMap = null;
        } else {
            simpleArrayMap = (SimpleArrayMap) sparseArray.get(i);
        }
        if (simpleArrayMap == null) {
            simpleArrayMap = new SimpleArrayMap();
            this.mBackupArrays.put(i, simpleArrayMap);
        }
        dataItemRunning.restoreArrayMap(simpleArrayMap);
        dataItemRunning.setBackupKey(i);
    }

    public void startBackup(ProviderEvent providerEvent, int i) {
    }
}
