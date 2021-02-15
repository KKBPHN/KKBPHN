package com.android.camera;

import android.content.Context;
import android.media.AudioRecord;
import android.text.TextUtils;
import com.android.camera.log.Log;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class AudioCalculateDecibels {
    private static final boolean DEBUG = false;
    private static final String DEF_AUDIO_FILE = "audio_test.pcm";
    public static final float DEF_VOLUME_SIZE = 25.0f;
    private static final int MIN_AUDIO_BUFFER_SIZE = 8192;
    private static final int SAMPLE_RATE = 44100;
    private static final String TAG = "AudioCalculateDecibels";
    /* access modifiers changed from: private */
    public FileOutputStream mAudioDataOs;
    private String mAudioFileName;
    /* access modifiers changed from: private */
    public AudioRecord mAudioRecord;
    private int mAudioSource;
    /* access modifiers changed from: private */
    public byte[] mBuffer;
    private String mCacheDir;
    /* access modifiers changed from: private */
    public OnVolumeValueListener mOnVolumeListener;
    /* access modifiers changed from: private */
    public int mRecBufSize;
    private AudioRecordRunnale mRunnale;
    private Thread mThread;

    class AudioRecordRunnale implements Runnable {
        private volatile boolean mIsStopRecordThread;

        private AudioRecordRunnale() {
            this.mIsStopRecordThread = false;
        }

        public void run() {
            if (AudioCalculateDecibels.this.mAudioRecord != null) {
                while (true) {
                    AudioRecord access$000 = AudioCalculateDecibels.this.mAudioRecord;
                    String str = AudioCalculateDecibels.TAG;
                    if (access$000 != null && !this.mIsStopRecordThread) {
                        int read = AudioCalculateDecibels.this.mAudioRecord.read(AudioCalculateDecibels.this.mBuffer, 0, AudioCalculateDecibels.this.mRecBufSize);
                        if (read != -1) {
                            if (this.mIsStopRecordThread) {
                                StringBuilder sb = new StringBuilder();
                                sb.append("mIsStopRecordThread=true,stop record return,current_thread:");
                                sb.append(Thread.currentThread().getName());
                                Log.d(str, sb.toString());
                                return;
                            }
                            StringBuilder sb2 = new StringBuilder();
                            sb2.append("record read size:");
                            sb2.append(read);
                            Log.d(str, sb2.toString());
                            if (read > 0) {
                                try {
                                    byte[] bArr = new byte[read];
                                    System.arraycopy(AudioCalculateDecibels.this.mBuffer, 0, bArr, 0, read);
                                    AudioCalculateDecibels.this.mOnVolumeListener.onVolumeValue(AudioCalculateDecibels.this.calculateVolumeSize(bArr));
                                    if (AudioCalculateDecibels.this.mAudioDataOs == null) {
                                        StringBuilder sb3 = new StringBuilder();
                                        sb3.append("audio data outputstream is null,return thread:");
                                        sb3.append(Thread.currentThread().getName());
                                        Log.d(str, sb3.toString());
                                        return;
                                    } else if (this.mIsStopRecordThread) {
                                        try {
                                            AudioCalculateDecibels.this.mAudioDataOs.close();
                                            break;
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            break;
                                        }
                                    } else {
                                        AudioCalculateDecibels.this.mAudioDataOs.write(AudioCalculateDecibels.this.mBuffer, 0, read);
                                    }
                                } catch (IOException e2) {
                                    e2.printStackTrace();
                                }
                            }
                        }
                    }
                    try {
                        AudioCalculateDecibels.this.mAudioDataOs.close();
                    } catch (IOException e3) {
                        e3.printStackTrace();
                    }
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append("record stop, current_thread:");
                    sb4.append(Thread.currentThread().getName());
                    Log.d(str, sb4.toString());
                    break;
                }
            }
        }

        public void setIsStopRecordThread(boolean z) {
            this.mIsStopRecordThread = z;
        }
    }

    public interface OnVolumeValueListener {
        void onVolumeValue(float[] fArr);
    }

    public AudioCalculateDecibels(int i, Context context) {
        this.mCacheDir = context.getCacheDir().getAbsolutePath();
        this.mRecBufSize = AudioRecord.getMinBufferSize(44100, 3, 2);
        this.mRecBufSize = Math.max(8192, this.mRecBufSize);
        int i2 = this.mRecBufSize;
        this.mBuffer = new byte[i2];
        this.mAudioSource = i;
        AudioRecord audioRecord = new AudioRecord(this.mAudioSource, 44100, 3, 2, i2);
        this.mAudioRecord = audioRecord;
    }

    /* access modifiers changed from: private */
    public float[] calculateVolumeSize(byte[] bArr) {
        float[] fArr = new float[2];
        int length = bArr.length;
        float f = 0.0f;
        float f2 = 0.0f;
        for (int i = 0; i < length; i += 4) {
            int i2 = (bArr[i] & -1) + ((bArr[i + 1] & -1) << 8);
            int i3 = (bArr[i + 2] & -1) + ((bArr[i + 3] & -1) << 8);
            if (i2 >= 32768) {
                i2 = 65535 - i2;
            }
            if (i3 >= 32768) {
                i3 = 65535 - i3;
            }
            f += (float) Math.abs(i2);
            f2 += (float) Math.abs(i3);
        }
        float f3 = (float) length;
        float log10 = ((float) Math.log10((double) (((f / f3) / 4.0f) + 1.0f))) * 10.0f;
        float log102 = ((float) Math.log10((double) (((f2 / f3) / 4.0f) + 1.0f))) * 10.0f;
        fArr[0] = log10;
        fArr[1] = log102;
        return fArr;
    }

    public void release() {
        String str = TAG;
        Log.d(str, "release()");
        stopRecord();
        AudioRecord audioRecord = this.mAudioRecord;
        if (audioRecord != null && audioRecord.getState() == 1) {
            this.mAudioRecord.release();
        }
        this.mAudioRecord = null;
        Log.d(str, "release record...");
    }

    public void setOnVolumeListener(OnVolumeValueListener onVolumeValueListener) {
        this.mOnVolumeListener = onVolumeValueListener;
    }

    public synchronized void start() {
        File file = new File(this.mCacheDir, TextUtils.isEmpty(this.mAudioFileName) ? DEF_AUDIO_FILE : this.mAudioFileName);
        if (file.exists()) {
            file.delete();
        }
        try {
            if (file.createNewFile()) {
                this.mAudioDataOs = new FileOutputStream(file);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (this.mAudioRecord == null) {
            AudioRecord audioRecord = new AudioRecord(this.mAudioSource, 44100, 2, 2, this.mRecBufSize);
            this.mAudioRecord = audioRecord;
        }
        Log.d(TAG, "start record...");
        this.mAudioRecord.startRecording();
        this.mRunnale = new AudioRecordRunnale();
        this.mThread = new Thread(this.mRunnale);
        this.mThread.start();
    }

    public void stopRecord() {
        String str = TAG;
        Log.d(str, "stopRecord()");
        AudioRecordRunnale audioRecordRunnale = this.mRunnale;
        if (audioRecordRunnale != null) {
            audioRecordRunnale.setIsStopRecordThread(true);
        }
        AudioRecord audioRecord = this.mAudioRecord;
        if (audioRecord != null && audioRecord.getState() == 1) {
            this.mAudioRecord.stop();
        }
        this.mThread = null;
        this.mRunnale = null;
        Log.d(str, "stop record...");
    }
}
