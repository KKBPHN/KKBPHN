package miui.widget;

import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View.BaseSavedState;
import android.view.ViewGroup;
import android.view.accessibility.AccessibilityEvent;
import android.view.accessibility.AccessibilityNodeInfo;
import android.widget.LinearLayout;
import com.android.camera.data.data.config.SupportedConfigFactory;
import com.miui.internal.R;
import java.util.Locale;
import miui.date.Calendar;
import miui.date.DateUtils;
import miui.util.AppConstants;
import miui.widget.NumberPicker.OnValueChangeListener;

public class DateTimePicker extends LinearLayout {
    private static final int DAYPICKER_ALL_ITEM_MAX_VALUE = 4;
    private static final int DAYPICKER_WHEEL_ITEM_COUNT = 5;
    private static final DayFormatter DEFAULT_DAY_FORMATTER = new DayFormatter();
    private static final int DEFAULT_MINUTE_INTERVAL = 1;
    private static final int HALF_WHEEL_ITEM_COUNT = 1;
    private static final ThreadLocal sCalCache = new ThreadLocal();
    /* access modifiers changed from: private */
    public static ThreadLocal sCalendarCache = new ThreadLocal();
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    String[] mDayDisplayValues;
    private DayFormatter mDayFormatter;
    /* access modifiers changed from: private */
    public int mDayLastValue;
    /* access modifiers changed from: private */
    public NumberPicker mDayPicker;
    /* access modifiers changed from: private */
    public NumberPicker mHourPicker;
    private boolean mIsLunarMode;
    /* access modifiers changed from: private */
    public OnDateTimeChangedListener mListener;
    private DayFormatter mLunarFormatter;
    private Calendar mMaxDate;
    private Calendar mMinDate;
    private String[] mMinuteDisplayValues;
    /* access modifiers changed from: private */
    public int mMinuteInterval;
    /* access modifiers changed from: private */
    public NumberPicker mMinutePicker;

    public class DayFormatter {
        public String formatDay(int i, int i2, int i3) {
            Calendar calendar = (Calendar) DateTimePicker.sCalendarCache.get();
            if (calendar == null) {
                calendar = new Calendar();
                DateTimePicker.sCalendarCache.set(calendar);
            }
            calendar.set(1, i);
            calendar.set(5, i2);
            calendar.set(9, i3);
            if (!Locale.getDefault().getLanguage().equals(Locale.CHINESE.getLanguage())) {
                return DateUtils.formatDateTime(calendar.getTimeInMillis(), 13696);
            }
            String formatDateTime = DateUtils.formatDateTime(calendar.getTimeInMillis(), 4480);
            String formatDateTime2 = DateUtils.formatDateTime(calendar.getTimeInMillis(), 9216);
            StringBuilder sb = new StringBuilder();
            String str = " ";
            sb.append(formatDateTime.replace(str, ""));
            sb.append(str);
            sb.append(formatDateTime2);
            return sb.toString();
        }
    }

    public class LunarFormatter extends DayFormatter {
        public String formatDay(int i, int i2, int i3) {
            Calendar calendar = (Calendar) DateTimePicker.sCalendarCache.get();
            if (calendar == null) {
                calendar = new Calendar();
                DateTimePicker.sCalendarCache.set(calendar);
            }
            calendar.set(1, i);
            calendar.set(5, i2);
            calendar.set(9, i3);
            return calendar.format(AppConstants.getCurrentApplication().getString(R.string.fmt_chinese_date));
        }
    }

    public interface OnDateTimeChangedListener {
        void onDateTimeChanged(DateTimePicker dateTimePicker, long j);
    }

    class PickerValueChangeListener implements OnValueChangeListener {
        private PickerValueChangeListener() {
        }

        private void notifyTimeChanged(DateTimePicker dateTimePicker) {
            DateTimePicker.this.sendAccessibilityEvent(4);
            if (DateTimePicker.this.mListener != null) {
                DateTimePicker.this.mListener.onDateTimeChanged(dateTimePicker, DateTimePicker.this.getTimeInMillis());
            }
        }

