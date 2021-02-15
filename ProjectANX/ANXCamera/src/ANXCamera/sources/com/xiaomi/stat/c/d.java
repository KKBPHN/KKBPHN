package com.xiaomi.stat.c;

import android.content.ComponentName;
import android.content.ServiceConnection;
import android.os.IBinder;
import com.xiaomi.stat.b.e;
import com.xiaomi.stat.d.k;
import java.util.Map;

final class d implements ServiceConnection {
    final /* synthetic */ String[] a;
    final /* synthetic */ String b;
    final /* synthetic */ Map c;

    d(String[] strArr, String str, Map map) {
        this.a = strArr;
        this.b = str;
        this.c = map;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x000b */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onBindingDied(ComponentName componentName) {
        synchronized (i.class) {
            i.class.notify();
        }
    }

    public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
        e.a().execute(new e(this, iBinder));
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0021 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onServiceDisconnected(ComponentName componentName) {
        StringBuilder sb = new StringBuilder();
        sb.append("onServiceDisconnected ");
        sb.append(componentName);
        k.b("UploadMode", sb.toString());
        synchronized (i.class) {
            i.class.notify();
        }
    }
}
