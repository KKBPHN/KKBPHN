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
import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel.MapMode;
import miui.media.Recorder.OnErrorListener;

class LocalWavRecorder implements Recorder {
    private static final Android_Media_AudioRecord_class AudioRecordClass = Factory.getInstance().get();
    private static final int EVENT_ERROR = 1;
    private static final String LOG_TAG = "WavRecorder";
    private static final int STATE_IDEL = 0;
    private static final int STATE_PAUSED = 3;
    private static final int STATE_PREPARED = 1;
    private static final int STATE_RECORDING = 2;
    private static final int STATE_STOPED = 4;
    private final int OFFSET_WAVE_HEADER_AUDIO_DATA_LENGTH = 40;
    private final int OFFSET_WAVE_HEADER_DATA_LENGTH = 4;
    private final int WAVE_HEADER_LENGTH = 44;
    private int mAudioChannel;
    private int mAudioSource;
    /* access modifiers changed from: private */
    public short[] mBufferPCM;
    private int mChannelCount;
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
    public OnErrorListener mOnErrorListener;
    /* access modifiers changed from: private */
    public File mOutputFile;
    /* access modifiers changed from: private */
    public String mOutputFilePath;
    /* access modifiers changed from: private */
    public DataOutputStream mOutputStream;
    private String mParams;
    /* access modifiers changed from: private */
    public AudioRecord mRecorder;
    /* access modifiers changed from: private */
    public int mRecordingState;
    private RecordingThread mRecordingThread;
    private int mSampleRate;

    class EventHandler extends Handler {
        public EventHandler(Looper looper) {
            super(looper);
        }

        public void handleMessage(Message message) {
            if (message.what == 1 && LocalWavRecorder.this.mOnErrorListener != null) {
                LocalWavRecorder.this.mOnErrorListener.onError(LocalWavRecorder.this, RecorderUtils.convertErrorCode(message.arg1, false));
            }
            super.handleMessage(message);
        }
    }

    class RecordingThread extends Thread {
        private RecordingThread() {
        }

