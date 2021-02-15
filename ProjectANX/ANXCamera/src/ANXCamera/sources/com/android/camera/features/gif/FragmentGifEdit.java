package com.android.camera.features.gif;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout.LayoutParams;
import androidx.annotation.Nullable;
import com.android.camera.ActivityBase;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.data.DataRepository;
import com.android.camera.features.mimoji2.module.protocol.MimojiModeProtocol.MimojiAvatarEngine2;
import com.android.camera.features.mimoji2.utils.MimojiViewUtil;
import com.android.camera.fragment.BaseFragment;
import com.android.camera.log.Log;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.ActionProcessing;
import com.android.camera.protocol.ModeProtocol.BaseDelegate;
import com.android.camera.protocol.ModeProtocol.CameraAction;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.MimojiGifEditor;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.protocol.ModeProtocol.RecordState;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.Mimoji;
import com.android.camera.storage.Storage;
import java.util.List;

public class FragmentGifEdit extends BaseFragment implements OnClickListener, HandleBackTrace, MimojiGifEditor {
    public static final int FRAGMENT_INFO = 65524;
    public static final String TAG = "MimojiFragmentGifEdit";
    private boolean isBackToPreviewState = false;
    private ImageView mBackBtn;
    private ProgressBar mConcatProgressBar;
    private ImageView mConfirmBtn;
    private Context mContext;
    private long mDuration;
    private Bitmap mGifBitmap;
    private GifEditLayout mGifEditLayout;
    private GifMediaPlayer mGifMediaPlayer;
    private View mGifViewLayout;
    private GifViewPresenter mGifViewPresenter;
    private LinearLayout mIlShare;
    private boolean mIsNeedCombine = true;
    private boolean mIsShare;
    private FrameLayout mLlTextureContainer;
    private ImageView mPreviewCoverView;
    private TextureView mTextureView;
    private String mVideoPath;

    private void backToPreview() {
        if (!this.isBackToPreviewState) {
            this.isBackToPreviewState = true;
            if (getActivity() != null) {
                getActivity().runOnUiThread(new O000000o(this));
            }
        }
    }

    private void initGifMediaPlayer() {
        if (this.mGifMediaPlayer == null) {
            boolean z = !C0122O00000o.instance().OOo0O0() || DataRepository.dataItemLive().getMimojiStatusManager2().getCurrentMimojiInfo() != null;
            this.mGifMediaPlayer = new GifMediaPlayer(this.mContext, z);
        }
        this.mGifViewPresenter.setGifMediaPlayer(this.mGifMediaPlayer);
        this.mGifMediaPlayer.setTextureView(this.mTextureView);
        this.mGifMediaPlayer.setPreviewCoverView(this.mPreviewCoverView);
        this.mGifMediaPlayer.setConcatProgressBar(this.mConcatProgressBar);
        this.mGifMediaPlayer.setGifEditLayout(this.mGifEditLayout);
        if (this.mIsNeedCombine) {
            this.mIsNeedCombine = false;
            combineVideoAudio(this.mVideoPath, this.mDuration);
        }
    }

