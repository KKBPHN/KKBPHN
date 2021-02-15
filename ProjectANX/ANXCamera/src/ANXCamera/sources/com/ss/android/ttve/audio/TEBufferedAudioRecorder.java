package com.ss.android.ttve.audio;

import android.media.AudioRecord;
import android.util.Log;
import com.xiaomi.asr.engine.record.AudioType.Frequency;

public class TEBufferedAudioRecorder {
    private static final String TAG = "TEBufferedAudioRecorder";
    protected static int channelConfigOffset = -1;
    protected static int[] channelConfigSuggested = {12, 16, 1};
    protected static int sampleRateOffset = -1;
    protected static int[] sampleRateSuggested = {44100, 8000, Frequency.FREQ_11KHZ, 16000, Frequency.FREQ_22KHZ};
    AudioRecord audio;
    TEAudioWriterInterface audioCaller;
    int audioFormat = 2;
    int bufferSizeInBytes = 0;
    int channelConfig = -1;
    boolean isRecording = false;
    int sampleRateInHz = -1;

    public TEBufferedAudioRecorder(TEAudioWriterInterface tEAudioWriterInterface) {
        this.audioCaller = tEAudioWriterInterface;
    }

    /* access modifiers changed from: protected */
    public void finalize() {
        AudioRecord audioRecord = this.audio;
        if (audioRecord != null) {
            try {
                if (audioRecord.getState() != 0) {
                    this.audio.stop();
                }
                this.audio.release();
            } catch (Exception unused) {
            }
            this.audio = null;
        }
        super.finalize();
    }

    public int getSampleRateInHz() {
        return this.sampleRateInHz;
    }

    public void init(int i) {
        int i2;
        String str = " ";
        AudioRecord audioRecord = this.audio;
        String str2 = TAG;
        if (audioRecord != null) {
            Log.e(str2, "second time audio init(), skip");
            return;
        }
        int i3 = -1;
        try {
            if (!(channelConfigOffset == -1 || sampleRateOffset == -1)) {
                this.channelConfig = channelConfigSuggested[channelConfigOffset];
                this.sampleRateInHz = sampleRateSuggested[sampleRateOffset];
                this.bufferSizeInBytes = AudioRecord.getMinBufferSize(this.sampleRateInHz, this.channelConfig, this.audioFormat);
                AudioRecord audioRecord2 = new AudioRecord(i, this.sampleRateInHz, this.channelConfig, this.audioFormat, this.bufferSizeInBytes);
                this.audio = audioRecord2;
            }
        } catch (Exception e) {
            StringBuilder sb = new StringBuilder();
            sb.append("使用预设配置");
            sb.append(channelConfigOffset);
            sb.append(",");
            sb.append(sampleRateOffset);
            sb.append("实例化audio recorder失败，重新测试配置。");
            sb.append(e);
            Log.e(str2, sb.toString());
        }
        if (this.audio == null) {
            channelConfigOffset = -1;
            int[] iArr = channelConfigSuggested;
            int length = iArr.length;
            int i4 = 0;
            boolean z = false;
            while (i4 < length) {
                this.channelConfig = iArr[i4];
                int i5 = 1;
                channelConfigOffset++;
                sampleRateOffset = i3;
                int[] iArr2 = sampleRateSuggested;
                int length2 = iArr2.length;
                int i6 = 0;
                while (true) {
                    if (i6 >= length2) {
                        break;
                    }
                    int i7 = iArr2[i6];
                    sampleRateOffset += i5;
                    try {
                        this.bufferSizeInBytes = AudioRecord.getMinBufferSize(i7, this.channelConfig, this.audioFormat);
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("试用hz ");
                        sb2.append(i7);
                        sb2.append(str);
                        sb2.append(this.channelConfig);
                        sb2.append(str);
                        sb2.append(this.audioFormat);
                        Log.i(str2, sb2.toString());
                        if (this.bufferSizeInBytes > 0) {
                            this.sampleRateInHz = i7;
                            int i8 = i;
                            AudioRecord audioRecord3 = new AudioRecord(i8, this.sampleRateInHz, this.channelConfig, this.audioFormat, this.bufferSizeInBytes);
                            this.audio = audioRecord3;
                            z = true;
                            break;
                        }
                        sampleRateOffset++;
                        i2 = 1;
                        i6++;
                        i5 = i2;
                    } catch (Exception e2) {
                        this.sampleRateInHz = 0;
                        this.audio = null;
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("apply audio record sample rate ");
                        sb3.append(i7);
                        sb3.append(" failed: ");
                        sb3.append(e2.getMessage());
                        Log.e(str2, sb3.toString());
                        i2 = 1;
                        sampleRateOffset++;
                    }
                }
                if (z) {
                    break;
                }
                i4++;
                i3 = -1;
            }
        }
        if (this.sampleRateInHz <= 0) {
            StringBuilder sb4 = new StringBuilder();
            sb4.append("!Init audio recorder failed, hz ");
            sb4.append(this.sampleRateInHz);
            Log.e(str2, sb4.toString());
            return;
        }
        StringBuilder sb5 = new StringBuilder();
        sb5.append("Init audio recorder succeed, apply audio record sample rate ");
        sb5.append(this.sampleRateInHz);
        sb5.append(" buffer ");
        sb5.append(this.bufferSizeInBytes);
        sb5.append(" state ");
        sb5.append(this.audio.getState());
        Log.i(str2, sb5.toString());
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0015, code lost:
        r10 = r9.audioCaller.initWavFile(r10, r9.sampleRateInHz, 2, r11, r13, r14);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0022, code lost:
        if (r10 == 0) goto L_0x003b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0024, code lost:
        r9 = new java.lang.StringBuilder();
        r9.append("init wav file failed, ret = ");
        r9.append(r10);
        android.util.Log.e(TAG, r9.toString());
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x003a, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x003b, code lost:
        new java.lang.Thread(new com.ss.android.ttve.audio.TEBufferedAudioRecorder.AnonymousClass1(r9)).start();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0048, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void startRecording(String str, double d, int i, int i2) {
        Log.e(TAG, "audio startRecording");
        synchronized (this) {
            if (!this.isRecording) {
                if (this.audio != null) {
                    this.isRecording = true;
                }
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0014, code lost:
        if (r3.audio.getState() == 0) goto L_0x001b;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0016, code lost:
        r3.audio.stop();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:12:0x001b, code lost:
        r3.audioCaller.closeWavFile();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:13:0x0021, code lost:
        return true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:20:0x0033, code lost:
        return false;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean stopRecording() {
        synchronized (this) {
            if (this.isRecording) {
                if (this.audio != null) {
                    this.isRecording = false;
                }
            }
            Log.e(TAG, "未启动音频模块但调用stopRecording");
            if (this.audio != null) {
                this.audio.release();
            }
        }
    }

    public void unInit() {
        AudioRecord audioRecord = this.audio;
        if (audioRecord != null) {
            try {
                if (audioRecord.getState() != 0) {
                    this.audio.stop();
                }
                this.audio.release();
            } catch (Exception unused) {
            }
            this.audio = null;
        }
        TEAudioWriterInterface tEAudioWriterInterface = this.audioCaller;
        if (tEAudioWriterInterface != null) {
            tEAudioWriterInterface.destroy();
        }
    }
}
