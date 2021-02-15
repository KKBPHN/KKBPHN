package com.iqiyi.android.qigsaw.core.extension.fakecomponents;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP})
public class FakeReceiver extends BroadcastReceiver {
    public void onReceive(Context context, Intent intent) {
    }
}