        public void onValueChange(NumberPicker numberPicker, int i, int i2) {
            Calendar access$400;
            int i3;
            int access$700;
            if (numberPicker == DateTimePicker.this.mDayPicker) {
                int i4 = 1;
                if (((numberPicker.getValue() - DateTimePicker.this.mDayLastValue) + 5) % 5 != 1) {
                    i4 = -1;
                }
                DateTimePicker.this.mCalendar.add(12, i4);
                DateTimePicker.this.mDayLastValue = numberPicker.getValue();
            } else {
                if (numberPicker == DateTimePicker.this.mHourPicker) {
                    access$400 = DateTimePicker.this.mCalendar;
                    i3 = 18;
                    access$700 = DateTimePicker.this.mHourPicker.getValue();
                } else if (numberPicker == DateTimePicker.this.mMinutePicker) {
                    access$400 = DateTimePicker.this.mCalendar;
                    i3 = 20;
                    access$700 = DateTimePicker.this.mMinuteInterval * DateTimePicker.this.mMinutePicker.getValue();
                }
                access$400.set(i3, access$700);
            }
            DateTimePicker.this.checkCurrentTime();
            DateTimePicker.this.updateDayPicker();
            DateTimePicker.this.updateHourPicker();
            DateTimePicker.this.updateMinutePicker();
            notifyTimeChanged(DateTimePicker.this);
        }
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
        private long nTimeInMillis;

        public SavedState(Parcel parcel) {
            super(parcel);
            this.nTimeInMillis = parcel.readLong();
        }

        public SavedState(Parcelable parcelable, long j) {
            super(parcelable);
            this.nTimeInMillis = j;
        }

        public long getTimeInMillis() {
            return this.nTimeInMillis;
        }

        public void writeToParcel(Parcel parcel, int i) {
            super.writeToParcel(parcel, i);
            parcel.writeLong(this.nTimeInMillis);
        }
    }

    public DateTimePicker(Context context) {
        this(context, null);
    }

    public DateTimePicker(Context context, AttributeSet attributeSet) {
        this(context, attributeSet, R.attr.dateTimePickerStyle);
    }

    public DateTimePicker(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        this.mMinuteInterval = 1;
        this.mMinDate = null;
        this.mMaxDate = null;
        this.mDayDisplayValues = null;
        this.mIsLunarMode = false;
        ((LayoutInflater) context.getSystemService("layout_inflater")).inflate(R.layout.datetime_picker, this, true);
        PickerValueChangeListener pickerValueChangeListener = new PickerValueChangeListener();
        this.mCalendar = new Calendar();
        adjustCalendar(this.mCalendar, true);
        Calendar calendar = (Calendar) sCalCache.get();
        if (calendar == null) {
            calendar = new Calendar();
            sCalCache.set(calendar);
        }
        calendar.setTimeInMillis(0);
        this.mDayPicker = (NumberPicker) findViewById(R.id.day);
        this.mHourPicker = (NumberPicker) findViewById(R.id.hour);
        this.mMinutePicker = (NumberPicker) findViewById(R.id.minute);
        this.mDayPicker.setOnValueChangedListener(pickerValueChangeListener);
        this.mDayPicker.setMaxFlingSpeedFactor(3.0f);
        this.mHourPicker.setOnValueChangedListener(pickerValueChangeListener);
        this.mMinutePicker.setOnValueChangedListener(pickerValueChangeListener);
        this.mMinutePicker.setMinValue(0);
        this.mMinutePicker.setMaxValue(59);
        this.mHourPicker.setFormatter(NumberPicker.TWO_DIGIT_FORMATTER);
        TypedArray obtainStyledAttributes = context.obtainStyledAttributes(attributeSet, R.styleable.DateTimePicker, i, 0);
        this.mIsLunarMode = obtainStyledAttributes.getBoolean(R.styleable.DateTimePicker_lunarCalendar, false);
        obtainStyledAttributes.recycle();
        reorderLayout();
        checkCurrentTime();
        updateDayPicker();
        updateHourPicker();
        updateMinutePicker();
        if (getImportantForAccessibility() == 0) {
            setImportantForAccessibility(1);
        }
    }

    private void adjustCalendar(Calendar calendar, boolean z) {
        calendar.set(22, 0);
        calendar.set(21, 0);
        int i = calendar.get(20);
        int i2 = this.mMinuteInterval;
        int i3 = i % i2;
        if (i3 != 0) {
            calendar.add(20, z ? i2 - i3 : -i3);
        }
    }

