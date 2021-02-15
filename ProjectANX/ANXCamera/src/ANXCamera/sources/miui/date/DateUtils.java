package miui.date;

import android.app.Application;
import android.content.res.Resources;
import android.text.format.DateFormat;
import com.miui.internal.R;
import java.util.TimeZone;
import miui.util.AppConstants;
import miui.util.Pools;
import miui.util.Pools.Manager;
import miui.util.Pools.Pool;

public class DateUtils {
    private static final Pool CALENDAR_POOL = Pools.createSoftReferencePool(new Manager() {
        public Calendar createInstance() {
            return new Calendar();
        }
    }, 1);
    public static final int FORMAT_12HOUR = 16;
    public static final int FORMAT_24HOUR = 32;
    public static final int FORMAT_ABBREV_ALL = 28672;
    public static final int FORMAT_ABBREV_MONTH = 4096;
    public static final int FORMAT_ABBREV_TIME = 16384;
    public static final int FORMAT_ABBREV_WEEKDAY = 8192;
    public static final int FORMAT_NO_AM_PM = 64;
    public static final int FORMAT_NUMERIC_DATE = 32768;
    public static final int FORMAT_SHOW_BRIEF_TIME = 12;
    public static final int FORMAT_SHOW_DATE = 896;
    public static final int FORMAT_SHOW_HOUR = 8;
    public static final int FORMAT_SHOW_MILLISECOND = 1;
    public static final int FORMAT_SHOW_MINUTE = 4;
    public static final int FORMAT_SHOW_MONTH = 256;
    public static final int FORMAT_SHOW_MONTH_DAY = 128;
    public static final int FORMAT_SHOW_SECOND = 2;
    public static final int FORMAT_SHOW_TIME = 15;
    public static final int FORMAT_SHOW_TIME_ZONE = 2048;
    public static final int FORMAT_SHOW_WEEKDAY = 1024;
    public static final int FORMAT_SHOW_YEAR = 512;

    protected DateUtils() {
        throw new InstantiationException("Cannot instantiate utility class");
    }

    public static String formatDateTime(long j, int i) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        formatDateTime(sb, j, i, null);
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    public static String formatDateTime(long j, int i, TimeZone timeZone) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        formatDateTime(sb, j, i, timeZone);
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    public static StringBuilder formatDateTime(StringBuilder sb, long j, int i) {
        formatDateTime(sb, j, i, null);
        return sb;
    }

    public static StringBuilder formatDateTime(StringBuilder sb, long j, int i, TimeZone timeZone) {
        int i2;
        Application currentApplication = AppConstants.getCurrentApplication();
        if ((i & 16) == 0 && (i & 32) == 0) {
            i |= DateFormat.is24HourFormat(currentApplication) ? 32 : 16;
        }
        String string = currentApplication.getString(getFormatResId(i));
        StringBuilder sb2 = (StringBuilder) Pools.getStringBuilderPool().acquire();
        Calendar calendar = (Calendar) CALENDAR_POOL.acquire();
        calendar.setTimeZone(timeZone);
        calendar.setTimeInMillis(j);
        int length = string.length();
        for (int i3 = 0; i3 < length; i3++) {
            char charAt = string.charAt(i3);
            if (charAt == 'D') {
                i2 = getDatePatternResId(i);
            } else if (charAt == 'T') {
                i2 = getTimePatternResId(calendar, i);
            } else if (charAt != 'W') {
                sb2.append(charAt);
            } else {
                i2 = getWeekdayPatternResId(i);
            }
            sb2.append(currentApplication.getString(i2));
        }
        calendar.format(sb, (CharSequence) sb2);
        Pools.getStringBuilderPool().release(sb2);
        CALENDAR_POOL.release(calendar);
        return sb;
    }

    public static String formatRelativeTime(long j, boolean z) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        formatRelativeTime(sb, j, z, null);
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    public static String formatRelativeTime(long j, boolean z, TimeZone timeZone) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        formatRelativeTime(sb, j, z, timeZone);
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    public static StringBuilder formatRelativeTime(StringBuilder sb, long j, boolean z) {
        formatRelativeTime(sb, j, z, null);
        return sb;
    }

