package com.arcsoft.camera.utils;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class b implements Parcelable {
    public static final Creator c = new C0704O00000oO();
    public int a;
    public int b;

    public b() {
    }

    public b(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public b(b bVar) {
        this.a = bVar.a;
        this.b = bVar.b;
    }

    public void O000000o(int i, int i2) {
        this.a = i;
        this.b = i2;
    }

    public void O000000o(Parcel parcel, int i) {
        parcel.writeInt(this.a);
        parcel.writeInt(this.b);
    }

    public final void O00000oO(int i, int i2) {
        this.a += i;
        this.b += i2;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:9:0x001b, code lost:
        if (r3.b != r4.b) goto L_0x001d;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean O00000oO(Object obj) {
        if (this != obj) {
            if (obj != null && b.class == obj.getClass()) {
                b bVar = (b) obj;
                if (this.a != bVar.a) {
                    return false;
                }
            }
            return false;
        }
        return true;
    }

    public int a() {
        return 0;
    }

    public void a(Parcel parcel) {
        this.a = parcel.readInt();
        this.b = parcel.readInt();
    }

    public final boolean a(int i, int i2) {
        return this.a == i && this.b == i2;
    }

    public int b() {
        return (this.a * 31) + this.b;
    }

    public final void c() {
        this.a = -this.a;
        this.b = -this.b;
    }

    public String d() {
        StringBuilder sb = new StringBuilder();
        sb.append("Point(");
        sb.append(this.a);
        sb.append(", ");
        sb.append(this.b);
        sb.append(")");
        return sb.toString();
    }
}
