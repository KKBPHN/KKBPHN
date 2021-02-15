package com.miui.internal.variable.v29;

import android.text.Spannable;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.Log;
import com.miui.internal.variable.api.Overridable;
import com.miui.internal.variable.api.v29.Android_Text_Util_Linkify.Extension;
import com.miui.internal.variable.api.v29.Android_Text_Util_Linkify.Interface;
import java.util.ArrayList;
import java.util.regex.Pattern;
import miui.reflect.Method;

public class Android_Text_Util_Linkify_class extends com.miui.internal.variable.v27.Android_Text_Util_Linkify_class implements Overridable {
    private static final String TAG = "Linkify.v29";
    protected static final Method mApplyLink;
    private Interface mImpl = new Interface() {
        public boolean addLinks(Linkify linkify, Spannable spannable, int i) {
            return Android_Text_Util_Linkify_class.this.handleAddLinks(0, linkify, spannable, i);
        }

        public void gatherLinks(Linkify linkify, ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
            Android_Text_Util_Linkify_class.this.handleGatherLinks(0, linkify, arrayList, spannable, pattern, strArr, matchFilter, transformFilter);
        }
    };
    private Interface mOriginal;

    static {
        Method method;
        try {
            method = Method.of(Linkify.class, "applyLink", "(Ljava/lang/String;IILandroid/text/Spannable;Ljava/util/function/Function;)V");
        } catch (Throwable th) {
            Log.w(TAG, "reflect applyLink failed: ", th);
            method = null;
        }
        mApplyLink = method;
    }

    /* access modifiers changed from: protected */
    public void applyLink(String str, int i, int i2, Spannable spannable) {
        Method method = mApplyLink;
        if (method != null) {
            method.invoke(null, null, str, Integer.valueOf(i), Integer.valueOf(i2), spannable, null);
            return;
        }
        throw new RuntimeException("applyLink: reflect failed, method not found");
    }

    public Interface asInterface() {
        return this.mImpl;
    }

    public void bind(Interface interfaceR) {
        this.mOriginal = interfaceR;
    }

    /* access modifiers changed from: protected */
    public boolean callOriginalAddLinks(long j, Linkify linkify, Spannable spannable, int i) {
        Interface interfaceR = this.mOriginal;
        return interfaceR != null ? interfaceR.addLinks(linkify, spannable, i) : super.callOriginalAddLinks(j, linkify, spannable, i);
    }

    /* access modifiers changed from: protected */
    public void callOriginalGatherLinks(long j, Linkify linkify, ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Interface interfaceR = this.mOriginal;
        if (interfaceR != null) {
            interfaceR.gatherLinks(linkify, arrayList, spannable, pattern, strArr, matchFilter, transformFilter);
        } else {
            super.callOriginalGatherLinks(j, linkify, arrayList, spannable, pattern, strArr, matchFilter, transformFilter);
        }
    }

    /* access modifiers changed from: protected */
    public void onClassProxyDisabled() {
        Extension.get().setExtension(this);
    }
}
