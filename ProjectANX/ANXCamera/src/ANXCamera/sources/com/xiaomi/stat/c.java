package com.xiaomi.stat;

import android.os.Handler;
import android.os.HandlerThread;
import com.xiaomi.stat.d.k;
import java.io.File;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.nio.channels.FileLock;

public class c {
    private static final String a = "DBExecutor";
    private static String b = "mistat_db";
    private static final String c = "mistat";
    private static final String d = "db.lk";
    private static Handler e;
    private static FileLock f;
    private static FileChannel g;

    class a implements Runnable {
        private Runnable a;

        public a(Runnable runnable) {
            this.a = runnable;
        }

        public void run() {
            if (c.d()) {
                Runnable runnable = this.a;
                if (runnable != null) {
                    runnable.run();
                }
                c.e();
            }
        }
    }

    public static void a(Runnable runnable) {
        c();
        e.post(new a(runnable));
    }

    private static void c() {
        if (e == null) {
            synchronized (c.class) {
                if (e == null) {
                    HandlerThread handlerThread = new HandlerThread(b);
                    handlerThread.start();
                    e = new Handler(handlerThread.getLooper());
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public static boolean d() {
        String str = "acquire lock for db failed with ";
        String str2 = a;
        File file = new File(ak.a().getFilesDir(), c);
        if (!file.exists()) {
            file.mkdir();
        }
        try {
            try {
                g = new FileOutputStream(new File(file, d)).getChannel();
                f = g.lock();
                k.c(str2, "acquire lock for db");
                return true;
            } catch (Exception e2) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append(e2);
                k.c(str2, sb.toString());
                try {
                    g.close();
                    g = null;
                } catch (Exception e3) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("close file stream failed with ");
                    sb2.append(e3);
                    k.c(str2, sb2.toString());
                }
                return false;
            }
        } catch (Exception e4) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append(str);
            sb3.append(e4);
            k.c(str2, sb3.toString());
            return false;
        }
    }

    /* access modifiers changed from: private */
    public static void e() {
        String str = a;
        try {
            if (f != null) {
                f.release();
                f = null;
            }
            k.c(str, "release sDBFileLock for db");
        } catch (Exception e2) {
            StringBuilder sb = new StringBuilder();
            sb.append("release sDBFileLock for db failed with ");
            sb.append(e2);
            k.c(str, sb.toString());
        }
        try {
            if (g != null) {
                g.close();
                g = null;
            }
            k.c(str, "release sLockFileChannel for db");
        } catch (Exception e3) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("release sLockFileChannel for db failed with ");
            sb2.append(e3);
            k.c(str, sb2.toString());
        }
    }
}
