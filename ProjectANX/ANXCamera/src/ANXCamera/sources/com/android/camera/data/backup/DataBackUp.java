package com.android.camera.data.backup;

import androidx.annotation.Nullable;
import androidx.collection.SimpleArrayMap;
import com.android.camera.data.data.runing.DataItemRunning;
import com.android.camera.data.provider.DataProvider.ProviderEvent;

public interface DataBackUp {
    void backupRunning(DataItemRunning dataItemRunning, int i);

    void clearBackUp();

    @Nullable
    SimpleArrayMap getBackupRunning(int i);

    void revertOrCreateRunning(DataItemRunning dataItemRunning, int i);

    void startBackup(ProviderEvent providerEvent, int i);
}
