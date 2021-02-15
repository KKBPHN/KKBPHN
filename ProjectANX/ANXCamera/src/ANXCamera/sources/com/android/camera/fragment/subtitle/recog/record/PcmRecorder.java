package com.android.camera.fragment.subtitle.recog.record;

import android.media.AudioRecord;
import com.android.camera.log.Log;
import java.lang.ref.WeakReference;

public class PcmRecorder extends Thread {
    private static final short DEFAULT_CHANNELS = 1;
    public static final int RATE16K = 16000;
    public static final int READ_INTERVAL40MS = 40;
    private static final int RECORD_BUFFER_TIMES_FOR_FRAME = 4;
    private static final String TAG = "PcmRecorder";
    private final short DEFAULT_BIT_SAMPLES;
    private double checkDataSum;
    private double checkStandDev;
    private volatile boolean exit;
    private int mAudioSource;
    private byte[] mDataBuffer;
    private int mInterval;
    private WeakReference mOutListener;
    private int mRate;
    private int mReadInterval;
    private AudioRecord mRecorder;
    private WeakReference mStopListener;

    public interface PcmRecordListener {
        void onError(int i);

        void onRecordBuffer(byte[] bArr, int i, int i2, int i3);

        void onRecordReleased();

        void onRecordStarted(boolean z);
    }

    public PcmRecorder(int i, int i2) {
        this(i, i2, 1);
    }

    public PcmRecorder(int i, int i2, int i3) {
        this.DEFAULT_BIT_SAMPLES = 16;
        this.mDataBuffer = null;
        this.mRecorder = null;
        this.mOutListener = null;
        this.mStopListener = null;
        this.exit = false;
        this.checkDataSum = 0.0d;
        this.checkStandDev = 0.0d;
        this.mRate = 16000;
        this.mInterval = 40;
        this.mReadInterval = 40;
        this.mAudioSource = i3;
        this.mRate = i;
        this.mInterval = i2;
        int i4 = this.mInterval;
        if (i4 < 40 || i4 > 100) {
            this.mInterval = 40;
        }
        this.mReadInterval = 10;
    }

    private double checkAudio(byte[] bArr, int i) {
        double d = 0.0d;
        if (bArr == null || i <= 0) {
            return 0.0d;
        }
        double d2 = 0.0d;
        for (byte b : bArr) {
            d2 += (double) b;
        }
        double length = d2 / ((double) bArr.length);
        for (byte b2 : bArr) {
            d += Math.pow(((double) b2) - length, 2.0d);
        }
        return Math.sqrt(d / ((double) (bArr.length - 1)));
    }

    private double getVolume(int i, byte[] bArr) {
        long j = 0;
        for (byte b : bArr) {
            j += (long) (b * b);
        }
        double log10 = Math.log10(((double) j) / ((double) i)) * 10.0d;
        return log10 > 20.0d ? log10 - 20.0d : log10;
    }

    private int readRecordData() {
        AudioRecord audioRecord = this.mRecorder;
        if (audioRecord == null || this.mOutListener == null) {
            return 0;
        }
        byte[] bArr = this.mDataBuffer;
        int read = audioRecord.read(bArr, 0, bArr.length);
        if (read > 0) {
            int computeVolume = VolumeUtil.computeVolume(this.mDataBuffer, read);
            PcmRecordListener pcmRecordListener = (PcmRecordListener) this.mOutListener.get();
            if (pcmRecordListener == null) {
                return 0;
            }
            pcmRecordListener.onRecordBuffer(this.mDataBuffer, 0, read, computeVolume);
        } else if (read < 0) {
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("Record read data error: ");
            sb.append(read);
            Log.e(str, sb.toString());
        }
        return read;
    }

    private void release() {
        synchronized (this) {
            try {
                if (this.mRecorder != null) {
                    Log.d(TAG, "release: ");
                    this.mRecorder.release();
                    this.mRecorder = null;
                    if (this.mStopListener != null) {
                        PcmRecordListener pcmRecordListener = (PcmRecordListener) this.mStopListener.get();
                        if (pcmRecordListener != null) {
                            pcmRecordListener.onRecordReleased();
                            this.mStopListener = null;
                            Log.d(TAG, "release record over");
                        }
                    }
                }
            } catch (Exception e) {
                Log.e(TAG, e.toString());
            }
        }
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        Log.d(TAG, "[finalize] release recoder");
        release();
        super.finalize();
    }

