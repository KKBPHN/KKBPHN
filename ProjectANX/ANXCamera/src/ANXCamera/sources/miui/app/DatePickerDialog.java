package miui.app;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.CompoundButton;
import com.miui.internal.R;
import miui.date.Calendar;
import miui.date.DateUtils;
import miui.widget.DatePicker;
import miui.widget.DatePicker.OnDateChangedListener;
import miui.widget.SlidingButton;

public class DatePickerDialog extends AlertDialog {
    private static final String DAY = "miui:day";
    private static final String MONTH = "miui:month";
    private static final String YEAR = "miui:year";
    private final Calendar mCalendar;
    private final OnDateSetListener mCallBack;
    private final DatePicker mDatePicker;
    private View mLunarModePanel;
    private SlidingButton mLunarModeState;
    private OnDateChangedListener mOnDateChangedListener;
    /* access modifiers changed from: private */
    public boolean mTitleNeedsUpdate;

    public interface OnDateSetListener {
        void onDateSet(DatePicker datePicker, int i, int i2, int i3);
    }

    public DatePickerDialog(Context context, int i, OnDateSetListener onDateSetListener, int i2, int i3, int i4) {
        super(context, i);
        this.mTitleNeedsUpdate = true;
        this.mOnDateChangedListener = new OnDateChangedListener() {
            public void onDateChanged(DatePicker datePicker, int i, int i2, int i3, boolean z) {
                if (DatePickerDialog.this.mTitleNeedsUpdate) {
                    DatePickerDialog.this.updateTitle(i, i2, i3);
                }
            }
        };
        this.mCallBack = onDateSetListener;
        this.mCalendar = new Calendar();
        Context context2 = getContext();
        setButton(-1, context2.getText(17039370), new OnClickListener() {
            public void onClick(DialogInterface dialogInterface, int i) {
                DatePickerDialog.this.tryNotifyDateSet();
            }
        });
        setButton(-2, getContext().getText(17039360), null);
        setIcon(0);
        View inflate = ((LayoutInflater) context2.getSystemService("layout_inflater")).inflate(R.layout.date_picker_dialog, null);
        setView(inflate);
        this.mDatePicker = (DatePicker) inflate.findViewById(R.id.datePicker);
        this.mDatePicker.init(i2, i3, i4, this.mOnDateChangedListener);
        updateTitle(i2, i3, i4);
        this.mLunarModePanel = inflate.findViewById(R.id.lunarModePanel);
        this.mLunarModeState = (SlidingButton) inflate.findViewById(R.id.datePickerLunar);
        this.mLunarModeState.setOnPerformCheckedChangeListener(new O000000o(this));
    }

    public DatePickerDialog(Context context, OnDateSetListener onDateSetListener, int i, int i2, int i3) {
        this(context, 0, onDateSetListener, i, i2, i3);
    }

    /* access modifiers changed from: private */
    public void tryNotifyDateSet() {
        if (this.mCallBack != null) {
            this.mDatePicker.clearFocus();
            OnDateSetListener onDateSetListener = this.mCallBack;
            DatePicker datePicker = this.mDatePicker;
            onDateSetListener.onDateSet(datePicker, datePicker.getYear(), this.mDatePicker.getMonth(), this.mDatePicker.getDayOfMonth());
        }
    }

    /* access modifiers changed from: private */
    public void updateTitle(int i, int i2, int i3) {
        this.mCalendar.set(1, i);
        this.mCalendar.set(5, i2);
        this.mCalendar.set(9, i3);
        super.setTitle(DateUtils.formatDateTime(this.mCalendar.getTimeInMillis(), 14208));
    }

    public /* synthetic */ void O000000o(CompoundButton compoundButton, boolean z) {
        this.mDatePicker.setLunarMode(z);
    }

    public DatePicker getDatePicker() {
        return this.mDatePicker;
    }

    public void onRestoreInstanceState(Bundle bundle) {
        super.onRestoreInstanceState(bundle);
        this.mDatePicker.init(bundle.getInt(YEAR), bundle.getInt(MONTH), bundle.getInt(DAY), this.mOnDateChangedListener);
    }

    public Bundle onSaveInstanceState() {
        Bundle onSaveInstanceState = super.onSaveInstanceState();
        onSaveInstanceState.putInt(YEAR, this.mDatePicker.getYear());
        onSaveInstanceState.putInt(MONTH, this.mDatePicker.getMonth());
        onSaveInstanceState.putInt(DAY, this.mDatePicker.getDayOfMonth());
        return onSaveInstanceState;
    }

    public void setLunarMode(boolean z) {
        this.mLunarModePanel.setVisibility(z ? 0 : 8);
    }

    public void setTitle(int i) {
        super.setTitle(i);
        this.mTitleNeedsUpdate = false;
    }

    public void setTitle(CharSequence charSequence) {
        super.setTitle(charSequence);
        this.mTitleNeedsUpdate = false;
    }

    public void switchLunarState(boolean z) {
        this.mLunarModeState.setChecked(z);
    }

    public void updateDate(int i, int i2, int i3) {
        this.mDatePicker.updateDate(i, i2, i3);
    }
}
