package com.android.camera.fragment.film;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.ViewCompat;
import com.android.camera.R;
import com.android.camera.data.DataRepository;
import com.android.camera.fragment.BaseViewPagerFragment;
import com.android.camera.ui.TextureVideoView;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.BaseRequestOptions;
import com.bumptech.glide.request.RequestOptions;
import java.util.Locale;

public class FragmentFilmPreviewItem extends BaseViewPagerFragment implements OnClickListener {
    private OnClickListener mClickListener;
    private FilmItem mFilmItem;
    private boolean mFirstPreviewItem;
    private RequestOptions mGlideOptions;
    private int mImageHeight;
    private int mImageWidth;
    private int mIndex;
    private boolean mIsPlaying;
    private ImageView mPreviewStart;
    private TextureVideoView mTextureVideoView;
    private boolean mTransitionHide;
    private boolean mVisible;

    private String getDurationString(long j) {
        int floor = (int) Math.floor((double) (((float) j) / 1000.0f));
        return String.format(Locale.ENGLISH, "%02d", new Object[]{Integer.valueOf(Math.abs(floor))});
    }

    private void initView(View view) {
        ImageView imageView = (ImageView) view.findViewById(R.id.vv_preview_item_image);
        ImageView imageView2 = (ImageView) view.findViewById(R.id.vv_preview_item_collapsing);
        this.mPreviewStart = (ImageView) view.findViewById(R.id.vv_preview_item_preview);
        this.mPreviewStart.setOnClickListener(this);
        this.mTextureVideoView = (TextureVideoView) view.findViewById(R.id.vv_preview_texture_video);
        TextView textView = (TextView) view.findViewById(R.id.vv_preview_item_hint);
        view.setTag(Integer.valueOf(this.mIndex));
        imageView2.setTag(Integer.valueOf(this.mIndex));
        imageView2.setOnClickListener(this);
        if (this.mImageWidth > 0 && this.mImageHeight > 0) {
            MarginLayoutParams marginLayoutParams = (MarginLayoutParams) imageView.getLayoutParams();
            marginLayoutParams.width = this.mImageWidth;
            marginLayoutParams.height = this.mImageHeight;
            MarginLayoutParams marginLayoutParams2 = (MarginLayoutParams) this.mTextureVideoView.getLayoutParams();
            marginLayoutParams2.width = this.mImageWidth;
            marginLayoutParams2.height = this.mImageHeight;
        }
        ViewCompat.setTransitionName(imageView, this.mFilmItem.id);
        this.mTextureVideoView.setVisibility(4);
        this.mTextureVideoView.setOnClickListener(this);
        this.mTextureVideoView.setLoop(true);
        this.mGlideOptions = new RequestOptions();
        this.mGlideOptions.skipMemoryCache(false);
        this.mGlideOptions.diskCacheStrategy(DiskCacheStrategy.NONE);
        Glide.with(getContext()).load(this.mFilmItem.coverPath).apply((BaseRequestOptions) this.mGlideOptions).into(imageView);
        textView.setText(this.mFilmItem.getName());
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

    private void startPlay() {
        if (!this.mIsPlaying) {
            this.mIsPlaying = true;
            this.mTextureVideoView.setVideoPath(this.mFilmItem.previewVideoPath);
            this.mTextureVideoView.setVisibility(0);
            this.mTextureVideoView.start(0);
        }
    }

    private void stopPlay() {
        if (this.mIsPlaying) {
            this.mIsPlaying = false;
            this.mTextureVideoView.stop();
            this.mTextureVideoView.setVisibility(4);
        }
    }

    public void onClick(View view) {
        if (view.getId() == R.id.vv_preview_item_collapsing) {
            stopPlay();
            this.mClickListener.onClick(view);
        }
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
        if (this.mVisible && DataRepository.dataItemGlobal().getCurrentMode() == 211) {
            startPlay();
        }
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

    public void setData(int i, FilmItem filmItem, int i2, int i3, OnClickListener onClickListener, int i4) {
        this.mIndex = i;
        this.mFilmItem = filmItem;
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
}