    public static StringBuilder formatRelativeTime(StringBuilder sb, long j, boolean z, TimeZone timeZone) {
        int i;
        StringBuilder sb2 = sb;
        long j2 = j;
        TimeZone timeZone2 = timeZone;
        long currentTimeMillis = System.currentTimeMillis();
        boolean z2 = currentTimeMillis >= j2;
        long abs = Math.abs(currentTimeMillis - j2) / 60000;
        Resources resources = AppConstants.getCurrentApplication().getResources();
        int i2 = (abs > 60 ? 1 : (abs == 60 ? 0 : -1));
        if (i2 > 0 || z) {
            Calendar calendar = (Calendar) CALENDAR_POOL.acquire();
            calendar.setTimeZone(timeZone2);
            calendar.setTimeInMillis(currentTimeMillis);
            int i3 = calendar.get(1);
            int i4 = calendar.get(12);
            int i5 = calendar.get(14);
            calendar.setTimeInMillis(j2);
            boolean z3 = i3 == calendar.get(1);
            if (!z3 || i4 != calendar.get(12)) {
                if (!z3 || Math.abs(i4 - calendar.get(12)) >= 2) {
                    if (z3 && Math.abs(i4 - calendar.get(12)) < 7) {
                        if (z2 == (i5 > calendar.get(14))) {
                            i = 13324;
                            formatDateTime(sb2, j2, i, timeZone2);
                            CALENDAR_POOL.release(calendar);
                        }
                    }
                    int i6 = z3 ? z ? m.dw : m.aV : z ? 908 : FORMAT_SHOW_DATE;
                    i = i6 | 12288;
                    formatDateTime(sb2, j2, i, timeZone2);
                    CALENDAR_POOL.release(calendar);
                } else {
                    sb2.append(resources.getString(z2 ? R.string.yesterday : R.string.tomorrow));
                    sb2.append(' ');
                }
            }
            formatDateTime(sb2, j2, 12300, timeZone2);
            CALENDAR_POOL.release(calendar);
        } else {
            int i7 = z2 ? i2 == 0 ? R.plurals.abbrev_a_hour_ago : abs == 30 ? R.plurals.abbrev_half_hour_ago : abs == 0 ? R.plurals.abbrev_less_than_one_minute_ago : R.plurals.abbrev_num_minutes_ago : i2 == 0 ? R.plurals.abbrev_in_a_hour : abs == 30 ? R.plurals.abbrev_in_half_hour : abs == 0 ? R.plurals.abbrev_in_less_than_one_minute : R.plurals.abbrev_in_num_minutes;
            sb2.append(String.format(resources.getQuantityString(i7, (int) abs), new Object[]{Long.valueOf(abs)}));
        }
        return sb2;
    }

    private static int getDatePatternResId(int i) {
        String str = "no any time date";
        if ((i & 32768) != 32768) {
            if ((i & 4096) == 4096) {
                if ((i & 512) != 512) {
                    int i2 = i & 256;
                    int i3 = i & 128;
                    if (i2 == 256) {
                        return i3 == 128 ? R.string.fmt_date_short_month_day : R.string.fmt_date_short_month;
                    }
                    if (i3 != 128) {
                        throw new IllegalArgumentException(str);
                    }
                    return R.string.fmt_date_day;
                } else if ((i & 256) == 256) {
                    return (i & 128) == 128 ? R.string.fmt_date_short_year_month_day : R.string.fmt_date_short_year_month;
                }
            } else if ((i & 512) != 512) {
                int i4 = i & 256;
                int i5 = i & 128;
                if (i4 == 256) {
                    return i5 == 128 ? R.string.fmt_date_long_month_day : R.string.fmt_date_long_month;
                }
                if (i5 != 128) {
                    throw new IllegalArgumentException(str);
                }
                return R.string.fmt_date_day;
            } else if ((i & 256) == 256) {
                return (i & 128) == 128 ? R.string.fmt_date_long_year_month_day : R.string.fmt_date_long_year_month;
            }
            return R.string.fmt_date_year;
        } else if ((i & 512) == 512) {
            return (i & 256) == 256 ? (i & 128) == 128 ? R.string.fmt_date_numeric_year_month_day : R.string.fmt_date_numeric_year_month : R.string.fmt_date_numeric_year;
        } else {
            int i6 = i & 256;
            int i7 = i & 128;
            if (i6 == 256) {
                return i7 == 128 ? R.string.fmt_date_numeric_month_day : R.string.fmt_date_numeric_month;
            }
            if (i7 == 128) {
                return R.string.fmt_date_numeric_day;
            }
            throw new IllegalArgumentException(str);
        }
    }

