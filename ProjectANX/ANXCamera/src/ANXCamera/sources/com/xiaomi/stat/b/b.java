package com.xiaomi.stat.b;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import com.xiaomi.stat.d.k;

class b extends BroadcastReceiver {
    final /* synthetic */ a a;

    b(a aVar) {
        this.a = aVar;
    }

    public void onReceive(Context context, Intent intent) {
        try {
            if (this.a.u != 1) {
                context.unregisterReceiver(this.a.x);
            } else {
                e.a().execute(new c(this));
            }
        } catch (Exception e) {
            k.d("ConfigManager", "mNetReceiver exception", e);
        }
    }
}
