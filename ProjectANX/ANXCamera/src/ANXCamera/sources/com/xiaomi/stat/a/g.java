package com.xiaomi.stat.a;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;
import com.xiaomi.stat.a.l.a;
import com.xiaomi.stat.aj;
import com.xiaomi.stat.d.k;
import com.xiaomi.stat.d.r;
import java.util.Calendar;

class g implements Runnable {
    final /* synthetic */ c a;

    g(c cVar) {
        this.a = cVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:33:0x0138  */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0140  */
    /* JADX WARNING: Removed duplicated region for block: B:45:? A[RETURN, SYNTHETIC] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        Cursor cursor;
        Cursor cursor2;
        String str;
        String str2 = "ts";
        String str3 = "EventManager";
        try {
            SQLiteDatabase writableDatabase = this.a.l.getWritableDatabase();
            Calendar instance = Calendar.getInstance();
            instance.setTimeInMillis(r.b());
            instance.set(6, instance.get(6) - 7);
            instance.set(11, 0);
            instance.set(12, 0);
            instance.set(13, 0);
            String str4 = "ts < ? and e != ?";
            String[] strArr = {Long.toString(instance.getTimeInMillis()), a.m};
            int i = 1;
            String[] strArr2 = strArr;
            int i2 = 2;
            cursor = writableDatabase.query(j.b, new String[]{str2}, str4, strArr, null, null, "ts ASC");
            try {
                int count = cursor.getCount();
                if (count != 0) {
                    aj ajVar = new aj();
                    ajVar.putInt(a.x, count);
                    StringBuilder sb = new StringBuilder();
                    sb.append("delete obsolete events total number ");
                    sb.append(count);
                    k.c(str3, sb.toString());
                    int columnIndex = cursor.getColumnIndex(str2);
                    String str5 = null;
                    int i3 = 0;
                    while (true) {
                        boolean moveToNext = cursor.moveToNext();
                        str = a.y;
                        if (!moveToNext) {
                            break;
                        }
                        instance.setTimeInMillis(cursor.getLong(columnIndex));
                        String format = String.format("%4d%02d%02d", new Object[]{Integer.valueOf(instance.get(i)), Integer.valueOf(instance.get(i2) + i), Integer.valueOf(instance.get(5))});
                        if (!TextUtils.equals(str5, format)) {
                            if (str5 != null) {
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append(str);
                                sb2.append(str5);
                                ajVar.putInt(sb2.toString(), i3);
                            }
                            str5 = format;
                            i3 = 1;
                        } else {
                            i3++;
                        }
                        i = 1;
                        i2 = 2;
                    }
                    if (str5 != null) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append(str);
                        sb3.append(str5);
                        ajVar.putInt(sb3.toString(), i3);
                    }
                    this.a.b(l.a(ajVar));
                    writableDatabase.delete(j.b, str4, strArr2);
                }
                if (cursor != null) {
                    cursor.close();
                }
            } catch (Exception e) {
                e = e;
                cursor2 = cursor;
                try {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("remove obsolete events failed with ");
                    sb4.append(e);
                    k.c(str3, sb4.toString());
                    if (cursor2 == null) {
                    }
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
        } catch (Exception e2) {
            e = e2;
            cursor2 = null;
            StringBuilder sb42 = new StringBuilder();
            sb42.append("remove obsolete events failed with ");
            sb42.append(e);
            k.c(str3, sb42.toString());
            if (cursor2 == null) {
                cursor2.close();
            }
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
                cursor.close();
            }
            throw th;
        }
    }
}
