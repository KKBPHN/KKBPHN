package defpackage;

import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.util.Log;
import java.util.List;

/* renamed from: ak reason: default package */
final class ak extends AsyncTask {
    final /* synthetic */ al a;
    private int b;
    private int c;

    public ak(al alVar) {
        this.a = alVar;
    }

    /* JADX WARNING: Removed duplicated region for block: B:42:0x0075  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private final int a(String str) {
        Throwable th;
        Cursor cursor;
        if (al.c.contains(Build.MODEL)) {
            return this.a.a() ? bh.b : bh.c;
        }
        try {
            cursor = this.a.d.getContentResolver().query(Uri.parse(str), null, null, null, null);
            if (cursor != null) {
                try {
                    if (cursor.getCount() != 0) {
                        cursor.moveToFirst();
                        int parseInt = Integer.parseInt(cursor.getString(0));
                        if (parseInt > 12) {
                            parseInt = 12;
                        }
                        if (bh.a(parseInt) != 0) {
                            int a2 = bh.a(parseInt);
                            cursor.close();
                            return a2;
                        }
                        int i = bh.m;
                        cursor.close();
                        return i;
                    }
                } catch (Throwable th2) {
                    th = th2;
                    if (cursor != null) {
                        cursor.close();
                    }
                    throw th;
                }
            }
            int i2 = bh.f;
            if (cursor != null) {
                cursor.close();
            }
            return i2;
        } catch (Exception e) {
            Log.e("LensSdkParamsReader", "Failed to start Lens due to unexpected exception.", e);
            return bh.f;
        } catch (Throwable th3) {
            th = th3;
            cursor = null;
            if (cursor != null) {
            }
            throw th;
        }
    }

    public final void a(int i, int i2) {
        int i3 = i - 2;
        if (i != 0) {
            StringBuilder sb = new StringBuilder(36);
            sb.append("Lens availability result:");
            sb.append(i3);
            sb.toString();
            int i4 = i2 - 2;
            if (i2 != 0) {
                StringBuilder sb2 = new StringBuilder(40);
                sb2.append("Stickers availability result:");
                sb2.append(i4);
                sb2.toString();
                al alVar = this.a;
                String str = al.a;
                bi biVar = alVar.f;
                da daVar = (da) biVar.b(5);
                daVar.O000000o((de) biVar);
                bf bfVar = (bf) daVar;
                if (bfVar.c) {
                    bfVar.b();
                    bfVar.c = false;
                }
                de deVar = bfVar.b;
                bi biVar2 = (bi) deVar;
                bi biVar3 = bi.f;
                if (i != 0) {
                    biVar2.d = i3;
                    biVar2.a |= 4;
                    bi biVar4 = (bi) deVar;
                    if (i2 != 0) {
                        biVar4.e = i4;
                        biVar4.a |= 8;
                        alVar.f = (bi) bfVar.h();
                        al alVar2 = this.a;
                        alVar2.g = true;
                        List list = alVar2.e;
                        int size = list.size();
                        for (int i5 = 0; i5 < size; i5++) {
                            ((ai) list.get(i5)).O000000o(this.a.f);
                        }
                        this.a.e.clear();
                        return;
                    }
                    throw null;
                }
                throw null;
            }
            throw null;
        }
        throw null;
    }

    /* access modifiers changed from: protected */
    public final /* bridge */ /* synthetic */ Object doInBackground(Object[] objArr) {
        Void[] voidArr = (Void[]) objArr;
        this.b = a(al.a);
        this.c = a(al.b);
        return null;
    }

    /* access modifiers changed from: protected */
    public final /* bridge */ /* synthetic */ void onPostExecute(Object obj) {
        Void voidR = (Void) obj;
        a(this.b, this.c);
    }

    /* access modifiers changed from: protected */
    public final void onPreExecute() {
        al alVar = this.a;
        String str = al.a;
        new Handler(alVar.d.getMainLooper()).postDelayed(new aj(this), 2000);
    }
}
