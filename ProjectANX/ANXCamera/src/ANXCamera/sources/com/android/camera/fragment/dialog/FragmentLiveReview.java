package com.android.camera.fragment.dialog;

import android.app.AlertDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;
import android.content.pm.ResolveInfo;
import android.graphics.Bitmap;
import android.graphics.Rect;
import android.graphics.SurfaceTexture;
import android.net.Uri;
import android.view.TextureView;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.FrameLayout;
import android.widget.FrameLayout.LayoutParams;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Space;
import android.widget.TextView;
import androidx.annotation.CallSuper;
import androidx.recyclerview.widget.RecyclerView;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.ActivityBase;
import com.android.camera.Camera;
import com.android.camera.CameraIntentManager;
import com.android.camera.Display;
import com.android.camera.R;
import com.android.camera.RotateDialogController;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.type.SlideInOnSubscribe;
import com.android.camera.animation.type.SlideOutOnSubscribe;
import com.android.camera.constant.ShareConstant;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.RecyclerAdapterWrapper;
import com.android.camera.fragment.beauty.LinearLayoutManagerWrapper;
import com.android.camera.fragment.fullscreen.ShareAdapter;
import com.android.camera.fragment.fullscreen.ShareInfo;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.ILive;
import com.android.camera.module.impl.component.ILive.ILivePlayerStateListener;
import com.android.camera.module.impl.component.MiLivePlayer;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MiLivePlayerControl;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.MiLive;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import miui.view.animation.QuarticEaseInInterpolator;
import miui.view.animation.QuarticEaseOutInterpolator;

public class FragmentLiveReview extends BaseFragment implements MiLivePlayerControl, HandleBackTrace, OnClickListener {
    public static final int FRAGMENT_INFO = 1048561;
    private static final int STATE_IDLE = 0;
    private static final int STATE_PAUSED = 4;
    private static final int STATE_PAUSE_AFTER_PLAY = 10;
    private static final int STATE_PENDING_PLAY = 2;
    private static final int STATE_PENDING_RESUME = 5;
    private static final int STATE_PENDING_SAVE = 8;
    private static final int STATE_PENDING_SHARE = 6;
    private static final int STATE_PLAYING = 3;
    private static final int STATE_SAVE = 9;
    private static final int STATE_SHARE = 7;
    private static final int STATE_WAIT_SURFACE_CREATE = 1;
    public final String TAG;
    /* access modifiers changed from: private */
    public String mAudioString;
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    private CameraSnapView mCameraSnapView;
    private ProgressBar mCancelProgress;
    /* access modifiers changed from: private */
    public int mComposeState;
    private Consumer mComposeStateConsumer = new O0000O0o(this);
    private Disposable mComposeStateDisposable;
    /* access modifiers changed from: private */
    public ObservableEmitter mComposeStateEmitter;
    /* access modifiers changed from: private */
    public AlertDialog mExitDialog;
    /* access modifiers changed from: private */
    public Bitmap mFirstFrame;
    private boolean mPaused;
    private Consumer mPlayStateConsumer = new C0298O00000oO(this);
    private Disposable mPlayStateDisposable;
    /* access modifiers changed from: private */
    public ObservableEmitter mPlayStateEmitter;
    /* access modifiers changed from: private */
    public MiLivePlayer mPlayer;
    /* access modifiers changed from: private */
    public int mPlayerState;
    private ImageView mPreviewBack;
    private LottieAnimationView mPreviewCombine;
    /* access modifiers changed from: private */
    public ImageView mPreviewCover;
    private FrameLayout mPreviewLayout;
    private ImageView mPreviewShare;
    private ImageView mPreviewStart;
    private ProgressBar mPreviewStartProgress;
    private View mRootView;
    private ContentValues mSaveContentValues;
    private String mSavePath;
    private ImageView mSaveProgress;
    private boolean mSaved;
    private Uri mSavedUri;
    /* access modifiers changed from: private */
    public List mSegmentData;
    private ShareAdapter mShareAdapter;
    private View mShareCancel;
    private ViewGroup mShareLayout;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    private ILivePlayerStateListener mStateListener = new ILivePlayerStateListener() {
        public void onComposeStateChange(int i) {
            if (FragmentLiveReview.this.mComposeState != i) {
                FragmentLiveReview.this.mComposeState = i;
            }
            if (FragmentLiveReview.this.mComposeStateEmitter != null) {
                FragmentLiveReview.this.mComposeStateEmitter.onNext(Integer.valueOf(i));
            }
        }

        public void onPlayStateChange(int i) {
            if (FragmentLiveReview.this.mPlayerState != i) {
                FragmentLiveReview.this.mPlayerState = i;
            }
            if (FragmentLiveReview.this.mPlayStateEmitter != null) {
                FragmentLiveReview.this.mPlayStateEmitter.onNext(Integer.valueOf(i));
            }
        }
    };
    /* access modifiers changed from: private */
    public TextureView mTextureView;
    private TextView mTimeView;
    private ViewGroup mTopLayout;
    private Consumer mUIStateConsumer = new C0299O00000oo(this);
    private Disposable mUIStateDisposable;
    private ObservableEmitter mUIStateEmitter;
    /* access modifiers changed from: private */
    public int mUiState = 0;
    /* access modifiers changed from: private */
    public int mVideoHeight;
    /* access modifiers changed from: private */
    public int mVideoWidth;

