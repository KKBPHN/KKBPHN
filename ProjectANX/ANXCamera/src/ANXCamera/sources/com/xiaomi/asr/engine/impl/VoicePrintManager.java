package com.xiaomi.asr.engine.impl;

import android.media.AudioRecord;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import com.xiaomi.asr.engine.WVPListener;
import com.xiaomi.asr.engine.jni.VoicePrintInterface;
import com.xiaomi.asr.engine.record.AudioSource;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Arrays;

public class VoicePrintManager {
    private static final int REGISTER_FEED_RESULT_NO_RESULT = 3;
    private static final int REGISTER_FEED_RESULT_SUCCESS = 0;
    private static final int SIZEOF_REGISTER_PCM = 5;
    private static final String TAG = "VoicePrintManager";
    FileRecordWorker fileRecordWorker;
    private boolean init = false;
    /* access modifiers changed from: private */
    public int mCurrentRecordingPos = 0;
    /* access modifiers changed from: private */
    public WVPListener mOutListener;
    /* access modifiers changed from: private */
    public byte[][] mPcmSet = new byte[5][];
    /* access modifiers changed from: private */
    public int mRecordSource;
    private RecordWorker mRecordWorker;
    /* access modifiers changed from: private */
    public String mSpeakerName;
    /* access modifiers changed from: private */
    public VoicePrintInterface mVoicePrintInterface;
    /* access modifiers changed from: private */
    public String mVoicePrintModelFullPath;

    class FileRecordWorker extends Thread {
        private AudioSource mAudioSource;
        private ByteArrayOutputStream mCacheData;
        private boolean mCancel;
        private int mMaxTime;
        private boolean mRecordWorking;
        private int mRegisterState;

        private FileRecordWorker(AudioSource audioSource, int i) {
            this.mAudioSource = null;
            this.mAudioSource = audioSource;
            this.mMaxTime = i;
            this.mCacheData = new ByteArrayOutputStream();
        }

        public void cancel() {
            this.mCancel = true;
            this.mRecordWorking = false;
        }

        public void end() {
            this.mRecordWorking = false;
        }

