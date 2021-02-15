package com.android.camera;

import android.media.AudioTrack;
import android.os.Process;
import com.android.camera.log.Log;
import com.xiaomi.asr.engine.record.AudioType.Frequency;
import java.lang.Thread.State;
import java.util.Arrays;

public class AudioMonitorPlayer {
    private static final String TAG = "MiuiAudioMonitor";
    /* access modifiers changed from: private */
    public int bufferSize = AudioTrack.getMinBufferSize(Frequency.FREQ_48KHZ, 4, 2);
    final int channelConfig = 4;
    final int encoding = 2;
    /* access modifiers changed from: private */
    public short[] mPlayBuffer = new short[this.bufferSize];
    /* access modifiers changed from: private */
    public boolean mPlayThreadRunning = false;
    /* access modifiers changed from: private */
    public AudioTrack mPlayer = null;
    Runnable playbackRunnable = new Runnable() {
        public void run() {
            String str = AudioMonitorPlayer.TAG;
            try {
                Log.w(str, "STAR PLAY AUDIO ...");
                Process.setThreadPriority(-19);
                if (AudioMonitorPlayer.this.mPlayer == null) {
                    AudioMonitorPlayer audioMonitorPlayer = AudioMonitorPlayer.this;
                    AudioTrack audioTrack = new AudioTrack(3, Frequency.FREQ_48KHZ, 4, 2, AudioMonitorPlayer.this.bufferSize, 1);
                    audioMonitorPlayer.mPlayer = audioTrack;
                }
                while (AudioMonitorPlayer.this.mPlayThreadRunning) {
                    AudioMonitorPlayer.this.mPlayer.play();
                    AudioMonitorPlayer.this.mPlayer.write(AudioMonitorPlayer.this.mPlayBuffer, 0, AudioMonitorPlayer.this.bufferSize);
                }
            } catch (Exception e) {
                Log.e(str, "exception when play audio :", (Throwable) e);
            }
        }
    };
    private Thread playbackThread;
    final int sampleRateInHz = Frequency.FREQ_48KHZ;

    public AudioMonitorPlayer() {
        Arrays.fill(this.mPlayBuffer, 0);
    }

    private void destroyThread() {
        Thread thread = this.playbackThread;
        if (thread != null && State.RUNNABLE == thread.getState()) {
            try {
                this.playbackThread.interrupt();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startThread() {
        destroyThread();
        Thread thread = this.playbackThread;
        if (thread == null || !thread.isAlive()) {
            this.playbackThread = new Thread(this.playbackRunnable);
        }
        this.playbackThread.start();
    }

    public void startPlay() {
        if (!this.mPlayThreadRunning) {
            this.mPlayThreadRunning = true;
            try {
                Log.w(TAG, "startThread ...");
                startThread();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void stopPlay() {
        String str = TAG;
        if (this.mPlayThreadRunning) {
            this.mPlayThreadRunning = false;
            try {
                Log.w(str, "destroyThread ...");
                destroyThread();
                if (this.mPlayer != null) {
                    if (this.mPlayer.getState() == 1) {
                        this.mPlayer.stop();
                    }
                    Log.w(str, "RELEASE AUDIO TRACK ...");
                    this.mPlayer.release();
                    this.mPlayer = null;
                }
            } catch (Exception e) {
                Log.e(str, "exception when stop audio :", (Throwable) e);
            }
        }
    }
}
