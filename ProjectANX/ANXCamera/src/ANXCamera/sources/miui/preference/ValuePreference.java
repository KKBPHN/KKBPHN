package miui.preference;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.preference.Preference;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import miui.R;

public class ValuePreference extends Preference {
    private Drawable mRightArrowDrawable;
    private boolean mShowRightArrow;
    private CharSequence mValue;
    private int mValueRes;

    public ValuePreference(Context context) {
        this(context, null);
    }

    public ValuePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.valuePreferenceStyle);
    }

    public ValuePreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
    }

    public Drawable getRightArrowDrawable() {
        return this.mRightArrowDrawable;
    }

    public CharSequence getValue() {
        return this.mValue;
    }

    public int getValueRes() {
        return this.mValueRes;
    }

    public boolean isShowRightArrow() {
        return this.mShowRightArrow;
    }

    /* access modifiers changed from: protected */
    public void onBindView(View view) {
        super.onBindView(view);
        TextView textView = (TextView) view.findViewById(R.id.value_right);
        if (textView != null) {
            CharSequence value = getValue();
            if (!TextUtils.isEmpty(value)) {
                textView.setText(value);
                textView.setVisibility(0);
            } else {
                textView.setVisibility(8);
            }
        }
        ImageView imageView = (ImageView) view.findViewById(com.miui.internal.R.id.arrow_right);
        if (imageView != null) {
            imageView.setVisibility(this.mShowRightArrow ? 0 : 8);
            if (this.mShowRightArrow) {
                imageView.setVisibility(0);
                Drawable drawable = this.mRightArrowDrawable;
                if (drawable != null) {
                    imageView.setImageDrawable(drawable);
                    return;
                }
                return;
            }
            imageView.setVisibility(8);
        }
    }

    public void setRightArrowDrawable(Drawable drawable) {
        if ((drawable == null && this.mRightArrowDrawable != null) || (drawable != null && this.mRightArrowDrawable != drawable)) {
            this.mRightArrowDrawable = drawable;
            setShowRightArrow(drawable != null);
            notifyChanged();
        }
    }

    public void setRightArrowRes(int i) {
        setRightArrowDrawable(getContext().getResources().getDrawable(i));
    }

    public void setShowRightArrow(boolean z) {
        this.mShowRightArrow = z;
    }

    public void setValue(int i) {
        setValue(getContext().getString(i));
        this.mValueRes = i;
    }

    public void setValue(String str) {
        if (!TextUtils.equals(str, this.mValue)) {
            this.mValueRes = 0;
            this.mValue = str;
            notifyChanged();
        }
    }
}
