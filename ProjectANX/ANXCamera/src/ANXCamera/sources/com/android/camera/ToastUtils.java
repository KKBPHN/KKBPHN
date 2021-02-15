package com.android.camera;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.text.TextUtils;
import android.view.InflateException;
import android.view.WindowManager.LayoutParams;
import android.widget.Toast;
import com.android.camera.log.Log;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class ToastUtils {
    private static final long SHORT_DURATION_TIMEOUT = 2000;
    public static final String TAG = "ToastUtils";
    private static int sGravity = 17;
    private static String sOldMsg = null;
    private static long sOneTime = 0;
    protected static Toast sToast = null;
    private static long sTwoTime = 0;
    private static int sXOffset = 0;
    private static int sYOffset = 0;
    private static int sYOffsetMiui12 = 176;

    private static LayoutParams getWindowParams(Toast toast) {
        String str;
        String str2 = TAG;
        if (toast == null) {
            return null;
        }
        try {
            Method method = Toast.class.getMethod("getWindowParams", new Class[0]);
            method.setAccessible(true);
            return (LayoutParams) method.invoke(toast, new Object[0]);
        } catch (NoSuchMethodException e) {
            e = e;
            str = "getWindowParams: no such method";
            Log.d(str2, str, e);
            StringBuilder sb = new StringBuilder();
            sb.append("getWindowsParam: ret: ");
            sb.append(null);
            Log.d(str2, sb.toString());
            return null;
        } catch (IllegalAccessException e2) {
            e = e2;
            str = "getWindowParams: cannot access";
            Log.d(str2, str, e);
            StringBuilder sb2 = new StringBuilder();
            sb2.append("getWindowsParam: ret: ");
            sb2.append(null);
            Log.d(str2, sb2.toString());
            return null;
        } catch (InvocationTargetException e3) {
            e = e3;
            str = "getWindowParams: invoke failed: ";
            Log.d(str2, str, e);
            StringBuilder sb22 = new StringBuilder();
            sb22.append("getWindowsParam: ret: ");
            sb22.append(null);
            Log.d(str2, sb22.toString());
            return null;
        }
    }

    private static void prepareShowOnKeyguard(Toast toast) {
        LayoutParams windowParams = getWindowParams(toast);
        if (windowParams != null) {
            windowParams.flags |= 524288;
        }
    }

    public static void showToast(Context context, int i) {
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                showToast(new WeakReference(context), resources.getString(i), 17, 0, 0, false);
            }
        }
    }

    public static void showToast(Context context, int i, int i2) {
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                showToast(new WeakReference(context), resources.getString(i), i2, 0, i2 == 80 ? sYOffsetMiui12 : 0, false);
            }
        }
    }

    public static void showToast(Context context, int i, int i2, int i3, int i4) {
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                showToast(new WeakReference(context), resources.getString(i), i2, i3, i4, false);
            }
        }
    }

    public static void showToast(Context context, int i, boolean z) {
        if (context != null) {
            Resources resources = context.getResources();
            if (resources != null) {
                showToast(new WeakReference(context), resources.getString(i), 17, 0, 0, z);
            }
        }
    }

    public static void showToast(Context context, String str) {
        showToast(new WeakReference(context), str, 17, 0, 0, false);
    }

    public static void showToast(Context context, String str, int i) {
        if (context != null && context.getResources() != null) {
            showToast(new WeakReference(context), str, i, 0, i == 80 ? sYOffsetMiui12 : 0, false);
        }
    }

    public static void showToast(Context context, String str, int i, int i2, int i3) {
        if (context != null && context.getResources() != null) {
            showToast(new WeakReference(context), str, i, i2, i3, false);
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:15:0x0044 A[Catch:{ InflateException -> 0x005d, Exception -> 0x0058 }] */
    /* JADX WARNING: Removed duplicated region for block: B:17:0x0049 A[Catch:{ InflateException -> 0x005d, Exception -> 0x0058 }] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private static void showToast(WeakReference weakReference, String str, int i, int i2, int i3, boolean z) {
        boolean z2;
        Context context = (Context) weakReference.get();
        if (context instanceof Activity) {
            Intent intent = ((Activity) context).getIntent();
            if (intent != null && intent.getBooleanExtra("StartActivityWhenLocked", false)) {
                z2 = true;
                if (!TextUtils.isEmpty(str) && context != null) {
                    Toast makeText = Toast.makeText(context.getApplicationContext(), str, 0);
                    sGravity = makeText.getGravity();
                    sXOffset = makeText.getXOffset();
                    sYOffset = makeText.getYOffset();
                    if (!z) {
                        makeText.setGravity(i, i2, i3);
                    }
                    if (z2) {
                        prepareShowOnKeyguard(makeText);
                    }
                    makeText.show();
                    sOldMsg = str;
                    sOneTime = System.currentTimeMillis();
                }
            }
        }
        z2 = false;
        try {
            Toast makeText2 = Toast.makeText(context.getApplicationContext(), str, 0);
            sGravity = makeText2.getGravity();
            sXOffset = makeText2.getXOffset();
            sYOffset = makeText2.getYOffset();
            if (!z) {
            }
            if (z2) {
            }
            makeText2.show();
            sOldMsg = str;
            sOneTime = System.currentTimeMillis();
        } catch (InflateException e) {
            e.printStackTrace();
        } catch (Exception e2) {
            e2.printStackTrace();
        }
    }
}
