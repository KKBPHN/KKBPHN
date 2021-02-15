package com.android.camera.fragment.film;

import android.animation.ValueAnimator;
import android.animation.ValueAnimator.AnimatorUpdateListener;
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
import com.android.camera.data.observeable.FilmDreamProcessing;
import com.android.camera.data.observeable.RxData.DataWrap;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.bottom.BottomAnimationConfig;
import com.android.camera.fragment.vv.VVShareAdapter;
import com.android.camera.fragment.vv.page.PageIndicatorView;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager;
import com.android.camera.fragment.vv.page.PagerGridLayoutManager.PageListener;
import com.android.camera.fragment.vv.page.PagerGridSnapHelper;
import com.android.camera.log.Log;
import com.android.camera.module.FilmDreamModule;
import com.android.camera.module.Module;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import com.android.camera.module.loader.NullHolder;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.FilmDreamConfig;
import com.android.camera.protocol.ModeProtocol.FilmDreamProcess;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.FilmAttr;
import com.android.camera.ui.CameraSnapView;
import com.android.camera.ui.CameraSnapView.SnapListener;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.TextureVideoView.MediaPlayerCallback;
import com.android.camera.ui.drawable.snap.PaintConditionReferred;
import com.xiaomi.camera.rx.CameraSchedulers;
import io.reactivex.Completable;
import io.reactivex.CompletableEmitter;
import io.reactivex.CompletableOnSubscribe;
import io.reactivex.Single;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;
import java.util.List;

