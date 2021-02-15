package miuix.preference;

import android.content.Context;
import android.graphics.drawable.AnimatedVectorDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.os.Build.VERSION;
import android.util.AttributeSet;
import android.view.View;
import android.widget.Checkable;
import android.widget.CompoundButton;
import androidx.preference.CheckBoxPreference;
import androidx.preference.PreferenceViewHolder;
import miuix.util.HapticFeedbackCompat;

public class RadioButtonPreference extends CheckBoxPreference implements Checkable {
    private boolean mChangeFromClick;
    private HapticFeedbackCompat mHapticFeedbackCompat;
    private OnPreferenceChangeInternalListener mInternalListener;
    private View mRadioButton;
    private boolean mValueChangedHaptic;

    public RadioButtonPreference(Context context) {
        this(context, null);
    }

    public RadioButtonPreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.radioButtonPreferenceStyle);
    }

    public RadioButtonPreference(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mValueChangedHaptic = true;
        this.mHapticFeedbackCompat = new HapticFeedbackCompat(context);
    }

    private void syncStartCheckAnim(CompoundButton compoundButton) {
        if (VERSION.SDK_INT >= 24) {
            Drawable buttonDrawable = compoundButton.getButtonDrawable();
            if (buttonDrawable instanceof StateListDrawable) {
                Drawable current = buttonDrawable.getCurrent();
                if (current instanceof AnimatedVectorDrawable) {
                    ((AnimatedVectorDrawable) current).start();
                }
            }
        }
    }

    public boolean callChangeListener(Object obj) {
        OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mInternalListener;
        boolean z = true;
        if (!(onPreferenceChangeInternalListener != null ? onPreferenceChangeInternalListener.onPreferenceChangeInternal(this, obj) : true) || !super.callChangeListener(obj)) {
            z = false;
        }
        if (!z && this.mChangeFromClick) {
            this.mChangeFromClick = false;
        }
        return z;
    }

    /* access modifiers changed from: protected */
    public void notifyChanged() {
        super.notifyChanged();
        OnPreferenceChangeInternalListener onPreferenceChangeInternalListener = this.mInternalListener;
        if (onPreferenceChangeInternalListener != null) {
            onPreferenceChangeInternalListener.notifyPreferenceChangeInternal(this);
        }
        if (this.mChangeFromClick && this.mValueChangedHaptic) {
            this.mHapticFeedbackCompat.performExtHapticFeedback(167);
            this.mChangeFromClick = false;
        }
    }

    public void onBindViewHolder(PreferenceViewHolder preferenceViewHolder) {
        super.onBindViewHolder(preferenceViewHolder);
        View view = preferenceViewHolder.itemView;
        View findViewById = view.findViewById(16908310);
        if (findViewById != null && (findViewById instanceof Checkable)) {
            ((Checkable) findViewById).setChecked(isChecked());
        }
        View findViewById2 = view.findViewById(16908304);
        if (findViewById2 != null && (findViewById2 instanceof Checkable)) {
            ((Checkable) findViewById2).setChecked(isChecked());
        }
        this.mRadioButton = view.findViewById(16908289);
        View view2 = this.mRadioButton;
        if (view2 != null) {
            view2.setImportantForAccessibility(2);
            if ((this.mRadioButton instanceof CompoundButton) && isChecked()) {
                syncStartCheckAnim((CompoundButton) this.mRadioButton);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        this.mChangeFromClick = true;
        super.onClick();
    }

    /* access modifiers changed from: 0000 */
    public void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
        this.mInternalListener = onPreferenceChangeInternalListener;
    }

    public void setValueChangedHaptic(boolean z) {
        this.mValueChangedHaptic = z;
    }

    public void toggle() {
        setChecked(!isChecked());
    }
}
