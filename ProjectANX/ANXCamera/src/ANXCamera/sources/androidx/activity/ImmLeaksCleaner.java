package androidx.activity;

import android.app.Activity;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import androidx.annotation.MainThread;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.GenericLifecycleObserver;
import androidx.lifecycle.Lifecycle.Event;
import androidx.lifecycle.LifecycleOwner;
import java.lang.reflect.Field;

@RequiresApi(19)
final class ImmLeaksCleaner implements GenericLifecycleObserver {
    private static final int INIT_FAILED = 2;
    private static final int INIT_SUCCESS = 1;
    private static final int NOT_INITIALIAZED = 0;
    private static Field sHField;
    private static Field sNextServedViewField;
    private static int sReflectedFieldsInitialized;
    private static Field sServedViewField;
    private Activity mActivity;

    ImmLeaksCleaner(Activity activity) {
        this.mActivity = activity;
    }

    @MainThread
    private static void initializeReflectiveFields() {
        try {
            sReflectedFieldsInitialized = 2;
            sServedViewField = InputMethodManager.class.getDeclaredField("mServedView");
            sServedViewField.setAccessible(true);
            sNextServedViewField = InputMethodManager.class.getDeclaredField("mNextServedView");
            sNextServedViewField.setAccessible(true);
            sHField = InputMethodManager.class.getDeclaredField("mH");
            sHField.setAccessible(true);
            sReflectedFieldsInitialized = 1;
        } catch (NoSuchFieldException unused) {
        }
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(3:29|30|31) */
    /* JADX WARNING: Code restructure failed: missing block: B:28:0x0040, code lost:
        r1.isActive();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:31:0x0045, code lost:
        return;
     */
    /* JADX WARNING: Missing exception handler attribute for start block: B:29:0x0044 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onStateChanged(LifecycleOwner lifecycleOwner, Event event) {
        if (event == Event.ON_DESTROY) {
            if (sReflectedFieldsInitialized == 0) {
                initializeReflectiveFields();
            }
            if (sReflectedFieldsInitialized == 1) {
                InputMethodManager inputMethodManager = (InputMethodManager) this.mActivity.getSystemService("input_method");
                try {
                    Object obj = sHField.get(inputMethodManager);
                    if (obj != null) {
                        synchronized (obj) {
                            try {
                                View view = (View) sServedViewField.get(inputMethodManager);
                                if (view != null) {
                                    if (!view.isAttachedToWindow()) {
                                        sNextServedViewField.set(inputMethodManager, null);
                                    }
                                }
                            } catch (IllegalAccessException unused) {
                            } catch (ClassCastException unused2) {
                            } catch (Throwable th) {
                                throw th;
                            }
                        }
                    }
                } catch (IllegalAccessException unused3) {
                }
            }
        }
    }
}
