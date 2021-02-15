package miui.animation.font;

import android.view.View;
import android.widget.TextView;
import java.lang.ref.WeakReference;
import java.util.Objects;
import miui.animation.property.ISpecificProperty;
import miui.animation.property.ViewProperty;

public class FontWeightProperty extends ViewProperty implements ISpecificProperty {
    private static final String NAME = "fontweight";
    private float mCurWeight = Float.MAX_VALUE;
    private int mFontType;
    private WeakReference mTextViewRef;

    public FontWeightProperty(TextView textView, int i) {
        super(NAME);
        this.mTextViewRef = new WeakReference(textView);
        this.mFontType = i;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || FontWeightProperty.class != obj.getClass() || !super.equals(obj)) {
            return false;
        }
        TextView textView = (TextView) this.mTextViewRef.get();
        TextView textView2 = (TextView) ((FontWeightProperty) obj).mTextViewRef.get();
        if (textView == null || !textView.equals(textView2)) {
            z = false;
        }
        return z;
    }

    public int getFontType() {
        return this.mFontType;
    }

    public float getScaledTextSize() {
        TextView textView = (TextView) this.mTextViewRef.get();
        if (textView != null) {
            return textView.getTextSize() / textView.getResources().getDisplayMetrics().scaledDensity;
        }
        return 0.0f;
    }

    public float getSpecificValue(float f) {
        if (f >= ((float) VarFontUtils.MIN_WGHT)) {
            return f;
        }
        return (float) VarFontUtils.getScaleWght((int) f, getScaledTextSize(), this.mFontType, VarFontUtils.getSysFontScale());
    }

    public TextView getTextView() {
        return (TextView) this.mTextViewRef.get();
    }

    public float getValue(View view) {
        return this.mCurWeight;
    }

    public int hashCode() {
        TextView textView = (TextView) this.mTextViewRef.get();
        if (textView != null) {
            return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), textView});
        }
        return Objects.hash(new Object[]{Integer.valueOf(super.hashCode()), this.mTextViewRef});
    }

    public void setValue(View view, float f) {
        this.mCurWeight = f;
        TextView textView = (TextView) this.mTextViewRef.get();
        if (textView != null) {
            VarFontUtils.setVariationFont(textView, (int) f);
        }
    }
}
