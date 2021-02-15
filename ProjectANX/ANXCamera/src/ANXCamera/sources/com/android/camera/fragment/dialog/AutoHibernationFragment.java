package com.android.camera.fragment.dialog;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnKeyListener;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.TextUtils;
import android.text.format.DateFormat;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.Window;
import android.view.animation.LinearInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.config.ComponentManuallyET;
import com.android.camera.lib.compatibility.util.CompatibilityUtils;
import com.android.camera.log.Log;
import com.android.camera.module.Camera2Module;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.AmbilightProtocol;
import com.android.camera.protocol.ModeProtocol.AutoHibernation;
import com.android.camera.protocol.ModeProtocol.BackStack;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.ui.AutoHibernationBatteryView;
import com.android.camera.ui.AutoHibernationView;
import com.ss.android.vesdk.VEResult;
import com.xiaomi.stat.MiStat.Param;
import java.text.DateFormatSymbols;
import java.util.Calendar;
import miui.text.ExtraTextUtils;
import miuix.animation.Folme;
import miuix.animation.base.AnimConfig;
import miuix.animation.controller.AnimState;
import miuix.animation.listener.TransitionListener;
import miuix.animation.property.ViewProperty;

public class AutoHibernationFragment extends DialogFragment implements OnClickListener, OnKeyListener, HandleBackTrace, AutoHibernation {
    public static final String FONT_MICLOCK_MONO = "miclock-mono";
    public static final String FONT_MITYPE_MONO = "mitype-mono";
    public static final String FONT_MITYPE_MONO_LIGHT = "mitype-mono-light";
    private static final String M12 = "hh:mm";
    private static final String M24 = "kk:mm";
    public static final String TAG = "AutoHibernation";
    private String mAmString;
    /* access modifiers changed from: private */
    public AutoHibernationBatteryView mAutoHibernationBatteryView;
    /* access modifiers changed from: private */
    public ImageView mAutoHibernationRedIndicator;
    private AutoHibernationView mAutoHibernationView;
    private BatteryReceiver mBatteryReceiver;
    private Calendar mCalendar;
    /* access modifiers changed from: private */
    public FrameLayout mContentFrameLayout;
    /* access modifiers changed from: private */
    public int mCurrentDegree = 0;
    private TextView mFastmotionFirstTime;
    private TextView mFastmotionSecondTime;
    private TextView mFastmotionSystemTime;
    private String mFirstTimeStr;
    private ValueAnimator mIndicatorAnimator;
    public int mModeIndex = 169;
    private int mOrientation = 0;
    private LinearLayout mOthersTimeContent;
    private ImageView mOthersTimeDivider;
    private TextView mOthersTimeFirst;
    private TextView mOthersTimeSecond;
    private String mPmString;
    private ValueAnimator mRotationAnimator;
    private String mSecondTimeStr;
    private int mStartDegree = 0;
    private int mTargetDegree = 0;
    private String mTimeFormat = M12;
    private String[] mWeekdayDes;
    private String[] mWeekdayShortDes;
    private boolean mhasDismissed = false;

    class BatteryReceiver extends BroadcastReceiver {
        private BatteryReceiver() {
        }

        public void onReceive(Context context, Intent intent) {
            AutoHibernationFragment.this.mAutoHibernationBatteryView.showBattery(Util.clamp((int) ((((float) intent.getIntExtra(Param.LEVEL, 0)) / ((float) intent.getIntExtra("scale", 100))) * 100.0f), 0, 100));
        }
    }

    private void adjustViewSize(View view) {
        int windowWidth = Display.getWindowWidth();
        int windowHeight = Display.getWindowHeight();
        LayoutParams layoutParams = view.getLayoutParams();
        if (layoutParams.width != windowWidth || layoutParams.height != windowHeight) {
            layoutParams.width = windowWidth;
            layoutParams.height = windowHeight;
        }
    }

