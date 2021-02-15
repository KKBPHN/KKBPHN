package com.android.camera.fragment.subtitle;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Rect;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.Animation;
import android.widget.TextView;
import androidx.annotation.RequiresApi;
import com.android.camera.Camera;
import com.android.camera.CameraSettings;
import com.android.camera.R;
import com.android.camera.ToastUtils;
import com.android.camera.Util;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.data.DataRepository;
import com.android.camera.data.data.global.DataItemGlobal;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.CtaNoticeFragment;
import com.android.camera.fragment.CtaNoticeFragment.OnCtaNoticeClickListener;
import com.android.camera.fragment.subtitle.recog.VoiceOnlineRecog;
import com.android.camera.log.Log;
import com.android.camera.module.loader.StartControl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.SubtitleRecording;
import com.android.camera.protocol.ModeProtocol.SubtitleRecording.Listener;
import com.android.camera.protocol.ModeProtocol.TopAlert;
import com.android.camera.statistic.CameraStatUtils;
import java.util.List;

public class FragmentSubtitle extends BaseFragment implements HandleBackTrace, SubtitleRecording, OnCtaNoticeClickListener {
    private static final int SUBTITLE_CHECK_NETWORK = 102;
    private static final int SUBTITLE_CINEMATICASPECTRATIO_OFFSET = Util.dpToPixel(26.2f);
    private static final int SUBTITLE_NO_SPEAK_TIP = 101;
    private static final int SUBTITLE_SHOW_TIME_END = 100;
    private static final int SUBTITLE_VERTICAL_OFFSET = Util.dpToPixel(188.36f);
    private static final int SUBTITLE_VIEW_WIDTH = Util.dpToPixel(229.09f);
    private static final String TAG = "FragmentSubtitle";
    /* access modifiers changed from: private */
    public boolean isNetWorkAvailable;
    private boolean isRecording;
    private CtaNoticeFragment mCtaNoticeFragment;
    /* access modifiers changed from: private */
    public TextView mLeftShow;
    /* access modifiers changed from: private */
    public RecognitionListener mRecognitionListener;
    /* access modifiers changed from: private */
    public TextView mRightShow;
    private SoundWaveView mSwViewLeft;
    private SoundWaveView mSwViewRight;
    private SoundWaveView mSwViewVertical;
    /* access modifiers changed from: private */
    public TextView mVerticalShow;
    private VoiceOnlineRecog mVoiceOnlineRecog;
    /* access modifiers changed from: private */
    @SuppressLint({"HandlerLeak"})
    public Handler subtitleHandler = new Handler() {
        public void handleMessage(Message message) {
            super.handleMessage(message);
            switch (message.what) {
                case 100:
                    if (FragmentSubtitle.this.mLeftShow != null && FragmentSubtitle.this.mRightShow != null && FragmentSubtitle.this.mVerticalShow != null) {
                        String str = "";
                        FragmentSubtitle.this.mLeftShow.setText(str);
                        FragmentSubtitle.this.mRightShow.setText(str);
                        FragmentSubtitle.this.mVerticalShow.setText(str);
                        return;
                    }
                    return;
                case 101:
                    if (FragmentSubtitle.this.isAdded()) {
                        FragmentSubtitle.this.mRecognitionListener.onRecognitionListener(FragmentSubtitle.this.getResources().getString(R.string.hint_subtitle_prompt));
                        return;
                    }
                    return;
                case 102:
                    FragmentSubtitle.this.checkNetWorkStatus();
                    return;
                default:
                    return;
            }
        }
    };
    private View subtitleView;
    private int top;
    private TopAlert topAlert;
    private int verticalViewHeight;

    public class RecognitionListener {
        public RecognitionListener() {
        }

        public void onFailure() {
            FragmentSubtitle.this.checkNetWorkStatus();
            if (FragmentSubtitle.this.isNetWorkAvailable) {
                FragmentSubtitle.this.subtitleHandler.sendEmptyMessageDelayed(102, 500);
            } else {
                CameraStatUtils.trackInterruptionNetwork();
            }
        }

        @RequiresApi(api = 21)
        public void onRecognitionListener(String str) {
            Handler handler;
            long j;
            FragmentSubtitle.this.subtitleHandler.removeCallbacksAndMessages(null);
            FragmentSubtitle.this.mLeftShow.setText(str);
            FragmentSubtitle.this.mRightShow.setText(str);
            FragmentSubtitle.this.mVerticalShow.setText(str);
            if (!FragmentSubtitle.this.isAdded() || !FragmentSubtitle.this.getResources().getString(R.string.hint_subtitle_prompt).equals(str)) {
                FragmentSubtitle.this.mLeftShow.setLetterSpacing(0.05f);
                FragmentSubtitle.this.mRightShow.setLetterSpacing(0.05f);
                FragmentSubtitle.this.mVerticalShow.setLetterSpacing(0.05f);
                handler = FragmentSubtitle.this.subtitleHandler;
                j = 2000;
            } else {
                FragmentSubtitle.this.mLeftShow.setLetterSpacing(0.0f);
                FragmentSubtitle.this.mRightShow.setLetterSpacing(0.0f);
                FragmentSubtitle.this.mVerticalShow.setLetterSpacing(0.0f);
                handler = FragmentSubtitle.this.subtitleHandler;
                j = 5000;
            }
            handler.sendEmptyMessageDelayed(100, j);
        }
    }

