package miui.text.util;

import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.URLSpan;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import miui.util.Patterns;

public class Linkify {
    public static final int ALL = (TIME_PHRASES | 15);
    private static final Comparator COMPARATOR = new Comparator() {
        public int compare(LinkSpec linkSpec, LinkSpec linkSpec2) {
            int i = linkSpec.start;
            int i2 = linkSpec2.start;
            if (i < i2) {
                return -1;
            }
            if (i > i2) {
                return 1;
            }
            int i3 = linkSpec.end;
            int i4 = linkSpec2.end;
            if (i3 < i4) {
                return 1;
            }
            return i3 > i4 ? -1 : 0;
        }
    };
    public static final int EMAIL_ADDRESSES = 2;
    private static final String[] EMPTY = new String[0];
    public static final int MAP_ADDRESSES = 8;
    private static final int PATTERNS_FROM_FRAMEWORK = 14;
    public static final int PHONE_NUMBERS = 4;
    public static int TIME_PHRASES = 16;
    public static final int WEB_URLS = 1;

    class LinkSpec {
        int end;
        URLSpan span;
        int start;
        String url;

        private LinkSpec() {
        }
    }

    class Super extends android.text.util.Linkify {
        private Super() {
        }
    }

    private Linkify() {
    }

    private static void addLinkMovementMethod(TextView textView) {
        if (!(textView.getMovementMethod() instanceof LinkMovementMethod) && textView.getLinksClickable()) {
            textView.setMovementMethod(LinkMovementMethod.getInstance());
        }
    }

    public static boolean addLinks(Spannable spannable, int i) {
        if (i == 0) {
            return false;
        }
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (int length = uRLSpanArr.length - 1; length >= 0; length--) {
            spannable.removeSpan(uRLSpanArr[length]);
        }
        android.text.util.Linkify.addLinks(spannable, i & 14);
        int i2 = i & -15 & ALL;
        ArrayList arrayList = new ArrayList();
        if ((i2 & 1) != 0) {
            gatherLinks(arrayList, spannable, Patterns.WEB_URL, new String[]{"http://", "https://", "rtsp://"}, android.text.util.Linkify.sUrlMatchFilter, null);
        }
        if ((i2 & TIME_PHRASES) != 0) {
            String str = "time:";
            ArrayList arrayList2 = arrayList;
            Spannable spannable2 = spannable;
            gatherLinks(arrayList2, spannable2, Patterns.CHINESE_TIME_PATTERN, new String[]{str}, null, null);
            gatherLinks(arrayList2, spannable2, Patterns.ENGLISH_TIME_PATTERN, new String[]{str}, null, null);
        }
        pruneOverlaps(arrayList, spannable);
        if (arrayList.isEmpty()) {
            return false;
        }
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            LinkSpec linkSpec = (LinkSpec) it.next();
            String str2 = linkSpec.url;
            if (str2 != null) {
                applyLink(str2, linkSpec.start, linkSpec.end, spannable);
            }
        }
        return true;
    }

    public static boolean addLinks(TextView textView, int i) {
        if (i == 0) {
            return false;
        }
        CharSequence text = textView.getText();
        if (!(text instanceof Spannable)) {
            SpannableString valueOf = SpannableString.valueOf(text);
            if (!addLinks((Spannable) valueOf, i)) {
                return false;
            }
            addLinkMovementMethod(textView);
            textView.setText(valueOf);
            return true;
        } else if (!addLinks((Spannable) text, i)) {
            return false;
        } else {
            addLinkMovementMethod(textView);
            return true;
        }
    }

    private static void applyLink(String str, int i, int i2, Spannable spannable) {
        spannable.setSpan(new URLSpan(str), i, i2, 33);
    }

    private static void gatherLinks(ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Matcher matcher = pattern.matcher(spannable);
        while (matcher.find()) {
            int start = matcher.start();
            int end = matcher.end();
            if (matchFilter == null || matchFilter.acceptMatch(spannable, start, end)) {
                LinkSpec linkSpec = new LinkSpec();
                linkSpec.url = makeUrl(matcher.group(0), strArr, matcher, transformFilter);
                linkSpec.start = start;
                linkSpec.end = end;
                arrayList.add(linkSpec);
            }
        }
    }

    private static String makeUrl(String str, String[] strArr, Matcher matcher, TransformFilter transformFilter) {
        boolean z;
        if (transformFilter != null) {
            str = transformFilter.transformUrl(matcher, str);
        }
        int i = 0;
        while (true) {
            z = true;
            if (i >= strArr.length) {
                z = false;
                break;
            }
            if (str.regionMatches(true, 0, strArr[i], 0, strArr[i].length())) {
                if (!str.regionMatches(false, 0, strArr[i], 0, strArr[i].length())) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(strArr[i]);
                    sb.append(str.substring(strArr[i].length()));
                    str = sb.toString();
                }
            } else {
                i++;
            }
        }
        if (z) {
            return str;
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(strArr[0]);
        sb2.append(str);
        return sb2.toString();
    }

    private static void pruneOverlaps(ArrayList arrayList, Spannable spannable) {
        int i = 0;
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable.getSpans(0, spannable.length(), URLSpan.class);
        for (int i2 = 0; i2 < uRLSpanArr.length; i2++) {
            LinkSpec linkSpec = new LinkSpec();
            linkSpec.span = uRLSpanArr[i2];
            linkSpec.start = spannable.getSpanStart(uRLSpanArr[i2]);
            linkSpec.end = spannable.getSpanEnd(uRLSpanArr[i2]);
            arrayList.add(linkSpec);
        }
        Collections.sort(arrayList, COMPARATOR);
        int size = arrayList.size();
        while (i < size - 1) {
            LinkSpec linkSpec2 = (LinkSpec) arrayList.get(i);
            int i3 = i + 1;
            LinkSpec linkSpec3 = (LinkSpec) arrayList.get(i3);
            int i4 = linkSpec2.start;
            int i5 = linkSpec3.start;
            if (i4 <= i5) {
                int i6 = linkSpec2.end;
                if (i6 > i5) {
                    int i7 = linkSpec3.end;
                    int i8 = (i7 > i6 && i6 - i4 <= i7 - i5) ? i6 - i4 < i7 - i5 ? i : -1 : i3;
                    if (i8 != -1) {
                        URLSpan uRLSpan = ((LinkSpec) arrayList.get(i8)).span;
                        if (uRLSpan != null) {
                            spannable.removeSpan(uRLSpan);
                        }
                        arrayList.remove(i8);
                        size--;
                    }
                }
            }
            i = i3;
        }
    }
}
