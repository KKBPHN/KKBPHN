package com.airbnb.lottie.model.layer;

import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Typeface;
import androidx.annotation.Nullable;
import androidx.collection.LongSparseArray;
import com.airbnb.lottie.C0064O0000o0O;
import com.airbnb.lottie.C0083O000OoO0;
import com.airbnb.lottie.C0087O000Ooo0;
import com.airbnb.lottie.C0094O000o0OO;
import com.airbnb.lottie.O000000o.O000000o.C0007O00000oo;
import com.airbnb.lottie.O000000o.O00000Oo.C0028O0000oOO;
import com.airbnb.lottie.O000000o.O00000Oo.C0031O0000oo0;
import com.airbnb.lottie.O000000o.O00000Oo.O0000O0o;
import com.airbnb.lottie.O00000o.O0000OOo;
import com.airbnb.lottie.O00000oO.O0000Oo;
import com.airbnb.lottie.model.DocumentData$Justification;
import com.airbnb.lottie.model.O000000o.C0099O0000OoO;
import com.airbnb.lottie.model.O000000o.O000000o;
import com.airbnb.lottie.model.O000000o.O00000Oo;
import com.airbnb.lottie.model.O00000o;
import com.airbnb.lottie.model.O00000o0;
import com.airbnb.lottie.model.content.O0000o00;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class O0000o0 extends O00000o0 {
    private final C0064O0000o0O O00000oo;
    private final C0083O000OoO0 O000OoO0;
    @Nullable
    private O0000O0o O00OOOo;
    private final RectF O00ooOOO = new RectF();
    private final Paint O00ooOOo = new C0113O0000OoO(this, 1);
    private final Paint O00ooOo = new C0114O0000Ooo(this, 1);
    private final Map O00ooOoO = new HashMap();
    private final LongSparseArray O00ooOoo = new LongSparseArray();
    @Nullable
    private O0000O0o O00ooo;
    @Nullable
    private O0000O0o O00ooo0;
    private final C0028O0000oOO O00ooo00;
    @Nullable
    private O0000O0o O00ooo0o;
    @Nullable
    private O0000O0o O00oooO0;
    private final Matrix matrix = new Matrix();
    private final StringBuilder stringBuilder = new StringBuilder(2);

    O0000o0(C0083O000OoO0 o000OoO0, O0000O0o o0000O0o) {
        super(o000OoO0, o0000O0o);
        this.O000OoO0 = o000OoO0;
        this.O00000oo = o0000O0o.O000O0OO();
        this.O00ooo00 = o0000O0o.getText().O00000o();
        this.O00ooo00.O00000Oo(this);
        O000000o(this.O00ooo00);
        C0099O0000OoO O00o00oO = o0000O0o.O00o00oO();
        if (O00o00oO != null) {
            O000000o o000000o = O00o00oO.color;
            if (o000000o != null) {
                this.O00OOOo = o000000o.O00000o();
                this.O00OOOo.O00000Oo(this);
                O000000o(this.O00OOOo);
            }
        }
        if (O00o00oO != null) {
            O000000o o000000o2 = O00o00oO.stroke;
            if (o000000o2 != null) {
                this.O00ooo0 = o000000o2.O00000o();
                this.O00ooo0.O00000Oo(this);
                O000000o(this.O00ooo0);
            }
        }
        if (O00o00oO != null) {
            O00000Oo o00000Oo = O00o00oO.strokeWidth;
            if (o00000Oo != null) {
                this.O00ooo0o = o00000Oo.O00000o();
                this.O00ooo0o.O00000Oo(this);
                O000000o(this.O00ooo0o);
            }
        }
        if (O00o00oO != null) {
            O00000Oo o00000Oo2 = O00o00oO.O00oO0O0;
            if (o00000Oo2 != null) {
                this.O00ooo = o00000Oo2.O00000o();
                this.O00ooo.O00000Oo(this);
                O000000o(this.O00ooo);
            }
        }
    }

    private float O000000o(String str, O00000o0 o00000o0, float f, float f2) {
        float f3 = 0.0f;
        for (int i = 0; i < str.length(); i++) {
            O00000o o00000o = (O00000o) this.O00000oo.getCharacters().get(O00000o.O000000o(str.charAt(i), o00000o0.getFamily(), o00000o0.getStyle()));
            if (o00000o != null) {
                f3 = (float) (((double) f3) + (o00000o.getWidth() * ((double) f) * ((double) O0000OOo.O00o0O0O()) * ((double) f2)));
            }
        }
        return f3;
    }

    private List O000000o(O00000o o00000o) {
        if (this.O00ooOoO.containsKey(o00000o)) {
            return (List) this.O00ooOoO.get(o00000o);
        }
        List O00Oo = o00000o.O00Oo();
        int size = O00Oo.size();
        ArrayList arrayList = new ArrayList(size);
        for (int i = 0; i < size; i++) {
            arrayList.add(new C0007O00000oo(this.O000OoO0, this, (O0000o00) O00Oo.get(i)));
        }
        this.O00ooOoO.put(o00000o, arrayList);
        return arrayList;
    }

    private void O000000o(Path path, Paint paint, Canvas canvas) {
        if (paint.getColor() != 0) {
            if (paint.getStyle() != Style.STROKE || paint.getStrokeWidth() != 0.0f) {
                canvas.drawPath(path, paint);
            }
        }
    }

    private void O000000o(DocumentData$Justification documentData$Justification, Canvas canvas, float f) {
        float f2;
        int i = O0000o00.O0O00o0[documentData$Justification.ordinal()];
        if (i != 1) {
            if (i == 2) {
                f2 = -f;
            } else if (i == 3) {
                f2 = (-f) / 2.0f;
            } else {
                return;
            }
            canvas.translate(f2, 0.0f);
        }
    }

    private void O000000o(com.airbnb.lottie.model.O00000Oo o00000Oo, Matrix matrix2, O00000o0 o00000o0, Canvas canvas) {
        com.airbnb.lottie.model.O00000Oo o00000Oo2 = o00000Oo;
        Canvas canvas2 = canvas;
        O0000O0o o0000O0o = this.O00oooO0;
        float floatValue = (o0000O0o == null ? o00000Oo2.size : ((Float) o0000O0o.getValue()).floatValue()) / 100.0f;
        float O000000o2 = O0000OOo.O000000o(matrix2);
        float O00o0O0O = o00000Oo2.lineHeight * O0000OOo.O00o0O0O();
        List O00O0Oo = O00O0Oo(o00000Oo2.text);
        int size = O00O0Oo.size();
        int i = 0;
        while (i < size) {
            String str = (String) O00O0Oo.get(i);
            float O000000o3 = O000000o(str, o00000o0, floatValue, O000000o2);
            canvas.save();
            O000000o(o00000Oo2.O00o, canvas2, O000000o3);
            canvas2.translate(0.0f, (((float) i) * O00o0O0O) - ((((float) (size - 1)) * O00o0O0O) / 2.0f));
            int i2 = i;
            O000000o(str, o00000Oo, matrix2, o00000o0, canvas, O000000o2, floatValue);
            canvas.restore();
            i = i2 + 1;
        }
    }

    private void O000000o(com.airbnb.lottie.model.O00000Oo o00000Oo, O00000o0 o00000o0, Matrix matrix2, Canvas canvas) {
        float O000000o2 = O0000OOo.O000000o(matrix2);
        Typeface O000000o3 = this.O000OoO0.O000000o(o00000o0.getFamily(), o00000o0.getStyle());
        if (O000000o3 != null) {
            String str = o00000Oo.text;
            C0094O000o0OO O000O0o0 = this.O000OoO0.O000O0o0();
            if (O000O0o0 != null) {
                str = O000O0o0.O00oOooO(str);
            }
            this.O00ooOOo.setTypeface(O000000o3);
            O0000O0o o0000O0o = this.O00oooO0;
            this.O00ooOOo.setTextSize((o0000O0o == null ? o00000Oo.size : ((Float) o0000O0o.getValue()).floatValue()) * O0000OOo.O00o0O0O());
            this.O00ooOo.setTypeface(this.O00ooOOo.getTypeface());
            this.O00ooOo.setTextSize(this.O00ooOOo.getTextSize());
            float O00o0O0O = o00000Oo.lineHeight * O0000OOo.O00o0O0O();
            List O00O0Oo = O00O0Oo(str);
            int size = O00O0Oo.size();
            for (int i = 0; i < size; i++) {
                String str2 = (String) O00O0Oo.get(i);
                O000000o(o00000Oo.O00o, canvas, this.O00ooOo.measureText(str2));
                canvas.translate(0.0f, (((float) i) * O00o0O0O) - ((((float) (size - 1)) * O00o0O0O) / 2.0f));
                O000000o(str2, o00000Oo, canvas, O000000o2);
                canvas.setMatrix(matrix2);
            }
        }
    }

    private void O000000o(O00000o o00000o, Matrix matrix2, float f, com.airbnb.lottie.model.O00000Oo o00000Oo, Canvas canvas) {
        Paint paint;
        List O000000o2 = O000000o(o00000o);
        for (int i = 0; i < O000000o2.size(); i++) {
            Path path = ((C0007O00000oo) O000000o2.get(i)).getPath();
            path.computeBounds(this.O00ooOOO, false);
            this.matrix.set(matrix2);
            this.matrix.preTranslate(0.0f, (-o00000Oo.baselineShift) * O0000OOo.O00o0O0O());
            this.matrix.preScale(f, f);
            path.transform(this.matrix);
            if (o00000Oo.O00oO000) {
                O000000o(path, this.O00ooOOo, canvas);
                paint = this.O00ooOo;
            } else {
                O000000o(path, this.O00ooOo, canvas);
                paint = this.O00ooOOo;
            }
            O000000o(path, paint, canvas);
        }
    }

    private void O000000o(String str, Paint paint, Canvas canvas) {
        if (paint.getColor() != 0) {
            if (paint.getStyle() != Style.STROKE || paint.getStrokeWidth() != 0.0f) {
                canvas.drawText(str, 0, str.length(), 0.0f, 0.0f, paint);
            }
        }
    }

    private void O000000o(String str, com.airbnb.lottie.model.O00000Oo o00000Oo, Canvas canvas) {
        Paint paint;
        if (o00000Oo.O00oO000) {
            O000000o(str, this.O00ooOOo, canvas);
            paint = this.O00ooOo;
        } else {
            O000000o(str, this.O00ooOo, canvas);
            paint = this.O00ooOOo;
        }
        O000000o(str, paint, canvas);
    }

    private void O000000o(String str, com.airbnb.lottie.model.O00000Oo o00000Oo, Canvas canvas, float f) {
        int i = 0;
        while (i < str.length()) {
            String O0000O0o = O0000O0o(str, i);
            i += O0000O0o.length();
            O000000o(O0000O0o, o00000Oo, canvas);
            float measureText = this.O00ooOOo.measureText(O0000O0o, 0, 1);
            float f2 = ((float) o00000Oo.O00oO0O0) / 10.0f;
            O0000O0o o0000O0o = this.O00ooo;
            if (o0000O0o != null) {
                f2 += ((Float) o0000O0o.getValue()).floatValue();
            }
            canvas.translate(measureText + (f2 * f), 0.0f);
        }
    }

    private void O000000o(String str, com.airbnb.lottie.model.O00000Oo o00000Oo, Matrix matrix2, O00000o0 o00000o0, Canvas canvas, float f, float f2) {
        for (int i = 0; i < str.length(); i++) {
            O00000o o00000o = (O00000o) this.O00000oo.getCharacters().get(O00000o.O000000o(str.charAt(i), o00000o0.getFamily(), o00000o0.getStyle()));
            if (o00000o != null) {
                O000000o(o00000o, matrix2, f2, o00000Oo, canvas);
                float width = ((float) o00000o.getWidth()) * f2 * O0000OOo.O00o0O0O() * f;
                float f3 = ((float) o00000Oo.O00oO0O0) / 10.0f;
                O0000O0o o0000O0o = this.O00ooo;
                if (o0000O0o != null) {
                    f3 += ((Float) o0000O0o.getValue()).floatValue();
                }
                canvas.translate(width + (f3 * f), 0.0f);
            }
        }
    }

    private String O0000O0o(String str, int i) {
        int codePointAt = str.codePointAt(i);
        int charCount = Character.charCount(codePointAt) + i;
        while (charCount < str.length()) {
            int codePointAt2 = str.codePointAt(charCount);
            if (!O000OOoo(codePointAt2)) {
                break;
            }
            charCount += Character.charCount(codePointAt2);
            codePointAt = (codePointAt * 31) + codePointAt2;
        }
        long j = (long) codePointAt;
        if (this.O00ooOoo.containsKey(j)) {
            return (String) this.O00ooOoo.get(j);
        }
        this.stringBuilder.setLength(0);
        while (i < charCount) {
            int codePointAt3 = str.codePointAt(i);
            this.stringBuilder.appendCodePoint(codePointAt3);
            i += Character.charCount(codePointAt3);
        }
        String sb = this.stringBuilder.toString();
        this.O00ooOoo.put(j, sb);
        return sb;
    }

    private boolean O000OOoo(int i) {
        return Character.getType(i) == 16 || Character.getType(i) == 27 || Character.getType(i) == 6 || Character.getType(i) == 28 || Character.getType(i) == 19;
    }

    private List O00O0Oo(String str) {
        String str2 = "\r";
        return Arrays.asList(str.replaceAll("\r\n", str2).replaceAll("\n", str2).split(str2));
    }

    public void O000000o(RectF rectF, Matrix matrix2, boolean z) {
        super.O000000o(rectF, matrix2, z);
        rectF.set(0.0f, 0.0f, (float) this.O00000oo.getBounds().width(), (float) this.O00000oo.getBounds().height());
    }

    public void O000000o(Object obj, @Nullable O0000Oo o0000Oo) {
        O0000O0o o0000O0o;
        O0000O0o o0000O0o2;
        super.O000000o(obj, o0000Oo);
        if (obj == C0087O000Ooo0.COLOR) {
            o0000O0o2 = this.O00OOOo;
            if (o0000O0o2 == null) {
                if (o0000Oo == null) {
                    if (o0000O0o2 != null) {
                        O00000Oo(o0000O0o2);
                    }
                    this.O00OOOo = null;
                    return;
                }
                this.O00OOOo = new C0031O0000oo0(o0000Oo);
                this.O00OOOo.O00000Oo(this);
                o0000O0o = this.O00OOOo;
                O000000o(o0000O0o);
                return;
            }
        } else if (obj == C0087O000Ooo0.STROKE_COLOR) {
            o0000O0o2 = this.O00ooo0;
            if (o0000O0o2 == null) {
                if (o0000Oo == null) {
                    if (o0000O0o2 != null) {
                        O00000Oo(o0000O0o2);
                    }
                    this.O00ooo0 = null;
                    return;
                }
                this.O00ooo0 = new C0031O0000oo0(o0000Oo);
                this.O00ooo0.O00000Oo(this);
                o0000O0o = this.O00ooo0;
                O000000o(o0000O0o);
                return;
            }
        } else if (obj == C0087O000Ooo0.STROKE_WIDTH) {
            o0000O0o2 = this.O00ooo0o;
            if (o0000O0o2 == null) {
                if (o0000Oo == null) {
                    if (o0000O0o2 != null) {
                        O00000Oo(o0000O0o2);
                    }
                    this.O00ooo0o = null;
                    return;
                }
                this.O00ooo0o = new C0031O0000oo0(o0000Oo);
                this.O00ooo0o.O00000Oo(this);
                o0000O0o = this.O00ooo0o;
                O000000o(o0000O0o);
                return;
            }
        } else {
            if (obj == C0087O000Ooo0.OO00Oo) {
                o0000O0o2 = this.O00ooo;
                if (o0000O0o2 == null) {
                    if (o0000Oo == null) {
                        if (o0000O0o2 != null) {
                            O00000Oo(o0000O0o2);
                        }
                        this.O00ooo = null;
                        return;
                    }
                    this.O00ooo = new C0031O0000oo0(o0000Oo);
                    this.O00ooo.O00000Oo(this);
                    o0000O0o = this.O00ooo;
                }
            } else if (obj != C0087O000Ooo0.TEXT_SIZE) {
                return;
            } else {
                if (o0000Oo == null) {
                    O0000O0o o0000O0o3 = this.O00oooO0;
                    if (o0000O0o3 != null) {
                        O00000Oo(o0000O0o3);
                    }
                    this.O00oooO0 = null;
                    return;
                }
                this.O00oooO0 = new C0031O0000oo0(o0000Oo);
                this.O00oooO0.O00000Oo(this);
                o0000O0o = this.O00oooO0;
            }
            O000000o(o0000O0o);
            return;
        }
        o0000O0o2.O000000o(o0000Oo);
    }

    /* access modifiers changed from: 0000 */
    public void O00000Oo(Canvas canvas, Matrix matrix2, int i) {
        canvas.save();
        if (!this.O000OoO0.O000OO()) {
            canvas.setMatrix(matrix2);
        }
        com.airbnb.lottie.model.O00000Oo o00000Oo = (com.airbnb.lottie.model.O00000Oo) this.O00ooo00.getValue();
        O00000o0 o00000o0 = (O00000o0) this.O00000oo.getFonts().get(o00000Oo.O00o0ooo);
        if (o00000o0 == null) {
            canvas.restore();
            return;
        }
        O0000O0o o0000O0o = this.O00OOOo;
        if (o0000O0o != null) {
            this.O00ooOOo.setColor(((Integer) o0000O0o.getValue()).intValue());
        } else {
            this.O00ooOOo.setColor(o00000Oo.color);
        }
        O0000O0o o0000O0o2 = this.O00ooo0;
        if (o0000O0o2 != null) {
            this.O00ooOo.setColor(((Integer) o0000O0o2.getValue()).intValue());
        } else {
            this.O00ooOo.setColor(o00000Oo.strokeColor);
        }
        int intValue = ((this.O00Ooo0O.getOpacity() == null ? 100 : ((Integer) this.O00Ooo0O.getOpacity().getValue()).intValue()) * 255) / 100;
        this.O00ooOOo.setAlpha(intValue);
        this.O00ooOo.setAlpha(intValue);
        O0000O0o o0000O0o3 = this.O00ooo0o;
        if (o0000O0o3 != null) {
            this.O00ooOo.setStrokeWidth(((Float) o0000O0o3.getValue()).floatValue());
        } else {
            this.O00ooOo.setStrokeWidth(o00000Oo.strokeWidth * O0000OOo.O00o0O0O() * O0000OOo.O000000o(matrix2));
        }
        if (this.O000OoO0.O000OO()) {
            O000000o(o00000Oo, matrix2, o00000o0, canvas);
        } else {
            O000000o(o00000Oo, o00000o0, matrix2, canvas);
        }
        canvas.restore();
    }
}
