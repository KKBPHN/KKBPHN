package com.bef.effectsdk;

import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.MediaPlayer.OnErrorListener;
import android.media.MediaPlayer.OnInfoListener;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import androidx.annotation.Keep;

@Keep
public class AudioPlayer {
    public static final String TAG = "AudioPlayer";
    private String mFilename;
    /* access modifiers changed from: private */
    public boolean mIsPrepared;
    /* access modifiers changed from: private */
    public MediaPlayer mMediaPlayer;
    /* access modifiers changed from: private */
    public long mNativePtr;

    @Keep
    public AudioPlayer() {
        this.mIsPrepared = false;
        this.mMediaPlayer = null;
        this.mIsPrepared = false;
        this.mFilename = null;
    }

    @Keep
    public native void nativeOnCompletion(long j);

    @Keep
    public native void nativeOnError(long j, int i, int i2);

    @Keep
    public native void nativeOnInfo(long j, int i, int i2);

    @Keep
    public native void nativeOnPrepared(long j);

    @Keep
    public float getCurrentPlayTime() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return ((float) mediaPlayer.getCurrentPosition()) / 1000.0f;
        }
        Log.e(TAG, "MediaPlayer is null!");
        return 0.0f;
    }

    @Keep
    public float getTotalPlayTime() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            return ((float) mediaPlayer.getDuration()) / 1000.0f;
        }
        Log.e(TAG, "MediaPlayer is null!");
        return 0.0f;
    }

    @Keep
    public int init() {
        this.mIsPrepared = false;
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            this.mMediaPlayer.release();
        }
        this.mMediaPlayer = new MediaPlayer();
        this.mMediaPlayer.setOnInfoListener(new OnInfoListener() {
            public boolean onInfo(MediaPlayer mediaPlayer, int i, int i2) {
                String str = AudioPlayer.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer onInfo: [what, extra] = [");
                sb.append(i);
                sb.append(", ");
                sb.append(i2);
                sb.append("]");
                Log.i(str, sb.toString());
                AudioPlayer audioPlayer = AudioPlayer.this;
                audioPlayer.nativeOnInfo(audioPlayer.mNativePtr, i, i2);
                return false;
            }
        });
        this.mMediaPlayer.setOnErrorListener(new OnErrorListener() {
            public boolean onError(MediaPlayer mediaPlayer, int i, int i2) {
                String str = AudioPlayer.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer onError: [what, extra] = [");
                sb.append(i);
                sb.append(", ");
                sb.append(i2);
                sb.append("]");
                Log.d(str, sb.toString());
                try {
                    AudioPlayer.this.mMediaPlayer.stop();
                    AudioPlayer.this.mMediaPlayer.release();
                } catch (Exception e) {
                    e.printStackTrace();
                    String str2 = AudioPlayer.TAG;
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("MediaPlayer stop exception on error ");
                    sb2.append(e.toString());
                    Log.e(str2, sb2.toString());
                }
                AudioPlayer.this.mMediaPlayer = null;
                AudioPlayer audioPlayer = AudioPlayer.this;
                audioPlayer.nativeOnError(audioPlayer.mNativePtr, i, i2);
                return false;
            }
        });
        this.mMediaPlayer.setOnPreparedListener(new OnPreparedListener() {
            public void onPrepared(MediaPlayer mediaPlayer) {
                Log.i(AudioPlayer.TAG, "MediaPlayer onPrepared...");
                AudioPlayer.this.mIsPrepared = true;
                AudioPlayer audioPlayer = AudioPlayer.this;
                audioPlayer.nativeOnPrepared(audioPlayer.mNativePtr);
            }
        });
        this.mMediaPlayer.setOnCompletionListener(new OnCompletionListener() {
            public void onCompletion(MediaPlayer mediaPlayer) {
                Log.i(AudioPlayer.TAG, "MediaPlayer onCompletion...");
                AudioPlayer audioPlayer = AudioPlayer.this;
                audioPlayer.nativeOnCompletion(audioPlayer.mNativePtr);
            }
        });
        return 0;
    }

    @Keep
    public boolean isPlaying() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        String str = "MediaPlayer is null!";
        boolean z = false;
        if (mediaPlayer != null && this.mIsPrepared) {
            try {
                z = mediaPlayer.isPlaying();
            } catch (Exception e) {
                e.printStackTrace();
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer isPlaying exception. ");
                sb.append(e.toString());
                Log.e(str2, sb.toString());
            }
            return z;
        }
        Log.e(TAG, str);
        return false;
    }

    @Keep
    public boolean pause() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        String str = "MediaPlayer is null!";
        if (mediaPlayer != null && this.mIsPrepared) {
            mediaPlayer.pause();
            return true;
        }
        Log.e(TAG, str);
        return false;
    }

    @Keep
    public int release() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            try {
                mediaPlayer.stop();
                this.mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer stop exception on release ");
                sb.append(e.toString());
                Log.e(str, sb.toString());
            }
            this.mMediaPlayer = null;
        }
        return 0;
    }

    @Keep
    public boolean resume() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        String str = "MediaPlayer is null!";
        if (mediaPlayer != null && this.mIsPrepared) {
            mediaPlayer.start();
            return true;
        }
        Log.e(TAG, str);
        return false;
    }

    @Keep
    public boolean seek(int i) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        String str = "MediaPlayer is null!";
        if (mediaPlayer != null && this.mIsPrepared) {
            try {
                mediaPlayer.seekTo(i);
            } catch (Exception e) {
                e.printStackTrace();
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer seek exception. ");
                sb.append(e.toString());
                Log.e(str2, sb.toString());
            }
            return true;
        }
        Log.e(TAG, str);
        return false;
    }

    @Keep
    public void setDataSource(String str) {
        if (this.mMediaPlayer == null) {
            init();
        }
        if (!str.equals(this.mFilename) || !this.mIsPrepared || !this.mMediaPlayer.isPlaying()) {
            try {
                this.mMediaPlayer.reset();
                this.mMediaPlayer.setDataSource(str);
            } catch (Exception e) {
                e.printStackTrace();
                String str2 = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer setDataSource exception. ");
                sb.append(e.toString());
                Log.e(str2, sb.toString());
            }
            this.mFilename = str;
        }
    }

    @Keep
    public boolean setLoop(boolean z) {
        if (this.mMediaPlayer == null) {
            Log.e(TAG, "MediaPlayer is null!");
            return false;
        }
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("set isLoop ");
        sb.append(z);
        Log.i(str, sb.toString());
        this.mMediaPlayer.setLooping(z);
        return true;
    }

    @Keep
    public void setNativePtr(long j) {
        this.mNativePtr = j;
    }

    @Keep
    public boolean setVolume(float f) {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        String str = "MediaPlayer is null!";
        if (mediaPlayer != null && this.mIsPrepared) {
            mediaPlayer.setVolume(f, f);
            return true;
        }
        Log.e(TAG, str);
        return false;
    }

    @Keep
    public void startPlay() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer != null) {
            try {
                if (!this.mIsPrepared) {
                    mediaPlayer.prepare();
                    this.mIsPrepared = true;
                }
                this.mMediaPlayer.start();
            } catch (Exception e) {
                e.printStackTrace();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer setDataSource exception. ");
                sb.append(e.toString());
                Log.e(str, sb.toString());
            }
        }
    }

    @Keep
    public void stopPlay() {
        MediaPlayer mediaPlayer = this.mMediaPlayer;
        if (mediaPlayer == null) {
            Log.e(TAG, "MediaPlayer is null!");
            return;
        }
        if (this.mIsPrepared) {
            try {
                mediaPlayer.stop();
                this.mMediaPlayer.release();
            } catch (Exception e) {
                e.printStackTrace();
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("MediaPlayer stop exception on stop ");
                sb.append(e.toString());
                Log.e(str, sb.toString());
            }
            this.mMediaPlayer = null;
            this.mIsPrepared = false;
        }
    }
}
