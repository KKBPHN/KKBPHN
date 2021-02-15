package miui.animation.utils;

import android.animation.ArgbEvaluator;
import android.graphics.RectF;
import android.util.Log;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnPreDrawListener;
import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.ref.WeakReference;
import java.lang.reflect.Array;
import java.util.Map;
import java.util.Map.Entry;
import miui.animation.IAnimTarget;
import miui.animation.utils.EaseManager.EaseStyle;

public class CommonUtils {
    private static final Class[] BUILT_IN = {String.class, Integer.TYPE, Integer.class, Long.TYPE, Long.class, Short.TYPE, Short.class, Float.TYPE, Float.class, Double.TYPE, Double.class};
    public static final int KEY_BASE = 269156352;
    public static final int KEY_FOLME_FORGROUND_COLOR = 269156355;
    public static final int KEY_FOLME_INIT_LAYOUT = 269156356;
    public static final int KEY_FOLME_IN_DRAGGING = 269156357;
    public static final int KEY_FOLME_LISTVIEW_POS = 269156353;
    public static final int KEY_FOLME_LISTVIEW_TOUCH_LISTENER = 269156354;
    public static final int KEY_FOLME_SET_HEIGHT = 269156359;
    public static final int KEY_FOLME_SET_WIDTH = 269156358;
    public static final String TAG = "miuisdk_anim";
    public static final int UNIT_SECOND = 1000;
    public static final ArgbEvaluator sArgbEvaluator = new ArgbEvaluator();
    public static final EaseStyle sDefEase = EaseManager.getStyle(-2, 0.85f, 0.3f);
    private static float sTouchSlop;

    class OnPreDrawTask implements OnPreDrawListener {
        Runnable mTask;
        WeakReference mView;

        OnPreDrawTask(Runnable runnable) {
            this.mTask = runnable;
        }

        public boolean onPreDraw() {
            View view = (View) this.mView.get();
            if (view != null) {
                Runnable runnable = this.mTask;
                if (runnable != null) {
                    runnable.run();
                }
                view.getViewTreeObserver().removeOnPreDrawListener(this);
            }
            this.mTask = null;
            return true;
        }

        public void start(View view) {
            ViewTreeObserver viewTreeObserver = view.getViewTreeObserver();
            this.mView = new WeakReference(view);
            viewTreeObserver.addOnPreDrawListener(this);
        }
    }

    private CommonUtils() {
    }

    private static void closeQuietly(Closeable closeable) {
        if (closeable != null) {
            try {
                closeable.close();
            } catch (Exception e) {
                StringBuilder sb = new StringBuilder();
                sb.append("close ");
                sb.append(closeable);
                sb.append(" failed");
                Log.w(TAG, sb.toString(), e);
            }
        }
    }

    public static void getRect(IAnimTarget iAnimTarget, RectF rectF) {
        rectF.left = iAnimTarget.getValue(0);
        rectF.top = iAnimTarget.getValue(1);
        rectF.right = rectF.left + iAnimTarget.getValue(6);
        rectF.bottom = rectF.top + iAnimTarget.getValue(5);
    }

    public static float getSize(IAnimTarget iAnimTarget, int i) {
        if (i == 0) {
            i = 6;
        } else if (i == 1) {
            i = 5;
        } else if (!(i == 6 || i == 5)) {
            i = -1;
        }
        if (i == -1) {
            return 0.0f;
        }
        return iAnimTarget.getValue(i);
    }

    public static float getTouchSlop(View view) {
        if (sTouchSlop == 0.0f) {
            sTouchSlop = (float) ViewConfiguration.get(view.getContext()).getScaledTouchSlop();
        }
        return sTouchSlop;
    }

    public static boolean hasFlags(long j, long j2) {
        return (j & j2) != 0;
    }

    public static boolean inArray(Object[] objArr, Object obj) {
        if (!(obj == null || objArr == null || objArr.length <= 0)) {
            for (Object equals : objArr) {
                if (equals.equals(obj)) {
                    return true;
                }
            }
        }
        return false;
    }

    public static boolean isArrayEmpty(Object[] objArr) {
        return objArr == null || objArr.length == 0;
    }

    public static boolean isBuiltInClass(Class cls) {
        return inArray(BUILT_IN, cls);
    }