    private static int getFormatResId(int i) {
        if ((i & 1024) == 1024) {
            if ((i & FORMAT_SHOW_DATE) != 0) {
                int i2 = i & 15;
                int i3 = i & 2048;
                return i2 != 0 ? i3 == 2048 ? R.string.fmt_weekday_date_time_timezone : R.string.fmt_weekday_date_time : i3 == 2048 ? R.string.fmt_weekday_date_timezone : R.string.fmt_weekday_date;
            }
            int i4 = i & 15;
            int i5 = i & 2048;
            return i4 != 0 ? i5 == 2048 ? R.string.fmt_weekday_time_timezone : R.string.fmt_weekday_time : i5 == 2048 ? R.string.fmt_weekday_timezone : R.string.fmt_weekday;
        } else if ((i & FORMAT_SHOW_DATE) != 0) {
            int i6 = i & 15;
            int i7 = i & 2048;
            return i6 != 0 ? i7 == 2048 ? R.string.fmt_date_time_timezone : R.string.fmt_date_time : i7 == 2048 ? R.string.fmt_date_timezone : R.string.fmt_date;
        } else {
            int i8 = i & 15;
            int i9 = i & 2048;
            return i8 != 0 ? i9 == 2048 ? R.string.fmt_time_timezone : R.string.fmt_time : i9 == 2048 ? R.string.fmt_timezone : R.string.empty;
        }
    }

    private static int getTimePatternResId(Calendar calendar, int i) {
        if ((i & 16384) == 16384 && (((i & 1) != 1 || calendar.get(22) == 0) && (i & 14) != 0)) {
            i &= -2;
            if (((i & 2) != 2 || calendar.get(21) == 0) && (i & 12) != 0) {
                i &= -3;
                if (calendar.get(20) == 0 && (i & 8) != 0) {
                    i &= -5;
                }
            }
        }
        if ((i & 8) == 8) {
            return (i & 16) == 16 ? (i & 64) == 64 ? (i & 4) == 4 ? (i & 2) == 2 ? (i & 1) == 1 ? R.string.fmt_time_12hour_minute_second_millis : R.string.fmt_time_12hour_minute_second : R.string.fmt_time_12hour_minute : R.string.fmt_time_12hour : (i & 4) == 4 ? (i & 2) == 2 ? (i & 1) == 1 ? R.string.fmt_time_12hour_minute_second_millis_pm : R.string.fmt_time_12hour_minute_second_pm : R.string.fmt_time_12hour_minute_pm : R.string.fmt_time_12hour_pm : (i & 4) == 4 ? (i & 2) == 2 ? (i & 1) == 1 ? R.string.fmt_time_24hour_minute_second_millis : R.string.fmt_time_24hour_minute_second : R.string.fmt_time_24hour_minute : R.string.fmt_time_24hour;
        }
        if ((i & 4) == 4) {
            return (i & 2) == 2 ? (i & 1) == 1 ? R.string.fmt_time_minute_second_millis : R.string.fmt_time_minute_second : R.string.fmt_time_minute;
        }
        if ((i & 2) == 2) {
            return (i & 1) == 1 ? R.string.fmt_time_second_millis : R.string.fmt_time_second;
        }
        if ((i & 1) == 1) {
            return R.string.fmt_time_millis;
        }
        throw new IllegalArgumentException("no any time date");
    }

    private static int getWeekdayPatternResId(int i) {
        return (i & 8192) == 8192 ? R.string.fmt_weekday_short : R.string.fmt_weekday_long;
    }
}
