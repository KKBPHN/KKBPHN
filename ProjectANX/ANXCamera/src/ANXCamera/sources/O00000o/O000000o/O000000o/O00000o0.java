package O00000o.O000000o.O000000o;

import android.media.AudioRecord;
import android.util.Log;
import com.ss.android.medialib.audio.AudioDataProcessThread;
import com.ss.android.medialib.common.LogUtil;
import com.ss.android.vesdk.VELogUtil;
import com.xiaomi.asr.engine.record.AudioType.Frequency;

public class O00000o0 {
    private static final String TAG = "BufferedAudioRecorder";
    protected static int channelConfigOffset = -1;
    protected static int[] channelConfigSuggested = {12, 16, 1};
    protected static int sampleRateOffset = -1;
    protected static int[] sampleRateSuggested = {44100, 8000, Frequency.FREQ_11KHZ, 16000, Frequency.FREQ_22KHZ};
    O000000o OO000OO;
    boolean OO000Oo = false;
    int OO000o0 = 1;
    AudioDataProcessThread OoOOO;
    AudioRecord audio;
    int audioFormat = 2;
    int bufferSizeInBytes = 0;
    int channelConfig = -1;
    boolean isRecording = false;
    int sampleRateInHz = -1;

    public O00000o0(O000000o o000000o) {
        this.OO000OO = o000000o;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:?, code lost:
        new java.lang.Thread(new O00000o.O000000o.O000000o.O00000Oo(r2, r3, r5)).start();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:21:0x0042, code lost:
        java.lang.Runtime.getRuntime().gc();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:24:?, code lost:
        java.lang.Thread.sleep(100);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:25:0x004e, code lost:
        java.lang.System.runFinalization();
        new java.lang.Thread(new O00000o.O000000o.O000000o.O00000Oo(r2, r3, r5)).start();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x0019, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void O000000o(double d, boolean z) {
        VELogUtil.i(TAG, "startRecording() called");
        synchronized (this) {
            if (this.isRecording) {
                VELogUtil.w(TAG, "recorder is started");
                if (z) {
                    O00000Oo(d);
                }
            } else {
                if (this.audio == null) {
                    init(this.OO000o0);
                    if (this.audio == null) {
                        VELogUtil.e(TAG, "recorder is null");
                        return;
                    }
                }
                this.isRecording = true;
            }
        }
    }

    public boolean O00000Oo(double d) {
        StringBuilder sb = new StringBuilder();
        sb.append("startFeeding() called with: speed = [");
        sb.append(d);
        sb.append("]");
        String sb2 = sb.toString();
        String str = TAG;
        VELogUtil.i(str, sb2);
        if (this.isRecording) {
            AudioDataProcessThread audioDataProcessThread = this.OoOOO;
            if (audioDataProcessThread != null) {
                if (audioDataProcessThread.isProcessing()) {
                    VELogUtil.w(str, "startFeeding 失败，已经调用过一次了");
                    return false;
                }
                this.OO000Oo = false;
                this.OoOOO.startFeeding(this.sampleRateInHz, d);
                return true;
            }
        }
        VELogUtil.w(str, "startFeeding 录音未启动，将先启动startRecording");
        O000000o(d, true);
        return true;
    }

    public int O000OOoO(int i) {
        return 16 == i ? 1 : 2;
    }

    public void Oo0ooO() {
        synchronized (this) {
            this.OO000Oo = true;
        }
    }

    public void discard() {
        AudioDataProcessThread audioDataProcessThread = this.OoOOO;
        if (audioDataProcessThread != null) {
            audioDataProcessThread.discard();
        }
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

    /* JADX WARNING: Removed duplicated region for block: B:41:0x0168  */
    /* JADX WARNING: Removed duplicated region for block: B:43:0x017d  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void init(int i) {
        int i2;
        StringBuilder sb;
        int state;
        String sb2;
        int i3;
        String str;
        int i4;
        int i5;
        int i6;
        int[] iArr;
        int i7;
        int i8;
        String str2 = " ";
        this.OO000o0 = i;
        AudioRecord audioRecord = this.audio;
        String str3 = TAG;
        if (audioRecord != null) {
            sb2 = "second time audio init(), skip";
        } else {
            int i9 = -1;
            try {
                if (!(channelConfigOffset == -1 || sampleRateOffset == -1)) {
                    this.channelConfig = channelConfigSuggested[channelConfigOffset];
                    this.sampleRateInHz = sampleRateSuggested[sampleRateOffset];
                    this.bufferSizeInBytes = AudioRecord.getMinBufferSize(this.sampleRateInHz, this.channelConfig, this.audioFormat);
                    AudioRecord audioRecord2 = new AudioRecord(i, this.sampleRateInHz, this.channelConfig, this.audioFormat, this.bufferSizeInBytes);
                    this.audio = audioRecord2;
                }
            } catch (Exception e) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append("使用预设配置");
                sb3.append(channelConfigOffset);
                sb3.append(",");
                sb3.append(sampleRateOffset);
                sb3.append("实例化audio recorder失败，重新测试配置。");
                sb3.append(e);
                VELogUtil.e(str3, sb3.toString());
                this.OO000OO.lackPermission();
            }
            int i10 = 1;
            if (this.audio == null) {
                channelConfigOffset = -1;
                int[] iArr2 = channelConfigSuggested;
                int length = iArr2.length;
                int i11 = 0;
                boolean z = false;
                while (true) {
                    if (i11 >= length) {
                        break;
                    }
                    this.channelConfig = iArr2[i11];
                    channelConfigOffset += i10;
                    sampleRateOffset = i9;
                    int[] iArr3 = sampleRateSuggested;
                    int length2 = iArr3.length;
                    int i12 = 0;
                    while (true) {
                        if (i12 >= length2) {
                            str = str2;
                            i4 = i11;
                            i2 = i10;
                            break;
                        }
                        int i13 = iArr3[i12];
                        sampleRateOffset += i10;
                        try {
                            this.bufferSizeInBytes = AudioRecord.getMinBufferSize(i13, this.channelConfig, this.audioFormat);
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("试用hz ");
                            sb4.append(i13);
                            sb4.append(str2);
                            sb4.append(this.channelConfig);
                            sb4.append(str2);
                            sb4.append(this.audioFormat);
                            VELogUtil.e(str3, sb4.toString());
                            if (this.bufferSizeInBytes > 0) {
                                this.sampleRateInHz = i13;
                                int i14 = this.sampleRateInHz;
                                int i15 = this.channelConfig;
                                int i16 = i14;
                                r3 = r3;
                                str = str2;
                                i8 = i13;
                                i5 = i12;
                                int i17 = i16;
                                i6 = length2;
                                int i18 = i15;
                                iArr = iArr3;
                                i4 = i11;
                                try {
                                    AudioRecord audioRecord3 = new AudioRecord(i, i17, i18, this.audioFormat, this.bufferSizeInBytes);
                                    this.audio = audioRecord3;
                                    i2 = 1;
                                    z = true;
                                    break;
                                } catch (Exception e2) {
                                    e = e2;
                                    this.sampleRateInHz = 0;
                                    this.audio = null;
                                    StringBuilder sb5 = new StringBuilder();
                                    sb5.append("apply audio record sample rate ");
                                    sb5.append(i8);
                                    sb5.append(" failed: ");
                                    sb5.append(e.getMessage());
                                    VELogUtil.e(str3, sb5.toString());
                                    i7 = 1;
                                    sampleRateOffset++;
                                    i12 = i5 + 1;
                                    i10 = i7;
                                    iArr3 = iArr;
                                    i11 = i4;
                                    length2 = i6;
                                    str2 = str;
                                }
                            } else {
                                str = str2;
                                int i19 = i13;
                                i5 = i12;
                                i6 = length2;
                                iArr = iArr3;
                                i4 = i11;
                                sampleRateOffset++;
                                i7 = 1;
                                i12 = i5 + 1;
                                i10 = i7;
                                iArr3 = iArr;
                                i11 = i4;
                                length2 = i6;
                                str2 = str;
                            }
                        } catch (Exception e3) {
                            e = e3;
                            str = str2;
                            i8 = i13;
                            i5 = i12;
                            i6 = length2;
                            iArr = iArr3;
                            i4 = i11;
                            this.sampleRateInHz = 0;
                            this.audio = null;
                            StringBuilder sb52 = new StringBuilder();
                            sb52.append("apply audio record sample rate ");
                            sb52.append(i8);
                            sb52.append(" failed: ");
                            sb52.append(e.getMessage());
                            VELogUtil.e(str3, sb52.toString());
                            i7 = 1;
                            sampleRateOffset++;
                            i12 = i5 + 1;
                            i10 = i7;
                            iArr3 = iArr;
                            i11 = i4;
                            length2 = i6;
                            str2 = str;
                        }
                    }
                    if (z) {
                        break;
                    }
                    i11 = i4 + 1;
                    i10 = i2;
                    str2 = str;
                    i9 = -1;
                }
                if (this.sampleRateInHz > 0) {
                    sb = new StringBuilder();
                    sb.append("!Init audio recorder failed, hz ");
                    state = this.sampleRateInHz;
                } else {
                    if (this.channelConfig != 16) {
                        i2 = 2;
                    }
                    this.OO000OO.initAudioConfig(this.sampleRateInHz, i2);
                    sb = new StringBuilder();
                    sb.append("Init audio recorder succeed, apply audio record sample rate ");
                    sb.append(this.sampleRateInHz);
                    sb.append(" buffer ");
                    sb.append(this.bufferSizeInBytes);
                    sb.append(" state ");
                    state = this.audio.getState();
                }
                sb.append(state);
                sb2 = sb.toString();
            }
            i2 = i3;
            if (this.sampleRateInHz > 0) {
            }
            sb.append(state);
            sb2 = sb.toString();
        }
        VELogUtil.e(str3, sb2);
    }

    public synchronized boolean isProcessing() {
        boolean z;
        z = this.OoOOO != null && this.OoOOO.isProcessing();
        return z;
    }

    public boolean stopFeeding() {
        String str;
        String str2 = TAG;
        VELogUtil.i(str2, "stopFeeding() called");
        if (!this.isRecording || this.audio != null) {
            if (this.isRecording) {
                AudioDataProcessThread audioDataProcessThread = this.OoOOO;
                if (audioDataProcessThread != null) {
                    if (!audioDataProcessThread.isProcessing()) {
                        str = "stopFeeding 失败，请先startFeeding再stopFeeding";
                        VELogUtil.e(str2, str);
                        return false;
                    }
                    this.OoOOO.stopFeeding();
                    return true;
                }
            }
            str = "stopFeeding 失败，请先调用startRecording";
            VELogUtil.e(str2, str);
            return false;
        }
        VELogUtil.e(str2, "stopFeeding: 状态异常，重置状态");
        this.isRecording = false;
        this.OO000Oo = true;
        AudioDataProcessThread audioDataProcessThread2 = this.OoOOO;
        if (audioDataProcessThread2 != null) {
            audioDataProcessThread2.stop();
        }
        return false;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:18:0x0035, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean stopRecording() {
        synchronized (this) {
            Log.d(TAG, "stopRecording() called");
            if (!this.isRecording) {
                return false;
            }
            this.isRecording = false;
            if (this.audio == null) {
                LogUtil.e(TAG, "未启动音频模块但调用stopRecording");
            } else if (this.audio.getState() != 0) {
                this.audio.stop();
            }
            if (this.OoOOO != null) {
                this.OoOOO.stop();
            }
        }
    }

    public void unInit() {
        if (this.isRecording) {
            stopRecording();
        }
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
        VELogUtil.i(TAG, "unInit()");
    }

    public void waitUtilAudioProcessDone() {
        AudioDataProcessThread audioDataProcessThread = this.OoOOO;
        if (audioDataProcessThread != null) {
            audioDataProcessThread.waitUtilAudioProcessDone();
        }
    }
}
