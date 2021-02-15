package com.iqiyi.android.qigsaw.core.extension.fakecomponents;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class FakeService extends Service {
    @Nullable
    public IBinder onBind(Intent intent) {
        return null;
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        stopSelf();
        return super.onStartCommand(intent, i, i2);
    }
}
