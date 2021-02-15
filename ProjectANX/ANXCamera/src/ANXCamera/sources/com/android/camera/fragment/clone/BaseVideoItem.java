package com.android.camera.fragment.clone;

import android.graphics.Rect;
import android.view.View;
import com.android.camera.videoplayer.manager.VideoItem;
import com.android.camera.videoplayer.manager.VideoPlayerManager;
import com.android.camera.videoplayer.meta.CurrentItemMetaData;
import com.android.camera.videoplayer.ui.SimpleMainThreadMediaPlayerListener;
import com.android.camera.visibilityutils.items.ListItem;

public abstract class BaseVideoItem implements VideoItem, ListItem {
    private static final boolean SHOW_LOGS = false;
    private static final String TAG = "BaseVideoItem";
    private final Rect mCurrentViewRect = new Rect();
    private final VideoPlayerManager mVideoPlayerManager;

    protected BaseVideoItem(VideoPlayerManager videoPlayerManager) {
        this.mVideoPlayerManager = videoPlayerManager;
    }

    private boolean viewIsPartiallyHiddenBottom(int i) {
        int i2 = this.mCurrentViewRect.bottom;
        return i2 > 0 && i2 < i;
    }

    private boolean viewIsPartiallyHiddenTop() {
        return this.mCurrentViewRect.top > 0;
    }

    public View createView(View view, int i) {
        final VideoViewHolder videoViewHolder = new VideoViewHolder(view);
        view.setTag(videoViewHolder);
        videoViewHolder.mTextureView.addMediaPlayerListener(new SimpleMainThreadMediaPlayerListener() {
            public void onVideoPreparedMainThread() {
                videoViewHolder.mCover.setVisibility(4);
            }

            public void onVideoSizeChangedMainThread(int i, int i2) {
            }

            public void onVideoStoppedMainThread() {
                videoViewHolder.mCover.setVisibility(0);
            }
        });
        return view;
    }

    public void deactivate(View view, int i) {
        stopPlayback(this.mVideoPlayerManager);
    }

    public abstract String getContentDescription();

    public int getVisibilityPercents(View view) {
        int i;
        view.getLocalVisibleRect(this.mCurrentViewRect);
        int height = view.getHeight();
        if (viewIsPartiallyHiddenTop()) {
            i = height - this.mCurrentViewRect.top;
        } else if (!viewIsPartiallyHiddenBottom(height)) {
            return 100;
        } else {
            i = this.mCurrentViewRect.bottom;
        }
        return (i * 100) / height;
    }

    public void setActive(View view, int i) {
        playNewVideo(new CurrentItemMetaData(i, view), ((VideoViewHolder) view.getTag()).mTextureView, this.mVideoPlayerManager);
    }

    public abstract void update(int i, VideoViewHolder videoViewHolder, VideoPlayerManager videoPlayerManager);
}
