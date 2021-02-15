package com.android.camera2.autozoom;

import android.content.Context;
import android.graphics.RectF;
import android.util.Size;
import android.view.WindowManager;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class AutoZoomUtils {
    private AutoZoomUtils() {
    }

    public static int dp2px(Context context, float f) {
        return (int) ((f * context.getResources().getDisplayMetrics().density) + 0.5f);
    }

    public static void fromVidhanceCoordinateSystem(RectF rectF) {
        rectF.offsetTo(rectF.left + 0.5f, rectF.top + 0.5f);
    }

    public static int getDisplayRotation(Context context) {
        int rotation = ((WindowManager) context.getSystemService("window")).getDefaultDisplay().getRotation();
        if (rotation == 0) {
            return 0;
        }
        if (rotation == 1) {
            return 90;
        }
        if (rotation != 2) {
            return rotation != 3 ? 0 : 270;
        }
        return 180;
    }

    public static long mean(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        long j = 0;
        while (it.hasNext()) {
            j += ((Long) it.next()).longValue();
        }
        return j / ((long) arrayList.size());
    }

    public static float median(ArrayList arrayList) {
        if (arrayList.isEmpty()) {
            return 0.0f;
        }
        Collections.sort(arrayList);
        return arrayList.size() % 2 == 0 ? ((Float) arrayList.get(arrayList.size() / 2)).floatValue() + (((Float) arrayList.get((arrayList.size() / 2) - 1)).floatValue() / 2.0f) : ((Float) arrayList.get(arrayList.size() / 2)).floatValue();
    }

    public static void normalizedRectToSize(RectF rectF, Size size) {
        rectF.set(rectF.left * ((float) size.getWidth()), rectF.top * ((float) size.getHeight()), rectF.right * ((float) size.getWidth()), rectF.bottom * ((float) size.getHeight()));
    }

    public static void rotateFromVidhance(Context context, RectF rectF) {
        int displayRotation = getDisplayRotation(context);
        if (displayRotation != 0) {
            if (displayRotation == 90) {
                return;
            }
            if (displayRotation != 180) {
                if (displayRotation == 270) {
                    rectF.set(1.0f - rectF.right, 1.0f - rectF.bottom, 1.0f - rectF.left, 1.0f - rectF.top);
                    return;
                }
                return;
            }
        }
        rectF.set(1.0f - rectF.bottom, rectF.left, 1.0f - rectF.top, rectF.right);
    }

    public static void rotateToVidhance(Context context, RectF rectF) {
        float f;
        float f2;
        float f3;
        float f4;
        int displayRotation = getDisplayRotation(context);
        if (displayRotation != 0) {
            if (displayRotation == 90) {
                return;
            }
            if (displayRotation != 180) {
                if (displayRotation == 270) {
                    f = 1.0f - rectF.right;
                    f4 = 1.0f - rectF.bottom;
                    f3 = 1.0f - rectF.left;
                    f2 = rectF.top;
                    rectF.set(f, f4, f3, 1.0f - f2);
                }
                return;
            }
        }
        f = rectF.top;
        f4 = 1.0f - rectF.right;
        f3 = rectF.bottom;
        f2 = rectF.left;
        rectF.set(f, f4, f3, 1.0f - f2);
    }

    public static void toVidhanceCoordinateSystem(RectF rectF) {
        rectF.offsetTo(rectF.left - 0.5f, rectF.top - 0.5f);
    }
}
