package com.miui.internal.variable.v16;

import android.text.Spannable;
import android.text.style.URLSpan;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import android.util.Patterns;
import java.util.ArrayList;
import java.util.regex.Pattern;
import miui.reflect.Field;
import miui.reflect.Method;

public class Android_Text_Util_Linkify_class extends com.miui.internal.variable.Android_Text_Util_Linkify_class {
    private static final String TAG = "Linkify.v16";
    private static final Method mApplyLink;
    private static boolean mDisabled = false;
    private static final Field mEnd;
    private static final Method mGatherLinks;
    private static final Method mGatherMapLinks = Method.of(Linkify.class, "gatherMapLinks", "(Ljava/util/ArrayList;Landroid/text/Spannable;)V");
    private static final Method mPruneOverlaps = Method.of(Linkify.class, "pruneOverlaps", "(Ljava/util/ArrayList;)V");
    private static final Field mStart;
    private static final Field mUrl;

    static {
        Method method;
        String str = TAG;
        String str2 = "android.text.util.LinkSpec";
        mUrl = Field.of(str2, "url", "Ljava/lang/String;");
        String str3 = "I";
        mStart = Field.of(str2, "start", str3);
        mEnd = Field.of(str2, "end", str3);
        Method method2 = null;
        try {
            method = Method.of(Linkify.class, "gatherLinks", "(Ljava/util/ArrayList;Landroid/text/Spannable;Ljava/util/regex/Pattern;[Ljava/lang/String;Landroid/text/util/Linkify$MatchFilter;Landroid/text/util/Linkify$TransformFilter;)V");
        } catch (Throwable th) {
            Log.w(str, "reflect gatherLinks failed: ", th);
            method = null;
        }
        mGatherLinks = method;
        try {
            method2 = Method.of(Linkify.class, "applyLink", "(Ljava/lang/String;IILandroid/text/Spannable;)V");
        } catch (Throwable th2) {
            Log.w(str, "reflect applyLink failed: ", th2);
        }
        mApplyLink = method2;
    }

    static void disable() {
        mDisabled = true;
    }

    static void enable() {
        mDisabled = false;
    }

    /* access modifiers changed from: protected */
    public boolean addEmailLinks(long j, Linkify linkify, ArrayList arrayList, Spannable spannable, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        int size = arrayList.size();
        gatherLinks(arrayList, spannable, Patterns.EMAIL_ADDRESS, new String[]{"mailto:"}, null, null);
        return arrayList.size() > size;
    }

    /* access modifiers changed from: protected */
    public void applyLink(String str, int i, int i2, Spannable spannable) {
        Method method = mApplyLink;
        if (method != null) {
            method.invoke(null, null, str, Integer.valueOf(i), Integer.valueOf(i2), spannable);
            return;
        }
        throw new RuntimeException("applyLink: reflect failed, method not found");
    }

    public void buildProxy() {
        attachMethod("addLinks", "(Landroid/text/Spannable;I)Z");
        super.buildProxy();
    }

    /* access modifiers changed from: protected */
    public boolean callOriginalAddLinks(long j, Linkify linkify, Spannable spannable, int i) {
        return originalAddLinks(j, linkify, spannable, i);
    }

    /* access modifiers changed from: protected */
    public void gatherLinks(ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Method method = mGatherLinks;
        if (method != null) {
            method.invoke(null, null, arrayList, spannable, pattern, strArr, matchFilter, transformFilter);
            return;
        }
        throw new RuntimeException("gatherLinks: reflect failed, method not found");
    }

    /* access modifiers changed from: protected */
    public void gatherTelLinks(ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        gatherLinks(arrayList, spannable, Patterns.PHONE, new String[]{"tel:"}, Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleAddLinks(0, null, null, 0);
    }

    /* access modifiers changed from: protected */
    public boolean handleAddLinks(long j, Linkify linkify, Spannable spannable, int i) {
        boolean z;
        Spannable spannable2 = spannable;
        if (i == 0) {
            return false;
        }
        if (mDisabled) {
            return callOriginalAddLinks(j, linkify, spannable, i);
        }
        URLSpan[] uRLSpanArr = (URLSpan[]) spannable2.getSpans(0, spannable.length(), URLSpan.class);
        for (int length = uRLSpanArr.length - 1; length >= 0; length--) {
            spannable2.removeSpan(uRLSpanArr[length]);
        }
        ArrayList arrayList = new ArrayList();
        if ((i & miui.text.util.Linkify.TIME_PHRASES) != 0) {
            String str = "time:";
            ArrayList arrayList2 = arrayList;
            Spannable spannable3 = spannable;
            gatherLinks(arrayList2, spannable3, miui.util.Patterns.CHINESE_TIME_PATTERN, new String[]{str}, null, null);
            gatherLinks(arrayList2, spannable3, miui.util.Patterns.ENGLISH_TIME_PATTERN, new String[]{str}, null, null);
        }
        if ((i & 1) != 0) {
            gatherLinks(arrayList, spannable, miui.util.Patterns.WEB_URL, new String[]{"http://", "https://", "rtsp://"}, Linkify.sUrlMatchFilter, null);
        }
        if ((i & 2) != 0) {
            z = addEmailLinks(j, linkify, arrayList, spannable, new String[]{"mailto:"}, null, null);
        } else {
            z = false;
        }
        if ((i & 4) != 0) {
            gatherTelLinks(arrayList, spannable, Patterns.PHONE, new String[]{"tel:"}, Linkify.sPhoneNumberMatchFilter, Linkify.sPhoneNumberTransformFilter);
        }
        if ((i & 8) != 0) {
            mGatherMapLinks.invoke(null, null, arrayList, spannable2);
        }
        mPruneOverlaps.invoke(null, null, arrayList);
        if (arrayList.size() == 0 && !z) {
            return false;
        }
        for (int i2 = 0; i2 < arrayList.size(); i2++) {
            Object obj = arrayList.get(i2);
            applyLink((String) mUrl.get(obj), mStart.getInt(obj), mEnd.getInt(obj), spannable2);
        }
        return true;
    }

    /* access modifiers changed from: protected */
    public boolean originalAddLinks(long j, Linkify linkify, Spannable spannable, int i) {
        throw new IllegalStateException("com.miui.internal.variable.v16.Android_Text_Util_Linkify_class.originalAddLinks(long, Linkify, Spannable, int)");
    }
}
