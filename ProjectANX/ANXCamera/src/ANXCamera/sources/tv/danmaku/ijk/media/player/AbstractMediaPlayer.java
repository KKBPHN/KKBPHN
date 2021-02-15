package tv.danmaku.ijk.media.player;

import tv.danmaku.ijk.media.player.IMediaPlayer.OnBufferingUpdateListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnCompletionListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnErrorListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnInfoListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnPreparedListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnSeekCompleteListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnTimedTextListener;
import tv.danmaku.ijk.media.player.IMediaPlayer.OnVideoSizeChangedListener;
import tv.danmaku.ijk.media.player.misc.IMediaDataSource;

public abstract class AbstractMediaPlayer implements IMediaPlayer {
    private OnBufferingUpdateListener mOnBufferingUpdateListener;
    private OnCompletionListener mOnCompletionListener;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private OnPreparedListener mOnPreparedListener;
    private OnSeekCompleteListener mOnSeekCompleteListener;
    private OnTimedTextListener mOnTimedTextListener;
    private OnVideoSizeChangedListener mOnVideoSizeChangedListener;

    /* access modifiers changed from: protected */
    public final void notifyOnBufferingUpdate(int i) {
        OnBufferingUpdateListener onBufferingUpdateListener = this.mOnBufferingUpdateListener;
        if (onBufferingUpdateListener != null) {
            onBufferingUpdateListener.onBufferingUpdate(this, i);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnCompletion() {
        OnCompletionListener onCompletionListener = this.mOnCompletionListener;
        if (onCompletionListener != null) {
            onCompletionListener.onCompletion(this);
        }
    }

    /* access modifiers changed from: protected */
    public final boolean notifyOnError(int i, int i2) {
        OnErrorListener onErrorListener = this.mOnErrorListener;
        return onErrorListener != null && onErrorListener.onError(this, i, i2);
    }

    /* access modifiers changed from: protected */
    public final boolean notifyOnInfo(int i, int i2) {
        OnInfoListener onInfoListener = this.mOnInfoListener;
        return onInfoListener != null && onInfoListener.onInfo(this, i, i2);
    }

    /* access modifiers changed from: protected */
    public final void notifyOnPrepared() {
        OnPreparedListener onPreparedListener = this.mOnPreparedListener;
        if (onPreparedListener != null) {
            onPreparedListener.onPrepared(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnSeekComplete() {
        OnSeekCompleteListener onSeekCompleteListener = this.mOnSeekCompleteListener;
        if (onSeekCompleteListener != null) {
            onSeekCompleteListener.onSeekComplete(this);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnTimedText(IjkTimedText ijkTimedText) {
        OnTimedTextListener onTimedTextListener = this.mOnTimedTextListener;
        if (onTimedTextListener != null) {
            onTimedTextListener.onTimedText(this, ijkTimedText);
        }
    }

    /* access modifiers changed from: protected */
    public final void notifyOnVideoSizeChanged(int i, int i2, int i3, int i4) {
        OnVideoSizeChangedListener onVideoSizeChangedListener = this.mOnVideoSizeChangedListener;
        if (onVideoSizeChangedListener != null) {
            onVideoSizeChangedListener.onVideoSizeChanged(this, i, i2, i3, i4);
        }
    }

    public void resetListeners() {
        this.mOnPreparedListener = null;
        this.mOnBufferingUpdateListener = null;
        this.mOnCompletionListener = null;
        this.mOnSeekCompleteListener = null;
        this.mOnVideoSizeChangedListener = null;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mOnTimedTextListener = null;
    }

    public void setDataSource(IMediaDataSource iMediaDataSource) {
        throw new UnsupportedOperationException();
    }

    public final void setOnBufferingUpdateListener(OnBufferingUpdateListener onBufferingUpdateListener) {
        this.mOnBufferingUpdateListener = onBufferingUpdateListener;
    }

    public final void setOnCompletionListener(OnCompletionListener onCompletionListener) {
        this.mOnCompletionListener = onCompletionListener;
    }

    public final void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public final void setOnInfoListener(OnInfoListener onInfoListener) {
        this.mOnInfoListener = onInfoListener;
    }

    public final void setOnPreparedListener(OnPreparedListener onPreparedListener) {
        this.mOnPreparedListener = onPreparedListener;
    }

    public final void setOnSeekCompleteListener(OnSeekCompleteListener onSeekCompleteListener) {
        this.mOnSeekCompleteListener = onSeekCompleteListener;
    }

    public final void setOnTimedTextListener(OnTimedTextListener onTimedTextListener) {
        this.mOnTimedTextListener = onTimedTextListener;
    }

    public final void setOnVideoSizeChangedListener(OnVideoSizeChangedListener onVideoSizeChangedListener) {
        this.mOnVideoSizeChangedListener = onVideoSizeChangedListener;
    }
}
