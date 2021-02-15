package com.airbnb.lottie.O00000o;

import android.content.ContentResolver;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.PathMeasure;
import android.graphics.PointF;
import android.graphics.RectF;
import android.os.Build.VERSION;
import android.provider.Settings.Global;
import android.provider.Settings.System;
import androidx.annotation.Nullable;
import com.airbnb.lottie.C0053O00000oO;
import com.airbnb.lottie.O000000o.O000000o;
import com.airbnb.lottie.O000000o.O000000o.C0018O0000ooO;
import com.airbnb.lottie.O000000o.O00000Oo.O0000Oo0;
import java.io.Closeable;
import java.io.InterruptedIOException;
import java.net.ProtocolException;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.net.UnknownServiceException;
import java.nio.channels.ClosedChannelException;
import javax.net.ssl.SSLException;

public final class O0000OOo {
    private static final PathMeasure O00o00oO = new PathMeasure();
    private static final Path O00o0O00 = new Path();
    private static float O0OOO0 = -1.0f;
    public static final int O0OOO00 = 1000000000;
    private static final float O0OOoO = ((float) (Math.sqrt(2.0d) / 2.0d));
    private static final Path O0OOoo = new Path();
    private static final float[] points = new float[4];

    private O0000OOo() {
    }

    public static float O000000o(Matrix matrix) {
        float[] fArr = points;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        float f = O0OOoO;
        fArr[2] = f;
        fArr[3] = f;
        matrix.mapPoints(fArr);
        float[] fArr2 = points;
        return (float) Math.hypot((double) (fArr2[2] - fArr2[0]), (double) (fArr2[3] - fArr2[1]));
    }

    public static int O000000o(float f, float f2, float f3, float f4) {
        int i = f != 0.0f ? (int) (((float) 527) * f) : 17;
        if (f2 != 0.0f) {
            i = (int) (((float) (i * 31)) * f2);
        }
        if (f3 != 0.0f) {
            i = (int) (((float) (i * 31)) * f3);
        }
        return f4 != 0.0f ? (int) (((float) (i * 31)) * f4) : i;
    }

    public static Bitmap O000000o(Bitmap bitmap, int i, int i2) {
        if (bitmap.getWidth() == i && bitmap.getHeight() == i2) {
            return bitmap;
        }
        Bitmap createScaledBitmap = Bitmap.createScaledBitmap(bitmap, i, i2, true);
        bitmap.recycle();
        return createScaledBitmap;
    }

    public static Path O000000o(PointF pointF, PointF pointF2, PointF pointF3, PointF pointF4) {
        Path path = new Path();
        path.moveTo(pointF.x, pointF.y);
        if (pointF3 == null || pointF4 == null || (pointF3.length() == 0.0f && pointF4.length() == 0.0f)) {
            path.lineTo(pointF2.x, pointF2.y);
        } else {
            float f = pointF3.x + pointF.x;
            float f2 = pointF.y + pointF3.y;
            float f3 = pointF2.x;
            float f4 = f3 + pointF4.x;
            float f5 = pointF2.y;
            path.cubicTo(f, f2, f4, f5 + pointF4.y, f3, f5);
        }
        return path;
    }

    public static void O000000o(Canvas canvas, RectF rectF, Paint paint) {
        O000000o(canvas, rectF, paint, 31);
    }

    public static void O000000o(Canvas canvas, RectF rectF, Paint paint, int i) {
        String str = "Utils#saveLayer";
        C0053O00000oO.beginSection(str);
        if (VERSION.SDK_INT < 23) {
            canvas.saveLayer(rectF, paint, i);
        } else {
            canvas.saveLayer(rectF, paint);
        }
        C0053O00000oO.O0000oOo(str);
    }