        public void run() {
            LocalWavRecorder localWavRecorder;
            int i;
            Process.setThreadPriority(-19);
            String str = LocalWavRecorder.LOG_TAG;
            Log.v(str, "RecordingThread started");
            if (LocalWavRecorder.this.mRecordingState != 2) {
                StringBuilder sb = new StringBuilder();
                sb.append("Error illegal state: ");
                sb.append(LocalWavRecorder.this.mRecordingState);
                Log.e(str, sb.toString());
                LocalWavRecorder.this.notifyError(1001);
            } else if (LocalWavRecorder.this.mOutputStream == null) {
                Log.e(str, "Error out put stream not ready");
                LocalWavRecorder.this.notifyError(1002);
            } else {
                while (true) {
                    if (LocalWavRecorder.this.mRecordingState != 2) {
                        break;
                    }
                    int read = LocalWavRecorder.this.mRecorder.read(LocalWavRecorder.this.mBufferPCM, 0, LocalWavRecorder.this.mBufferPCM.length);
                    LocalWavRecorder.access$614(LocalWavRecorder.this, (long) read);
                    if (read <= 0) {
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("Error record sample failed, read size: ");
                        sb2.append(read);
                        Log.e(str, sb2.toString());
                        localWavRecorder = LocalWavRecorder.this;
                        i = 1003;
                        break;
                    }
                    LocalWavRecorder localWavRecorder2 = LocalWavRecorder.this;
                    localWavRecorder2.mMaxAmplitude = localWavRecorder2.findMaxAmplitude(localWavRecorder2.mBufferPCM, read);
                    if (!LocalWavRecorder.this.mOutputFile.exists()) {
                        localWavRecorder = LocalWavRecorder.this;
                        i = 1009;
                        break;
                    }
                    try {
                        LocalWavRecorder.this.mOutputStream.write(RecorderUtils.short2byte(LocalWavRecorder.this.mBufferPCM, read));
                        LocalWavRecorder.access$1014(LocalWavRecorder.this, (long) (read * 2));
                        if (LocalWavRecorder.this.mCurrentRecordingSize >= LocalWavRecorder.this.mMaxFileSize) {
                            LocalWavRecorder.this.notifyError(1007);
                            break;
                        }
                    } catch (IOException unused) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Error when write sample to file: ");
                        sb3.append(LocalWavRecorder.this.mOutputFilePath);
                        Log.e(str, sb3.toString());
                        localWavRecorder = LocalWavRecorder.this;
                        i = 1005;
                    }
                }
                localWavRecorder.notifyError(i);
                LocalWavRecorder.this.mMaxAmplitude = 0;
                Log.v(str, "RecordingThread stoped");
            }
        }
    }

    public LocalWavRecorder() {
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

    static /* synthetic */ long access$1014(LocalWavRecorder localWavRecorder, long j) {
        long j2 = localWavRecorder.mCurrentRecordingSize + j;
        localWavRecorder.mCurrentRecordingSize = j2;
        return j2;
    }

    static /* synthetic */ long access$614(LocalWavRecorder localWavRecorder, long j) {
        long j2 = localWavRecorder.mCountRecordingSamples + j;
        localWavRecorder.mCountRecordingSamples = j2;
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
            handler.sendMessage(handler.obtainMessage(1, i, 0));
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

    /* JADX WARNING: type inference failed for: r2v0 */
    /* JADX WARNING: type inference failed for: r3v0, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r2v1, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r3v1 */
    /* JADX WARNING: type inference failed for: r2v3 */
    /* JADX WARNING: type inference failed for: r12v0 */
    /* JADX WARNING: type inference failed for: r3v2 */
    /* JADX WARNING: type inference failed for: r2v4 */
    /* JADX WARNING: type inference failed for: r3v3, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r2v5, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r2v6, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r3v6 */
    /* JADX WARNING: type inference failed for: r3v7 */
    /* JADX WARNING: type inference failed for: r3v8, types: [java.io.RandomAccessFile] */
    /* JADX WARNING: type inference failed for: r2v7 */
    /* JADX WARNING: type inference failed for: r12v1 */
    /* JADX WARNING: type inference failed for: r3v9 */
    /* JADX WARNING: type inference failed for: r2v8 */
    /* JADX WARNING: type inference failed for: r2v9 */
    /* JADX WARNING: type inference failed for: r12v2 */
    /* JADX WARNING: type inference failed for: r3v10 */
    /* JADX WARNING: type inference failed for: r2v10 */
    /* JADX WARNING: type inference failed for: r2v11 */
    /* JADX WARNING: type inference failed for: r2v12, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r6v0, types: [java.nio.channels.FileChannel] */
    /* JADX WARNING: type inference failed for: r2v13 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r2v14 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r2v15 */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: type inference failed for: r2v16 */
    /* JADX WARNING: type inference failed for: r2v17 */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r2v8
  assigns: [?[OBJECT, ARRAY], ?[int, float, boolean, short, byte, char, OBJECT, ARRAY], java.nio.channels.FileChannel]
  uses: [?[int, boolean, OBJECT, ARRAY, byte, short, char], java.io.RandomAccessFile, ?[OBJECT, ARRAY], java.nio.channels.FileChannel]
  mth insns count: 83
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Removed duplicated region for block: B:22:0x0056 A[SYNTHETIC, Splitter:B:22:0x0056] */
    /* JADX WARNING: Removed duplicated region for block: B:25:0x005b A[Catch:{ IOException -> 0x006c }] */
    /* JADX WARNING: Removed duplicated region for block: B:32:0x0068 A[SYNTHETIC, Splitter:B:32:0x0068] */
    /* JADX WARNING: Removed duplicated region for block: B:37:0x0070 A[Catch:{ IOException -> 0x006c }] */
    /* JADX WARNING: Removed duplicated region for block: B:42:0x007e A[SYNTHETIC, Splitter:B:42:0x007e] */
    /* JADX WARNING: Removed duplicated region for block: B:47:0x0086 A[Catch:{ IOException -> 0x0082 }] */
    /* JADX WARNING: Removed duplicated region for block: B:52:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Removed duplicated region for block: B:53:? A[RETURN, SYNTHETIC] */
    /* JADX WARNING: Unknown variable types count: 15 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void updateWaveHeader() {
        ? r3;
        ? r32;
        ? r2;
        ? r33;
        ? r34;
        ? r22;
        ? r23;
        String str = "file close";
        String str2 = LOG_TAG;
        ? r24 = 0;
        try {
            ? randomAccessFile = new RandomAccessFile(this.mOutputFile, "rw");
            try {
                r23 = r24;
                r24 = randomAccessFile.getChannel();
                MappedByteBuffer map = r24.map(MapMode.READ_WRITE, 0, 44);
                RecorderUtils.updateBytes(map, 4, RecorderUtils.getBytes(this.mCurrentRecordingSize + 36));
                RecorderUtils.updateBytes(map, 40, RecorderUtils.getBytes(this.mCurrentRecordingSize));
                r23 = r24;
            } catch (FileNotFoundException e) {
                e = e;
                ? r12 = randomAccessFile;
                r33 = r22;
                ? r25 = r12;
                r32 = r32;
                r2 = r2;
                Log.e(str2, "FileNotFoundException", e);
                if (r2 != 0) {
                    r2.close();
                }
                if (r32 == 0) {
                    r32.close();
                    return;
                }
                return;
            } catch (IOException e2) {
                e = e2;
                ? r122 = randomAccessFile;
                r34 = r24;
                ? r26 = r122;
                try {
                    r32 = r32;
                    r2 = r2;
                    Log.e(str2, "IOException", e);
                    if (r2 != 0) {
                        r2.close();
                    }
                    if (r32 == 0) {
                        r32.close();
                        return;
                    }
                    return;
                } catch (Throwable th) {
                    th = th;
                    ? r123 = r32;
                    r3 = r2;
                    r24 = r123;
                    if (r3 != 0) {
                    }
                    if (r24 != 0) {
                    }
                    throw th;
                }
            } catch (Throwable th2) {
                th = th2;
                r3 = randomAccessFile;
                r24 = r23;
                if (r3 != 0) {
                    try {
                        r3.close();
                    } catch (IOException e3) {
                        Log.e(str2, str, e3);
                        throw th;
                    }
                }
                if (r24 != 0) {
                    r24.close();
                }
                throw th;
            }
            try {
                randomAccessFile.close();
                if (r24 != 0) {
                    r24.close();
                }
            } catch (IOException e4) {
                Log.e(str2, str, e4);
            }
        } catch (FileNotFoundException e5) {
            e = e5;
            r33 = 0;
            r32 = r32;
            r2 = r2;
            Log.e(str2, "FileNotFoundException", e);
            if (r2 != 0) {
            }
            if (r32 == 0) {
            }
        } catch (IOException e6) {
            e = e6;
            r34 = 0;
            r32 = r32;
            r2 = r2;
            Log.e(str2, "IOException", e);
            if (r2 != 0) {
            }
            if (r32 == 0) {
            }
        } catch (Throwable th3) {
            th = th3;
            r3 = 0;
            if (r3 != 0) {
            }
            if (r24 != 0) {
            }
            throw th;
        }
    }

    public boolean canPause() {
        return true;
    }

    public int getMaxAmplitude() {
        return this.mMaxAmplitude;
    }

    public long getRecordingSizeInByte() {
        return this.mCurrentRecordingSize;
    }

    public long getRecordingTimeInMillis() {
        return (long) (((((double) this.mCountRecordingSamples) * 2.0d) / ((double) ((this.mSampleRate * 2) * this.mChannelCount))) * 1000.0d);
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
        Log.v(LOG_TAG, "WAVRecorder paused");
    }

    public void prepare() {
        int i = 2;
        this.mMinBufferSize = AudioRecord.getMinBufferSize(this.mSampleRate, this.mAudioChannel, 2);
        int i2 = this.mMinBufferSize;
        String str = LOG_TAG;
        if (i2 >= 0) {
            this.mBufferPCM = new short[(i2 * 2)];
            AudioRecord audioRecord = new AudioRecord(this.mAudioSource, this.mSampleRate, this.mAudioChannel, 2, this.mBufferPCM.length);
            this.mRecorder = audioRecord;
            prepareExtraParameters(this.mRecorder, this.mParams);
            Log.v(str, "Apply new AudioRecord");
            if (this.mAudioChannel != 12) {
                i = 1;
            }
            this.mChannelCount = i;
            this.mOutputFile = new File(this.mOutputFilePath);
            this.mRecordingState = 1;
            Log.v(str, "WAVRecorder prepared");
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
        Log.v(str, "WAVRecorder released");
    }

    public void reset() {
        this.mRecordingState = 0;
        this.mRecorder = null;
        this.mAudioSource = 1;
        this.mSampleRate = 44100;
        this.mAudioChannel = 16;
        this.mRecordingState = 0;
        this.mCountRecordingSamples = 0;
        this.mCurrentRecordingSize = 0;
        this.mMaxFileSize = Long.MAX_VALUE;
    }

    public synchronized void resume() {
        if (this.mRecordingState == 3) {
            AudioRecord audioRecord = new AudioRecord(this.mAudioSource, this.mSampleRate, this.mAudioChannel, 2, this.mBufferPCM.length);
            this.mRecorder = audioRecord;
            Log.v(LOG_TAG, "Apply new AudioRecord");
            prepareExtraParameters(this.mRecorder, this.mParams);
            this.mRecorder.startRecording();
            if (this.mRecorder.getRecordingState() == 3) {
                Log.v(LOG_TAG, "AudioRecord started");
                this.mRecordingState = 2;
                this.mRecordingThread = new RecordingThread();
                this.mRecordingThread.start();
                Log.v(LOG_TAG, "WAVRecorder resumed");
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

    public void setAudioEncoder(int i) {
    }

    public void setAudioEncodingBitRate(int i) {
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

    public void setMaxDuration(int i) {
        this.mMaxFileSize = i > 0 ? (long) (((((i / 1000) * this.mSampleRate) * 16) * this.mChannelCount) / 8) : Long.MAX_VALUE;
    }

    public void setMaxFileSize(long j) {
        if (j <= 0) {
            j = Long.MAX_VALUE;
        }
        this.mMaxFileSize = j;
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        this.mOnErrorListener = onErrorListener;
    }

    public void setOutputFile(String str) {
        this.mOutputFilePath = str;
    }

    public void setOutputFormat(int i) {
    }

    public void setQuality(int i) {
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
                FileOutputStream fileOutputStream = new FileOutputStream(this.mOutputFile);
                this.mOutputStream = new DataOutputStream(new BufferedOutputStream(fileOutputStream));
                Log.v(LOG_TAG, "AudioRecord started");
                fileOutputStream.write(RecorderUtils.getWaveHeader(this.mAudioChannel, this.mSampleRate));
                this.mRecordingThread = new RecordingThread();
                this.mRecordingThread.start();
                Log.v(LOG_TAG, "WAVRecorder started");
            } else {
                throw new IllegalStateException("Wav record could not start: other input already started");
            }
        } else {
            Log.e(LOG_TAG, "Recorder not prepared");
            throw new IllegalStateException("Recorder not prepared");
        }
    }

    public synchronized void stop() {
        if (this.mRecordingState != 3) {
            if (this.mRecordingState != 2) {
                String str = LOG_TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Recorder should not be stopped in state:");
                sb.append(this.mRecordingState);
                Log.e(str, sb.toString());
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Recorder should not be stopped in state : ");
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
        }
        this.mRecordingThread = null;
        try {
            this.mOutputStream.close();
        } catch (IOException e2) {
            Log.e(LOG_TAG, "Error file cannot be closed", e2);
        }
        if (this.mRecorder != null) {
            this.mRecorder.stop();
            Log.v(LOG_TAG, "AudioRecord stoped");
        }
        updateWaveHeader();
        Log.v(LOG_TAG, "WavRecorder stoped");
    }
}