    private void setAnyViewGone() {
        this.mRightShow.setVisibility(8);
        this.mLeftShow.setVisibility(8);
        this.mVerticalShow.setVisibility(8);
        this.mSwViewLeft.setVisibility(8);
        this.mSwViewVertical.setVisibility(8);
        this.mSwViewRight.setVisibility(8);
        this.mSwViewLeft.resetAnimation();
        this.mSwViewRight.resetAnimation();
        this.mSwViewVertical.resetAnimation();
    }

    private void updateLightingRelativeView() {
        SoundWaveView soundWaveView;
        setAnyViewGone();
        if (isLandScape()) {
            ((MarginLayoutParams) this.subtitleView.getLayoutParams()).topMargin = Util.getDisplayRect(1).centerY() - (SUBTITLE_VIEW_WIDTH / 2);
            if (isLeftLandScape()) {
                this.mLeftShow.setVisibility(0);
                this.mSwViewLeft.setVisibility(0);
                ((MarginLayoutParams) this.mSwViewLeft.getLayoutParams()).leftMargin = CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode) ? (Util.getCinematicAspectRatioMargin() - this.mSwViewLeft.getWidth()) / 2 : SUBTITLE_CINEMATICASPECTRATIO_OFFSET;
                soundWaveView = this.mSwViewLeft;
            } else if (isRightLandScape()) {
                this.mRightShow.setVisibility(0);
                this.mSwViewRight.setVisibility(0);
                ((MarginLayoutParams) this.mSwViewRight.getLayoutParams()).rightMargin = CameraSettings.isCinematicAspectRatioEnabled(this.mCurrentMode) ? (Util.getCinematicAspectRatioMargin() - this.mSwViewLeft.getWidth()) / 2 : SUBTITLE_CINEMATICASPECTRATIO_OFFSET;
                this.mRightShow.setVisibility(0);
                this.mSwViewRight.setVisibility(0);
                soundWaveView = this.mSwViewRight;
            } else {
                return;
            }
        } else {
            ((MarginLayoutParams) this.subtitleView.getLayoutParams()).topMargin = this.top - SUBTITLE_VERTICAL_OFFSET;
            this.mVerticalShow.setVisibility(0);
            this.mSwViewVertical.setVisibility(0);
            soundWaveView = this.mSwViewVertical;
        }
        soundWaveView.startAnimation();
    }

    public void checkNetWorkStatus() {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) getActivity().getSystemService("connectivity")).getActiveNetworkInfo();
        if (activeNetworkInfo == null || !activeNetworkInfo.isConnected()) {
            this.isNetWorkAvailable = false;
            Log.d(TAG, "checkNetWorkStatus: netWork is unavailable");
            ((Activity) getContext()).runOnUiThread(new Runnable() {
                public void run() {
                    if (FragmentSubtitle.this.isAdded()) {
                        ToastUtils.showToast(FragmentSubtitle.this.getContext(), FragmentSubtitle.this.getResources().getString(R.string.live_sticker_network_error_hint), 80);
                    }
                }
            });
            return;
        }
        this.isNetWorkAvailable = true;
        Log.d(TAG, "checkNetWorkStatus: netWork is available ");
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_SUBTITLE;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_subtitle;
    }

    public void getSubtitleContentAsync(Listener listener, long j) {
        this.mVoiceOnlineRecog.getSubtitleContentAsync(listener, j);
    }

    public String getSubtitleContentSync() {
        return this.mVoiceOnlineRecog.getSubtitleContent();
    }

    @RequiresApi(api = 21)
    public void handleSubtitleRecordingPause() {
        Log.d(TAG, "handleSubtitleRecordingPause: ");
        this.isRecording = false;
        setAnyViewGone();
        this.subtitleView.setVisibility(8);
        TopAlert topAlert2 = this.topAlert;
        if (topAlert2 != null) {
            topAlert2.updateTopAlertLayout();
        }
        this.mRecognitionListener.onRecognitionListener("");
        this.mVoiceOnlineRecog.pauseRecording();
    }

    public void handleSubtitleRecordingResume() {
        Log.d(TAG, "handleSubtitleRecordingResume: ");
        this.isRecording = true;
        this.subtitleView.setVisibility(0);
        TopAlert topAlert2 = this.topAlert;
        if (topAlert2 != null) {
            topAlert2.updateTopAlertLayout();
        }
        updateLightingRelativeView();
        this.mVoiceOnlineRecog.resumeRecording();
    }

    public void handleSubtitleRecordingStart() {
        checkNetWorkStatus();
        CameraStatUtils.trackSubtitleRecordingStart();
        Log.d(TAG, "handleSubtitleRecordingStart: ");
        if (this.topAlert != null && isLandScape()) {
            this.topAlert.setAlertAnim(false);
        }
        this.topAlert.alertSubtitleHint(8, R.string.pref_video_subtitle);
        this.isRecording = true;
        this.subtitleView.setVisibility(0);
        updateLightingRelativeView();
        TopAlert topAlert2 = this.topAlert;
        if (topAlert2 != null) {
            topAlert2.updateTopAlertLayout();
        }
        if (this.isNetWorkAvailable) {
            this.subtitleHandler.sendEmptyMessageDelayed(101, 2000);
            this.mVoiceOnlineRecog.startRecording();
            return;
        }
        CameraStatUtils.trackWithoutNetwork();
    }

    @RequiresApi(api = 21)
    public void handleSubtitleRecordingStop() {
        Log.d(TAG, "handleSubtitleRecordingStop: ");
        this.topAlert.alertSubtitleHint(0, R.string.pref_video_subtitle);
        TopAlert topAlert2 = this.topAlert;
        if (topAlert2 != null) {
            topAlert2.updateTopAlertLayout();
        }
        this.isRecording = false;
        this.subtitleView.setVisibility(0);
        this.mRecognitionListener.onRecognitionListener("");
        this.mVoiceOnlineRecog.stopRecording();
        setAnyViewGone();
    }

    public void initPermission() {
        if (!((DataItemGlobal) DataRepository.provider().dataGlobal()).getCTACanCollect()) {
            this.mCtaNoticeFragment = CtaNoticeFragment.showCta(getActivity().getFragmentManager(), this, 2);
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.subtitleView = view.findViewById(R.id.subtitle_view_menu);
        Rect displayRect = Util.getDisplayRect(1);
        this.top = displayRect.top;
        this.verticalViewHeight = displayRect.height();
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.subtitleView.getLayoutParams();
        marginLayoutParams.topMargin = this.top;
        marginLayoutParams.height = this.verticalViewHeight;
        this.mLeftShow = (TextView) view.findViewById(R.id.voice_value_left);
        this.mRightShow = (TextView) view.findViewById(R.id.voice_value_right);
        this.mVerticalShow = (TextView) view.findViewById(R.id.voice_value_vertical);
        this.mSwViewVertical = (SoundWaveView) view.findViewById(R.id.speed_sw_view);
        this.mSwViewLeft = (SoundWaveView) view.findViewById(R.id.speed_sw_view_left);
        this.mSwViewRight = (SoundWaveView) view.findViewById(R.id.speed_sw_view_right);
        registerProtocol();
        this.topAlert = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        this.mRecognitionListener = new RecognitionListener();
        this.mVoiceOnlineRecog = new VoiceOnlineRecog(getContext(), new RecognitionListener());
    }

    public boolean onBackEvent(int i) {
        return false;
    }

    public void onDestroy() {
        super.onDestroy();
        CtaNoticeFragment ctaNoticeFragment = this.mCtaNoticeFragment;
        if (ctaNoticeFragment != null) {
            ctaNoticeFragment.dismiss();
        }
        this.mVoiceOnlineRecog.onDestroy();
        TopAlert topAlert2 = this.topAlert;
        if (topAlert2 != null) {
            topAlert2.setAlertAnim(true);
        }
    }

    public void onNegativeClick(DialogInterface dialogInterface, int i) {
        this.subtitleView.setVisibility(8);
        CameraSettings.setSubtitleEnabled(this.mCurrentMode, false);
        TopAlert topAlert2 = (TopAlert) ModeCoordinatorImpl.getInstance().getAttachProtocol(172);
        if (topAlert2 != null && topAlert2.isExtraMenuShowing()) {
            topAlert2.refreshExtraMenu();
        }
        DataRepository.dataItemGlobal().setCurrentMode(162);
        ((Camera) getActivity()).onModeSelected(StartControl.create(162).setViewConfigType(1).setNeedBlurAnimation(true).setNeedReConfigureCamera(true));
    }

    public void onPositiveClick(DialogInterface dialogInterface, int i) {
    }

    public void onResume() {
        super.onResume();
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(-1, -1);
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation() {
        return FragmentAnimationFactory.wrapperAnimation(-1, -1);
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        if (DataRepository.dataItemRunning().getComponentRunningSubtitle().isSwitchOn(this.mCurrentMode) && this.isRecording) {
            updateLightingRelativeView();
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(231, this);
        registerBackStack(modeCoordinator, this);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(231, this);
        unRegisterBackStack(modeCoordinator, this);
    }
}
