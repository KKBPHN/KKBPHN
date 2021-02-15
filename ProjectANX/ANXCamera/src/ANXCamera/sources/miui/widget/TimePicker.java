package miui.widget;

import android.content.Context;
import android.content.res.Configuration;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.BaseSavedState;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.miui.internal.R;
import java.util.Locale;
import miui.date.Calendar;
import miui.date.CalendarFormatSymbols;
import miui.date.DateUtils;
import miui.widget.NumberPicker.Formatter;
import miui.widget.NumberPicker.OnValueChangeListener;

public class TimePicker extends FrameLayout {
    private static final boolean DEFAULT_ENABLED_STATE = true;
    private static final int HOURS_IN_HALF_DAY = 12;
    private static final OnTimeChangedListener NO_OP_CHANGE_LISTENER = new OnTimeChangedListener() {
        public void onTimeChanged(TimePicker timePicker, int i, int i2) {
        }
    };
    private final Button mAmPmButton;
    private final NumberPicker mAmPmSpinner;
    private Locale mCurrentLocale;
    private final NumberPicker mHourSpinner;
    private boolean mIs24HourView;
    /* access modifiers changed from: private */
    public boolean mIsAm;
    private boolean mIsEnabled;
    private final NumberPicker mMinuteSpinner;
    private OnTimeChangedListener mOnTimeChangedListener;
    private Calendar mTempCalendar;

    public interface OnTimeChangedListener {
        void onTimeChanged(TimePicker timePicker, int i, int i2);
    }

    class SavedState extends BaseSavedState {
        public static final Creator CREATOR = new Creator() {
            public SavedState createFromParcel(Parcel parcel) {
                return new SavedState(parcel);
            }

            public SavedState[] newArray(int i) {
                return new SavedState[i];
            }
        };
        private final int mHour;
        private final int mMinute;

        private SavedState(Parcel parcel) {
            super(parcel);
            this.mHour = parcel.readInt();
            this.mMinute = parcel.readInt();
        }

        private SavedState(Parcelable parcelable, int i, int i2) {
            super(parcelable);
            this.mHour = i;
            this.mMinute = i2;
        }

        public int getHour() {
            return this.mHour;
        }

        public int getMinute() {
            return this.mMinute;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeInt(this.mHour);
            parcel.writeInt(this.mMinute);
        }
    }

    public TimePicker(Context context) {
        this(context, null);
    }

