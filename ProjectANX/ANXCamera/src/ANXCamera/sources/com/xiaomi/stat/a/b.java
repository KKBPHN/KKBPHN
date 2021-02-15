package com.xiaomi.stat.a;

import android.text.TextUtils;
import com.xiaomi.stat.a.l.a;

public class b {
    public static final int a = 0;
    public static final int b = 1;
    private String c;
    private int d;
    private boolean e;
    private boolean f;

    public b(String str, int i, boolean z) {
        this.c = str;
        this.d = i;
        this.e = z;
        this.f = TextUtils.isEmpty(str);
    }

    public String a() {
        StringBuilder sb = new StringBuilder();
        sb.append(j.i);
        String str = "\"";
        String str2 = " = \"";
        if (this.f) {
            sb.append(" is null");
        } else {
            sb.append(str2);
            sb.append(this.c);
            sb.append(str);
        }
        String str3 = " and ";
        if (this.d != 0) {
            sb.append(str3);
            sb.append("eg");
            sb.append(str2);
            sb.append(a.h);
            sb.append(str);
        }
        sb.append(str3);
        sb.append(j.j);
        sb.append(" = ");
        sb.append(this.e ? 1 : 0);
        return sb.toString();
    }

    public boolean a(String str, String str2, boolean z) {
        if (TextUtils.equals(str, this.c) && this.e == z) {
            if (this.d == 0) {
                return true;
            }
            return this.f && TextUtils.equals(str2, a.h);
        }
    }
}
