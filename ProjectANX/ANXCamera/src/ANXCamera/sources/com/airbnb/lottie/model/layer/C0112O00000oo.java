package com.airbnb.lottie.model.layer;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;

/* renamed from: com.airbnb.lottie.model.layer.O00000oo reason: case insensitive filesystem */
public class C0112O00000oo extends O00000o0 {
    @Nullable
    private O0000O0o O00O0oo;
    private final Rect O00ooOO = new Rect();
    private final Paint paint = new O000000o(3);
    private final Rect src = new Rect();

    C0112O00000oo(C0083O000OoO0 o000OoO0, O0000O0o o0000O0o) {
        super(o000OoO0, o0000O0o);
    }

    @Nullable
    private Bitmap getBitmap() {
        return this.O000OoO0.O000000o(this.O00oo0Oo.O00o00OO());
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        super.O000000o(rectF, matrix, z);
        Bitmap bitmap = getBitmap();
        if (bitmap != null) {
            rectF.set(0.0f, 0.0f, ((float) bitmap.getWidth()) * O0000OOo.O00o0O0O(), ((float) bitmap.getHeight()) * O0000OOo.O00o0O0O());
            this.O00oo0OO.mapRect(rectF);
        }
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        super.O000000o(obj, o0000Oo);
        if (obj == C0087O000Ooo0.OO00ooO) {
            this.O00O0oo = o0000Oo == null ? null : new C0031O0000oo0(o0000Oo);
        }
    }

    public void O00000Oo(@NonNull Canvas canvas, Matrix matrix, int i) {
        Bitmap bitmap = getBitmap();
        if (bitmap != null && !bitmap.isRecycled()) {
            float O00o0O0O = O0000OOo.O00o0O0O();
            this.paint.setAlpha(i);
            O0000O0o o0000O0o = this.O00O0oo;
            if (o0000O0o != null) {
                this.paint.setColorFilter((ColorFilter) o0000O0o.getValue());
            }
            canvas.save();
            canvas.concat(matrix);
            this.src.set(0, 0, bitmap.getWidth(), bitmap.getHeight());
            this.O00ooOO.set(0, 0, (int) (((float) bitmap.getWidth()) * O00o0O0O), (int) (((float) bitmap.getHeight()) * O00o0O0O));
            canvas.drawBitmap(bitmap, this.src, this.O00ooOO, this.paint);
            canvas.restore();
        }
    }
}
