package com.android.camera.fragment.vv;

import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.media.MediaMetadataRetriever;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.view.Surface;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.core.view.ViewCompat;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.Display;
import com.android.camera.FileCompat;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.AlphaInOnSubscribe;
import com.android.camera.animation.type.AlphaOutOnSubscribe;
import com.android.camera.data.DataRepository;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.data.observeable.VMProcessing;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.fragment.vv.page.PageIndicatorView;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager.PageListener;
import com.android.camera.fragment.vv.page.PagerGridSnapHelper;
import com.android.camera.log.Log;
import com.android.camera.module.LiveModuleSubVV;
import com.android.camera.module.Module;
import com.android.camera.module.loader.NullHolder;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.LiveConfigVV;
import com.android.camera.protocol.ModeProtocol.LiveVVProcess;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Live;
import com.android.camera.statistic.MistatsConstants.VLogAttr;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.CameraSnapView.SnapListener;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.TextureVideoView.MediaPlayerCallback;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.android.camera.ui.vv.VVProgressView;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class FragmentVVProcess extends BaseFragment implements OnClickListener, HandleBackTrace, LiveVVProcess, SnapListener {
    /* access modifiers changed from: private */
    public static final String TAG = "FragmentVVProcess";
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    private CameraSnapView mCameraSnapView;
    /* access modifiers changed from: private */
    public CheckBox mCheckbox;
    /* access modifiers changed from: private */
    public ViewGroup mCheckboxLayout;
    private TextView mCheckboxText;
    private ImageView mCombineProgress;
    private ProgressBar mConcatProgress;
    private int mCurrentIndex;
    private VVItem mCurrentVVItem;
    private VVWorkspaceItem mCurrentWorkspaceItem;
    private List mDurationList;
    private Handler mHandler;
    private boolean mIsIntentAction;
    private TextView mLandscapeHint;
    /* access modifiers changed from: private */
    public PageIndicatorView mPageIndicatorView;
    private PagerGridSnapHelper mPagerGridSnapHelper;
    private boolean mPaused;
    private boolean mPendingShare;
    private ImageView mPreviewBack;
    private LottieAnimationView mPreviewCombine;
    private FrameLayout mPreviewCover;
    private FrameLayout mPreviewLayout;
    private ImageView mPreviewNext;
    private ImageView mPreviewPrevious;
    private ImageView mPreviewShare;
    /* access modifiers changed from: private */
    public ImageView mPreviewStart;
    /* access modifiers changed from: private */
    public ImageView mPreviewThumb;
    private VVProgressView mProgressView;
    private View mRootView;
    private ContentValues mSaveContentValues;
    private String mSavedPath;
    private Uri mSavedUri;
    private ImageView mSegmentPreview;
    private ImageView mSegmentReverse;
    private VVShareAdapter mShareAdapter;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    private TextView mStabilityHint;
    /* access modifiers changed from: private */
    public TextureVideoView mTextureVideoView;
    /* access modifiers changed from: private */
    public View mVVDialog;
    private TextView mVVDialogCancel;
    private TextView mVVDialogConfirm;
    private TextView mVVDialogMessage;
    /* access modifiers changed from: private */
    public View mVVShare;
    private TextView mVVShareMessage;
    private boolean mVideoRecordingPaused;
    private boolean mVideoRecordingStarted;
    private VMProcessing mVmProcessing;
    /* access modifiers changed from: private */
    public boolean mWaitingResultSurfaceTexture;

    static /* synthetic */ NullHolder O0000o0o(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        return rotateBitmap(mediaMetadataRetriever.getFrameAtTime(0), 90);
    }

    private void animateIn(View view) {
        if (view.getVisibility() != 0) {
            Completable.create(new AlphaInOnSubscribe(view)).subscribe();
        }
    }

    private void animateOut(View view) {
        if (view.getVisibility() == 0) {
            Completable.create(new AlphaOutOnSubscribe(view)).subscribe();
        }
    }

    private boolean checkAndShare() {
        if (this.mSavedUri == null) {
            return false;
        }
        showShareSheet();
        return true;
    }

    /* access modifiers changed from: private */
    public boolean hideShareSheet() {
        if (this.mVVShare.getVisibility() != 0) {
            return false;
        }
        this.mVVShare.setVisibility(8);
        this.mPreviewStart.setVisibility(0);
        this.mPreviewThumb.setVisibility(0);
        return true;
    }

    /* access modifiers changed from: private */
    public boolean hideVVDialog() {
        if (this.mVVDialog.getVisibility() != 0) {
            return false;
        }
        this.mVVDialog.setVisibility(8);
        return true;
    }

    private void initHandler() {
        if (this.mHandler == null) {
            this.mHandler = new Handler() {
                public void handleMessage(Message message) {
                    super.handleMessage(message);
                }
            };
        }
    }

    private void initTextureView() {
        this.mTextureVideoView = new TextureVideoView(getContext());
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        Rect displayRect = Util.getDisplayRect();
        layoutParams.topMargin = displayRect.top;
        layoutParams.height = displayRect.height();
        this.mPreviewLayout.removeAllViews();
        this.mPreviewLayout.setBackground(null);
        this.mPreviewLayout.addView(this.mTextureVideoView, layoutParams);
        this.mPreviewCover.setLayoutParams(layoutParams);
        this.mTextureVideoView.setLoop(true);
        this.mTextureVideoView.setClearSurface(true);
        this.mTextureVideoView.setScaleType(6);
        this.mTextureVideoView.setVisibility(4);
        this.mTextureVideoView.setMediaPlayerCallback(new MediaPlayerCallback() {
            public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
            }

            public void onCompletion(MediaPlayer mediaPlayer) {
                FragmentVVProcess.this.stopSegmentPreview(false);
            }

            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(" | ");
                sb.append(i2);
                Log.e("onError:", sb.toString());
                return false;
            }

            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                StringBuilder sb = new StringBuilder();
                sb.append(i);
                sb.append(" | ");
                sb.append(i2);
                Log.e("onInfo:", sb.toString());
                return false;
            }

            public void onPrepared(MediaPlayer mediaPlayer) {
            }

            public void onSurfaceReady(Surface surface) {
                if (FragmentVVProcess.this.mWaitingResultSurfaceTexture) {
                    FragmentVVProcess.this.mWaitingResultSurfaceTexture = false;
                    FragmentVVProcess.this.mTextureVideoView.setVideoSpecifiedSize(1920, 1080);
                    FragmentVVProcess.this.startPlay(surface);
                }
            }

            public void onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            }

            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
            }
        });
        this.mTextureVideoView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (FragmentVVProcess.this.mVVShare.getVisibility() != 0 && FragmentVVProcess.this.mVVDialog.getVisibility() != 0) {
                    FragmentVVProcess.this.pausePlay(false, true);
                }
            }
        });
    }

    private void intoResultPreview() {
        if (!this.mWaitingResultSurfaceTexture && !isFullSegmentsPlaying()) {
            Log.d(TAG, "startConcat");
            this.mVmProcessing.updateState(3);
            updateThumbnail();
            if (this.mTextureVideoView.getPreviewSurface() != null) {
                startPlay(this.mTextureVideoView.getPreviewSurface());
            } else {
                this.mWaitingResultSurfaceTexture = true;
            }
        }
    }

    private boolean isFullSegmentsPlaying() {
        return this.mVmProcessing.getCurrentState() == 6;
    }

    private boolean isRecordingFinished() {
        List<Long> list = this.mDurationList;
        if (list == null) {
            return false;
        }
        for (Long longValue : list) {
            if (longValue.longValue() <= 0) {
                return false;
            }
        }
        return true;
    }

    private void onProcessingSateChanged(int i) {
        ImageView imageView;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("newState: ");
        sb.append(i);
        Log.d(str, sb.toString());
        switch (i) {
            case 2:
                this.mPreviewStart.setVisibility(0);
                imageView = this.mPreviewThumb;
                break;
            case 3:
                animateOut(this.mSegmentPreview);
                animateOut(this.mSegmentReverse);
                AlphaOutOnSubscribe.directSetResult(this.mPreviewNext);
                animateIn(this.mPreviewBack);
                animateIn(this.mPreviewShare);
                AlphaInOnSubscribe.directSetResult(this.mPreviewCombine);
                this.mCameraSnapView.setVisibility(8);
                this.mPreviewCombine.setScale(0.38f);
                this.mPreviewCombine.O0000O0o((int) R.raw.vv_save);
                this.mPreviewCombine.O0000oO();
                animateIn(this.mConcatProgress);
                this.mPreviewLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
                this.mTextureVideoView.setVisibility(0);
                return;
            case 4:
            case 5:
                imageView = this.mPreviewStart;
                break;
            case 6:
                this.mPreviewStart.setVisibility(8);
                this.mPreviewThumb.setVisibility(8);
                break;
            case 7:
                if (this.mPendingShare) {
                    this.mPreviewShare.setVisibility(8);
                    this.mShareProgress.setVisibility(0);
                    return;
                }
                this.mPreviewStart.setVisibility(8);
                this.mPreviewCombine.setVisibility(8);
                RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, (float) (this.mCombineProgress.getLayoutParams().width / 2), (float) (this.mCombineProgress.getLayoutParams().height / 2));
                rotateAnimation.setDuration((long) getResources().getInteger(R.integer.post_process_duration));
                rotateAnimation.setInterpolator(new LinearInterpolator());
                rotateAnimation.setRepeatMode(1);
                rotateAnimation.setRepeatCount(-1);
                this.mCombineProgress.setAnimation(rotateAnimation);
                this.mCombineProgress.setVisibility(0);
                return;
            case 8:
                onResultCombineFinished(true);
                return;
            case 9:
                onResultCombineFinished(false);
                return;
            default:
                return;
        }
        imageView.setVisibility(0);
        this.mConcatProgress.setVisibility(8);
    }

    private void onSegmentsChanged() {
        int i = 0;
        for (Long longValue : this.mDurationList) {
            if (longValue.longValue() > 0) {
                i++;
            }
        }
        animateIn(this.mProgressView);
        if (i == 0) {
            animateOut(this.mSegmentReverse);
            animateOut(this.mSegmentPreview);
        } else if (i == this.mDurationList.size()) {
            animateIn(this.mSegmentReverse);
            animateIn(this.mSegmentPreview);
            animateIn(this.mPreviewNext);
            return;
        } else {
            animateIn(this.mSegmentReverse);
            animateIn(this.mSegmentPreview);
        }
        animateOut(this.mPreviewNext);
    }

    /* access modifiers changed from: private */
    public void pausePlay(boolean z, boolean z2) {
        TextureVideoView textureVideoView = this.mTextureVideoView;
        if (textureVideoView != null && textureVideoView.isPlaying()) {
            stopSegmentPreview(z2);
        } else if (!isFullSegmentsPlaying()) {
            if (z && this.mVmProcessing.getCurrentState() == 5) {
                this.mVmProcessing.updateState(2);
            }
        } else {
            VMProcessing vMProcessing = this.mVmProcessing;
            if (z) {
                vMProcessing.updateState(2);
            } else {
                vMProcessing.updateState(5);
            }
            LiveConfigVV liveConfigVV = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
            if (liveConfigVV != null) {
                if (z2) {
                    CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_ALL_PAUSE);
                }
                liveConfigVV.pausePlay();
            }
        }
    }

    private void previewLastSegment() {
        int i;
        Log.u(TAG, "previewLastSegment");
        List list = this.mDurationList;
        if (list == null || list.size() <= 0) {
            i = 0;
        } else {
            i = 0;
            for (int i2 = 0; i2 < this.mDurationList.size(); i2++) {
                if (((Long) this.mDurationList.get(i2)).longValue() > 0) {
                    i = i2;
                }
            }
        }
        this.mBottomActionView.setVisibility(4);
        this.mPreviewLayout.setBackgroundColor(ViewCompat.MEASURED_STATE_MASK);
        this.mTextureVideoView.setVisibility(0);
        LiveConfigVV liveConfigVV = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
        if (liveConfigVV != null) {
            this.mTextureVideoView.setVideoPath(liveConfigVV.getSegmentPath(i));
            this.mTextureVideoView.start();
        }
    }

    private void resumePlay() {
        if (!isFullSegmentsPlaying()) {
            if (this.mVmProcessing.getCurrentState() != 5) {
                intoResultPreview();
                return;
            }
            LiveConfigVV liveConfigVV = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
            if (liveConfigVV == null) {
                Log.w(TAG, "resumePlay failed, vv is null");
                return;
            }
            this.mVmProcessing.updateState(6);
            liveConfigVV.resumePlay();
        }
    }

    public static NullHolder rotateBitmap(Bitmap bitmap, int i) {
        if (bitmap == null) {
            return NullHolder.ofNullable(bitmap);
        }
        if (i == 0) {
            return NullHolder.ofNullable(bitmap);
        }
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate((float) i, ((float) width) / 2.0f, ((float) height) / 2.0f);
        Bitmap bitmap2 = null;
        try {
            Bitmap createBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
            if (bitmap != createBitmap) {
                bitmap.recycle();
                bitmap2 = createBitmap;
            } else {
                bitmap2 = bitmap;
            }
        } catch (OutOfMemoryError unused) {
            Log.d(TAG, "rotate out of memory");
        } catch (Exception unused2) {
        }
        return NullHolder.ofNullable(bitmap2);
    }

    private void showReverseConfirmDialog() {
        if (this.mVVDialog.getVisibility() != 0) {
            CameraStatUtils.trackLiveClick(Live.VALUE_LIVE_CLICK_REVERSE);
            this.mCheckboxLayout.setVisibility(8);
            this.mVVDialogMessage.setText(R.string.live_reverse_message);
            this.mVVDialogConfirm.setText(R.string.live_reverse_confirm);
            this.mVVDialogCancel.setText(R.string.snap_cancel);
            this.mVVDialogConfirm.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Log.u(FragmentVVProcess.TAG, "showReverseConfirmDialog onClick positive");
                    CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_REVERSE_CONFIRM);
                    ActivityBase activityBase = (ActivityBase) FragmentVVProcess.this.getContext();
                    if (activityBase == null || activityBase.getCurrentModule() == null || activityBase.getCurrentModule().getModuleIndex() != 179) {
                        Log.w(FragmentVVProcess.TAG, "onClick: delete is not allowed!!!");
                        return;
                    }
                    ((LiveModuleSubVV) activityBase.getCurrentModule()).doReverse();
                    FragmentVVProcess.this.hideVVDialog();
                }
            });
            this.mVVDialogCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Log.u(FragmentVVProcess.TAG, "showReverseConfirmDialog onClick negative");
                    FragmentVVProcess.this.hideVVDialog();
                }
            });
            this.mVVDialog.setVisibility(0);
        }
    }

    private void showShareSheet() {
        this.mPendingShare = false;
        if (!this.mPaused) {
            pausePlay(false, false);
            Intent shareMediaIntent = Util.getShareMediaIntent(getContext(), this.mSavedPath, true);
            PackageManager packageManager = getContext().getPackageManager();
            List queryIntentActivities = packageManager.queryIntentActivities(shareMediaIntent, 65536);
            if (queryIntentActivities == null || queryIntentActivities.isEmpty()) {
                Log.d(TAG, "no IntentActivities");
                return;
            }
            int i = this.mShareRecyclerView.getLayoutParams().width / 4;
            VVShareAdapter vVShareAdapter = this.mShareAdapter;
            if (vVShareAdapter == null || vVShareAdapter.getItemCount() != queryIntentActivities.size()) {
                this.mShareAdapter = new VVShareAdapter(packageManager, queryIntentActivities, this, i);
                PagerGridLayoutManager pagerGridLayoutManager = new PagerGridLayoutManager(2, 4, 1);
                pagerGridLayoutManager.setPageListener(new PageListener() {
                    public void onPageSelect(int i) {
                        FragmentVVProcess.this.mPageIndicatorView.setSelectedPage(i);
                    }

                    public void onPageSizeChanged(int i) {
                    }
                });
                int ceil = (int) Math.ceil((double) (((float) queryIntentActivities.size()) / 8.0f));
                this.mPageIndicatorView.initIndicator(ceil);
                if (ceil <= 1) {
                    this.mPageIndicatorView.setVisibility(8);
                } else {
                    this.mPageIndicatorView.setVisibility(0);
                }
                if (this.mPagerGridSnapHelper == null) {
                    this.mPagerGridSnapHelper = new PagerGridSnapHelper();
                    this.mShareRecyclerView.setLayoutManager(pagerGridLayoutManager);
                    this.mPagerGridSnapHelper.attachToRecyclerView(this.mShareRecyclerView);
                }
                this.mShareRecyclerView.setAdapter(this.mShareAdapter);
                this.mVVShareMessage.setText(R.string.snap_cancel);
                this.mVVShareMessage.setOnClickListener(new OnClickListener() {
                    public void onClick(View view) {
                        FragmentVVProcess.this.hideShareSheet();
                    }
                });
            } else {
                this.mShareAdapter.setShareInfoList(queryIntentActivities);
                this.mShareAdapter.notifyDataSetChanged();
            }
            this.mPreviewStart.setVisibility(8);
            Completable.create(new AlphaInOnSubscribe(this.mVVShare)).subscribe();
        }
    }

    /* access modifiers changed from: private */
    public void startPlay(Surface surface) {
        if (!isFullSegmentsPlaying()) {
            this.mVmProcessing.updateState(6);
            LiveConfigVV liveConfigVV = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
            if (liveConfigVV != null) {
                liveConfigVV.startPlay(surface);
            }
        }
    }

    private void startSave() {
        Log.d(TAG, "startSave");
        if (!this.mWaitingResultSurfaceTexture) {
            this.mVmProcessing.updateState(7);
            if (this.mSaveContentValues == null) {
                ((LiveModuleSubVV) ((ActivityBase) getContext()).getCurrentModule()).showPreview();
            }
            VVItem vVItem = this.mCurrentVVItem;
            if (vVItem != null) {
                CameraStatUtils.trackVVSave(vVItem.name);
            }
            final String asString = this.mSaveContentValues.getAsString("_data");
            Completable.create(new CompletableOnSubscribe() {
                public void subscribe(CompletableEmitter completableEmitter) {
                    LiveConfigVV liveConfigVV = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
                    if (liveConfigVV != null) {
                        liveConfigVV.combineVideoAudio(asString);
                    }
                    completableEmitter.onComplete();
                }
            }).subscribeOn(CameraSchedulers.sCameraSetupScheduler).subscribe();
        }
    }

    /* access modifiers changed from: private */
    public void stopSegmentPreview(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("stopSegmentPreview fromUser=");
        sb.append(z);
        Log.u(str, sb.toString());
        if (z) {
            CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_SEGMENT_EXIT);
        }
        this.mTextureVideoView.stop();
        this.mTextureVideoView.setVisibility(4);
        this.mBottomActionView.setVisibility(0);
        this.mPreviewLayout.setBackground(null);
    }

    private void updateThumbnail() {
        LiveConfigVV liveConfigVV = (LiveConfigVV) ModeCoordinatorImpl.getInstance().getAttachProtocol(228);
        String segmentPath = liveConfigVV != null ? liveConfigVV.getSegmentPath(0) : "";
        if (!TextUtils.isEmpty(segmentPath) && FileCompat.exists(segmentPath)) {
            Single.just(segmentPath).map(C0335O0000o0O.INSTANCE).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new C0336O0000o0o(this));
        }
    }

    public /* synthetic */ void O00000o0(NullHolder nullHolder) {
        if (nullHolder.isPresent()) {
            this.mPreviewThumb.setImageBitmap((Bitmap) nullHolder.get());
        }
    }

    public /* synthetic */ void O0000Oo(DataWrap dataWrap) {
        onProcessingSateChanged(((Integer) dataWrap.get()).intValue());
    }

    public boolean canSnap() {
        return true;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_VV_PROCESS;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_vv_process;
    }

    public ContentValues getSaveContentValues() {
        return this.mSaveContentValues;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mRootView = view;
        this.mPreviewLayout = (FrameLayout) view.findViewById(R.id.vv_preview_layout);
        this.mPreviewCover = (FrameLayout) view.findViewById(R.id.vv_process_video_cover);
        this.mConcatProgress = (ProgressBar) view.findViewById(R.id.vv_concat_progress);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.vv_share_progress);
        this.mCameraSnapView = (CameraSnapView) view.findViewById(R.id.vv_preview_save_circle);
        this.mCameraSnapView.setSnapListener(this);
        this.mCombineProgress = (ImageView) view.findViewById(R.id.vv_preview_operation_image);
        this.mPreviewCombine = (LottieAnimationView) view.findViewById(R.id.vv_preview_save);
        this.mPreviewBack = (ImageView) view.findViewById(R.id.vv_preview_back);
        this.mPreviewShare = (ImageView) view.findViewById(R.id.vv_preview_share);
        this.mPreviewStart = (ImageView) view.findViewById(R.id.vv_preview_play);
        this.mPreviewThumb = (ImageView) view.findViewById(R.id.vv_preview_thumb);
        this.mPreviewNext = (ImageView) view.findViewById(R.id.vv_preview_next);
        this.mSegmentReverse = (ImageView) view.findViewById(R.id.vv_segment_reverse);
        this.mSegmentPreview = (ImageView) view.findViewById(R.id.vv_segment_preview);
        this.mLandscapeHint = (TextView) view.findViewById(R.id.vv_process_hint);
        this.mStabilityHint = (TextView) view.findViewById(R.id.vv_stability_hint);
        this.mVVDialog = view.findViewById(R.id.vv_dialog);
        this.mVVDialog.setOnClickListener(this);
        this.mVVDialogMessage = (TextView) this.mVVDialog.findViewById(R.id.vv_dialog_message);
        this.mVVDialogConfirm = (TextView) this.mVVDialog.findViewById(R.id.vv_dialog_positive);
        this.mVVDialogCancel = (TextView) this.mVVDialog.findViewById(R.id.vv_dialog_negative);
        this.mCheckboxLayout = (ViewGroup) this.mVVDialog.findViewById(R.id.vv_dialog_checkbox_layout);
        this.mCheckbox = (CheckBox) this.mCheckboxLayout.findViewById(R.id.vv_dialog_checkbox);
        this.mCheckboxText = (TextView) this.mCheckboxLayout.findViewById(R.id.vv_dialog_checkbox_message);
        this.mVVShare = view.findViewById(R.id.vv_share);
        this.mVVShareMessage = (TextView) this.mVVShare.findViewById(R.id.vv_share_message);
        this.mShareRecyclerView = (RecyclerView) this.mVVShare.findViewById(R.id.vv_share_recyclerview);
        this.mShareRecyclerView.setFocusable(false);
        this.mPageIndicatorView = (PageIndicatorView) this.mVVShare.findViewById(R.id.vv_share_recyclerview_indicator);
        this.mProgressView = (VVProgressView) view.findViewById(R.id.vv_progress);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mProgressView.getLayoutParams();
        Rect displayRect = Util.getDisplayRect(1);
        int height = (displayRect.height() - marginLayoutParams.height) / 2;
        int i = displayRect.top;
        marginLayoutParams.topMargin = height + i;
        int windowHeight = (i - ((Display.getWindowHeight() - i) - displayRect.height())) / 2;
        ((MarginLayoutParams) this.mLandscapeHint.getLayoutParams()).topMargin = windowHeight;
        ((MarginLayoutParams) this.mStabilityHint.getLayoutParams()).topMargin = windowHeight;
        ((MarginLayoutParams) this.mVVDialog.getLayoutParams()).topMargin = windowHeight;
        ((MarginLayoutParams) this.mVVShare.getLayoutParams()).topMargin = windowHeight;
        ViewCompat.setRotation(this.mPreviewStart, 90.0f);
        ViewCompat.setRotation(this.mCameraSnapView, 90.0f);
        ViewCompat.setRotation(this.mPreviewCombine, 90.0f);
        ViewCompat.setRotation(this.mPreviewBack, 90.0f);
        ViewCompat.setRotation(this.mPreviewShare, 90.0f);
        ViewCompat.setRotation(this.mPreviewNext, 90.0f);
        ViewCompat.setRotation(this.mSegmentReverse, 90.0f);
        ViewCompat.setRotation(this.mSegmentPreview, 90.0f);
        ViewCompat.setRotation(this.mLandscapeHint, 90.0f);
        ViewCompat.setRotation(this.mStabilityHint, 90.0f);
        ViewCompat.setRotation(this.mVVDialog, 90.0f);
        ViewCompat.setRotation(this.mVVShare, 90.0f);
        this.mCameraSnapView.setOnClickListener(this);
        this.mPreviewCombine.setOnClickListener(this);
        this.mPreviewBack.setOnClickListener(this);
        this.mPreviewShare.setOnClickListener(this);
        this.mPreviewStart.setOnClickListener(this);
        this.mSegmentReverse.setOnClickListener(this);
        this.mSegmentPreview.setOnClickListener(this);
        this.mPreviewNext.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mPreviewBack, this.mPreviewShare, this.mPreviewStart, this.mSegmentReverse, this.mSegmentPreview, this.mPreviewNext);
        FolmeUtils.touchScale(this.mPreviewCombine);
        FolmeUtils.touchDialogButtonTint(this.mVVDialogCancel, this.mVVDialogConfirm, this.mVVDialogMessage);
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.vv_preview_bottom_action);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomActionView.getLayoutParams();
        if (Display.fitDisplayFull(1.3333333f)) {
            marginLayoutParams2.bottomMargin = Display.getNavigationBarHeight();
        }
        marginLayoutParams2.height = Display.getBottomHeight();
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.vv_preview_bottom_parent);
        MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mBottomLayout.getLayoutParams();
        marginLayoutParams3.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
        marginLayoutParams3.bottomMargin = Display.getBottomMargin();
        marginLayoutParams3.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        if (this.mCurrentMode != 179) {
            quit();
        }
        this.mVmProcessing = (VMProcessing) DataRepository.dataItemObservable().get(VMProcessing.class);
        this.mVmProcessing.startObservable(this, new O0000o0(this));
    }

    public boolean onBackEvent(int i) {
        TextureVideoView textureVideoView = this.mTextureVideoView;
        if (textureVideoView == null || !textureVideoView.isPlaying()) {
            return hideVVDialog() || hideShareSheet();
        }
        stopSegmentPreview(true);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:33:0x00a3, code lost:
        startSave();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        if (this.mConcatProgress.getVisibility() != 0 && this.mCombineProgress.getVisibility() != 0 && this.mShareProgress.getVisibility() != 0 && (view.getId() == R.id.live_share_item || (this.mVVShare.getVisibility() != 0 && this.mVVDialog.getVisibility() != 0))) {
            switch (view.getId()) {
                case R.id.live_share_item /*2131296726*/:
                    Log.u(TAG, "onClick: live_share_item");
                    hideShareSheet();
                    ResolveInfo resolveInfo = (ResolveInfo) view.getTag();
                    Util.startShareMedia(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, getContext(), this.mSavedPath, true);
                    break;
                case R.id.vv_dialog /*2131297218*/:
                    Log.u(TAG, "onClick: vv_dialog");
                    break;
                case R.id.vv_preview_back /*2131297240*/:
                    Log.u(TAG, "onClick: vv_preview_back");
                    showExitConfirm();
                    break;
                case R.id.vv_preview_next /*2131297250*/:
                    Log.u(TAG, "onClick: vv_preview_next");
                    if (isRecordingFinished()) {
                        CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_ALL);
                        intoResultPreview();
                        break;
                    } else {
                        return;
                    }
                case R.id.vv_preview_play /*2131297252*/:
                    Log.u(TAG, "onClick: vv_preview_play");
                    CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_ALL_RESUME);
                    resumePlay();
                    break;
                case R.id.vv_preview_save /*2131297253*/:
                    Log.u(TAG, "onClick: vv_preview_save");
                    CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_SAVE);
                    if (this.mSavedUri != null) {
                        quitLiveRecordPreview(true);
                        break;
                    }
                case R.id.vv_preview_share /*2131297255*/:
                    Log.u(TAG, "onClick: vv_preview_share");
                    CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_SHARE);
                    if (!checkAndShare()) {
                        this.mPendingShare = true;
                    }
                    break;
                case R.id.vv_segment_preview /*2131297261*/:
                    Log.u(TAG, "onClick: vv_segment_preview");
                    VMProcessing vMProcessing = this.mVmProcessing;
                    if (vMProcessing == null || vMProcessing.getCurrentState() == 1) {
                        CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_PLAY_SEGMENT);
                        previewLastSegment();
                        break;
                    } else {
                        return;
                    }
                    break;
                case R.id.vv_segment_reverse /*2131297262*/:
                    Log.u(TAG, "onClick: vv_segment_reverse");
                    VMProcessing vMProcessing2 = this.mVmProcessing;
                    if (vMProcessing2 == null || vMProcessing2.getCurrentState() == 1) {
                        CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_REVERSE);
                        showReverseConfirmDialog();
                        break;
                    } else {
                        return;
                    }
            }
        }
    }

    public void onCombinePrepare(ContentValues contentValues) {
        this.mSavedUri = null;
        this.mSaveContentValues = contentValues;
        initHandler();
    }

    public void onDestroy() {
        super.onDestroy();
        this.mCombineProgress.clearAnimation();
    }

    public void onKeyCodeCamera() {
        if (isAdded() && isRecordingFinished()) {
            onSnapClick();
        }
    }

    public void onLiveSaveToLocalFinished(Uri uri, String str) {
        this.mSavedUri = uri;
        this.mSavedPath = str;
        if (this.mPendingShare) {
            this.mPreviewShare.setVisibility(0);
            this.mShareProgress.setVisibility(8);
            showShareSheet();
        }
    }

    public void onPause() {
        super.onPause();
        this.mPaused = true;
        pausePlay(true, false);
        hideVVDialog();
        hideShareSheet();
    }

    public void onRecordingFragmentUpdate(int i, long j) {
        this.mVideoRecordingStarted = false;
        this.mDurationList.set(i, Long.valueOf(j));
        this.mProgressView.updateDuration(i, j);
        onSegmentsChanged();
    }

    public void onRecordingNewFragmentStart(int i, long j, VVWorkspaceItem vVWorkspaceItem) {
        this.mCurrentWorkspaceItem = vVWorkspaceItem;
        this.mCurrentIndex = i;
    }

    public void onResultCombineFinished(boolean z) {
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("combineFinished ");
        sb.append(z);
        Log.d(str, sb.toString());
        if (!this.mPendingShare || !z) {
            Log.d(TAG, "combineFinished and finish");
            this.mHandler.post(new Runnable() {
                public void run() {
                    FragmentVVProcess.this.quitLiveRecordPreview(true);
                }
            });
            return;
        }
        Log.d(TAG, "combineFinished and share");
        ActivityBase activityBase = (ActivityBase) getContext();
        if (activityBase == null || activityBase.getCurrentModule() == null || activityBase.getCurrentModule().getModuleIndex() != 179) {
            Log.w(TAG, "combineSuccess and share is not allowed!!!");
        } else {
            ((LiveModuleSubVV) activityBase.getCurrentModule()).startSaveToLocal();
        }
    }

    public void onResultPreviewFinished(boolean z) {
        this.mHandler.post(new Runnable() {
            public void run() {
                FragmentVVProcess.this.mPreviewStart.setVisibility(0);
                FragmentVVProcess.this.mPreviewThumb.setVisibility(0);
            }
        });
    }

    public void onResume() {
        super.onResume();
        this.mPaused = false;
    }

    public void onSnapClick() {
        ImageView imageView;
        if (!(this.mConcatProgress.getVisibility() == 0 || this.mCombineProgress.getVisibility() == 0 || this.mShareProgress.getVisibility() == 0)) {
            if (this.mPreviewNext.getVisibility() == 0) {
                imageView = this.mPreviewNext;
            } else if (this.mPreviewCombine.getVisibility() == 0) {
                imageView = this.mPreviewCombine;
            } else {
                Camera camera = (Camera) getContext();
                if (camera != null) {
                    Module currentModule = camera.getCurrentModule();
                    if (currentModule == null || !currentModule.isIgnoreTouchEvent() || currentModule.isFrameAvailable()) {
                        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                        if (cameraAction != null && this.mCurrentMode == 179 && !this.mVideoRecordingStarted) {
                            CameraStatUtils.trackVVClick(VLogAttr.VALUE_VV_CLICK_START_SEGMENT);
                            cameraAction.onShutterButtonClick(10);
                        } else {
                            return;
                        }
                    } else {
                        return;
                    }
                } else {
                    return;
                }
            }
            imageView.performClick();
        }
    }

    public void onSnapLongPress() {
    }

    public void onSnapLongPressCancelIn() {
    }

    public void onSnapLongPressCancelOut() {
    }

    public void onSnapPrepare() {
    }

    public void onTrackSnapMissTaken(long j) {
    }

    public void onTrackSnapTaken(long j) {
    }

    public void prepare(VVItem vVItem, VVWorkspaceItem vVWorkspaceItem) {
        this.mCurrentVVItem = vVItem;
        this.mCurrentWorkspaceItem = vVWorkspaceItem;
        this.mIsIntentAction = DataRepository.dataItemGlobal().isIntentAction();
        this.mRootView.setVisibility(0);
        this.mCurrentMode = 179;
        this.mCameraSnapView.setVisibility(0);
        this.mCameraSnapView.setParameters(PaintConditionReferred.create(this.mCurrentMode));
        this.mCameraSnapView.showPaintCenterVVItem();
        this.mCameraSnapView.hideRoundPaintItem();
        this.mConcatProgress.setVisibility(8);
        this.mCombineProgress.setVisibility(8);
        this.mShareProgress.setVisibility(8);
        this.mPreviewCombine.setVisibility(8);
        this.mPreviewBack.setVisibility(8);
        this.mPreviewShare.setVisibility(8);
        this.mPreviewStart.setVisibility(8);
        this.mPreviewThumb.setVisibility(8);
        this.mPreviewNext.setVisibility(8);
        this.mSegmentReverse.setVisibility(8);
        this.mSegmentPreview.setVisibility(8);
        this.mLandscapeHint.setVisibility(0);
        this.mStabilityHint.setVisibility(8);
        this.mDurationList = new ArrayList(vVItem.durationList.size());
        int completeSize = vVWorkspaceItem == null ? 0 : vVWorkspaceItem.completeSize();
        for (int i = 0; i < vVItem.durationList.size(); i++) {
            long longValue = ((Long) vVItem.durationList.get(i)).longValue();
            List list = this.mDurationList;
            if (i >= completeSize) {
                longValue = -longValue;
            }
            list.add(Long.valueOf(longValue));
        }
        if (completeSize > 0) {
            this.mLandscapeHint.setVisibility(8);
            onSegmentsChanged();
        }
        this.mProgressView.setDurationList(this.mDurationList);
        AlphaInOnSubscribe.directSetResult(this.mProgressView);
        initTextureView();
    }

    public void processingFinish() {
        this.mVideoRecordingStarted = false;
        if (Util.isAccessible()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_button));
        }
    }

    public void processingPause() {
        this.mStabilityHint.setVisibility(8);
        this.mCameraSnapView.triggerAnimation(BottomAnimationConfig.generate(false, this.mCurrentMode, false, false, false).configVariables());
        if (Util.isAccessible()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_button));
        }
    }

    public void processingPrepare() {
        this.mVideoRecordingStarted = true;
        this.mCameraSnapView.prepareRecording(BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables());
    }

    public void processingResume() {
        long j;
        animateOut(this.mSegmentReverse);
        animateOut(this.mSegmentPreview);
        animateOut(this.mProgressView);
        this.mLandscapeHint.setVisibility(8);
        BottomAnimationConfig configVariables = BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables();
        Iterator it = this.mDurationList.iterator();
        while (true) {
            j = 0;
            if (!it.hasNext()) {
                break;
            }
            long longValue = ((Long) it.next()).longValue();
            if (longValue < 0) {
                j = Math.abs(longValue);
                break;
            }
        }
        configVariables.setSpecifiedDuration((int) j);
        this.mCameraSnapView.triggerAnimation(configVariables);
        if (Util.isAccessible()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_process_button));
        }
    }

    public void processingStart() {
        processingResume();
    }

    public void quit() {
        this.mRootView.setVisibility(8);
    }

    public void quitLiveRecordPreview(boolean z) {
        String str;
        String str2;
        String str3;
        String str4;
        if (this.mConcatProgress.getVisibility() == 0) {
            this.mConcatProgress.setVisibility(8);
        }
        if (this.mCombineProgress.getVisibility() == 0) {
            this.mCombineProgress.setVisibility(8);
            this.mCombineProgress.clearAnimation();
        }
        if (this.mShareProgress.getVisibility() == 0) {
            this.mShareProgress.setVisibility(8);
        }
        pausePlay(false, false);
        this.mVmProcessing.reset();
        if (this.mCurrentWorkspaceItem != null) {
            if (this.mVVDialog.getVisibility() == 0 && this.mCheckboxLayout.getVisibility() == 0) {
                if (this.mCheckbox.isChecked()) {
                    if (this.mCurrentWorkspaceItem.getFragments().size() > 0) {
                        Log.d(TAG, "checked: confirm");
                        this.mCurrentWorkspaceItem.confirmOperation();
                    } else {
                        str2 = TAG;
                        str = "checked: remove";
                    }
                } else if (this.mCurrentWorkspaceItem.getLastSize() > 0) {
                    str4 = TAG;
                    str3 = "uncheck: undo";
                } else {
                    str2 = TAG;
                    str = "uncheck: remove";
                }
                Log.d(str2, str);
                this.mCurrentWorkspaceItem.removeSelf();
            } else if (this.mCurrentWorkspaceItem.getLastSize() <= 0 || z) {
                str2 = TAG;
                str = "finish: remove";
                Log.d(str2, str);
                this.mCurrentWorkspaceItem.removeSelf();
            } else {
                str4 = TAG;
                str3 = "finish: undo";
            }
            Log.d(str4, str3);
            this.mCurrentWorkspaceItem.undo();
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null) {
            Log.d(TAG, "concat error, action null");
            return;
        }
        if (z) {
            cameraAction.onReviewDoneClicked();
        } else {
            cameraAction.onReviewCancelClicked();
        }
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(230, this);
        registerBackStack(modeCoordinator, this);
    }

    public void showExitConfirm() {
        if (this.mVVDialog.getVisibility() != 0) {
            boolean z = false;
            int i = 0;
            for (Long longValue : this.mDurationList) {
                if (longValue.longValue() > 0) {
                    i++;
                }
            }
            VVWorkspaceItem vVWorkspaceItem = this.mCurrentWorkspaceItem;
            int lastSize = vVWorkspaceItem == null ? 0 : vVWorkspaceItem.getLastSize();
            final boolean z2 = lastSize > 0;
            final boolean z3 = this.mPreviewBack.getVisibility() == 0;
            if (!(i == 0 && lastSize == 0) && !z3) {
                this.mCheckboxLayout.setVisibility(0);
                this.mCheckbox.setChecked(true);
                this.mCheckboxText.setText(R.string.live_edit_exit_message_draft);
            } else {
                this.mCheckboxLayout.setVisibility(8);
                this.mCheckbox.setChecked(false);
            }
            this.mCheckbox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
                public void onCheckedChanged(CompoundButton compoundButton, boolean z) {
                    String access$000 = FragmentVVProcess.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("showExitConfirm onCheckedChanged ");
                    sb.append(z);
                    Log.u(access$000, sb.toString());
                }
            });
            this.mVVDialogMessage.setText(R.string.live_edit_exit_message);
            this.mVVDialogConfirm.setText(R.string.live_edit_exit_confirm);
            this.mVVDialogCancel.setText(R.string.snap_cancel);
            this.mVVDialogConfirm.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    String access$000 = FragmentVVProcess.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("showExitConfirm onClick positive, isChecked=");
                    sb.append(FragmentVVProcess.this.mCheckbox.isChecked());
                    Log.u(access$000, sb.toString());
                    CameraStatUtils.trackVV2ExitConfirm(z2, z3, FragmentVVProcess.this.mCheckboxLayout.getVisibility() == 0, FragmentVVProcess.this.mCheckbox.isChecked());
                    FragmentVVProcess.this.quitLiveRecordPreview(false);
                    FragmentVVProcess.this.hideVVDialog();
                }
            });
            this.mVVDialogCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    Log.u(FragmentVVProcess.TAG, "showExitConfirm onClick negative");
                    FragmentVVProcess.this.hideVVDialog();
                }
            });
            if (this.mCheckboxLayout.getVisibility() == 0) {
                z = true;
            }
            CameraStatUtils.trackVV2Exit(z2, z3, z);
            Completable.create(new AlphaInOnSubscribe(this.mVVDialog)).subscribe();
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(230, this);
        unRegisterBackStack(modeCoordinator, this);
    }

    public void updateRecordingTime(String str) {
        this.mCameraSnapView.setDurationText(str);
    }
}