    public static void O000000o(Path path, float f, float f2, float f3) {
        String str = "applyTrimPathIfNeeded";
        C0053O00000oO.beginSection(str);
        O00o00oO.setPath(path, false);
        float length = O00o00oO.getLength();
        if (f == 1.0f && f2 == 0.0f) {
            C0053O00000oO.O0000oOo(str);
        } else if (length < 1.0f || ((double) Math.abs((f2 - f) - 1.0f)) < 0.01d) {
            C0053O00000oO.O0000oOo(str);
        } else {
            float f4 = f * length;
            float f5 = f2 * length;
            float f6 = f3 * length;
            float min = Math.min(f4, f5) + f6;
            float max = Math.max(f4, f5) + f6;
            if (min >= length && max >= length) {
                min = (float) O0000O0o.O00000oo(min, length);
                max = (float) O0000O0o.O00000oo(max, length);
            }
            if (min < 0.0f) {
                min = (float) O0000O0o.O00000oo(min, length);
            }
            if (max < 0.0f) {
                max = (float) O0000O0o.O00000oo(max, length);
            }
            int i = (min > max ? 1 : (min == max ? 0 : -1));
            if (i == 0) {
                path.reset();
            } else {
                if (i >= 0) {
                    min -= length;
                }
                O00o0O00.reset();
                O00o00oO.getSegment(min, max, O00o0O00, true);
                if (max > length) {
                    O0OOoo.reset();
                    O00o00oO.getSegment(0.0f, max % length, O0OOoo, true);
                } else {
                    if (min < 0.0f) {
                        O0OOoo.reset();
                        O00o00oO.getSegment(min + length, length, O0OOoo, true);
                    }
                    path.set(O00o0O00);
                }
                O00o0O00.addPath(O0OOoo);
                path.set(O00o0O00);
            }
            C0053O00000oO.O0000oOo(str);
        }
    }

    public static void O000000o(Path path, @Nullable C0018O0000ooO o0000ooO) {
        if (o0000ooO != null && !o0000ooO.isHidden()) {
            O000000o(path, ((O0000Oo0) o0000ooO.getStart()).getFloatValue() / 100.0f, ((O0000Oo0) o0000ooO.getEnd()).getFloatValue() / 100.0f, ((O0000Oo0) o0000ooO.getOffset()).getFloatValue() / 360.0f);
        }
    }

    public static boolean O000000o(int i, int i2, int i3, int i4, int i5, int i6) {
        boolean z = false;
        if (i < i4) {
            return false;
        }
        if (i > i4) {
            return true;
        }
        if (i2 < i5) {
            return false;
        }
        if (i2 > i5) {
            return true;
        }
        if (i3 >= i6) {
            z = true;
        }
        return z;
    }

    public static float O00000Oo(Context context) {
        int i = VERSION.SDK_INT;
        String str = "animator_duration_scale";
        ContentResolver contentResolver = context.getContentResolver();
        return i >= 17 ? Global.getFloat(contentResolver, str, 1.0f) : System.getFloat(contentResolver, str, 1.0f);
    }

    public static Bitmap O00000Oo(Path path) {
        RectF rectF = new RectF();
        path.computeBounds(rectF, false);
        Bitmap createBitmap = Bitmap.createBitmap((int) rectF.right, (int) rectF.bottom, Config.ARGB_8888);
        Canvas canvas = new Canvas(createBitmap);
        O000000o o000000o = new O000000o();
        o000000o.setAntiAlias(true);
        o000000o.setColor(-16776961);
        canvas.drawPath(path, o000000o);
        return createBitmap;
    }

    public static boolean O00000Oo(Matrix matrix) {
        float[] fArr = points;
        fArr[0] = 0.0f;
        fArr[1] = 0.0f;
        fArr[2] = 37394.73f;
        fArr[3] = 39575.234f;
        matrix.mapPoints(fArr);
        float[] fArr2 = points;
        return fArr2[0] == fArr2[2] || fArr2[1] == fArr2[3];
    }

    public static float O00o0O0O() {
        if (O0OOO0 == -1.0f) {
            O0OOO0 = Resources.getSystem().getDisplayMetrics().density;
        }
        return O0OOO0;
    }

    public static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (RuntimeException e) {
                throw e;
            } catch (Exception unused) {
            }
        }
    }

    public static boolean isNetworkException(Throwable th) {
        return (th instanceof SocketException) || (th instanceof ClosedChannelException) || (th instanceof InterruptedIOException) || (th instanceof ProtocolException) || (th instanceof SSLException) || (th instanceof UnknownHostException) || (th instanceof UnknownServiceException);
    }
}