public class FragmentFilmDreamProcess extends BaseFragment implements OnClickListener, HandleBackTrace, FilmDreamProcess, SnapListener {
    private static final String TAG = "FragmentFilmDreamProcess";
    private ViewGroup mBottomActionView;
    private ViewGroup mBottomLayout;
    private CameraSnapView mCameraSnapView;
    private ImageView mCombineProgress;
    private ProgressBar mConcatProgress;
    private FilmDreamProcessing mFilmDreamProcessing;
    private Handler mHandler;
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
    private TextView mPreviewTime;
    private FrameLayout mPreviewTimeLayout;
    private boolean mProcessingStarted = false;
    private ContentValues mSaveContentValues;
    private String mSavedPath;
    private Uri mSavedUri;
    private VVShareAdapter mShareAdapter;
    private ProgressBar mShareProgress;
    private RecyclerView mShareRecyclerView;
    private TextureVideoView mTextureVideoView;
    private int mThumbnailOrientation;
    /* access modifiers changed from: private */
    public View mVVDialog;
    private TextView mVVDialogCancel;
    private TextView mVVDialogConfirm;
    private TextView mVVDialogMessage;
    /* access modifiers changed from: private */
    public View mVVShare;
    private TextView mVVShareMessage;
    /* access modifiers changed from: private */
    public boolean mWaitingResultSurfaceTexture;

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
                FragmentFilmDreamProcess.this.stopSegmentPreview(false);
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
                if (FragmentFilmDreamProcess.this.mWaitingResultSurfaceTexture) {
                    FragmentFilmDreamProcess.this.mWaitingResultSurfaceTexture = false;
                    FragmentFilmDreamProcess.this.startPlay(surface);
                }
            }

            public void onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
            }

            public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
            }
        });
        this.mTextureVideoView.setOnClickListener(new OnClickListener() {
            public void onClick(View view) {
                if (FragmentFilmDreamProcess.this.mVVShare.getVisibility() != 0 && FragmentFilmDreamProcess.this.mVVDialog.getVisibility() != 0) {
                    FragmentFilmDreamProcess.this.pausePlay(false, true);
                }
            }
        });
    }

    private void intoResultPreview() {
        if (!this.mWaitingResultSurfaceTexture && !isFullSegmentsPlaying()) {
            Log.d(TAG, "startConcat");
            updateThumbnail();
            this.mFilmDreamProcessing.updateState(2);
            if (this.mTextureVideoView.getPreviewSurface() != null) {
                startPlay(this.mTextureVideoView.getPreviewSurface());
            } else {
                this.mWaitingResultSurfaceTexture = true;
            }
        }
    }

    private boolean isFullSegmentsPlaying() {
        return this.mFilmDreamProcessing.getCurrentState() == 5;
    }

    private void onProcessingSateChanged(int i) {
        ProgressBar progressBar;
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("newState: ");
        sb.append(i);
        Log.d(str, sb.toString());
        switch (i) {
            case 2:
                this.mPreviewTimeLayout.setVisibility(0);
                AlphaOutOnSubscribe.directSetResult(this.mPreviewNext);
                animateIn(this.mPreviewBack);
                animateIn(this.mPreviewShare);
                AlphaInOnSubscribe.directSetResult(this.mPreviewCombine);
                this.mCameraSnapView.setVisibility(8);
                this.mPreviewCombine.setScale(0.38f);
                this.mPreviewCombine.O0000O0o((int) R.raw.vv_save);
                this.mPreviewCombine.O0000oO();
                animateIn(this.mConcatProgress);
                this.mTextureVideoView.setVisibility(0);
                return;
            case 3:
            case 4:
                this.mPreviewStart.setVisibility(0);
                break;
            case 5:
                this.mPreviewStart.setVisibility(8);
                this.mPreviewThumb.setVisibility(8);
                break;
            case 6:
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
            case 7:
                onResultCombineFinished(true);
                return;
            case 8:
                onResultCombineFinished(false);
                return;
            case 9:
                this.mPreviewThumb.setVisibility(0);
                this.mPreviewStart.setVisibility(0);
                this.mConcatProgress.setVisibility(8);
                if (this.mPendingShare) {
                    this.mPendingShare = false;
                    this.mPreviewShare.setVisibility(0);
                    progressBar = this.mShareProgress;
                    break;
                } else {
                    return;
                }
            case 10:
                updateThumbnail();
                this.mPreviewThumb.setVisibility(0);
                this.mPreviewStart.setVisibility(0);
                this.mConcatProgress.setVisibility(8);
                this.mPreviewTimeLayout.setVisibility(0);
                AlphaOutOnSubscribe.directSetResult(this.mPreviewNext);
                animateIn(this.mPreviewBack);
                animateIn(this.mPreviewShare);
                AlphaInOnSubscribe.directSetResult(this.mPreviewCombine);
                this.mCameraSnapView.setVisibility(8);
                this.mPreviewCombine.setScale(0.38f);
                this.mPreviewCombine.O0000O0o((int) R.raw.vv_save);
                this.mPreviewCombine.O0000oO();
                return;
            default:
                return;
        }
        progressBar = this.mConcatProgress;
        progressBar.setVisibility(8);
    }

    /* access modifiers changed from: private */
    public void pausePlay(boolean z, boolean z2) {
        int i;
        FilmDreamProcessing filmDreamProcessing;
        if (!z) {
            filmDreamProcessing = this.mFilmDreamProcessing;
            i = 4;
        } else if (this.mFilmDreamProcessing.getCurrentState() == 1) {
            this.mFilmDreamProcessing.updateState(10);
            return;
        } else if (this.mFilmDreamProcessing.getCurrentState() >= 3) {
            filmDreamProcessing = this.mFilmDreamProcessing;
            i = 9;
        } else {
            return;
        }
        filmDreamProcessing.updateState(i);
        FilmDreamConfig filmDreamConfig = (FilmDreamConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(930);
        if (filmDreamConfig != null) {
            filmDreamConfig.pausePlay();
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
                        FragmentFilmDreamProcess.this.mPageIndicatorView.setSelectedPage(i);
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
                        CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_PLAY_SHARE_CANCEL);
                        FragmentFilmDreamProcess.this.hideShareSheet();
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
        if (!isFullSegmentsPlaying() && !this.mPaused) {
            this.mFilmDreamProcessing.updateState(5);
            FilmDreamConfig filmDreamConfig = (FilmDreamConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(930);
            if (filmDreamConfig != null) {
                filmDreamConfig.startPlay(surface);
            }
        }
    }

    private void startSave() {
        Log.d(TAG, "startSave");
        if (!this.mWaitingResultSurfaceTexture) {
            int currentState = this.mFilmDreamProcessing.getCurrentState();
            if (!(currentState == 6 || currentState == 7 || currentState == 8)) {
                this.mFilmDreamProcessing.updateState(6);
                final String asString = this.mSaveContentValues.getAsString("_data");
                Completable.create(new CompletableOnSubscribe() {
                    public void subscribe(CompletableEmitter completableEmitter) {
                        FilmDreamConfig filmDreamConfig = (FilmDreamConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(930);
                        if (filmDreamConfig != null) {
                            filmDreamConfig.combineVideoAudio(asString);
                        }
                        completableEmitter.onComplete();
                    }
                }).subscribeOn(CameraSchedulers.sCameraSetupScheduler).subscribe();
            }
        }
    }

    /* access modifiers changed from: private */
    public void stopSegmentPreview(boolean z) {
        this.mTextureVideoView.stop();
        this.mTextureVideoView.setVisibility(4);
        this.mBottomActionView.setVisibility(0);
        this.mPreviewLayout.setBackground(null);
    }

    private void updateThumbnail() {
        List tempVideoList = this.mFilmDreamProcessing.getTempVideoList();
        String str = (tempVideoList == null || tempVideoList.size() <= 0) ? "" : (String) tempVideoList.get(0);
        if (!TextUtils.isEmpty(str) && FileCompat.exists(str)) {
            Single.just(str).map(new O00000Oo(this)).subscribeOn(Schedulers.computation()).observeOn(AndroidSchedulers.mainThread()).subscribe((Consumer) new O000000o(this));
        }
    }

    private void updateUIRotate(int i, boolean z) {
        float f;
        ImageView imageView;
        if (z) {
            int rotation = (int) this.mCameraSnapView.getRotation();
            final ArrayList arrayList = new ArrayList();
            arrayList.add(this.mPreviewStart);
            arrayList.add(this.mCameraSnapView);
            arrayList.add(this.mPreviewCombine);
            arrayList.add(this.mPreviewBack);
            arrayList.add(this.mPreviewShare);
            arrayList.add(this.mPreviewNext);
            arrayList.add(this.mVVDialog);
            arrayList.add(this.mVVShare);
            ValueAnimator ofInt = ValueAnimator.ofInt(new int[]{rotation, i});
            ofInt.setInterpolator(new LinearInterpolator());
            ofInt.removeAllUpdateListeners();
            ofInt.addUpdateListener(new AnimatorUpdateListener() {
                public void onAnimationUpdate(ValueAnimator valueAnimator) {
                    int intValue = ((Integer) valueAnimator.getAnimatedValue()).intValue();
                    for (View view : arrayList) {
                        if (view != null) {
                            ViewCompat.setRotation(view, (float) intValue);
                        }
                    }
                }
            });
            if ((i == 90 || i == 0) && rotation != 90) {
                ofInt.setIntValues(new int[]{rotation, 90});
            } else if ((i == 180 || i == 270) && rotation != 270) {
                ofInt.setIntValues(new int[]{rotation, 270});
            } else {
                return;
            }
            ofInt.start();
            return;
        }
        if (isLeftBothLandScape()) {
            imageView = this.mPreviewStart;
            f = 90.0f;
        } else {
            imageView = this.mPreviewStart;
            f = 270.0f;
        }
        imageView.setRotation(f);
        this.mCameraSnapView.setRotation(f);
        this.mPreviewCombine.setRotation(f);
        this.mPreviewBack.setRotation(f);
        this.mPreviewShare.setRotation(f);
        this.mPreviewNext.setRotation(f);
        this.mVVDialog.setRotation(f);
        this.mVVShare.setRotation(f);
    }

    public /* synthetic */ void O00000Oo(NullHolder nullHolder) {
        if (nullHolder.isPresent()) {
            this.mPreviewThumb.setImageBitmap((Bitmap) nullHolder.get());
        }
    }

    public /* synthetic */ void O00000o(DataWrap dataWrap) {
        onProcessingSateChanged(((Integer) dataWrap.get()).intValue());
    }

    public /* synthetic */ NullHolder O0000o0O(String str) {
        MediaMetadataRetriever mediaMetadataRetriever = new MediaMetadataRetriever();
        mediaMetadataRetriever.setDataSource(str);
        return rotateBitmap(mediaMetadataRetriever.getFrameAtTime(0), this.mThumbnailOrientation);
    }

    public boolean canSnap() {
        return true;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_FILM_DREAM_PROCESS;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_file_dream_process;
    }

    public ContentValues getSaveContentValues() {
        return this.mSaveContentValues;
    }

    public String getTimeValue() {
        return Util.millisecondToTimeString(Util.clamp(this.mFilmDreamProcessing.getTotalTime(), 0, (long) FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME), false, false);
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        this.mPreviewTimeLayout = (FrameLayout) view.findViewById(R.id.film_dream_preview_top);
        this.mPreviewTime = (TextView) view.findViewById(R.id.film_dream_preview_time);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mPreviewTimeLayout.getLayoutParams();
        marginLayoutParams.topMargin = Display.getTopMargin();
        marginLayoutParams.height = Display.getTopBarHeight();
        this.mPreviewTimeLayout.setLayoutParams(marginLayoutParams);
        this.mPreviewLayout = (FrameLayout) view.findViewById(R.id.film_dream_preview_layout);
        this.mPreviewCover = (FrameLayout) view.findViewById(R.id.film_dream_process_video_cover);
        this.mConcatProgress = (ProgressBar) view.findViewById(R.id.film_dream_concat_progress);
        this.mShareProgress = (ProgressBar) view.findViewById(R.id.film_dream_share_progress);
        this.mCameraSnapView = (CameraSnapView) view.findViewById(R.id.film_dream_preview_save_circle);
        this.mCameraSnapView.setSnapListener(this);
        this.mCombineProgress = (ImageView) view.findViewById(R.id.film_dream_preview_operation_image);
        this.mPreviewCombine = (LottieAnimationView) view.findViewById(R.id.film_dream_preview_save);
        this.mPreviewBack = (ImageView) view.findViewById(R.id.film_dream_preview_back);
        this.mPreviewShare = (ImageView) view.findViewById(R.id.film_dream_preview_share);
        this.mPreviewStart = (ImageView) view.findViewById(R.id.film_dream_preview_play);
        this.mPreviewThumb = (ImageView) view.findViewById(R.id.film_dream_preview_thumb);
        this.mPreviewNext = (ImageView) view.findViewById(R.id.film_dream_preview_next);
        this.mVVDialog = view.findViewById(R.id.vv_dialog);
        this.mVVDialogMessage = (TextView) this.mVVDialog.findViewById(R.id.vv_dialog_message);
        this.mVVDialogConfirm = (TextView) this.mVVDialog.findViewById(R.id.vv_dialog_positive);
        this.mVVDialogCancel = (TextView) this.mVVDialog.findViewById(R.id.vv_dialog_negative);
        this.mVVShare = view.findViewById(R.id.vv_share);
        this.mVVShareMessage = (TextView) this.mVVShare.findViewById(R.id.vv_share_message);
        this.mShareRecyclerView = (RecyclerView) this.mVVShare.findViewById(R.id.vv_share_recyclerview);
        this.mShareRecyclerView.setFocusable(false);
        this.mPageIndicatorView = (PageIndicatorView) this.mVVShare.findViewById(R.id.vv_share_recyclerview_indicator);
        this.mCameraSnapView.setOnClickListener(this);
        this.mPreviewCombine.setOnClickListener(this);
        this.mPreviewBack.setOnClickListener(this);
        this.mPreviewShare.setOnClickListener(this);
        this.mPreviewStart.setOnClickListener(this);
        this.mPreviewNext.setOnClickListener(this);
        this.mPreviewTimeLayout.setOnClickListener(this);
        FolmeUtils.touchScaleTint(this.mPreviewBack, this.mPreviewShare, this.mPreviewStart, this.mPreviewNext);
        FolmeUtils.touchScale(this.mPreviewCombine);
        FolmeUtils.touchDialogButtonTint(this.mVVDialogCancel, this.mVVDialogConfirm, this.mVVDialogMessage);
        this.mBottomActionView = (FrameLayout) view.findViewById(R.id.film_dream_preview_bottom_action);
        MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mBottomActionView.getLayoutParams();
        marginLayoutParams2.height = Display.getBottomHeight();
        if (Display.fitDisplayFull(1.3333333f)) {
            marginLayoutParams2.bottomMargin = Display.getNavigationBarHeight();
        }
        this.mBottomLayout = (RelativeLayout) view.findViewById(R.id.film_dream_preview_bottom_parent);
        MarginLayoutParams marginLayoutParams3 = (MarginLayoutParams) this.mBottomLayout.getLayoutParams();
        marginLayoutParams3.height = Math.round(((float) Display.getBottomBarHeight()) * 0.7f);
        marginLayoutParams3.bottomMargin = Display.getBottomMargin();
        marginLayoutParams3.topMargin = Math.round(((float) Display.getBottomBarHeight()) * 0.3f);
        this.mFilmDreamProcessing = (FilmDreamProcessing) DataRepository.dataItemObservable().get(FilmDreamProcessing.class);
        this.mFilmDreamProcessing.startObservable(this, new O00000o0(this));
        Rect displayRect = Util.getDisplayRect(1);
        int i = displayRect.top;
        int windowHeight = (i - ((Display.getWindowHeight() - i) - displayRect.height())) / 2;
        ((MarginLayoutParams) this.mVVDialog.getLayoutParams()).topMargin = windowHeight;
        ((MarginLayoutParams) this.mVVShare.getLayoutParams()).topMargin = windowHeight;
        prepare();
        provideAnimateElement(this.mCurrentMode, null, 2);
    }

    public void notifyAfterFrameAvailable(int i) {
        super.notifyAfterFrameAvailable(i);
    }

    public boolean onBackEvent(int i) {
        TextureVideoView textureVideoView = this.mTextureVideoView;
        if (textureVideoView == null || !textureVideoView.isPlaying()) {
            return hideVVDialog() || hideShareSheet();
        }
        stopSegmentPreview(true);
        return true;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:22:0x0072, code lost:
        startSave();
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void onClick(View view) {
        if (this.mConcatProgress.getVisibility() != 0 && this.mCombineProgress.getVisibility() != 0 && this.mShareProgress.getVisibility() != 0 && (view.getId() == R.id.live_share_item || (this.mVVShare.getVisibility() != 0 && this.mVVDialog.getVisibility() != 0))) {
            switch (view.getId()) {
                case R.id.film_dream_preview_back /*2131296590*/:
                    showExitConfirm();
                    break;
                case R.id.film_dream_preview_play /*2131296596*/:
                    resumePlay();
                    break;
                case R.id.film_dream_preview_save /*2131296597*/:
                    CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_PLAY_SAVE);
                    if (this.mSavedUri != null) {
                        quitLiveRecordPreview(true);
                        break;
                    }
                case R.id.film_dream_preview_share /*2131296599*/:
                    CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_PLAY_SHARE);
                    if (!checkAndShare()) {
                        this.mPendingShare = true;
                    }
                    break;
                case R.id.live_share_item /*2131296726*/:
                    hideShareSheet();
                    ResolveInfo resolveInfo = (ResolveInfo) view.getTag();
                    Util.startShareMedia(resolveInfo.activityInfo.packageName, resolveInfo.activityInfo.name, getContext(), this.mSavedPath, true);
                    break;
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
        if (isAdded()) {
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
                    FragmentFilmDreamProcess.this.quitLiveRecordPreview(true);
                }
            });
            return;
        }
        Log.d(TAG, "combineFinished and share");
        ActivityBase activityBase = (ActivityBase) getContext();
        if (activityBase == null || activityBase.getCurrentModule() == null || activityBase.getCurrentModule().getModuleIndex() != 212) {
            Log.w(TAG, "combineSuccess and share is not allowed!!!");
        } else {
            ((FilmDreamModule) activityBase.getCurrentModule()).startSaveToLocal();
        }
    }

    public void onResultPreviewFinished(boolean z) {
        this.mHandler.post(new Runnable() {
            public void run() {
                FragmentFilmDreamProcess.this.mPreviewStart.setVisibility(0);
                FragmentFilmDreamProcess.this.mPreviewThumb.setVisibility(0);
            }
        });
    }

    public void onResume() {
        super.onResume();
        this.mPaused = false;
        FilmDreamProcessing filmDreamProcessing = this.mFilmDreamProcessing;
        if (filmDreamProcessing != null) {
            onProcessingSateChanged(filmDreamProcessing.getCurrentState());
        }
    }

    public void onSnapClick() {
        if (!(this.mConcatProgress.getVisibility() == 0 || this.mCombineProgress.getVisibility() == 0 || this.mShareProgress.getVisibility() == 0)) {
            if (this.mPreviewNext.getVisibility() == 0) {
                this.mPreviewNext.performClick();
            } else if (this.mPreviewCombine.getVisibility() == 0) {
                this.mPreviewCombine.performClick();
            } else {
                Camera camera = (Camera) getContext();
                if (camera != null) {
                    Module currentModule = camera.getCurrentModule();
                    if (currentModule == null || !currentModule.isIgnoreTouchEvent() || currentModule.isFrameAvailable()) {
                        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
                        if (cameraAction != null) {
                            cameraAction.onShutterButtonClick(10);
                        }
                    }
                }
            }
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

    public void prepare() {
        this.mCurrentMode = 212;
        this.mCameraSnapView.setVisibility(0);
        this.mCameraSnapView.setParameters(PaintConditionReferred.create(this.mCurrentMode));
        this.mCameraSnapView.onTimeOut();
        this.mConcatProgress.setVisibility(8);
        this.mCombineProgress.setVisibility(8);
        this.mShareProgress.setVisibility(8);
        this.mPreviewCombine.setVisibility(8);
        this.mPreviewBack.setVisibility(8);
        this.mPreviewShare.setVisibility(8);
        this.mPreviewStart.setVisibility(8);
        this.mPreviewThumb.setVisibility(8);
        this.mPreviewNext.setVisibility(8);
        initTextureView();
    }

    public void processingFinish() {
        if (Util.isAccessible()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_button));
        }
    }

    public void processingPause() {
        this.mCameraSnapView.pauseRecording();
    }

    public void processingPrepare() {
        this.mCameraSnapView.prepareRecording(BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables());
    }

    public void processingStart() {
        this.mProcessingStarted = true;
        this.mCameraSnapView.triggerAnimation(BottomAnimationConfig.generate(false, this.mCurrentMode, true, false, false).configVariables());
        if (Util.isAccessible()) {
            this.mCameraSnapView.setContentDescription(getString(R.string.accessibility_shutter_process_button));
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        updateUIRotate(this.mDegree, false);
    }

    public void provideRotateItem(List list, int i) {
        super.provideRotateItem(list, i);
        updateUIRotate(this.mDegree, true);
    }

    public void quitLiveRecordPreview(boolean z) {
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
        modeCoordinator.attachProtocol(931, this);
        registerBackStack(modeCoordinator, this);
    }

    public void resumePlay() {
        if (this.mPaused || isFullSegmentsPlaying()) {
            return;
        }
        if (this.mFilmDreamProcessing.getCurrentState() != 4) {
            intoResultPreview();
            return;
        }
        FilmDreamConfig filmDreamConfig = (FilmDreamConfig) ModeCoordinatorImpl.getInstance().getAttachProtocol(930);
        if (filmDreamConfig == null) {
            Log.w(TAG, "resumePlay failed, filmDreamConfig is null");
            return;
        }
        this.mFilmDreamProcessing.updateState(5);
        filmDreamConfig.resumePlay();
    }

    public void setThumbnailOrientation(int i) {
        if (Math.abs(i) == 90) {
            i = -i;
        }
        this.mThumbnailOrientation = i;
    }

    public void showExitConfirm() {
        if (this.mFilmDreamProcessing.getCurrentState() == 0) {
            CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_EXIT_PREVIEW);
            quitLiveRecordPreview(false);
        } else if (this.mVVDialog.getVisibility() != 0) {
            this.mVVDialogMessage.setText(R.string.live_edit_exit_message);
            this.mVVDialogConfirm.setText(R.string.live_edit_exit_confirm);
            this.mVVDialogCancel.setText(R.string.snap_cancel);
            this.mVVDialogConfirm.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    CameraStatUtils.trackFilmDreamClick(FilmAttr.VALUE_FILM_DREAM_CLICK_EXIT_CONFIRM);
                    FragmentFilmDreamProcess.this.quitLiveRecordPreview(false);
                    FragmentFilmDreamProcess.this.hideVVDialog();
                }
            });
            this.mVVDialogCancel.setOnClickListener(new OnClickListener() {
                public void onClick(View view) {
                    FragmentFilmDreamProcess.this.hideVVDialog();
                }
            });
            Completable.create(new AlphaInOnSubscribe(this.mVVDialog)).subscribe();
        }
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(931, this);
        unRegisterBackStack(modeCoordinator, this);
    }

    public void updateRecordingTime(String str) {
        this.mCameraSnapView.setDurationText(str);
    }
}