    public TimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, 0);
    }

    public TimePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mIsEnabled = true;
        setCurrentLocale(Locale.getDefault());
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.time_picker, this, true);
        this.mHourSpinner = (NumberPicker) findViewById(R.id.hour);
        this.mHourSpinner.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                if (!TimePicker.this.is24HourView() && ((i == 11 && i2 == 12) || (i == 12 && i2 == 11))) {
                    TimePicker timePicker = TimePicker.this;
                    timePicker.mIsAm = !timePicker.mIsAm;
                    TimePicker.this.updateAmPmControl();
                }
                TimePicker.this.onTimeChanged();
            }
        });
        ((EditText) this.mHourSpinner.findViewById(R.id.numberpicker_input)).setImeOptions(5);
        this.mMinuteSpinner = (NumberPicker) findViewById(R.id.minute);
        this.mMinuteSpinner.setMinValue(0);
        this.mMinuteSpinner.setMaxValue(59);
        this.mMinuteSpinner.setOnLongPressUpdateInterval(100);
        this.mMinuteSpinner.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        this.mMinuteSpinner.setOnValueChangedListener(new OnValueChangeListener() {
            public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                TimePicker.this.onTimeChanged();
            }
        });
        ((EditText) this.mMinuteSpinner.findViewById(R.id.numberpicker_input)).setImeOptions(5);
        View findViewById = findViewById(R.id.amPm);
        if (findViewById instanceof Button) {
            this.mAmPmSpinner = null;
            this.mAmPmButton = (Button) findViewById;
            this.mAmPmButton.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    view.requestFocus();
                    TimePicker timePicker = TimePicker.this;
                    timePicker.mIsAm = !timePicker.mIsAm;
                    TimePicker.this.updateAmPmControl();
                    TimePicker.this.onTimeChanged();
                }
            });
        } else {
            this.mAmPmButton = null;
            this.mAmPmSpinner = (NumberPicker) findViewById;
            this.mAmPmSpinner.setMinValue(0);
            this.mAmPmSpinner.setMaxValue(1);
            this.mAmPmSpinner.setDisplayedValues(CalendarFormatSymbols.getDefault().getAmPms());
            this.mAmPmSpinner.setOnValueChangedListener(new OnValueChangeListener() {
                public void onValueChange(NumberPicker numberPicker, int i, int i2) {
                    numberPicker.requestFocus();
                    TimePicker timePicker = TimePicker.this;
                    timePicker.mIsAm = !timePicker.mIsAm;
                    TimePicker.this.updateAmPmControl();
                    TimePicker.this.onTimeChanged();
                }
            });
            ((EditText) this.mAmPmSpinner.findViewById(R.id.numberpicker_input)).setImeOptions(6);
        }
        if (isAmPmAtStart()) {
            ViewGroup viewGroup = (ViewGroup) findViewById(R.id.timePickerLayout);
            viewGroup.removeView(findViewById);
            viewGroup.addView(findViewById, 0);
        }
        updateHourControl();
        updateAmPmControl();
        setOnTimeChangedListener(NO_OP_CHANGE_LISTENER);
        setCurrentHour(Integer.valueOf(this.mTempCalendar.get(18)));
        setCurrentMinute(Integer.valueOf(this.mTempCalendar.get(20)));
        if (!isEnabled()) {
            setEnabled(false);
        }
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    private boolean isAmPmAtStart() {
        boolean startsWith = getContext().getString(R.string.fmt_time_12hour_pm).startsWith(SupportedConfigFactory.CLOSE_BY_HHT);
        return getResources().getConfiguration().getLayoutDirection() == 1 ? !startsWith : startsWith;
    }

    /* access modifiers changed from: private */
    public void onTimeChanged() {
        sendAccessibilityEvent(4);
        OnTimeChangedListener onTimeChangedListener = this.mOnTimeChangedListener;
        if (onTimeChangedListener != null) {
            onTimeChangedListener.onTimeChanged(this, getCurrentHour().intValue(), getCurrentMinute().intValue());
        }
    }

    private void setCurrentLocale(Locale locale) {
        if (!locale.equals(this.mCurrentLocale)) {
            this.mCurrentLocale = locale;
            if (this.mTempCalendar == null) {
                this.mTempCalendar = new Calendar();
            }
        }
    }

    private void trySetContentDescription(View view, int i, int i2) {
        View findViewById = view.findViewById(i);
        if (findViewById != null) {
            findViewById.setContentDescription(getContext().getString(i2));
        }
    }

    /* access modifiers changed from: private */
    public void updateAmPmControl() {
        if (is24HourView()) {
            NumberPicker numberPicker = this.mAmPmSpinner;
            if (numberPicker != null) {
                numberPicker.setVisibility(8);
            } else {
                this.mAmPmButton.setVisibility(8);
            }
        } else {
            int i = !this.mIsAm;
            NumberPicker numberPicker2 = this.mAmPmSpinner;
            if (numberPicker2 != null) {
                numberPicker2.setValue(i);
                this.mAmPmSpinner.setVisibility(0);
            } else {
                this.mAmPmButton.setText(CalendarFormatSymbols.getDefault().getAmPms()[i]);
                this.mAmPmButton.setVisibility(0);
            }
        }
        sendAccessibilityEvent(4);
    }

    private void updateHourControl() {
        NumberPicker numberPicker;
        Formatter formatter;
        if (is24HourView()) {
            this.mHourSpinner.setMinValue(0);
            this.mHourSpinner.setMaxValue(23);
            numberPicker = this.mHourSpinner;
            formatter = NumberPicker.TWO_DIGIT_FORMATTER;
        } else {
            this.mHourSpinner.setMinValue(1);
            this.mHourSpinner.setMaxValue(12);
            numberPicker = this.mHourSpinner;
            formatter = null;
        }
        numberPicker.setFormatter(formatter);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    public int getBaseline() {
        return this.mHourSpinner.getBaseline();
    }

    public Integer getCurrentHour() {
        int value = this.mHourSpinner.getValue();
        if (is24HourView()) {
            return Integer.valueOf(value);
        }
        int i = value % 12;
        return this.mIsAm ? Integer.valueOf(i) : Integer.valueOf(i + 12);
    }

    public Integer getCurrentMinute() {
        return Integer.valueOf(this.mMinuteSpinner.getValue());
    }

    public boolean is24HourView() {
        return this.mIs24HourView;
    }

    public boolean isEnabled() {
        return this.mIsEnabled;
    }

    /* access modifiers changed from: protected */
    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        setCurrentLocale(configuration.locale);
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(TimePicker.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(TimePicker.class.getName());
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        int i = this.mIs24HourView ? 44 : 28;
        this.mTempCalendar.set(18, getCurrentHour().intValue());
        this.mTempCalendar.set(20, getCurrentMinute().intValue());
        accessibilityEvent.getText().add(DateUtils.formatDateTime(this.mTempCalendar.getTimeInMillis(), i));
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        setCurrentHour(Integer.valueOf(savedState.getHour()));
        setCurrentMinute(Integer.valueOf(savedState.getMinute()));
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getCurrentHour().intValue(), getCurrentMinute().intValue());
    }

    public void setCurrentHour(Integer num) {
        if (num != null && !num.equals(getCurrentHour())) {
            if (!is24HourView()) {
                if (num.intValue() >= 12) {
                    this.mIsAm = false;
                    if (num.intValue() > 12) {
                        num = Integer.valueOf(num.intValue() - 12);
                    }
                } else {
                    this.mIsAm = true;
                    if (num.intValue() == 0) {
                        num = Integer.valueOf(12);
                    }
                }
                updateAmPmControl();
            }
            this.mHourSpinner.setValue(num.intValue());
            onTimeChanged();
        }
    }

    public void setCurrentMinute(Integer num) {
        if (!num.equals(getCurrentMinute())) {
            this.mMinuteSpinner.setValue(num.intValue());
            onTimeChanged();
        }
    }

    public void setEnabled(boolean z) {
        if (this.mIsEnabled != z) {
            super.setEnabled(z);
            this.mMinuteSpinner.setEnabled(z);
            this.mHourSpinner.setEnabled(z);
            NumberPicker numberPicker = this.mAmPmSpinner;
            if (numberPicker != null) {
                numberPicker.setEnabled(z);
            } else {
                this.mAmPmButton.setEnabled(z);
            }
            this.mIsEnabled = z;
        }
    }

    public void setIs24HourView(Boolean bool) {
        if (this.mIs24HourView != bool.booleanValue()) {
            this.mIs24HourView = bool.booleanValue();
            int intValue = getCurrentHour().intValue();
            updateHourControl();
            setCurrentHour(Integer.valueOf(intValue));
            updateAmPmControl();
        }
    }

    public void setOnTimeChangedListener(OnTimeChangedListener onTimeChangedListener) {
        this.mOnTimeChangedListener = onTimeChangedListener;
    }
}
