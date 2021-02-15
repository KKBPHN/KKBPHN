package com.android.camera.videoplayer.ui;

import android.content.res.AssetFileDescriptor;
import android.graphics.SurfaceTexture;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnBufferingUpdateListener;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnVideoSizeChangedListener;
import android.os.Handler;
import android.os.Looper;
import android.view.Surface;
import com.android.camera.videoplayer.utils.Logger;
import java.io.IOException;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;
import tv.danmaku.ijk.media.player.IMediaPlayer;

public abstract class MediaPlayerWrapper implements OnErrorListener, OnBufferingUpdateListener, OnInfoListener, OnCompletionListener, OnVideoSizeChangedListener {
    public static final int POSITION_UPDATE_NOTIFYING_PERIOD = 1000;
    private static final boolean SHOW_LOGS = false;
    private String TAG;
    private ScheduledFuture mFuture;
    /* access modifiers changed from: private */
    public MainThreadMediaPlayerListener mListener;
    private final Handler mMainThreadHandler = new Handler(Looper.getMainLooper());
    private final MediaPlayer mMediaPlayer;
    private final Runnable mNotifyPositionUpdateRunnable = new Runnable() {
        public void run() {
            MediaPlayerWrapper.this.notifyPositionUpdated();
        }
    };
    private final Runnable mOnVideoPreparedMessage = new Runnable() {
        public void run() {
            MediaPlayerWrapper.this.mListener.onVideoPreparedMainThread();
        }
    };
    private final Runnable mOnVideoStopMessage = new Runnable() {
        public void run() {
            MediaPlayerWrapper.this.mListener.onVideoStoppedMainThread();
        }
    };
    private ScheduledExecutorService mPositionUpdateNotifier = Executors.newScheduledThreadPool(1);
    private final AtomicReference mState = new AtomicReference();
    private Surface mSurface;
    private VideoStateListener mVideoStateListener;