    public FragmentLiveReview() {
        StringBuilder sb = new StringBuilder();
        sb.append("LiveReview@");
        sb.append(hashCode());
        this.TAG = sb.toString();
    }

    private boolean checkAndShare() {
        if (this.mSavedUri == null) {
            return false;
        }
        shareMore();
        return true;
    }

    private String getCurStateString() {
        return getStateString(this.mUiState);
    }

    private String getStateString(int i) {
        switch (i) {
            case 0:
                return "STATE_IDLE";
            case 1:
                return "STATE_WAIT_SURFACE_CREATE";
            case 2:
                return "STATE_PENDING_PLAY";
            case 3:
                return "STATE_PLAYING";
            case 4:
                return "STATE_PAUSED";
            case 5:
                return "STATE_PENDING_RESUME";
            case 6:
                return "STATE_PENDING_SHARE";
            case 7:
                return "STATE_SHARE";
            case 8:
                return "STATE_PENDING_SAVE";
            case 9:
                return "STATE_SAVE";
            case 10:
                return "STATE_PAUSE_AFTER_PLAY";
            default:
                return "STATE_UNKNOWN";
        }
    }

    /* access modifiers changed from: private */
    public boolean hasFrame() {
        Bitmap bitmap = this.mFirstFrame;
        return bitmap != null && !bitmap.isRecycled();
    }

    private boolean hideShareSheet() {
        if (this.mShareLayout.getVisibility() != 0) {
            return false;
        }
        Completable.create(new SlideOutOnSubscribe(this.mShareLayout, 80).setInterpolator(new QuarticEaseInInterpolator()).setDurationTime(200)).subscribe();
        return true;
    }

    /* access modifiers changed from: private */
    public void initPlayer() {
        Log.d(this.TAG, "initPlayer");
        if (this.mPlayer == null) {
            this.mSavePath = null;
            this.mUIStateDisposable = Observable.create(new O00000o0(this)).observeOn(AndroidSchedulers.mainThread()).subscribe(this.mUIStateConsumer);
            this.mPlayStateDisposable = Observable.create(new O0000Oo0(this)).observeOn(AndroidSchedulers.mainThread()).subscribe(this.mPlayStateConsumer);
            this.mComposeStateDisposable = Observable.create(new O0000OOo(this)).observeOn(AndroidSchedulers.mainThread()).subscribe(this.mComposeStateConsumer);
            this.mPlayer = new MiLivePlayer(getActivity());
            this.mPlayer.setStateListener(this.mStateListener);
        }
    }