    /* access modifiers changed from: private */
    public void checkCurrentTime() {
        Calendar calendar = this.mMinDate;
        if (calendar != null && calendar.getTimeInMillis() > this.mCalendar.getTimeInMillis()) {
            this.mCalendar.setTimeInMillis(this.mMinDate.getTimeInMillis());
        }
        Calendar calendar2 = this.mMaxDate;
        if (calendar2 != null && calendar2.getTimeInMillis() < this.mCalendar.getTimeInMillis()) {
            this.mCalendar.setTimeInMillis(this.mMaxDate.getTimeInMillis());
        }
    }

    private void checkDisplayeValid(NumberPicker numberPicker, int i, int i2) {
        String[] displayedValues = numberPicker.getDisplayedValues();
        if (displayedValues != null && displayedValues.length < (i2 - i) + 1) {
            numberPicker.setDisplayedValues(null);
        }
    }

    private int computeDayCount(Calendar calendar, Calendar calendar2) {
        Calendar calendar3 = (Calendar) calendar.clone();
        Calendar calendar4 = (Calendar) calendar2.clone();
        calendar3.set(18, 0);
        calendar3.set(20, 0);
        calendar3.set(21, 0);
        calendar3.set(22, 0);
        calendar4.set(18, 0);
        calendar4.set(20, 0);
        calendar4.set(21, 0);
        calendar4.set(22, 0);
        return (int) (((((calendar3.getTimeInMillis() / 1000) / 60) / 60) / 24) - ((((calendar4.getTimeInMillis() / 1000) / 60) / 60) / 24));
    }

    private String formatDay(int i, int i2, int i3) {
        DayFormatter dayFormatter = DEFAULT_DAY_FORMATTER;
        if (this.mIsLunarMode) {
            if (this.mLunarFormatter == null) {
                this.mLunarFormatter = new LunarFormatter();
            }
            dayFormatter = this.mLunarFormatter;
        }
        DayFormatter dayFormatter2 = this.mDayFormatter;
        if (dayFormatter2 == null) {
            dayFormatter2 = dayFormatter;
        }
        return dayFormatter2.formatDay(i, i2, i3);
    }

    private void reorderLayout() {
        Resources resources = getResources();
        boolean z = false;
        boolean z2 = resources.getConfiguration().getLayoutDirection() == 1;
        boolean startsWith = resources.getString(R.string.fmt_time_12hour_minute).startsWith(SupportedConfigFactory.CLOSE_BY_VIDEO);
        if ((startsWith && z2) || (!startsWith && !z2)) {
            z = true;
        }
        if (z) {
            ViewGroup viewGroup = (ViewGroup) this.mHourPicker.getParent();
            viewGroup.removeView(this.mHourPicker);
            viewGroup.addView(this.mHourPicker, viewGroup.getChildCount());
        }
    }

