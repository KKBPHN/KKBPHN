package com.airbnb.lottie.O000000o.O000000o;

import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O00000Oo;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O000000o.O00000Oo.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.C0102O00000oO;
import com.airbnb.lottie.model.content.C0107O0000Ooo;
import com.airbnb.lottie.model.layer.O00000o0;
import java.util.ArrayList;
import java.util.List;

public class O0000Oo0 implements O0000O0o, O00000Oo, O0000o00 {
    private final C0083O000OoO0 O000OoO0;
    private final List O00O0o00 = new ArrayList();
    private final O0000O0o O00O0oOO;
    @Nullable
    private O0000O0o O00O0oo;
    private final O0000O0o O00OOOo;
    private final boolean hidden;
    private final O00000o0 layer;
    private final String name;
    private final Paint paint = new O000000o(1);
    private final Path path = new Path();

    public O0000Oo0(C0083O000OoO0 o000OoO0, O00000o0 o00000o0, C0107O0000Ooo o0000Ooo) {
        this.layer = o00000o0;
        this.name = o0000Ooo.getName();
        this.hidden = o0000Ooo.isHidden();
        this.O000OoO0 = o000OoO0;
        if (o0000Ooo.getColor() == null || o0000Ooo.getOpacity() == null) {
            this.O00OOOo = null;
            this.O00O0oOO = null;
            return;
        }
        this.path.setFillType(o0000Ooo.getFillType());
        this.O00OOOo = o0000Ooo.getColor().O00000o();
        this.O00OOOo.O00000Oo(this);
        o00000o0.O000000o(this.O00OOOo);
        this.O00O0oOO = o0000Ooo.getOpacity().O00000o();
        this.O00O0oOO.O00000Oo(this);
        o00000o0.O000000o(this.O00O0oOO);
    }

    public void O000000o(Canvas canvas, Matrix matrix, int i) {
        if (!this.hidden) {
            String str = "FillContent#draw";
            C0053O00000oO.beginSection(str);
            this.paint.setColor(((O0000OOo) this.O00OOOo).getIntValue());
            this.paint.setAlpha(com.airbnb.lottie.O00000o.O0000O0o.clamp((int) ((((((float) i) / 255.0f) * ((float) ((Integer) this.O00O0oOO.getValue()).intValue())) / 100.0f) * 255.0f), 0, 255));
            O0000O0o o0000O0o = this.O00O0oo;
            if (o0000O0o != null) {
                this.paint.setColorFilter((ColorFilter) o0000O0o.getValue());
            }
            this.path.reset();
            for (int i2 = 0; i2 < this.O00O0o00.size(); i2++) {
                this.path.addPath(((O0000o) this.O00O0o00.get(i2)).getPath(), matrix);
            }
            canvas.drawPath(this.path, this.paint);
            C0053O00000oO.O0000oOo(str);
        }
    }

    public void O000000o(RectF rectF, Matrix matrix, boolean z) {
        this.path.reset();
        for (int i = 0; i < this.O00O0o00.size(); i++) {
            this.path.addPath(((O0000o) this.O00O0o00.get(i)).getPath(), matrix);
        }
        this.path.computeBounds(rectF, false);
        rectF.set(rectF.left - 1.0f, rectF.top - 1.0f, rectF.right + 1.0f, rectF.bottom + 1.0f);
    }

    public void O000000o(C0102O00000oO o00000oO, int i, List list, C0102O00000oO o00000oO2) {
        com.airbnb.lottie.O00000o.O0000O0o.O000000o(o00000oO, i, list, o00000oO2, this);
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        O0000O0o o0000O0o;
        if (obj == C0087O000Ooo0.COLOR) {
            o0000O0o = this.O00OOOo;
        } else if (obj == C0087O000Ooo0.OO000oO) {
            o0000O0o = this.O00O0oOO;
        } else if (obj != C0087O000Ooo0.OO00ooO) {
            return;
        } else {
            if (o0000Oo == null) {
                this.O00O0oo = null;
                return;
            }
            this.O00O0oo = new C0031O0000oo0(o0000Oo);
            this.O00O0oo.O00000Oo(this);
            this.layer.O000000o(this.O00O0oo);
            return;
        }
        o0000O0o.O000000o(o0000Oo);
    }

    public void O000000o(List list, List list2) {
        for (int i = 0; i < list2.size(); i++) {
            C0006O00000oO o00000oO = (C0006O00000oO) list2.get(i);
            if (o00000oO instanceof O0000o) {
                this.O00O0o00.add((O0000o) o00000oO);
            }
        }
    }

    public void O00000oO() {
        this.O000OoO0.invalidateSelf();
    }

    public String getName() {
        return this.name;
    }
}