    private void initTextureView() {
        this.mTextureView = new TextureView(getContext());
        this.mPreviewCover = new ImageView(getContext());
        this.mPreviewCover.setBackground(null);
        this.mPreviewCover.setVisibility(8);
        LayoutParams layoutParams = new LayoutParams(-1, -1);
        Rect displayRect = Util.getDisplayRect();
        layoutParams.topMargin = displayRect.top;
        layoutParams.height = displayRect.height();
        this.mPreviewLayout.removeAllViews();
        this.mPreviewLayout.setBackground(null);
        this.mPreviewLayout.addView(this.mTextureView, layoutParams);
        this.mPreviewLayout.addView(this.mPreviewCover, layoutParams);
        this.mTextureView.setSurfaceTextureListener(new SurfaceTextureListener() {
            public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
                String str = FragmentLiveReview.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onSurfaceTextureAvailable : ");
                sb.append(surfaceTexture);
                sb.append(", size = ");
                sb.append(i);
                sb.append("x");
                sb.append(i2);
                Log.d(str, sb.toString());
                FragmentLiveReview.this.releaseCover();
                FragmentLiveReview.this.initPlayer();
                if (FragmentLiveReview.this.mUiState == 1) {
                    FragmentLiveReview.this.mPlayer.init(FragmentLiveReview.this.mVideoWidth, FragmentLiveReview.this.mVideoHeight, i, i2, FragmentLiveReview.this.mSegmentData, FragmentLiveReview.this.mAudioString);
                    FragmentLiveReview.this.startPlay(surfaceTexture);
                }
            }

            public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
                String str = FragmentLiveReview.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onSurfaceTextureDestroyed : ");
                sb.append(surfaceTexture);
                Log.d(str, sb.toString());
                FragmentLiveReview.this.release();
                return false;
            }

            public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
                String str = FragmentLiveReview.this.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("onSurfaceTextureAvailable : ");
                sb.append(surfaceTexture);
                sb.append(", size = ");
                sb.append(i);
                sb.append("x");
                sb.append(i2);
                Log.d(str, sb.toString());
            }