    /* access modifiers changed from: protected */
    public void initRecord(short s, int i, int i2) {
        if (this.mRecorder != null) {
            Log.d(TAG, "[initRecord] recoder release first");
            release();
        }
        int i3 = (i2 * i) / 1000;
        int i4 = (((i3 * 4) * 16) * s) / 8;
        int i5 = s == 1 ? 2 : 3;
        int minBufferSize = AudioRecord.getMinBufferSize(i, i5, 2);
        if (i4 < minBufferSize) {
            i4 = minBufferSize;
        }
        AudioRecord audioRecord = new AudioRecord(this.mAudioSource, i, i5, 2, i4);
        this.mRecorder = audioRecord;
        this.mDataBuffer = new byte[(((s * i3) * 16) / 8)];
        String str = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("\nSampleRate:");
        sb.append(i);
        sb.append("\nChannel:");
        sb.append(i5);
        sb.append("\nFormat:");
        sb.append(2);
        sb.append("\nFramePeriod:");
        sb.append(i3);
        sb.append("\nBufferSize:");
        sb.append(i4);
        sb.append("\nMinBufferSize:");
        sb.append(minBufferSize);
        sb.append("\nActualBufferSize:");
        sb.append(this.mDataBuffer.length);
        sb.append("\n");
        Log.d(str, sb.toString());
        if (this.mRecorder.getState() != 1) {
            Log.d(TAG, "create AudioRecord error");
        }
    }

    public void run() {
        boolean z;
        int i = 0;
        while (true) {
            try {
                z = true;
                if (this.exit) {
                    break;
                }
                try {
                    initRecord(1, this.mRate, this.mInterval);
                    break;
                } catch (Exception unused) {
                    i++;
                    if (i >= 10) {
                        break;
                    }
                    Thread.sleep(40);
                }
            } catch (Exception e) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("run: ");
                sb.append(e);
                Log.e(str, sb.toString());
                WeakReference weakReference = this.mOutListener;
                if (weakReference != null) {
                    PcmRecordListener pcmRecordListener = (PcmRecordListener) weakReference.get();
                    if (pcmRecordListener != null) {
                        pcmRecordListener.onError(111);
                    }
                }
            }
        }
        int i2 = 0;
        while (true) {
            if (this.exit) {
                break;
            }
            try {
                this.mRecorder.startRecording();
                if (this.mRecorder.getRecordingState() != 3) {
                    Log.e(TAG, "recorder state is not recoding");
                }
            } catch (Exception unused2) {
                i2++;
                if (i2 >= 10) {
                    Log.e(TAG, "recoder start failed");
                    break;
                }
                Thread.sleep(40);
            }
        }
        Log.d(TAG, "recoder start success ");
        if (this.mOutListener != null) {
            PcmRecordListener pcmRecordListener2 = (PcmRecordListener) this.mOutListener.get();
            if (pcmRecordListener2 != null) {
                pcmRecordListener2.onRecordStarted(true);
                long currentTimeMillis = System.currentTimeMillis();
                while (true) {
                    if (this.exit) {
                        break;
                    }
                    int readRecordData = readRecordData();
                    if (z) {
                        this.checkDataSum += (double) readRecordData;
                        this.checkStandDev += checkAudio(this.mDataBuffer, this.mDataBuffer.length);
                        if (System.currentTimeMillis() - currentTimeMillis >= 1000) {
                            if (this.checkDataSum == 0.0d) {
                                break;
                            } else if (this.checkStandDev == 0.0d) {
                                break;
                            } else {
                                z = false;
                            }
                        }
                    }
                    if (this.mDataBuffer.length > readRecordData) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("current record read size is less than buffer size: ");
                        sb2.append(readRecordData);
                        Log.d(str2, sb2.toString());
                        Thread.sleep((long) this.mReadInterval);
                    }
                }
                Log.e(TAG, "cannot get record permission, get invalid audio data.");
                release();
            }
        }
    }

    public void startRecording(PcmRecordListener pcmRecordListener) {
        this.mOutListener = new WeakReference(pcmRecordListener);
        setPriority(10);
        start();
    }

    public void stopRecord(boolean z) {
        this.exit = true;
        if (this.mStopListener == null) {
            this.mStopListener = this.mOutListener;
        }
        this.mOutListener = null;
        if (z) {
            synchronized (this) {
                try {
                    Log.d(TAG, "stopRecord...release");
                    if (this.mRecorder != null) {
                        if (3 == this.mRecorder.getRecordingState() && 1 == this.mRecorder.getState()) {
                            Log.d(TAG, "stopRecord releaseRecording ing...");
                            this.mRecorder.release();
                            Log.d(TAG, "stopRecord releaseRecording end...");
                            this.mRecorder = null;
                        }
                        if (this.mStopListener != null) {
                            PcmRecordListener pcmRecordListener = (PcmRecordListener) this.mStopListener.get();
                            if (pcmRecordListener != null) {
                                pcmRecordListener.onRecordReleased();
                                this.mStopListener = null;
                            } else {
                                return;
                            }
                        } else {
                            return;
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        Log.d(TAG, "stop record");
        Log.d(TAG, "stop record");
    }
}
