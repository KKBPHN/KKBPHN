package miui.preference;

import android.content.Context;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.Preference.OnPreferenceClickListener;
import android.preference.PreferenceCategory;
import android.util.AttributeSet;

public class RadioButtonPreferenceCategory extends PreferenceCategory implements OnPreferenceChangeListener {
    private static final String TAG = "RadioButtonPreferenceCategory";
    private int mCheckedPosition;
    private RadioButtonPreference mRadioButtonPreference;

    public RadioButtonPreferenceCategory(Context context) {
        this(context, null);
    }

    public RadioButtonPreferenceCategory(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mCheckedPosition = -1;
    }

    public RadioButtonPreferenceCategory(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCheckedPosition = -1;
    }

    public boolean addPreference(Preference preference) {
        if (preference instanceof RadioButtonPreference) {
            boolean addPreference = super.addPreference(preference);
            if (addPreference) {
                preference.setOnPreferenceChangeListener(this);
            }
            return addPreference;
        }
        throw new IllegalArgumentException("Only RadioButtonPreference can be added toRadioButtonPreferenceCategory");
    }

    public int getCheckedPosition() {
        return this.mCheckedPosition;
    }

    public Preference getCheckedPreference() {
        return this.mRadioButtonPreference;
    }

    public boolean onPreferenceChange(Preference preference, Object obj) {
        if (preference != this.mRadioButtonPreference) {
            setCheckedPreference(preference);
        }
        OnPreferenceClickListener onPreferenceClickListener = getOnPreferenceClickListener();
        if (onPreferenceClickListener != null) {
            onPreferenceClickListener.onPreferenceClick(this);
        }
        return false;
    }

    public void setCheckedPosition(int i) {
        setCheckedPreference(getPreference(i));
    }

    public void setCheckedPreference(Preference preference) {
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            RadioButtonPreference radioButtonPreference = (RadioButtonPreference) getPreference(i);
            if (radioButtonPreference == preference) {
                this.mRadioButtonPreference = radioButtonPreference;
                this.mCheckedPosition = i;
                radioButtonPreference.setChecked(true);
            } else {
                radioButtonPreference.setChecked(false);
            }
        }
    }

    public void uncheckAllPreference() {
        int preferenceCount = getPreferenceCount();
        for (int i = 0; i < preferenceCount; i++) {
            ((RadioButtonPreference) getPreference(i)).setChecked(false);
        }
    }
}