    private void release() {
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null) {
            gifMediaPlayer.releaseMedia(false);
            this.mGifMediaPlayer = null;
        }
        Bitmap bitmap = this.mGifBitmap;
        if (bitmap != null && !bitmap.isRecycled()) {
            this.mGifBitmap.recycle();
            this.mGifBitmap = null;
        }
    }

    private void saveGif() {
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null) {
            gifMediaPlayer.startVideo2Gif();
        }
    }

    public /* synthetic */ void O000Oo0o() {
        MimojiViewUtil.setViewVisible(this.mGifViewLayout, false);
        BaseDelegate baseDelegate = (BaseDelegate) ModeCoordinatorImpl.getInstance().getAttachProtocol(160);
        if (baseDelegate != null) {
            baseDelegate.delegateEvent(20);
        }
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null) {
            gifMediaPlayer.setIsComposing(false);
        }
    }

    public /* synthetic */ void O000OoO0() {
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (mimojiAvatarEngine2 != null) {
            mimojiAvatarEngine2.deleteMimojiCache(3);
        }
        CameraAction cameraAction = (CameraAction) ModeCoordinatorImpl.getInstance().getAttachProtocol(161);
        if (cameraAction != null) {
            cameraAction.onReviewCancelClicked();
        } else {
            String str = "MimojiFragmentGifEdit";
            Log.d(str, "mimoji void onCombineError[] cameraAction null");
            RecordState recordState = (RecordState) ModeCoordinatorImpl.getInstance().getAttachProtocol(212);
            if (recordState != null) {
                recordState.onFinish();
            } else {
                Log.d(str, "mimoji void onCombineError[] recordState null");
            }
        }
        this.mGifViewLayout.postDelayed(new O00000Oo(this), 200);
    }

    public void combineVideoAudio(String str, long j) {
        this.mVideoPath = str;
        this.mDuration = j;
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer == null || gifMediaPlayer.getSurface() == null) {
            Log.e("MimojiFragmentGifEdit", "mimoji void combineVideoAudio[savePath] mGifMediaPlayer null");
            this.mIsNeedCombine = true;
            GifMediaPlayer gifMediaPlayer2 = this.mGifMediaPlayer;
            if (gifMediaPlayer2 != null) {
                gifMediaPlayer2.setVideoUrl(this.mVideoPath, this.mDuration);
            }
            return;
        }
        this.mIsNeedCombine = false;
        this.mGifMediaPlayer.openVideoUrl(this.mVideoPath, this.mDuration);
    }

    public void coverGifError() {
        backToPreview();
    }

    public void coverGifSuccess(String str) {
        if (this.mIsShare) {
            Uri addGifSync = ((ActivityBase) getActivity()).getImageSaver().addGifSync(str, 300, 300);
            Intent intent = new Intent("android.intent.action.SEND");
            intent.putExtra("android.intent.extra.STREAM", addGifSync);
            intent.setType(Storage.MIME_GIF);
            startActivity(Intent.createChooser(intent, getString(R.string.share)));
            GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
            if (gifMediaPlayer != null) {
                gifMediaPlayer.setIsComposing(false);
                return;
            }
            return;
        }
        ActionProcessing actionProcessing = (ActionProcessing) ModeCoordinatorImpl.getInstance().getAttachProtocol(162);
        if (actionProcessing != null) {
            actionProcessing.announceForAccessibility(R.string.accessibility_camera_shutter_finish);
        }
        ((ActivityBase) getActivity()).getImageSaver().addGif(str, 300, 300);
        backToPreview();
    }

    public int getFragmentInto() {
        return 65524;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return R.layout.fragment_gif_bottom;
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        if (view == null) {
            Log.e("MimojiFragmentGifEdit", " rootview null");
            return;
        }
        this.mGifViewLayout = view;
        this.mContext = getContext();
        this.mLlTextureContainer = (FrameLayout) view.findViewById(R.id.ll_operate_gif_words);
        MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mLlTextureContainer.getLayoutParams();
        marginLayoutParams.height = Util.getDisplayRect(4).right;
        marginLayoutParams.width = Util.getDisplayRect(4).right;
        marginLayoutParams.topMargin = Util.getDisplayRect(4).top;
        this.mLlTextureContainer.setLayoutParams(marginLayoutParams);
        LayoutParams layoutParams = (LayoutParams) view.findViewById(R.id.rv_bottom_navigation_layout).getLayoutParams();
        if (layoutParams != null) {
            if (!Util.isFullScreenNavBarHidden(getContext())) {
                layoutParams.bottomMargin = getResources().getDimensionPixelOffset(R.dimen.dimen_gif_full_marginBottom);
            } else {
                layoutParams.bottomMargin = 0;
            }
        }
        this.mGifViewPresenter = new GifViewPresenter(getContext());
        this.mGifViewPresenter.initView(this.mGifViewLayout);
        this.mIlShare = (LinearLayout) view.findViewById(R.id.ll_gif_share);
        this.mIlShare.setOnClickListener(this);
        this.mTextureView = (TextureView) view.findViewById(R.id.gif_texture_view);
        this.mTextureView.setOnClickListener(this);
        this.mPreviewCoverView = (ImageView) view.findViewById(R.id.image_mimoji_cover);
        this.mConcatProgressBar = (ProgressBar) view.findViewById(R.id.concat_mimoji_progress);
        this.mGifEditLayout = (GifEditLayout) view.findViewById(R.id.fl_edit_gif);
        this.mBackBtn = (ImageView) view.findViewById(R.id.iv_gif_back);
        this.mBackBtn.setOnClickListener(this);
        this.mConfirmBtn = (ImageView) view.findViewById(R.id.iv_gif_confirm);
        this.mConfirmBtn.setOnClickListener(this);
        MimojiAvatarEngine2 mimojiAvatarEngine2 = (MimojiAvatarEngine2) ModeCoordinatorImpl.getInstance().getAttachProtocol(246);
        if (mimojiAvatarEngine2 == null || mimojiAvatarEngine2.getGifBitmap() == null || mimojiAvatarEngine2.getGifBitmap().isRecycled()) {
            MimojiViewUtil.setViewVisible(this.mPreviewCoverView, false);
        } else {
            this.mGifBitmap = mimojiAvatarEngine2.getGifBitmap();
            this.mPreviewCoverView.setImageBitmap(this.mGifBitmap);
            MimojiViewUtil.setViewVisible(this.mPreviewCoverView, true);
        }
        MimojiViewUtil.setViewVisible(this.mConcatProgressBar, true);
        initGifMediaPlayer();
    }

    public boolean onBackEvent(int i) {
        if (i != 1) {
            return false;
        }
        if (!TextUtils.isEmpty(this.mVideoPath) && this.mGifMediaPlayer != null) {
            backToPreview();
        }
        return true;
    }

    public void onClick(View view) {
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null && gifMediaPlayer.isEnable() && !this.mGifMediaPlayer.isComposing()) {
            switch (view.getId()) {
                case R.id.gif_texture_view /*2131296624*/:
                    this.mGifEditLayout.hideBorder(false);
                    return;
                case R.id.iv_gif_back /*2131296679*/:
                    backToPreview();
                    return;
                case R.id.iv_gif_confirm /*2131296680*/:
                    CameraStatUtils.trackMimoji2Click(null, Mimoji.GIF_SAVE);
                    this.mIsShare = false;
                    break;
                case R.id.ll_gif_share /*2131296747*/:
                    CameraStatUtils.trackMimoji2Click(null, Mimoji.GIF_SHARE);
                    this.mIsShare = true;
                    break;
                default:
                    return;
            }
            saveGif();
        }
    }

    @Nullable
    public View onCreateView(LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_gif_bottom, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onDestroyView() {
        release();
        super.onDestroyView();
        Log.i("MimojiFragmentGifEdit", "onDestroy  ReleaseMedia");
    }

    public void onPause() {
        super.onPause();
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null && !this.mIsNeedCombine) {
            gifMediaPlayer.pausePlay();
        }
        GifEditLayout gifEditLayout = this.mGifEditLayout;
        if (gifEditLayout != null) {
            gifEditLayout.hideBorder(true);
        }
    }

    public void onResume() {
        super.onResume();
        this.isBackToPreviewState = false;
        if (this.mIsNeedCombine) {
            initView(getView());
            return;
        }
        GifMediaPlayer gifMediaPlayer = this.mGifMediaPlayer;
        if (gifMediaPlayer != null) {
            gifMediaPlayer.resumePlay();
        }
    }

    public void onStop() {
        super.onStop();
    }

    public void operateGifPannelVisibleState(int i) {
        this.mGifViewPresenter.operateGifPannelVisibleState(i);
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        StringBuilder sb = new StringBuilder();
        sb.append("provideAnimateElement, animateInElements");
        sb.append(list);
        sb.append("resetType = ");
        sb.append(i2);
        Log.d("MimojiFragmentGifEdit", sb.toString());
        if (MimojiViewUtil.getViewIsVisible(this.mGifViewLayout) && i2 == 3) {
            backToPreview();
        }
    }

    public void reconfigPreviewRadio(int i) {
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        registerBackStack(modeCoordinator, this);
        ModeCoordinatorImpl.getInstance().attachProtocol(251, this);
    }

    public void setPreviewCover(Bitmap bitmap) {
    }

    public void showPreviewCover(boolean z) {
        MimojiViewUtil.setViewVisible(this.mPreviewCoverView, z);
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        unRegisterBackStack(modeCoordinator, this);
        ModeCoordinatorImpl.getInstance().detachProtocol(251, this);
    }
}
