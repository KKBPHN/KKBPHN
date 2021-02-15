package com.xiaomi.stat.c;

import android.content.Context;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.text.TextUtils;
import com.xiaomi.stat.a.k;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.b;
import com.xiaomi.stat.b.g;
import com.xiaomi.stat.d;
import com.xiaomi.stat.d.c;
import com.xiaomi.stat.d.e;
import com.xiaomi.stat.d.j;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;
import com.xiaomi.stat.d.r;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.zip.GZIPOutputStream;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class i {
    private static final String a = "3.0";
    private static final String b = "UploaderEngine";
    private static final String c = "code";
    private static final String d = "UTF-8";
    private static final String e = "mistat";
    private static final String f = "uploader";
    private static final String g = "3.0.16";
    private static final String h = "Android";
    private static final int i = 200;
    private static final int j = 1;
    private static final int k = -1;
    private static final int l = 3;
    private static volatile i m;
    private final byte[] n = new byte[0];
    private FileLock o;
    private FileChannel p;
    private g q;
    private a r;

    class a extends Handler {
        public a(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            super.handleMessage(message);
            if (message.what == 1) {
                i.this.g();
            }
        }
    }

    private i() {
        e();
    }

    private int a(int i2) {
        int i3 = 1;
        if (i2 == 1) {
            return -1;
        }
        if (i2 == 3) {
            i3 = 0;
        }
        return i3;
    }

    public static i a() {
        if (m == null) {
            synchronized (i.class) {
                if (m == null) {
                    m = new i();
                }
            }
        }
        return m;
    }

    private String a(JSONArray jSONArray, String str) {
        JSONObject jSONObject = new JSONObject();
        try {
            jSONObject.put("id", str);
            a(str, jSONObject);
            jSONObject.put(d.I, e.d());
            jSONObject.put("rc", m.h());
            jSONObject.put(d.j, c.b());
            jSONObject.put(d.k, b.t());
            jSONObject.put(d.l, h);
            jSONObject.put(d.Z, m.a(ak.a()));
            jSONObject.put(d.m, this.q != null ? this.q.a() : 0);
            jSONObject.put(d.n, String.valueOf(r.b()));
            jSONObject.put(d.o, m.e());
            jSONObject.put(d.p, a.a(ak.b()));
            String[] o2 = b.o();
            if (o2 != null && o2.length > 0) {
                jSONObject.put(d.v, a(o2));
            }
            jSONObject.put(d.q, m.d());
            jSONObject.put("n", l.b(ak.a()));
            jSONObject.put(d.t, b.h());
            jSONObject.put(d.u, jSONArray);
        } catch (JSONException e2) {
            e2.printStackTrace();
        }
        return jSONObject.toString();
    }

    private JSONArray a(String[] strArr) {
        JSONArray jSONArray = new JSONArray();
        for (int i2 = 0; i2 < strArr.length; i2++) {
            JSONObject jSONObject = new JSONObject();
            try {
                jSONObject.put(strArr[i2], a.a(strArr[i2]));
                jSONArray.put(jSONObject);
            } catch (JSONException e2) {
                e2.printStackTrace();
            }
        }
        return jSONArray;
    }

    private void a(Message message) {
        synchronized (this.n) {
            if (this.r == null || this.q == null) {
                e();
            }
            this.r.sendMessage(message);
        }
    }

    private void a(String str, JSONObject jSONObject) {
        try {
            if (!b.e() && TextUtils.isEmpty(str)) {
                Context a2 = ak.a();
                jSONObject.put(d.C, e.b(a2));
                jSONObject.put(d.J, e.k(a2));
                jSONObject.put(d.L, e.n(a2));
                jSONObject.put(d.O, e.q(a2));
                jSONObject.put("ai", e.p(a2));
            }
        } catch (Exception unused) {
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:22:0x0080  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0088  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00c1  */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x00c3 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void a(com.xiaomi.stat.a.b[] bVarArr, String str) {
        boolean z;
        k a2;
        int length = bVarArr.length;
        String str2 = b;
        if (length == 0) {
            com.xiaomi.stat.d.k.e(str2, "privacy policy or network state not matched");
            return;
        }
        k a3 = com.xiaomi.stat.a.c.a().a(bVarArr);
        AtomicInteger atomicInteger = new AtomicInteger();
        boolean z2 = a3 != null ? a3.c : true;
        StringBuilder sb = new StringBuilder();
        sb.append(str2);
        sb.append(a3);
        com.xiaomi.stat.d.k.b(sb.toString());
        boolean z3 = z2;
        boolean z4 = false;
        while (true) {
            if (a3 == null) {
                z = z4;
                break;
            }
            ArrayList arrayList = a3.b;
            try {
                String a4 = a(a3.a, str);
                com.xiaomi.stat.d.k.a(str2, " payload:", a4);
                String b2 = b(a(a(a4)));
                com.xiaomi.stat.d.k.a(str2, " encodePayload ", b2);
                String c2 = g.a().c();
                if (com.xiaomi.stat.d.k.b()) {
                    c2 = com.xiaomi.stat.d.k.c;
                }
                String a5 = c.a(c2, (Map) c(b2), true);
                com.xiaomi.stat.d.k.a(str2, " sendDataToServer response: ", a5);
                if (TextUtils.isEmpty(a5)) {
                    z = false;
                    if (!z) {
                        com.xiaomi.stat.a.c.a().a(arrayList);
                    } else {
                        atomicInteger.addAndGet(1);
                    }
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(" deleteData= ");
                    sb2.append(z);
                    sb2.append(" retryCount.get()= ");
                    sb2.append(atomicInteger.get());
                    com.xiaomi.stat.d.k.b(str2, sb2.toString());
                    if (z3 || (!z && atomicInteger.get() > 3)) {
                        break;
                    }
                    a2 = com.xiaomi.stat.a.c.a().a(bVarArr);
                    if (a2 == null) {
                        z3 = a2.c;
                    }
                    k kVar = a2;
                    z4 = z;
                    a3 = kVar;
                } else {
                    z = b(a5);
                    if (!z) {
                    }
                    StringBuilder sb22 = new StringBuilder();
                    sb22.append(" deleteData= ");
                    sb22.append(z);
                    sb22.append(" retryCount.get()= ");
                    sb22.append(atomicInteger.get());
                    com.xiaomi.stat.d.k.b(str2, sb22.toString());
                    a2 = com.xiaomi.stat.a.c.a().a(bVarArr);
                    if (a2 == null) {
                    }
                    k kVar2 = a2;
                    z4 = z;
                    a3 = kVar2;
                }
            } catch (Exception unused) {
            }
        }
        g gVar = this.q;
        if (gVar != null) {
            gVar.b(z);
        }
    }

    public static byte[] a(String str) {
        GZIPOutputStream gZIPOutputStream;
        ByteArrayOutputStream byteArrayOutputStream;
        GZIPOutputStream gZIPOutputStream2;
        String str2 = "UTF-8";
        byte[] bArr = null;
        try {
            byteArrayOutputStream = new ByteArrayOutputStream(str.getBytes(str2).length);
            try {
                gZIPOutputStream = new GZIPOutputStream(byteArrayOutputStream);
                try {
                    gZIPOutputStream.write(str.getBytes(str2));
                    gZIPOutputStream.finish();
                    bArr = byteArrayOutputStream.toByteArray();
                } catch (Exception e2) {
                    e = e2;
                }
            } catch (Exception e3) {
                e = e3;
                gZIPOutputStream2 = null;
                String str3 = b;
                try {
                    StringBuilder sb = new StringBuilder();
                    sb.append(" zipData failed! ");
                    sb.append(e.toString());
                    com.xiaomi.stat.d.k.e(str3, sb.toString());
                    j.a((OutputStream) byteArrayOutputStream);
                    j.a((OutputStream) gZIPOutputStream);
                    return bArr;
                } catch (Throwable th) {
                    th = th;
                    j.a((OutputStream) byteArrayOutputStream);
                    j.a((OutputStream) gZIPOutputStream);
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                gZIPOutputStream = null;
                j.a((OutputStream) byteArrayOutputStream);
                j.a((OutputStream) gZIPOutputStream);
                throw th;
            }
        } catch (Exception e4) {
            e = e4;
            ByteArrayOutputStream byteArrayOutputStream2 = null;
            gZIPOutputStream2 = null;
            String str32 = b;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(" zipData failed! ");
            sb2.append(e.toString());
            com.xiaomi.stat.d.k.e(str32, sb2.toString());
            j.a((OutputStream) byteArrayOutputStream);
            j.a((OutputStream) gZIPOutputStream);
            return bArr;
        } catch (Throwable th3) {
            th = th3;
            byteArrayOutputStream = null;
            gZIPOutputStream = null;
            j.a((OutputStream) byteArrayOutputStream);
            j.a((OutputStream) gZIPOutputStream);
            throw th;
        }
        j.a((OutputStream) byteArrayOutputStream);
        j.a((OutputStream) gZIPOutputStream);
        return bArr;
    }

    private byte[] a(byte[] bArr) {
        return com.xiaomi.stat.b.i.a().a(bArr);
    }

    private String b(byte[] bArr) {
        return com.xiaomi.stat.d.d.a(bArr);
    }

    private void b(boolean z) {
        a(c(z), com.xiaomi.stat.b.d.a().a(z));
    }

    private boolean b(String str) {
        try {
            int optInt = new JSONObject(str).optInt(c);
            if (optInt != 200) {
                if (!(optInt == 1002 || optInt == 1004 || optInt == 1005 || optInt == 1006 || optInt == 1007)) {
                    if (optInt != 1011) {
                        if (optInt == 2002 || optInt == 1012) {
                            com.xiaomi.stat.b.i.a().a(true);
                            com.xiaomi.stat.b.d.a().b();
                        }
                    }
                }
                com.xiaomi.stat.b.i.a().a(true);
                com.xiaomi.stat.b.d.a().b();
                return false;
            }
            return true;
        } catch (Exception e2) {
            com.xiaomi.stat.d.k.d(b, "parseUploadingResult exception ", e2);
            return false;
        }
    }

    private HashMap c(String str) {
        HashMap hashMap = new HashMap();
        hashMap.put("ai", ak.b());
        hashMap.put(d.b, "3.0.16");
        hashMap.put(d.c, a);
        hashMap.put(d.d, m.g());
        hashMap.put("p", str);
        hashMap.put(d.ak, com.xiaomi.stat.b.i.a().c());
        hashMap.put(d.g, com.xiaomi.stat.b.i.a().b());
        return hashMap;
    }

    private com.xiaomi.stat.a.b[] c(boolean z) {
        ArrayList h2 = h();
        int size = h2.size();
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < size; i2++) {
            String str = (String) h2.get(i2);
            int a2 = a(new f(str, z).a());
            if (a2 != -1) {
                arrayList.add(new com.xiaomi.stat.a.b(str, a2, z));
            }
        }
        com.xiaomi.stat.a.b d2 = d(z);
        if (d2 != null) {
            arrayList.add(d2);
        }
        return (com.xiaomi.stat.a.b[]) arrayList.toArray(new com.xiaomi.stat.a.b[arrayList.size()]);
    }

    private com.xiaomi.stat.a.b d(boolean z) {
        int a2 = new f(z).a();
        StringBuilder sb = new StringBuilder();
        sb.append(" createMainAppFilter: ");
        sb.append(a2);
        com.xiaomi.stat.d.k.b(b, sb.toString());
        int a3 = a(a2);
        if (a3 != -1) {
            return new com.xiaomi.stat.a.b(null, a3, z);
        }
        return null;
    }

    private void e() {
        HandlerThread handlerThread = new HandlerThread("mi_analytics_uploader_worker");
        handlerThread.start();
        this.r = new a(handlerThread.getLooper());
        this.q = new g(handlerThread.getLooper());
    }

    private void f() {
        g gVar = this.q;
        if (gVar != null) {
            gVar.c();
        }
    }

    /* access modifiers changed from: private */
    public void g() {
        if (i()) {
            if (b.e()) {
                b(true);
                b(false);
            } else {
                a(c(false), com.xiaomi.stat.b.d.a().c());
            }
            j();
        }
    }

    private ArrayList h() {
        String[] o2 = b.o();
        int length = o2 != null ? o2.length : 0;
        ArrayList arrayList = new ArrayList();
        for (int i2 = 0; i2 < length; i2++) {
            if (!TextUtils.isEmpty(o2[i2])) {
                arrayList.add(o2[i2]);
            }
        }
        return arrayList;
    }

    private boolean i() {
        String str = " acquire lock for uploader failed with ";
        String str2 = b;
        File file = new File(ak.a().getFilesDir(), e);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            this.p = new FileOutputStream(new File(file, f)).getChannel();
            try {
                this.o = this.p.tryLock();
                if (this.o != null) {
                    com.xiaomi.stat.d.k.c(str2, " acquire lock for uploader");
                    if (this.o == null) {
                        try {
                            this.p.close();
                            this.p = null;
                        } catch (Exception unused) {
                        }
                    }
                    return true;
                }
                com.xiaomi.stat.d.k.c(str2, " acquire lock for uploader failed");
                if (this.o == null) {
                    try {
                        this.p.close();
                        this.p = null;
                    } catch (Exception unused2) {
                    }
                }
                return false;
            } catch (Exception e2) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(e2);
                com.xiaomi.stat.d.k.c(str2, sb.toString());
                if (this.o == null) {
                    try {
                        this.p.close();
                        this.p = null;
                    } catch (Exception unused3) {
                    }
                }
                return false;
            } catch (Throwable th) {
                if (this.o == null) {
                    try {
                        this.p.close();
                        this.p = null;
                    } catch (Exception unused4) {
                    }
                }
                throw th;
            }
        } catch (FileNotFoundException e3) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(e3);
            com.xiaomi.stat.d.k.c(str2, sb2.toString());
            return false;
        }
    }

    private void j() {
        String str = b;
        try {
            if (this.o != null) {
                this.o.release();
                this.o = null;
            }
            if (this.p != null) {
                this.p.close();
                this.p = null;
            }
            com.xiaomi.stat.d.k.c(str, " releaseLock lock for uploader");
        } catch (IOException e2) {
            StringBuilder sb = new StringBuilder();
            sb.append(" releaseLock lock for uploader failed with ");
            sb.append(e2);
            com.xiaomi.stat.d.k.c(str, sb.toString());
        }
    }

    public void a(boolean z) {
        g gVar = this.q;
        if (gVar != null) {
            gVar.a(z);
        }
    }

    public void b() {
        this.q.b();
        c();
    }

    public void c() {
        if (!l.a()) {
            f();
            return;
        }
        boolean a2 = b.a();
        String str = b;
        if (!a2 || !b.b()) {
            com.xiaomi.stat.d.k.b(str, " postToServer statistic disable or network disable access! ");
        } else if (!b.B()) {
            com.xiaomi.stat.d.k.b(str, " postToServer can not upload data because of configuration!");
        } else {
            Message obtain = Message.obtain();
            obtain.what = 1;
            a(obtain);
        }
    }

    public synchronized void d() {
        if (this.q != null) {
            this.q.d();
        }
    }
}
