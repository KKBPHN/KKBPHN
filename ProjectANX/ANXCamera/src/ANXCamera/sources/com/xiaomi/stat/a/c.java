package com.xiaomi.stat.a;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.text.TextUtils;
import com.xiaomi.stat.MiStatParams;
import com.xiaomi.stat.a.l.a;
import com.xiaomi.stat.ak;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.l;
import com.xiaomi.stat.d.m;
import java.io.File;
import java.util.ArrayList;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class c {
    private static final String a = "EventManager";
    private static final int b = 10;
    private static final int c = 0;
    private static final int d = 300;
    private static final int e = 122880;
    private static final int f = 55;
    private static final int g = 2;
    private static final String h = "priority DESC, _id ASC";
    private static final int i = 7;
    private static final long j = 52428800;
    private static c k;
    /* access modifiers changed from: private */
    public a l;
    private File m;

    private c() {
        Context a2 = ak.a();
        this.l = new a(a2);
        this.m = a2.getDatabasePath(j.a);
    }

    public static c a() {
        if (k == null) {
            synchronized (c.class) {
                if (k == null) {
                    k = new c();
                }
            }
        }
        return k;
    }

    private void a(MiStatParams miStatParams) {
        miStatParams.putString(a.n, com.xiaomi.stat.d.c.b());
        miStatParams.putString(a.o, com.xiaomi.stat.a.g);
        miStatParams.putString(a.p, m.c());
        miStatParams.putString(a.q, m.d());
        miStatParams.putString(a.r, l.b(ak.a()));
        miStatParams.putString(a.s, m.a(ak.a()));
        miStatParams.putString(a.t, Build.MANUFACTURER);
        miStatParams.putString(a.u, Build.MODEL);
        miStatParams.putString(a.v, m.b());
    }

    private boolean a(b[] bVarArr, String str, String str2, boolean z) {
        for (b a2 : bVarArr) {
            if (a2.a(str, str2, z)) {
                return true;
            }
        }
        return false;
    }

    /* access modifiers changed from: private */
    /* JADX WARNING: Can't wrap try/catch for region: R(5:30|(4:31|32|33|(14:34|35|36|37|38|39|40|41|42|43|44|45|46|47))|58|59|(2:92|61)(3:62|95|63)) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:58:0x013c */
    /* JADX WARNING: Removed duplicated region for block: B:62:0x014a A[Catch:{ Exception -> 0x0185, all -> 0x0183 }] */
    /* JADX WARNING: Removed duplicated region for block: B:84:0x0198  */
    /* JADX WARNING: Removed duplicated region for block: B:90:0x01a1  */
    /* JADX WARNING: Removed duplicated region for block: B:92:0x0144 A[SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public k b(b[] bVarArr) {
        Cursor cursor;
        Cursor cursor2;
        String str;
        boolean z;
        ArrayList arrayList;
        JSONArray jSONArray;
        boolean z2;
        String str2;
        String str3;
        String str4;
        JSONArray jSONArray2;
        ArrayList arrayList2;
        c cVar = this;
        b[] bVarArr2 = bVarArr;
        String str5 = "ps";
        String str6 = "ts";
        String str7 = "tp";
        String str8 = "eg";
        String str9 = "e";
        try {
            if (bVarArr2.length == 1) {
                str = bVarArr2[0].a();
                z = false;
            } else {
                z = true;
                str = null;
            }
            cursor = cVar.l.getReadableDatabase().query(j.b, null, str, null, null, null, h);
            try {
                int columnIndex = cursor.getColumnIndex("_id");
                int columnIndex2 = cursor.getColumnIndex(str9);
                int columnIndex3 = cursor.getColumnIndex(str8);
                int columnIndex4 = cursor.getColumnIndex(str7);
                int columnIndex5 = cursor.getColumnIndex(str6);
                int columnIndex6 = cursor.getColumnIndex(str5);
                int columnIndex7 = cursor.getColumnIndex(j.i);
                String str10 = str5;
                int columnIndex8 = cursor.getColumnIndex(j.j);
                String str11 = str6;
                JSONArray jSONArray3 = new JSONArray();
                ArrayList arrayList3 = new ArrayList();
                int i2 = 0;
                while (true) {
                    if (!cursor.moveToNext()) {
                        arrayList = arrayList3;
                        jSONArray = jSONArray3;
                        z2 = true;
                        break;
                    }
                    ArrayList arrayList4 = arrayList3;
                    String str12 = str7;
                    long j2 = cursor.getLong(columnIndex);
                    int i3 = columnIndex;
                    String string = cursor.getString(columnIndex2);
                    int i4 = columnIndex2;
                    String string2 = cursor.getString(columnIndex3);
                    int i5 = columnIndex3;
                    String string3 = cursor.getString(columnIndex4);
                    long j3 = j2;
                    long j4 = cursor.getLong(columnIndex5);
                    int i6 = columnIndex5;
                    String string4 = cursor.getString(columnIndex6);
                    int i7 = columnIndex6;
                    String string5 = cursor.getString(columnIndex7);
                    int i8 = columnIndex7;
                    int i9 = columnIndex8;
                    boolean z3 = cursor.getInt(columnIndex8) == 1;
                    if (z) {
                        if (!cVar.a(bVarArr2, string5, string2, z3)) {
                            str4 = str10;
                            str2 = str11;
                            jSONArray2 = jSONArray3;
                            str3 = str12;
                            arrayList2 = arrayList4;
                            cVar = this;
                            jSONArray3 = jSONArray2;
                            str10 = str4;
                            str7 = str3;
                            str11 = str2;
                            columnIndex = i3;
                            columnIndex2 = i4;
                            columnIndex3 = i5;
                            columnIndex5 = i6;
                            columnIndex6 = i7;
                            columnIndex7 = i8;
                            columnIndex8 = i9;
                            arrayList3 = arrayList2;
                            bVarArr2 = bVarArr;
                        }
                    }
                    int length = i2 + (string4.length() * 2) + 55;
                    if (!TextUtils.isEmpty(string)) {
                        length += string.length() * 2;
                    }
                    if (!TextUtils.isEmpty(string2)) {
                        length += string2.length() * 2;
                    }
                    int i10 = length;
                    if (i10 > e) {
                        jSONArray = jSONArray3;
                        arrayList = arrayList4;
                        z2 = false;
                        break;
                    }
                    JSONObject jSONObject = new JSONObject();
                    try {
                        jSONObject.put(str9, string);
                        jSONObject.put(str8, string2);
                        str3 = str12;
                        try {
                            jSONObject.put(str3, string3);
                            str2 = str11;
                            try {
                                jSONObject.put(str2, j4);
                                long j5 = j3;
                                jSONObject.put(a.g, j5);
                                str4 = str10;
                                try {
                                    jSONObject.put(str4, new JSONObject(string4));
                                    jSONArray2 = jSONArray3;
                                    try {
                                        jSONArray2.put(jSONObject);
                                        arrayList2 = arrayList4;
                                        arrayList2.add(Long.valueOf(j5));
                                    } catch (JSONException unused) {
                                        arrayList2 = arrayList4;
                                        if (arrayList2.size() >= 300) {
                                        }
                                    }
                                } catch (JSONException unused2) {
                                    jSONArray2 = jSONArray3;
                                    arrayList2 = arrayList4;
                                    if (arrayList2.size() >= 300) {
                                    }
                                }
                            } catch (JSONException unused3) {
                                str4 = str10;
                                jSONArray2 = jSONArray3;
                                arrayList2 = arrayList4;
                                if (arrayList2.size() >= 300) {
                                }
                            }
                        } catch (JSONException unused4) {
                            str4 = str10;
                            str2 = str11;
                            jSONArray2 = jSONArray3;
                            arrayList2 = arrayList4;
                            if (arrayList2.size() >= 300) {
                            }
                        }
                    } catch (JSONException unused5) {
                        str4 = str10;
                        str2 = str11;
                        jSONArray2 = jSONArray3;
                        str3 = str12;
                        arrayList2 = arrayList4;
                        if (arrayList2.size() >= 300) {
                        }
                    }
                    if (arrayList2.size() >= 300) {
                        z2 = cursor.isLast();
                        break;
                    }
                    i2 = i10;
                    cVar = this;
                    jSONArray3 = jSONArray2;
                    str10 = str4;
                    str7 = str3;
                    str11 = str2;
                    columnIndex = i3;
                    columnIndex2 = i4;
                    columnIndex3 = i5;
                    columnIndex5 = i6;
                    columnIndex6 = i7;
                    columnIndex7 = i8;
                    columnIndex8 = i9;
                    arrayList3 = arrayList2;
                    bVarArr2 = bVarArr;
                }
                if (arrayList.size() > 0) {
                    k kVar = new k(jSONArray, arrayList, z2);
                    if (cursor != null) {
                        cursor.close();
                    }
                    return kVar;
                }
                if (cursor != null) {
                    cursor.close();
                }
                return null;
            } catch (Exception e2) {
                e = e2;
                cursor2 = cursor;
                try {
                    k.b(a, e.toString());
                    if (cursor2 != null) {
                    }
                    return null;
                } catch (Throwable th) {
                    th = th;
                    cursor = cursor2;
                    if (cursor != null) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                if (cursor != null) {
                }
                throw th;
            }
        } catch (Exception e3) {
            e = e3;
            cursor2 = null;
            k.b(a, e.toString());
            if (cursor2 != null) {
                cursor2.close();
            }
            return null;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }

    /* access modifiers changed from: private */
    public void b(l lVar) {
        d();
        SQLiteDatabase writableDatabase = this.l.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("e", lVar.a);
        contentValues.put("eg", lVar.b);
        contentValues.put("tp", lVar.c);
        contentValues.put("ts", Long.valueOf(lVar.e));
        if (c(lVar)) {
            a((MiStatParams) lVar.d);
        }
        contentValues.put("ps", lVar.d.toJsonString());
        contentValues.put(j.i, lVar.f);
        contentValues.put(j.j, Integer.valueOf(lVar.g ? 1 : 0));
        contentValues.put(j.k, Integer.valueOf(TextUtils.equals(lVar.b, a.h) ? 10 : 0));
        writableDatabase.insert(j.b, null, contentValues);
    }

    /* access modifiers changed from: private */
    public void b(ArrayList arrayList) {
        if (arrayList != null && arrayList.size() != 0) {
            try {
                SQLiteDatabase writableDatabase = this.l.getWritableDatabase();
                StringBuilder sb = new StringBuilder(((Long.toString(((Long) arrayList.get(0)).longValue()).length() + 1) * arrayList.size()) + 16);
                sb.append("_id");
                sb.append(" in (");
                sb.append(arrayList.get(0));
                int size = arrayList.size();
                for (int i2 = 1; i2 < size; i2++) {
                    sb.append(",");
                    sb.append(arrayList.get(i2));
                }
                sb.append(")");
                int delete = writableDatabase.delete(j.b, sb.toString(), null);
                String str = a;
                StringBuilder sb2 = new StringBuilder();
                sb2.append("deleted events number ");
                sb2.append(delete);
                k.c(str, sb2.toString());
            } catch (Exception unused) {
            }
        }
    }

    private boolean c(l lVar) {
        return !lVar.c.startsWith(a.w);
    }

    private void d() {
        if (this.m.exists() && this.m.length() >= j) {
            StringBuilder sb = new StringBuilder();
            sb.append("database too big: ");
            sb.append(this.m.length());
            k.e(a, sb.toString());
            this.l.getWritableDatabase().delete(j.b, null, null);
        }
    }

    public k a(b[] bVarArr) {
        FutureTask futureTask = new FutureTask(new e(this, bVarArr));
        com.xiaomi.stat.c.a(futureTask);
        try {
            return (k) futureTask.get();
        } catch (InterruptedException | ExecutionException unused) {
            return null;
        }
    }

    public void a(l lVar) {
        com.xiaomi.stat.c.a(new d(this, lVar));
        StringBuilder sb = new StringBuilder();
        sb.append("add event: name=");
        sb.append(lVar.a);
        k.c(a, sb.toString());
    }

    public void a(String str) {
        com.xiaomi.stat.c.a(new h(this, str));
    }

    public void a(ArrayList arrayList) {
        FutureTask futureTask = new FutureTask(new f(this, arrayList), null);
        com.xiaomi.stat.c.a(futureTask);
        try {
            futureTask.get();
        } catch (InterruptedException | ExecutionException unused) {
        }
    }

    public void b() {
        com.xiaomi.stat.c.a(new g(this));
    }

    public long c() {
        FutureTask futureTask = new FutureTask(new i(this));
        com.xiaomi.stat.c.a(futureTask);
        try {
            return ((Long) futureTask.get()).longValue();
        } catch (InterruptedException | ExecutionException unused) {
            return -1;
        }
    }
}