            public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
                if (!FragmentLiveReview.this.hasFrame()) {
                    Log.d(FragmentLiveReview.this.TAG, "capture cover.");
                    FragmentLiveReview fragmentLiveReview = FragmentLiveReview.this;
                    fragmentLiveReview.mFirstFrame = fragmentLiveReview.mTextureView.getBitmap();
                    FragmentLiveReview.this.mPreviewCover.setImageBitmap(FragmentLiveReview.this.mFirstFrame);
                }
                if (FragmentLiveReview.this.mUiState == 2 || FragmentLiveReview.this.mUiState == 5) {
                    FragmentLiveReview.this.setUiState(3);
                } else if (FragmentLiveReview.this.mUiState == 10) {
                    FragmentLiveReview.this.pausePlay(false, false);
                }
            }
        });
    }

    private boolean isComposing() {
        int i = this.mUiState;
        return i == 8 || i == 9 || i == 6 || i == 7;
    }

    /* access modifiers changed from: private */
    public void releaseCover() {
        if (hasFrame()) {
            this.mFirstFrame.recycle();
            this.mFirstFrame = null;
        }
    }

    /* access modifiers changed from: private */
    public void setUiState(int i) {
        if (this.mUiState != i) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("ui state change from ");
            sb.append(getCurStateString());
            sb.append(" to ");
            sb.append(getStateString(i));
            Log.d(str, sb.toString());
            this.mUiState = i;
            ObservableEmitter observableEmitter = this.mUIStateEmitter;
            if (observableEmitter != null) {
                observableEmitter.onNext(Integer.valueOf(this.mUiState));
            }
        }
    }

    private void shareMore() {
        Util.shareMediaToMore(getContext(), this.mSavePath, getString(R.string.live_edit_share_title), true);
    }

    private boolean showCover() {
        if (this.mPreviewCover == null || !hasFrame()) {
            return false;
        }
        this.mPreviewCover.setVisibility(0);
        return true;
    }

    private void showExitConfirm() {
        if (this.mExitDialog == null) {
            int i = this.mUiState;
            if (i == 3 || i == 4) {
                this.mExitDialog = RotateDialogController.showSystemAlertDialog(getContext(), null, getString(R.string.live_edit_exit_message), getString(R.string.live_edit_exit_confirm), new Runnable() {
                    public void run() {
                        Log.u(FragmentLiveReview.this.TAG, "showExitConfirm onClick positive");
                        FragmentLiveReview.this.mExitDialog.dismiss();
                        CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_CANCEL);
                        FragmentLiveReview.this.quitLiveRecordPreview(false);
                    }
                }, null, null, getString(R.string.snap_cancel), new O00000Oo(this));
                this.mExitDialog.setOnDismissListener(new OnDismissListener() {
                    public void onDismiss(DialogInterface dialogInterface) {
                        FragmentLiveReview.this.mExitDialog = null;
                    }
                });
            }
        }
    }

    /* JADX WARNING: Removed duplicated region for block: B:29:0x00a9  */
    /* JADX WARNING: Removed duplicated region for block: B:31:0x00ad  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void showShareSheet() {
        boolean z;
        String str;
        String str2;
        if (!this.mPaused) {
            pausePlay(false, false);
            setUiState(4);
            List<ResolveInfo> queryIntentActivities = getContext().getPackageManager().queryIntentActivities(Util.getShareMediaIntent(getContext(), this.mSavePath, true), 65536);
            if (queryIntentActivities.isEmpty()) {
                Log.d(this.TAG, "no IntentActivities");
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
                str = this.TAG;
                str2 = "no default share entry";
            } else if (((ShareInfo) arrayList.get(0)).index <= 1 || arrayList.size() >= 2) {
                z = false;
                if (!z) {
                    shareMore();
                    return;
                }
                ShareInfo shareInfo2 = new ShareInfo(length + 1, CameraIntentManager.CALLER_MIUI_CAMERA, "more", R.drawable.ic_live_share_more, R.string.accessibility_more);
                arrayList.add(shareInfo2);
                Collections.sort(arrayList, O00000o.INSTANCE);
                int dimensionPixelSize = getResources().getDimensionPixelSize(R.dimen.live_share_item_margin);
                int max = Math.max((Display.getWindowWidth() - (dimensionPixelSize * 2)) / arrayList.size(), (int) (((float) (Display.getWindowWidth() - dimensionPixelSize)) / 5.5f));
                ShareAdapter shareAdapter = this.mShareAdapter;
                if (shareAdapter == null || shareAdapter.getItemCount() != arrayList.size()) {
                    this.mShareAdapter = new ShareAdapter(arrayList, this, max);
                    LinearLayoutManagerWrapper linearLayoutManagerWrapper = new LinearLayoutManagerWrapper(getContext(), "FullScreen");
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
                str = this.TAG;
                str2 = "not match default share strategy";
            }
            Log.d(str, str2);
            z = true;
            if (!z) {
            }
        }
    }

    private void showTime(List list) {
        if (list != null && !list.isEmpty()) {
            long totalDuration = ILive.getTotalDuration(list);
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("showTime = ");
            sb.append(totalDuration);
            Log.d(str, sb.toString());
            this.mTimeView.setText(Util.millisecondToTimeString(Util.clamp(totalDuration, 1000, 15000), false, false));
            this.mTimeView.setVisibility(0);
        }
    }

    private void startSave(boolean z) {
        int i = this.mUiState;
        if (i != 3 && i != 4) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("skip save, caz ui state is ");
            sb.append(getCurStateString());
            Log.d(str, sb.toString());
        } else if (this.mComposeState != 1) {
            String str2 = this.TAG;
            StringBuilder sb2 = new StringBuilder();
            sb2.append("skip save, caz composer state is ");
            sb2.append(this.mComposeState);
            Log.d(str2, sb2.toString());
        } else {
            setUiState(z ? 6 : 8);
            Log.d(this.TAG, "startSave");
            combineVideoAudio();
        }
    }

    private void startSaveToLocal() {
        if (!this.mSaved && this.mSavePath != null) {
            this.mSaved = true;
            this.mSaveContentValues.put("datetaken", Long.valueOf(System.currentTimeMillis()));
            if (getActivity() instanceof Camera) {
                ((Camera) getActivity()).getImageSaver().addVideo(this.mSavePath, this.mSaveContentValues, true);
            } else {
                Log.e(this.TAG, "can't add to db.");
            }
        }
    }

    public /* synthetic */ void O000000o(ObservableEmitter observableEmitter) {
        this.mUIStateEmitter = observableEmitter;
    }

    public /* synthetic */ void O000000o(Integer num) {
        ProgressBar progressBar;
        ProgressBar progressBar2;
        Disposable disposable = this.mUIStateDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("UI state consumer get state : ");
            sb.append(num);
            Log.d(str, sb.toString());
            ActivityBase activityBase = (ActivityBase) getContext();
            int intValue = num.intValue();
            if (intValue != 0) {
                if (intValue == 1 || intValue == 2) {
                    if (activityBase.startFromKeyguard()) {
                        this.mPreviewShare.setVisibility(8);
                    } else {
                        this.mPreviewShare.setVisibility(0);
                    }
                    this.mCameraSnapView.setVisibility(8);
                    this.mPreviewCombine.setVisibility(0);
                    this.mPreviewBack.setVisibility(0);
                    showTime(this.mSegmentData);
                    this.mPreviewStart.setVisibility(8);
                    this.mSaveProgress.clearAnimation();
                    this.mSaveProgress.setVisibility(8);
                    this.mShareProgress.setVisibility(8);
                    this.mCancelProgress.setVisibility(8);
                    this.mShareLayout.setVisibility(4);
                    progressBar = this.mPreviewStartProgress;
                } else {
                    if (intValue == 3) {
                        this.mPreviewCover.setVisibility(8);
                        this.mPreviewStart.setVisibility(8);
                        progressBar2 = this.mPreviewStartProgress;
                    } else if (intValue == 4) {
                        this.mPreviewStart.setVisibility(0);
                        this.mPreviewStartProgress.setVisibility(8);
                        this.mCameraSnapView.setVisibility(8);
                        this.mPreviewCombine.setVisibility(0);
                        this.mSaveProgress.clearAnimation();
                        this.mSaveProgress.setVisibility(8);
                        if (activityBase.startFromKeyguard()) {
                            this.mPreviewShare.setVisibility(8);
                        } else {
                            this.mPreviewShare.setVisibility(0);
                        }
                        progressBar2 = this.mShareProgress;
                    } else if (intValue == 7) {
                        this.mPreviewStart.setVisibility(0);
                        this.mPreviewStartProgress.setVisibility(8);
                        this.mPreviewShare.setVisibility(8);
                        progressBar = this.mShareProgress;
                    } else if (intValue == 9) {
                        this.mPreviewStart.setVisibility(8);
                        this.mPreviewStartProgress.setVisibility(8);
                        this.mCameraSnapView.setVisibility(8);
                        this.mPreviewCombine.setVisibility(8);
                        RotateAnimation rotateAnimation = new RotateAnimation(0.0f, 360.0f, (float) (this.mSaveProgress.getLayoutParams().width / 2), (float) (this.mSaveProgress.getLayoutParams().height / 2));
                        rotateAnimation.setDuration((long) getResources().getInteger(R.integer.post_process_duration));
                        rotateAnimation.setInterpolator(new LinearInterpolator());
                        rotateAnimation.setRepeatMode(1);
                        rotateAnimation.setRepeatCount(-1);
                        this.mSaveProgress.setAnimation(rotateAnimation);
                        this.mSaveProgress.setVisibility(0);
                        return;
                    } else {
                        return;
                    }
                    progressBar2.setVisibility(8);
                    return;
                }
                progressBar.setVisibility(0);
            }
        }
    }

    public /* synthetic */ void O00000Oo(ObservableEmitter observableEmitter) {
        this.mPlayStateEmitter = observableEmitter;
    }

    public /* synthetic */ void O00000Oo(Integer num) {
        Disposable disposable = this.mPlayStateDisposable;
        if (disposable == null || disposable.isDisposed()) {
        }
    }

    public /* synthetic */ void O00000o0(ObservableEmitter observableEmitter) {
        this.mComposeStateEmitter = observableEmitter;
    }

    public /* synthetic */ void O00000o0(Integer num) {
        Disposable disposable = this.mComposeStateDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            int intValue = num.intValue();
            if (intValue != -1) {
                if (intValue == 2) {
                    int i = this.mUiState;
                    if (i == 6) {
                        setUiState(7);
                        return;
                    } else if (i == 8) {
                        setUiState(9);
                        return;
                    } else {
                        return;
                    }
                } else if (intValue == 3) {
                    this.mSavePath = this.mSaveContentValues.getAsString("_data");
                    int i2 = this.mUiState;
                    if (i2 == 9) {
                        startSaveToLocal();
                        quitLiveRecordPreview(true);
                        return;
                    } else if (i2 == 7) {
                        startSaveToLocal();
                        return;
                    } else {
                        String str = this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("onComposed error, state : ");
                        sb.append(getCurStateString());
                        Log.d(str, sb.toString());
                    }
                } else {
                    return;
                }
            }
            setUiState(4);
        }
    }

    public /* synthetic */ void O000oOO() {
        Log.u(this.TAG, "showExitConfirm onClick negative");
    }

    public void attemptStartPlay() {
        if (this.mPlayerState == 1) {
            if (this.mTextureView.getSurfaceTexture() == null || this.mPlayer == null) {
                initTextureView();
            } else {
                startPlay(this.mTextureView.getSurfaceTexture());
            }
        }
    }

    public void combineVideoAudio() {
        ContentValues contentValues = this.mSaveContentValues;
        if (contentValues != null && this.mSavePath == null) {
            String asString = contentValues.getAsString("_data");
            MiLivePlayer miLivePlayer = this.mPlayer;
            if (miLivePlayer != null) {
                miLivePlayer.stopPlayer();
                this.mPlayer.startCompose(asString);
            }
        }
    }

    public int getFragmentInto() {
        return 1048561;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_live_record_preview;
    }

    public ContentValues getSaveContentValues() {
        return this.mSaveContentValues;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Log.d(this.TAG, "initView");
        this.mRootView = view;
        this.mTopLayout = (ViewGroup) view.findViewById(R.id.live_preview_top);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mTopLayout.getLayoutParams();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.height = Display.getTopBarHeight();
        this.mPreviewLayout = (FrameLayout) view.findViewById(R.id.live_preview_layout);
        this.mPreviewStartProgress = (ProgressBar) view.findViewById(R.id.live_concat_progress);
        this.mPreviewStartProgress.setVisibility(0);
        this.mSaveProgress = (ImageView) view.findViewById(R.id.live_save_progress);
        this.mSaveProgress.setVisibility(8);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.live_share_progress);
        this.mShareProgress.setVisibility(8);
        this.mCancelProgress = (ProgressBar) view.findViewById(R.id.live_back_progress);
        this.mCancelProgress.setVisibility(8);
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
        this.mPreviewStart.setVisibility(8);
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
        FolmeUtils.touchScaleTint(this.mPreviewBack, this.mPreviewShare, this.mPreviewStart);
        FolmeUtils.touchScale(this.mPreviewCombine);
        if (((Camera) getActivity()).startFromKeyguard()) {
            this.mPreviewShare.setVisibility(8);
        } else {
            this.mPreviewShare.setVisibility(0);
        }
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.live_preview_bottom_action);
        ((MarginLayoutParams) this.mBottomActionView.getLayoutParams()).height = Display.getBottomHeight();
        this.mBottomActionView.setOnClickListener(this);
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.live_preview_bottom_parent);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomLayout.getLayoutParams();
        marginLayoutParams2.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
        marginLayoutParams2.bottomMargin = Display.getBottomMargin();
        marginLayoutParams2.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        ((MarginLayoutParams) this.mTimeView.getLayoutParams()).height = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        this.mBottomActionView.setBackgroundResource(R.color.fullscreen_background);
        this.mRootView.setVisibility(8);
    }

    public boolean isShowing() {
        return this.mUiState != 0;
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("notifyAfterFrameAvailable ui state : ");
        sb.append(getCurStateString());
        Log.d(str, sb.toString());
        if (!showCover() && this.mPlayerState == 1) {
            attemptStartPlay();
            setUiState(10);
        }
    }

    public boolean onBackEvent(int i) {
        if (this.mUiState == 0) {
            return false;
        }
        if (hideShareSheet()) {
            return true;
        }
        showExitConfirm();
        return true;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.live_preview_back /*2131296712*/:
                Log.u(this.TAG, "onClick: live_preview_back");
                showExitConfirm();
                break;
            case R.id.live_preview_bottom_action /*2131296713*/:
                Log.u(this.TAG, "onClick: live_preview_bottom_action");
                break;
            case R.id.live_preview_layout /*2131296715*/:
                if (this.mUiState == 3) {
                    Log.u(this.TAG, "onClick: live_preview_layout");
                    pausePlay(false, true);
                    break;
                } else {
                    return;
                }
            case R.id.live_preview_play /*2131296716*/:
                if (this.mUiState == 4) {
                    Log.u(this.TAG, "onClick: live_preview_play");
                    hideShareSheet();
                    int i = this.mPlayerState;
                    if (i != 1) {
                        if (i != 4) {
                            Log.d(this.TAG, "skip start play~");
                            break;
                        } else {
                            resumePlay();
                            break;
                        }
                    } else {
                        attemptStartPlay();
                        break;
                    }
                } else {
                    return;
                }
            case R.id.live_preview_save /*2131296718*/:
            case R.id.live_preview_save_circle /*2131296719*/:
                Log.u(this.TAG, "onClick: live_preview_save");
                int i2 = this.mUiState;
                if (i2 == 4 || i2 == 3) {
                    CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_DONE);
                    if (this.mSavedUri == null) {
                        if (this.mSavePath == null) {
                            startSave(false);
                            break;
                        } else {
                            startSaveToLocal();
                        }
                    }
                    quitLiveRecordPreview(true);
                    break;
                } else {
                    return;
                }
            case R.id.live_preview_share /*2131296720*/:
                int i3 = this.mUiState;
                if (i3 == 4 || i3 == 3) {
                    Log.u(this.TAG, "onClick: live_preview_share");
                    CameraStatUtils.trackMiLiveClick(MiLive.VALUE_MI_LIVE_CLICK_SHARE);
                    if (this.mSavePath == null || this.mSavedUri != null) {
                        if (!checkAndShare()) {
                            startSave(true);
                            break;
                        }
                    } else {
                        setUiState(7);
                        startSaveToLocal();
                        break;
                    }
                } else {
                    return;
                }
                break;
            case R.id.live_share_cancel /*2131296724*/:
                Log.u(this.TAG, "onClick: live_share_cancel");
                break;
            case R.id.live_share_item /*2131296726*/:
                Log.u(this.TAG, "onClick: live_share_item");
                ShareInfo shareInfo = (ShareInfo) view.getTag();
                if (!shareInfo.className.equals("more")) {
                    Util.startShareMedia(shareInfo.packageName, shareInfo.className, getContext(), this.mSavePath, true);
                    break;
                } else {
                    shareMore();
                    break;
                }
        }
        hideShareSheet();
    }

    public void onDestroy() {
        super.onDestroy();
        ImageView imageView = this.mSaveProgress;
        if (imageView != null) {
            imageView.clearAnimation();
        }
    }

    public void onLiveSaveToLocalFinished(Uri uri, String str) {
        this.mSavedUri = uri;
        if (this.mUiState == 7) {
            shareMore();
        }
    }

    public void onPause() {
        super.onPause();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onPause ui state :");
        sb.append(getCurStateString());
        Log.d(str, sb.toString());
        pausePlay(true, false);
        hideShareSheet();
        this.mPaused = true;
    }

    public void onResume() {
        super.onResume();
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("onResume ui state :");
        sb.append(getCurStateString());
        Log.d(str, sb.toString());
        this.mPaused = false;
        if (!showCover() && this.mPlayerState == 1) {
            attemptStartPlay();
            setUiState(10);
        }
    }

    public void pausePlay(boolean z, boolean z2) {
        MiLivePlayer miLivePlayer;
        int i = this.mUiState;
        if (i == 3 || i == 2) {
            setUiState(4);
            miLivePlayer = this.mPlayer;
            if (miLivePlayer == null) {
                return;
            }
            if (z) {
                miLivePlayer.stopPlayer();
                return;
            }
        } else if (isComposing()) {
            setUiState(4);
            return;
        } else if (this.mUiState == 10) {
            setUiState(4);
            miLivePlayer = this.mPlayer;
            if (miLivePlayer == null) {
                return;
            }
        } else {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("skip pause play, caz state is ");
            sb.append(getCurStateString());
            Log.d(str, sb.toString());
            return;
        }
        miLivePlayer.pausePlayer();
    }

    public void prepare(ContentValues contentValues, List list, String str) {
        if (this.mUiState != 0) {
            String str2 = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("prepare fail, state is ");
            sb.append(getCurStateString());
            Log.d(str2, sb.toString());
            return;
        }
        this.mSaved = false;
        this.mSavedUri = null;
        this.mSaveContentValues = contentValues;
        this.mSegmentData = new ArrayList(list);
        this.mAudioString = str;
        String[] split = contentValues.getAsString("resolution").split("x");
        this.mVideoWidth = Integer.valueOf(split[0]).intValue();
        this.mVideoHeight = Integer.valueOf(split[1]).intValue();
        MiLivePlayer miLivePlayer = this.mPlayer;
        if (miLivePlayer != null) {
            miLivePlayer.init(this.mVideoWidth, this.mVideoHeight, this.mTextureView.getWidth(), this.mTextureView.getHeight(), this.mSegmentData, this.mAudioString);
            startPlay(this.mTextureView.getSurfaceTexture());
        } else {
            setUiState(1);
        }
    }

    public void quitLiveRecordPreview(boolean z) {
        String str = this.TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("quitLiveRecordPreview ");
        sb.append(z);
        Log.d(str, sb.toString());
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction == null) {
            Log.d(this.TAG, "concat error, action null");
            return;
        }
        if (z) {
            cameraAction.onReviewDoneClicked();
        } else {
            cameraAction.onReviewCancelClicked();
        }
        if (getActivity().getVolumeControlStream() != 1) {
            getActivity().setVolumeControlStream(1);
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        Log.d(this.TAG, "register");
        modeCoordinator.attachProtocol(242, this);
        registerBackStack(modeCoordinator, this);
    }

    public void release() {
        Log.d(this.TAG, "release");
        Disposable disposable = this.mUIStateDisposable;
        if (disposable != null) {
            disposable.dispose();
            this.mUIStateDisposable = null;
        }
        Disposable disposable2 = this.mPlayStateDisposable;
        if (disposable2 != null) {
            disposable2.dispose();
            this.mPlayStateDisposable = null;
        }
        Disposable disposable3 = this.mComposeStateDisposable;
        if (disposable3 != null) {
            disposable3.dispose();
            this.mComposeStateDisposable = null;
        }
        MiLivePlayer miLivePlayer = this.mPlayer;
        if (miLivePlayer != null) {
            miLivePlayer.release();
            this.mPlayer = null;
        }
        List list = this.mSegmentData;
        if (list != null) {
            list.clear();
        }
        setUiState(0);
    }

    public void resumePlay() {
        if (this.mUiState != 4) {
            String str = this.TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("skip resume play, caz state is ");
            sb.append(getCurStateString());
            Log.d(str, sb.toString());
            return;
        }
        MiLivePlayer miLivePlayer = this.mPlayer;
        if (miLivePlayer != null) {
            miLivePlayer.resumePlayer();
        }
        setUiState(5);
    }

    public void show() {
        this.mRootView.setVisibility(0);
        initTextureView();
    }

    public void startLiveRecordSaving() {
        if (isAdded() && this.mCameraSnapView.getVisibility() == 0) {
            this.mCameraSnapView.performClick();
        }
    }

    public void startPlay(SurfaceTexture surfaceTexture) {
        if (this.mPlayer != null && !this.mPaused && this.mPlayerState == 1) {
            setUiState(2);
            this.mPlayer.cancelCompose();
            this.mPlayer.startPlayer(surfaceTexture);
        }
    }

    /* access modifiers changed from: protected */
    @CallSuper
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        Log.d(this.TAG, "unRegister");
        modeCoordinator.detachProtocol(242, this);
        unRegisterBackStack(modeCoordinator, this);
    }
}
