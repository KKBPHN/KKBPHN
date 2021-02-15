package com.android.camera.fragment.clone;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import android.content.res.AssetFileDescriptor;
import android.content.res.Resources;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnLayoutChangeListener;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import androidx.annotation.Nullable;
import com.android.camera.R;
import com.android.camera.Util;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.animation.FragmentAnimationFactory;
import com.android.camera.fragment.BaseFragmentDelegate;
import com.android.camera.fragment.BasePanelFragment;
import com.android.camera.fragment.FragmentUtils;
import com.android.camera.log.Log;
import com.android.camera.module.impl.component.MultiFeatureManagerImpl;
import com.android.camera.protocol.ModeCoordinatorImpl;
import com.android.camera.protocol.ModeProtocol.CloneChooser;
import com.android.camera.protocol.ModeProtocol.ConfigChanges;
import com.android.camera.protocol.ModeProtocol.HandleBackTrace;
import com.android.camera.protocol.ModeProtocol.ModeCoordinator;
import com.android.camera.statistic.CameraStatUtils;
import com.android.camera.statistic.MistatsConstants.CloneAttr;
import com.android.camera.ui.TextureVideoView;
import com.android.camera.ui.TextureVideoView.MediaPlayerAdapter;
import com.xiaomi.fenshen.FenShenCam.Mode;
import java.io.IOException;
import java.util.List;

public class FragmentCloneGallery extends BasePanelFragment implements OnClickListener, CloneChooser, HandleBackTrace {
    public static final String TAG = "FragmentCloneGallery";
    private ImageView mCopyButton;
    private ImageView mCopyIndicator;
    private ViewGroup mCopyLayout;
    private TextureVideoView mCopyVideoView;
    private boolean mIsSupportCloneCopyMode = false;
    private ImageView mPhotoButton;
    private ImageView mPhotoIndicator;
    private ViewGroup mPhotoLayout;
    /* access modifiers changed from: private */
    public HorizontalScrollView mScrollView;
    private Mode mSelectedMode;
    private ImageView mVideoButton;
    private ImageView mVideoIndicator;
    private ViewGroup mVideoLayout;
    private TextureVideoView mVideoVideoView;