    /* renamed from: com.android.camera.videoplayer.ui.MediaPlayerWrapper$5 reason: invalid class name */
    /* synthetic */ class AnonymousClass5 {
        static final /* synthetic */ int[] $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State = new int[State.values().length];

        /* JADX WARNING: Can't wrap try/catch for region: R(20:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|(3:19|20|22)) */
        /* JADX WARNING: Can't wrap try/catch for region: R(22:0|1|2|3|4|5|6|7|8|9|10|11|12|13|14|15|16|17|18|19|20|22) */
        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:11:0x0040 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:13:0x004b */
        /* JADX WARNING: Missing exception handler attribute for start block: B:15:0x0056 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:17:0x0062 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:19:0x006e */
        /* JADX WARNING: Missing exception handler attribute for start block: B:3:0x0014 */
        /* JADX WARNING: Missing exception handler attribute for start block: B:5:0x001f */
        /* JADX WARNING: Missing exception handler attribute for start block: B:7:0x002a */
        /* JADX WARNING: Missing exception handler attribute for start block: B:9:0x0035 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        static {
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.STOPPED.ordinal()] = 1;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.INITIALIZED.ordinal()] = 2;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.IDLE.ordinal()] = 3;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PREPARING.ordinal()] = 4;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PREPARED.ordinal()] = 5;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.STARTED.ordinal()] = 6;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PAUSED.ordinal()] = 7;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.PLAYBACK_COMPLETED.ordinal()] = 8;
            $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.END.ordinal()] = 9;
            try {
                $SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[State.ERROR.ordinal()] = 10;
            } catch (NoSuchFieldError unused) {
            }
        }
    }

    public interface MainThreadMediaPlayerListener {
        void onBufferingUpdateMainThread(int i);

        void onErrorMainThread(int i, int i2);

        void onVideoCompletionMainThread();

        void onVideoPreparedMainThread();

        void onVideoSizeChangedMainThread(int i, int i2);

        void onVideoStoppedMainThread();
    }

    public enum State {
        IDLE,
        INITIALIZED,
        PREPARING,
        PREPARED,
        STARTED,
        PAUSED,
        STOPPED,
        PLAYBACK_COMPLETED,
        END,
        ERROR
    }

    public interface VideoStateListener {
        void onVideoPlayTimeChanged(int i);
    }

    protected MediaPlayerWrapper(MediaPlayer mediaPlayer) {
        StringBuilder sb = new StringBuilder();
        sb.append("");
        sb.append(this);
        this.TAG = sb.toString();
        if (Looper.myLooper() == null) {
            this.mMediaPlayer = mediaPlayer;
            this.mState.set(State.IDLE);
            this.mMediaPlayer.setOnVideoSizeChangedListener(this);
            this.mMediaPlayer.setOnCompletionListener(this);
            this.mMediaPlayer.setOnErrorListener(this);
            this.mMediaPlayer.setOnBufferingUpdateListener(this);
            this.mMediaPlayer.setOnInfoListener(this);
            return;
        }
        throw new RuntimeException("myLooper not null, a bug in some MediaPlayer implementation cause that listeners are not called at all. Please use a thread without Looper");
    }

    private boolean inUiThread() {
        return Thread.currentThread().getId() == 1;
    }

    /* access modifiers changed from: private */
    public void notifyPositionUpdated() {
        synchronized (this.mState) {
            if (this.mVideoStateListener != null && this.mState.get() == State.STARTED) {
                this.mVideoStateListener.onVideoPlayTimeChanged(this.mMediaPlayer.getCurrentPosition());
            }
        }
    }

    private void onPrepareError(IOException iOException) {
        this.mState.set(State.ERROR);
        MainThreadMediaPlayerListener mainThreadMediaPlayerListener = this.mListener;
        if (mainThreadMediaPlayerListener != null) {
            mainThreadMediaPlayerListener.onErrorMainThread(1, IMediaPlayer.MEDIA_ERROR_IO);
        }
        if (this.mListener != null) {
            this.mMainThreadHandler.post(new Runnable() {
                public void run() {
                    MediaPlayerWrapper.this.mListener.onErrorMainThread(1, IMediaPlayer.MEDIA_ERROR_IO);
                }
            });
        }
    }

    public static int positionToPercent(int i, int i2) {
        return Math.round((((float) i) / ((float) i2)) * 100.0f);
    }

    private boolean positionUpdaterIsWorking() {
        return this.mFuture != null;
    }

    private void printInfo(int i) {
        if (i != 1 && i != 3 && i != 901) {
            switch (i) {
                case 700:
                case 701:
                case IMediaPlayer.MEDIA_INFO_BUFFERING_END /*702*/:
                    return;
                default:
                    switch (i) {
                    }
                    return;
            }
        }
    }

    private void startPositionUpdateNotifier() {
        this.mFuture = this.mPositionUpdateNotifier.scheduleAtFixedRate(this.mNotifyPositionUpdateRunnable, 0, 1000, TimeUnit.MILLISECONDS);
    }

    private void stopPositionUpdateNotifier() {
        this.mFuture.cancel(true);
        this.mFuture = null;
    }

    public void clearAll() {
        synchronized (this.mState) {
            this.mMediaPlayer.setOnVideoSizeChangedListener(null);
            this.mMediaPlayer.setOnCompletionListener(null);
            this.mMediaPlayer.setOnErrorListener(null);
            this.mMediaPlayer.setOnBufferingUpdateListener(null);
            this.mMediaPlayer.setOnInfoListener(null);
        }
    }

    public int getCurrentPosition() {
        return this.mMediaPlayer.getCurrentPosition();
    }

    public State getCurrentState() {
        State state;
        synchronized (this.mState) {
            state = (State) this.mState.get();
        }
        return state;
    }

    public int getDuration() {
        int i;
        synchronized (this.mState) {
            i = 0;
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 5:
                case 6:
                case 7:
                case 8:
                    i = this.mMediaPlayer.getDuration();
                    break;
                case 2:
                case 3:
                case 4:
                case 9:
                case 10:
                    break;
            }
        }
        return i;
    }