    /* access modifiers changed from: private */
    public void updateDayPicker() {
        Calendar calendar = this.mMinDate;
        int i = Integer.MAX_VALUE;
        int computeDayCount = calendar == null ? Integer.MAX_VALUE : computeDayCount(this.mCalendar, calendar);
        Calendar calendar2 = this.mMaxDate;
        if (calendar2 != null) {
            i = computeDayCount(calendar2, this.mCalendar);
        }
        if (computeDayCount > 1 || i > 1) {
            checkDisplayeValid(this.mDayPicker, 0, 4);
            this.mDayPicker.setMinValue(0);
            this.mDayPicker.setMaxValue(4);
            if (computeDayCount <= 1) {
                this.mDayPicker.setValue(computeDayCount);
                this.mDayLastValue = computeDayCount;
                this.mDayPicker.setWrapSelectorWheel(false);
            }
            if (i <= 1) {
                this.mDayLastValue = 4 - i;
                this.mDayPicker.setValue(this.mDayLastValue);
                this.mDayPicker.setWrapSelectorWheel(false);
            }
            if (computeDayCount > 1 && i > 1) {
                this.mDayPicker.setWrapSelectorWheel(true);
            }
        } else {
            int computeDayCount2 = computeDayCount(this.mMaxDate, this.mMinDate);
            checkDisplayeValid(this.mDayPicker, 0, computeDayCount2);
            this.mDayPicker.setMinValue(0);
            this.mDayPicker.setMaxValue(computeDayCount2);
            this.mDayPicker.setValue(computeDayCount);
            this.mDayLastValue = computeDayCount;
            this.mDayPicker.setWrapSelectorWheel(false);
        }
        int maxValue = (this.mDayPicker.getMaxValue() - this.mDayPicker.getMinValue()) + 1;
        String[] strArr = this.mDayDisplayValues;
        if (strArr == null || strArr.length != maxValue) {
            this.mDayDisplayValues = new String[maxValue];
        }
        int value = this.mDayPicker.getValue();
        Calendar calendar3 = (Calendar) sCalCache.get();
        if (calendar3 == null) {
            calendar3 = new Calendar();
            sCalCache.set(calendar3);
        }
        calendar3.setTimeInMillis(this.mCalendar.getTimeInMillis());
        this.mDayDisplayValues[value] = formatDay(calendar3.get(1), calendar3.get(5), calendar3.get(9));
        for (int i2 = 1; i2 <= 2; i2++) {
            calendar3.add(12, 1);
            int i3 = (value + i2) % 5;
            String[] strArr2 = this.mDayDisplayValues;
            if (i3 >= strArr2.length) {
                break;
            }
            strArr2[i3] = formatDay(calendar3.get(1), calendar3.get(5), calendar3.get(9));
        }
        calendar3.setTimeInMillis(this.mCalendar.getTimeInMillis());
        for (int i4 = 1; i4 <= 2; i4++) {
            calendar3.add(12, -1);
            int i5 = ((value - i4) + 5) % 5;
            String[] strArr3 = this.mDayDisplayValues;
            if (i5 >= strArr3.length) {
                break;
            }
            strArr3[i5] = formatDay(calendar3.get(1), calendar3.get(5), calendar3.get(9));
        }
        this.mDayPicker.setDisplayedValues(this.mDayDisplayValues);
    }

    /* access modifiers changed from: private */
    public void updateHourPicker() {
        boolean z;
        Calendar calendar = this.mMaxDate;
        if (calendar == null || computeDayCount(this.mCalendar, calendar) != 0) {
            z = false;
        } else {
            this.mHourPicker.setMaxValue(this.mMaxDate.get(18));
            this.mHourPicker.setWrapSelectorWheel(false);
            z = true;
        }
        Calendar calendar2 = this.mMinDate;
        if (calendar2 != null && computeDayCount(this.mCalendar, calendar2) == 0) {
            this.mHourPicker.setMinValue(this.mMinDate.get(18));
            this.mHourPicker.setWrapSelectorWheel(false);
            z = true;
        }
        if (!z) {
            this.mHourPicker.setMinValue(0);
            this.mHourPicker.setMaxValue(23);
            this.mHourPicker.setWrapSelectorWheel(true);
        }
        this.mHourPicker.setValue(this.mCalendar.get(18));
    }

    /* access modifiers changed from: private */
    public void updateMinutePicker() {
        boolean z;
        Calendar calendar = this.mMaxDate;
        if (calendar != null && computeDayCount(this.mCalendar, calendar) == 0 && this.mCalendar.get(18) == this.mMaxDate.get(18)) {
            int i = this.mMaxDate.get(20);
            this.mMinutePicker.setMinValue(0);
            this.mMinutePicker.setMaxValue(i / this.mMinuteInterval);
            this.mMinutePicker.setWrapSelectorWheel(false);
            z = true;
        } else {
            z = false;
        }
        Calendar calendar2 = this.mMinDate;
        if (calendar2 != null && computeDayCount(this.mCalendar, calendar2) == 0 && this.mCalendar.get(18) == this.mMinDate.get(18)) {
            this.mMinutePicker.setMinValue(this.mMinDate.get(20) / this.mMinuteInterval);
            this.mMinutePicker.setWrapSelectorWheel(false);
            z = true;
        }
        if (!z) {
            checkDisplayeValid(this.mMinutePicker, 0, (60 / this.mMinuteInterval) - 1);
            this.mMinutePicker.setMinValue(0);
            this.mMinutePicker.setMaxValue((60 / this.mMinuteInterval) - 1);
            this.mMinutePicker.setWrapSelectorWheel(true);
        }
        int maxValue = (this.mMinutePicker.getMaxValue() - this.mMinutePicker.getMinValue()) + 1;
        String[] strArr = this.mMinuteDisplayValues;
        if (strArr == null || strArr.length != maxValue) {
            this.mMinuteDisplayValues = new String[maxValue];
            for (int i2 = 0; i2 < maxValue; i2++) {
                this.mMinuteDisplayValues[i2] = NumberPicker.TWO_DIGIT_FORMATTER.format((this.mMinutePicker.getMinValue() + i2) * this.mMinuteInterval);
            }
            this.mMinutePicker.setDisplayedValues(this.mMinuteDisplayValues);
        }
        this.mMinutePicker.setValue(this.mCalendar.get(20) / this.mMinuteInterval);
    }