    public static StringBuilder mapToString(Map map, String str) {
        StringBuilder sb = new StringBuilder();
        sb.append('{');
        if (map != null && map.size() > 0) {
            for (Entry entry : map.entrySet()) {
                sb.append(10);
                sb.append(str);
                sb.append(entry.getKey());
                sb.append('=');
                sb.append(entry.getValue());
            }
            sb.append(10);
        }
        sb.append('}');
        return sb;
    }

    public static String mapsToString(Map[] mapArr) {
        StringBuilder sb = new StringBuilder();
        sb.append('[');
        int i = 0;
        while (true) {
            int length = mapArr.length;
            sb.append(10);
            if (i < length) {
                sb.append(i);
                sb.append('.');
                sb.append(mapToString(mapArr[i], "    "));
                i++;
            } else {
                sb.append(']');
                return sb.toString();
            }
        }
    }

    @SafeVarargs
    public static Object[] mergeArray(Object[] objArr, Object... objArr2) {
        if (objArr == null) {
            return objArr2;
        }
        if (objArr2 == null) {
            return objArr;
        }
        Object newInstance = Array.newInstance(objArr.getClass().getComponentType(), objArr.length + objArr2.length);
        System.arraycopy(objArr, 0, newInstance, 0, objArr.length);
        System.arraycopy(objArr2, 0, newInstance, objArr.length, objArr2.length);
        return (Object[]) newInstance;
    }

    public static String readProp(String str) {
        InputStreamReader inputStreamReader;
        BufferedReader bufferedReader;
        BufferedReader bufferedReader2 = null;
        try {
            Runtime runtime = Runtime.getRuntime();
            StringBuilder sb = new StringBuilder();
            sb.append("getprop ");
            sb.append(str);
            inputStreamReader = new InputStreamReader(runtime.exec(sb.toString()).getInputStream());
            try {
                bufferedReader = new BufferedReader(inputStreamReader);
            } catch (IOException e) {
                e = e;
                try {
                    Log.i(TAG, "readProp failed", e);
                    closeQuietly(bufferedReader2);
                    closeQuietly(inputStreamReader);
                    return "";
                } catch (Throwable th) {
                    th = th;
                    closeQuietly(bufferedReader2);
                    closeQuietly(inputStreamReader);
                    throw th;
                }
            }
            try {
                String readLine = bufferedReader.readLine();
                closeQuietly(bufferedReader);
                closeQuietly(inputStreamReader);
                return readLine;
            } catch (IOException e2) {
                Throwable th2 = e2;
                bufferedReader2 = bufferedReader;
                e = th2;
                Log.i(TAG, "readProp failed", e);
                closeQuietly(bufferedReader2);
                closeQuietly(inputStreamReader);
                return "";
            } catch (Throwable th3) {
                Throwable th4 = th3;
                bufferedReader2 = bufferedReader;
                th = th4;
                closeQuietly(bufferedReader2);
                closeQuietly(inputStreamReader);
                throw th;
            }
        } catch (IOException e3) {
            e = e3;
            inputStreamReader = null;
            Log.i(TAG, "readProp failed", e);
            closeQuietly(bufferedReader2);
            closeQuietly(inputStreamReader);
            return "";
        } catch (Throwable th5) {
            th = th5;
            inputStreamReader = null;
            closeQuietly(bufferedReader2);
            closeQuietly(inputStreamReader);
            throw th;
        }
    }

    public static void runOnPreDraw(View view, Runnable runnable) {
        if (view != null) {
            new OnPreDrawTask(runnable).start(view);
        }
    }

    public static float toFloatValue(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).floatValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).floatValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("toFloat failed, value is ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }

    public static int[] toIntArray(float[] fArr) {
        int[] iArr = new int[fArr.length];
        for (int i = 0; i < fArr.length; i++) {
            iArr[i] = (int) fArr[i];
        }
        return iArr;
    }

    public static int toIntValue(Object obj) {
        if (obj instanceof Integer) {
            return ((Integer) obj).intValue();
        }
        if (obj instanceof Float) {
            return ((Float) obj).intValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("toFloat failed, value is ");
        sb.append(obj);
        throw new IllegalArgumentException(sb.toString());
    }
}
