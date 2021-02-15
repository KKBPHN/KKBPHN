package com.android.camera.effect.renders;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Paint.FontMetricsInt;
import android.graphics.Paint.Style;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.text.TextPaint;
import android.text.TextUtils;
import com.android.camera.Util;
import com.android.camera.log.Log;
import com.xiaomi.stat.d;

public class CustomTextWaterMark {
    private static final int BACK_TEXT_START_MARGIN = 64;
    private static final int BACK_WATER_MARK_HEIGHT = 195;
    private static final String DEVICE_PREFIX_GEN2 = "SHOT ON ";
    public static final String FONT_MIPRO_MEDIUM = "mipro-medium";
    public static final String FONT_NAME = "mipro-regular";
    public static final String FONT_SANS_SERIF_LIGHT = "sans-serif-light";
    private static final int FRONT_TEXT_START_MARGIN = 50;
    private static final int FRONT_WATER_MARK_HEIGHT = 120;
    private static final int NO_ICON_DESCRIPTION_TEXT_SIZE = 68;
    private static final int NO_ICON_TITLE_TEXT_SIZE = 118;
    private static final int NO_ICON_VERTICAL_MARGIN = 5;
    private static final String TAG = "CustomTextWaterMark";
    public static final int TEXT_COLOR = -1;
    private static final int TEXT_SIZE = 70;
    private static final int TEXT_SIZE_GEN2 = 72;
    private static final int TEXT_TOP_MARGIN_ROW1_GEN2 = 36;
    private static final int TEXT_TOP_MARGIN_ROW2_GEN2 = 18;
    private static final int VERTICAL_MARGIN = 23;
    private static final int WATER_MARK_CINEMATIC_MAX_LENGTH = 1100;
    private static final int WATER_MARK_MAX_LENGTH = 1400;
    private static final float WATER_MARK_SHADOW_Y = 2.0f;
    private static final int WATER_MARK_SHADOW_Y_COLOR = 771751936;

    public static Bitmap drawBackWaterMark(Bitmap bitmap, String str, String str2, boolean z) {
        return C0122O00000o.instance().OO0oo() ? drawBackWaterMarkGen2(bitmap, str, str2, z) : bitmap == null ? drawNoIcon(str, str2) : drawWithIcon(bitmap, str, str2);
    }

