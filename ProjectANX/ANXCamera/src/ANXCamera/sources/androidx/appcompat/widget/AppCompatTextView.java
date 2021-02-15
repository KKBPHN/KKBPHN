package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.ActionMode.Callback;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputConnection;
import android.view.textclassifier.TextClassifier;
import android.widget.TextView;
import androidx.annotation.DrawableRes;
import androidx.annotation.IntRange;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.Px;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.core.graphics.TypefaceCompat;
import androidx.core.text.PrecomputedTextCompat;
import androidx.core.text.PrecomputedTextCompat.Params;
import androidx.core.view.TintableBackgroundView;
import androidx.core.widget.AutoSizeableTextView;
import androidx.core.widget.TextViewCompat;
import androidx.core.widget.TintableCompoundDrawablesView;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class AppCompatTextView extends TextView implements TintableBackgroundView, TintableCompoundDrawablesView, AutoSizeableTextView {
    private final AppCompatBackgroundHelper mBackgroundTintHelper;
    @Nullable
    private Future mPrecomputedTextFuture;
    private final AppCompatTextClassifierHelper mTextClassifierHelper;
    private final AppCompatTextHelper mTextHelper;

    public AppCompatTextView(Context context) {
        this(context, null);
    }

    public AppCompatTextView(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 16842884);
    }

    public AppCompatTextView(Context context, AttributeSet attributeSet, int i) {
        super(TintContextWrapper.wrap(context), attributeSet, i);
        this.mBackgroundTintHelper = new AppCompatBackgroundHelper(this);
        this.mBackgroundTintHelper.loadFromAttributes(attributeSet, i);
        this.mTextHelper = new AppCompatTextHelper(this);
        this.mTextHelper.loadFromAttributes(attributeSet, i);
        this.mTextHelper.applyCompoundDrawablesTints();
        this.mTextClassifierHelper = new AppCompatTextClassifierHelper(this);
    }

    private void consumeTextFutureAndSetBlocking() {
        Future future = this.mPrecomputedTextFuture;
        if (future != null) {
            try {
                this.mPrecomputedTextFuture = null;
                TextViewCompat.setPrecomputedText(this, (PrecomputedTextCompat) future.get());
            } catch (InterruptedException | ExecutionException unused) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void drawableStateChanged() {
        super.drawableStateChanged();
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.applySupportBackgroundTint();
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.applyCompoundDrawablesTints();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getAutoSizeMaxTextSize() {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMaxTextSize();
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            return appCompatTextHelper.getAutoSizeMaxTextSize();
        }
        return -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getAutoSizeMinTextSize() {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeMinTextSize();
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            return appCompatTextHelper.getAutoSizeMinTextSize();
        }
        return -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getAutoSizeStepGranularity() {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeStepGranularity();
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            return appCompatTextHelper.getAutoSizeStepGranularity();
        }
        return -1;
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int[] getAutoSizeTextAvailableSizes() {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            return super.getAutoSizeTextAvailableSizes();
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        return appCompatTextHelper != null ? appCompatTextHelper.getAutoSizeTextAvailableSizes() : new int[0];
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int getAutoSizeTextType() {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            int i = 1;
            if (super.getAutoSizeTextType() != 1) {
                i = 0;
            }
            return i;
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            return appCompatTextHelper.getAutoSizeTextType();
        }
        return 0;
    }

    public int getFirstBaselineToTopHeight() {
        return TextViewCompat.getFirstBaselineToTopHeight(this);
    }

    public int getLastBaselineToBottomHeight() {
        return TextViewCompat.getLastBaselineToBottomHeight(this);
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public ColorStateList getSupportBackgroundTintList() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            return appCompatBackgroundHelper.getSupportBackgroundTintList();
        }
        return null;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public Mode getSupportBackgroundTintMode() {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            return appCompatBackgroundHelper.getSupportBackgroundTintMode();
        }
        return null;
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public ColorStateList getSupportCompoundDrawablesTintList() {
        return this.mTextHelper.getCompoundDrawableTintList();
    }

    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public Mode getSupportCompoundDrawablesTintMode() {
        return this.mTextHelper.getCompoundDrawableTintMode();
    }

    public CharSequence getText() {
        consumeTextFutureAndSetBlocking();
        return super.getText();
    }

    @RequiresApi(api = 26)
    @NonNull
    public TextClassifier getTextClassifier() {
        if (VERSION.SDK_INT < 28) {
            AppCompatTextClassifierHelper appCompatTextClassifierHelper = this.mTextClassifierHelper;
            if (appCompatTextClassifierHelper != null) {
                return appCompatTextClassifierHelper.getTextClassifier();
            }
        }
        return super.getTextClassifier();
    }

    @NonNull
    public Params getTextMetricsParamsCompat() {
        return TextViewCompat.getTextMetricsParams(this);
    }

    public InputConnection onCreateInputConnection(EditorInfo editorInfo) {
        InputConnection onCreateInputConnection = super.onCreateInputConnection(editorInfo);
        AppCompatHintHelper.onCreateInputConnection(onCreateInputConnection, editorInfo, this);
        return onCreateInputConnection;
    }

    /* access modifiers changed from: protected */
    public void onLayout(boolean z, int i, int i2, int i3, int i4) {
        super.onLayout(z, i, i2, i3, i4);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onLayout(z, i, i2, i3, i4);
        }
    }

    /* access modifiers changed from: protected */
    public void onMeasure(int i, int i2) {
        consumeTextFutureAndSetBlocking();
        super.onMeasure(i, i2);
    }

    /* access modifiers changed from: protected */
    public void onTextChanged(CharSequence charSequence, int i, int i2, int i3) {
        super.onTextChanged(charSequence, i, i2, i3);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null && !AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE && appCompatTextHelper.isAutoSizeEnabled()) {
            this.mTextHelper.autoSizeText();
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setAutoSizeTextTypeUniformWithConfiguration(int i, int i2, int i3, int i4) {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
            return;
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.setAutoSizeTextTypeUniformWithConfiguration(i, i2, i3, i4);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setAutoSizeTextTypeUniformWithPresetSizes(@NonNull int[] iArr, int i) {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
            return;
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.setAutoSizeTextTypeUniformWithPresetSizes(iArr, i);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setAutoSizeTextTypeWithDefaults(int i) {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setAutoSizeTextTypeWithDefaults(i);
            return;
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.setAutoSizeTextTypeWithDefaults(i);
        }
    }

    public void setBackgroundDrawable(Drawable drawable) {
        super.setBackgroundDrawable(drawable);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundDrawable(drawable);
        }
    }

    public void setBackgroundResource(@DrawableRes int i) {
        super.setBackgroundResource(i);
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.onSetBackgroundResource(i);
        }
    }

    public void setCompoundDrawables(@Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
        super.setCompoundDrawables(drawable, drawable2, drawable3, drawable4);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetCompoundDrawables();
        }
    }

    @RequiresApi(17)
    public void setCompoundDrawablesRelative(@Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
        super.setCompoundDrawablesRelative(drawable, drawable2, drawable3, drawable4);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetCompoundDrawables();
        }
    }

    @RequiresApi(17)
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        Context context = getContext();
        Drawable drawable = null;
        Drawable drawable2 = i != 0 ? AppCompatResources.getDrawable(context, i) : null;
        Drawable drawable3 = i2 != 0 ? AppCompatResources.getDrawable(context, i2) : null;
        Drawable drawable4 = i3 != 0 ? AppCompatResources.getDrawable(context, i3) : null;
        if (i4 != 0) {
            drawable = AppCompatResources.getDrawable(context, i4);
        }
        setCompoundDrawablesRelativeWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetCompoundDrawables();
        }
    }

    @RequiresApi(17)
    public void setCompoundDrawablesRelativeWithIntrinsicBounds(@Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
        super.setCompoundDrawablesRelativeWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetCompoundDrawables();
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(int i, int i2, int i3, int i4) {
        Context context = getContext();
        Drawable drawable = null;
        Drawable drawable2 = i != 0 ? AppCompatResources.getDrawable(context, i) : null;
        Drawable drawable3 = i2 != 0 ? AppCompatResources.getDrawable(context, i2) : null;
        Drawable drawable4 = i3 != 0 ? AppCompatResources.getDrawable(context, i3) : null;
        if (i4 != 0) {
            drawable = AppCompatResources.getDrawable(context, i4);
        }
        setCompoundDrawablesWithIntrinsicBounds(drawable2, drawable3, drawable4, drawable);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetCompoundDrawables();
        }
    }

    public void setCompoundDrawablesWithIntrinsicBounds(@Nullable Drawable drawable, @Nullable Drawable drawable2, @Nullable Drawable drawable3, @Nullable Drawable drawable4) {
        super.setCompoundDrawablesWithIntrinsicBounds(drawable, drawable2, drawable3, drawable4);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetCompoundDrawables();
        }
    }

    public void setCustomSelectionActionModeCallback(Callback callback) {
        super.setCustomSelectionActionModeCallback(TextViewCompat.wrapCustomSelectionActionModeCallback(this, callback));
    }

    public void setFirstBaselineToTopHeight(@Px @IntRange(from = 0) int i) {
        if (VERSION.SDK_INT >= 28) {
            super.setFirstBaselineToTopHeight(i);
        } else {
            TextViewCompat.setFirstBaselineToTopHeight(this, i);
        }
    }

    public void setLastBaselineToBottomHeight(@Px @IntRange(from = 0) int i) {
        if (VERSION.SDK_INT >= 28) {
            super.setLastBaselineToBottomHeight(i);
        } else {
            TextViewCompat.setLastBaselineToBottomHeight(this, i);
        }
    }

    public void setLineHeight(@Px @IntRange(from = 0) int i) {
        TextViewCompat.setLineHeight(this, i);
    }

    public void setPrecomputedText(@NonNull PrecomputedTextCompat precomputedTextCompat) {
        TextViewCompat.setPrecomputedText(this, precomputedTextCompat);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSupportBackgroundTintList(@Nullable ColorStateList colorStateList) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintList(colorStateList);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSupportBackgroundTintMode(@Nullable Mode mode) {
        AppCompatBackgroundHelper appCompatBackgroundHelper = this.mBackgroundTintHelper;
        if (appCompatBackgroundHelper != null) {
            appCompatBackgroundHelper.setSupportBackgroundTintMode(mode);
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSupportCompoundDrawablesTintList(@Nullable ColorStateList colorStateList) {
        this.mTextHelper.setCompoundDrawableTintList(colorStateList);
        this.mTextHelper.applyCompoundDrawablesTints();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public void setSupportCompoundDrawablesTintMode(@Nullable Mode mode) {
        this.mTextHelper.setCompoundDrawableTintMode(mode);
        this.mTextHelper.applyCompoundDrawablesTints();
    }

    public void setTextAppearance(Context context, int i) {
        super.setTextAppearance(context, i);
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.onSetTextAppearance(context, i);
        }
    }

    @RequiresApi(api = 26)
    public void setTextClassifier(@Nullable TextClassifier textClassifier) {
        if (VERSION.SDK_INT < 28) {
            AppCompatTextClassifierHelper appCompatTextClassifierHelper = this.mTextClassifierHelper;
            if (appCompatTextClassifierHelper != null) {
                appCompatTextClassifierHelper.setTextClassifier(textClassifier);
                return;
            }
        }
        super.setTextClassifier(textClassifier);
    }

    public void setTextFuture(@Nullable Future future) {
        this.mPrecomputedTextFuture = future;
        if (future != null) {
            requestLayout();
        }
    }

    public void setTextMetricsParamsCompat(@NonNull Params params) {
        TextViewCompat.setTextMetricsParams(this, params);
    }

    public void setTextSize(int i, float f) {
        if (AutoSizeableTextView.PLATFORM_SUPPORTS_AUTOSIZE) {
            super.setTextSize(i, f);
            return;
        }
        AppCompatTextHelper appCompatTextHelper = this.mTextHelper;
        if (appCompatTextHelper != null) {
            appCompatTextHelper.setTextSize(i, f);
        }
    }

    public void setTypeface(@Nullable Typeface typeface, int i) {
        Typeface create = (typeface == null || i <= 0) ? null : TypefaceCompat.create(getContext(), typeface, i);
        if (create != null) {
            typeface = create;
        }
        super.setTypeface(typeface, i);
    }
}
