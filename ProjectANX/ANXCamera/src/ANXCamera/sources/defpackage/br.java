package defpackage;

import android.graphics.Bitmap;

/* renamed from: br reason: default package */
public final class br {
    public final bs a;

    public br() {
        this.a = new bs();
    }

    public br(bs bsVar) {
        this.a = bsVar;
    }

    public final void O000000o(Bitmap bitmap) {
        this.a.b = bitmap;
    }

    public final bs a() {
        bs bsVar = this.a;
        if (bsVar.a == null || bsVar.b == null) {
            return bsVar;
        }
        throw new IllegalStateException("Cannot set both Bitmap and Bitmap URI.");
    }

    public final void a(Long l) {
        this.a.c = l;
    }
}