    /* renamed from: com.android.camera.fragment.clone.FragmentCloneGallery$3 reason: invalid class name */
    /* synthetic */ class AnonymousClass3 {
        static final /* synthetic */ int[] $SwitchMap$com$xiaomi$fenshen$FenShenCam$Mode = new int[Mode.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(8:0|1|2|3|4|5|6|8) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Mode[Mode.PHOTO.ordinal()] = 1;
            $SwitchMap$com$xiaomi$fenshen$FenShenCam$Mode[Mode.VIDEO.ordinal()] = 2;
            try {
                $SwitchMap$com$xiaomi$fenshen$FenShenCam$Mode[Mode.MCOPY.ordinal()] = 3;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    private void onCopyModeClick(boolean z) {
        Log.u(TAG, "onClick: clone_copy_layout");
        unSelectVideoButton();
        startPlay(this.mCopyVideoView, "clone_freeze_frame_mode.mp4", this.mCopyButton);
        int i = 1;
        animateViews(1, true, (View) this.mCopyIndicator);
        animateViews(-1, false, (View) this.mPhotoIndicator);
        this.mSelectedMode = Mode.MCOPY;
        Config.setCloneMode(this.mSelectedMode);
        setAccessibility(true);
        if (!Util.isLayoutRTL(getContext())) {
            i = 3;
        }
        scrollToChildView(i, z);
    }

    private void onPhotoModeClick(boolean z) {
        Log.u(TAG, "onClick: clone_photo_layout");
        int i = 1;
        animateViews(1, true, (View) this.mPhotoIndicator);
        unSelectVideoButton();
        unSelectCopyButton();
        this.mSelectedMode = Mode.PHOTO;
        Config.setCloneMode(this.mSelectedMode);
        setAccessibility(true);
        if (Util.isLayoutRTL(getContext())) {
            i = 3;
        }
        scrollToChildView(i, z);
    }

    private void onVideoModeClick(boolean z) {
        Log.u(TAG, "onClick: clone_video_layout");
        unSelectCopyButton();
        startPlay(this.mVideoVideoView, "clone_video_mode.mp4", this.mVideoButton);
        animateViews(1, true, (View) this.mVideoIndicator);
        animateViews(-1, false, (View) this.mPhotoIndicator);
        this.mSelectedMode = Mode.VIDEO;
        Config.setCloneMode(this.mSelectedMode);
        setAccessibility(false);
        scrollToChildView(2, z);
    }

    private void scrollToChildView(int i, final boolean z) {
        if (this.mScrollView != null) {
            Resources resources = getResources();
            int dimensionPixelOffset = resources.getDimensionPixelOffset(R.dimen.clone_list_item_image_width_3_column);
            int dimensionPixelOffset2 = resources.getDimensionPixelOffset(R.dimen.clone_list_item_spacer_item_width_3_column);
            final int i2 = 0;
            if (i != 1) {
                if (i == 2) {
                    i2 = ((dimensionPixelOffset2 * 2) + dimensionPixelOffset) - ((Util.getDisplayRect().width() - dimensionPixelOffset) / 2);
                } else if (i == 3) {
                    i2 = ((dimensionPixelOffset * 3) + (dimensionPixelOffset2 * 4)) - Util.getDisplayRect().width();
                }
            }
            this.mScrollView.addOnLayoutChangeListener(new OnLayoutChangeListener() {
                public void onLayoutChange(View view, int i, int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
                    FragmentCloneGallery.this.mScrollView.removeOnLayoutChangeListener(this);
                    if (z) {
                        FragmentCloneGallery.this.mScrollView.smoothScrollTo(i2, 0);
                    } else {
                        FragmentCloneGallery.this.mScrollView.scrollTo(i2, 0);
                    }
                }
            });
        }
    }

    private void selectLastModule() {
        Mode cloneMode = Config.getCloneMode();
        if (cloneMode == null) {
            cloneMode = Mode.PHOTO;
        }
        int i = AnonymousClass3.$SwitchMap$com$xiaomi$fenshen$FenShenCam$Mode[cloneMode.ordinal()];
        if (i == 1) {
            onPhotoModeClick(false);
        } else if (i == 2) {
            onVideoModeClick(false);
        } else if (i == 3) {
            onCopyModeClick(false);
        }
    }

    private void setAccessibility(boolean z) {
        ViewGroup viewGroup;
        Runnable runnable;
        if (isAdded()) {
            String str = ", ";
            if (z) {
                ViewGroup viewGroup2 = this.mPhotoLayout;
                StringBuilder sb = new StringBuilder();
                sb.append(getString(R.string.clone_photo_mode));
                sb.append(str);
                sb.append(getString(R.string.accessibility_selected));
                viewGroup2.setContentDescription(sb.toString());
                this.mVideoLayout.setContentDescription(getString(R.string.clone_video_mode));
                if (Util.isAccessible()) {
                    viewGroup = this.mPhotoLayout;
                    runnable = new O000000o(this);
                }
            }
            this.mPhotoLayout.setContentDescription(getString(R.string.clone_photo_mode));
            ViewGroup viewGroup3 = this.mVideoLayout;
            StringBuilder sb2 = new StringBuilder();
            sb2.append(getString(R.string.clone_video_mode));
            sb2.append(str);
            sb2.append(getString(R.string.accessibility_selected));
            viewGroup3.setContentDescription(sb2.toString());
            if (Util.isAccessible()) {
                viewGroup = this.mVideoLayout;
                runnable = new O00000Oo(this);
            }
            viewGroup.postDelayed(runnable, 100);
        }
    }

    private void startPlay(TextureVideoView textureVideoView, String str, final ImageView imageView) {
        try {
            final AssetFileDescriptor openFd = MultiFeatureManagerImpl.getAssetManager(getContext()).openFd(str);
            textureVideoView.setVideoFileDescriptor(openFd);
            textureVideoView.setVisibility(0);
            textureVideoView.setIsNeedAudio(false);
            textureVideoView.setLoop(true);
            textureVideoView.setMediaPlayerCallback(new MediaPlayerAdapter() {
                public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                    if (i == 3) {
                        imageView.setVisibility(4);
                    }
                    return super.onInfo(mediaPlayer, i, i2);
                }

                public void onPrepared(MediaPlayer mediaPlayer) {
                    super.onPrepared(mediaPlayer);
                    Util.closeSafely(openFd);
                }
            });
            textureVideoView.start(0);
        } catch (IOException unused) {
            Log.w(TAG, "open failed");
        }
    }

    private void stopPlay(TextureVideoView textureVideoView) {
        if (textureVideoView.isPlaying()) {
            textureVideoView.stop();
            textureVideoView.setVisibility(4);
        }
    }

    private void unSelectCopyButton() {
        if (this.mIsSupportCloneCopyMode) {
            animateViews(-1, false, (View) this.mCopyIndicator);
            this.mCopyButton.setVisibility(0);
            stopPlay(this.mVideoVideoView);
        }
    }

    private void unSelectVideoButton() {
        animateViews(-1, false, (View) this.mVideoIndicator);
        this.mVideoButton.setVisibility(0);
        stopPlay(this.mVideoVideoView);
    }

    public /* synthetic */ void O000o() {
        if (isAdded()) {
            this.mVideoLayout.sendAccessibilityEvent(128);
        }
    }

    public /* synthetic */ void O000o0oo() {
        if (isAdded()) {
            this.mPhotoLayout.sendAccessibilityEvent(128);
        }
    }

    /* access modifiers changed from: protected */
    public int getAnimationType() {
        return 1;
    }

    public int getFragmentInto() {
        return BaseFragmentDelegate.FRAGMENT_CLONE_GALLERY;
    }

    /* access modifiers changed from: protected */
    public int getLayoutResourceId() {
        return this.mIsSupportCloneCopyMode ? R.layout.fragment_clone_gallery_with_copy_mode : R.layout.fragment_clone_gallery;
    }

    public void hide() {
        FragmentUtils.removeFragmentByTag(getFragmentManager(), String.valueOf(BaseFragmentDelegate.FRAGMENT_CLONE_GALLERY));
    }

    /* access modifiers changed from: protected */
    public void initView(View view) {
        Util.alignPopupBottom(view);
        this.mScrollView = (HorizontalScrollView) view.findViewById(R.id.clone_scroll_layout);
        this.mPhotoLayout = (ViewGroup) view.findViewById(R.id.clone_photo_layout);
        this.mVideoLayout = (ViewGroup) view.findViewById(R.id.clone_video_layout);
        setAccessibility(true);
        this.mPhotoLayout.setOnClickListener(this);
        this.mVideoLayout.setOnClickListener(this);
        FolmeUtils.touchItemScale(this.mPhotoLayout);
        FolmeUtils.touchItemScale(this.mVideoLayout);
        this.mPhotoButton = (ImageView) this.mPhotoLayout.findViewById(R.id.clone_photo_mode);
        this.mPhotoIndicator = (ImageView) this.mPhotoLayout.findViewById(R.id.clone_photo_mode_indicator);
        this.mVideoButton = (ImageView) this.mVideoLayout.findViewById(R.id.clone_video_mode);
        this.mVideoVideoView = (TextureVideoView) this.mVideoLayout.findViewById(R.id.clone_video_video_view);
        this.mVideoIndicator = (ImageView) this.mVideoLayout.findViewById(R.id.clone_video_mode_indicator);
        if (this.mIsSupportCloneCopyMode) {
            this.mCopyLayout = (ViewGroup) view.findViewById(R.id.clone_copy_layout);
            this.mCopyLayout.setOnClickListener(this);
            FolmeUtils.touchItemScale(this.mCopyLayout);
            this.mCopyButton = (ImageView) this.mCopyLayout.findViewById(R.id.clone_copy_mode);
            this.mCopyVideoView = (TextureVideoView) this.mCopyLayout.findViewById(R.id.clone_copy_video_view);
            this.mCopyIndicator = (ImageView) this.mCopyLayout.findViewById(R.id.clone_copy_mode_indicator);
        }
    }

    public boolean isShow() {
        return isAdded() && getView().getVisibility() == 0;
    }

    public boolean onBackEvent(int i) {
        return false;
    }

    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.clone_copy_layout) {
            onCopyModeClick(true);
        } else if (id == R.id.clone_photo_layout) {
            onPhotoModeClick(true);
        } else if (id == R.id.clone_video_layout) {
            onVideoModeClick(true);
        }
    }

