package miui.widget;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Handler;
import android.text.format.DateFormat;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.miui.internal.R;
import java.util.TimeZone;
import miui.date.Calendar;
import miui.date.CalendarFormatSymbols;
import miui.date.DateUtils;

public class PictureClock extends LinearLayout {
    private AmPm mAmPm;
    private boolean mAttached;
    /* access modifiers changed from: private */
    public Calendar mCalendar;
    /* access modifiers changed from: private */
    public final Handler mHandler;
    private final BroadcastReceiver mIntentReceiver;
    /* access modifiers changed from: private */
    public boolean mLive;
    private ImageTextView mTimeDisplay;

    class AmPm {
        private TextView mAmPm;
        private String mAmString;
        private String mPmString;

        AmPm(View view) {
            this.mAmPm = (TextView) view.findViewById(R.id.am_pm);
            String[] amPms = CalendarFormatSymbols.getDefault().getAmPms();
            this.mAmString = amPms[0];
            this.mPmString = amPms[1];
        }

        /* access modifiers changed from: 0000 */
        public void setIsMorning(boolean z) {
            TextView textView = this.mAmPm;
            if (textView != null) {
                textView.setText(z ? this.mAmString : this.mPmString);
            }
        }

        /* access modifiers changed from: 0000 */
        public void setShowAmPm(boolean z) {
            TextView textView = this.mAmPm;
            if (textView != null) {
                textView.setVisibility(z ? 0 : 8);
            }
        }
    }

    public PictureClock(Context context) {
        this(context, null);
    }

    public PictureClock(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        this.mHandler = new Handler();
        this.mIntentReceiver = new BroadcastReceiver() {
            public void onReceive(Context context, Intent intent) {
                if (PictureClock.this.mLive && intent.getAction().equals("android.intent.action.TIMEZONE_CHANGED")) {
                    PictureClock.this.mCalendar = new Calendar(TimeZone.getTimeZone(intent.getStringExtra("time-zone")));
                }
                PictureClock.this.mHandler.post(new Runnable() {
                    public void run() {
                        PictureClock.this.updateTime();
                    }
                });
            }
        };
        this.mLive = true;
    }

    /* access modifiers changed from: private */
    public void updateTime() {
        if (this.mLive) {
            this.mCalendar.setTimeInMillis(System.currentTimeMillis());
        }
        this.mTimeDisplay.setText(DateUtils.formatDateTime(this.mCalendar.getTimeInMillis(), 76));
        boolean z = true;
        this.mAmPm.setShowAmPm(!DateFormat.is24HourFormat(getContext()));
        AmPm amPm = this.mAmPm;
        if (this.mCalendar.get(17) != 0) {
            z = false;
        }
        amPm.setIsMorning(z);
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!this.mAttached) {
            this.mAttached = true;
            if (this.mLive) {
                IntentFilter intentFilter = new IntentFilter();
                intentFilter.addAction("android.intent.action.TIME_TICK");
                intentFilter.addAction("android.intent.action.TIME_SET");
                intentFilter.addAction("android.intent.action.TIMEZONE_CHANGED");
                intentFilter.addAction("android.intent.action.CONFIGURATION_CHANGED");
                getContext().registerReceiver(this.mIntentReceiver, intentFilter);
            }
            this.mCalendar = new Calendar();
            updateTime();
        }
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (this.mAttached) {
            this.mAttached = false;
            if (this.mLive) {
                getContext().unregisterReceiver(this.mIntentReceiver);
            }
        }
    }

    /* access modifiers changed from: protected */
    public void onFinishInflate() {
        super.onFinishInflate();
        this.mTimeDisplay = (ImageTextView) findViewById(R.id.time_display);
        this.mAmPm = new AmPm(this);
    }

    /* access modifiers changed from: 0000 */
    public void setLive(boolean z) {
        this.mLive = z;
    }
}
