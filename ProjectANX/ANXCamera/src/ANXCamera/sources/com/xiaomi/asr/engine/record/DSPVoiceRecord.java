package com.xiaomi.asr.engine.record;

import android.media.AudioAttributes;
import android.media.AudioAttributes.Builder;
import android.media.AudioFormat;
import android.media.AudioRecord;
import android.os.Process;
import android.util.Log;
import com.android.camera.statistic.MistatsConstants.BaseEvent;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

public class DSPVoiceRecord {
    public static final int PARAM_KEY_SET_AUDIO_SOURCE = 1;
    public static final int PARAM_KEY_SET_CHANNEL = 0;
    public static final int PARAM_KEY_SET_ENCODING_BITS = 4;
    public static final int PARAM_KEY_SET_PRINT_LOG = 5;
    public static final int PARAM_KEY_SET_RECORD_BUFFER_SIZE = 2;
    public static final int PARAM_KEY_SET_SAMPLE_RATE = 3;
    private static final String TAG = "DSPVoiceRecord";
    private boolean isPrintLog = true;
    /* access modifiers changed from: private */
    public int mAudioEncodingBits = 2;
    /* access modifiers changed from: private */
    public int mAudioSource = 1;
    /* access modifiers changed from: private */
    public int mCaptureSession;
    /* access modifiers changed from: private */
    public int mChannels = 16;
    /* access modifiers changed from: private */
    public RecordListener mListener;
    /* access modifiers changed from: private */
    public int mRecordBufferSize = 1536;
    private RecordingRunnable mRecordThread;
    /* access modifiers changed from: private */
    public int mSampleRate = 16000;
    /* access modifiers changed from: private */
    public final Object syncObj = new Object();

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
            try {
                int minBufferSize = AudioRecord.getMinBufferSize(DSPVoiceRecord.this.mSampleRate, DSPVoiceRecord.this.mChannels, DSPVoiceRecord.this.mAudioEncodingBits);
                if (minBufferSize < 0) {
                    if (DSPVoiceRecord.this.mListener != null) {
                        DSPVoiceRecord.this.mListener.onRecordCreateError();
                    }
                    return false;
                }
                AudioRecord audioRecord = this.mAudioRecord;
                String str = DSPVoiceRecord.TAG;
                if (audioRecord == null) {
                    Constructor constructor = AudioRecord.class.getConstructor(new Class[]{AudioAttributes.class, AudioFormat.class, Integer.TYPE, Integer.TYPE});
                    Method method = Builder.class.getMethod("setInternalCapturePreset", new Class[]{Integer.TYPE});
                    AudioFormat build = new AudioFormat.Builder().setChannelMask(DSPVoiceRecord.this.mChannels).setEncoding(DSPVoiceRecord.this.mAudioEncodingBits).setSampleRate(DSPVoiceRecord.this.mSampleRate).build();
                    Builder builder = new Builder();
                    method.invoke(builder, new Object[]{Integer.valueOf(DSPVoiceRecord.this.mAudioSource)});
                    this.mAudioRecord = (AudioRecord) constructor.newInstance(new Object[]{builder.build(), build, Integer.valueOf(800000), Integer.valueOf(DSPVoiceRecord.this.mCaptureSession)});
                    StringBuilder sb = new StringBuilder();
                    sb.append("mAudioSource:");
                    sb.append(DSPVoiceRecord.this.mAudioSource);
                    sb.append(", mSampleRate:");
                    sb.append(DSPVoiceRecord.this.mSampleRate);
                    sb.append(", mChannels:");
                    sb.append(DSPVoiceRecord.this.mChannels);
                    sb.append(", mAudioEncodingBits:");
                    sb.append(DSPVoiceRecord.this.mAudioEncodingBits);
                    sb.append(", mRecordBufferSize:");
                    sb.append(DSPVoiceRecord.this.mRecordBufferSize);
                    sb.append(", mixRecordBufferSize:");
                    sb.append(minBufferSize);
                    Log.d(str, sb.toString());
                    if (DSPVoiceRecord.this.mListener != null) {
                        DSPVoiceRecord.this.mListener.onAudioSessionId(this.mAudioRecord.getAudioSessionId());
                    }
                }
                if (this.mAudioRecord.getState() != 1) {
                    if (DSPVoiceRecord.this.mListener != null) {
                        DSPVoiceRecord.this.mListener.onRecordCreateError();
                    }
                    return false;
                }
                Log.d(str, "init Recording");
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                if (DSPVoiceRecord.this.mListener != null) {
                    DSPVoiceRecord.this.mListener.onRecordCreateError();
                }
                return false;
            }
        }

        private boolean startup() {
            String str = DSPVoiceRecord.TAG;
            this.mIsEnd = false;
            this.mIsExit = false;
            long currentTimeMillis = System.currentTimeMillis();
            if (this.mAudioRecord.getState() == 1) {
                try {
                    Log.d(str, "start Recording");
                    this.mAudioRecord.startRecording();
                    long currentTimeMillis2 = System.currentTimeMillis();
                    StringBuilder sb = new StringBuilder();
                    sb.append("start recording deltaTime = ");
                    sb.append(currentTimeMillis2 - currentTimeMillis);
                    Log.d(str, sb.toString());
                    return true;
                } catch (IllegalStateException e) {
                    e.printStackTrace();
                    Log.e(str, "start Recording failed");
                    if (DSPVoiceRecord.this.mListener != null) {
                        DSPVoiceRecord.this.mListener.onRecordCreateError();
                    }
                    return false;
                }
            } else {
                if (DSPVoiceRecord.this.mListener != null) {
                    DSPVoiceRecord.this.mListener.onRecordCreateError();
                }
                return false;
            }
        }

        public void run() {
            Process.setThreadPriority(-19);
            if (init()) {
                try {
                    byte[] bArr = new byte[DSPVoiceRecord.this.mRecordBufferSize];
                    String str = DSPVoiceRecord.TAG;
                    StringBuilder sb = new StringBuilder();
                    sb.append("mRecordBufferSize: ");
                    sb.append(DSPVoiceRecord.this.mRecordBufferSize);
                    Log.d(str, sb.toString());
                    if (startup()) {
                        if (DSPVoiceRecord.this.mListener != null) {
                            DSPVoiceRecord.this.mListener.onRecordingStart();
                        }
                        while (true) {
                            if (this.mIsExit) {
                                break;
                            }
                            int read = this.mAudioRecord.read(bArr, 0, DSPVoiceRecord.this.mRecordBufferSize);
                            if (read <= 0) {
                                if (DSPVoiceRecord.this.mListener != null) {
                                    DSPVoiceRecord.this.mListener.onRecordingFailed();
                                }
                                String str2 = DSPVoiceRecord.TAG;
                                StringBuilder sb2 = new StringBuilder();
                                sb2.append("read() error :");
                                sb2.append(read);
                                Log.e(str2, sb2.toString());
                            } else {
                                synchronized (DSPVoiceRecord.this.syncObj) {
                                    if (DSPVoiceRecord.this.mListener != null) {
                                        DSPVoiceRecord.this.mListener.onRecording(bArr, read);
                                    }
                                    this.mIsExit = this.mIsEnd;
                                }
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                if (DSPVoiceRecord.this.mListener != null) {
                    DSPVoiceRecord.this.mListener.onRecordingEnd();
                }
                if (this.mAudioRecord != null) {
                    synchronized (DSPVoiceRecord.this.syncObj) {
                        try {
                            this.mAudioRecord.stop();
                            this.mAudioRecord.release();
                            this.mAudioRecord = null;
                        } catch (Exception e2) {
                            e2.printStackTrace();
                        }
                    }
                }
                Log.d(DSPVoiceRecord.TAG, "RecordingRunnable is exit");
                if (DSPVoiceRecord.this.mListener != null) {
                    DSPVoiceRecord.this.mListener.onRecordRelease();
                }
            }
        }

        public void stop() {
            synchronized (DSPVoiceRecord.this.syncObj) {
                this.mIsEnd = true;
                Log.d(DSPVoiceRecord.TAG, BaseEvent.STOP);
            }
        }
    }

    public DSPVoiceRecord(RecordListener recordListener) {
        this.mListener = recordListener;
    }

    private void printLog(String str, String str2) {
        if (this.isPrintLog) {
            Log.d(str, str2);
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

    public void startRecord(int i) {
        Thread thread;
        try {
            this.mCaptureSession = i;
            if (this.mRecordThread != null) {
                thread = new Thread(this.mRecordThread);
            } else {
                this.mRecordThread = new RecordingRunnable();
                thread = new Thread(this.mRecordThread);
            }
            thread.start();
        } catch (IllegalStateException e) {
            e.printStackTrace();
            RecordListener recordListener = this.mListener;
            if (recordListener != null) {
                recordListener.onRecordingFailed();
            }
        }
    }

    public void stopRecord() {
        if (this.mRecordThread != null) {
            Log.d(TAG, "stopRecord");
            this.mRecordThread.stop();
            this.mRecordThread = null;
        }
    }
}
