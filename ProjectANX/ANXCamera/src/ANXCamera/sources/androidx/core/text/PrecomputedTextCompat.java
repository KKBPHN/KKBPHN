package androidx.core.text;

import android.annotation.SuppressLint;
import android.os.Build.VERSION;
import android.text.Layout.Alignment;
import android.text.PrecomputedText;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.StaticLayout;
import android.text.StaticLayout.Builder;
import android.text.TextDirectionHeuristic;
import android.text.TextDirectionHeuristics;
import android.text.TextPaint;
import android.text.TextUtils;
import android.text.style.MetricAffectingSpan;
import androidx.annotation.GuardedBy;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.UiThread;
import androidx.core.os.TraceCompat;
import androidx.core.util.ObjectsCompat;
import androidx.core.util.Preconditions;
import java.util.ArrayList;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

public class PrecomputedTextCompat implements Spannable {
    private static final char LINE_FEED = '\n';
    @GuardedBy("sLock")
    @NonNull
    private static Executor sExecutor = null;
    private static final Object sLock = new Object();
    @NonNull
    private final int[] mParagraphEnds;
    @NonNull
    private final Params mParams;
    @NonNull
    private final Spannable mText;
    @Nullable
    private final PrecomputedText mWrapped;

    public final class Params {
        private final int mBreakStrategy;
        private final int mHyphenationFrequency;
        @NonNull
        private final TextPaint mPaint;
        @Nullable
        private final TextDirectionHeuristic mTextDir;
        final android.text.PrecomputedText.Params mWrapped;

        public class Builder {
            private int mBreakStrategy;
            private int mHyphenationFrequency;
            @NonNull
            private final TextPaint mPaint;
            private TextDirectionHeuristic mTextDir;

            public Builder(@NonNull TextPaint textPaint) {
                this.mPaint = textPaint;
                if (VERSION.SDK_INT >= 23) {
                    this.mBreakStrategy = 1;
                    this.mHyphenationFrequency = 1;
                } else {
                    this.mHyphenationFrequency = 0;
                    this.mBreakStrategy = 0;
                }
                this.mTextDir = VERSION.SDK_INT >= 18 ? TextDirectionHeuristics.FIRSTSTRONG_LTR : null;
            }

            @NonNull
            public Params build() {
                return new Params(this.mPaint, this.mTextDir, this.mBreakStrategy, this.mHyphenationFrequency);
            }

            @RequiresApi(23)
            public Builder setBreakStrategy(int i) {
                this.mBreakStrategy = i;
                return this;
            }

            @RequiresApi(23)
            public Builder setHyphenationFrequency(int i) {
                this.mHyphenationFrequency = i;
                return this;
            }

            @RequiresApi(18)
            public Builder setTextDirection(@NonNull TextDirectionHeuristic textDirectionHeuristic) {
                this.mTextDir = textDirectionHeuristic;
                return this;
            }
        }

        @RequiresApi(28)
        public Params(@NonNull android.text.PrecomputedText.Params params) {
            this.mPaint = params.getTextPaint();
            this.mTextDir = params.getTextDirection();
            this.mBreakStrategy = params.getBreakStrategy();
            this.mHyphenationFrequency = params.getHyphenationFrequency();
            if (VERSION.SDK_INT < 29) {
                params = null;
            }
            this.mWrapped = params;
        }

        @SuppressLint({"NewApi"})
        Params(@NonNull TextPaint textPaint, @NonNull TextDirectionHeuristic textDirectionHeuristic, int i, int i2) {
            this.mWrapped = VERSION.SDK_INT >= 29 ? new android.text.PrecomputedText.Params.Builder(textPaint).setBreakStrategy(i).setHyphenationFrequency(i2).setTextDirection(textDirectionHeuristic).build() : null;
            this.mPaint = textPaint;
            this.mTextDir = textDirectionHeuristic;
            this.mBreakStrategy = i;
            this.mHyphenationFrequency = i2;
        }

