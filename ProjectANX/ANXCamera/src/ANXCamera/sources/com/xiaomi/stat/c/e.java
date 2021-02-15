package com.xiaomi.stat.c;

import android.os.IBinder;
import android.os.RemoteException;
import com.xiaomi.a.a.a.a;
import com.xiaomi.a.a.a.a.C0000a;
import com.xiaomi.stat.b;
import com.xiaomi.stat.d.k;

class e implements Runnable {
    final /* synthetic */ IBinder a;
    final /* synthetic */ d b;

    e(d dVar, IBinder iBinder) {
        this.b = dVar;
        this.a = iBinder;
    }

    public void run() {
        a a2 = C0000a.a(this.a);
        try {
            if (!b.e()) {
                this.b.a[0] = a2.a(this.b.b, this.b.c);
            } else if (b.x()) {
                this.b.a[0] = a2.b(this.b.b, this.b.c);
            } else {
                this.b.a[0] = null;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(" connected, do remote http post ");
            sb.append(this.b.a[0]);
            k.b("UploadMode", sb.toString());
            synchronized (i.class) {
                try {
                    i.class.notify();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (RemoteException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" error while uploading the data by IPC.");
            sb2.append(e2.toString());
            k.e("UploadMode", sb2.toString());
            this.b.a[0] = null;
        }
    }
}