    public boolean dispatchPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        onPopulateAccessibilityEvent(accessibilityEvent);
        return true;
    }

    /* access modifiers changed from: protected */
    public void dispatchRestoreInstanceState(SparseArray sparseArray) {
        dispatchThawSelfOnly(sparseArray);
    }

    public long getTimeInMillis() {
        return this.mCalendar.getTimeInMillis();
    }

    public void onInitializeAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onInitializeAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.setClassName(DateTimePicker.class.getName());
    }

    public void onInitializeAccessibilityNodeInfo(AccessibilityNodeInfo accessibilityNodeInfo) {
        super.onInitializeAccessibilityNodeInfo(accessibilityNodeInfo);
        accessibilityNodeInfo.setClassName(DateTimePicker.class.getName());
    }

    public void onPopulateAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        super.onPopulateAccessibilityEvent(accessibilityEvent);
        accessibilityEvent.getText().add(DateUtils.formatDateTime(this.mCalendar.getTimeInMillis(), 1420));
    }

    /* access modifiers changed from: protected */
    public void onRestoreInstanceState(Parcelable parcelable) {
        SavedState savedState = (SavedState) parcelable;
        super.onRestoreInstanceState(savedState.getSuperState());
        update(savedState.getTimeInMillis());
    }

    /* access modifiers changed from: protected */
    public Parcelable onSaveInstanceState() {
        return new SavedState(super.onSaveInstanceState(), getTimeInMillis());
    }

    public void setDayFormatter(DayFormatter dayFormatter) {
        this.mDayFormatter = dayFormatter;
        updateDayPicker();
    }

    public void setLunarMode(boolean z) {
        this.mIsLunarMode = z;
        updateDayPicker();
    }

    public void setMaxDateTime(long j) {
        if (j <= 0) {
            this.mMaxDate = null;
        } else {
            this.mMaxDate = new Calendar();
            this.mMaxDate.setTimeInMillis(j);
            adjustCalendar(this.mMaxDate, false);
            Calendar calendar = this.mMinDate;
            if (calendar != null && calendar.getTimeInMillis() > this.mMaxDate.getTimeInMillis()) {
                this.mMaxDate.setTimeInMillis(this.mMinDate.getTimeInMillis());
            }
        }
        checkCurrentTime();
        updateDayPicker();
        updateHourPicker();
        updateMinutePicker();
    }

    public void setMinDateTime(long j) {
        if (j <= 0) {
            this.mMinDate = null;
        } else {
            this.mMinDate = new Calendar();
            this.mMinDate.setTimeInMillis(j);
            if (!(this.mMinDate.get(21) == 0 && this.mMinDate.get(22) == 0)) {
                this.mMinDate.add(20, 1);
            }
            adjustCalendar(this.mMinDate, true);
            Calendar calendar = this.mMaxDate;
            if (calendar != null && calendar.getTimeInMillis() < this.mMinDate.getTimeInMillis()) {
                this.mMinDate.setTimeInMillis(this.mMaxDate.getTimeInMillis());
            }
        }
        checkCurrentTime();
        updateDayPicker();
        updateHourPicker();
        updateMinutePicker();
    }

    public void setMinuteInterval(int i) {
        if (this.mMinuteInterval != i) {
            this.mMinuteInterval = i;
            adjustCalendar(this.mCalendar, true);
            checkCurrentTime();
            updateMinutePicker();
        }
    }

    public void setOnTimeChangedListener(OnDateTimeChangedListener onDateTimeChangedListener) {
        this.mListener = onDateTimeChangedListener;
    }

    public void update(long j) {
        this.mCalendar.setTimeInMillis(j);
        adjustCalendar(this.mCalendar, true);
        checkCurrentTime();
        updateDayPicker();
        updateHourPicker();
        updateMinutePicker();
    }
}