    public void onCreate(@Nullable Bundle bundle) {
        super.onCreate(bundle);
        this.mIsSupportCloneCopyMode = C0122O00000o.instance().OO0oOoo();
    }

    public void onResume() {
        super.onResume();
        selectLastModule();
    }

    public void onStop() {
        super.onStop();
        TextureVideoView textureVideoView = this.mVideoVideoView;
        if (textureVideoView != null) {
            stopPlay(textureVideoView);
        }
        TextureVideoView textureVideoView2 = this.mCopyVideoView;
        if (textureVideoView2 != null) {
            stopPlay(textureVideoView2);
        }
    }

    public void provideAnimateElement(int i, List list, int i2) {
        super.provideAnimateElement(i, list, i2);
        if (i != 210) {
            hide();
        }
    }

    /* access modifiers changed from: protected */
    public Animation provideEnterAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(161);
    }

    /* access modifiers changed from: protected */
    public Animation provideExitAnimation(int i) {
        return FragmentAnimationFactory.wrapperAnimation(162);
    }

    /* access modifiers changed from: protected */
    public void register(ModeCoordinator modeCoordinator) {
        super.register(modeCoordinator);
        modeCoordinator.attachProtocol(416, this);
        registerBackStack(modeCoordinator, this);
    }

    public void show(int i) {
    }

    public boolean startShot() {
        String str;
        Mode mode = this.mSelectedMode;
        String str2 = TAG;
        if (mode == null) {
            Log.w(str2, "startShot ignore, mSelectedMode is null");
            return false;
        }
        ConfigChanges configChanges = (ConfigChanges) ModeCoordinatorImpl.getInstance().getAttachProtocol(164);
        if (configChanges == null) {
            Log.w(str2, "startShot ignore, configChanges is null");
            return false;
        }
        Mode mode2 = this.mSelectedMode;
        if (mode2 == Mode.PHOTO) {
            str = CloneAttr.VALUE_START_PHOTO_CLICK;
        } else if (mode2 == Mode.VIDEO) {
            str = CloneAttr.VALUE_START_VIDEO_CLICK;
        } else {
            if (mode2 == Mode.MCOPY) {
                str = CloneAttr.VALUE_START_FREEZE_FRAME_CLICK;
            }
            configChanges.configClone(this.mSelectedMode, true);
            return true;
        }
        CameraStatUtils.trackCloneClick(str);
        configChanges.configClone(this.mSelectedMode, true);
        return true;
    }

    /* access modifiers changed from: protected */
    public void unRegister(ModeCoordinator modeCoordinator) {
        super.unRegister(modeCoordinator);
        modeCoordinator.detachProtocol(416, this);
        unRegisterBackStack(modeCoordinator, this);
    }
}
