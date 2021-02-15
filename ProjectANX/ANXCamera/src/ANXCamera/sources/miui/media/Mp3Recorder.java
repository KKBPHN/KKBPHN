package miui.media;

import android.media.AudioRecord;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.os.Process;
import android.text.TextUtils;
import android.util.Log;
import com.miui.internal.variable.Android_Media_AudioRecord_class;
import com.miui.internal.variable.Android_Media_AudioRecord_class.Factory;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Mp3Recorder {
    private static final Android_Media_AudioRecord_class AudioRecordClass = Factory.getInstance().get();
    public static final int ERR_COULD_NOT_START = 6;
    public static final int ERR_ENCODE_PCM_FAILED = 4;
    public static final int ERR_FILE_NOT_EXIST = 8;
    public static final int ERR_ILLEGAL_STATE = 1;
    public static final int ERR_MAX_SIZE_REACHED = 7;
    public static final int ERR_OUT_STREAM_NOT_READY = 2;
    public static final int ERR_RECORD_PCM_FAILED = 3;
    public static final int ERR_WRITE_TO_FILE = 5;
    private static final String LOG_TAG = "Mp3Recorder";
    private static final int MP3_RECORDER_EVENT_ERROR = 1;
    private static final int STATE_IDEL = 0;
    private static final int STATE_PAUSED = 3;
    private static final int STATE_PREPARED = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_STOPED = 4;
    public static final int VBR_QUALITY_HIGH = 2;
    public static final int VBR_QUALITY_LOW = 9;
    public static final int VBR_QUALITY_MEDIUM = 6;
    /* access modifiers changed from: private */
    public byte[] bufferMP3;
    /* access modifiers changed from: private */
    public short[] bufferPCM;
    /* access modifiers changed from: private */
    public int mAudioChannel;
    private int mAudioSource;
    /* access modifiers changed from: private */
    public int mChannelCount;
    private long mCountRecordingSamples;
    /* access modifiers changed from: private */
    public long mCurrentRecordingSize;
    private Handler mEventHandler;
    /* access modifiers changed from: private */
    public int mMaxAmplitude;
    /* access modifiers changed from: private */
    public long mMaxFileSize;
    private int mMinBufferSize;
    /* access modifiers changed from: private */
    public Mp3Encoder mMp3Encoder;
    /* access modifiers changed from: private */
    public RecordingErrorListener mOnErrorListener;
    private int mOutBitRate;
    private int mOutMode;
    /* access modifiers changed from: private */
    public File mOutputFile;
    /* access modifiers changed from: private */
    public String mOutputFilePath;
    /* access modifiers changed from: private */
    public FileOutputStream mOutputStream;
    private String mParams;
    private int mQuality;
    /* access modifiers changed from: private */
    public AudioRecord mRecorder;
    /* access modifiers changed from: private */
    public int mRecordingState;
    private RecordingThread mRecordingThread;
    private int mSampleRate;
    private int mVBRQuality;

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 1 && Mp3Recorder.this.mOnErrorListener != null) {
                Mp3Recorder.this.mOnErrorListener.onError(Mp3Recorder.this, message.arg1);
            }
            super.handleMessage(message);
        }
    }

    public interface RecordingErrorListener {
        void onError(Mp3Recorder mp3Recorder, int i);
    }

    class RecordingThread extends Thread {
        private RecordingThread() {
        }

        public void run() {
            Mp3Recorder mp3Recorder;
            int i;
            int i2;
            Process.setThreadPriority(-19);
            String str = Mp3Recorder.LOG_TAG;
            Log.v(str, "RecordingThread started");
            if (Mp3Recorder.this.mRecordingState != 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error illegal state: ");
                sb.append(Mp3Recorder.this.mRecordingState);
                Log.e(str, sb.toString());
                Mp3Recorder.this.notifyError(1);
            } else if (Mp3Recorder.this.mOutputStream == null) {
                Log.e(str, "Error out put stream not ready");
                Mp3Recorder.this.notifyError(2);
            } else {
                while (true) {
                    if (Mp3Recorder.this.mRecordingState != 2) {
                        break;
                    }
                    int read = Mp3Recorder.this.mRecorder.read(Mp3Recorder.this.bufferPCM, 0, Mp3Recorder.this.bufferPCM.length);
                    Mp3Recorder.access$614(Mp3Recorder.this, (long) read);
                    if (read <= 0) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error record sample failed, read size: ");
                        sb2.append(read);
                        Log.e(str, sb2.toString());
                        mp3Recorder = Mp3Recorder.this;
                        i = 3;
                        break;
                    }
                    Mp3Recorder mp3Recorder2 = Mp3Recorder.this;
                    mp3Recorder2.mMaxAmplitude = mp3Recorder2.findMaxAmplitude(mp3Recorder2.bufferPCM, read);
                    if (Mp3Recorder.this.mAudioChannel == 16) {
                        i2 = Mp3Recorder.this.mMp3Encoder.encode(Mp3Recorder.this.bufferPCM, Mp3Recorder.this.bufferPCM, read, Mp3Recorder.this.bufferMP3, Mp3Recorder.this.bufferMP3.length);
                    } else {
                        i2 = Mp3Recorder.this.mMp3Encoder.encodeInterleaved(Mp3Recorder.this.bufferPCM, read / Mp3Recorder.this.mChannelCount, Mp3Recorder.this.bufferMP3, Mp3Recorder.this.bufferMP3.length);
                    }
                    if (i2 <= 0) {
                        if (i2 == 0) {
                            Log.e(str, "Not a complete frame samples to encode: skiped");
                        } else {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Error encode PCM failed, encode size: ");
                            sb3.append(i2);
                            sb3.append(" read size: ");
                            sb3.append(read);
                            Log.e(str, sb3.toString());
                            if (Mp3Recorder.this.mOnErrorListener != null) {
                                mp3Recorder = Mp3Recorder.this;
                                i = 4;
                            }
                        }
                    } else if (!Mp3Recorder.this.mOutputFile.exists()) {
                        mp3Recorder = Mp3Recorder.this;
                        i = 8;
                        break;
                    } else {
                        try {
                            Mp3Recorder.this.mOutputStream.write(Mp3Recorder.this.bufferMP3, 0, i2);
                            Mp3Recorder.access$1514(Mp3Recorder.this, (long) i2);
                            if (Mp3Recorder.this.mCurrentRecordingSize >= Mp3Recorder.this.mMaxFileSize) {
                                Mp3Recorder.this.notifyError(7);
                                break;
                            }
                        } catch (IOException unused) {
                            StringBuilder sb4 = new StringBuilder();
                            sb4.append("Error when write sample to file: ");
                            sb4.append(Mp3Recorder.this.mOutputFilePath);
                            Log.e(str, sb4.toString());
                            mp3Recorder = Mp3Recorder.this;
                            i = 5;
                            mp3Recorder.notifyError(i);
                            Mp3Recorder.this.mMaxAmplitude = 0;
                            Log.v(str, "RecordingThread stoped");
                        }
                    }
                }
                Mp3Recorder.this.mMaxAmplitude = 0;
                Log.v(str, "RecordingThread stoped");
            }
        }
    }

    public Mp3Recorder() {
        EventHandler eventHandler;
        Looper myLooper = Looper.myLooper();
        if (myLooper != null) {
            eventHandler = new EventHandler(myLooper);
        } else {
            Looper mainLooper = Looper.getMainLooper();
            if (mainLooper != null) {
                eventHandler = new EventHandler(mainLooper);
            } else {
                Log.e(LOG_TAG, "Could not create event handler");
                this.mEventHandler = null;
                reset();
            }
        }
        this.mEventHandler = eventHandler;
        reset();
    }

    static /* synthetic */ long access$1514(Mp3Recorder mp3Recorder, long j) {
        long j2 = mp3Recorder.mCurrentRecordingSize + j;
        mp3Recorder.mCurrentRecordingSize = j2;
        return j2;
    }

    static /* synthetic */ long access$614(Mp3Recorder mp3Recorder, long j) {
        long j2 = mp3Recorder.mCountRecordingSamples + j;
        mp3Recorder.mCountRecordingSamples = j2;
        return j2;
    }

    /* JADX WARNING: type inference failed for: r3v0, types: [short[]] */
    /* JADX WARNING: type inference failed for: r0v0 */
    /* JADX WARNING: type inference failed for: r0v1, types: [int] */
    /* JADX WARNING: type inference failed for: r1v0, types: [short] */
    /* JADX WARNING: type inference failed for: r1v1 */
    /* JADX WARNING: type inference failed for: r0v2 */
    /* JADX WARNING: type inference failed for: r0v3 */
    /* JADX WARNING: type inference failed for: r1v2, types: [short] */
    /* JADX WARNING: type inference failed for: r1v3, types: [int, short] */
    /* JADX WARNING: type inference failed for: r1v4, types: [int] */
    /* JADX WARNING: type inference failed for: r0v4 */
    /* JADX WARNING: type inference failed for: r0v5 */
    /* JADX WARNING: type inference failed for: r1v5 */
    /* JADX WARNING: type inference failed for: r1v6 */
    /* access modifiers changed from: private */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=null, for r1v0, types: [short] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=null, for r1v2, types: [short] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=null, for r1v3, types: [int, short] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short[], code=null, for r3v0, types: [short[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r0v2
  assigns: []
  uses: []
  mth insns count: 16
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 8 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public int findMaxAmplitude(short[] r3, int i) {
        int i2 = 0;
        ? r0 = 0;
        while (i2 < i) {
            ? r1 = r3[i2] < 0 ? -r3[i2] : r3[i2];
            i2++;
            r0 = r1 > r0 ? r1 : r0;
        }
        return r0;
    }

    /* access modifiers changed from: private */
    public void notifyError(int i) {
        Handler handler = this.mEventHandler;
        if (handler != null) {
            Message obtainMessage = handler.obtainMessage(1);
            obtainMessage.arg1 = i;
            this.mEventHandler.sendMessage(obtainMessage);
        }
    }

    private void prepareExtraParameters(AudioRecord audioRecord, String str) {
        if (!TextUtils.isEmpty(str)) {
            boolean isExtraParamSupported = AudioRecordClass.isExtraParamSupported();
            String str2 = LOG_TAG;
            if (isExtraParamSupported) {
                int parameters = AudioRecordClass.setParameters(audioRecord, str);
                StringBuilder sb = new StringBuilder();
                sb.append("setParameters: ");
                sb.append(parameters);
                Log.d(str2, sb.toString());
                return;
            }
            Log.e(str2, "Do not support extra parameters");
        }
    }

    public int getMaxAmplitude() {
        return this.mMaxAmplitude;
    }

    public long getRecordingSizeInByte() {
        return this.mCurrentRecordingSize;
    }

    public long getRecordingTimeInMillis() {
        return (long) (((((double) this.mCountRecordingSamples) / ((double) this.mSampleRate)) * 1000.0d) / ((double) this.mChannelCount));
    }

    public boolean isExtraParamSupported() {
        return AudioRecordClass.isExtraParamSupported();
    }

    public boolean isPaused() {
        return this.mRecordingState == 3;
    }

    public synchronized void pause() {
        if (this.mRecordingState != 2 || this.mRecordingThread == null) {
            throw new IllegalStateException("Recording not started");
        }
        this.mRecordingState = 3;
        try {
            this.mRecordingThread.join();
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "InterruptedException when pause", e);
        }
        this.mRecorder.stop();
        Log.v(LOG_TAG, "AudioRecord stoped");
        this.mRecorder.release();
        Log.v(LOG_TAG, "AudioRecord released");
        this.mRecorder = null;
        Log.v(LOG_TAG, "Mp3Recorder paused");
    }

    public void prepare() {
        int i = 2;
        this.mMinBufferSize = AudioRecord.getMinBufferSize(this.mSampleRate, this.mAudioChannel, 2);
        int i2 = this.mMinBufferSize;
        String str = LOG_TAG;
        if (i2 >= 0) {
            this.bufferPCM = new short[(i2 * 2)];
            AudioRecord audioRecord = new AudioRecord(this.mAudioSource, this.mSampleRate, this.mAudioChannel, 2, this.bufferPCM.length);
            this.mRecorder = audioRecord;
            prepareExtraParameters(this.mRecorder, this.mParams);
            Log.v(str, "Apply new AudioRecord");
            this.mOutMode = this.mAudioChannel == 12 ? 0 : 3;
            if (this.mAudioChannel != 12) {
                i = 1;
            }
            this.mChannelCount = i;
            this.bufferMP3 = new byte[((int) ((((double) this.bufferPCM.length) * 1.25d) + 7200.0d))];
            this.mMp3Encoder.setInSampleRate(this.mSampleRate);
            this.mMp3Encoder.setOutMode(this.mOutMode);
            this.mMp3Encoder.setChannelCount(this.mChannelCount);
            this.mMp3Encoder.setOutSampleRate(this.mSampleRate);
            this.mMp3Encoder.setOutBitRate(this.mOutBitRate);
            this.mMp3Encoder.setQuality(this.mQuality);
            this.mMp3Encoder.setVBRQuality(this.mVBRQuality);
            this.mMp3Encoder.init();
            this.mOutputFile = new File(this.mOutputFilePath);
            this.mRecordingState = 1;
            Log.v(str, "Mp3Recorder prepared");
            return;
        }
        Log.e(str, "Error when getting min buffer size");
        throw new IllegalStateException("Could not calculate the min buffer size");
    }

    public void release() {
        AudioRecord audioRecord = this.mRecorder;
        String str = LOG_TAG;
        if (audioRecord != null) {
            audioRecord.release();
            this.mRecorder = null;
            Log.v(str, "AudioRecord released");
        }
        this.mMp3Encoder.close();
        Log.v(str, "Mp3Recorder released");
    }

    public void reset() {
        this.mRecordingState = 0;
        this.mRecorder = null;
        this.mAudioSource = 1;
        this.mSampleRate = 44100;
        this.mAudioChannel = 16;
        this.mQuality = 0;
        this.mRecordingState = 0;
        this.mOutBitRate = 64;
        this.mCountRecordingSamples = 0;
        this.mCurrentRecordingSize = 0;
        this.mMaxFileSize = Long.MAX_VALUE;
        this.mVBRQuality = -1;
        this.mMp3Encoder = new Mp3Encoder();
    }

    public synchronized void resume() {
        if (this.mRecordingState == 3) {
            AudioRecord audioRecord = new AudioRecord(this.mAudioSource, this.mSampleRate, this.mAudioChannel, 2, this.bufferPCM.length);
            this.mRecorder = audioRecord;
            Log.v(LOG_TAG, "Apply new AudioRecord");
            prepareExtraParameters(this.mRecorder, this.mParams);
            this.mRecorder.startRecording();
            if (this.mRecorder.getRecordingState() == 3) {
                Log.v(LOG_TAG, "AudioRecord started");
                this.mRecordingState = 2;
                this.mRecordingThread = new RecordingThread();
                this.mRecordingThread.start();
                Log.v(LOG_TAG, "Mp3Recorder resumed");
            } else {
                throw new IllegalStateException("Mp3 record could not start: other input already started");
            }
        } else {
            Log.e(LOG_TAG, "Recording is going on");
            throw new IllegalStateException("Recording is going on");
        }
    }

    public void setAudioChannel(int i) {
        this.mAudioChannel = i;
    }

    public void setAudioSamplingRate(int i) {
        this.mSampleRate = i;
    }

    public void setAudioSource(int i) {
        this.mAudioSource = i;
    }

    public void setExtraParameters(String str) {
        if (AudioRecordClass.isExtraParamSupported()) {
            this.mParams = str;
            return;
        }
        throw new IllegalArgumentException("Do not support extra parameter");
    }

    public void setMaxDuration(long j) {
        this.mMaxFileSize = j > 0 ? j * ((long) (this.mOutBitRate / 8)) : Long.MAX_VALUE;
    }

    public void setMaxFileSize(long j) {
        if (j <= 0) {
            j = Long.MAX_VALUE;
        }
        this.mMaxFileSize = j;
    }

    public void setOnErrorListener(RecordingErrorListener recordingErrorListener) {
        this.mOnErrorListener = recordingErrorListener;
    }

    public void setOutBitRate(int i) {
        this.mOutBitRate = i;
    }

    public void setOutputFile(String str) {
        this.mOutputFilePath = str;
    }

    public void setQuality(int i) {
        this.mQuality = i;
    }

    public void setVBRQuality(int i) {
        this.mVBRQuality = i;
    }

    public synchronized void start() {
        if (this.mRecordingThread != null || this.mRecordingState == 2) {
            Log.e(LOG_TAG, "Recording has started");
            throw new IllegalStateException("Recording has already started");
        } else if (this.mRecordingState == 1) {
            this.mCountRecordingSamples = 0;
            this.mCurrentRecordingSize = 0;
            this.mRecordingState = 2;
            this.mRecorder.startRecording();
            if (this.mRecorder.getRecordingState() == 3) {
                this.mOutputStream = new FileOutputStream(this.mOutputFile);
                Log.v(LOG_TAG, "AudioRecord started");
                this.mRecordingThread = new RecordingThread();
                this.mRecordingThread.start();
                Log.v(LOG_TAG, "Mp3Recorder started");
            } else {
                throw new IllegalStateException("Mp3 record could not start: other input already started");
            }
        } else {
            Log.e(LOG_TAG, "Recorder not prepared");
            throw new IllegalStateException("Recorder not prepared");
        }
    }

    public synchronized void stop() {
        String str;
        String str2;
        if (this.mRecordingState != 3) {
            if (this.mRecordingState != 2) {
                String str3 = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Recorder should not be stopped in state:");
                sb.append(this.mRecordingState);
                Log.e(str3, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Recorder shoul not be stopped in state : ");
                sb2.append(this.mRecordingState);
                throw new IllegalStateException(sb2.toString());
            }
        }
        this.mRecordingState = 4;
        try {
            if (this.mRecordingThread != null && this.mRecordingThread.isAlive()) {
                this.mRecordingThread.join();
            }
        } catch (InterruptedException e) {
            Log.e(LOG_TAG, "InterruptedException when stop", e);
        } catch (Throwable th) {
            try {
                this.mOutputStream.close();
            } catch (IOException e2) {
                Log.e(LOG_TAG, "Error file cannot be closed", e2);
            }
            if (this.mRecorder != null) {
                this.mRecorder.stop();
                Log.v(LOG_TAG, "AudioRecord stoped");
            }
            throw th;
        }
        this.mRecordingThread = null;
        int flush = this.mMp3Encoder.flush(this.bufferMP3, this.bufferMP3.length);
        if (flush > 0) {
            try {
                this.mOutputStream.write(this.bufferMP3, 0, flush);
                if (this.mVBRQuality >= 0 && this.mVBRQuality <= 9) {
                    this.mMp3Encoder.writeVBRHeader(this.mOutputFilePath);
                }
                try {
                    this.mOutputStream.close();
                } catch (IOException e3) {
                    Log.e(LOG_TAG, "Error file cannot be closed", e3);
                }
                if (this.mRecorder != null) {
                    this.mRecorder.stop();
                    str = LOG_TAG;
                    str2 = "AudioRecord stoped";
                }
            } catch (IOException e4) {
                Log.e(LOG_TAG, "Error file cannot be written when flush", e4);
                try {
                    this.mOutputStream.close();
                } catch (IOException e5) {
                    Log.e(LOG_TAG, "Error file cannot be closed", e5);
                }
                if (this.mRecorder != null) {
                    this.mRecorder.stop();
                    str = LOG_TAG;
                    str2 = "AudioRecord stoped";
                }
            }
            Log.v(LOG_TAG, "Mp3Recorder stoped");
        } else {
            throw new IllegalStateException("Buffer flush must greater than 0");
        }
        Log.v(str, str2);
        Log.v(LOG_TAG, "Mp3Recorder stoped");
    }
}
