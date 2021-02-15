package com.miui.internal.variable;

import android.text.Spannable;
import android.text.util.Linkify;
import android.text.util.Linkify.MatchFilter;
import android.text.util.Linkify.TransformFilter;
import android.util.Patterns;
import com.miui.internal.util.ClassProxy;
import java.util.ArrayList;
import java.util.regex.Pattern;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Text_Util_Linkify_class extends ClassProxy implements IManagedClassProxy {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Text_Util_Linkify_class Android_Text_Util_Linkify_class;

        private Factory() {
            this.Android_Text_Util_Linkify_class = (Android_Text_Util_Linkify_class) create("Android_Text_Util_Linkify_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Text_Util_Linkify_class get() {
            return this.Android_Text_Util_Linkify_class;
        }
    }

    public Android_Text_Util_Linkify_class() {
        super(Linkify.class);
    }

    public void buildProxy() {
        attachMethod("gatherLinks", "(Ljava/util/ArrayList;Landroid/text/Spannable;Ljava/util/regex/Pattern;[Ljava/lang/String;Landroid/text/util/Linkify$MatchFilter;Landroid/text/util/Linkify$TransformFilter;)V");
    }

    /* access modifiers changed from: protected */
    public void callOriginalGatherLinks(long j, Linkify linkify, ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        originalGatherLinks(j, linkify, arrayList, spannable, pattern, strArr, matchFilter, transformFilter);
    }

    /* access modifiers changed from: protected */
    public void handle() {
        handleGatherLinks(0, null, null, null, null, null, null, null);
    }

    /* access modifiers changed from: protected */
    public void handleGatherLinks(long j, Linkify linkify, ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        Pattern pattern2 = pattern;
        callOriginalGatherLinks(j, linkify, arrayList, spannable, pattern2 == Patterns.WEB_URL ? miui.util.Patterns.WEB_URL : pattern2, strArr, matchFilter, transformFilter);
    }

    /* access modifiers changed from: protected */
    public void originalGatherLinks(long j, Linkify linkify, ArrayList arrayList, Spannable spannable, Pattern pattern, String[] strArr, MatchFilter matchFilter, TransformFilter transformFilter) {
        throw new IllegalStateException("com.miui.internal.variable.Android_Text_Util_Linkify_class.originalGatherLinks(long, Linkify, ArrayList, Spannable, Pattern, String[], MatchFilter, TransformFilter)");
    }
}
