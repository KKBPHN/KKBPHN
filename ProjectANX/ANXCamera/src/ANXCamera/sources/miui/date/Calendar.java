package miui.date;

import com.android.camera.Util;
import com.miui.internal.util.SimpleNumberFormatter;
import java.io.Serializable;
import java.util.Date;
import java.util.TimeZone;
import miui.media.Recorder.ErrorCode;
import miui.util.Pools;

public class Calendar implements Serializable, Cloneable, Comparable {
    public static final int AD = 1;
    public static final int AFTERNOON = 4;
    public static final int AM = 0;
    public static final int AM_PM = 17;
    public static final int APRIL = 3;
    public static final int AUGUST = 7;
    public static final int AUTUMN_BEGINS = 15;
    public static final int AUTUMN_EQUINOX = 18;
    public static final int BC = 0;
    public static final int CHICKEN = 9;
    public static final int CHINESE_ERA_DAY = 11;
    public static final int CHINESE_ERA_HOUR = 19;
    public static final int CHINESE_ERA_MONTH = 7;
    public static final int CHINESE_ERA_YEAR = 4;
    public static final int CHINESE_MONTH = 6;
    public static final int CHINESE_MONTH_IS_LEAP = 8;
    public static final int CHINESE_YEAR = 2;
    private static final int[] CHINESE_YEAR_INFO = {19416, 19168, 42352, 21717, 53856, 55632, 21844, 22191, 39632, 21970, 19168, 42422, 42192, 53840, 53909, 46415, 54944, 44450, 38320, 18807, 18815, 42160, 46261, 27216, 27968, 43860, 11119, 38256, 21234, 18800, 25958, 54432, 59984, 27285, 23263, 11104, 34531, 37615, 51415, 51551, 54432, 55462, 46431, 22176, 42420, 9695, 37584, 53938, 43344, 46423, 27808, 46416, 21333, 19887, 42416, 17779, 21183, 43432, 59728, 27296, 44710, 43856, 19296, 43748, 42352, 21088, 62051, 55632, 23383, 22176, 38608, 19925, 19152, 42192, 54484, 53840, 54616, 46400, 46752, 38310, 38335, 18864, 43380, 42160, 45690, 27216, 27968, 44870, 43872, 38256, 19189, 18800, 25776, 29859, 59984, 27480, 23232, 43872, 38613, 37600, 51552, 55636, 54432, 55888, 30034, 22176, 43959, 9680, 37584, 51893, 43344, 46240, 47780, 44368, 21977, 19360, 42416, 20854, 21183, 43312, 31060, 27296, 44368, 23378, 19296, 42726, 42208, 53856, 60005, 54576, 23200, 30371, 38608, 19195, 19152, 42192, 53430, 53855, 54560, 56645, 46496, 22224, 21938, 18864, 42359, 42160, 43600, 45653, 27951, 44448, 19299, 37759, 18936, 18800, 25776, 26790, 59999, 27424, 42692, 43759, 37600, 53987, 51552, 54615, 54432, 55888, 23893, 22176, 42704, 21972, 21200, 43448, 43344, 46240, 46758, 44368, 21920, 43940, 42416, 21168, 45683, 26928, 29495, 27296, 44368, 19285, 19311, 42352, 21732, 53856, 59752, 54560, 55968, 27302, 22239, 19168, 43476, 42192, 53584, 62034, 54560};
    public static final int CHINESE_YEAR_SYMBOL_ANIMAL = 3;
    public static final int CLEAR_AND_BRIGHT = 7;
    public static final int COLD_DEWS = 19;
    public static final int COW = 1;
    private static final int DAYS_19000131_TO_19700101 = -25537;
    private static final byte[] DAYS_IN_MONTH = {31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
    private static final int[] DAYS_IN_YEAR = {0, 31, 59, 90, 120, 151, 181, 212, 243, 273, af.bj, m.cy, m.cU};
    private static final int[] DAY_CHINESE_YEAR_FROM_19000101 = {0, m.aV, 738, 1093, 1476, 1830, 2185, 2569, 2923, 3278, 3662, 4016, 4400, 4754, 5108, 5492, 5846, 6201, 6585, 6940, 7324, 7678, 8032, 8416, 8770, 9124, 9509, 9863, 10218, 10602, 10956, 11339, 11693, 12048, 12432, 12787, 13141, 13525, 13879, 14263, 14617, 14971, 15355, 15710, 16064, 16449, 16803, 17157, 17541, 17895, 18279, 18633, 18988, 19372, 19726, 20081, 20465, 20819, 21202, 21557, 21911, 22295, 22650, 23004, 23388, 23743, 24096, 24480, 24835, 25219, 25573, 25928, 26312, 26666, 27020, 27404, 27758, 28142, 28496, 28851, 29235, 29590, 29944, 30328, 30682, 31066, 31420, 31774, 32158, 32513, 32868, 33252, 33606, 33960, 34343, 34698, 35082, 35436, 35791, 36175, 36529, 36883, 37267, 37621, 37976, 38360, 38714, 39099, 39453, 39807, 40191, 40545, 40899, 41283, 41638, 42022, 42376, 42731, 43115, 43469, 43823, 44207, 44561, 44916, 45300, 45654, 46038, 46392, 46746, 47130, 47485, 47839, 48223, 48578, 48962, 49316, 49670, 50054, 50408, 50762, 51146, 51501, 51856, 52240, 52594, 52978, 53332, 53686, 54070, 54424, 54779, 55163, 55518, 55902, 56256, 56610, 56993, 57348, 57702, 58086, 58441, 58795, 59179, 59533, 59917, 60271, 60626, 61010, 61364, 61719, 62103, 62457, 62841, 63195, 63549, 63933, 64288, 64642, 65026, 65381, 65735, 66119, 66473, 66857, 67211, 67566, 67950, 68304, 68659, 69042, 69396, 69780, 70134, 70489, 70873, 71228, 71582, 71966, 72320, 72674, 73058, 73412};
    public static final int DAY_OF_CHINESE_MONTH = 10;
    public static final int DAY_OF_CHINESE_YEAR = 13;
    public static final int DAY_OF_MONTH = 9;
    public static final int DAY_OF_WEEK = 14;
    public static final int DAY_OF_YEAR = 12;
    public static final int DECEMBER = 11;
    public static final int DETAIL_AM_PM = 16;
    public static final int DOG = 10;
    public static final int DRAGON = 4;
    public static final int DST_OFFSET = 24;
    public static final int EARLY_MORNING = 1;
    public static final int ERA = 0;
    public static final int EVENING = 5;
    public static final int FEBRUARY = 1;
    public static final int FIELD_COUNT = 25;
    private static final String[] FIELD_NAMES = {"ERA", "YEAR", "CHINESE_YEAR", "CHINESE_YEAR_SYMBOL_ANIMAL", "CHINESE_ERA_YEAR", "MONTH", "CHINESE_MONTH", "CHINESE_ERA_MONTH", "CHINESE_MONTH_IS_LEAP", "DAY_OF_MONTH", "DAY_OF_CHINESE_MONTH", "CHINESE_ERA_DAY", "DAY_OF_YEAR", "DAY_OF_CHINESE_YEAR", "DAY_OF_WEEK", "SOLAR_TERM", "DETAIL_AM_PM", "AM_PM", "HOUR", "CHINESE_ERA_HOUR", "MINUTE", "SECOND", "MILLISECOND", "ZONE_OFFSET", "DST_OFFSET"};
    private static final int[] FORMAT_CHARACTERS = {3, -1, -1, 12, 14, 26, 0, 18, 19, -1, 18, 5, 5, 7, -1, -1, -1, -1, 22, -1, -1, -1, 26, -1, 4, 25, -1, -1, -1, -1, -1, -1, 16, -1, 14, 9, 7, -1, -1, 18, -1, -1, 18, -1, 20, -1, -1, -1, -1, -1, 21, 15, -1, -1, 26, -1, 1, 25};
    public static final int FRIDAY = 6;
    public static final int GRAIN_BUDS = 10;
    public static final int GRAIN_IN_EAR = 11;
    public static final int GRAIN_RAIN = 8;
    public static final int GREAT_COLD = 2;
    public static final int GREAT_HEAT = 14;
    public static final int HEAVY_SNOW = 23;
    public static final int HOAR_FROST_FALLS = 20;
    public static final int HORSE = 6;
    public static final int HOUR = 18;
    public static final int INSECTS_AWAKEN = 5;
    public static final int IS_CHINESE_LEAP_MONTH = 1;
    public static final int JANUARY = 0;
    public static final int JULY = 6;
    public static final int JUNE = 5;
    public static final int LIGHT_SNOW = 22;
    public static final int MARCH = 2;
    private static final long MAX_CHINESE_CALENDAR_MILLISECOND = 4136400000000L;
    private static final int MAX_CHINESE_YEAR = 2100;
    public static final int MAY = 4;
    public static final int MIDNIGHT = 0;
    public static final int MILLISECOND = 22;
    public static final int MILLISECOND_OF_DAY = 86400000;
    public static final int MILLISECOND_OF_HOUR = 3600000;
    public static final int MILLISECOND_OF_MINUTE = 60000;
    public static final int MILLISECOND_OF_SECOND = 1000;
    public static final int MINUTE = 20;
    private static final long MIN_CHINESE_CALENDAR_MILLISECOND = -2206396800000L;
    private static final int MIN_CHINESE_YEAR = 1900;
    public static final int MONDAY = 2;
    public static final int MONKEY = 8;
    public static final int MONTH = 5;
    public static final int MORNING = 2;
    public static final int MOUSE = 0;
    public static final int NIGHT = 6;
    public static final int NOON = 3;
    public static final int NOT_CHINESE_LEAP_MONTH = 0;
    public static final int NOVEMBER = 10;
    public static final int NO_SOLAR_TERM = 0;
    public static final int OCTOBER = 9;
    public static final int PIG = 11;
    public static final int PM = 1;
    public static final int RABBIT = 3;
    public static final int SATURDAY = 7;
    public static final int SECOND = 21;
    public static final int SEPTEMBER = 8;
    public static final int SHEEP = 7;
    public static final int SLIGHT_COLD = 1;
    public static final int SLIGHT_HEAT = 13;
    public static final int SNAKE = 5;
    public static final int SOLAR_TERM = 15;
    private static final int[] SOLAR_TERM_BASE = {4, 19, 3, 18, 4, 19, 4, 19, 4, 20, 4, 20, 6, 22, 6, 22, 6, 22, 7, 22, 6, 21, 6, 21};
    private static final byte[] SOLAR_TERM_INDEX = "0123415341536789:;<9:=<>:=1>?012@015@015@015AB78CDE8CD=1FD01GH01GH01IH01IJ0KLMN;LMBEOPDQRST0RUH0RVH0RWH0RWM0XYMNZ[MB\\]PT^_ST`_WH`_WH`_WM`_WM`aYMbc[Mde]Sfe]gfh_gih_Wih_WjhaWjka[jkl[jmn]ope]qph_qrh_sth_W".getBytes();
    private static final byte[] SOLAR_TERM_OS = "211122112122112121222211221122122222212222222221222122222232222222222222222233223232223232222222322222112122112121222211222122222222222222222222322222112122112121222111211122122222212221222221221122122222222222222222222223222232222232222222222222112122112121122111211122122122212221222221221122122222222222222221211122112122212221222211222122222232222232222222222222112122112121111111222222112121112121111111222222111121112121111111211122112122112121122111222212111121111121111111111122112122112121122111211122112122212221222221222211111121111121111111222111111121111111111111111122112121112121111111222111111111111111111111111122111121112121111111221122122222212221222221222111011111111111111111111122111121111121111111211122112122112121122211221111011111101111111111111112111121111121111111211122112122112221222211221111011111101111111110111111111121111111111111111122112121112121122111111011111121111111111111111011111111112111111111111011111111111111111111221111011111101110111110111011011111111111111111221111011011101110111110111011011111101111111111211111001011101110111110110011011111101111111111211111001011001010111110110011011111101111111110211111001011001010111100110011011011101110111110211111001011001010011100110011001011101110111110211111001010001010011000100011001011001010111110111111001010001010011000111111111111111111111111100011001011001010111100111111001010001010000000111111000010000010000000100011001011001010011100110011001011001110111110100011001010001010011000110011001011001010111110111100000010000000000000000011001010001010011000111100000000000000000000000011001010001010000000111000000000000000000000000011001010000010000000".getBytes();
    public static final int SPRING_BEGINS = 3;
    public static final int STOPPING_THE_HEAT = 16;
    public static final int SUMMER_BEGINS = 9;
    public static final int SUMMER_SOLSTICE = 12;
    public static final int SUNDAY = 1;
    public static final int THE_RAINS = 4;
    public static final int THURSDAY = 5;
    public static final int TIGER = 2;
    public static final int TUESDAY = 3;
    public static final int VERNAL_EQUINOX = 6;
    public static final int WEDNESDAY = 4;
    public static final int WHITE_DEWS = 17;
    public static final int WINTER_BEGINS = 21;
    public static final int WINTER_SOLSTICE = 24;
    public static final int YEAR = 1;
    public static final int ZONE_OFFSET = 23;
    private static final long defaultGregorianCutover = -12219292800000L;
    private static final long serialVersionUID = 1;
    private transient int changeYear;
    private transient int currentYearSkew;
    private transient long gregorianCutover;
    private transient int julianError;
    private transient int julianSkew;
    private transient int lastYearSkew;
    private int[] mFields;
    private long mMillisecond;
    private TimeZone mTimeZone;

    public Calendar() {
        this(null);
    }

    public Calendar(TimeZone timeZone) {
        this.mFields = new int[25];
        this.gregorianCutover = defaultGregorianCutover;
        this.changeYear = 1582;
        int i = this.changeYear;
        this.julianError = ((i / 100) - (i / 400)) - 2;
        this.julianSkew = (((i - 2000) / 400) + this.julianError) - ((i - 2000) / 100);
        this.currentYearSkew = 10;
        this.lastYearSkew = 0;
        this.mMillisecond = System.currentTimeMillis();
        setTimeZone(timeZone);
    }

    private void appendNumericTimeZone(StringBuilder sb, boolean z, boolean z2) {
        char c;
        int i = get(23) + get(24);
        if (i < 0) {
            c = '-';
            i = -i;
        } else {
            c = '+';
        }
        if (z) {
            sb.append("GMT");
        }
        sb.append(c);
        appendNumericValue(sb, 2, i / MILLISECOND_OF_HOUR);
        if (z2) {
            sb.append(Util.ENABLED_ACCESSIBILITY_SERVICES_SEPARATOR);
        }
        appendNumericValue(sb, 2, (i % MILLISECOND_OF_HOUR) / 60000);
    }

    private static void appendNumericValue(StringBuilder sb, int i, int i2) {
        sb.append(SimpleNumberFormatter.format(i, i2));
    }

    private void appendTimeZone(StringBuilder sb, CalendarFormatSymbols calendarFormatSymbols, int i) {
        TimeZone timeZone = this.mTimeZone;
        int i2 = 1;
        boolean z = this.mFields[24] != 0;
        if (i != 4) {
            i2 = 0;
        }
        String displayName = timeZone.getDisplayName(z, i2, calendarFormatSymbols.getLocale());
        if (displayName != null) {
            sb.append(displayName);
        } else {
            appendNumericTimeZone(sb, false, false);
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:51:0x011a, code lost:
        if (r14 != 4) goto L_0x0129;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x011c, code lost:
        r10 = r12.getWeekDays()[r10.mFields[14] - 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:53:0x0129, code lost:
        if (r14 != 5) goto L_0x0138;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:0x012b, code lost:
        r10 = r12.getShortestWeekDays()[r10.mFields[14] - 1];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x0138, code lost:
        r10 = r12.getShortWeekDays()[r10.mFields[14] - 1];
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void appendValue(StringBuilder sb, CalendarFormatSymbols calendarFormatSymbols, char c, int i, int i2) {
        String str;
        int i3;
        if (c != 'A') {
            if (c != 'S') {
                if (c == 'W') {
                    return;
                }
                if (c != 'a') {
                    if (c == 'h') {
                        i3 = this.mFields[18] % 12;
                        if (i3 == 0) {
                            i3 = 12;
                        }
                    } else if (c == 'k') {
                        i3 = this.mFields[18];
                    } else if (c != 'm') {
                        if (c == 'w') {
                            return;
                        }
                        if (c != 'Y') {
                            if (c != 'Z') {
                                if (c != 's') {
                                    if (c == 't') {
                                        str = calendarFormatSymbols.getSolarTerms()[this.mFields[15]];
                                    } else if (c == 'y') {
                                        int[] iArr = this.mFields;
                                        i3 = i == 2 ? iArr[1] % 100 : iArr[1];
                                    } else if (c != 'z') {
                                        switch (c) {
                                            case 'D':
                                            case 'H':
                                                break;
                                            case 'E':
                                                break;
                                            case 'F':
                                                return;
                                            case 'G':
                                                str = calendarFormatSymbols.getEras()[this.mFields[0]];
                                                break;
                                            case 'I':
                                                if (i == 2) {
                                                    sb.append(calendarFormatSymbols.getHeavenlyStems()[this.mFields[19] % 10]);
                                                }
                                                str = calendarFormatSymbols.getEarthlyBranches()[this.mFields[19] % 12];
                                                break;
                                            default:
                                                switch (c) {
                                                    case 'K':
                                                        i3 = this.mFields[18] % 12;
                                                        break;
                                                    case 'L':
                                                    case 'M':
                                                        if (i >= 3) {
                                                            if (i != 4) {
                                                                if (i != 5) {
                                                                    str = calendarFormatSymbols.getShortMonths()[this.mFields[5]];
                                                                    break;
                                                                } else {
                                                                    str = calendarFormatSymbols.getShortestMonths()[this.mFields[5]];
                                                                    break;
                                                                }
                                                            } else {
                                                                str = calendarFormatSymbols.getMonths()[this.mFields[5]];
                                                                break;
                                                            }
                                                        } else {
                                                            i3 = this.mFields[5] + 1;
                                                            break;
                                                        }
                                                    case 'N':
                                                        if (i == 2) {
                                                            sb.append(calendarFormatSymbols.getHeavenlyStems()[this.mFields[7] % 10]);
                                                            str = calendarFormatSymbols.getEarthlyBranches()[this.mFields[7] % 12];
                                                            break;
                                                        } else {
                                                            sb.append(calendarFormatSymbols.getChineseLeapMonths()[this.mFields[8]]);
                                                            str = calendarFormatSymbols.getChineseMonths()[this.mFields[6]];
                                                            break;
                                                        }
                                                    default:
                                                        switch (c) {
                                                            case 'c':
                                                                break;
                                                            case 'd':
                                                                break;
                                                            case 'e':
                                                                if (i == 2) {
                                                                    sb.append(calendarFormatSymbols.getHeavenlyStems()[this.mFields[11] % 10]);
                                                                    str = calendarFormatSymbols.getEarthlyBranches()[this.mFields[11] % 12];
                                                                    break;
                                                                } else {
                                                                    str = calendarFormatSymbols.getChineseDays()[this.mFields[10] - 1];
                                                                    break;
                                                                }
                                                            default:
                                                                return;
                                                        }
                                                }
                                        }
                                    } else {
                                        appendTimeZone(sb, calendarFormatSymbols, i);
                                        return;
                                    }
                                }
                            } else if (i == 4) {
                                appendNumericTimeZone(sb, true, true);
                                return;
                            } else if (i == 5) {
                                appendNumericTimeZone(sb, false, true);
                                return;
                            } else {
                                appendNumericTimeZone(sb, false, false);
                                return;
                            }
                        } else if (i != 2) {
                            String[] chineseDigits = calendarFormatSymbols.getChineseDigits();
                            int i4 = this.mFields[2];
                            int length = sb.length();
                            while (i4 > 0) {
                                int i5 = i4 % 10;
                                i4 /= 10;
                                sb.insert(length, chineseDigits[i5]);
                            }
                            return;
                        } else {
                            sb.append(calendarFormatSymbols.getHeavenlyStems()[this.mFields[4] % 10]);
                            str = calendarFormatSymbols.getEarthlyBranches()[this.mFields[4] % 12];
                        }
                    }
                    appendNumericValue(sb, i, i3);
                    return;
                }
                str = i != 2 ? calendarFormatSymbols.getAmPms()[this.mFields[17]] : calendarFormatSymbols.getDetailedAmPms()[this.mFields[16]];
            }
            i3 = this.mFields[i2];
            appendNumericValue(sb, i, i3);
            return;
        }
        str = calendarFormatSymbols.getChineseSymbolAnimals()[this.mFields[3]];
        sb.append(str);
    }

    private void compute() {
        long computeDateTime = computeDateTime();
        if (!outOfChineseCalendarRange()) {
            computeChineseDate(computeDateTime);
            computeSolarTerm();
            computeChineseEraNames(computeDateTime);
        }
    }

    private void computeChineseDate(long j) {
        int i;
        int i2 = (int) (j - -25537);
        int[] iArr = this.mFields;
        int i3 = iArr[1];
        int i4 = MAX_CHINESE_YEAR;
        if (i3 < MAX_CHINESE_YEAR) {
            i4 = iArr[1] + 1;
        }
        int i5 = i2 - DAY_CHINESE_YEAR_FROM_19000101[i4 - 1900];
        if (i5 < 0) {
            i4--;
            i5 += daysInChineseYear(i4);
        }
        if (i5 < 0) {
            i4--;
            i5 += daysInChineseYear(i4);
        }
        int[] iArr2 = this.mFields;
        iArr2[2] = i4;
        iArr2[13] = i5 + 1;
        int leapChineseMonth = leapChineseMonth(i4);
        int i6 = 0;
        int i7 = i5;
        int i8 = 0;
        int i9 = 0;
        int i10 = 0;
        while (i < 12 && i7 > 0) {
            if (leapChineseMonth >= 0 && i == leapChineseMonth + 1 && i9 == 0) {
                i--;
                i10 = leapDaysInChineseYear(i4);
                i9 = 1;
            } else {
                i10 = daysInChineseMonth(i4, i);
            }
            if (i9 != 0 && i == leapChineseMonth + 1) {
                i9 = 0;
            }
            i7 -= i10;
            i8 = i + 1;
        }
        if (i7 != 0 || leapChineseMonth <= 0 || i != leapChineseMonth + 1) {
            i6 = i9;
        } else if (i9 == 0) {
            i--;
            i6 = 1;
        }
        if (i7 < 0) {
            i7 += i10;
            i--;
        }
        int[] iArr3 = this.mFields;
        iArr3[8] = i6;
        iArr3[6] = i;
        iArr3[10] = i7 + 1;
    }

    private void computeChineseEraNames(long j) {
        int[] iArr = this.mFields;
        int i = iArr[1];
        if (iArr[5] < 2 && (iArr[6] >= 2 || iArr[13] < 1)) {
            i--;
        }
        int i2 = i - 1900;
        this.mFields[3] = mod((long) (i2 + 12), 12);
        this.mFields[4] = mod((long) (i2 + 36), 60);
        int[] iArr2 = this.mFields;
        int solarTermDaysOfMonth = solarTermDaysOfMonth(iArr2[1], iArr2[5]) >> 8;
        int[] iArr3 = this.mFields;
        int i3 = ((iArr3[1] - 1900) * 12) + iArr3[5];
        if (iArr3[9] >= solarTermDaysOfMonth) {
            i3++;
        }
        this.mFields[7] = mod((long) (i3 + 12), 60);
        int i4 = (int) (j - -25537);
        this.mFields[11] = mod((long) (i4 + 40), 60);
        int[] iArr4 = this.mFields;
        iArr4[19] = mod((long) ((i4 * 12) + (((iArr4[18] + 1) % 24) / 2)), 60);
    }

    private void computeDate(long j, long j2) {
        int computeYearAndDay = computeYearAndDay(j, j2);
        int[] iArr = this.mFields;
        iArr[12] = computeYearAndDay;
        if (iArr[1] == this.changeYear && this.gregorianCutover <= j2) {
            computeYearAndDay += this.currentYearSkew;
        }
        int i = computeYearAndDay / 32;
        boolean isLeapYear = isLeapYear(this.mFields[1]);
        int daysInYear = computeYearAndDay - daysInYear(isLeapYear, i);
        if (daysInYear > daysInMonth(isLeapYear, i)) {
            daysInYear -= daysInMonth(isLeapYear, i);
            i++;
        }
        int[] iArr2 = this.mFields;
        iArr2[5] = i;
        iArr2[9] = daysInYear;
        iArr2[14] = mod(j - 3, 7) + 1;
    }

    private long computeDateTime() {
        int i;
        long j;
        this.mFields[23] = this.mTimeZone.getRawOffset();
        long j2 = this.mMillisecond;
        long j3 = j2 / 86400000;
        int i2 = (int) (j2 % 86400000);
        if (i2 < 0) {
            i2 += MILLISECOND_OF_DAY;
            j3--;
        }
        int i3 = i2 + this.mFields[23];
        while (i3 < 0) {
            i3 += MILLISECOND_OF_DAY;
            j3--;
        }
        while (i >= 86400000) {
            i3 = i - MILLISECOND_OF_DAY;
            j3++;
        }
        int i4 = this.mFields[23];
        long j4 = this.mMillisecond;
        long j5 = ((long) i4) + j4;
        if (j4 > 0 && j5 < 0 && i4 > 0) {
            j5 = Long.MAX_VALUE;
        } else if (this.mMillisecond < 0 && j5 > 0 && i4 < 0) {
            j5 = Long.MIN_VALUE;
        }
        computeDate(j3, j5);
        int dstOffset = getDstOffset(i);
        this.mFields[24] = dstOffset;
        if (dstOffset != 0) {
            i += dstOffset;
            if (i < 0) {
                i += MILLISECOND_OF_DAY;
                j = j3 - 1;
            } else if (i >= 86400000) {
                i -= MILLISECOND_OF_DAY;
                j = 1 + j3;
            } else {
                j = j3;
            }
            if (j3 != j) {
                int[] iArr = this.mFields;
                int i5 = iArr[24] - iArr[23];
                long j6 = this.mMillisecond;
                long j7 = ((long) i5) + j6;
                if (j6 > 0 && j7 < 0 && i5 > 0) {
                    j7 = Long.MAX_VALUE;
                } else if (this.mMillisecond < 0 && j7 > 0 && i5 < 0) {
                    j7 = Long.MIN_VALUE;
                }
                computeDate(j, j7);
            }
        } else {
            j = j3;
        }
        int[] iArr2 = this.mFields;
        if (iArr2[1] <= 0) {
            iArr2[0] = 0;
            iArr2[1] = 1 - iArr2[1];
        } else {
            iArr2[0] = 1;
        }
        int[] iArr3 = this.mFields;
        iArr3[22] = i % 1000;
        int i6 = i / 1000;
        iArr3[21] = i6 % 60;
        int i7 = i6 / 60;
        iArr3[20] = i7 % 60;
        iArr3[18] = (i7 / 60) % 24;
        iArr3[17] = iArr3[18] > 11 ? 1 : 0;
        int[] iArr4 = this.mFields;
        switch (iArr4[18]) {
            case 0:
                iArr4[16] = 0;
                break;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                iArr4[16] = 1;
                break;
            case 6:
            case 7:
            case 8:
            case 9:
            case 10:
            case 11:
                iArr4[16] = 2;
                break;
            case 12:
                iArr4[16] = 3;
                break;
            case 13:
            case 14:
            case 15:
            case 16:
            case 17:
                iArr4[16] = 4;
                break;
            case 18:
                iArr4[16] = 5;
                break;
            case 19:
            case 20:
            case 21:
            case 22:
            case 23:
                iArr4[16] = 6;
                break;
        }
        return j;
    }

    private void computeSolarTerm() {
        int[] iArr = this.mFields;
        int solarTermDaysOfMonth = solarTermDaysOfMonth(iArr[1], iArr[5]);
        int[] iArr2 = this.mFields;
        if (iArr2[9] == (solarTermDaysOfMonth >> 8)) {
            iArr2[15] = (iArr2[5] * 2) + 1;
        } else if (iArr2[9] == (solarTermDaysOfMonth & 255)) {
            iArr2[15] = (iArr2[5] * 2) + 2;
        } else {
            iArr2[15] = 0;
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:10:0x0023  */
    /* JADX WARNING: Removed duplicated region for block: B:7:0x0016  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private int computeYearAndDay(long j, long j2) {
        long j3;
        int i;
        long daysFromBaseYear;
        int i2 = 1970;
        if (j2 < this.gregorianCutover) {
            daysFromBaseYear = (long) this.julianSkew;
        } else {
            j3 = j;
            i = (int) (j3 / 365);
            if (i == 0) {
                i2 += i;
                daysFromBaseYear = daysFromBaseYear((long) i2);
            }
            if (j3 < 0) {
                i2--;
                j3 += (long) daysInYear(i2);
            }
            this.mFields[1] = i2;
            return ((int) j3) + 1;
        }
        j3 = j - daysFromBaseYear;
        i = (int) (j3 / 365);
        if (i == 0) {
        }
        if (j3 < 0) {
        }
        this.mFields[1] = i2;
        return ((int) j3) + 1;
    }

    private long daysFromBaseYear(long j) {
        long j2;
        if (j >= 1970) {
            long j3 = ((j - 1970) * 365) + ((j - 1969) / 4);
            int i = this.changeYear;
            if (j > ((long) i)) {
                j2 = j3 - (((j - 1901) / 100) - ((j - 1601) / 400));
            } else {
                int i2 = j == ((long) i) ? this.currentYearSkew : j == ((long) (i + -1)) ? this.lastYearSkew : this.julianSkew;
                j2 = j3 + ((long) i2);
            }
            return j2;
        }
        long j4 = (j - 1970) * 365;
        if (j <= ((long) this.changeYear)) {
            return j4 + ((j - 1972) / 4) + ((long) this.julianSkew);
        }
        long j5 = j - 2000;
        return ((j4 + ((j - 1972) / 4)) - (j5 / 100)) + (j5 / 400);
    }

    static int daysInChineseMonth(int i, int i2) {
        return (CHINESE_YEAR_INFO[i + -1900] & (65536 >> (i2 + 1))) != 0 ? 30 : 29;
    }

    static int daysInChineseYear(int i) {
        int[] iArr = DAY_CHINESE_YEAR_FROM_19000101;
        int i2 = i - 1900;
        return iArr[i2 + 1] - iArr[i2];
    }

    private static int daysInMonth(boolean z, int i) {
        return (!z || i != 1) ? DAYS_IN_MONTH[i] : DAYS_IN_MONTH[i] + 1;
    }

    private int daysInYear(int i) {
        int i2 = isLeapYear(i) ? m.cV : m.cU;
        if (i == this.changeYear) {
            i2 -= this.currentYearSkew;
        }
        return i == this.changeYear + -1 ? i2 - this.lastYearSkew : i2;
    }

    private static int daysInYear(boolean z, int i) {
        return (!z || i <= 1) ? DAYS_IN_YEAR[i] : DAYS_IN_YEAR[i] + 1;
    }

    private int getDstOffset(int i) {
        int[] iArr = this.mFields;
        int offset = iArr[1] <= 0 ? 0 : this.mTimeZone.getOffset(1, iArr[1], iArr[5], iArr[9], iArr[14], i);
        int[] iArr2 = this.mFields;
        if (iArr2[1] > 0) {
            return offset - iArr2[23];
        }
        return 0;
    }

    private static int leapChineseMonth(int i) {
        int i2 = CHINESE_YEAR_INFO[i - 1900] & 15;
        if (i2 == 15) {
            return -1;
        }
        return i2 - 1;
    }

    static int leapDaysInChineseYear(int i) {
        if (leapChineseMonth(i) >= 0) {
            return (CHINESE_YEAR_INFO[(i + -1900) + 1] & 15) == 15 ? 30 : 29;
        }
        return 0;
    }

    private static int mod(long j, int i) {
        int i2 = (int) (j % ((long) i));
        return (j >= 0 || i2 >= 0) ? i2 : i2 + i;
    }

    private void onChineseDateChange() {
        int[] iArr;
        int leapDaysInChineseYear;
        long j = ((long) DAY_CHINESE_YEAR_FROM_19000101[this.mFields[2] - 1900]) - 25537;
        int i = 0;
        while (true) {
            iArr = this.mFields;
            if (i >= iArr[6]) {
                break;
            }
            j += (long) daysInChineseMonth(iArr[2], i);
            i++;
        }
        if (iArr[8] == 1) {
            leapDaysInChineseYear = daysInChineseMonth(iArr[2], iArr[6]);
        } else {
            int leapChineseMonth = leapChineseMonth(iArr[2]);
            if (leapChineseMonth >= 0) {
                int[] iArr2 = this.mFields;
                if (leapChineseMonth < iArr2[6]) {
                    leapDaysInChineseYear = leapDaysInChineseYear(iArr2[2]);
                }
            }
            computeDate(j + ((long) (this.mFields[10] - 1)), 0);
            onSolarDateChange();
        }
        j += (long) leapDaysInChineseYear;
        computeDate(j + ((long) (this.mFields[10] - 1)), 0);
        onSolarDateChange();
    }

    private void onSolarDateChange() {
        int[] iArr = this.mFields;
        int i = iArr[1];
        int i2 = iArr[5];
        int i3 = iArr[9];
        long daysFromBaseYear = daysFromBaseYear((long) i) + ((long) ((daysInYear(isLeapYear(i), i2) + i3) - 1));
        int mod = mod(daysFromBaseYear - 3, 7) + 1;
        this.mFields[14] = mod;
        int[] iArr2 = this.mFields;
        int i4 = iArr2[22] + (iArr2[18] * MILLISECOND_OF_HOUR) + (iArr2[20] * 60000) + (iArr2[21] * 1000);
        this.mMillisecond = (daysFromBaseYear * 86400000) + ((long) i4);
        long offset = (long) this.mTimeZone.getOffset(1, i, i2, i3, mod, i4);
        if (this.mTimeZone.inDaylightTime(new Date(this.mMillisecond))) {
            offset -= (long) getDstOffset(i4);
        }
        long j = this.mMillisecond;
        if (i <= 0) {
            offset = 0;
        }
        this.mMillisecond = j - offset;
        compute();
    }

    static int solarTermDaysOfMonth(int i, int i2) {
        if (i > MAX_CHINESE_YEAR) {
            return 0;
        }
        int i3 = i2 * 2;
        int i4 = ((SOLAR_TERM_INDEX[i - 1900] - 48) * 24) + i3;
        int i5 = i4 + 1;
        byte[] bArr = SOLAR_TERM_OS;
        int i6 = bArr[i4] - 48;
        int[] iArr = SOLAR_TERM_BASE;
        return ((i6 + iArr[i3]) << 8) + (bArr[i5] - 48) + iArr[i3 + 1];
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Calendar add(int i, int i2) {
        long j;
        boolean z;
        boolean z2;
        boolean z3;
        if (i < 0 || i >= 25) {
            StringBuilder sb = new StringBuilder();
            sb.append("Field out of range [0-25]: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        boolean z4 = false;
        if (i == 0) {
            if (i2 != 0) {
                int[] iArr = this.mFields;
                if (iArr[0] != (iArr[0] + i2) % 2 && iArr[0] == 0) {
                    iArr[1] = -1 - iArr[1];
                }
            }
            return this;
        } else if (i != 1) {
            String str = "out of range of Chinese Lunar Year";
            if (i != 2) {
                if (i != 5) {
                    if (i != 6) {
                        String str2 = "out of range";
                        if (i == 9 || i == 10 || i == 12 || i == 13) {
                            if (i2 != 0) {
                                j = this.mMillisecond + (((long) i2) * 86400000);
                                boolean z5 = i2 > 0;
                                if (j > this.mMillisecond) {
                                    z = true;
                                }
                                if (z5 != z) {
                                    throw new IllegalArgumentException(str2);
                                }
                            }
                        } else if (i != 18) {
                            switch (i) {
                                case 20:
                                    if (i2 != 0) {
                                        j = this.mMillisecond + (((long) i2) * 60000);
                                        boolean z6 = i2 > 0;
                                        if (j > this.mMillisecond) {
                                            z3 = true;
                                        }
                                        if (z6 != z3) {
                                            throw new IllegalArgumentException(str2);
                                        }
                                    }
                                    break;
                                case 21:
                                    if (i2 != 0) {
                                        j = this.mMillisecond + (((long) i2) * 1000);
                                        boolean z7 = i2 > 0;
                                        if (j > this.mMillisecond) {
                                            z4 = true;
                                        }
                                        if (z7 != z4) {
                                            throw new IllegalArgumentException(str2);
                                        }
                                    }
                                    break;
                                case 22:
                                    if (i2 != 0) {
                                        j = this.mMillisecond + ((long) i2);
                                        boolean z8 = i2 > 0;
                                        if (j > this.mMillisecond) {
                                            z4 = true;
                                        }
                                        if (z8 != z4) {
                                            throw new IllegalArgumentException(str2);
                                        }
                                    }
                                    break;
                                default:
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("unsupported set field:");
                                    sb2.append(FIELD_NAMES[i]);
                                    throw new IllegalArgumentException(sb2.toString());
                            }
                        } else if (i2 != 0) {
                            j = this.mMillisecond + (((long) i2) * 3600000);
                            boolean z9 = i2 > 0;
                            if (j > this.mMillisecond) {
                                z2 = true;
                            }
                            if (z9 != z2) {
                                throw new IllegalArgumentException(str2);
                            }
                        }
                        this.mMillisecond = j;
                        compute();
                    } else if (i2 != 0) {
                        if (!outOfChineseCalendarRange()) {
                            int leapChineseMonth = leapChineseMonth(this.mFields[2]);
                            while (i2 > 0) {
                                int[] iArr2 = this.mFields;
                                if (iArr2[6] == leapChineseMonth && iArr2[8] == 0) {
                                    iArr2[8] = 1;
                                } else {
                                    int[] iArr3 = this.mFields;
                                    iArr3[6] = iArr3[6] + 1;
                                    iArr3[8] = 0;
                                    if (iArr3[6] > 11) {
                                        iArr3[6] = 0;
                                        iArr3[2] = iArr3[2] + 1;
                                        if (iArr3[2] <= MAX_CHINESE_YEAR) {
                                            leapChineseMonth = leapChineseMonth(iArr3[2]);
                                        } else {
                                            throw new IllegalArgumentException(str);
                                        }
                                    } else {
                                        continue;
                                    }
                                }
                                i2--;
                            }
                            while (i2 < 0) {
                                int[] iArr4 = this.mFields;
                                if (iArr4[6] == leapChineseMonth && iArr4[8] == 1) {
                                    iArr4[8] = 0;
                                } else {
                                    int[] iArr5 = this.mFields;
                                    iArr5[6] = iArr5[6] - 1;
                                    if (iArr5[6] < 0) {
                                        iArr5[6] = 11;
                                        iArr5[6] = iArr5[6] - 1;
                                        if (iArr5[2] >= MIN_CHINESE_YEAR) {
                                            leapChineseMonth = leapChineseMonth(iArr5[1]);
                                        } else {
                                            throw new IllegalArgumentException(str);
                                        }
                                    }
                                    int[] iArr6 = this.mFields;
                                    if (iArr6[6] == leapChineseMonth) {
                                        iArr6[8] = 1;
                                    }
                                }
                                i2++;
                            }
                            int[] iArr7 = this.mFields;
                            int leapDaysInChineseYear = iArr7[8] == 1 ? leapDaysInChineseYear(iArr7[2]) : daysInChineseMonth(iArr7[2], iArr7[6]);
                            int[] iArr8 = this.mFields;
                            if (iArr8[10] > leapDaysInChineseYear) {
                                iArr8[10] = leapDaysInChineseYear;
                            }
                        } else {
                            throw new IllegalArgumentException(str);
                        }
                    }
                } else if (i2 != 0) {
                    int i3 = i2 + this.mFields[5];
                    int i4 = i3 / 12;
                    int i5 = i3 % 12;
                    if (i5 < 0) {
                        i5 += 12;
                        i4--;
                    }
                    int[] iArr9 = this.mFields;
                    iArr9[5] = i5;
                    if (i4 == 0) {
                        if (iArr9[0] == 0) {
                            iArr9[1] = -1 - iArr9[1];
                        }
                        int daysInMonth = daysInMonth(isLeapYear(this.mFields[1]), this.mFields[5]);
                        int[] iArr10 = this.mFields;
                        if (iArr10[9] > daysInMonth) {
                            iArr10[9] = daysInMonth;
                        }
                    } else {
                        add(1, i4);
                    }
                }
                return this;
            }
            if (i2 != 0) {
                int i6 = i2 + this.mFields[2];
                if (outOfChineseCalendarRange() || i6 < MIN_CHINESE_YEAR || i6 > MAX_CHINESE_YEAR) {
                    throw new IllegalArgumentException(str);
                }
                int[] iArr11 = this.mFields;
                iArr11[2] = i6;
                if (iArr11[8] == 1 && iArr11[6] == leapChineseMonth(iArr11[2])) {
                    this.mFields[8] = 0;
                }
                int[] iArr12 = this.mFields;
                int leapDaysInChineseYear2 = iArr12[8] == 1 ? leapDaysInChineseYear(iArr12[2]) : daysInChineseMonth(iArr12[2], iArr12[6]);
                int[] iArr13 = this.mFields;
                if (iArr13[10] > leapDaysInChineseYear2) {
                    iArr13[10] = leapDaysInChineseYear2;
                }
            }
            return this;
            onChineseDateChange();
            return this;
        } else {
            if (i2 != 0) {
                int[] iArr14 = this.mFields;
                if (iArr14[0] == 0) {
                    iArr14[1] = -1 - iArr14[1];
                }
                int[] iArr15 = this.mFields;
                iArr15[1] = iArr15[1] + i2;
                int daysInMonth2 = daysInMonth(isLeapYear(iArr15[1]), this.mFields[5]);
                int[] iArr16 = this.mFields;
                if (iArr16[9] > daysInMonth2) {
                    iArr16[9] = daysInMonth2;
                }
            }
            return this;
        }
        onSolarDateChange();
        return this;
    }

    public boolean after(Calendar calendar) {
        return getTimeInMillis() > calendar.getTimeInMillis();
    }

    public boolean before(Calendar calendar) {
        return getTimeInMillis() < calendar.getTimeInMillis();
    }

    public final Object clone() {
        try {
            Calendar calendar = (Calendar) super.clone();
            calendar.mFields = (int[]) this.mFields.clone();
            calendar.mTimeZone = (TimeZone) this.mTimeZone.clone();
            return calendar;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException(e);
        }
    }

    public int compareTo(Calendar calendar) {
        long j = this.mMillisecond;
        long j2 = calendar.mMillisecond;
        if (j < j2) {
            return -1;
        }
        return j == j2 ? 0 : 1;
    }

    public boolean equals(Object obj) {
        return obj == this || ((obj instanceof Calendar) && this.mMillisecond == ((Calendar) obj).mMillisecond);
    }

    public String format(CharSequence charSequence) {
        return format(charSequence, (CalendarFormatSymbols) null);
    }

    public String format(CharSequence charSequence, CalendarFormatSymbols calendarFormatSymbols) {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        String sb2 = format(sb, charSequence, calendarFormatSymbols).toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }

    public StringBuilder format(StringBuilder sb, CharSequence charSequence) {
        return format(sb, charSequence, null);
    }

    public StringBuilder format(StringBuilder sb, CharSequence charSequence, CalendarFormatSymbols calendarFormatSymbols) {
        int i;
        if (calendarFormatSymbols == null) {
            calendarFormatSymbols = CalendarFormatSymbols.getDefault();
        }
        int length = charSequence.length();
        int i2 = 0;
        boolean z = false;
        while (i2 < length) {
            char charAt = charSequence.charAt(i2);
            if (!z) {
                if (charAt == '\'') {
                    i = i2 + 1;
                    if (i >= length || charSequence.charAt(i) != charAt) {
                        z = true;
                    }
                    sb.append(charAt);
                    i2 = i;
                } else if (charAt >= 'A' && charAt <= 'z') {
                    int i3 = charAt - 'A';
                    if (FORMAT_CHARACTERS[i3] >= 0) {
                        int i4 = i2;
                        int i5 = 1;
                        while (true) {
                            int i6 = i4 + 1;
                            if (i6 >= length || charSequence.charAt(i6) != charAt) {
                                appendValue(sb, calendarFormatSymbols, charAt, i5, FORMAT_CHARACTERS[i3]);
                                i2 = i4;
                            } else {
                                i5++;
                                i4 = i6;
                            }
                        }
                        appendValue(sb, calendarFormatSymbols, charAt, i5, FORMAT_CHARACTERS[i3]);
                        i2 = i4;
                    }
                }
                i2++;
            } else if (charAt == '\'') {
                i = i2 + 1;
                if (i >= length || charSequence.charAt(i) != charAt) {
                    z = false;
                    i2++;
                }
                sb.append(charAt);
                i2 = i;
                i2++;
            }
            sb.append(charAt);
            i2++;
        }
        return sb;
    }

    public int get(int i) {
        if (i >= 0 && i < 25) {
            return this.mFields[i];
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Field out of range [0-25]: ");
        sb.append(i);
        throw new IllegalArgumentException(sb.toString());
    }

    public int getActualMaximum(int i) {
        if (i < 0 || i >= 25) {
            StringBuilder sb = new StringBuilder();
            sb.append("Field out of range [0-25]: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        int i2 = 0;
        switch (i) {
            case 0:
                return 1;
            case 1:
                return this.mFields[0] == 1 ? 292278994 : 292269055;
            case 2:
                return MAX_CHINESE_YEAR;
            case 3:
                return 11;
            case 4:
            case 7:
            case 11:
            case 19:
                return 59;
            case 5:
            case 6:
                return 11;
            case 8:
                return 1;
            case 9:
                return daysInMonth(isLeapYear(this.mFields[1]), this.mFields[5]);
            case 10:
                if (!outOfChineseCalendarRange()) {
                    boolean isChineseLeapMonth = isChineseLeapMonth();
                    int[] iArr = this.mFields;
                    i2 = isChineseLeapMonth ? leapDaysInChineseYear(iArr[2]) : daysInChineseMonth(iArr[2], iArr[6]);
                }
                return i2;
            case 12:
                return daysInYear(this.mFields[1]);
            case 13:
                if (!outOfChineseCalendarRange()) {
                    i2 = daysInChineseYear(this.mFields[2]);
                }
                return i2;
            case 14:
                return 7;
            case 15:
                return 24;
            case 16:
                return 6;
            case 17:
                return 1;
            case 18:
                return 23;
            case 20:
            case 21:
                return 59;
            case 22:
                return ErrorCode.UNKNOWN;
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("unsupported field: ");
                sb2.append(FIELD_NAMES[i]);
                throw new IllegalArgumentException(sb2.toString());
        }
    }

    public int getActualMinimum(int i) {
        if (i < 0 || i >= 25) {
            StringBuilder sb = new StringBuilder();
            sb.append("Field out of range [0-25]: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        switch (i) {
            case 0:
                return 0;
            case 1:
                return 1;
            case 2:
                return MIN_CHINESE_YEAR;
            case 3:
            case 4:
            case 5:
            case 6:
            case 7:
            case 8:
            case 11:
            case 19:
                return 0;
            case 9:
                return 1;
            case 10:
                return outOfChineseCalendarRange() ^ true ? 1 : 0;
            case 12:
                return 1;
            case 13:
                return outOfChineseCalendarRange() ^ true ? 1 : 0;
            case 14:
                return 1;
            case 15:
            case 16:
            case 17:
            case 18:
            case 20:
            case 21:
            case 22:
                return 0;
            default:
                StringBuilder sb2 = new StringBuilder();
                sb2.append("unsupported field: ");
                sb2.append(FIELD_NAMES[i]);
                throw new IllegalArgumentException(sb2.toString());
        }
    }

    public int getChineseLeapMonth() {
        return leapChineseMonth(this.mFields[2]);
    }

    public long getTimeInMillis() {
        return this.mMillisecond;
    }

    public TimeZone getTimeZone() {
        return this.mTimeZone;
    }

    public int hashCode() {
        long j = this.mMillisecond;
        return (int) (j ^ (j >>> 32));
    }

    public boolean isChineseLeapMonth() {
        return this.mFields[8] == 1;
    }

    public boolean isLeapYear(int i) {
        boolean z = true;
        if (i > this.changeYear) {
            if (i % 4 != 0 || (i % 100 == 0 && i % 400 != 0)) {
                z = false;
            }
            return z;
        }
        if (i % 4 != 0) {
            z = false;
        }
        return z;
    }

    public boolean outOfChineseCalendarRange() {
        long j = this.mMillisecond;
        int[] iArr = this.mFields;
        return j < (MIN_CHINESE_CALENDAR_MILLISECOND - ((long) iArr[23])) - ((long) iArr[24]) || j >= (MAX_CHINESE_CALENDAR_MILLISECOND - ((long) iArr[23])) - ((long) iArr[24]);
    }

    public Calendar set(int i, int i2) {
        String str = "]: ";
        String str2 = "-";
        String str3 = "value is out of date range [";
        if (i == 6) {
            if (i2 < 0) {
                i2 = -i2;
                if (i2 == leapChineseMonth(this.mFields[2])) {
                    this.mFields[8] = 1;
                } else {
                    StringBuilder sb = new StringBuilder();
                    sb.append("year ");
                    sb.append(this.mFields[2]);
                    sb.append(" has no such leap month:");
                    sb.append(i2);
                    throw new IllegalArgumentException(sb.toString());
                }
            } else if (i2 >= getActualMinimum(i) || i2 <= getActualMaximum(i)) {
                this.mFields[8] = 0;
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str3);
                sb2.append(getActualMinimum(i));
                sb2.append(str2);
                sb2.append(getActualMaximum(i));
                sb2.append(str);
                sb2.append(i2);
                throw new IllegalArgumentException(sb2.toString());
            }
            int[] iArr = this.mFields;
            iArr[6] = i2;
            int leapDaysInChineseYear = iArr[8] == 1 ? leapDaysInChineseYear(iArr[2]) : daysInChineseMonth(iArr[2], iArr[6]);
            int[] iArr2 = this.mFields;
            if (iArr2[10] > leapDaysInChineseYear) {
                iArr2[10] = leapDaysInChineseYear;
            }
            onChineseDateChange();
            return this;
        } else if (i2 >= getActualMinimum(i) || i2 <= getActualMaximum(i)) {
            try {
                add(i, i2 - this.mFields[i]);
                return this;
            } catch (IllegalArgumentException unused) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("unsupported set field:");
                sb3.append(FIELD_NAMES[i]);
                throw new IllegalArgumentException(sb3.toString());
            }
        } else {
            StringBuilder sb4 = new StringBuilder();
            sb4.append(str3);
            sb4.append(getActualMinimum(i));
            sb4.append(str2);
            sb4.append(getActualMaximum(i));
            sb4.append(str);
            sb4.append(i2);
            throw new IllegalArgumentException(sb4.toString());
        }
    }

    public Calendar set(int i, int i2, int i3, int i4, int i5, int i6, int i7) {
        String str = "Year ";
        if (i2 < 0 || i2 > 11) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(i);
            sb.append(" has no month ");
            sb.append(i2);
            throw new IllegalArgumentException(sb.toString());
        } else if (i3 <= 0 || i3 > daysInMonth(isLeapYear(i), i2)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(i);
            sb2.append(" month ");
            sb2.append(i2);
            sb2.append(" has no day ");
            sb2.append(i3);
            throw new IllegalArgumentException(sb2.toString());
        } else if (i4 < 0 || i4 > 23) {
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Invalid hour ");
            sb3.append(i4);
            throw new IllegalArgumentException(sb3.toString());
        } else if (i5 < 0 || i5 > 59) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("Invalid minute ");
            sb4.append(i5);
            throw new IllegalArgumentException(sb4.toString());
        } else if (i6 < 0 || i6 > 59) {
            StringBuilder sb5 = new StringBuilder();
            sb5.append("Invalid second ");
            sb5.append(i6);
            throw new IllegalArgumentException(sb5.toString());
        } else if (i7 < 0 || i7 > 999) {
            StringBuilder sb6 = new StringBuilder();
            sb6.append("Invalid millisecond ");
            sb6.append(i7);
            throw new IllegalArgumentException(sb6.toString());
        } else {
            int[] iArr = this.mFields;
            iArr[1] = i;
            iArr[5] = i2;
            iArr[9] = i3;
            iArr[18] = i4;
            iArr[20] = i5;
            iArr[21] = i6;
            iArr[22] = i7;
            onSolarDateChange();
            return this;
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=boolean, code=int, for r8v0, types: [int, boolean] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public Calendar setChineseTime(int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        if (i < MIN_CHINESE_YEAR || i > MAX_CHINESE_YEAR) {
            StringBuilder sb = new StringBuilder();
            sb.append("Date out of range [1900 - 2100] expected, but year is ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
        String str = "Year ";
        if (i2 < 0 || i2 > 11) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append(str);
            sb2.append(i);
            sb2.append(" has no month ");
            sb2.append(i2);
            throw new IllegalArgumentException(sb2.toString());
        } else if (i4 == 0 || leapChineseMonth(i) == i2) {
            String str2 = " has no day ";
            String str3 = " month ";
            if (i4 != 0) {
                if (i3 <= 0 || i3 > leapDaysInChineseYear(i)) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(str);
                    sb3.append(i);
                    sb3.append(str3);
                    sb3.append(i2);
                    sb3.append(str2);
                    sb3.append(i3);
                    throw new IllegalArgumentException(sb3.toString());
                }
            } else if (i3 <= 0 || i3 > daysInChineseMonth(i, i2)) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str);
                sb4.append(i);
                sb4.append(str3);
                sb4.append(i2);
                sb4.append(str2);
                sb4.append(i3);
                throw new IllegalArgumentException(sb4.toString());
            }
            if (i5 < 0 || i5 > 23) {
                StringBuilder sb5 = new StringBuilder();
                sb5.append("Invalid hour ");
                sb5.append(i5);
                throw new IllegalArgumentException(sb5.toString());
            } else if (i6 < 0 || i6 > 59) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append("Invalid minute ");
                sb6.append(i6);
                throw new IllegalArgumentException(sb6.toString());
            } else if (i7 < 0 || i7 > 59) {
                StringBuilder sb7 = new StringBuilder();
                sb7.append("Invalid second ");
                sb7.append(i7);
                throw new IllegalArgumentException(sb7.toString());
            } else if (i8 < 0 || i8 > 1000) {
                StringBuilder sb8 = new StringBuilder();
                sb8.append("Invalid millisecond ");
                sb8.append(i8);
                throw new IllegalArgumentException(sb8.toString());
            } else {
                int[] iArr = this.mFields;
                iArr[2] = i;
                iArr[6] = i2;
                iArr[10] = i3;
                iArr[8] = i4;
                iArr[18] = i5;
                iArr[20] = i6;
                iArr[21] = i7;
                iArr[22] = i8;
                onChineseDateChange();
                return this;
            }
        } else {
            StringBuilder sb9 = new StringBuilder();
            sb9.append(str);
            sb9.append(i);
            sb9.append(" has no leap month ");
            sb9.append(i2);
            throw new IllegalArgumentException(sb9.toString());
        }
    }

    public void setGregorianChange(long j) {
        this.gregorianCutover = j;
        Calendar calendar = new Calendar(TimeZone.getTimeZone("GMT"));
        calendar.setTimeInMillis(j);
        this.changeYear = calendar.get(1);
        if (calendar.get(0) == 0) {
            this.changeYear = 1 - this.changeYear;
        }
        int i = this.changeYear;
        this.julianError = ((i / 100) - (i / 400)) - 2;
        this.julianSkew = (((i - 2000) / 400) + this.julianError) - ((i - 2000) / 100);
        int i2 = calendar.get(12);
        int i3 = this.julianSkew;
        if (i2 < i3) {
            this.currentYearSkew = i2 - 1;
            this.lastYearSkew = (i3 - i2) + 1;
            return;
        }
        this.lastYearSkew = 0;
        this.currentYearSkew = i3;
    }

    public Calendar setTimeInMillis(long j) {
        this.mMillisecond = j;
        compute();
        return this;
    }

    public Calendar setTimeZone(TimeZone timeZone) {
        if (timeZone == null) {
            timeZone = TimeZone.getDefault();
        }
        TimeZone timeZone2 = this.mTimeZone;
        if (timeZone2 == null || !timeZone2.getID().equals(timeZone.getID())) {
            this.mTimeZone = timeZone;
            compute();
        }
        return this;
    }

    public String toString() {
        StringBuilder sb = (StringBuilder) Pools.getStringBuilderPool().acquire();
        sb.append(Calendar.class.getName());
        sb.append("[time");
        sb.append(this.mMillisecond);
        sb.append(",zone=");
        sb.append(this.mTimeZone.getID());
        for (int i = 0; i < 25; i++) {
            sb.append(',');
            sb.append(FIELD_NAMES[i]);
            sb.append('=');
            sb.append(this.mFields[i]);
        }
        sb.append(']');
        String sb2 = sb.toString();
        Pools.getStringBuilderPool().release(sb);
        return sb2;
    }
}
