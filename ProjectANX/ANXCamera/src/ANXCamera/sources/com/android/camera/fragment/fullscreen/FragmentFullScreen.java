package com.android.camera.fragment.fullscreen;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Pair;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.ViewStub;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.annotation.UiThread;
import androidx.core.view.ViewCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.ActivityBase;
import com.android.camera.CameraIntentManager;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.constant.ShareConstant;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.fragment.RecyclerAdapterWrapper;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.mimoji.FragmentMimojiEdit;
import com.android.camera.log.Log;
import com.android.camera.module.LiveModule;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.FullScreenProtocol;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.LiveConfigChanges;
import com.android.camera.protocol.ModeProtocol.LiveVideoEditor;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.ss.android.vesdk.TERecorder.OnConcatFinishedListener;
import com.ss.android.vesdk.VECommonCallback;
import io.reactivex.Completable;
import io.reactivex.functions.Action;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import miui.view.animation.QuarticEaseInInterpolator;
import miui.view.animation.QuarticEaseOutInterpolator;

public class FragmentFullScreen extends BaseFragment implements FullScreenProtocol, HandleBackTrace, OnClickListener {
    public static final int FRAGMENT_INFO = 4086;
    private static final String TAG = "FullScreen";
    private static final int WAIT_RECORD_SEGMENT_CREATED = 0;
    private static final int WAIT_RECORD_SEGMENT_CREATED_TIME_OUT = 5000;
    private static final int WAIT_RECORD_SEGMENT_ERROR_TIME_OUT = 5000;
    private static final int WAIT_RECORD_SEGMENT_TO_CAPTURE = 1;
    private static final int WAIT_RECORD_SEGMENT_WHEN_ERROR = 2;
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    private CameraSnapView mCameraSnapView;
    private ProgressBar mCancelProgress;
    private VECommonCallback mCombineListener;
    private ImageView mCombineProgress;
    private boolean mCombineReady;
    private OnConcatFinishedListener mConcatListener;
    private ProgressBar mConcatProgress;
    private boolean mConcatReady;
    private VECommonCallback mErrorListener;
    /* access modifiers changed from: private */
    public AlertDialog mExitDialog;
    /* access modifiers changed from: private */
    public Bitmap mFirstFrame;
    private FragmentMimojiEdit mFragmentMimojiEdit;
    private Handler mHandler;
    private boolean mIsIntentAction;
    /* access modifiers changed from: private */
    public boolean mIsPlaying;
    private View mLiveViewLayout;
    private ViewStub mLiveViewStub;
    /* access modifiers changed from: private */
    public boolean mPaused;
    private boolean mPendingSaveFinish;
    private boolean mPendingShare;
    private boolean mPendingStartPlay;
    private Runnable mPendingTask;
    private ImageView mPreviewBack;
    private LottieAnimationView mPreviewCombine;
    private ImageView mPreviewCover;
    private FrameLayout mPreviewLayout;
    private ImageView mPreviewShare;
    /* access modifiers changed from: private */
    public ImageView mPreviewStart;
    private TextureView mPreviewTextureView;
    private ContentValues mSaveContentValues;
    private String mSavedPath;
    private Uri mSavedUri;
    /* access modifiers changed from: private */
    public View mScreenLightIndicator;
    /* access modifiers changed from: private */
    public View mScreenLightRootView;
    private ShareAdapter mShareAdapter;
    private View mShareCancel;
    private ViewGroup mShareLayout;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    /* access modifiers changed from: private */
    public TextureListenerWrapper mTextureListener;
    private TextView mTimeView;
    private ViewGroup mTopLayout;

    interface OnFrameUpdatedCallback {
        void onUpdate();
    }

    class TextureListenerWrapper implements SurfaceTextureListener {
        private OnFrameUpdatedCallback mOnFrameUpdatedCallback;
        private volatile boolean mRequestCapture;
        private SurfaceTextureListener mSuperListener;
        private TextureView mTextureHolder;

        public TextureListenerWrapper(TextureView textureView) {
            this.mTextureHolder = textureView;
            this.mSuperListener = textureView.getSurfaceTextureListener();
            if (this.mSuperListener != null) {
                textureView.setSurfaceTextureListener(this);
                return;
            }
            throw new IllegalStateException("plz call this after videoEditor.init()~");
        }

        private Bitmap getFrame() {
            try {
                TextureView textureView = this.mTextureHolder;
                if (textureView != null) {
                    return textureView.getBitmap();
                }
            } catch (OutOfMemoryError e) {
                e.printStackTrace();
            }
            return null;
        }

        @UiThread
        public void clearRequest() {
            this.mOnFrameUpdatedCallback = null;
            this.mRequestCapture = false;
        }

        public boolean hasCaptureRequest() {
            return this.mRequestCapture;
        }

        public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
            this.mSuperListener.onSurfaceTextureAvailable(surfaceTexture, i, i2);
            StringBuilder sb = new StringBuilder();
            sb.append("onSurfaceTextureAvailable : ");
            sb.append(surfaceTexture);
            Log.d(FragmentFullScreen.TAG, sb.toString());
        }