    public int getVideoHeight() {
        return this.mMediaPlayer.getVideoHeight();
    }

    public int getVideoWidth() {
        return this.mMediaPlayer.getVideoWidth();
    }

    public boolean isPlaying() {
        return this.mMediaPlayer.isPlaying();
    }

    public boolean isReadyForPlayback() {
        boolean z;
        synchronized (this.mState) {
            z = false;
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 9:
                case 10:
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    z = true;
                    break;
            }
        }
        return z;
    }

    public void onBufferingUpdate(MediaPlayer mediaPlayer, int i) {
        MainThreadMediaPlayerListener mainThreadMediaPlayerListener = this.mListener;
        if (mainThreadMediaPlayerListener != null) {
            mainThreadMediaPlayerListener.onBufferingUpdateMainThread(i);
        }
    }

    public void onCompletion(MediaPlayer mediaPlayer) {
        synchronized (this.mState) {
            this.mState.set(State.PLAYBACK_COMPLETED);
        }
        MainThreadMediaPlayerListener mainThreadMediaPlayerListener = this.mListener;
        if (mainThreadMediaPlayerListener != null) {
            mainThreadMediaPlayerListener.onVideoCompletionMainThread();
        }
    }

    public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
        synchronized (this.mState) {
            this.mState.set(State.ERROR);
        }
        if (positionUpdaterIsWorking()) {
            stopPositionUpdateNotifier();
        }
        MainThreadMediaPlayerListener mainThreadMediaPlayerListener = this.mListener;
        if (mainThreadMediaPlayerListener != null) {
            mainThreadMediaPlayerListener.onErrorMainThread(i, i2);
        }
        return true;
    }

    public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
        printInfo(i);
        return false;
    }

    public void onVideoSizeChanged(MediaPlayer mediaPlayer, int i, int i2) {
        MainThreadMediaPlayerListener mainThreadMediaPlayerListener = this.mListener;
        if (mainThreadMediaPlayerListener != null) {
            mainThreadMediaPlayerListener.onVideoSizeChangedMainThread(i, i2);
        }
    }

    public void pause() {
        synchronized (this.mState) {
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 5:
                case 7:
                case 8:
                case 9:
                case 10:
                    StringBuilder sb = new StringBuilder();
                    sb.append("pause, called from illegal state ");
                    sb.append(this.mState);
                    throw new IllegalStateException(sb.toString());
                case 6:
                    this.mMediaPlayer.pause();
                    this.mState.set(State.PAUSED);
                    break;
            }
        }
    }

    public void prepare() {
        synchronized (this.mState) {
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 2:
                    try {
                        this.mMediaPlayer.prepare();
                        this.mState.set(State.PREPARED);
                        if (this.mListener != null) {
                            this.mMainThreadHandler.post(this.mOnVideoPreparedMessage);
                            break;
                        }
                    } catch (IllegalStateException e) {
                        String str = this.TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("catch IllegalStateException [");
                        sb.append(e);
                        sb.append("]");
                        Logger.err(str, sb.toString());
                        break;
                    } catch (IOException e2) {
                        onPrepareError(e2);
                        break;
                    }
                    break;
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 9:
                case 10:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("prepare, called from illegal state ");
                    sb2.append(this.mState);
                    throw new IllegalStateException(sb2.toString());
            }
        }
    }

    public void release() {
        synchronized (this.mState) {
            this.mMediaPlayer.release();
            this.mState.set(State.END);
        }
    }

    public void reset() {
        synchronized (this.mState) {
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 5:
                case 6:
                case 7:
                case 8:
                case 10:
                    this.mMediaPlayer.reset();
                    this.mState.set(State.IDLE);
                    break;
                case 4:
                case 9:
                    StringBuilder sb = new StringBuilder();
                    sb.append("cannot call reset from state ");
                    sb.append(this.mState.get());
                    throw new IllegalStateException(sb.toString());
            }
        }
    }

    public void seekToPercent(int i) {
        synchronized (this.mState) {
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 2:
                case 3:
                case 4:
                case 9:
                case 10:
                    break;
                case 5:
                case 6:
                case 7:
                case 8:
                    this.mMediaPlayer.seekTo((int) ((((float) i) / 100.0f) * ((float) getDuration())));
                    notifyPositionUpdated();
                    break;
            }
        }
    }

    public void setDataSource(AssetFileDescriptor assetFileDescriptor) {
        synchronized (this.mState) {
            if (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()] == 3) {
                this.mMediaPlayer.setDataSource(assetFileDescriptor.getFileDescriptor(), assetFileDescriptor.getStartOffset(), assetFileDescriptor.getLength());
                this.mState.set(State.INITIALIZED);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("setDataSource called in state ");
                sb.append(this.mState);
                throw new IllegalStateException(sb.toString());
            }
        }
    }

    public void setDataSource(String str) {
        synchronized (this.mState) {
            if (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()] == 3) {
                this.mMediaPlayer.setDataSource(str);
                this.mState.set(State.INITIALIZED);
            } else {
                StringBuilder sb = new StringBuilder();
                sb.append("setDataSource called in state ");
                sb.append(this.mState);
                throw new IllegalStateException(sb.toString());
            }
        }
    }

    public void setLooping(boolean z) {
        this.mMediaPlayer.setLooping(z);
    }

    public void setMainThreadMediaPlayerListener(MainThreadMediaPlayerListener mainThreadMediaPlayerListener) {
        this.mListener = mainThreadMediaPlayerListener;
    }

    public void setSurfaceTexture(SurfaceTexture surfaceTexture) {
        if (surfaceTexture != null) {
            this.mSurface = new Surface(surfaceTexture);
            this.mMediaPlayer.setSurface(this.mSurface);
            return;
        }
        this.mMediaPlayer.setSurface(null);
    }

    public void setVideoStateListener(VideoStateListener videoStateListener) {
        this.mVideoStateListener = videoStateListener;
    }

    public void setVolume(float f, float f2) {
        this.mMediaPlayer.setVolume(f, f2);
    }

    public void start() {
        synchronized (this.mState) {
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                case 5:
                case 7:
                case 8:
                    this.mMediaPlayer.start();
                    startPositionUpdateNotifier();
                    this.mState.set(State.STARTED);
                    break;
                case 2:
                case 3:
                case 4:
                case 6:
                    StringBuilder sb = new StringBuilder();
                    sb.append("start, called from illegal state ");
                    sb.append(this.mState);
                    throw new IllegalStateException(sb.toString());
                case 9:
                case 10:
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("start, called from illegal state ");
                    sb2.append(this.mState);
                    throw new IllegalStateException(sb2.toString());
            }
        }
    }

    public void stop() {
        synchronized (this.mState) {
            switch (AnonymousClass5.$SwitchMap$com$android$camera$videoplayer$ui$MediaPlayerWrapper$State[((State) this.mState.get()).ordinal()]) {
                case 1:
                    throw new IllegalStateException("stop, already stopped");
                case 2:
                case 3:
                case 9:
                case 10:
                    StringBuilder sb = new StringBuilder();
                    sb.append("cannot stop. Player in mState ");
                    sb.append(this.mState);
                    throw new IllegalStateException(sb.toString());
                case 4:
                case 5:
                case 8:
                    break;
                case 6:
                case 7:
                    stopPositionUpdateNotifier();
                    break;
            }
            this.mMediaPlayer.stop();
            this.mState.set(State.STOPPED);
            if (this.mListener != null) {
                this.mMainThreadHandler.post(this.mOnVideoStopMessage);
            }
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(getClass().getSimpleName());
        sb.append("@");
        sb.append(hashCode());
        return sb.toString();
    }
}
