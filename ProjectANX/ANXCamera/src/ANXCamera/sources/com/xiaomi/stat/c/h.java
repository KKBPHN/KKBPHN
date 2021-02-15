package com.xiaomi.stat.c;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

class h extends BroadcastReceiver {
    final /* synthetic */ g a;

    h(g gVar) {
        this.a = gVar;
    }

    public void onReceive(Context context, Intent intent) {
        this.a.sendEmptyMessage(3);
    }
}