        public void run() {
            byte[] bArr = new byte[m.ch];
            long uptimeMillis = SystemClock.uptimeMillis();
            this.mAudioSource.init();
            try {
                this.mAudioSource.startRecording();
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onStartAudio();
                }
                while (this.mRecordWorking && SystemClock.uptimeMillis() - uptimeMillis <= ((long) this.mMaxTime) && this.mAudioSource.read(bArr, 0, m.ch) > 0) {
                    try {
                        this.mCacheData.write(bArr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (this.mCacheData.size() > 0) {
                    int size = this.mCacheData.size();
                    this.mRegisterState = VoicePrintManager.this.mVoicePrintInterface.voiceprintReEnrollRegister(VoicePrintManager.this.mSpeakerName, this.mCacheData.toByteArray(), 0, size);
                }
                this.mAudioSource.release();
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onStopAudio();
                }
                if (this.mCancel) {
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onAbortEnrollmentComplete();
                    }
                    return;
                }
                if (this.mRegisterState == 0) {
                    if (VoicePrintManager.this.mCurrentRecordingPos >= 5) {
                        VoicePrintManager.this.mCurrentRecordingPos = 0;
                    }
                    VoicePrintManager voicePrintManager = VoicePrintManager.this;
                    voicePrintManager.mCurrentRecordingPos = voicePrintManager.mCurrentRecordingPos + 1;
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onEnrollmentComplete(true, false, 0.0f, 0);
                    }
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onEnrollAudioBufferAvailable(this.mCacheData.toByteArray(), true);
                    }
                } else {
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onEnrollmentComplete(false, false, 0.0f, this.mRegisterState);
                    }
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onEnrollAudioBufferAvailable(this.mCacheData.toByteArray(), false);
                    }
                }
            } catch (IllegalStateException e2) {
                Log.e(VoicePrintManager.TAG, "IllegalStateException", e2);
            }
        }

        public void start() {
            this.mRecordWorking = true;
            this.mCancel = false;
            super.start();
        }
    }

    class RecordWorker extends Thread {
        private ByteArrayOutputStream mCacheData = new ByteArrayOutputStream();
        private boolean mCancel;
        private int mMaxTime;
        private boolean mRecordWorking;
        public int mRegisterState;

        public RecordWorker(int i) {
            this.mMaxTime = i;
        }

        public void cancel() {
            this.mCancel = true;
            this.mRecordWorking = false;
        }

        public void end() {
            this.mRecordWorking = false;
        }

        public void run() {
            byte[] bArr = new byte[m.ch];
            long uptimeMillis = SystemClock.uptimeMillis();
            int minBufferSize = AudioRecord.getMinBufferSize(16000, 16, 2);
            StringBuilder sb = new StringBuilder();
            sb.append("mixRecordBufferSize:");
            sb.append(minBufferSize);
            sb.append(", mRecordSource:");
            sb.append(VoicePrintManager.this.mRecordSource);
            String sb2 = sb.toString();
            String str = VoicePrintManager.TAG;
            Log.d(str, sb2);
            AudioRecord audioRecord = new AudioRecord(VoicePrintManager.this.mRecordSource, 16000, 16, 2, minBufferSize);
            audioRecord.startRecording();
            if (VoicePrintManager.this.mOutListener != null) {
                VoicePrintManager.this.mOutListener.onStartAudio();
            }
            while (true) {
                if (!this.mRecordWorking || SystemClock.uptimeMillis() - uptimeMillis > ((long) this.mMaxTime)) {
                    break;
                }
                int read = audioRecord.read(bArr, 0, m.ch);
                if (read > 0) {
                    this.mRegisterState = VoicePrintManager.this.mVoicePrintInterface.voiceprintRegister(VoicePrintManager.this.mSpeakerName, bArr);
                    if (this.mRegisterState != 3) {
                        break;
                    }
                    try {
                        this.mCacheData.write(bArr);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onEnergyLevelAvailable(VoicePrintManager.this.calculateVolume(Arrays.copyOf(bArr, read)), true);
                    }
                } else if (read < 0) {
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("local record error:");
                    sb3.append(read);
                    Log.d(str, sb3.toString());
                    if (VoicePrintManager.this.mOutListener != null) {
                        VoicePrintManager.this.mOutListener.onConflictAudio();
                    }
                }
            }
            audioRecord.stop();
            audioRecord.release();
            if (VoicePrintManager.this.mOutListener != null) {
                VoicePrintManager.this.mOutListener.onStopAudio();
            }
            if (this.mCancel) {
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onAbortEnrollmentComplete();
                }
                return;
            }
            if (this.mRegisterState == 0) {
                if (VoicePrintManager.this.mCurrentRecordingPos >= 5) {
                    VoicePrintManager.this.mCurrentRecordingPos = 0;
                }
                VoicePrintManager.this.mPcmSet[VoicePrintManager.this.mCurrentRecordingPos] = Arrays.copyOf(this.mCacheData.toByteArray(), this.mCacheData.size());
                VoicePrintManager voicePrintManager = VoicePrintManager.this;
                voicePrintManager.mCurrentRecordingPos = voicePrintManager.mCurrentRecordingPos + 1;
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onEnrollmentComplete(true, false, 0.0f, 0);
                }
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onEnrollAudioBufferAvailable(this.mCacheData.toByteArray(), true);
                }
            } else {
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onEnrollmentComplete(false, false, 0.0f, this.mRegisterState);
                }
                if (VoicePrintManager.this.mOutListener != null) {
                    VoicePrintManager.this.mOutListener.onEnrollAudioBufferAvailable(this.mCacheData.toByteArray(), false);
                }
            }
        }

        public void start() {
            this.mRecordWorking = true;
            this.mCancel = false;
            super.start();
        }
    }

    class RegisterWorker extends Thread {
        RegisterWorker() {
        }

        public void run() {
            boolean z;
            WVPListener wVPListener;
            boolean voiceprintGenerateModel = VoicePrintManager.this.mVoicePrintInterface.voiceprintGenerateModel(VoicePrintManager.this.mSpeakerName);
            String str = VoicePrintManager.TAG;
            if (voiceprintGenerateModel) {
                StringBuilder sb = new StringBuilder();
                sb.append(VoicePrintManager.this.mSpeakerName);
                sb.append(" register success.");
                Log.d(str, sb.toString());
                if (VoicePrintManager.this.mOutListener != null) {
                    wVPListener = VoicePrintManager.this.mOutListener;
                    z = true;
                } else {
                    return;
                }
            } else {
                StringBuilder sb2 = new StringBuilder();
                sb2.append(VoicePrintManager.this.mSpeakerName);
                sb2.append(" register fail.");
                Log.d(str, sb2.toString());
                if (VoicePrintManager.this.mOutListener != null) {
                    wVPListener = VoicePrintManager.this.mOutListener;
                    z = false;
                } else {
                    return;
                }
            }
            wVPListener.onGenerateModel(z, VoicePrintManager.this.mVoicePrintModelFullPath);
        }

        public void start() {
            super.start();
        }
    }

    public VoicePrintManager(String str) {
        String str2 = "";
        this.mVoicePrintModelFullPath = str2;
        this.mSpeakerName = str2;
        this.mRecordSource = 1;
        this.fileRecordWorker = null;
        VoicePrintInterface.loadLibrary(str);
        this.mVoicePrintInterface = new VoicePrintInterface();
    }

    /* access modifiers changed from: private */
    public float calculateVolume(byte[] bArr) {
        int length = bArr.length;
        if (bArr.length % 2 != 0) {
            length = bArr.length - 1;
        }
        double d = 0.0d;
        for (int i = 0; i < length; i += 2) {
            int i2 = (bArr[i] & -1) + ((bArr[i + 1] & -1) << 8);
            if (i2 >= 32768) {
                i2 = 65535 - i2;
            }
            d += Math.pow((double) i2, 2.0d);
        }
        double abs = Math.abs(Math.log10(d / ((double) (length / 2))) * 10.0d);
        if (abs > 100.0d) {
            abs = 100.0d;
        }
        return (float) Math.round(abs);
    }

    public void abortEnrollment() {
        RecordWorker recordWorker = this.mRecordWorker;
        if (recordWorker != null) {
            recordWorker.cancel();
            this.mRecordWorker = null;
            return;
        }
        WVPListener wVPListener = this.mOutListener;
        if (wVPListener != null) {
            wVPListener.onAbortEnrollmentComplete();
        }
    }

    public void cancelEnrollment() {
        abortEnrollment();
        this.mCurrentRecordingPos = 0;
    }

    public void commitEnrollment() {
        WVPListener wVPListener = this.mOutListener;
        if (wVPListener != null) {
            wVPListener.onCommitEnrollmentComplete();
        }
    }

    public void generateModel() {
        if (this.init && this.mCurrentRecordingPos >= 5) {
            new RegisterWorker().start();
        }
    }

    public String getAllRegister() {
        return this.init ? this.mVoicePrintInterface.voiceprintGetAllRegister() : "";
    }

    public int init(String str, String str2) {
        if (TextUtils.isEmpty(str) || TextUtils.isEmpty(str2)) {
            return -1;
        }
        this.mVoicePrintModelFullPath = str;
        this.mSpeakerName = str2;
        int voiceprintInit = this.mVoicePrintInterface.voiceprintInit(this.mVoicePrintModelFullPath, this.mSpeakerName);
        if (voiceprintInit == 0) {
            this.init = true;
        }
        return voiceprintInit;
    }

    public String recognizeVoice(byte[] bArr, int i, int i2) {
        return this.init ? this.mVoicePrintInterface.voiceprintRecognition(bArr, i, i2) : "error:not init";
    }

    public void release() {
        if (this.init) {
            this.mVoicePrintInterface.voiceprintDestroy();
            this.init = false;
        }
    }

    public void removeAllRegister() {
        if (this.init) {
            this.mVoicePrintInterface.voiceprintRemoveAll();
        }
    }

    public void setListener(WVPListener wVPListener) {
        this.mOutListener = wVPListener;
    }

    public void setRecordSource(int i) {
        this.mRecordSource = i;
    }

    public void startEnrollment(int i) {
        if (this.init) {
            this.mRecordWorker = new RecordWorker(i);
            this.mRecordWorker.start();
        }
    }

    public void startEnrollmentFileRecorded(AudioSource audioSource, int i) {
        if (this.init) {
            this.fileRecordWorker = new FileRecordWorker(audioSource, i);
            this.fileRecordWorker.start();
        }
    }

    public String version() {
        return this.init ? this.mVoicePrintInterface.voiceprintVersion() : "";
    }
}