        public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            boolean onSurfaceTextureDestroyed = this.mSuperListener.onSurfaceTextureDestroyed(surfaceTexture);
            StringBuilder sb = new StringBuilder();
            sb.append("onSurfaceTextureDestroyed : ");
            sb.append(surfaceTexture);
            Log.d(FragmentFullScreen.TAG, sb.toString());
            return onSurfaceTextureDestroyed;
        }

        public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
            this.mSuperListener.onSurfaceTextureSizeChanged(surfaceTexture, i, i2);
            StringBuilder sb = new StringBuilder();
            sb.append("onSurfaceTextureAvailable : ");
            sb.append(surfaceTexture);
            Log.d(FragmentFullScreen.TAG, sb.toString());
        }

        public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
            this.mSuperListener.onSurfaceTextureUpdated(surfaceTexture);
            if (!FragmentFullScreen.this.hasFrame() && FragmentFullScreen.this.mIsPlaying) {
                StringBuilder sb = new StringBuilder();
                sb.append("onSurfaceTextureUpdated : ");
                sb.append(surfaceTexture);
                Log.d(FragmentFullScreen.TAG, sb.toString());
                FragmentFullScreen.this.mFirstFrame = getFrame();
            }
            OnFrameUpdatedCallback onFrameUpdatedCallback = this.mOnFrameUpdatedCallback;
            if (onFrameUpdatedCallback != null) {
                onFrameUpdatedCallback.onUpdate();
                this.mOnFrameUpdatedCallback = null;
            }
        }

        public void requestCapture() {
            this.mRequestCapture = true;
        }

        @UiThread
        public void requestUpdateCallbackOnce(OnFrameUpdatedCallback onFrameUpdatedCallback) {
            this.mOnFrameUpdatedCallback = onFrameUpdatedCallback;
        }
    }

    private void animateIn() {
    }

    private void attempt(Runnable runnable) {
        if (runnable != null) {
            LiveConfigChanges liveConfigChanges = (LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
            if (liveConfigChanges == null || liveConfigChanges.getSegmentSize() == 0) {
                Handler handler = this.mHandler;
                if (handler != null) {
                    this.mPendingTask = runnable;
                    handler.sendEmptyMessageDelayed(0, 5000);
                }
            }
            runnable.run();
        }
    }

    private void attemptCaptureFrame() {
        TextureListenerWrapper textureListenerWrapper = this.mTextureListener;
        String str = TAG;
        if (textureListenerWrapper == null) {
            Log.d(str, "mPreviewTextureManager is null~");
            return;
        }
        Log.d(str, "attemptCaptureFrame");
        this.mTextureListener.clearRequest();
        showEmptyView();
        LiveConfigChanges liveConfigChanges = (LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
        if (liveConfigChanges == null || liveConfigChanges.getSegmentSize() == 0) {
            Handler handler = this.mHandler;
            if (handler != null) {
                handler.sendEmptyMessageDelayed(1, 5000);
            }
        }
        this.mTextureListener.requestCapture();
        startConcatVideoIfNeed();
    }

    private boolean checkAndShare() {
        if (this.mSavedUri == null) {
            return false;
        }
        startShare();
        return true;
    }

    /* access modifiers changed from: private */
    public void concatResult(Pair pair) {
        String str;
        if (canProvide()) {
            String str2 = (String) pair.first;
            String str3 = (String) pair.second;
            if (!str2.isEmpty() || !str3.isEmpty()) {
                this.mPendingStartPlay = false;
                this.mConcatReady = true;
                StringBuilder sb = new StringBuilder();
                sb.append("concat: ");
                sb.append(str2);
                sb.append(" | ");
                sb.append(str3);
                String sb2 = sb.toString();
                String str4 = TAG;
                Log.d(str4, sb2);
                LiveVideoEditor liveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
                initCombineListener();
                this.mPreviewTextureView = new TextureView(getContext());
                initCoverView();
                LayoutParams layoutParams = new LayoutParams(-1, -1);
                Rect displayRect = Util.getDisplayRect();
                layoutParams.topMargin = displayRect.top;
                layoutParams.height = displayRect.height();
                this.mPreviewLayout.removeAllViews();
                this.mPreviewLayout.addView(this.mPreviewTextureView, layoutParams);
                this.mPreviewLayout.addView(this.mPreviewCover, layoutParams);
                if (!liveVideoEditor.init(this.mPreviewTextureView, str2, str3, this.mCombineListener, this.mErrorListener)) {
                    onCombineError();
                    return;
                }
                TextureListenerWrapper textureListenerWrapper = this.mTextureListener;
                boolean hasCaptureRequest = textureListenerWrapper != null ? textureListenerWrapper.hasCaptureRequest() : false;
                this.mTextureListener = new TextureListenerWrapper(this.mPreviewTextureView);
                if (hasCaptureRequest) {
                    str = "concat finish and capture frame";
                } else {
                    if (this.mConcatProgress.getVisibility() == 0) {
                        Log.d(str4, "concat finish and start preview");
                        this.mConcatProgress.setVisibility(8);
                        startPlay();
                    } else {
                        if (this.mCombineProgress.getVisibility() == 0) {
                            Log.d(str4, "concat finish and start save");
                            this.mPreviewCombine.setVisibility(8);
                            setProgressBarVisible(0);
                        } else if (this.mShareProgress.getVisibility() == 0) {
                            Log.d(str4, "concat finish and pending share");
                        } else if (this.mPreviewCover.getVisibility() == 0) {
                            str = "concat finish ,capture frame & stop play";
                        }
                        startCombine();
                    }
                    return;
                }
                Log.d(str4, str);
                startCaptureFrame(liveVideoEditor);
                return;
            }
            onCombineError();
        }
    }

    private FragmentMimojiEdit getFragmentMiMoji() {
        String str;
        FragmentMimojiEdit fragmentMimojiEdit = this.mFragmentMimojiEdit;
        String str2 = TAG;
        if (fragmentMimojiEdit == null) {
            str = "getFragmentMiMoji(): fragment is null";
        } else if (fragmentMimojiEdit.isAdded()) {
            return this.mFragmentMimojiEdit;
        } else {
            str = "getFragmentMiMoji(): fragment is not added yet";
        }
        Log.d(str2, str);
        return null;
    }

    /* access modifiers changed from: private */
    public boolean hasFrame() {
        Bitmap bitmap = this.mFirstFrame;
        return bitmap != null && !bitmap.isRecycled();
    }

    private void hideCombineProgress() {
        this.mCombineProgress.clearAnimation();
        this.mCombineProgress.setVisibility(8);
    }

    private void hideCover() {
        ImageView imageView = this.mPreviewCover;
        if (imageView != null) {
            imageView.setVisibility(8);
        }
    }

    private void hideShareSheet() {
        if (this.mShareLayout.getVisibility() != 4) {
            Completable.create(new SlideOutOnSubscribe(this.mShareLayout, 80).setInterpolator(new QuarticEaseInInterpolator()).setDurationTime(200)).subscribe();
        }
    }

    private void initCombineListener() {
        this.mCombineListener = new VECommonCallback() {
            public void onCallback(int i, int i2, float f, String str) {
                String str2;
                String str3 = FragmentFullScreen.TAG;
                if (i == 4098) {
                    str2 = "play finished and loop";
                } else if (i == 4101) {
                    return;
                } else {
                    if (i == 4103) {
                        FragmentFullScreen.this.onCombineSuccess();
                        return;
                    } else if (i == 4105) {
                        return;
                    } else {
                        if (i != 4112) {
                            StringBuilder sb = new StringBuilder();
                            sb.append("CombineCallback: ");
                            sb.append(i);
                            str2 = sb.toString();
                        } else {
                            str2 = "CombineStart";
                        }
                    }
                }
                Log.d(str3, str2);
            }
        };
        this.mErrorListener = new VECommonCallback() {
            public void onCallback(int i, int i2, float f, String str) {
                StringBuilder sb = new StringBuilder();
                sb.append("CombineError: ");
                sb.append(i);
                String str2 = " | ";
                sb.append(str2);
                sb.append(i2);
                sb.append(str2);
                sb.append(f);
                sb.append(str2);
                sb.append(str);
                Log.e(FragmentFullScreen.TAG, sb.toString());
                FragmentFullScreen.this.onCombineError();
            }
        };
    }

    private void initConcatListener() {
        this.mConcatListener = new OnConcatFinishedListener() {
            public void onConcatFinished(int i) {
                StringBuilder sb = new StringBuilder();
                sb.append("onConcatFinished ");
                sb.append(i);
                Log.d(FragmentFullScreen.TAG, sb.toString());
                LiveConfigChanges liveConfigChanges = (LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
                if (!FragmentFullScreen.this.mPaused && liveConfigChanges != null) {
                    FragmentFullScreen.this.concatResult(liveConfigChanges.getConcatResult());
                }
            }
        };
    }

    private void initCoverView() {
        if (this.mPreviewCover == null) {
            this.mPreviewCover = new ImageView(getContext());
            this.mPreviewCover.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
            this.mPreviewCover.setVisibility(8);
        }
    }

    private void initHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler() {
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                    int i = message.what;
                    if (i == 0) {
                        FragmentFullScreen.this.runPendingTask();
                    } else if (i == 1) {
                        if (FragmentFullScreen.this.mTextureListener != null) {
                            FragmentFullScreen.this.mTextureListener.requestCapture();
                        }
                        FragmentFullScreen.this.startConcatVideoIfNeed();
                    } else if (i == 2) {
                        FragmentFullScreen.this.quitLiveRecordPreview(false);
                    }
                }
            };
        }
    }

    private void initLiveView(View view) {
        this.mTopLayout = (ViewGroup) view.findViewById(R.id.live_preview_top);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopLayout.getLayoutParams();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.height = Display.getTopBarHeight();
        this.mPreviewLayout = (FrameLayout) view.findViewById(R.id.live_preview_layout);
        this.mConcatProgress = (ProgressBar) view.findViewById(R.id.live_concat_progress);
        this.mCombineProgress = (ImageView) view.findViewById(R.id.live_save_progress);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.live_share_progress);
        this.mCancelProgress = (ProgressBar) view.findViewById(R.id.live_back_progress);
        this.mTimeView = (TextView) view.findViewById(R.id.live_preview_recording_time_view);
        this.mCameraSnapView = (CameraSnapView) view.findViewById(R.id.live_preview_save_circle);
        this.mCameraSnapView.setParameters(PaintConditionReferred.create(this.mCurrentMode));
        this.mCameraSnapView.hideCirclePaintItem();
        this.mCameraSnapView.hideRoundPaintItem();
        this.mCameraSnapView.setSnapClickEnable(false);
        this.mPreviewCombine = (LottieAnimationView) view.findViewById(R.id.live_preview_save);
        this.mPreviewCombine.setScale(0.38f);
        this.mPreviewCombine.O0000O0o((int) R.raw.vv_save);
        this.mPreviewCombine.O0000oO();
        this.mPreviewBack = (ImageView) view.findViewById(R.id.live_preview_back);
        this.mPreviewShare = (ImageView) view.findViewById(R.id.live_preview_share);
        this.mPreviewStart = (ImageView) view.findViewById(R.id.live_preview_play);
        this.mShareLayout = (ViewGroup) view.findViewById(R.id.live_share_layout);
        this.mShareRecyclerView = (RecyclerView) this.mShareLayout.findViewById(R.id.live_share_list);
        this.mShareRecyclerView.setFocusable(false);
        this.mShareCancel = this.mShareLayout.findViewById(R.id.live_share_cancel);
        this.mShareCancel.setOnClickListener(this);
        this.mPreviewLayout.setOnClickListener(this);
        this.mCameraSnapView.setOnClickListener(this);
        this.mPreviewCombine.setOnClickListener(this);
        this.mPreviewBack.setOnClickListener(this);
        this.mPreviewShare.setOnClickListener(this);
        this.mPreviewStart.setOnClickListener(this);
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.live_preview_bottom_action);
        ((MarginLayoutParams) this.mBottomActionView.getLayoutParams()).height = Display.getBottomHeight();
        this.mBottomActionView.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mPreviewBack, this.mPreviewShare, this.mPreviewStart);
        FolmeUtils.touchScale(this.mPreviewCombine);
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.live_preview_bottom_parent);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomLayout.getLayoutParams();
        marginLayoutParams2.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
        marginLayoutParams2.bottomMargin = Display.getBottomMargin();
        marginLayoutParams2.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        ((MarginLayoutParams) this.mTimeView.getLayoutParams()).height = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        this.mBottomActionView.setBackgroundResource(R.color.fullscreen_background);
    }

    /* access modifiers changed from: private */
    public void onCombineError() {
        Log.d(TAG, "onCombineError");
        Handler handler = this.mHandler;
        if (handler != null) {
            handler.post(new O000000o(this));
        }
    }

    /* access modifiers changed from: private */
    public void onCombineSuccess() {
        String str = TAG;
        Log.d(str, "combineSuccess");
        this.mCombineReady = true;
        if (!this.mPaused) {
            if (this.mPendingShare) {
                Log.d(str, "combineSuccess and share");
                ((LiveModule) ((ActivityBase) getContext()).getCurrentModule()).startSaveToLocal();
            } else if (this.mPendingSaveFinish) {
                Log.d(str, "combineSuccess and finish");
                this.mHandler.post(new O0000O0o(this));
            } else {
                Log.d(str, "combineSuccess and skip");
            }
        }
    }

    private void onPlayCompleted() {
        this.mHandler.post(new Runnable() {
            public void run() {
                FragmentFullScreen.this.mIsPlaying = false;
                FragmentFullScreen.this.mPreviewStart.setVisibility(0);
            }
        });
    }

    private void pausePlay() {
        if (this.mIsPlaying) {
            this.mIsPlaying = false;
            this.mPreviewStart.setVisibility(0);
            LiveVideoEditor liveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
            if (liveVideoEditor != null) {
                liveVideoEditor.pausePlay();
            }
        }
    }

    private void resumePlay() {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mPreviewStart.setVisibility(8);
            LiveVideoEditor liveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
            if (liveVideoEditor != null) {
                liveVideoEditor.resumePlay();
            }
        }
    }

    /* access modifiers changed from: private */
    public void runPendingTask() {
        Runnable runnable = this.mPendingTask;
        if (runnable != null) {
            runnable.run();
            this.mPendingTask = null;
        }
    }

    private void setProgressBarVisible(int i) {
        if (this.mCombineProgress.getVisibility() != i) {
            if (i == 0) {
                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, (float) (this.mCombineProgress.getLayoutParams().width / 2), (float) (this.mCombineProgress.getLayoutParams().height / 2));
                rotateAnimation.setDuration((long) getResources().getInteger(R.integer.post_process_duration));
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setRepeatMode(1);
                rotateAnimation.setRepeatCount(-1);
                this.mCombineProgress.setAnimation(rotateAnimation);
                this.mCombineProgress.setVisibility(0);
            } else {
                hideCombineProgress();
            }
        }
    }

    private void shareMore() {
        Util.shareMediaToMore(getContext(), this.mSavedPath, getString(R.string.live_edit_share_title), true);
    }

    private void showCover() {
        ImageView imageView = this.mPreviewCover;
        if (imageView != null) {
            imageView.setVisibility(0);
        }
    }

    private void showEmptyView() {
        initCoverView();
        if (this.mPreviewLayout.indexOfChild(this.mPreviewCover) == -1) {
            this.mPreviewLayout.addView(this.mPreviewCover);
        }
        showCover();
        this.mPreviewStart.setVisibility(8);
        this.mConcatProgress.setVisibility(0);
        this.mPreviewLayout.setVisibility(0);
    }

    /* access modifiers changed from: private */
    public void showExitConfirm() {
        this.mPreviewBack.setVisibility(0);
        this.mCancelProgress.setVisibility(8);
        if (this.mExitDialog == null) {
            CameraStatUtils.trackLiveClick("exit");
            this.mExitDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.live_edit_exit_message), getString(R.string.live_edit_exit_confirm), new Runnable() {
                public void run() {
                    Log.u(FragmentFullScreen.TAG, "showExitConfirm onClick positive");
                    CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_PLAY_EXIT_CONFIRM);
                    FragmentFullScreen.this.quitLiveRecordPreview(false);
                }
            }, null, null, getString(R.string.snap_cancel), C0306O00000oo.INSTANCE);
            this.mExitDialog.setOnDismissListener(new OnDismissListener() {
                public void onDismiss(DialogInterface dialogInterface) {
                    FragmentFullScreen.this.mExitDialog = null;
                }
            });
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:26:0x0097  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x009b  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showShareSheet() {
        boolean z;
        String str;
        List<ResolveInfo> queryIntentActivities = getContext().getPackageManager().queryIntentActivities(Util.getShareMediaIntent(getContext(), this.mSavedPath, true), 65536);
        String str2 = TAG;
        if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
            Log.d(str2, "no IntentActivities");
            return;
        }
        ArrayList arrayList = new ArrayList();
        int length = ShareConstant.DEFAULT_SHARE_LIST.length;
        for (ResolveInfo resolveInfo : queryIntentActivities) {
            if (arrayList.size() == length) {
                break;
            }
            int i = 0;
            while (true) {
                if (i >= length) {
                    break;
                } else if (ShareConstant.DEFAULT_SHARE_LIST[i].equals(resolveInfo.activityInfo.name)) {
                    ShareInfo shareInfo = new ShareInfo(i, resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, ShareConstant.DEFAULT_SHARE_LIST_ICON[i], ShareConstant.DEFAULT_SHARE_LIST_NAME[i]);
                    arrayList.add(shareInfo);
                    break;
                } else {
                    i++;
                }
            }
        }
        if (arrayList.isEmpty()) {
            str = "no default share entry";
        } else if (((ShareInfo) arrayList.get(0)).index <= 1 || arrayList.size() >= 2) {
            z = false;
            if (!z) {
                shareMore();
                return;
            }
            ShareInfo shareInfo2 = new ShareInfo(length + 1, CameraIntentManager.CALLER_MIUI_CAMERA, "more", R.drawable.ic_live_share_more, R.string.accessibility_more);
            arrayList.add(shareInfo2);
            Collections.sort(arrayList, new Comparator() {
                public int compare(ShareInfo shareInfo, ShareInfo shareInfo2) {
                    int i = shareInfo.index;
                    int i2 = shareInfo2.index;
                    if (i == i2) {
                        return 0;
                    }
                    return i > i2 ? 1 : -1;
                }
            });
            int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.live_share_item_margin);
            int max = Math.max((Display.getWindowWidth() - (dimensionPixelSize * 2)) / arrayList.size(), (int) (((float) (Display.getWindowWidth() - dimensionPixelSize)) / 5.5f));
            ShareAdapter shareAdapter = this.mShareAdapter;
            if (shareAdapter == null || shareAdapter.getItemCount() != arrayList.size()) {
                this.mShareAdapter = new ShareAdapter(arrayList, this, max);
                LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), str2);
                linearLayoutManagerWrapper.setOrientation(0);
                this.mShareRecyclerView.setLayoutManager(linearLayoutManagerWrapper);
                RecyclerAdapterWrapper recyclerAdapterWrapper = new RecyclerAdapterWrapper(this.mShareAdapter);
                Space space = new Space(getContext());
                space.setMinimumWidth(dimensionPixelSize);
                recyclerAdapterWrapper.addHeader(space);
                Space space2 = new Space(getContext());
                space2.setMinimumWidth(dimensionPixelSize);
                recyclerAdapterWrapper.addFooter(space2);
                this.mShareRecyclerView.setAdapter(recyclerAdapterWrapper);
            } else {
                this.mShareAdapter.setShareInfoList(arrayList);
                this.mShareAdapter.notifyDataSetChanged();
            }
            if (Display.getNavigationBarHeight() > 0) {
                ((MarginLayoutParams) this.mShareLayout.getLayoutParams()).bottomMargin = Display.getNavigationBarHeight();
            }
            Completable.create(new SlideInOnSubscribe(this.mShareLayout, 80).setInterpolator(new QuarticEaseOutInterpolator()).setDurationTime(200)).subscribe();
            return;
        } else {
            str = "not match default share strategy";
        }
        Log.d(str2, str);
        z = true;
        if (!z) {
        }
    }

    private void startCaptureFrame(LiveVideoEditor liveVideoEditor) {
        this.mIsPlaying = true;
        this.mTextureListener.requestUpdateCallbackOnce(new O00000o(this, liveVideoEditor));
        liveVideoEditor.startPlay();
    }

    /* access modifiers changed from: private */
    public void startCombine() {
        this.mCombineReady = false;
        String asString = this.mSaveContentValues.getAsString("_data");
        LiveVideoEditor liveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
        if (liveVideoEditor != null) {
            liveVideoEditor.combineVideoAudio(asString, this.mCombineListener, this.mErrorListener);
        }
    }

    /* access modifiers changed from: private */
    public void startConcatVideoIfNeed() {
        String str = TAG;
        Log.d(str, "startConcatVideo");
        boolean z = false;
        this.mConcatReady = false;
        this.mIsPlaying = false;
        initConcatListener();
        LiveConfigChanges liveConfigChanges = (LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201);
        if (liveConfigChanges != null && liveConfigChanges.onRecordConcat(this.mConcatListener)) {
            z = true;
        }
        if (!z) {
            Log.d(str, "concat failed");
            String str2 = "";
            concatResult(new Pair(str2, str2));
        }
    }

    private void startPlay() {
        if (!this.mPaused && !this.mIsPlaying) {
            LiveVideoEditor liveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
            if (liveVideoEditor != null) {
                this.mIsPlaying = true;
                if (this.mTextureListener == null) {
                    this.mPreviewStart.setVisibility(8);
                    this.mPreviewLayout.setVisibility(0);
                    hideCover();
                } else {
                    this.mPreviewLayout.setVisibility(0);
                    this.mTextureListener.requestUpdateCallbackOnce(new O00000Oo(this));
                }
                liveVideoEditor.startPlay();
            }
        }
    }

    private void startShare() {
        this.mPendingShare = false;
        showShareSheet();
    }

    private void updateCoverView(ImageView imageView) {
        String str = TAG;
        if (imageView == null) {
            Log.w(str, "cover view is null~");
            return;
        }
        if (hasFrame()) {
            imageView.setImageBitmap(this.mFirstFrame);
        } else {
            Log.w(str, "frame didn't created~");
        }
    }

    public /* synthetic */ void O000000o(LiveVideoEditor liveVideoEditor) {
        this.mIsPlaying = false;
        updateCoverView(this.mPreviewCover);
        liveVideoEditor.pausePlay();
        this.mConcatProgress.setVisibility(8);
        this.mPreviewStart.setVisibility(0);
    }

    public /* synthetic */ void O000oOoO() {
        if (((CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161)) != null) {
            quitLiveRecordPreview(false);
        } else {
            this.mHandler.sendEmptyMessageDelayed(2, 5000);
        }
    }

    public /* synthetic */ void O000oOoo() {
        quitLiveRecordPreview(true);
    }

    public /* synthetic */ void O000oo0() {
        this.mFirstFrame = null;
        this.mTextureListener.requestCapture();
        startConcatVideoIfNeed();
    }

    public /* synthetic */ void O000oo0o() {
        this.mPreviewStart.setVisibility(8);
        hideCover();
    }

    public int getFragmentInto() {
        return 4086;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_full_screen;
    }

    public ContentValues getSaveContentValues() {
        return this.mSaveContentValues;
    }

    public void hideScreenLight() {
        if (this.mScreenLightIndicator.getVisibility() != 8) {
            this.mScreenLightRootView.setOnClickListener(null);
            Completable.create(new AlphaOutOnSubscribe(this.mScreenLightRootView).setDurationTime(200)).subscribe((Action) new Action() {
                public void run() {
                    FragmentFullScreen.this.mScreenLightIndicator.setVisibility(8);
                    FragmentFullScreen.this.mScreenLightRootView.setVisibility(8);
                }
            });
        }
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mScreenLightRootView = view.findViewById(R.id.screen_light_root_view);
        this.mScreenLightIndicator = view.findViewById(R.id.screen_light_indicator);
        this.mLiveViewStub = (ViewStub) view.findViewById(R.id.live_record_preview);
    }

    public boolean isLiveRecordPreviewShown() {
        View view = this.mLiveViewLayout;
        return view != null && view.getVisibility() == 0;
    }

    public boolean isLiveRecordSaving() {
        return this.mPendingSaveFinish || this.mPendingShare;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        if (this.mFragmentMimojiEdit == null && C0122O00000o.instance().OOO0Oo0()) {
            this.mFragmentMimojiEdit = new FragmentMimojiEdit();
            this.mFragmentMimojiEdit.registerProtocol();
            this.mFragmentMimojiEdit.setDegree(this.mDegree);
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentMimojiEdit fragmentMimojiEdit = this.mFragmentMimojiEdit;
            FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.full_screen_lift, (Fragment) fragmentMimojiEdit, fragmentMimojiEdit.getFragmentTag());
        }
    }

    public void notifyDataChanged(int i, int i2) {
        super.notifyDataChanged(i, i2);
        View view = this.mLiveViewLayout;
        if (view != null && view.getVisibility() == 0 && this.mCombineReady) {
            if (this.mPendingSaveFinish) {
                onCombineSuccess();
            } else if (this.mPendingShare) {
                this.mPendingShare = false;
                this.mPreviewShare.setVisibility(0);
                this.mShareProgress.setVisibility(8);
            }
        }
        if (this.mFragmentMimojiEdit == null && C0122O00000o.instance().OOOOoo()) {
            this.mFragmentMimojiEdit = new FragmentMimojiEdit();
            this.mFragmentMimojiEdit.registerProtocol();
            this.mFragmentMimojiEdit.setDegree(this.mDegree);
            FragmentManager childFragmentManager = getChildFragmentManager();
            FragmentMimojiEdit fragmentMimojiEdit = this.mFragmentMimojiEdit;
            FragmentUtils.addFragmentWithTag(childFragmentManager, (int) R.id.full_screen_lift, (Fragment) fragmentMimojiEdit, fragmentMimojiEdit.getFragmentTag());
        }
    }

    public boolean onBackEvent(int i) {
        if (i != 1) {
            return false;
        }
        View view = this.mLiveViewLayout;
        if (view == null || view.getVisibility() != 0) {
            FragmentMimojiEdit fragmentMiMoji = getFragmentMiMoji();
            return fragmentMiMoji != null && fragmentMiMoji.onBackEvent(i);
        }
        if (this.mShareLayout.getVisibility() == 0) {
            hideShareSheet();
        } else {
            showExitConfirm();
        }
        return true;
    }

    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        Runnable runnable;
        if (!(this.mConcatProgress.getVisibility() == 0 || this.mCombineProgress.getVisibility() == 0 || this.mShareProgress.getVisibility() == 0 || this.mCancelProgress.getVisibility() == 0)) {
            int id = view.getId();
            String str = TAG;
            switch (id) {
                case R.id.live_preview_back /*2131296712*/:
                    Log.u(str, "onClick: live_preview_back");
                    this.mCancelProgress.setVisibility(0);
                    this.mPreviewBack.setVisibility(8);
                    runnable = new C0305O00000oO(this);
                case R.id.live_preview_bottom_action /*2131296713*/:
                    Log.u(str, "onClick: live_preview_bottom_action");
                    break;
                case R.id.live_preview_layout /*2131296715*/:
                    Log.u(str, "onClick: live_preview_layout");
                    pausePlay();
                    break;
                case R.id.live_preview_play /*2131296716*/:
                    StringBuilder sb = new StringBuilder();
                    sb.append("onClick: live_preview_play, mConcatReady=");
                    sb.append(this.mConcatReady);
                    Log.u(str, sb.toString());
                    if (this.mConcatReady) {
                        hideShareSheet();
                        startPlay();
                        break;
                    } else {
                        Log.d(str, "concat not finished, show play progress");
                        this.mPreviewStart.setVisibility(8);
                        this.mConcatProgress.setVisibility(0);
                        runnable = new O00000o0(this);
                    }
                case R.id.live_preview_save /*2131296718*/:
                case R.id.live_preview_save_circle /*2131296719*/:
                    Log.u(str, "onClick: live_preview_save");
                    CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_PLAY_SAVE);
                    if (this.mSavedUri != null) {
                        this.mPendingSaveFinish = true;
                        onCombineSuccess();
                        break;
                    } else if (!this.mConcatReady) {
                        Log.d(str, "concat not finished, show save progress and wait to save");
                        this.mPendingSaveFinish = true;
                        this.mPreviewCombine.setVisibility(8);
                        setProgressBarVisible(0);
                        runnable = new O00000o0(this);
                    } else if (!this.mCombineReady) {
                        pausePlay();
                        this.mPreviewStart.setVisibility(8);
                        this.mPreviewCombine.setVisibility(8);
                        setProgressBarVisible(0);
                        this.mPendingSaveFinish = true;
                        runnable = new O0000OOo(this);
                    }
                    break;
                case R.id.live_preview_share /*2131296720*/:
                    Log.u(str, "onClick: live_preview_share");
                    if (this.mConcatProgress.getVisibility() == 0) {
                        Log.d(str, "concat not finished, skip share~");
                        return;
                    }
                    CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_PLAY_SHARE);
                    if (!checkAndShare()) {
                        this.mPendingShare = true;
                        this.mPreviewShare.setVisibility(8);
                        this.mShareProgress.setVisibility(0);
                        if (!this.mConcatReady) {
                            Log.d(str, "concat not finished, show share progress and wait to share");
                            runnable = new O00000o0(this);
                        } else {
                            pausePlay();
                            runnable = new O0000OOo(this);
                        }
                    }
                    break;
                case R.id.live_share_cancel /*2131296724*/:
                    Log.u(str, "onClick: live_share_cancel");
                    hideShareSheet();
                    break;
                case R.id.live_share_item /*2131296726*/:
                    Log.u(str, "onClick: live_share_item");
                    ShareInfo shareInfo = (ShareInfo) view.getTag();
                    hideShareSheet();
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(Live.VALUE_LIVE_CLICK_PLAY_SHARE_SHEET);
                    sb2.append(shareInfo.index);
                    CameraStatUtils.trackLiveClick(sb2.toString());
                    if (!shareInfo.className.equals("more")) {
                        Util.startShareMedia(shareInfo.packageName, shareInfo.className, getContext(), this.mSavedPath, true);
                        break;
                    } else {
                        shareMore();
                        break;
                    }
            }
            attempt(runnable);
        }
    }

    public void onDestroy() {
        super.onDestroy();
        ImageView imageView = this.mCombineProgress;
        if (imageView != null) {
            imageView.clearAnimation();
        }
    }

    public void onLiveSaveToLocalFinished(Uri uri, String str) {
        this.mSavedUri = uri;
        this.mSavedPath = str;
        if (!this.mPaused && this.mPendingShare) {
            this.mPreviewShare.setVisibility(0);
            this.mShareProgress.setVisibility(8);
            startShare();
        }
    }

    public void onPause() {
        super.onPause();
        String str = TAG;
        Log.d(str, "onPause + ");
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
            this.mPendingTask = null;
            this.mPreviewCombine.setVisibility(0);
            this.mPreviewStart.setVisibility(0);
            this.mPreviewShare.setVisibility(0);
            this.mPreviewBack.setVisibility(0);
            this.mConcatProgress.setVisibility(8);
            hideCombineProgress();
            this.mShareProgress.setVisibility(8);
            this.mCancelProgress.setVisibility(8);
        }
        Handler handler2 = this.mHandler;
        if (handler2 != null && handler2.hasMessages(1)) {
            this.mHandler.removeMessages(1);
        }
        Handler handler3 = this.mHandler;
        if (handler3 != null && handler3.hasMessages(2)) {
            this.mHandler.removeMessages(2);
        }
        this.mPaused = true;
        this.mConcatReady = false;
        this.mCombineReady = false;
        pausePlay();
        Log.d(str, "onPause - ");
    }

    public void onRecordSegmentCreated() {
        Log.d(TAG, "onRecordSegmentCreated");
        Handler handler = this.mHandler;
        if (handler != null && handler.hasMessages(0)) {
            this.mHandler.removeMessages(0);
            this.mHandler.post(new O0000Oo(this));
        }
        Handler handler2 = this.mHandler;
        if (handler2 != null && handler2.hasMessages(1)) {
            this.mHandler.removeMessages(1);
            this.mHandler.post(new O0000Oo0(this));
        }
    }

    public void onResume() {
        super.onResume();
        StringBuilder sb = new StringBuilder();
        sb.append("onResume + ");
        sb.append(this.mPendingShare);
        String str = ",";
        sb.append(str);
        sb.append(this.mPendingSaveFinish);
        sb.append(str);
        sb.append(this.mPendingStartPlay);
        sb.append(",mTextureListener + ");
        sb.append(this.mTextureListener);
        String sb2 = sb.toString();
        String str2 = TAG;
        Log.d(str2, sb2);
        if (this.mPendingShare) {
            this.mShareProgress.setVisibility(8);
            this.mPreviewShare.setVisibility(0);
            this.mPendingShare = false;
        }
        if (this.mPendingSaveFinish) {
            this.mPreviewCombine.setVisibility(0);
            hideCombineProgress();
            this.mPendingSaveFinish = false;
        }
        this.mPaused = false;
        if (this.mTextureListener != null) {
            if (hasFrame()) {
                this.mPreviewStart.setVisibility(0);
                this.mConcatProgress.setVisibility(8);
                this.mTextureListener.clearRequest();
                updateCoverView(this.mPreviewCover);
                showCover();
            } else {
                attemptCaptureFrame();
            }
        } else if (this.mPendingStartPlay) {
            showEmptyView();
            attempt(new O00000o0(this));
        }
        Log.d(str2, "onResume - ");
    }

    public void onStop() {
        super.onStop();
        hideScreenLight();
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        View view = this.mLiveViewLayout;
        if (view != null && view.getVisibility() == 0) {
            if (i2 == 3) {
                AlertDialog alertDialog = this.mExitDialog;
                if (alertDialog != null) {
                    alertDialog.dismiss();
                    this.mExitDialog = null;
                }
                this.mLiveViewLayout.setVisibility(8);
                return;
            }
            this.mConcatReady = false;
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return null;
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return null;
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        View view = this.mLiveViewLayout;
        if (view != null && view.getVisibility() == 0) {
            list.add(this.mPreviewStart);
            list.add(this.mCameraSnapView);
            list.add(this.mPreviewCombine);
            list.add(this.mPreviewBack);
            list.add(this.mPreviewShare);
        }
    }

    public void quitLiveRecordPreview(boolean z) {
        StringBuilder sb = new StringBuilder();
        sb.append("quitLiveRecordPreview ");
        sb.append(z);
        String sb2 = sb.toString();
        String str = TAG;
        Log.d(str, sb2);
        if (this.mConcatProgress.getVisibility() == 0) {
            this.mConcatProgress.setVisibility(8);
        }
        if (this.mCombineProgress.getVisibility() == 0) {
            hideCombineProgress();
        }
        if (this.mShareProgress.getVisibility() == 0) {
            this.mShareProgress.setVisibility(8);
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null) {
            Log.d(str, "concat error, action null");
            return;
        }
        this.mLiveViewLayout.setVisibility(8);
        this.mPendingSaveFinish = false;
        if (z) {
            cameraAction.onReviewDoneClicked();
        } else {
            cameraAction.onReviewCancelClicked();
        }
        LiveVideoEditor liveVideoEditor = (LiveVideoEditor) ModeCoordinatorImpl.getInstance().getAttachProtocol(209);
        if (liveVideoEditor != null) {
            liveVideoEditor.onDestory();
        }
        this.mCombineListener = null;
        this.mErrorListener = null;
        ImageView imageView = this.mPreviewCover;
        if (imageView != null) {
            imageView.setImageBitmap(null);
            this.mPreviewCover = null;
        }
        Bitmap bitmap = this.mFirstFrame;
        if (bitmap != null) {
            bitmap.recycle();
            this.mFirstFrame = null;
        }
        if (this.mTextureListener != null) {
            this.mTextureListener = null;
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(196, this);
        registerBackStack(modeCoordinator, this);
    }

    public void setScreenLightColor(int i) {
        this.mScreenLightRootView.setBackgroundColor(i);
    }

    public boolean showScreenLight() {
        if (this.mScreenLightIndicator.getVisibility() == 0) {
            return false;
        }
        this.mScreenLightRootView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
            }
        });
        this.mScreenLightIndicator.setVisibility(0);
        Completable.create(new AlphaInOnSubscribe(this.mScreenLightRootView).setDurationTime(100)).subscribe();
        return true;
    }

    public void startLiveRecordPreview(ContentValues contentValues) {
        this.mSavedUri = null;
        this.mSaveContentValues = contentValues;
        initHandler();
        if (this.mLiveViewLayout == null) {
            this.mLiveViewLayout = this.mLiveViewStub.inflate();
            initLiveView(this.mLiveViewLayout);
        }
        this.mIsIntentAction = DataRepository.dataItemGlobal().isIntentAction();
        this.mPreviewLayout.setVisibility(8);
        this.mPreviewLayout.removeAllViews();
        this.mLiveViewLayout.setVisibility(0);
        hideCombineProgress();
        this.mShareProgress.setVisibility(8);
        this.mCancelProgress.setVisibility(8);
        this.mShareLayout.setVisibility(4);
        ViewCompat.setRotation(this.mPreviewStart, (float) this.mDegree);
        ViewCompat.setRotation(this.mCameraSnapView, (float) this.mDegree);
        ViewCompat.setRotation(this.mPreviewCombine, (float) this.mDegree);
        ViewCompat.setRotation(this.mPreviewBack, (float) this.mDegree);
        ViewCompat.setRotation(this.mPreviewShare, (float) this.mDegree);
        Completable.create(new AlphaInOnSubscribe(this.mCameraSnapView)).subscribe();
        Completable.create(new AlphaInOnSubscribe(this.mPreviewCombine)).subscribe();
        Completable.create(new AlphaInOnSubscribe(this.mPreviewBack)).subscribe();
        Completable.create(new AlphaInOnSubscribe(this.mPreviewStart)).subscribe();
        if (((ActivityBase) getContext()).startFromKeyguard()) {
            this.mPreviewShare.setVisibility(8);
        } else {
            Completable.create(new AlphaInOnSubscribe(this.mPreviewShare)).subscribe();
        }
        this.mTimeView.setText(((LiveConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(201)).getTimeValue());
        this.mTimeView.setVisibility(0);
        this.mPreviewStart.setVisibility(8);
        this.mConcatProgress.setVisibility(0);
        this.mConcatReady = false;
        this.mCombineReady = false;
        this.mPendingShare = false;
        this.mPendingSaveFinish = false;
        this.mPendingStartPlay = true;
        startConcatVideoIfNeed();
    }

    public void startLiveRecordSaving() {
        if (isAdded() && this.mCameraSnapView.getVisibility() == 0) {
            this.mCameraSnapView.performClick();
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(196, this);
        unRegisterBackStack(modeCoordinator, this);
    }
}
