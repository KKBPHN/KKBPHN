package miuix.preference;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.Checkable;
import androidx.preference.Preference;
import androidx.preference.PreferenceCategory;

public class RadioButtonPreferenceCategory extends PreferenceCategory {
    private static final String TAG = "RadioButtonPreferenceCategory";
    private SingleChoiceHelper mCheckedChoice;
    private int mCheckedPosition;
    private OnPreferenceChangeInternalListener mInternalListener;

    class CategorySingleChoiceHelper extends SingleChoiceHelper {
        private RadioSetPreferenceCategory mCategory;

        CategorySingleChoiceHelper(RadioSetPreferenceCategory radioSetPreferenceCategory) {
            super(radioSetPreferenceCategory);
            this.mCategory = radioSetPreferenceCategory;
        }

        public Preference getPreference() {
            return this.mCategory;
        }

        public void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
            this.mCategory.setOnPreferenceChangeInternalListener(onPreferenceChangeInternalListener);
        }
    }

    class PreferenceSingleChoiceHelper extends SingleChoiceHelper {
        RadioButtonPreference mPreference;

        PreferenceSingleChoiceHelper(RadioButtonPreference radioButtonPreference) {
            super(radioButtonPreference);
            this.mPreference = radioButtonPreference;
        }

        public Preference getPreference() {
            return this.mPreference;
        }

        public void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener) {
            this.mPreference.setOnPreferenceChangeInternalListener(onPreferenceChangeInternalListener);
        }
    }

    abstract class SingleChoiceHelper implements Checkable {
        Checkable mCheckable;

        SingleChoiceHelper(Checkable checkable) {
            this.mCheckable = checkable;
        }

        public abstract Preference getPreference();

        public boolean isChecked() {
            return this.mCheckable.isChecked();
        }

        public void setChecked(boolean z) {
            this.mCheckable.setChecked(z);
        }

        public abstract void setOnPreferenceChangeInternalListener(OnPreferenceChangeInternalListener onPreferenceChangeInternalListener);

        public void toggle() {
            setChecked(!isChecked());
        }
    }

    public RadioButtonPreferenceCategory(Context context) {
        this(context, null);
    }

    public RadioButtonPreferenceCategory(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.preferenceCategoryRadioStyle);
    }

    public RadioButtonPreferenceCategory(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mCheckedChoice = null;
        this.mCheckedPosition = -1;
        this.mInternalListener = new OnPreferenceChangeInternalListener() {
            public void notifyPreferenceChangeInternal(Preference preference) {
                SingleChoiceHelper access$000 = RadioButtonPreferenceCategory.this.parse(preference);
                RadioButtonPreferenceCategory.this.updateCheckedPreference(access$000);
                RadioButtonPreferenceCategory.this.updateCheckedPosition(access$000);
            }

            public boolean onPreferenceChangeInternal(Preference preference, Object obj) {
                return !((Checkable) preference).isChecked();
            }
        };
    }

    private void clearChecked() {
        SingleChoiceHelper singleChoiceHelper = this.mCheckedChoice;
        if (singleChoiceHelper != null) {
            singleChoiceHelper.setChecked(false);
        }
        this.mCheckedChoice = null;
        this.mCheckedPosition = -1;
    }

    /* access modifiers changed from: private */
    public SingleChoiceHelper parse(Preference preference) {
        if (preference instanceof RadioButtonPreference) {
            return new PreferenceSingleChoiceHelper((RadioButtonPreference) preference);
        }
        if (preference instanceof RadioSetPreferenceCategory) {
            return new CategorySingleChoiceHelper((RadioSetPreferenceCategory) preference);
        }
        throw new IllegalArgumentException("Only RadioButtonPreference or RadioSetPreferenceCategory can be added to RadioButtonPreferenceCategory");
    }

    private void setCheckedPreferenceInternal(SingleChoiceHelper singleChoiceHelper) {
        singleChoiceHelper.setChecked(true);
    }

    /* access modifiers changed from: private */
    public void updateCheckedPosition(SingleChoiceHelper singleChoiceHelper) {
        if (singleChoiceHelper.isChecked()) {
            int preferenceCount = getPreferenceCount();
            for (int i = 0; i < preferenceCount; i++) {
                if (getPreference(i) == singleChoiceHelper.getPreference()) {
                    this.mCheckedPosition = i;
                    return;
                }
            }
        }
    }

    /* access modifiers changed from: private */
    public void updateCheckedPreference(SingleChoiceHelper singleChoiceHelper) {
        if (singleChoiceHelper.isChecked()) {
            SingleChoiceHelper singleChoiceHelper2 = this.mCheckedChoice;
            if (!(singleChoiceHelper2 == null || singleChoiceHelper2.getPreference() == singleChoiceHelper.getPreference())) {
                this.mCheckedChoice.setChecked(false);
            }
            this.mCheckedChoice = singleChoiceHelper;
        }
    }

    public boolean addPreference(Preference preference) {
        SingleChoiceHelper parse = parse(preference);
        boolean addPreference = super.addPreference(preference);
        if (addPreference) {
            parse.setOnPreferenceChangeInternalListener(this.mInternalListener);
        }
        if (parse.isChecked()) {
            if (this.mCheckedChoice == null) {
                this.mCheckedChoice = parse;
            } else {
                throw new IllegalStateException("Already has a checked item, please check state of new add preference");
            }
        }
        return addPreference;
    }

    public int getCheckedPosition() {
        if (this.mCheckedPosition == -1) {
            SingleChoiceHelper singleChoiceHelper = this.mCheckedChoice;
            if (singleChoiceHelper != null) {
                updateCheckedPosition(singleChoiceHelper);
            }
        }
        return this.mCheckedPosition;
    }

    public Preference getCheckedPreference() {
        SingleChoiceHelper singleChoiceHelper = this.mCheckedChoice;
        if (singleChoiceHelper == null) {
            return null;
        }
        return singleChoiceHelper.getPreference();
    }

    public boolean removePreference(Preference preference) {
        SingleChoiceHelper parse = parse(preference);
        boolean removePreference = super.removePreference(preference);
        if (removePreference) {
            parse.setOnPreferenceChangeInternalListener(null);
            if (parse.isChecked()) {
                parse.setChecked(false);
                this.mCheckedPosition = -1;
                this.mCheckedChoice = null;
            }
        }
        return removePreference;
    }

    public void setCheckedPosition(int i) {
        SingleChoiceHelper parse = parse(getPreference(i));
        if (!parse.isChecked()) {
            setCheckedPreferenceInternal(parse);
            updateCheckedPreference(parse);
            this.mCheckedPosition = i;
        }
    }

    public void setCheckedPreference(Preference preference) {
        if (preference == null) {
            clearChecked();
            return;
        }
        SingleChoiceHelper parse = parse(preference);
        if (!parse.isChecked()) {
            setCheckedPreferenceInternal(parse);
            updateCheckedPreference(parse);
            updateCheckedPosition(parse);
        }
    }
}
