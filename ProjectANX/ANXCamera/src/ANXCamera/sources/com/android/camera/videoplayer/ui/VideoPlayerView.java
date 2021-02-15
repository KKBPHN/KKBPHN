package com.android.camera.videoplayer.ui;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.os.Looper;
import android.preference.PreferenceManager;
import android.util.AttributeSet;
import android.view.TextureView.SurfaceTextureListener;
import android.view.View;
import com.android.camera.videoplayer.ui.MediaPlayerWrapper.MainThreadMediaPlayerListener;
import com.android.camera.videoplayer.ui.MediaPlayerWrapper.State;
import com.android.camera.videoplayer.ui.MediaPlayerWrapper.VideoStateListener;
import com.android.camera.videoplayer.ui.ScalableTextureView.ScaleType;
import com.android.camera.videoplayer.utils.HandlerThreadExtension;
import com.android.camera.videoplayer.utils.Logger;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class VideoPlayerView extends ScalableTextureView implements SurfaceTextureListener, MainThreadMediaPlayerListener, VideoStateListener {
    private static final String IS_VIDEO_MUTED = "IS_VIDEO_MUTED";
    private static final boolean SHOW_LOGS = false;
    private String TAG;
    private AssetFileDescriptor mAssetFileDescriptor;
    private SurfaceTextureListener mLocalSurfaceTextureListener;
    /* access modifiers changed from: private */
    public MediaPlayerWrapper mMediaPlayer;
    /* access modifiers changed from: private */
    public BackgroundThreadMediaPlayerListener mMediaPlayerListenerBackgroundThread;
    private final Set mMediaPlayerMainThreadListeners = new HashSet();
    private String mPath;
    /* access modifiers changed from: private */
    public final ReadyForPlaybackIndicator mReadyForPlaybackIndicator = new ReadyForPlaybackIndicator();
    private final Runnable mVideoCompletionBackgroundThreadRunnable = new Runnable() {
        public void run() {
            VideoPlayerView.this.mMediaPlayerListenerBackgroundThread.onVideoSizeChangedBackgroundThread(VideoPlayerView.this.getContentHeight().intValue(), VideoPlayerView.this.getContentWidth().intValue());
        }
    };
    private final Runnable mVideoPreparedBackgroundThreadRunnable = new Runnable() {
        public void run() {
            VideoPlayerView.this.mMediaPlayerListenerBackgroundThread.onVideoPreparedBackgroundThread();
        }
    };
    private final Runnable mVideoSizeAvailableRunnable = new Runnable() {
        public void run() {
            synchronized (VideoPlayerView.this.mReadyForPlaybackIndicator) {
                VideoPlayerView.this.mReadyForPlaybackIndicator.setVideoSize(VideoPlayerView.this.getContentHeight(), VideoPlayerView.this.getContentWidth());
                if (VideoPlayerView.this.mReadyForPlaybackIndicator.isReadyForPlayback()) {
                    VideoPlayerView.this.mReadyForPlaybackIndicator.notifyAll();
                }
            }
            if (VideoPlayerView.this.mMediaPlayerListenerBackgroundThread != null) {
                VideoPlayerView.this.mMediaPlayerListenerBackgroundThread.onVideoSizeChangedBackgroundThread(VideoPlayerView.this.getContentHeight().intValue(), VideoPlayerView.this.getContentWidth().intValue());
            }
        }
    };
    private VideoStateListener mVideoStateListener;
    private HandlerThreadExtension mViewHandlerBackgroundThread;

    public interface BackgroundThreadMediaPlayerListener {
        void onErrorBackgroundThread(int i, int i2);

        void onVideoCompletionBackgroundThread();

        void onVideoPreparedBackgroundThread();

        void onVideoSizeChangedBackgroundThread(int i, int i2);
    }

    public interface PlaybackStartedListener {
        void onPlaybackStarted();
    }

    public VideoPlayerView(Context context) {
        super(context);
        initView();
    }

    public VideoPlayerView(Context context, AttributeSet attributeSet) {
        super(context, attributeSet);
        initView();
    }

    public VideoPlayerView(Context context, AttributeSet attributeSet, int i) {
        super(context, attributeSet, i);
        initView();
    }

    @TargetApi(21)
    public VideoPlayerView(Context context, AttributeSet attributeSet, int i, int i2) {
        super(context, attributeSet, i, i2);
        initView();
    }

    private void checkThread() {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            throw new RuntimeException("cannot be in main thread");
        }
    }

    private void initView() {
        if (!isInEditMode()) {
            StringBuilder sb = new StringBuilder();
            sb.append("");
            sb.append(this);
            this.TAG = sb.toString();
            setScaleType(ScaleType.CENTER_CROP);
            super.setSurfaceTextureListener(this);
        }
    }

    private boolean isVideoSizeAvailable() {
        return (getContentHeight() == null || getContentWidth() == null) ? false : true;
    }

    private void notifyOnErrorMainThread(int i, int i2) {
        ArrayList<MainThreadMediaPlayerListener> arrayList;
        synchronized (this.mMediaPlayerMainThreadListeners) {
            arrayList = new ArrayList<>(this.mMediaPlayerMainThreadListeners);
        }
        for (MainThreadMediaPlayerListener onErrorMainThread : arrayList) {
            onErrorMainThread.onErrorMainThread(i, i2);
        }
    }

    private void notifyOnVideoCompletionMainThread() {
        ArrayList<MainThreadMediaPlayerListener> arrayList;
        synchronized (this.mMediaPlayerMainThreadListeners) {
            arrayList = new ArrayList<>(this.mMediaPlayerMainThreadListeners);
        }
        for (MainThreadMediaPlayerListener onVideoCompletionMainThread : arrayList) {
            onVideoCompletionMainThread.onVideoCompletionMainThread();
        }
    }

    private void notifyOnVideoPreparedMainThread() {
        ArrayList<MainThreadMediaPlayerListener> arrayList;
        synchronized (this.mMediaPlayerMainThreadListeners) {
            arrayList = new ArrayList<>(this.mMediaPlayerMainThreadListeners);
        }
        for (MainThreadMediaPlayerListener onVideoPreparedMainThread : arrayList) {
            onVideoPreparedMainThread.onVideoPreparedMainThread();
        }
    }

    private void notifyOnVideoSizeChangedMainThread(int i, int i2) {
        ArrayList<MainThreadMediaPlayerListener> arrayList;
        synchronized (this.mMediaPlayerMainThreadListeners) {
            arrayList = new ArrayList<>(this.mMediaPlayerMainThreadListeners);
        }
        for (MainThreadMediaPlayerListener onVideoSizeChangedMainThread : arrayList) {
            onVideoSizeChangedMainThread.onVideoSizeChangedMainThread(i, i2);
        }
    }

    private void notifyOnVideoStopped() {
        ArrayList<MainThreadMediaPlayerListener> arrayList;
        synchronized (this.mMediaPlayerMainThreadListeners) {
            arrayList = new ArrayList<>(this.mMediaPlayerMainThreadListeners);
        }
        for (MainThreadMediaPlayerListener onVideoStoppedMainThread : arrayList) {
            onVideoStoppedMainThread.onVideoStoppedMainThread();
        }
    }

    private void notifyTextureAvailable() {
        this.mViewHandlerBackgroundThread.post(new Runnable() {
            public void run() {
                synchronized (VideoPlayerView.this.mReadyForPlaybackIndicator) {
                    if (VideoPlayerView.this.mMediaPlayer != null) {
                        VideoPlayerView.this.mMediaPlayer.setSurfaceTexture(VideoPlayerView.this.getSurfaceTexture());
                    } else {
                        VideoPlayerView.this.mReadyForPlaybackIndicator.setVideoSize(null, null);
                    }
                    VideoPlayerView.this.mReadyForPlaybackIndicator.setSurfaceTextureAvailable(true);
                    if (VideoPlayerView.this.mReadyForPlaybackIndicator.isReadyForPlayback()) {
                        VideoPlayerView.this.mReadyForPlaybackIndicator.notifyAll();
                    }
                }
            }
        });
    }

    private void onVideoSizeAvailable() {
        updateTextureViewSize();
        if (isAttachedToWindow()) {
            this.mViewHandlerBackgroundThread.post(this.mVideoSizeAvailableRunnable);
        }
    }

    private void printErrorExtra(int i) {
        if (i != -1010 && i != -1007 && i != -1004) {
        }
    }

    private static String visibilityStr(int i) {
        if (i == 0) {
            return "VISIBLE";
        }
        if (i == 4) {
            return "INVISIBLE";
        }
        if (i == 8) {
            return "GONE";
        }
        throw new RuntimeException("unexpected");
    }

    public void addMediaPlayerListener(MainThreadMediaPlayerListener mainThreadMediaPlayerListener) {
        synchronized (this.mMediaPlayerMainThreadListeners) {
            this.mMediaPlayerMainThreadListeners.add(mainThreadMediaPlayerListener);
        }
    }

    public void clearPlayerInstance() {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mReadyForPlaybackIndicator.setVideoSize(null, null);
            this.mMediaPlayer.clearAll();
            this.mMediaPlayer = null;
        }
    }

    public void createNewPlayerInstance() {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer = new MediaPlayerWrapperImpl();
            this.mMediaPlayer.setLooping(true);
            this.mReadyForPlaybackIndicator.setVideoSize(null, null);
            this.mReadyForPlaybackIndicator.setFailedToPrepareUiForPlayback(false);
            if (this.mReadyForPlaybackIndicator.isSurfaceTextureAvailable()) {
                this.mMediaPlayer.setSurfaceTexture(getSurfaceTexture());
            }
            this.mMediaPlayer.setMainThreadMediaPlayerListener(this);
            this.mMediaPlayer.setVideoStateListener(this);
        }
    }

    public AssetFileDescriptor getAssetFileDescriptorDataSource() {
        return this.mAssetFileDescriptor;
    }

    public State getCurrentState() {
        State currentState;
        synchronized (this.mReadyForPlaybackIndicator) {
            currentState = this.mMediaPlayer.getCurrentState();
        }
        return currentState;
    }

    public int getDuration() {
        int duration;
        synchronized (this.mReadyForPlaybackIndicator) {
            duration = this.mMediaPlayer.getDuration();
        }
        return duration;
    }

    public String getVideoUrlDataSource() {
        return this.mPath;
    }

    public boolean isAllVideoMute() {
        return PreferenceManager.getDefaultSharedPreferences(getContext()).getBoolean(IS_VIDEO_MUTED, false);
    }

    public boolean isAttachedToWindow() {
        return this.mViewHandlerBackgroundThread != null;
    }

    public void muteVideo() {
        synchronized (this.mReadyForPlaybackIndicator) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(IS_VIDEO_MUTED, true).commit();
            this.mMediaPlayer.setVolume(0.0f, 0.0f);
        }
    }

    /* access modifiers changed from: protected */
    public void onAttachedToWindow() {
        super.onAttachedToWindow();
        if (!isInEditMode()) {
            this.mViewHandlerBackgroundThread = new HandlerThreadExtension(this.TAG, false);
            this.mViewHandlerBackgroundThread.startThread();
        }
    }

    public void onBufferingUpdateMainThread(int i) {
    }

    /* access modifiers changed from: protected */
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (!isInEditMode()) {
            this.mViewHandlerBackgroundThread.postQuit();
            this.mViewHandlerBackgroundThread = null;
        }
    }

    public void onErrorMainThread(final int i, final int i2) {
        if (i == 1 || i == 100) {
            printErrorExtra(i2);
        }
        notifyOnErrorMainThread(i, i2);
        if (this.mMediaPlayerListenerBackgroundThread != null) {
            this.mViewHandlerBackgroundThread.post(new Runnable() {
                public void run() {
                    VideoPlayerView.this.mMediaPlayerListenerBackgroundThread.onErrorBackgroundThread(i, i2);
                }
            });
        }
    }

    public void onSurfaceTextureAvailable(SurfaceTexture surfaceTexture, int i, int i2) {
        SurfaceTextureListener surfaceTextureListener = this.mLocalSurfaceTextureListener;
        if (surfaceTextureListener != null) {
            surfaceTextureListener.onSurfaceTextureAvailable(surfaceTexture, i, i2);
        }
        notifyTextureAvailable();
    }

    public boolean onSurfaceTextureDestroyed(SurfaceTexture surfaceTexture) {
        SurfaceTextureListener surfaceTextureListener = this.mLocalSurfaceTextureListener;
        if (surfaceTextureListener != null) {
            surfaceTextureListener.onSurfaceTextureDestroyed(surfaceTexture);
        }
        if (isAttachedToWindow()) {
            this.mViewHandlerBackgroundThread.post(new Runnable() {
                public void run() {
                    synchronized (VideoPlayerView.this.mReadyForPlaybackIndicator) {
                        VideoPlayerView.this.mReadyForPlaybackIndicator.setSurfaceTextureAvailable(false);
                        VideoPlayerView.this.mReadyForPlaybackIndicator.notifyAll();
                    }
                }
            });
        }
        surfaceTexture.release();
        return false;
    }

    public void onSurfaceTextureSizeChanged(SurfaceTexture surfaceTexture, int i, int i2) {
        SurfaceTextureListener surfaceTextureListener = this.mLocalSurfaceTextureListener;
        if (surfaceTextureListener != null) {
            surfaceTextureListener.onSurfaceTextureSizeChanged(surfaceTexture, i, i2);
        }
    }

    public void onSurfaceTextureUpdated(SurfaceTexture surfaceTexture) {
        SurfaceTextureListener surfaceTextureListener = this.mLocalSurfaceTextureListener;
        if (surfaceTextureListener != null) {
            surfaceTextureListener.onSurfaceTextureUpdated(surfaceTexture);
        }
    }

    public void onVideoCompletionMainThread() {
        notifyOnVideoCompletionMainThread();
        if (this.mMediaPlayerListenerBackgroundThread != null) {
            this.mViewHandlerBackgroundThread.post(this.mVideoCompletionBackgroundThreadRunnable);
        }
    }

    public void onVideoPlayTimeChanged(int i) {
    }

    public void onVideoPreparedMainThread() {
        notifyOnVideoPreparedMainThread();
        if (this.mMediaPlayerListenerBackgroundThread != null) {
            this.mViewHandlerBackgroundThread.post(this.mVideoPreparedBackgroundThreadRunnable);
        }
    }

    public void onVideoSizeChangedMainThread(int i, int i2) {
        if (i == 0 || i2 == 0) {
            synchronized (this.mReadyForPlaybackIndicator) {
                this.mReadyForPlaybackIndicator.setFailedToPrepareUiForPlayback(true);
                this.mReadyForPlaybackIndicator.notifyAll();
            }
        } else {
            setContentWidth(i);
            setContentHeight(i2);
            onVideoSizeAvailable();
        }
        notifyOnVideoSizeChangedMainThread(i, i2);
    }

    public void onVideoStoppedMainThread() {
        notifyOnVideoStopped();
    }

    /* access modifiers changed from: protected */
    public void onVisibilityChanged(View view, int i) {
        super.onVisibilityChanged(view, i);
        if (!isInEditMode() && i != 0) {
            if (i == 4 || i == 8) {
                synchronized (this.mReadyForPlaybackIndicator) {
                    this.mReadyForPlaybackIndicator.notifyAll();
                }
            }
        }
    }

    public void pause() {
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer.pause();
        }
    }

    public void prepare() {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer.prepare();
        }
    }

    public void release() {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer.release();
        }
    }

    public void reset() {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer.reset();
        }
    }

    public void setBackgroundThreadMediaPlayerListener(BackgroundThreadMediaPlayerListener backgroundThreadMediaPlayerListener) {
        this.mMediaPlayerListenerBackgroundThread = backgroundThreadMediaPlayerListener;
    }

    public void setDataSource(AssetFileDescriptor assetFileDescriptor) {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            try {
                this.mMediaPlayer.setDataSource(assetFileDescriptor);
                this.mAssetFileDescriptor = assetFileDescriptor;
            } catch (IOException e) {
                Logger.d(this.TAG, e.getMessage());
                throw new RuntimeException(e);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setDataSource(String str) {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            try {
                this.mMediaPlayer.setDataSource(str);
                this.mPath = str;
            } catch (IOException e) {
                Logger.d(this.TAG, e.getMessage());
                throw new RuntimeException(e);
            } catch (Throwable th) {
                throw th;
            }
        }
    }

    public void setOnVideoStateChangedListener(VideoStateListener videoStateListener) {
        this.mVideoStateListener = videoStateListener;
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer.setVideoStateListener(videoStateListener);
        }
    }

    public final void setSurfaceTextureListener(SurfaceTextureListener surfaceTextureListener) {
        this.mLocalSurfaceTextureListener = surfaceTextureListener;
    }

    public void start() {
        MediaPlayerWrapper mediaPlayerWrapper;
        synchronized (this.mReadyForPlaybackIndicator) {
            if (this.mReadyForPlaybackIndicator.isReadyForPlayback()) {
                mediaPlayerWrapper = this.mMediaPlayer;
            } else if (!this.mReadyForPlaybackIndicator.isFailedToPrepareUiForPlayback()) {
                try {
                    this.mReadyForPlaybackIndicator.wait();
                    if (this.mReadyForPlaybackIndicator.isReadyForPlayback()) {
                        mediaPlayerWrapper = this.mMediaPlayer;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            mediaPlayerWrapper.start();
        }
    }

    public void stop() {
        checkThread();
        synchronized (this.mReadyForPlaybackIndicator) {
            this.mMediaPlayer.stop();
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(VideoPlayerView.class.getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        return sb.toString();
    }

    public void unMuteVideo() {
        synchronized (this.mReadyForPlaybackIndicator) {
            PreferenceManager.getDefaultSharedPreferences(getContext()).edit().putBoolean(IS_VIDEO_MUTED, false).commit();
            this.mMediaPlayer.setVolume(1.0f, 1.0f);
        }
    }
}