    /* JADX WARNING: Removed duplicated region for block: B:13:0x009c  */
    /* JADX WARNING: Removed duplicated region for block: B:14:0x009f  */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x00e9 A[ADDED_TO_REGION] */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x01c0  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static Bitmap drawBackWaterMarkGen2(Bitmap bitmap, String str, String str2, boolean z) {
        boolean z2;
        TextPaint textPaintGen2;
        long j;
        int i;
        FontMetricsInt fontMetricsInt;
        int i2;
        Bitmap bitmap2;
        StringBuilder sb;
        Bitmap bitmap3 = bitmap;
        String str3 = str;
        String str4 = str2;
        long currentTimeMillis = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        StringBuilder sb2 = new StringBuilder();
        sb2.append("drawBackWaterMarkGen2: icon size = ");
        sb2.append(width);
        String str5 = "x";
        sb2.append(str5);
        sb2.append(height);
        String sb3 = sb2.toString();
        String str6 = TAG;
        Log.i(str6, sb3);
        int length = str.length();
        String str7 = DEVICE_PREFIX_GEN2;
        if (length <= 6) {
            sb = new StringBuilder();
        } else {
            if (str.length() < 12) {
                if (TextUtils.isEmpty(str2)) {
                    sb = new StringBuilder();
                } else {
                    z2 = true;
                    textPaintGen2 = getTextPaintGen2(72.0f);
                    FontMetricsInt fontMetricsInt2 = textPaintGen2.getFontMetricsInt();
                    int ceil = (int) Math.ceil((double) textPaintGen2.measureText(str3));
                    int i3 = fontMetricsInt2.descent - fontMetricsInt2.ascent;
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("drawBackWaterMarkGen2: deviceNameSize = ");
                    sb4.append(ceil);
                    sb4.append(str5);
                    sb4.append(i3);
                    Log.i(str6, sb4.toString());
                    String str8 = " | ";
                    if (TextUtils.isEmpty(str2)) {
                        j = currentTimeMillis;
                        i = 0;
                    } else {
                        StringBuilder sb5 = new StringBuilder();
                        sb5.append(str8);
                        sb5.append(str4);
                        j = currentTimeMillis;
                        i = (int) Math.ceil((double) textPaintGen2.measureText(sb5.toString()));
                    }
                    fontMetricsInt = textPaintGen2.getFontMetricsInt();
                    int i4 = i + ceil;
                    long j2 = j;
                    i2 = fontMetricsInt.descent - fontMetricsInt.ascent;
                    StringBuilder sb6 = new StringBuilder();
                    String str9 = str8;
                    String str10 = "drawBackWaterMarkGen2: size = ";
                    sb6.append(str10);
                    sb6.append(i4);
                    sb6.append(str5);
                    sb6.append(i2);
                    Log.i(str6, sb6.toString());
                    String str11 = str10;
                    if (!TextUtils.isEmpty(str2) || ((z && i4 <= WATER_MARK_CINEMATIC_MAX_LENGTH) || (!z && i4 <= WATER_MARK_MAX_LENGTH))) {
                        int max = Math.max(width, i4);
                        int i5 = height + 36;
                        int i6 = i2 + i5;
                        bitmap2 = Bitmap.createBitmap(max, i6, Config.ARGB_8888);
                        bitmap2.setPremultiplied(true);
                        Canvas canvas = new Canvas(bitmap2);
                        canvas.drawBitmap(bitmap3, 0.0f, 0.0f, null);
                        bitmap.recycle();
                        float f = (float) (i5 - fontMetricsInt.ascent);
                        canvas.drawText(str3, 0.0f, f, textPaintGen2);
                        if (!TextUtils.isEmpty(str2)) {
                            float f2 = (float) ceil;
                            StringBuilder sb7 = new StringBuilder();
                            sb7.append(str9);
                            sb7.append(str4);
                            canvas.drawText(sb7.toString(), f2, f, textPaintGen2);
                        }
                        StringBuilder sb8 = new StringBuilder();
                        sb8.append(str11);
                        sb8.append(max);
                        sb8.append(str5);
                        sb8.append(i6);
                        Log.d(str6, sb8.toString());
                    } else {
                        StringBuilder sb9 = new StringBuilder();
                        sb9.append("drawBackWaterMarkGen2: needCheck = ");
                        sb9.append(z2);
                        Log.d(str6, sb9.toString());
                        if (z2 && !str3.startsWith(str7)) {
                            StringBuilder sb10 = new StringBuilder();
                            sb10.append(str7);
                            sb10.append(str3);
                            str3 = sb10.toString();
                        }
                        int ceil2 = (int) Math.ceil((double) textPaintGen2.measureText(str3));
                        int ceil3 = (int) Math.ceil((double) textPaintGen2.measureText(str4));
                        int max2 = Math.max(ceil2, ceil3);
                        int i7 = height + 36;
                        int i8 = i7 + i2 + 18;
                        int i9 = i2 + i8;
                        Bitmap createBitmap = Bitmap.createBitmap(max2, i9, Config.ARGB_8888);
                        createBitmap.setPremultiplied(true);
                        Canvas canvas2 = new Canvas(createBitmap);
                        Bitmap bitmap4 = createBitmap;
                        canvas2.drawBitmap(bitmap3, 0.0f, 0.0f, null);
                        bitmap.recycle();
                        canvas2.drawText(str3, 0.0f, (float) (i7 - fontMetricsInt.ascent), textPaintGen2);
                        canvas2.drawText(str4, 0.0f, (float) (i8 - fontMetricsInt.ascent), textPaintGen2);
                        StringBuilder sb11 = new StringBuilder();
                        sb11.append("drawBackWaterMarkGen2: firstLineWidth = ");
                        sb11.append(ceil2);
                        sb11.append(", secondLineWidth = ");
                        sb11.append(ceil3);
                        sb11.append(", size = ");
                        sb11.append(max2);
                        sb11.append(str5);
                        sb11.append(i9);
                        Log.d(str6, sb11.toString());
                        bitmap2 = bitmap4;
                    }
                    StringBuilder sb12 = new StringBuilder();
                    sb12.append("drawBackWaterMarkGen2: cost time = ");
                    sb12.append(System.currentTimeMillis() - j2);
                    sb12.append(d.H);
                    Log.v(str6, sb12.toString());
                    return bitmap2;
                }
            }
            z2 = false;
            textPaintGen2 = getTextPaintGen2(72.0f);
            FontMetricsInt fontMetricsInt22 = textPaintGen2.getFontMetricsInt();
            int ceil4 = (int) Math.ceil((double) textPaintGen2.measureText(str3));
            int i32 = fontMetricsInt22.descent - fontMetricsInt22.ascent;
            StringBuilder sb42 = new StringBuilder();
            sb42.append("drawBackWaterMarkGen2: deviceNameSize = ");
            sb42.append(ceil4);
            sb42.append(str5);
            sb42.append(i32);
            Log.i(str6, sb42.toString());
            String str82 = " | ";
            if (TextUtils.isEmpty(str2)) {
            }
            fontMetricsInt = textPaintGen2.getFontMetricsInt();
            int i42 = i + ceil4;
            long j22 = j;
            i2 = fontMetricsInt.descent - fontMetricsInt.ascent;
            StringBuilder sb62 = new StringBuilder();
            String str92 = str82;
            String str102 = "drawBackWaterMarkGen2: size = ";
            sb62.append(str102);
            sb62.append(i42);
            sb62.append(str5);
            sb62.append(i2);
            Log.i(str6, sb62.toString());
            String str112 = str102;
            if (!TextUtils.isEmpty(str2)) {
            }
            int max3 = Math.max(width, i42);
            int i52 = height + 36;
            int i62 = i2 + i52;
            bitmap2 = Bitmap.createBitmap(max3, i62, Config.ARGB_8888);
            bitmap2.setPremultiplied(true);
            Canvas canvas3 = new Canvas(bitmap2);
            canvas3.drawBitmap(bitmap3, 0.0f, 0.0f, null);
            bitmap.recycle();
            float f3 = (float) (i52 - fontMetricsInt.ascent);
            canvas3.drawText(str3, 0.0f, f3, textPaintGen2);
            if (!TextUtils.isEmpty(str2)) {
            }
            StringBuilder sb82 = new StringBuilder();
            sb82.append(str112);
            sb82.append(max3);
            sb82.append(str5);
            sb82.append(i62);
            Log.d(str6, sb82.toString());
            StringBuilder sb122 = new StringBuilder();
            sb122.append("drawBackWaterMarkGen2: cost time = ");
            sb122.append(System.currentTimeMillis() - j22);
            sb122.append(d.H);
            Log.v(str6, sb122.toString());
            return bitmap2;
        }
        sb.append(str7);
        sb.append(str3);
        str3 = sb.toString();
        z2 = false;
        textPaintGen2 = getTextPaintGen2(72.0f);
        FontMetricsInt fontMetricsInt222 = textPaintGen2.getFontMetricsInt();
        int ceil42 = (int) Math.ceil((double) textPaintGen2.measureText(str3));
        int i322 = fontMetricsInt222.descent - fontMetricsInt222.ascent;
        StringBuilder sb422 = new StringBuilder();
        sb422.append("drawBackWaterMarkGen2: deviceNameSize = ");
        sb422.append(ceil42);
        sb422.append(str5);
        sb422.append(i322);
        Log.i(str6, sb422.toString());
        String str822 = " | ";
        if (TextUtils.isEmpty(str2)) {
        }
        fontMetricsInt = textPaintGen2.getFontMetricsInt();
        int i422 = i + ceil42;
        long j222 = j;
        i2 = fontMetricsInt.descent - fontMetricsInt.ascent;
        StringBuilder sb622 = new StringBuilder();
        String str922 = str822;
        String str1022 = "drawBackWaterMarkGen2: size = ";
        sb622.append(str1022);
        sb622.append(i422);
        sb622.append(str5);
        sb622.append(i2);
        Log.i(str6, sb622.toString());
        String str1122 = str1022;
        if (!TextUtils.isEmpty(str2)) {
        }
        int max32 = Math.max(width, i422);
        int i522 = height + 36;
        int i622 = i2 + i522;
        bitmap2 = Bitmap.createBitmap(max32, i622, Config.ARGB_8888);
        bitmap2.setPremultiplied(true);
        Canvas canvas32 = new Canvas(bitmap2);
        canvas32.drawBitmap(bitmap3, 0.0f, 0.0f, null);
        bitmap.recycle();
        float f32 = (float) (i522 - fontMetricsInt.ascent);
        canvas32.drawText(str3, 0.0f, f32, textPaintGen2);
        if (!TextUtils.isEmpty(str2)) {
        }
        StringBuilder sb822 = new StringBuilder();
        sb822.append(str1122);
        sb822.append(max32);
        sb822.append(str5);
        sb822.append(i622);
        Log.d(str6, sb822.toString());
        StringBuilder sb1222 = new StringBuilder();
        sb1222.append("drawBackWaterMarkGen2: cost time = ");
        sb1222.append(System.currentTimeMillis() - j222);
        sb1222.append(d.H);
        Log.v(str6, sb1222.toString());
        return bitmap2;
    }