    private void initRedIndicatorAnimator() {
        this.mIndicatorAnimator = ValueAnimator.ofFloat(new float[]{0.0f, 1.0f});
        this.mIndicatorAnimator.setDuration(1000);
        this.mIndicatorAnimator.addUpdateListener(new AnimatorUpdateListener() {
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                AutoHibernationFragment.this.mAutoHibernationRedIndicator.setAlpha(((Float) valueAnimator.getAnimatedValue()).floatValue());
            }
        });
        this.mIndicatorAnimator.setRepeatMode(1);
        this.mIndicatorAnimator.setRepeatCount(-1);
    }

    private void resumeMode() {
        ((BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171)).removeBackStack(this);
        Camera camera = (Camera) getActivity();
        if (!camera.isActivityPaused()) {
            camera.exitAutoHibernationMode();
        }
    }

    private void setTranslationY(final View view, float f, float f2) {
        AnimState add = new AnimState("translateFrom").add(ViewProperty.TRANSLATION_Y, 100, new long[0]);
        AnimState add2 = new AnimState("translateTo").add(ViewProperty.TRANSLATION_Y, 0, new long[0]);
        Folme.useAt(view).state().fromTo(add, add2, new AnimConfig().setEase(-2, f, f2).addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                super.onComplete(obj);
                FolmeUtils.clean(view);
            }
        }));
    }

    private void startAnimator() {
        AnimState add = new AnimState("scalefrom").add(ViewProperty.SCALE_X, 1.2f, new long[0]).add(ViewProperty.SCALE_Y, 1.2f, new long[0]);
        AnimState add2 = new AnimState("scaleto").add(ViewProperty.SCALE_X, 1.0f, new long[0]).add(ViewProperty.SCALE_Y, 1.0f, new long[0]);
        AnimState add3 = new AnimState("alphafrom").add(ViewProperty.AUTO_ALPHA, 0.0f, new long[0]);
        AnimState add4 = new AnimState("alphato").add(ViewProperty.AUTO_ALPHA, 1.0f, new long[0]);
        Folme.useAt(this.mContentFrameLayout).state().fromTo(add3, add4, new AnimConfig().setEase(16, new float[0])).fromTo(add, add2, new AnimConfig().setEase(-2, 1.3f, 0.3f).addListeners(new TransitionListener() {
            public void onComplete(Object obj) {
                super.onComplete(obj);
                FolmeUtils.clean(AutoHibernationFragment.this.mContentFrameLayout);
            }
        }));
        if (this.mModeIndex == 169) {
            setTranslationY(this.mFastmotionFirstTime, 0.9f, 0.3f);
            setTranslationY(this.mFastmotionSecondTime, 1.1f, 0.3f);
            setTranslationY(this.mFastmotionSystemTime, 1.3f, 0.3f);
        } else {
            setTranslationY(this.mOthersTimeContent, 0.9f, 0.3f);
            setTranslationY(this.mFastmotionSystemTime, 1.1f, 0.3f);
        }
        setTranslationY(this.mAutoHibernationBatteryView, 1.3f, 0.3f);
    }

    private void updateFirstVideoRecordingTime() {
        String str;
        TextView textView;
        int i = this.mModeIndex;
        if (i == 169) {
            if (!TextUtils.isEmpty(this.mFirstTimeStr)) {
                this.mFastmotionFirstTime.setText(this.mFirstTimeStr);
            }
            if (!TextUtils.isEmpty(this.mSecondTimeStr)) {
                textView = this.mFastmotionSecondTime;
                str = this.mSecondTimeStr;
            } else {
                return;
            }
        } else if ((i == 180 || i == 162 || i == 187) && !TextUtils.isEmpty(this.mFirstTimeStr)) {
            textView = this.mOthersTimeFirst;
            str = this.mFirstTimeStr;
        } else {
            return;
        }
        textView.setText(str);
    }

    private void updateSystemTime() {
        String str;
        this.mCalendar.setTimeInMillis(System.currentTimeMillis());
        try {
            int weekDay = Util.getWeekDay(this.mCalendar);
            str = Util.isLocaleChinese() ? this.mWeekdayShortDes[weekDay] : this.mWeekdayDes[weekDay];
        } catch (Exception unused) {
            str = "";
        }
        if (Util.isLocaleChinese()) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(this.mCalendar.get(9) == 0 ? this.mAmString : this.mPmString);
            str = sb.toString();
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append(str);
        sb2.append(DateFormat.format(this.mTimeFormat, this.mCalendar));
        this.mFastmotionSystemTime.setText(sb2.toString());
    }

    public final boolean canProvide() {
        return isAdded();
    }

    public void dismissAutoHibernation() {
        Log.d(TAG, "dismissAutoHibernation");
        dismissAllowingStateLoss();
    }

    public void initOrientation(int i) {
        this.mOrientation = i;
        this.mCurrentDegree = 360 - i;
    }

    public void onActivityCreated(Bundle bundle) {
        Window window = getDialog().getWindow();
        if (C0122O00000o.instance().OO00ooO()) {
            window.requestFeature(1);
        }
        super.onActivityCreated(bundle);
        window.setLayout(-1, -1);
        if (Display.isContentViewExtendToTopEdges()) {
            CompatibilityUtils.setCutoutModeShortEdges(window);
        }
        window.getDecorView().setSystemUiVisibility(4102);
    }

    public boolean onBackEvent(int i) {
        if (i == 5 && !this.mhasDismissed) {
            this.mhasDismissed = true;
            resumeMode();
            dismissAllowingStateLoss();
        }
        return false;
    }

    public void onCancel(DialogInterface dialogInterface) {
        super.onCancel(dialogInterface);
        resumeMode();
    }

    public void onClick(View view) {
        if (view.getId() == R.id.auto_hibernation_cover) {
            onBackEvent(5);
        }
    }

    public Dialog onCreateDialog(Bundle bundle) {
        Dialog onCreateDialog = super.onCreateDialog(bundle);
        onCreateDialog.getWindow().setGravity(48);
        return onCreateDialog;
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        ImageView imageView;
        int i = 0;
        View inflate = layoutInflater.inflate(R.layout.fragment_dialog_auto_hibernation, viewGroup, false);
        inflate.findViewById(R.id.auto_hibernation_cover).setOnClickListener(this);
        this.mContentFrameLayout = (FrameLayout) inflate.findViewById(R.id.auto_hibernation_content);
        this.mAutoHibernationView = (AutoHibernationView) inflate.findViewById(R.id.auto_hibernation_recording_view);
        this.mAutoHibernationRedIndicator = (ImageView) inflate.findViewById(R.id.auto_hibernation_red_indicator);
        this.mFastmotionFirstTime = (TextView) inflate.findViewById(R.id.auto_hibernation_fastmotion_first_time_text);
        this.mFastmotionSecondTime = (TextView) inflate.findViewById(R.id.auto_hibernation_fastmotion_second_time_text);
        this.mOthersTimeContent = (LinearLayout) inflate.findViewById(R.id.auto_hibernation_others_time_content);
        this.mOthersTimeFirst = (TextView) inflate.findViewById(R.id.auto_hibernation_others_time_first_text);
        this.mOthersTimeSecond = (TextView) inflate.findViewById(R.id.auto_hibernation_others_time_second_text);
        Typeface create = Typeface.create(C0122O00000o.instance().OO0O0OO() ? FONT_MITYPE_MONO_LIGHT : FONT_MICLOCK_MONO, 0);
        this.mOthersTimeFirst.setTypeface(create);
        this.mOthersTimeSecond.setTypeface(create);
        this.mFastmotionFirstTime.setTypeface(create);
        this.mFastmotionSecondTime.setTypeface(create);
        this.mOthersTimeDivider = (ImageView) inflate.findViewById(R.id.auto_hibernation_others_time_image);
        this.mFastmotionSystemTime = (TextView) inflate.findViewById(R.id.auto_hibernation_system_time_text);
        this.mAutoHibernationBatteryView = (AutoHibernationBatteryView) inflate.findViewById(R.id.auto_hibernation_battery_view);
        adjustViewSize(inflate.findViewById(R.id.auto_hibernation_layout));
        DateFormatSymbols dateFormatSymbols = new DateFormatSymbols();
        this.mWeekdayDes = dateFormatSymbols.getWeekdays();
        this.mWeekdayShortDes = dateFormatSymbols.getShortWeekdays();
        String[] amPmStrings = dateFormatSymbols.getAmPmStrings();
        this.mAmString = amPmStrings[0];
        this.mPmString = amPmStrings[1];
        this.mCalendar = Calendar.getInstance();
        if (getContext() != null) {
            this.mTimeFormat = DateFormat.is24HourFormat(getContext()) ? M24 : M12;
            LayoutParams layoutParams = this.mContentFrameLayout.getLayoutParams();
            LayoutParams layoutParams2 = this.mAutoHibernationView.getLayoutParams();
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mFastmotionSystemTime.getLayoutParams();
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mAutoHibernationBatteryView.getLayoutParams();
            if (this.mModeIndex == 169) {
                layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_fastmotion_width);
                layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_fastmotion_width);
                layoutParams2.width = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_fastmotion_width);
                layoutParams2.height = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_fastmotion_width);
                this.mFastmotionFirstTime.setVisibility(0);
                this.mFastmotionSecondTime.setVisibility(0);
                this.mOthersTimeContent.setVisibility(4);
                marginLayoutParams.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_fastmotion_system_time_text_margin_top);
                marginLayoutParams2.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_fastmotion_battery_view_margin_top);
                this.mFastmotionSystemTime.setTextSize(0, getContext().getResources().getDimension(R.dimen.auto_hibernation_fastmotion_system_time_text_size));
            } else {
                layoutParams.width = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_others_width);
                layoutParams.height = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_others_width);
                layoutParams2.width = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_others_width);
                layoutParams2.height = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_content_others_width);
                this.mFastmotionFirstTime.setVisibility(4);
                this.mFastmotionSecondTime.setVisibility(4);
                this.mOthersTimeContent.setVisibility(0);
                marginLayoutParams.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_others_system_time_text_margin_top);
                marginLayoutParams2.topMargin = getContext().getResources().getDimensionPixelSize(R.dimen.auto_hibernation_others_battery_view_margin_top);
                this.mFastmotionSystemTime.setTextSize(0, getContext().getResources().getDimension(R.dimen.auto_hibernation_others_system_time_text_size));
                int i2 = this.mModeIndex;
                if (i2 == 180 || i2 == 162 || i2 == 187) {
                    this.mOthersTimeFirst.setVisibility(0);
                    imageView = this.mOthersTimeDivider;
                    i = 8;
                } else if (i2 == 163 || i2 == 165 || i2 == 167) {
                    this.mOthersTimeFirst.setVisibility(0);
                    imageView = this.mOthersTimeDivider;
                }
                imageView.setVisibility(i);
                this.mOthersTimeSecond.setVisibility(i);
            }
        }
        this.mContentFrameLayout.setRotation((float) (360 - this.mOrientation));
        initRedIndicatorAnimator();
        return inflate;
    }

    public void onDestroyView() {
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.removeBackStack(this);
        }
        unRegisterProtocol();
        super.onDestroyView();
        getContext().unregisterReceiver(this.mBatteryReceiver);
    }

    public boolean onKey(DialogInterface dialogInterface, int i, KeyEvent keyEvent) {
        if (i != 25 && i != 24) {
            return false;
        }
        if (keyEvent.getAction() == 1) {
            onBackEvent(5);
        }
        return true;
    }

    public void onPause() {
        super.onPause();
        ValueAnimator valueAnimator = this.mIndicatorAnimator;
        if (valueAnimator != null) {
            valueAnimator.removeAllUpdateListeners();
            this.mIndicatorAnimator.cancel();
            this.mIndicatorAnimator = null;
        }
    }

    public void onResume() {
        int i;
        super.onResume();
        if (getDialog() != null) {
            getDialog().setOnKeyListener(this);
        }
        AutoHibernationView autoHibernationView = this.mAutoHibernationView;
        String str = TAG;
        if (autoHibernationView != null) {
            int i2 = this.mModeIndex;
            if (i2 == 169 || i2 == 187) {
                float cameraSnapPaintSecondTimeAngleRunning = DataRepository.dataItemRunning().getCameraSnapPaintSecondTimeAngleRunning();
                StringBuilder sb = new StringBuilder();
                sb.append("timeAngle ");
                sb.append(cameraSnapPaintSecondTimeAngleRunning);
                Log.i(str, sb.toString());
                if (this.mModeIndex == 169) {
                    i = 10000;
                } else {
                    AmbilightProtocol ambilightProtocol = (AmbilightProtocol) ModeCoordinatorImpl.getInstance().getAttachProtocol(420);
                    i = ambilightProtocol != null ? ambilightProtocol.getDuration() : 0;
                }
                if (cameraSnapPaintSecondTimeAngleRunning > (((float) (i + VEResult.TER_UNSUPPORTED)) / ((float) i)) * 360.0f) {
                    this.mAutoHibernationView.startRecord((long) i, 0.0f, false);
                } else {
                    this.mAutoHibernationView.startRecordForFastmotion(i, cameraSnapPaintSecondTimeAngleRunning, DataRepository.dataItemRunning().getCameraSnapPaintSecondClockWiseRunning());
                }
            }
        }
        int i3 = this.mModeIndex;
        if (i3 == 163 || i3 == 165 || i3 == 167) {
            String valueOf = String.valueOf(DataRepository.dataItemLive().getTimerBurstController().getCaptureIndex());
            StringBuilder sb2 = new StringBuilder();
            sb2.append(CameraSettings.getTimerBurstTotalCount());
            sb2.append("");
            updateRecordingTimeOrCaptureCount(valueOf, sb2.toString());
        }
        if (this.mModeIndex == 167) {
            long currentTimeMillis = System.currentTimeMillis() - ((Camera2Module) ((Camera) getActivity()).getCurrentModule()).getCaptureStartTime();
            ComponentManuallyET componentManuallyET = DataRepository.dataItemConfig().getmComponentManuallyET();
            if (componentManuallyET.isLongExpose(167)) {
                long parseLong = Long.parseLong(componentManuallyET.getComponentValue(167)) / ExtraTextUtils.MB;
                if (currentTimeMillis < parseLong) {
                    long j = parseLong - currentTimeMillis;
                    if (j > 200) {
                        this.mAutoHibernationView.startRecord(j, ((float) currentTimeMillis) / ((float) parseLong), true);
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("etValue ");
                        sb3.append(parseLong);
                        sb3.append(" diff ");
                        sb3.append(currentTimeMillis);
                        Log.i(str, sb3.toString());
                    }
                }
            }
        }
        updateSystemTime();
        updateFirstVideoRecordingTime();
        startAnimator();
    }

    public void onViewCreated(View view, @Nullable Bundle bundle) {
        super.onViewCreated(view, bundle);
        BackStack backStack = (BackStack) ModeCoordinatorImpl.getInstance().getAttachProtocol(171);
        if (backStack != null) {
            backStack.addInBackStack(this);
        }
        this.mBatteryReceiver = new BatteryReceiver();
        getContext().registerReceiver(this.mBatteryReceiver, new IntentFilter("android.intent.action.BATTERY_CHANGED"));
    }

    public void registerProtocol() {
        ModeCoordinatorImpl.getInstance().attachProtocol(936, this);
    }

    public void setModeIndex(int i) {
        this.mModeIndex = i;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0061, code lost:
        if (r1 == 0) goto L_0x006e;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void setOrientation(int i) {
        int i2;
        if (isResumed()) {
            int i3 = m.cQ;
            int i4 = i >= 0 ? i % m.cQ : (i % m.cQ) + m.cQ;
            int i5 = this.mOrientation;
            if (i5 != i4) {
                boolean z = i5 != -1;
                int i6 = i4 - this.mOrientation;
                if (i6 < 0) {
                    i6 += m.cQ;
                }
                if (i6 > 180) {
                    i6 += VEResult.TER_EGL_BAD_MATCH;
                }
                boolean z2 = i6 <= 0;
                this.mOrientation = i4;
                if (this.mOrientation != 0 || this.mCurrentDegree != 0) {
                    this.mTargetDegree = (360 - i4) % m.cQ;
                    if (!z) {
                        int i7 = this.mTargetDegree;
                        this.mCurrentDegree = i7;
                        this.mContentFrameLayout.setRotation((float) i7);
                        return;
                    }
                    ValueAnimator valueAnimator = this.mRotationAnimator;
                    if (valueAnimator != null) {
                        valueAnimator.cancel();
                    }
                    this.mStartDegree = this.mCurrentDegree;
                    int i8 = this.mStartDegree;
                    if (z2) {
                        if (i8 == 360) {
                            i8 = 0;
                        }
                        i2 = this.mTargetDegree;
                    } else {
                        if (i8 == 0) {
                            i8 = 360;
                        }
                        i2 = this.mTargetDegree;
                        if (i2 == 360) {
                            i3 = 0;
                            this.mRotationAnimator = ValueAnimator.ofInt(new int[]{i8, i3});
                            this.mRotationAnimator.setInterpolator(new LinearInterpolator());
                            this.mRotationAnimator.removeAllUpdateListeners();
                            this.mRotationAnimator.addUpdateListener(new AnimatorUpdateListener() {
                                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                                    AutoHibernationFragment.this.mCurrentDegree = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                                    AutoHibernationFragment.this.mContentFrameLayout.setRotation((float) AutoHibernationFragment.this.mCurrentDegree);
                                }
                            });
                            this.mRotationAnimator.start();
                        }
                    }
                    i3 = i2;
                    this.mRotationAnimator = ValueAnimator.ofInt(new int[]{i8, i3});
                    this.mRotationAnimator.setInterpolator(new LinearInterpolator());
                    this.mRotationAnimator.removeAllUpdateListeners();
                    this.mRotationAnimator.addUpdateListener(new AnimatorUpdateListener() {
                        public void onAnimationUpdate(ValueAnimator valueAnimator) {
                            AutoHibernationFragment.this.mCurrentDegree = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                            AutoHibernationFragment.this.mContentFrameLayout.setRotation((float) AutoHibernationFragment.this.mCurrentDegree);
                        }
                    });
                    this.mRotationAnimator.start();
                }
            }
        }
    }

    public void startAutoHibernationCaptureDelayNumber() {
        if (isResumed()) {
            startRedIndicatorAnimator();
        }
    }

    public void startRedIndicatorAnimator() {
        ValueAnimator valueAnimator = this.mIndicatorAnimator;
        if (valueAnimator != null && !valueAnimator.isRunning()) {
            this.mIndicatorAnimator.start();
        }
        AutoHibernationView autoHibernationView = this.mAutoHibernationView;
        if (autoHibernationView != null) {
            autoHibernationView.reset();
        }
    }

    public void stopAutoHibernationCaptureDelayNumber() {
        if (isResumed()) {
            stopRedIndicatorAnimator();
        }
    }

    public void stopRedIndicatorAnimator() {
        ValueAnimator valueAnimator = this.mIndicatorAnimator;
        if (valueAnimator != null && valueAnimator.isRunning()) {
            this.mIndicatorAnimator.cancel();
        }
    }

    public void unRegisterProtocol() {
        ModeCoordinatorImpl.getInstance().detachProtocol(936, this);
    }

    public void updataTimerBurstAnimator() {
        this.mAutoHibernationView.startRecord(Long.parseLong(DataRepository.dataItemConfig().getmComponentManuallyET().getComponentValue(167)) / ExtraTextUtils.MB, 0.0f, true);
    }

    public void updateAutoHibernationFirstRecordingTime(String str, String str2) {
        this.mFirstTimeStr = str;
        this.mSecondTimeStr = str2;
    }

    public void updateAutoHibernationRecordingTimeOrCaptureCount(String str, String str2) {
        if (isResumed()) {
            updateRecordingTimeOrCaptureCount(str, str2);
        }
    }

    public void updateRecordingTimeOrCaptureCount(String str, String str2) {
        TextView textView;
        int i = this.mModeIndex;
        if (i == 169) {
            this.mFastmotionFirstTime.setText(str);
            textView = this.mFastmotionSecondTime;
        } else if (i == 180 || i == 162 || i == 187) {
            this.mOthersTimeFirst.setText(str);
            updateSystemTime();
        } else {
            if (i == 165 || i == 163 || i == 167) {
                this.mOthersTimeFirst.setText(str);
                textView = this.mOthersTimeSecond;
            }
            updateSystemTime();
        }
        textView.setText(str2);
        updateSystemTime();
    }

    public void updateTimerBurstAnimator() {
        if (isResumed()) {
            updataTimerBurstAnimator();
        }
    }
}
