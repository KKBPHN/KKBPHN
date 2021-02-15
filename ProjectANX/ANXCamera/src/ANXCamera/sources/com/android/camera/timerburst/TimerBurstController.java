package com.android.camera.timerburst;

import android.view.View;
import android.view.ViewGroup;
import com.android.camera.CameraAppImpl;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.log.Log;

public class TimerBurstController implements AddSeekBarValueListener {
    public static final int SHOT_COUNT_ENLARGE_TIMES = 10;
    private static final String TAG = "TimerBurstController";
    public static final int TIME_INTERVAL_ENLARGE_TIMES = 1;
    public static final int[] shotCount = {1, 60};
    public static final int[] timeInterval = {1, 60};
    private volatile boolean isDecreasedCount = false;
    private volatile boolean isInTimerBurstShotting;
    private int mJpegRotation = -1;
    private int mOrientation = -1;
    private String mPictureName;
    TimerBurstBean mTimerBurstBeanSetting = new TimerBurstBean();
    private ViewGroup parent;
    private volatile boolean pendingStopTimerBurst;

    public TimerBurstController() {
        int timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
        int timerBurstInterval = CameraSettings.getTimerBurstInterval();
        this.mTimerBurstBeanSetting.setTotalCount(timerBurstTotalCount);
        this.mTimerBurstBeanSetting.setIntervalTimer((long) timerBurstInterval);
    }

    public static boolean isSupportTimerBurst(int i) {
        return i == 163 || i == 167 || i == 165;
    }

    private void resetTimerConfig() {
        this.isDecreasedCount = false;
        this.isInTimerBurstShotting = false;
        this.mTimerBurstBeanSetting.setTotalCount(CameraSettings.getTimerBurstTotalCount());
        this.mPictureName = null;
        this.mJpegRotation = -1;
        this.mOrientation = -1;
    }

    public /* synthetic */ void O00ooo() {
        if (this.parent != null) {
            int timerBurstInterval = CameraSettings.getTimerBurstInterval();
            ViewGroup viewGroup = this.parent;
            viewGroup.setContentDescription(viewGroup.getContext().getResources().getQuantityString(R.plurals.accessibility_timer_burst_interval, timerBurstInterval, new Object[]{Integer.valueOf(timerBurstInterval)}));
            this.parent.sendAccessibilityEvent(128);
        }
    }

    public /* synthetic */ void O00oooO0() {
        if (this.parent != null) {
            int timerBurstTotalCount = CameraSettings.getTimerBurstTotalCount();
            ViewGroup viewGroup = this.parent;
            viewGroup.setContentDescription(viewGroup.getContext().getResources().getQuantityString(R.plurals.accessibility_timer_burst_count, timerBurstTotalCount, new Object[]{Integer.valueOf(timerBurstTotalCount)}));
            this.parent.sendAccessibilityEvent(128);
        }
    }

    public void currentSeekBarValue(View view, int i, int i2) {
        Runnable runnable;
        ViewGroup viewGroup;
        if (Util.isLayoutRTL(CameraAppImpl.getAndroidContext())) {
            i2 = 61 - i2;
        }
        this.parent = (ViewGroup) view.getParent();
        switch (view.getId()) {
            case R.id.csb_count /*2131296471*/:
                int[] iArr = shotCount;
                if (iArr[0] <= i2 && i2 <= iArr[iArr.length - 1]) {
                    int i3 = i2 * 10;
                    this.mTimerBurstBeanSetting.setTotalCount(i3);
                    CameraSettings.setTimerBurstTotalCount(i2);
                    String str = TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("setTotalCount: ");
                    sb.append(i3);
                    Log.u(str, sb.toString());
                }
                if (Util.isAccessible()) {
                    viewGroup = this.parent;
                    if (viewGroup != null) {
                        runnable = new O000000o(this);
                        break;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            case R.id.csb_interval /*2131296472*/:
                int[] iArr2 = timeInterval;
                if (iArr2[0] <= i2 && i2 <= iArr2[iArr2.length - 1]) {
                    this.mTimerBurstBeanSetting.setIntervalTimer((long) i2);
                    CameraSettings.setTimerBurstInterval(i2);
                    String str2 = TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("setIntervalTimer: ");
                    sb2.append(i2);
                    Log.u(str2, sb2.toString());
                }
                if (Util.isAccessible()) {
                    viewGroup = this.parent;
                    if (viewGroup != null) {
                        runnable = new O00000Oo(this);
                        break;
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            default:
                return;
        }
        viewGroup.postDelayed(runnable, 500);
    }

    public void decreaseCount(int i) {
        this.mTimerBurstBeanSetting.setTotalCount(i - 1);
        if (i > 1) {
            this.isDecreasedCount = true;
        }
    }

    public int getCaptureIndex() {
        return (CameraSettings.getTimerBurstTotalCount() - getTotalCount()) + 1;
    }

    public long getIntervalTimer() {
        return this.mTimerBurstBeanSetting.getIntervalTimer();
    }

    public boolean getIsDecreasedCount() {
        return this.isDecreasedCount;
    }

    public int getJpegRotation(int i) {
        if (this.mJpegRotation == -1) {
            this.mJpegRotation = i;
        }
        return this.mJpegRotation;
    }

    public int getOrientation(boolean z, int i) {
        if (!z) {
            return i;
        }
        if (this.mOrientation == -1) {
            this.mOrientation = i;
        }
        return this.mOrientation;
    }

    public String getPictureTitle(String str) {
        if (this.mPictureName == null) {
            this.mPictureName = str;
        }
        return this.mPictureName;
    }

    public TimerBurstBean getTimerBurstBean() {
        return this.mTimerBurstBeanSetting;
    }

    public int getTotalCount() {
        return this.mTimerBurstBeanSetting.getTotalCount();
    }

    public boolean isInTimerBurstShotting() {
        return this.isInTimerBurstShotting && this.mTimerBurstBeanSetting.getTotalCount() > 0;
    }

    public boolean isPendingStopTimerBurst() {
        return this.pendingStopTimerBurst;
    }

    public void muteTimerConfig() {
        CameraSettings.setTimerBurstEnable(false);
        resetTimerRunningData();
        CameraSettings.setTimerBurstTotalCount(3);
        CameraSettings.setTimerBurstInterval(5);
    }

    public void resetTimerRunningData() {
        this.mTimerBurstBeanSetting.setTotalCount(CameraSettings.getTimerBurstTotalCount());
    }

    public void setInTimerBurstShotting(boolean z) {
        this.isInTimerBurstShotting = z;
        this.pendingStopTimerBurst = false;
        if (!this.isInTimerBurstShotting) {
            resetTimerConfig();
        }
    }

    public void setPendingStopTimerBurst(boolean z) {
        this.pendingStopTimerBurst = z;
    }
}
