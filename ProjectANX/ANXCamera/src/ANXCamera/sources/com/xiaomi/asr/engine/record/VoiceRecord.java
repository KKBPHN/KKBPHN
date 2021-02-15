package com.xiaomi.asr.engine.record;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Process;
import android.util.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;

public class VoiceRecord {
    public static final int PARAM_KEY_SET_AUDIO_SOURCE = 1;
    public static final int PARAM_KEY_SET_CHANNEL = 0;
    public static final int PARAM_KEY_SET_ENCODING_BITS = 4;
    public static final int PARAM_KEY_SET_PRINT_LOG = 5;
    public static final int PARAM_KEY_SET_RECORD_BUFFER_SIZE = 2;
    public static final int PARAM_KEY_SET_SAMPLE_RATE = 3;
    private static final String TAG = "VoiceRecord";
    private boolean isPrintLog = true;
    /* access modifiers changed from: private */
    public int mAudioEncodingBits = 2;
    /* access modifiers changed from: private */
    public int mAudioSource = 1;
    /* access modifiers changed from: private */
    public int mChannels = 16;
    private Handler mHandler;
    private HandlerThread mHandlerThread;
    /* access modifiers changed from: private */
    public RecordListener mListener;
    /* access modifiers changed from: private */
    public int mRecordBufferSize = 1536;
    /* access modifiers changed from: private */
    public volatile int mRecordTaskType = 0;
    /* access modifiers changed from: private */
    public RecordingRunnable mRecordThread;
    /* access modifiers changed from: private */
    public int mSampleRate = 16000;
    private Runnable mStartNewTask = new Runnable() {
        public void run() {
            RecorderSynchronizer.getInstance().stop();
            try {
                RecorderSynchronizer.getInstance().getCountDownLatch().await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            if (VoiceRecord.this.mRecordTaskType > 0) {
                VoiceRecord.this.startRecordImp();
            }
        }
    };
    protected Object syncObj = new Object();

    public interface RecordListener {
        void onAudioSessionId(int i);

        void onRecordCreateError();

        void onRecordRelease();

        void onRecording(byte[] bArr, int i);

        void onRecordingEnd();

        void onRecordingFailed();

        void onRecordingStart();
    }

    class RecordingRunnable implements Runnable {
        private AudioRecord mAudioRecord;
        private boolean mIsEnd;
        private boolean mIsExit;

        private RecordingRunnable() {
            this.mAudioRecord = null;
            this.mIsEnd = false;
            this.mIsExit = false;
        }

        private boolean init() {
            String str = VoiceRecord.TAG;
            Log.d(str, "init Recording");
            try {
                if (this.mIsEnd) {
                    if (VoiceRecord.this.mListener != null) {
                        VoiceRecord.this.mListener.onRecordingEnd();
                    }
                    Log.d(str, "The user set up stop");
                    return false;
                }
                int minBufferSize = AudioRecord.getMinBufferSize(VoiceRecord.this.mSampleRate, VoiceRecord.this.mChannels, VoiceRecord.this.mAudioEncodingBits);
                if (minBufferSize < 0) {
                    Log.e(str, "AudioRecord call getMinBufferSize < 0");
                    VoiceRecord.this.onAudioRecordFail();
                    return false;
                }
                if (this.mAudioRecord == null) {
                    AudioRecord audioRecord = new AudioRecord(VoiceRecord.this.mAudioSource, VoiceRecord.this.mSampleRate, VoiceRecord.this.mChannels, VoiceRecord.this.mAudioEncodingBits, minBufferSize);
                    this.mAudioRecord = audioRecord;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mAudioSource:");
                    sb.append(VoiceRecord.this.mAudioSource);
                    sb.append(", mSampleRate:");
                    sb.append(VoiceRecord.this.mSampleRate);
                    sb.append(", mChannels:");
                    sb.append(VoiceRecord.this.mChannels);
                    sb.append(", mAudioEncodingBits:");
                    sb.append(VoiceRecord.this.mAudioEncodingBits);
                    sb.append(", mRecordBufferSize:");
                    sb.append(VoiceRecord.this.mRecordBufferSize);
                    sb.append(", mixRecordBufferSize:");
                    sb.append(minBufferSize);
                    Log.d(str, sb.toString());
                    if (VoiceRecord.this.mListener != null) {
                        VoiceRecord.this.mListener.onAudioSessionId(this.mAudioRecord.getAudioSessionId());
                    }
                }
                if (this.mAudioRecord.getState() == 1) {
                    return true;
                }
                Log.e(str, "AudioRecord state is not correct");
                VoiceRecord.this.onAudioRecordFail();
                return false;
            } catch (Exception e) {
                e.printStackTrace();
                VoiceRecord.this.onAudioRecordFail();
                return false;
            }
        }

        private void releaseAudioRecordImp() {
            StringBuilder sb = new StringBuilder();
            sb.append("mAudioSource stopAudioRecordImp is not null: ");
            sb.append(this.mAudioRecord != null);
            String sb2 = sb.toString();
            String str = VoiceRecord.TAG;
            Log.d(str, sb2);
            AudioRecord audioRecord = this.mAudioRecord;
            if (audioRecord != null) {
                audioRecord.stop();
                this.mAudioRecord.release();
                this.mAudioRecord = null;
            }
            Log.d(str, "mAudioSource stopAudioRecordImp over");
        }

        private boolean startup() {
            String str = VoiceRecord.TAG;
            Log.d(str, "startup");
            this.mIsExit = false;
            if (this.mIsEnd) {
                if (VoiceRecord.this.mListener != null) {
                    VoiceRecord.this.mListener.onRecordingEnd();
                }
                Log.d(str, "The user set up stop");
                return false;
            }
            long currentTimeMillis = System.currentTimeMillis();
            String str2 = "start Recording failed";
            if (this.mAudioRecord.getState() == 1) {
                try {
                    Log.d(str, "start Recording");
                    this.mAudioRecord.startRecording();
                    if (this.mAudioRecord.getRecordingState() != 3) {
                        Log.e(str, "AudioRecord recordingState is not correct");
                        VoiceRecord.this.onAudioRecordFail();
                        return false;
                    }
                    long currentTimeMillis2 = System.currentTimeMillis();
                    StringBuilder sb = new StringBuilder();
                    sb.append("start recording deltaTime = ");
                    sb.append(currentTimeMillis2 - currentTimeMillis);
                    Log.d(str, sb.toString());
                    return true;
                } catch (Exception e) {
                    e.printStackTrace();
                    Log.e(str, str2);
                    VoiceRecord.this.onAudioRecordFail();
                    return false;
                }
            } else {
                Log.e(str, str2);
                VoiceRecord.this.onAudioRecordFail();
                return false;
            }
        }

        public void run() {
            Process.setThreadPriority(-19);
            RecorderSynchronizer.getInstance().start();
            if (!init()) {
                RecorderSynchronizer.getInstance().stop();
                RecorderSynchronizer.getInstance().countDown();
                return;
            }
            try {
                byte[] bArr = new byte[VoiceRecord.this.mRecordBufferSize];
                String str = VoiceRecord.TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("mRecordBufferSize: ");
                sb.append(VoiceRecord.this.mRecordBufferSize);
                Log.d(str, sb.toString());
                if (startup()) {
                    if (VoiceRecord.this.mListener != null) {
                        VoiceRecord.this.mListener.onRecordingStart();
                    }
                    while (!this.mIsExit) {
                        int read = this.mAudioRecord.read(bArr, 0, VoiceRecord.this.mRecordBufferSize);
                        if (read == -3) {
                            throw new IllegalStateException("read() returned AudioRecord.ERROR_INVALID_OPERATION");
                        } else if (read != -2) {
                            synchronized (VoiceRecord.this.syncObj) {
                                if (VoiceRecord.this.mListener != null) {
                                    VoiceRecord.this.mListener.onRecording(bArr, read);
                                }
                                if (this.mIsEnd) {
                                    String str2 = VoiceRecord.TAG;
                                    StringBuilder sb2 = new StringBuilder();
                                    sb2.append("mIsEnd:  ");
                                    sb2.append(this.mIsEnd);
                                    Log.d(str2, sb2.toString());
                                    this.mIsExit = true;
                                }
                            }
                        } else {
                            throw new IllegalStateException("read() returned AudioRecord.ERROR_BAD_VALUE");
                        }
                    }
                    if (VoiceRecord.this.mListener != null) {
                        VoiceRecord.this.mListener.onRecordingEnd();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
                VoiceRecord.this.mRecordThread = null;
                if (VoiceRecord.this.mListener != null) {
                    VoiceRecord.this.mListener.onRecordingFailed();
                }
            }
            if (this.mAudioRecord != null) {
                synchronized (VoiceRecord.this.syncObj) {
                    try {
                        releaseAudioRecordImp();
                    } catch (Exception e2) {
                        e2.printStackTrace();
                        this.mAudioRecord = null;
                    }
                }
            }
            Log.d(VoiceRecord.TAG, "RecordingRunnable is exit");
            RecorderSynchronizer.getInstance().stop();
            RecorderSynchronizer.getInstance().countDown();
            if (VoiceRecord.this.mListener != null) {
                VoiceRecord.this.mListener.onRecordRelease();
            }
        }

        public void stop() {
            synchronized (VoiceRecord.this.syncObj) {
                this.mIsEnd = true;
                Log.d(VoiceRecord.TAG, BaseEvent.STOP);
            }
        }
    }

    public VoiceRecord(RecordListener recordListener) {
        this.mListener = recordListener;
        this.mHandlerThread = new HandlerThread(TAG);
        this.mHandlerThread.start();
        this.mHandler = new Handler(this.mHandlerThread.getLooper());
    }

    /* access modifiers changed from: private */
    public void onAudioRecordFail() {
        Log.e(TAG, "onAudioRecordFail");
        this.mRecordThread = null;
        RecordListener recordListener = this.mListener;
        if (recordListener != null) {
            recordListener.onRecordCreateError();
        }
    }

    private void printLog(String str, String str2) {
        if (this.isPrintLog) {
            Log.d(str, str2);
        }
    }

    /* access modifiers changed from: private */
    public void startRecordImp() {
        String str = TAG;
        Log.d(str, "startRecordImp");
        try {
            if (this.mRecordThread != null) {
                Log.d(str, "record already start");
                return;
            }
            this.mRecordThread = new RecordingRunnable();
            new Thread(this.mRecordThread).start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            RecordListener recordListener = this.mListener;
            if (recordListener != null) {
                recordListener.onRecordCreateError();
                this.mRecordThread = null;
            }
        }
    }

    public int getParamValue(int i) {
        if (i == 0) {
            return this.mChannels;
        }
        if (i == 1) {
            return this.mAudioSource;
        }
        if (i == 2) {
            return this.mRecordBufferSize;
        }
        if (i == 3) {
            return this.mSampleRate;
        }
        if (i != 4) {
            return -1;
        }
        return this.mAudioEncodingBits;
    }

    public void setParam(int i, int i2) {
        if (i != 0) {
            boolean z = true;
            if (i == 1) {
                this.mAudioSource = i2;
            } else if (i == 2) {
                this.mRecordBufferSize = i2;
            } else if (i == 3) {
                this.mSampleRate = i2;
            } else if (i == 4) {
                this.mAudioEncodingBits = i2;
            } else if (i == 5) {
                if (i2 == 0) {
                    z = false;
                }
                this.isPrintLog = z;
            }
        } else {
            this.mChannels = i2;
        }
    }

    public void startRecord() {
        Log.d(TAG, "startRecord");
        this.mRecordTaskType = 1;
        this.mHandler.removeCallbacks(this.mStartNewTask);
        this.mHandler.post(this.mStartNewTask);
    }

    public void stopRecord() {
        Log.d(TAG, "startRecordImp");
        this.mRecordTaskType = 0;
        this.mHandler.removeCallbacks(this.mStartNewTask);
        stopRecordImp();
    }

    public void stopRecordImp() {
        String str = TAG;
        Log.d(str, "stopRecordImp");
        if (this.mRecordThread != null) {
            Log.d(str, "stopRecord");
            this.mRecordThread.stop();
            this.mRecordThread = null;
        }
    }
}
