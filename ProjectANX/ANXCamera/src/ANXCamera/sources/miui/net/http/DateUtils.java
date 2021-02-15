package miui.net.http;

import java.lang.ref.SoftReference;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.TimeZone;

class DateUtils {
    private static final String[] DEFAULT_PATTERNS = {PATTERN_RFC1036, PATTERN_RFC1123, PATTERN_ASCTIME};
    private static final Date DEFAULT_TWO_DIGIT_YEAR_START;
    public static final TimeZone GMT = TimeZone.getTimeZone("GMT");
    public static final String PATTERN_ASCTIME = "EEE MMM d HH:mm:ss yyyy";
    public static final String PATTERN_RFC1036 = "EEEE, dd-MMM-yy HH:mm:ss zzz";
    public static final String PATTERN_RFC1123 = "EEE, dd MMM yyyy HH:mm:ss zzz";

    final class DateFormatHolder {
        private static final ThreadLocal THREADLOCAL_FORMATS = new ThreadLocal() {
            /* access modifiers changed from: protected */
            public SoftReference initialValue() {
                return new SoftReference(new HashMap());
            }
        };

        DateFormatHolder() {
        }

        public static SimpleDateFormat formatFor(String str) {
            Map map = (Map) ((SoftReference) THREADLOCAL_FORMATS.get()).get();
            if (map == null) {
                map = new HashMap();
                THREADLOCAL_FORMATS.set(new SoftReference(map));
            }
            SimpleDateFormat simpleDateFormat = (SimpleDateFormat) map.get(str);
            if (simpleDateFormat != null) {
                return simpleDateFormat;
            }
            SimpleDateFormat simpleDateFormat2 = new SimpleDateFormat(str, Locale.US);
            simpleDateFormat2.setTimeZone(TimeZone.getTimeZone("GMT"));
            map.put(str, simpleDateFormat2);
            return simpleDateFormat2;
        }
    }

    static {
        Calendar instance = Calendar.getInstance();
        instance.setTimeZone(GMT);
        instance.set(2000, 0, 1, 0, 0, 0);
        instance.set(14, 0);
        DEFAULT_TWO_DIGIT_YEAR_START = instance.getTime();
    }

    private DateUtils() {
    }

    public static String formatDate(Date date) {
        return formatDate(date, PATTERN_RFC1123);
    }

    public static String formatDate(Date date, String str) {
        if (date == null) {
            throw new IllegalArgumentException("date is null");
        } else if (str != null) {
            return DateFormatHolder.formatFor(str).format(date);
        } else {
            throw new IllegalArgumentException("pattern is null");
        }
    }

    public static Date parseDate(String str) {
        return parseDate(str, null, null);
    }

    public static Date parseDate(String str, String[] strArr) {
        return parseDate(str, strArr, null);
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v6, types: [java.util.Date] */
    /* JADX WARNING: type inference failed for: r3v7, types: [java.lang.String] */
    /* JADX WARNING: type inference failed for: r3v8 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=java.lang.String, code=null, for r3v0, types: [java.lang.String] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v2
  assigns: []
  uses: []
  mth insns count: 40
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 5 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static Date parseDate(String r3, String[] strArr, Date date) {
        if (r3 != 0) {
            if (strArr == null) {
                strArr = DEFAULT_PATTERNS;
            }
            if (date == null) {
                date = DEFAULT_TWO_DIGIT_YEAR_START;
            }
            if (r3.length() > 1) {
                String str = "'";
                if (r3.startsWith(str) && r3.endsWith(str)) {
                    r3 = r3.substring(1, r3.length() - 1);
                }
            }
            int length = strArr.length;
            int i = 0;
            r3 = r3;
            while (i < length) {
                SimpleDateFormat formatFor = DateFormatHolder.formatFor(strArr[i]);
                formatFor.set2DigitYearStart(date);
                try {
                    r3 = r3;
                    r3 = formatFor.parse(r3);
                    r3 = r3;
                    return r3;
                } catch (ParseException unused) {
                    i++;
                    r3 = r3;
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Unable to parse the date ");
            sb.append(r3);
            throw new IllegalArgumentException(sb.toString());
        }
        throw new IllegalArgumentException("dateValue is null");
    }
}
