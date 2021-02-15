package com.xiaomi.stat;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.FileObserver;
import android.text.TextUtils;
import com.xiaomi.stat.d.k;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

public class ab {
    private static final String a = "MiStatPref";
    private static final String b = "true";
    private static final String c = "false";
    private static ab e;
    private FileObserver d;
    private Map f = new HashMap();
    /* access modifiers changed from: private */
    public SQLiteOpenHelper g;

    class a extends SQLiteOpenHelper {
        public static final String a = "mistat_pf";
        public static final String b = "pref";
        public static final String c = "pref_key";
        public static final String d = "pref_value";
        private static final int e = 1;
        private static final String f = "_id";
        private static final String g = "CREATE TABLE pref (_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL,pref_key TEXT,pref_value TEXT)";

        public a(Context context) {
            super(context, a, null, 1);
        }

        public void onCreate(SQLiteDatabase sQLiteDatabase) {
            sQLiteDatabase.execSQL(g);
        }

        public void onUpgrade(SQLiteDatabase sQLiteDatabase, int i, int i2) {
        }
    }

    private ab() {
        Context a2 = ak.a();
        this.g = new a(a2);
        b();
        c(a2.getDatabasePath(a.a).getAbsolutePath());
    }

    public static ab a() {
        if (e == null) {
            synchronized (ab.class) {
                if (e == null) {
                    e = new ab();
                }
            }
        }
        return e;
    }

    /* access modifiers changed from: private */
    public void b() {
        String str = a;
        FutureTask futureTask = new FutureTask(new ac(this));
        try {
            c.a(futureTask);
            Cursor cursor = null;
            try {
                cursor = (Cursor) futureTask.get(2, TimeUnit.SECONDS);
            } catch (InterruptedException | ExecutionException | TimeoutException unused) {
            }
            if (cursor != null) {
                this.f.clear();
                try {
                    k.c(str, "load pref from db");
                    int columnIndex = cursor.getColumnIndex(a.c);
                    int columnIndex2 = cursor.getColumnIndex(a.d);
                    while (cursor.moveToNext()) {
                        String string = cursor.getString(columnIndex);
                        String string2 = cursor.getString(columnIndex2);
                        this.f.put(string, string2);
                        StringBuilder sb = new StringBuilder();
                        sb.append("key=");
                        sb.append(string);
                        sb.append(" ,value=");
                        sb.append(string2);
                        k.c(str, sb.toString());
                    }
                } catch (Exception unused2) {
                } catch (Throwable th) {
                    cursor.close();
                    throw th;
                }
                cursor.close();
            }
        } catch (RejectedExecutionException e2) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("load data execute failed with ");
            sb2.append(e2);
            k.c(str, sb2.toString());
        }
    }

    private void c(String str) {
        this.d = new ad(this, str);
        this.d.startWatching();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(8:14|15|16|17|18|19|20|21) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x0050 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void c(String str, String str2) {
        synchronized (this) {
            boolean z = true;
            if (!TextUtils.isEmpty(str2)) {
                this.f.put(str, str2);
            } else if (this.f.containsKey(str)) {
                this.f.remove(str);
            } else {
                z = false;
            }
            String str3 = a;
            StringBuilder sb = new StringBuilder();
            sb.append("put value: key=");
            sb.append(str);
            sb.append(" ,value=");
            sb.append(str2);
            k.c(str3, sb.toString());
            if (z) {
                FutureTask futureTask = new FutureTask(new ae(this, str2, str), null);
                try {
                    c.a(futureTask);
                    futureTask.get();
                } catch (RejectedExecutionException e2) {
                    String str4 = a;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("execute failed with ");
                    sb2.append(e2);
                    k.c(str4, sb2.toString());
                }
            }
        }
    }

    public float a(String str, float f2) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                try {
                    float floatValue = Float.valueOf((String) this.f.get(str)).floatValue();
                    return floatValue;
                } catch (NumberFormatException unused) {
                    return f2;
                }
            }
        }
    }

    public int a(String str, int i) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                try {
                    int intValue = Integer.valueOf((String) this.f.get(str)).intValue();
                    return intValue;
                } catch (NumberFormatException unused) {
                    return i;
                }
            }
        }
    }

    public long a(String str, long j) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                try {
                    long longValue = Long.valueOf((String) this.f.get(str)).longValue();
                    return longValue;
                } catch (NumberFormatException unused) {
                    return j;
                }
            }
        }
    }

    public String a(String str, String str2) {
        synchronized (this) {
            if (!this.f.containsKey(str)) {
                return str2;
            }
            String str3 = (String) this.f.get(str);
            return str3;
        }
    }

    public boolean a(String str) {
        boolean containsKey;
        synchronized (this) {
            containsKey = this.f.containsKey(str);
        }
        return containsKey;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0028, code lost:
        return r3;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean a(String str, boolean z) {
        synchronized (this) {
            if (this.f.containsKey(str)) {
                String str2 = (String) this.f.get(str);
                if ("true".equalsIgnoreCase(str2)) {
                    return true;
                }
                if ("false".equalsIgnoreCase(str2)) {
                    return false;
                }
            }
        }
    }

    public void b(String str) {
        b(str, (String) null);
    }

    public void b(String str, float f2) {
        c(str, Float.toString(f2));
    }

    public void b(String str, int i) {
        c(str, Integer.toString(i));
    }

    public void b(String str, long j) {
        c(str, Long.toString(j));
    }

    public void b(String str, String str2) {
        c(str, str2);
    }

    public void b(String str, boolean z) {
        c(str, Boolean.toString(z));
    }
}