        public boolean equals(@Nullable Object obj) {
            if (obj == this) {
                return true;
            }
            if (!(obj instanceof Params)) {
                return false;
            }
            Params params = (Params) obj;
            if (!equalsWithoutTextDirection(params)) {
                return false;
            }
            return VERSION.SDK_INT < 18 || this.mTextDir == params.getTextDirection();
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        public boolean equalsWithoutTextDirection(@NonNull Params params) {
            if ((VERSION.SDK_INT >= 23 && (this.mBreakStrategy != params.getBreakStrategy() || this.mHyphenationFrequency != params.getHyphenationFrequency())) || this.mPaint.getTextSize() != params.getTextPaint().getTextSize() || this.mPaint.getTextScaleX() != params.getTextPaint().getTextScaleX() || this.mPaint.getTextSkewX() != params.getTextPaint().getTextSkewX()) {
                return false;
            }
            if ((VERSION.SDK_INT >= 21 && (this.mPaint.getLetterSpacing() != params.getTextPaint().getLetterSpacing() || !TextUtils.equals(this.mPaint.getFontFeatureSettings(), params.getTextPaint().getFontFeatureSettings()))) || this.mPaint.getFlags() != params.getTextPaint().getFlags()) {
                return false;
            }
            int i = VERSION.SDK_INT;
            if (i >= 24) {
                if (!this.mPaint.getTextLocales().equals(params.getTextPaint().getTextLocales())) {
                    return false;
                }
            } else if (i >= 17 && !this.mPaint.getTextLocale().equals(params.getTextPaint().getTextLocale())) {
                return false;
            }
            if (this.mPaint.getTypeface() == null) {
                if (params.getTextPaint().getTypeface() != null) {
                    return false;
                }
            } else if (!this.mPaint.getTypeface().equals(params.getTextPaint().getTypeface())) {
                return false;
            }
            return true;
        }

        @RequiresApi(23)
        public int getBreakStrategy() {
            return this.mBreakStrategy;
        }

        @RequiresApi(23)
        public int getHyphenationFrequency() {
            return this.mHyphenationFrequency;
        }

        @RequiresApi(18)
        @Nullable
        public TextDirectionHeuristic getTextDirection() {
            return this.mTextDir;
        }

        @NonNull
        public TextPaint getTextPaint() {
            return this.mPaint;
        }

        public int hashCode() {
            int i = VERSION.SDK_INT;
            Object[] objArr = i >= 24 ? new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocales(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)} : i >= 21 ? new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Float.valueOf(this.mPaint.getLetterSpacing()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), Boolean.valueOf(this.mPaint.isElegantTextHeight()), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)} : i >= 18 ? new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)} : i >= 17 ? new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTextLocale(), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)} : new Object[]{Float.valueOf(this.mPaint.getTextSize()), Float.valueOf(this.mPaint.getTextScaleX()), Float.valueOf(this.mPaint.getTextSkewX()), Integer.valueOf(this.mPaint.getFlags()), this.mPaint.getTypeface(), this.mTextDir, Integer.valueOf(this.mBreakStrategy), Integer.valueOf(this.mHyphenationFrequency)};
            return ObjectsCompat.hash(objArr);
        }

        /* JADX WARNING: Removed duplicated region for block: B:12:0x00e3  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public String toString() {
            StringBuilder sb;
            Object textLocale;
            StringBuilder sb2 = new StringBuilder("{");
            StringBuilder sb3 = new StringBuilder();
            sb3.append("textSize=");
            sb3.append(this.mPaint.getTextSize());
            sb2.append(sb3.toString());
            StringBuilder sb4 = new StringBuilder();
            sb4.append(", textScaleX=");
            sb4.append(this.mPaint.getTextScaleX());
            sb2.append(sb4.toString());
            StringBuilder sb5 = new StringBuilder();
            sb5.append(", textSkewX=");
            sb5.append(this.mPaint.getTextSkewX());
            sb2.append(sb5.toString());
            if (VERSION.SDK_INT >= 21) {
                StringBuilder sb6 = new StringBuilder();
                sb6.append(", letterSpacing=");
                sb6.append(this.mPaint.getLetterSpacing());
                sb2.append(sb6.toString());
                StringBuilder sb7 = new StringBuilder();
                sb7.append(", elegantTextHeight=");
                sb7.append(this.mPaint.isElegantTextHeight());
                sb2.append(sb7.toString());
            }
            int i = VERSION.SDK_INT;
            String str = ", textLocale=";
            if (i >= 24) {
                sb = new StringBuilder();
                sb.append(str);
                textLocale = this.mPaint.getTextLocales();
            } else {
                if (i >= 17) {
                    sb = new StringBuilder();
                    sb.append(str);
                    textLocale = this.mPaint.getTextLocale();
                }
                StringBuilder sb8 = new StringBuilder();
                sb8.append(", typeface=");
                sb8.append(this.mPaint.getTypeface());
                sb2.append(sb8.toString());
                if (VERSION.SDK_INT >= 26) {
                    StringBuilder sb9 = new StringBuilder();
                    sb9.append(", variationSettings=");
                    sb9.append(this.mPaint.getFontVariationSettings());
                    sb2.append(sb9.toString());
                }
                StringBuilder sb10 = new StringBuilder();
                sb10.append(", textDir=");
                sb10.append(this.mTextDir);
                sb2.append(sb10.toString());
                StringBuilder sb11 = new StringBuilder();
                sb11.append(", breakStrategy=");
                sb11.append(this.mBreakStrategy);
                sb2.append(sb11.toString());
                StringBuilder sb12 = new StringBuilder();
                sb12.append(", hyphenationFrequency=");
                sb12.append(this.mHyphenationFrequency);
                sb2.append(sb12.toString());
                sb2.append("}");
                return sb2.toString();
            }
            sb.append(textLocale);
            sb2.append(sb.toString());
            StringBuilder sb82 = new StringBuilder();
            sb82.append(", typeface=");
            sb82.append(this.mPaint.getTypeface());
            sb2.append(sb82.toString());
            if (VERSION.SDK_INT >= 26) {
            }
            StringBuilder sb102 = new StringBuilder();
            sb102.append(", textDir=");
            sb102.append(this.mTextDir);
            sb2.append(sb102.toString());
            StringBuilder sb112 = new StringBuilder();
            sb112.append(", breakStrategy=");
            sb112.append(this.mBreakStrategy);
            sb2.append(sb112.toString());
            StringBuilder sb122 = new StringBuilder();
            sb122.append(", hyphenationFrequency=");
            sb122.append(this.mHyphenationFrequency);
            sb2.append(sb122.toString());
            sb2.append("}");
            return sb2.toString();
        }
    }

    class PrecomputedTextFutureTask extends FutureTask {

        class PrecomputedTextCallback implements Callable {
            private Params mParams;
            private CharSequence mText;

            PrecomputedTextCallback(@NonNull Params params, @NonNull CharSequence charSequence) {
                this.mParams = params;
                this.mText = charSequence;
            }

            public PrecomputedTextCompat call() {
                return PrecomputedTextCompat.create(this.mText, this.mParams);
            }
        }

        PrecomputedTextFutureTask(@NonNull Params params, @NonNull CharSequence charSequence) {
            super(new PrecomputedTextCallback(params, charSequence));
        }
    }

    @RequiresApi(28)
    private PrecomputedTextCompat(@NonNull PrecomputedText precomputedText, @NonNull Params params) {
        this.mText = precomputedText;
        this.mParams = params;
        this.mParagraphEnds = null;
        if (VERSION.SDK_INT < 29) {
            precomputedText = null;
        }
        this.mWrapped = precomputedText;
    }

    private PrecomputedTextCompat(@NonNull CharSequence charSequence, @NonNull Params params, @NonNull int[] iArr) {
        this.mText = new SpannableString(charSequence);
        this.mParams = params;
        this.mParagraphEnds = iArr;
        this.mWrapped = null;
    }

    @SuppressLint({"NewApi"})
    public static PrecomputedTextCompat create(@NonNull CharSequence charSequence, @NonNull Params params) {
        Preconditions.checkNotNull(charSequence);
        Preconditions.checkNotNull(params);
        try {
            TraceCompat.beginSection("PrecomputedText");
            if (VERSION.SDK_INT >= 29 && params.mWrapped != null) {
                return new PrecomputedTextCompat(PrecomputedText.create(charSequence, params.mWrapped), params);
            }
            ArrayList arrayList = new ArrayList();
            int length = charSequence.length();
            int i = 0;
            while (i < length) {
                int indexOf = TextUtils.indexOf(charSequence, LINE_FEED, i, length);
                i = indexOf < 0 ? length : indexOf + 1;
                arrayList.add(Integer.valueOf(i));
            }
            int[] iArr = new int[arrayList.size()];
            for (int i2 = 0; i2 < arrayList.size(); i2++) {
                iArr[i2] = ((Integer) arrayList.get(i2)).intValue();
            }
            if (VERSION.SDK_INT >= 23) {
                Builder.obtain(charSequence, 0, charSequence.length(), params.getTextPaint(), Integer.MAX_VALUE).setBreakStrategy(params.getBreakStrategy()).setHyphenationFrequency(params.getHyphenationFrequency()).setTextDirection(params.getTextDirection()).build();
            } else if (VERSION.SDK_INT >= 21) {
                new StaticLayout(charSequence, params.getTextPaint(), Integer.MAX_VALUE, Alignment.ALIGN_NORMAL, 1.0f, 0.0f, false);
            }
            PrecomputedTextCompat precomputedTextCompat = new PrecomputedTextCompat(charSequence, params, iArr);
            TraceCompat.endSection();
            return precomputedTextCompat;
        } finally {
            TraceCompat.endSection();
        }
    }

    @UiThread
    public static Future getTextFuture(@NonNull CharSequence charSequence, @NonNull Params params, @Nullable Executor executor) {
        PrecomputedTextFutureTask precomputedTextFutureTask = new PrecomputedTextFutureTask(params, charSequence);
        if (executor == null) {
            synchronized (sLock) {
                if (sExecutor == null) {
                    sExecutor = Executors.newFixedThreadPool(1);
                }
                executor = sExecutor;
            }
        }
        executor.execute(precomputedTextFutureTask);
        return precomputedTextFutureTask;
    }

    public char charAt(int i) {
        return this.mText.charAt(i);
    }

    @SuppressLint({"NewApi"})
    @IntRange(from = 0)
    public int getParagraphCount() {
        return VERSION.SDK_INT >= 29 ? this.mWrapped.getParagraphCount() : this.mParagraphEnds.length;
    }

    @SuppressLint({"NewApi"})
    @IntRange(from = 0)
    public int getParagraphEnd(@IntRange(from = 0) int i) {
        Preconditions.checkArgumentInRange(i, 0, getParagraphCount(), "paraIndex");
        return VERSION.SDK_INT >= 29 ? this.mWrapped.getParagraphEnd(i) : this.mParagraphEnds[i];
    }

    @SuppressLint({"NewApi"})
    @IntRange(from = 0)
    public int getParagraphStart(@IntRange(from = 0) int i) {
        int i2 = 0;
        Preconditions.checkArgumentInRange(i, 0, getParagraphCount(), "paraIndex");
        if (VERSION.SDK_INT >= 29) {
            return this.mWrapped.getParagraphStart(i);
        }
        if (i != 0) {
            i2 = this.mParagraphEnds[i - 1];
        }
        return i2;
    }

    @NonNull
    public Params getParams() {
        return this.mParams;
    }

    @RequiresApi(28)
    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public PrecomputedText getPrecomputedText() {
        Spannable spannable = this.mText;
        if (spannable instanceof PrecomputedText) {
            return (PrecomputedText) spannable;
        }
        return null;
    }

    public int getSpanEnd(Object obj) {
        return this.mText.getSpanEnd(obj);
    }

    public int getSpanFlags(Object obj) {
        return this.mText.getSpanFlags(obj);
    }

    public int getSpanStart(Object obj) {
        return this.mText.getSpanStart(obj);
    }

    @SuppressLint({"NewApi"})
    public Object[] getSpans(int i, int i2, Class cls) {
        return VERSION.SDK_INT >= 29 ? this.mWrapped.getSpans(i, i2, cls) : this.mText.getSpans(i, i2, cls);
    }

    public int length() {
        return this.mText.length();
    }

    public int nextSpanTransition(int i, int i2, Class cls) {
        return this.mText.nextSpanTransition(i, i2, cls);
    }

    @SuppressLint({"NewApi"})
    public void removeSpan(Object obj) {
        if (obj instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be removed from PrecomputedText.");
        } else if (VERSION.SDK_INT >= 29) {
            this.mWrapped.removeSpan(obj);
        } else {
            this.mText.removeSpan(obj);
        }
    }

    @SuppressLint({"NewApi"})
    public void setSpan(Object obj, int i, int i2, int i3) {
        if (obj instanceof MetricAffectingSpan) {
            throw new IllegalArgumentException("MetricAffectingSpan can not be set to PrecomputedText.");
        } else if (VERSION.SDK_INT >= 29) {
            this.mWrapped.setSpan(obj, i, i2, i3);
        } else {
            this.mText.setSpan(obj, i, i2, i3);
        }
    }

    public CharSequence subSequence(int i, int i2) {
        return this.mText.subSequence(i, i2);
    }

    public String toString() {
        return this.mText.toString();
    }
}
