package O00000Oo.O000000o.O00000Oo;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.FontMetrics;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.text.TextPaint;
import java.io.PrintStream;
import java.util.regex.Pattern;

public class O000000o {
    private static int O000000o(Canvas canvas, String str, float f, float f2, Paint paint, int i) {
        int i2;
        String valueOf;
        Object[] objArr;
        PrintStream printStream;
        int i3;
        String str2;
        Rect rect = new Rect();
        System.out.printf("the text space is: %d\n", new Object[]{Integer.valueOf(i)});
        System.out.printf("the text length is: %d\n", new Object[]{Integer.valueOf(str.length())});
        float f3 = f;
        int i4 = 0;
        int i5 = 0;
        while (i4 < str.length()) {
            char charAt = str.charAt(i4);
            String valueOf2 = String.valueOf(charAt);
            Math.round(paint.measureText(String.valueOf(charAt)));
            if (charAt > 55296) {
                i4++;
                StringBuilder sb = new StringBuilder();
                sb.append(String.valueOf(charAt));
                sb.append(str.charAt(i4));
                valueOf = sb.toString();
                if (i4 != 1) {
                    paint.getTextBounds(str, i4, i4 + 1, rect);
                    rect.width();
                }
                int width = rect.width() + (i / 2);
                f3 += (float) width;
                i5 += width;
                if (canvas == null) {
                    i4++;
                }
            } else {
                if (charAt == ' ') {
                    i2 = Math.round(paint.measureText(String.valueOf(str.charAt(i4)))) + i;
                } else {
                    paint.getTextBounds(str, i4, i4 + 1, rect);
                    if (charAt >= 128) {
                        i3 = rect.width() + (i / 2);
                        printStream = System.out;
                        objArr = new Object[]{Character.valueOf(charAt)};
                        str2 = "%s is not NumOrLetters\n";
                    } else if (O000O0o0(valueOf2)) {
                        i3 = rect.width() + i;
                        printStream = System.out;
                        objArr = new Object[]{Character.valueOf(charAt)};
                        str2 = "%s is NumOrLetters\n";
                    } else {
                        i2 = rect.width() + (i * 2);
                        System.out.printf("%s is: %d, space is: %d, text is %d\n", new Object[]{Character.valueOf(str.charAt(i4)), Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(rect.width())});
                    }
                    printStream.printf(str2, objArr);
                    i2 = i3;
                    System.out.printf("%s is: %d, space is: %d, text is %d\n", new Object[]{Character.valueOf(str.charAt(i4)), Integer.valueOf(i2), Integer.valueOf(i), Integer.valueOf(rect.width())});
                }
                f3 += (float) i2;
                i5 += i2;
                if (canvas != null) {
                    valueOf = String.valueOf(str.charAt(i4));
                } else {
                    i4++;
                }
            }
            canvas.drawText(valueOf, f3, f2, paint);
            i4++;
        }
        return i5;
    }

