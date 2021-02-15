package com.android.camera.fragment.vv;

import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.airbnb.lottie.LottieAnimationView;
import com.android.camera.R;
import com.android.camera.animation.FolmeUtils;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseViewPagerFragment;
import com.android.camera.ui.TextureVideoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.RequestManager;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.Locale;

public class FragmentVVPreviewItem extends BaseViewPagerFragment implements OnClickListener {
    private OnClickListener mClickListener;
    private boolean mFirstPreviewItem;
    private RequestOptions mGlideOptions;
    private int mImageHeight;
    private int mImageWidth;
    private int mIndex;
    private boolean mIsPlaying;
    private ImageView mPreviewImage;
    private ProgressBar mPreviewProgressBar;
    private ImageView mPreviewStart;
    private LottieAnimationView mPreviewStateImage;
    private TextureVideoView mTextureVideoView;
    private boolean mTransitionHide;
    /* access modifiers changed from: private */
    public VVItem mVVItem;
    /* access modifiers changed from: private */
    public boolean mVisible;

    private String getDurationString(long j) {
        int floor = (int) Math.floor((double) (((float) j) / 1000.0f));
        return String.format(Locale.ENGLISH, "%02d", new Object[]{Integer.valueOf(Math.abs(floor))});
    }

    private void initView(View view) {
        this.mPreviewImage = (ImageView) view.findViewById(R.id.vv_preview_item_image);
        this.mPreviewImage.setTag(Integer.valueOf(this.mIndex));
        ImageView imageView = (ImageView) view.findViewById(R.id.vv_preview_item_collapsing);
        this.mPreviewStart = (ImageView) view.findViewById(R.id.vv_preview_item_preview);
        this.mPreviewStart.setOnClickListener(this);
        this.mTextureVideoView = (TextureVideoView) view.findViewById(R.id.vv_preview_texture_video);
        this.mPreviewStateImage = (LottieAnimationView) view.findViewById(R.id.vv_preview_item_state);
        this.mPreviewProgressBar = (ProgressBar) view.findViewById(R.id.vv_preview_item_loading);
        TextView textView = (TextView) view.findViewById(R.id.vv_preview_item_hint);
        view.setTag(Integer.valueOf(this.mIndex));
        imageView.setTag(Integer.valueOf(this.mIndex));
        imageView.setOnClickListener(this);
        if (this.mImageWidth > 0 && this.mImageHeight > 0) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) this.mPreviewImage.getLayoutParams();
            marginLayoutParams.width = this.mImageWidth;
            marginLayoutParams.height = this.mImageHeight;
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mTextureVideoView.getLayoutParams();
            marginLayoutParams2.width = this.mImageWidth;
            marginLayoutParams2.height = this.mImageHeight;
        }
        ViewCompat.setTransitionName(this.mPreviewImage, this.mVVItem.name);
        this.mTextureVideoView.setVisibility(4);
        this.mTextureVideoView.setOnClickListener(this);
        this.mTextureVideoView.setLoop(true);
        this.mGlideOptions = new RequestOptions();
        this.mGlideOptions.skipMemoryCache(false);
        this.mGlideOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        handleDownloadStateChanged(true);
        Resources resources = getResources();
        VVItem vVItem = this.mVVItem;
        String string = resources.getString(R.string.vv_duartion_hint, new Object[]{vVItem.name, Integer.valueOf(vVItem.getEssentialFragmentSize()), getDurationString(this.mVVItem.getTotalDuration())});
        textView.setText(string);
        this.mTextureVideoView.setContentDescription(string);
    }

    private void pausePlay() {
        if (this.mIsPlaying) {
            this.mIsPlaying = false;
            this.mTextureVideoView.pause();
        }
    }

    private void resumePlay() {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mTextureVideoView.resume();
        }
    }

    public VVItem getVVItem() {
        return this.mVVItem;
    }

    /* JADX WARNING: Removed duplicated region for block: B:16:0x007b  */
    /* JADX WARNING: Removed duplicated region for block: B:28:0x00e9  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void handleDownloadStateChanged(final boolean z) {
        int currentState;
        String str;
        RequestManager requestManager;
        if (isAdded()) {
            if (!this.mVVItem.isCloudItem() || this.mVVItem.getCurrentState() == 5 || this.mVVItem.getCurrentState() == 7) {
                this.mPreviewImage.setTag(null);
                this.mPreviewImage.setBackground(null);
                requestManager = Glide.with(getContext());
                str = this.mVVItem.coverPath;
            } else {
                int parseColor = Color.parseColor(this.mVVItem.placeholder);
                this.mPreviewImage.setImageDrawable(null);
                this.mPreviewImage.setBackgroundColor(parseColor);
                if (!TextUtils.isEmpty(this.mVVItem.iconUrl)) {
                    requestManager = Glide.with(getContext());
                    str = this.mVVItem.iconUrl;
                }
                currentState = this.mVVItem.getCurrentState();
                if (currentState != 0) {
                    this.mPreviewStart.setVisibility(8);
                    this.mPreviewImage.setOnClickListener(this);
                    this.mPreviewStateImage.setVisibility(0);
                    this.mPreviewStateImage.setImageResource(R.drawable.ic_vector_download_pro);
                    this.mPreviewProgressBar.setVisibility(8);
                } else if (currentState == 2) {
                    this.mPreviewStateImage.setVisibility(8);
                    this.mPreviewProgressBar.setVisibility(0);
                } else if (currentState == 7) {
                    this.mPreviewProgressBar.setVisibility(8);
                    this.mPreviewStateImage.setVisibility(8);
                } else if (currentState == 4) {
                    Toast.makeText(getContext(), R.string.live_music_network_exception, 0).show();
                } else if (currentState == 5) {
                    this.mPreviewStart.setOnClickListener(null);
                    this.mPreviewProgressBar.setVisibility(8);
                    if (this.mVVItem.isCloudItem()) {
                        this.mPreviewStateImage.setVisibility(0);
                        this.mPreviewStateImage.O0000O0o((int) R.raw.resource_anim_finish);
                        this.mPreviewStateImage.setScaleType(ScaleType.CENTER_CROP);
                        this.mPreviewStateImage.setProgress(1.0f);
                        this.mPreviewStateImage.O0000oO();
                        FolmeUtils.animateShrink(this.mPreviewStateImage, new Runnable() {
                            public void run() {
                                FragmentVVPreviewItem.this.mVVItem.setState(7);
                                if (!z && FragmentVVPreviewItem.this.mVisible) {
                                    FragmentVVPreviewItem.this.startPlay();
                                }
                            }
                        });
                    }
                }
            }
            requestManager.load(str).apply((BaseRequestOptions) this.mGlideOptions).into(this.mPreviewImage);
            currentState = this.mVVItem.getCurrentState();
            if (currentState != 0) {
            }
        }
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.vv_preview_item_collapsing /*2131297243*/:
                stopPlay();
                break;
            case R.id.vv_preview_item_image /*2131297245*/:
                break;
            default:
                return;
        }
        this.mClickListener.onClick(view);
    }

    @Nullable
    public View onCreateView(@NonNull LayoutInflater layoutInflater, @Nullable ViewGroup viewGroup, @Nullable Bundle bundle) {
        View inflate = layoutInflater.inflate(R.layout.fragment_vv_preview_item, viewGroup, false);
        initView(inflate);
        return inflate;
    }

    public void onPause() {
        super.onPause();
        stopPlay();
    }

    public void onResume() {
        super.onResume();
        if (this.mVisible && DataRepository.dataItemGlobal().getCurrentMode() == 209) {
            startPlay();
        }
    }

    public void onStop() {
        super.onStop();
        stopPlay();
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndJumpOut() {
        super.onViewCreatedAndJumpOut();
        this.mVisible = false;
        stopPlay();
    }

    /* access modifiers changed from: protected */
    public void onViewCreatedAndVisibleToUser(boolean z) {
        super.onViewCreatedAndVisibleToUser(z);
        this.mVisible = true;
        if (this.mFirstPreviewItem) {
            this.mFirstPreviewItem = false;
        }
        startPlay();
    }

    public void setData(int i, VVItem vVItem, int i2, int i3, OnClickListener onClickListener, int i4) {
        this.mIndex = i;
        this.mVVItem = vVItem;
        this.mImageWidth = i2;
        this.mImageHeight = i3;
        this.mClickListener = onClickListener;
        boolean z = false;
        this.mTransitionHide = Math.abs(i - i4) == 1;
        if (i == i4) {
            z = true;
        }
        this.mFirstPreviewItem = z;
    }

    public void startPlay() {
        if (!this.mIsPlaying) {
            if (!this.mVVItem.isCloudItem() || this.mVVItem.getCurrentState() == 7) {
                this.mIsPlaying = true;
                this.mTextureVideoView.setVideoPath(this.mVVItem.previewVideoPath);
                this.mTextureVideoView.setVisibility(0);
                this.mTextureVideoView.start(0);
            }
        }
    }

    public void stopPlay() {
        if (this.mIsPlaying || this.mTextureVideoView.isPlaying()) {
            this.mIsPlaying = false;
            this.mTextureVideoView.stop();
            this.mTextureVideoView.setVisibility(4);
        }
    }
}
