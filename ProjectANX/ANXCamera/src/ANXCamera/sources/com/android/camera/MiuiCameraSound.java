package com.android.camera;

import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.media.SoundPool.Builder;
import android.media.SoundPool.OnLoadCompleteListener;
import com.android.camera.customization.ShutterSound;
import com.android.camera.log.Log;
import io.reactivex.BackpressureStrategy;
import io.reactivex.Flowable;
import io.reactivex.FlowableEmitter;
import io.reactivex.FlowableOnSubscribe;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import io.reactivex.schedulers.Schedulers;
import java.util.ArrayList;

public class MiuiCameraSound implements Consumer {
    public static final int AUDIO_CAPTURE = 7;
    public static final int FAST_BURST = 4;
    public static final int FOCUS_COMPLETE = 1;
    public static final int KNOBS_SCROLL = 6;
    private static final int NUM_MEDIA_SOUND_STREAMS = 1;
    public static final int SHUTTER_CLICK = 0;
    public static final int SHUTTER_DELAY = 5;
    public static final int SOUND_NOT_LOADED = -1;
    public static final int START_VIDEO_RECORDING = 2;
    public static final int STOP_VIDEO_RECORDING = 3;
    private static final String TAG = "MiuiCameraSound";
    private final AudioManager mAudioManager;
    /* access modifiers changed from: private */
    public ArrayList mCompleteSampleList;
    private Disposable mDisposable;
    /* access modifiers changed from: private */
    public FlowableEmitter mFlowableEmitter;
    private boolean mForceSound;
    private long mLastPlayTime;
    private OnLoadCompleteListener mLoadCompleteListener;
    /* access modifiers changed from: private */
    public int mSoundIdToPlay;
    private SoundPool mSoundPool;

    public class PlayConfig {
        public int soundId;
        public float volume = 1.0f;
    }

    public MiuiCameraSound(Context context) {
        this(context, false);
    }

    public MiuiCameraSound(Context context, boolean z) {
        this.mLastPlayTime = 0;
        this.mCompleteSampleList = new ArrayList();
        this.mLoadCompleteListener = new OnLoadCompleteListener() {
            public void onLoadComplete(SoundPool soundPool, int i, int i2) {
                if (i2 == 0) {
                    if (MiuiCameraSound.this.mSoundIdToPlay == i) {
                        soundPool.play(i, 1.0f, 1.0f, 0, 0, 1.0f);
                        MiuiCameraSound.this.mSoundIdToPlay = -1;
                    }
                    MiuiCameraSound.this.mCompleteSampleList.add(Integer.valueOf(i));
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to read sound for playback (status: ");
                sb.append(i2);
                sb.append(")");
                Log.e(MiuiCameraSound.TAG, sb.toString());
            }
        };
        this.mAudioManager = (AudioManager) context.getSystemService("audio");
        Builder builder = new Builder();
        int i = 1;
        builder.setMaxStreams(1);
        AudioAttributes.Builder builder2 = new AudioAttributes.Builder();
        if (!C0124O00000oO.Oo00o0O() || this.mForceSound) {
            i = 7;
        }
        builder.setAudioAttributes(builder2.setInternalLegacyStreamType(i).build());
        ShutterSound.release();
        this.mSoundPool = builder.build();
        this.mSoundPool.setOnLoadCompleteListener(this.mLoadCompleteListener);
        this.mSoundIdToPlay = -1;
        this.mDisposable = Flowable.create(new FlowableOnSubscribe() {
            public void subscribe(FlowableEmitter flowableEmitter) {
                MiuiCameraSound.this.mFlowableEmitter = flowableEmitter;
            }
        }, BackpressureStrategy.DROP).observeOn(Schedulers.io()).onBackpressureDrop(new Consumer() {
            public void accept(@NonNull PlayConfig playConfig) {
                StringBuilder sb = new StringBuilder();
                sb.append("play sound too fast: ");
                sb.append(playConfig.soundId);
                Log.e(MiuiCameraSound.TAG, sb.toString());
            }
        }).subscribe((Consumer) this);
    }

    private synchronized void play(int i, float f, int i2) {
        if (i < 0 || i > 7) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown sound requested: ");
            sb.append(i);
            throw new RuntimeException(sb.toString());
        }
        int tryPlaySound = ShutterSound.tryPlaySound(i, this.mSoundPool, f, i2, this.mCompleteSampleList);
        if (tryPlaySound != -1) {
            this.mSoundIdToPlay = tryPlaySound;
        } else {
            this.mLastPlayTime = System.currentTimeMillis();
        }
    }

    private void playSound(int i, float f, int i2) {
        if (!C0124O00000oO.Oo00o0O() || this.mForceSound || this.mAudioManager.getRingerMode() == 2) {
            play(i, f, i2);
        }
    }

    public void accept(@NonNull PlayConfig playConfig) {
        playSound(playConfig.soundId, playConfig.volume, 1);
    }

    public long getLastSoundPlayTime() {
        return this.mLastPlayTime;
    }

    public synchronized void load(int i) {
        if (i < 0 || i > 7) {
            StringBuilder sb = new StringBuilder();
            sb.append("Unknown sound requested: ");
            sb.append(i);
            throw new RuntimeException(sb.toString());
        } else if (this.mSoundPool == null) {
            Log.d(TAG, "mSoundPool has not been init, skip this time");
        } else {
            ShutterSound.loadSound(this.mSoundPool, i);
        }
    }

    public void playSound(int i) {
        playSound(i, 1.0f);
    }

    public void playSound(int i, float f) {
        if (!this.mFlowableEmitter.isCancelled()) {
            PlayConfig playConfig = new PlayConfig();
            playConfig.soundId = i;
            playConfig.volume = f;
            this.mFlowableEmitter.onNext(playConfig);
        }
    }

    public void release() {
        ShutterSound.release();
        Disposable disposable = this.mDisposable;
        if (disposable != null && !disposable.isDisposed()) {
            this.mDisposable.dispose();
        }
        SoundPool soundPool = this.mSoundPool;
        if (soundPool != null) {
            soundPool.release();
            this.mSoundPool = null;
        }
    }
}