    private static Bitmap O000000o(byte[] bArr, String str, int i, int i2, int i3, float f, int i4, float f2, int i5, float f3, float f4, float f5, int i6, int i7, int i8, int i9) {
        float width;
        float f6;
        int i10 = i;
        int i11 = i3;
        int i12 = i7;
        int i13 = i8;
        int i14 = i9;
        String str2 = new String(bArr);
        Rect rect = new Rect(0, 0, i13, i14);
        Bitmap createBitmap = Bitmap.createBitmap(i13, i14, Config.ARGB_8888);
        Paint paint = new Paint();
        paint.setColor(0);
        paint.setStyle(Style.FILL);
        Canvas canvas = new Canvas(createBitmap);
        TextPaint textPaint = new TextPaint();
        textPaint.setColor(i2);
        float f7 = (float) i10;
        textPaint.setTextSize(f7);
        textPaint.setAntiAlias(true);
        textPaint.setStyle(Style.FILL);
        Align align = i12 == 0 ? Align.LEFT : i12 == 1 ? Align.CENTER : Align.RIGHT;
        textPaint.setTextAlign(align);
        if ((i4 & 16) == 16) {
            textPaint.setUnderlineText(true);
        }
        if ((i4 & 32) == 32) {
            textPaint.setStrikeThruText(true);
        }
        if ((i4 & 4) == 4) {
            textPaint.setTextSkewX((-f) / 90.0f);
        }
        if ((i4 & 8) == 8) {
            textPaint.setFakeBoldText(true);
        }
        if ((i4 & 2) == 2) {
            textPaint.setShadowLayer(f3, f4, f5, i6);
        }
        FontMetrics fontMetrics = textPaint.getFontMetrics();
        int centerY = (int) ((((float) rect.centerY()) - (fontMetrics.top / 2.0f)) - (fontMetrics.bottom / 2.0f));
        if ((i4 & 1) == 1) {
            TextPaint textPaint2 = new TextPaint();
            textPaint2.setColor(i5);
            textPaint2.setTextSize(textPaint.getTextSize());
            textPaint2.setAntiAlias(textPaint.isAntiAlias());
            textPaint2.setStyle(Style.STROKE);
            textPaint2.setStrokeWidth((5.0f * f2) / f7);
            textPaint2.setTextAlign(textPaint.getTextAlign());
            textPaint2.setTextSkewX(textPaint.getTextSkewX());
            textPaint.setFakeBoldText(false);
            textPaint2.setFakeBoldText(true);
            float f8 = ((float) i11) / f7;
            if (VERSION.SDK_INT >= 21) {
                textPaint2.setLetterSpacing(f8);
                int i15 = i12 == 0 ? rect.left : i12 == 1 ? rect.centerX() : rect.right;
                canvas.drawText(str2, (float) i15, (float) centerY, textPaint2);
            } else {
                Rect rect2 = new Rect();
                textPaint.getTextBounds(str2, 0, str2.length(), rect2);
                int i16 = 0;
                int i17 = 0;
                while (i16 < str2.length()) {
                    if (str2.charAt(i16) > 55296) {
                        i16++;
                        i17++;
                    }
                    i16++;
                }
                if (i12 == 0) {
                    f6 = 0.0f;
                } else {
                    int width2 = ((rect.width() - rect2.width()) + (i17 * i10)) - ((str2.length() - 1) * i11);
                    if (i12 == 1) {
                        width2 /= 2;
                    }
                    f6 = (float) width2;
                }
                O000000o(canvas, str2, f6, (float) centerY, textPaint2, i3);
            }
        }
        float f9 = ((float) i11) / f7;
        if (VERSION.SDK_INT >= 21) {
            textPaint.setLetterSpacing(f9);
            int i18 = i12 == 0 ? rect.left : i12 == 1 ? rect.centerX() : rect.right;
            canvas.drawText(str2, (float) i18, (float) centerY, textPaint);
        } else {
            Rect rect3 = new Rect();
            int i19 = 0;
            textPaint.getTextBounds(str2, 0, str2.length(), rect3);
            int i20 = 0;
            while (i19 < str2.length()) {
                if (str2.charAt(i19) > 55296) {
                    i19++;
                    i20++;
                }
                i19++;
            }
            if (i12 == 0) {
                width = 0.0f;
            } else {
                width = (float) (i12 == 1 ? (((rect.width() - rect3.width()) + (i20 * i10)) - ((str2.length() - 1) * i11)) / 2 : ((rect.width() - rect3.width()) + (i20 * i10)) - ((str2.length() - 1) * i11));
            }
            O000000o(canvas, str2, width, (float) centerY, textPaint, i3);
        }
        canvas.save(31);
        canvas.restore();
        return createBitmap;
    }

    public static boolean O000O0o0(String str) {
        return Pattern.compile("^[A-Za-z0-9_]+$").matcher(str).matches();
    }
}
