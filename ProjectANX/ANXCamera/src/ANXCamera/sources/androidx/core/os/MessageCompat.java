package androidx.core.os;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.os.Message;
import androidx.annotation.NonNull;

public final class MessageCompat {
    private static boolean sTryIsAsynchronous = true;
    private static boolean sTrySetAsynchronous = true;

    private MessageCompat() {
    }

    @SuppressLint({"NewApi"})
    public static boolean isAsynchronous(@NonNull Message message) {
        int i = VERSION.SDK_INT;
        if (i >= 22) {
            return message.isAsynchronous();
        }
        if (sTryIsAsynchronous && i >= 16) {
            try {
                return message.isAsynchronous();
            } catch (NoSuchMethodError unused) {
                sTryIsAsynchronous = false;
            }
        }
        return false;
    }

    @SuppressLint({"NewApi"})
    public static void setAsynchronous(@NonNull Message message, boolean z) {
        int i = VERSION.SDK_INT;
        if (i >= 22) {
            message.setAsynchronous(z);
            return;
        }
        if (sTrySetAsynchronous && i >= 16) {
            try {
                message.setAsynchronous(z);
            } catch (NoSuchMethodError unused) {
                sTrySetAsynchronous = false;
            }
        }
    }
}
