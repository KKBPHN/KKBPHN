package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;

public class O0000Oo extends O00000o0 {
    @Nullable
    private O0000O0o O00O0oo;
    private final O0000O0o O00oo0Oo;
    private final Paint paint = new O000000o();
    private final Path path = new Path();
    private final float[] points = new float[8];
    private final RectF rect = new RectF();

    O0000Oo(C0083O000OoO0 o000OoO0, O0000O0o o0000O0o) {
        super(o000OoO0, o0000O0o);
        this.O00oo0Oo = o0000O0o;
        this.paint.setAlpha(0);
        this.paint.setStyle(Style.FILL);
        this.paint.setColor(o0000O0o.getSolidColor());
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        super.O000000o(rectF, matrix, z);
        this.rect.set(0.0f, 0.0f, (float) this.O00oo0Oo.O00o00o0(), (float) this.O00oo0Oo.O00o00Oo());
        this.O00oo0OO.mapRect(this.rect);
        rectF.set(this.rect);
    }

    public void O000000o(Object obj, @Nullable com.airbnb.lottie.O00000oO.O0000Oo o0000Oo) {
        super.O000000o(obj, o0000Oo);
        if (obj == C0087O000Ooo0.OO00ooO) {
            this.O00O0oo = o0000Oo == null ? null : new C0031O0000oo0(o0000Oo);
        }
    }

    public void O00000Oo(Canvas canvas, Matrix matrix, int i) {
        int alpha = Color.alpha(this.O00oo0Oo.getSolidColor());
        if (alpha != 0) {
            int intValue = (int) ((((float) i) / 255.0f) * (((((float) alpha) / 255.0f) * ((float) (this.O00Ooo0O.getOpacity() == null ? 100 : ((Integer) this.O00Ooo0O.getOpacity().getValue()).intValue()))) / 100.0f) * 255.0f);
            this.paint.setAlpha(intValue);
            O0000O0o o0000O0o = this.O00O0oo;
            if (o0000O0o != null) {
                this.paint.setColorFilter((ColorFilter) o0000O0o.getValue());
            }
            if (intValue > 0) {
                float[] fArr = this.points;
                fArr[0] = 0.0f;
                fArr[1] = 0.0f;
                fArr[2] = (float) this.O00oo0Oo.O00o00o0();
                float[] fArr2 = this.points;
                fArr2[3] = 0.0f;
                fArr2[4] = (float) this.O00oo0Oo.O00o00o0();
                this.points[5] = (float) this.O00oo0Oo.O00o00Oo();
                float[] fArr3 = this.points;
                fArr3[6] = 0.0f;
                fArr3[7] = (float) this.O00oo0Oo.O00o00Oo();
                matrix.mapPoints(this.points);
                this.path.reset();
                Path path2 = this.path;
                float[] fArr4 = this.points;
                path2.moveTo(fArr4[0], fArr4[1]);
                Path path3 = this.path;
                float[] fArr5 = this.points;
                path3.lineTo(fArr5[2], fArr5[3]);
                Path path4 = this.path;
                float[] fArr6 = this.points;
                path4.lineTo(fArr6[4], fArr6[5]);
                Path path5 = this.path;
                float[] fArr7 = this.points;
                path5.lineTo(fArr7[6], fArr7[7]);
                Path path6 = this.path;
                float[] fArr8 = this.points;
                path6.lineTo(fArr8[0], fArr8[1]);
                this.path.close();
                canvas.drawPath(this.path, this.paint);
            }
        }
    }
}
