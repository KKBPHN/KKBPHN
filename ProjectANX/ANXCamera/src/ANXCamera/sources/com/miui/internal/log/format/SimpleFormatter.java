package com.miui.internal.log.format;

import com.android.camera.Util;
import com.miui.internal.log.Level;
import com.miui.internal.log.message.Message;
import java.io.PrintWriter;
import java.io.Writer;
import java.util.Calendar;

public class SimpleFormatter implements Formatter {
    private static final String DEFAULT_DELIMITER = " - ";
    private ThreadLocal mThreadCache = new ThreadLocal() {
        /* access modifiers changed from: protected */
        public ThreadCache initialValue() {
            return new ThreadCache();
        }
    };

    class CachedDateFormat {
        private static final int MILLISECONDS_RESERVE_OFFSET = 3;
        private static final int MINUTE_IN_MILLISECONDS = 60000;
        private static final int SECONDS_RESERVE_OFFSET = 6;
        private static final int SECOND_IN_MILLISECONDS = 1000;
        private StringBuilder iCache;
        private long iCacheTime;
        private Calendar iCalendar;
        private long iMillisecondsSlot;
        private long iSecondsSlot;

        private CachedDateFormat() {
            this.iCache = new StringBuilder();
            this.iCalendar = Calendar.getInstance();
        }

        /* JADX WARNING: Code restructure failed: missing block: B:10:0x001e, code lost:
            if (r4 < 10) goto L_0x001a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:8:0x0018, code lost:
            if (r4 < 100) goto L_0x001a;
         */
        /* JADX WARNING: Code restructure failed: missing block: B:9:0x001a, code lost:
            r3.append('0');
         */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        private StringBuilder appendInt(StringBuilder sb, int i, int i2) {
            if (i2 != 2) {
                if (i2 == 3) {
                    if (i < 10) {
                        sb.append("00");
                    }
                }
            }
            sb.append(i);
            return sb;
        }

        private void buildCache(long j) {
            this.iCache.setLength(0);
            this.iCalendar.setTimeInMillis(j);
            StringBuilder sb = this.iCache;
            sb.append(this.iCalendar.get(1));
            sb.append('-');
            StringBuilder sb2 = this.iCache;
            appendInt(sb2, this.iCalendar.get(2) + 1, 2);
            sb2.append('-');
            StringBuilder sb3 = this.iCache;
            appendInt(sb3, this.iCalendar.get(5), 2);
            sb3.append(' ');
            StringBuilder sb4 = this.iCache;
            appendInt(sb4, this.iCalendar.get(11), 2);
            sb4.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            StringBuilder sb5 = this.iCache;
            appendInt(sb5, this.iCalendar.get(12), 2);
            sb5.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
            StringBuilder sb6 = this.iCache;
            appendInt(sb6, this.iCalendar.get(13), 2);
            sb6.append(',');
            appendInt(this.iCache, this.iCalendar.get(14), 3);
        }

        public void format(StringBuilder sb, long j) {
            long j2;
            if (j != this.iCacheTime) {
                long j3 = this.iSecondsSlot;
                if (j3 == 0 || j < j3 || j >= j3 + 60000) {
                    buildCache(j);
                    this.iSecondsSlot = (j / 60000) * 60000;
                    j2 = (j / 1000) * 1000;
                } else {
                    long j4 = this.iMillisecondsSlot;
                    if (j < j4 || j >= 1000 + j4) {
                        int i = (int) (j - this.iSecondsSlot);
                        int i2 = i / 1000;
                        int i3 = i2 * 1000;
                        int i4 = i - i3;
                        StringBuilder sb2 = this.iCache;
                        sb2.setLength(sb2.length() - 6);
                        StringBuilder sb3 = this.iCache;
                        appendInt(sb3, i2, 2);
                        sb3.append(',');
                        appendInt(this.iCache, i4, 3);
                        j2 = this.iSecondsSlot + ((long) i3);
                    } else {
                        int i5 = (int) (j - j4);
                        StringBuilder sb4 = this.iCache;
                        sb4.setLength(sb4.length() - 3);
                        appendInt(this.iCache, i5, 3);
                        this.iCacheTime = j;
                    }
                }
                this.iMillisecondsSlot = j2;
                this.iCacheTime = j;
            }
            sb.append(this.iCache);
        }
    }

    class StringBuilderWriter extends Writer {
        private StringBuilder iOut;

        public StringBuilderWriter(StringBuilder sb) {
            this.iOut = sb;
        }

        private static void checkOffsetAndCount(int i, int i2, int i3) {
            if ((i2 | i3) < 0 || i2 > i || i - i2 < i3) {
                StringBuilder sb = new StringBuilder();
                sb.append("length=");
                sb.append(i);
                sb.append("; regionStart=");
                sb.append(i2);
                sb.append("; regionLength=");
                sb.append(i3);
                throw new ArrayIndexOutOfBoundsException(sb.toString());
            }
        }

        public void close() {
        }

        public void flush() {
        }

        public void write(char[] cArr, int i, int i2) {
            checkOffsetAndCount(cArr.length, i, i2);
            if (i2 != 0) {
                this.iOut.append(cArr, i, i2);
            }
        }
    }

    class ThreadCache {
        CachedDateFormat dateFormat = new CachedDateFormat();
        StringBuilder out = new StringBuilder();
        PrintWriter printer = new PrintWriter(new StringBuilderWriter(this.out));

        ThreadCache() {
        }
    }

    private String doFormat(String str, String str2, long j, Level level, String str3, Message message, Throwable th) {
        ThreadCache threadCache = (ThreadCache) this.mThreadCache.get();
        StringBuilder sb = threadCache.out;
        sb.setLength(0);
        threadCache.dateFormat.format(sb, j);
        String str4 = DEFAULT_DELIMITER;
        sb.append(str4);
        sb.append('[');
        sb.append(level.name());
        sb.append("::");
        sb.append(str);
        sb.append(']');
        sb.append(str4);
        sb.append(str2);
        sb.append(": ");
        if (str3 == null) {
            message.format(sb);
        } else {
            sb.append(str3);
        }
        sb.append(10);
        if (th != null) {
            th.printStackTrace(threadCache.printer);
        }
        if (sb.length() > 8192) {
            sb.setLength(8192);
            sb.trimToSize();
        }
        return sb.toString();
    }

    public String format(String str, String str2, long j, Level level, Message message) {
        return doFormat(str, str2, j, level, null, message, message.getThrowable());
    }

    public String format(String str, String str2, long j, Level level, String str3, Throwable th) {
        return doFormat(str, str2, j, level, str3, null, th);
    }
}
