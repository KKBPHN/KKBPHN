package miuix.overscroller.widget;

import android.util.Log;
import java.util.Locale;

class OverScrollLogger {
    private static final boolean DEBUG;
    private static final String TAG = "OverScroll";
    private static final boolean VERBOSE;

    static {
        String str = TAG;
        DEBUG = Log.isLoggable(str, 3);
        VERBOSE = Log.isLoggable(str, 2);
    }

    OverScrollLogger() {
    }

    public static void debug(String str) {
        if (DEBUG) {
            Log.d(TAG, str);
        }
    }

    public static void debug(String str, Object... objArr) {
        if (DEBUG) {
            Log.d(TAG, String.format(Locale.US, str, objArr));
        }
    }

    public static void verbose(String str) {
        if (VERBOSE) {
            Log.v(TAG, str);
        }
    }

    public static void verbose(String str, Object... objArr) {
        if (VERBOSE) {
            Log.v(TAG, String.format(Locale.US, str, objArr));
        }
    }
}
