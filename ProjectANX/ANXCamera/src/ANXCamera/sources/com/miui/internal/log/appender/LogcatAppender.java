package com.miui.internal.log.appender;

import android.util.Log;
import com.miui.internal.log.Level;
import com.miui.internal.log.format.Formatter;
import com.miui.internal.log.message.Message;

public class LogcatAppender implements Appender {
    private ThreadLocal mThreadCache = new ThreadLocal() {
        /* access modifiers changed from: protected */
        public StringBuilder initialValue() {
            return new StringBuilder();
        }
    };

    /* renamed from: com.miui.internal.log.appender.LogcatAppender$2 reason: invalid class name */
    /* synthetic */ class AnonymousClass2 {
        static final /* synthetic */ int[] $SwitchMap$com$miui$internal$log$Level = new int[Level.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(12:0|1|2|3|4|5|6|7|8|9|10|(3:11|12|14)) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$miui$internal$log$Level[Level.VERBOSE.ordinal()] = 1;
            $SwitchMap$com$miui$internal$log$Level[Level.DEBUG.ordinal()] = 2;
            $SwitchMap$com$miui$internal$log$Level[Level.INFO.ordinal()] = 3;
            $SwitchMap$com$miui$internal$log$Level[Level.WARNING.ordinal()] = 4;
            $SwitchMap$com$miui$internal$log$Level[Level.ERROR.ordinal()] = 5;
            try {
                $SwitchMap$com$miui$internal$log$Level[Level.FATAL.ordinal()] = 6;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public void append(String str, String str2, long j, Level level, Message message) {
        StringBuilder sb = (StringBuilder) this.mThreadCache.get();
        sb.setLength(0);
        message.format(sb);
        append(str, str2, j, level, sb.toString(), message.getThrowable());
        if (sb.length() > 8192) {
            sb.setLength(8192);
            sb.trimToSize();
        }
    }

    public void append(String str, String str2, long j, Level level, String str3, Throwable th) {
        switch (AnonymousClass2.$SwitchMap$com$miui$internal$log$Level[level.ordinal()]) {
            case 1:
                if (th == null) {
                    Log.v(str2, str3);
                    return;
                } else {
                    Log.v(str2, str3, th);
                    return;
                }
            case 2:
                if (th == null) {
                    Log.d(str2, str3);
                    return;
                } else {
                    Log.d(str2, str3, th);
                    return;
                }
            case 3:
                if (th == null) {
                    Log.i(str2, str3);
                    return;
                } else {
                    Log.i(str2, str3, th);
                    return;
                }
            case 4:
                if (th == null) {
                    Log.w(str2, str3);
                    return;
                } else {
                    Log.w(str2, str3, th);
                    return;
                }
            case 5:
                if (th == null) {
                    Log.e(str2, str3);
                    return;
                } else {
                    Log.e(str2, str3, th);
                    return;
                }
            case 6:
                if (th == null) {
                    Log.wtf(str2, str3);
                    return;
                } else {
                    Log.wtf(str2, str3, th);
                    return;
                }
            default:
                return;
        }
    }

    public void close() {
    }

    public Formatter getFormatter() {
        return null;
    }

    public void setFormatter(Formatter formatter) {
    }
}
