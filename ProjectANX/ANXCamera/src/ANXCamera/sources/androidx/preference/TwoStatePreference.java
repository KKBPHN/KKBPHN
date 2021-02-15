package androidx.preference;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.View;
import android.widget.TextView;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.preference.Preference.BaseSavedState;

public abstract class TwoStatePreference extends Preference {
    protected boolean mChecked;
    private boolean mCheckedSet;
    private boolean mDisableDependentsState;
    private CharSequence mSummaryOff;
    private CharSequence mSummaryOn;

    class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        boolean mChecked;

        SavedState(Parcel parcel) {
            super(parcel);
            boolean z = true;
            if (parcel.readInt() != 1) {
                z = false;
            }
            this.mChecked = z;
        }

        SavedState(Parcelable parcelable) {
            super(parcelable);
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mChecked ? 1 : 0);
        }
    }

    public TwoStatePreference(Context context) {
        this(context, null);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet, int i) {
        this(context, attributeSet, i, 0);
    }

    public TwoStatePreference(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
    }

    public boolean getDisableDependentsState() {
        return this.mDisableDependentsState;
    }

    public CharSequence getSummaryOff() {
        return this.mSummaryOff;
    }

    public CharSequence getSummaryOn() {
        return this.mSummaryOn;
    }

    public boolean isChecked() {
        return this.mChecked;
    }

    /* access modifiers changed from: protected */
    public void onClick() {
        super.onClick();
        boolean z = !isChecked();
        if (callChangeListener(Boolean.valueOf(z))) {
            setChecked(z);
        }
    }

    /* access modifiers changed from: protected */
    public Object onGetDefaultValue(TypedArray typedArray, int i) {
        return Boolean.valueOf(typedArray.getBoolean(i, false));
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        if (parcelable == null || !parcelable.getClass().equals(SavedState.class)) {
            super.onRestoreInstanceState(parcelable);
            return;
        }
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setChecked(savedState.mChecked);
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        Parcelable onSaveInstanceState = super.onSaveInstanceState();
        if (isPersistent()) {
            return onSaveInstanceState;
        }
        SavedState savedState = new SavedState(onSaveInstanceState);
        savedState.mChecked = isChecked();
        return savedState;
    }

    /* access modifiers changed from: protected */
    public void onSetInitialValue(Object obj) {
        if (obj == null) {
            obj = Boolean.valueOf(false);
        }
        setChecked(getPersistedBoolean(((Boolean) obj).booleanValue()));
    }

    public void setChecked(boolean z) {
        boolean z2 = this.mChecked != z;
        if (z2 || !this.mCheckedSet) {
            this.mChecked = z;
            this.mCheckedSet = true;
            persistBoolean(z);
            if (z2) {
                notifyDependencyChange(shouldDisableDependents());
                notifyChanged();
            }
        }
    }

    public void setDisableDependentsState(boolean z) {
        this.mDisableDependentsState = z;
    }

    public void setSummaryOff(int i) {
        setSummaryOff((CharSequence) getContext().getString(i));
    }

    public void setSummaryOff(CharSequence charSequence) {
        this.mSummaryOff = charSequence;
        if (!isChecked()) {
            notifyChanged();
        }
    }

    public void setSummaryOn(int i) {
        setSummaryOn((CharSequence) getContext().getString(i));
    }

    public void setSummaryOn(CharSequence charSequence) {
        this.mSummaryOn = charSequence;
        if (isChecked()) {
            notifyChanged();
        }
    }

    public boolean shouldDisableDependents() {
        boolean z = this.mDisableDependentsState ? this.mChecked : !this.mChecked;
        return z || super.shouldDisableDependents();
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Removed duplicated region for block: B:15:0x002d  */
    /* JADX WARNING: Removed duplicated region for block: B:20:0x003f  */
    /* JADX WARNING: Removed duplicated region for block: B:23:0x0046  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @RestrictTo({Scope.LIBRARY})
    public void syncSummaryView(View view) {
        int i;
        CharSequence charSequence;
        if (view instanceof TextView) {
            TextView textView = (TextView) view;
            boolean z = true;
            if (!this.mChecked || TextUtils.isEmpty(this.mSummaryOn)) {
                if (!this.mChecked && !TextUtils.isEmpty(this.mSummaryOff)) {
                    charSequence = this.mSummaryOff;
                }
                if (z) {
                    CharSequence summary = getSummary();
                    if (!TextUtils.isEmpty(summary)) {
                        textView.setText(summary);
                        z = false;
                    }
                }
                i = 8;
                if (!z) {
                    i = 0;
                }
                if (i != textView.getVisibility()) {
                    textView.setVisibility(i);
                }
            }
            charSequence = this.mSummaryOn;
            textView.setText(charSequence);
            z = false;
            if (z) {
            }
            i = 8;
            if (!z) {
            }
            if (i != textView.getVisibility()) {
            }
        }
    }

    /* access modifiers changed from: protected */
    public void syncSummaryView(PreferenceViewHolder preferenceViewHolder) {
        syncSummaryView(preferenceViewHolder.findViewById(16908304));
    }
}