    public static Bitmap drawFrontWaterMark(Bitmap bitmap, String str) {
        long currentTimeMillis = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("drawFrontWaterMark onDraw: icon = ");
        sb.append(width);
        sb.append("x");
        sb.append(height);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.i(str2, sb2);
        TextPaint textPaint = getTextPaint();
        textPaint.setLetterSpacing(0.03f);
        Rect rect = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), rect);
        int i = width + 50;
        Bitmap createBitmap = Bitmap.createBitmap(rect.width() + i, 120, Config.ARGB_8888);
        createBitmap.setPremultiplied(true);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, ((float) (120 - height)) / 2.0f, null);
        bitmap.recycle();
        float f = (float) (i - rect.left);
        float height2 = (((float) (120 - rect.height())) / 2.0f) - ((float) rect.top);
        canvas.drawText(str, f, height2, textPaint);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("drawFrontWaterMark: productBounds = ");
        sb3.append(rect);
        sb3.append(", height = ");
        sb3.append(rect.height());
        sb3.append(", y = ");
        sb3.append(height2);
        Log.d(str2, sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("drawFrontWaterMark: Custom watermark cost time = ");
        sb4.append(System.currentTimeMillis() - currentTimeMillis);
        sb4.append(d.H);
        Log.v(str2, sb4.toString());
        return createBitmap;
    }

    private static Bitmap drawNoIcon(String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        TextPaint textPaint = getTextPaint();
        Rect rect = new Rect();
        String str3 = "mipro-regular";
        textPaint.setTypeface(Typeface.create(str3, 1));
        textPaint.setTextSize(118.0f);
        textPaint.getTextBounds(str, 0, str.length(), rect);
        TextPaint textPaint2 = getTextPaint();
        Rect rect2 = new Rect();
        textPaint2.setTypeface(Typeface.create(str3, 0));
        textPaint2.setTextSize(68.0f);
        textPaint2.getTextBounds(str2, 0, str2.length(), rect2);
        Bitmap createBitmap = Bitmap.createBitmap(Math.max(rect.width(), rect2.width()), 195, Config.ARGB_8888);
        createBitmap.setPremultiplied(true);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawText(str, (float) (-rect.left), (float) (rect.height() + 5), textPaint);
        StringBuilder sb = new StringBuilder();
        sb.append("drawNoIcon: productBounds = ");
        sb.append(rect);
        String sb2 = sb.toString();
        String str4 = TAG;
        Log.d(str4, sb2);
        canvas.drawText(str2, (float) (-rect2.left), (float) ((createBitmap.getHeight() - 5) - rect.bottom), textPaint2);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("drawNoIcon: customBounds = ");
        sb3.append(rect2);
        Log.d(str4, sb3.toString());
        StringBuilder sb4 = new StringBuilder();
        sb4.append("drawNoIcon: Custom watermark cost time = ");
        sb4.append(System.currentTimeMillis() - currentTimeMillis);
        sb4.append(d.H);
        Log.v(str4, sb4.toString());
        return createBitmap;
    }

    private static Bitmap drawWithIcon(Bitmap bitmap, String str, String str2) {
        long currentTimeMillis = System.currentTimeMillis();
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        StringBuilder sb = new StringBuilder();
        sb.append("drawWithIcon: icon = ");
        sb.append(width);
        sb.append("x");
        sb.append(height);
        String sb2 = sb.toString();
        String str3 = TAG;
        Log.i(str3, sb2);
        TextPaint textPaint = getTextPaint();
        Rect rect = new Rect();
        textPaint.getTextBounds(str, 0, str.length(), rect);
        Rect rect2 = new Rect();
        textPaint.getTextBounds(str2, 0, str2.length(), rect2);
        int i = width + 64;
        Bitmap createBitmap = Bitmap.createBitmap(Math.max(rect.width(), rect2.width()) + i, 195, Config.ARGB_8888);
        createBitmap.setPremultiplied(true);
        Canvas canvas = new Canvas(createBitmap);
        canvas.drawBitmap(bitmap, 0.0f, 0.0f, null);
        bitmap.recycle();
        canvas.drawText(str, (float) (i - rect.left), (float) (rect.height() + 23), textPaint);
        StringBuilder sb3 = new StringBuilder();
        sb3.append("drawWithIcon: productBounds = ");
        sb3.append(rect);
        Log.d(str3, sb3.toString());
        canvas.drawText(str2, (float) ((i - rect2.left) - 2), (float) ((createBitmap.getHeight() - 23) - rect.bottom), textPaint);
        StringBuilder sb4 = new StringBuilder();
        sb4.append("drawWithIcon: customBounds = ");
        sb4.append(rect2);
        Log.d(str3, sb4.toString());
        StringBuilder sb5 = new StringBuilder();
        sb5.append("drawWithIcon: Custom watermark cost time = ");
        sb5.append(System.currentTimeMillis() - currentTimeMillis);
        sb5.append(d.H);
        Log.v(str3, sb5.toString());
        return createBitmap;
    }

    private static TextPaint getTextPaint() {
        TextPaint textPaint = new TextPaint(1);
        textPaint.setTextSize(70.0f);
        textPaint.setAntiAlias(true);
        textPaint.setColor(-1);
        textPaint.setTypeface(Typeface.create("mipro-regular", 0));
        textPaint.setShadowLayer(0.1f, 0.0f, 2.0f, 771751936);
        textPaint.setStrokeWidth(1.0f);
        textPaint.setStyle(Style.FILL_AND_STROKE);
        textPaint.setLetterSpacing(0.06f);
        textPaint.setTextScaleX(0.95f);
        return textPaint;
    }

    public static TextPaint getTextPaintGen2(float f) {
        TextPaint textPaint = new TextPaint(1);
        textPaint.setTextSize(f);
        textPaint.setColor(-1);
        textPaint.setTypeface(Util.isGlobalVersion() ? Typeface.create(FONT_SANS_SERIF_LIGHT, 1) : Typeface.create("mipro-medium", 0));
        textPaint.setShadowLayer(1.0f, 0.0f, 0.0f, Integer.MIN_VALUE);
        textPaint.setStyle(Style.FILL);
        textPaint.setLetterSpacing(0.06f);
        return textPaint;
    }
}
