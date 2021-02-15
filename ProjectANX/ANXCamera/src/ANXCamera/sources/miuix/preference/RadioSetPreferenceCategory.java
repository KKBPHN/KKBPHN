package miuix.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.Checkable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

public class RadioSetPreferenceCategory extends PreferenceCategory implements Checkable {
    private boolean mChecked;
    private boolean mCheckedSet;
    /* access modifiers changed from: private */
    public OnPreferenceChangeInternalListener mInternalListener;
    private OnPreferenceChangeInternalListener mInternalListenerProxy;
    private String mPrimaryKey;
    private RadioButtonPreference mPrimaryPreference;

    public RadioSetPreferenceCategory(Context context) {
        this(context, null);
    }

    public RadioSetPreferenceCategory(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.preferenceCategoryCheckableStyle);
    }

    public RadioSetPreferenceCategory(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public RadioSetPreferenceCategory(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        this.mInternalListenerProxy = new OnPreferenceChangeInternalListener() {
            public void notifyPreferenceChangeInternal(Preference preference) {
                if (preference instanceof RadioButtonPreference) {
                    RadioSetPreferenceCategory.this.setChecked(((RadioButtonPreference) preference).isChecked());
                }
                if (RadioSetPreferenceCategory.this.mInternalListener != null) {
                    RadioSetPreferenceCategory.this.mInternalListener.notifyPreferenceChangeInternal(preference);
                }
            }

            public boolean onPreferenceChangeInternal(Preference preference, Object obj) {
                if (RadioSetPreferenceCategory.this.mInternalListener != null) {
                    return RadioSetPreferenceCategory.this.mInternalListener.onPreferenceChangeInternal(preference, obj);
                }
                return true;
            }
        };
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.RadioSetPreferenceCategory, i, i2);
        this.mPrimaryKey = obtainStyledAttributes.getString(R.styleable.RadioSetPreferenceCategory_primaryKey);
        obtainStyledAttributes.recycle();
    }

    public boolean addPreference(Preference preference) {
        String str = this.mPrimaryKey;
        if (str == null) {
            if (getPreferenceCount() == 0) {
                if (!(preference instanceof RadioButtonPreference)) {
                    throw new IllegalArgumentException("The first preference must be RadioButtonPreference, if primary key is empty");
                }
            }
            return super.addPreference(preference);
        }
        if (str.equals(preference.getKey())) {
            RadioButtonPreference radioButtonPreference = this.mPrimaryPreference;
            if (radioButtonPreference != null && radioButtonPreference != preference) {
                throw new IllegalArgumentException("must not have two primary preference");
            } else if (!(preference instanceof RadioButtonPreference)) {
                throw new IllegalArgumentException("Primary preference must be RadioButtonPreference");
            }
        }
        return super.addPreference(preference);
        this.mPrimaryPreference = (RadioButtonPreference) preference;
        this.mPrimaryPreference.setOnPreferenceChangeInternalListener(this.mInternalListenerProxy);
        return super.addPreference(preference);
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    public void setChecked(boolean z) {
        if ((this.mChecked != z) || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
        }
    }

    /* access modifiers changed from: 0000 */
    public void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
        this.mInternalListener = onPreferenceChangeInternalListener;
    }

    public void toggle() {
        setChecked(!isChecked());
    }
}
