package miui.media;

import android.media.MediaRecorder;
import android.media.MediaRecorder.OnErrorListener;
import android.media.MediaRecorder.OnInfoListener;
import android.os.ParcelFileDescriptor;
import android.os.SystemClock;
import android.text.TextUtils;
import android.util.Log;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import miui.reflect.Method;
import miui.reflect.NoSuchMethodException;

class LocalMediaRecorder implements Recorder {
    private static final String TAG = "Media:LocalMediaRecorder";
    private static final Method setParameter;
    private int mAudioEncoder = -1;
    private int mAudioSource = -1;
    private int mBitRate = -1;
    /* access modifiers changed from: private */
    public String mDestFilePath;
    private String mExtraParameter;
    private boolean mIsPaused;
    private int mMaxDuration = -1;
    private long mMaxSize = -1;
    private MediaRecorder mMediaRecorder = new MediaRecorder();
    private int mNumChannels = -1;
    private OnErrorListener mOnErrorListener;
    private OnInfoListener mOnInfoListener;
    private int mOutputFormat = -1;
    private RecorderAppendThread mRecorderAppendThread;
    private ParcelFileDescriptor mRecorderAppendWriteFd;
    /* access modifiers changed from: private */
    public Recorder.OnErrorListener mRecorderOnErrorListener;
    private int mRecordingDuration;
    private int mSamplingRate = -1;
    private long mSizeRecored = 0;
    private long mStartTime = 0;
    private Object mSyncer = new Object();

    class RecorderAppendThread extends Thread {
        /* access modifiers changed from: private */
        public AtomicBoolean mRunningLatch;
        private ParcelFileDescriptor mSrcFd;

        RecorderAppendThread(ParcelFileDescriptor parcelFileDescriptor) {
            this.mSrcFd = parcelFileDescriptor;
        }

        /* JADX WARNING: type inference failed for: r2v0 */
        /* JADX WARNING: type inference failed for: r4v0, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r2v1, types: [java.io.FileOutputStream] */
        /* JADX WARNING: type inference failed for: r5v0 */
        /* JADX WARNING: type inference failed for: r4v2 */
        /* JADX WARNING: type inference failed for: r2v4 */
        /* JADX WARNING: type inference failed for: r5v1, types: [java.io.FileOutputStream] */
        /* JADX WARNING: type inference failed for: r2v5, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r4v4 */
        /* JADX WARNING: type inference failed for: r5v2 */
        /* JADX WARNING: type inference failed for: r4v5 */
        /* JADX WARNING: type inference failed for: r4v6, types: [java.io.FileInputStream] */
        /* JADX WARNING: type inference failed for: r5v5 */
        /* JADX WARNING: type inference failed for: r2v7 */
        /* JADX WARNING: type inference failed for: r5v6 */
        /* JADX WARNING: type inference failed for: r5v7, types: [java.io.FileOutputStream] */
        /* JADX WARNING: type inference failed for: r2v13 */
        /* JADX WARNING: type inference failed for: r4v7 */
        /* JADX WARNING: type inference failed for: r5v8 */
        /* JADX WARNING: type inference failed for: r4v8 */
        /* JADX WARNING: type inference failed for: r5v9 */
        /* JADX WARNING: type inference failed for: r5v10 */
        /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r4v2
  assigns: []
  uses: []
  mth insns count: 114
        	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
        	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$0(DepthTraversal.java:13)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:13)
        	at jadx.core.ProcessClass.process(ProcessClass.java:30)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
        	at java.util.ArrayList.forEach(ArrayList.java:1259)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
        	at jadx.core.ProcessClass.process(ProcessClass.java:35)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
         */
        /* JADX WARNING: Removed duplicated region for block: B:44:0x0086 A[SYNTHETIC, Splitter:B:44:0x0086] */
        /* JADX WARNING: Removed duplicated region for block: B:49:0x008e A[Catch:{ IOException -> 0x008a }] */
        /* JADX WARNING: Removed duplicated region for block: B:54:0x00a1 A[SYNTHETIC] */
        /* JADX WARNING: Removed duplicated region for block: B:65:0x00b5 A[SYNTHETIC, Splitter:B:65:0x00b5] */
        /* JADX WARNING: Removed duplicated region for block: B:70:0x00bd A[Catch:{ IOException -> 0x00b9 }] */
        /* JADX WARNING: Removed duplicated region for block: B:75:0x00d0 A[SYNTHETIC] */
        /* JADX WARNING: Unknown variable types count: 8 */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public void run() {
            ? r4;
            ? r5;
            ? r42;
            ? r52;
            ? r2;
            if (LocalMediaRecorder.this.canPause()) {
                this.mRunningLatch = new AtomicBoolean(true);
                byte[] bArr = new byte[1024];
                ? r22 = 0;
                try {
                    r4 = new FileInputStream(this.mSrcFd.getFileDescriptor());
                    try {
                        ? fileOutputStream = new FileOutputStream(LocalMediaRecorder.this.mDestFilePath, true);
                        try {
                            int headerLen = LocalMediaRecorder.this.getHeaderLen();
                            int i = 0;
                            while (headerLen - i > 0) {
                                i = r4.read(bArr, 0, headerLen);
                                headerLen -= i;
                            }
                            while (true) {
                                int read = r4.read(bArr);
                                if (read > 0) {
                                    fileOutputStream.write(bArr, 0, read);
                                } else {
                                    try {
                                        break;
                                    } catch (IOException e) {
                                        Log.e(LocalMediaRecorder.TAG, "IOException", e);
                                    }
                                }
                            }
                            r4.close();
                            fileOutputStream.close();
                            this.mSrcFd.close();
                            synchronized (this.mRunningLatch) {
                                this.mRunningLatch.set(false);
                                this.mRunningLatch.notify();
                            }
                        } catch (IOException e2) {
                            e = e2;
                            r2 = r4;
                            r52 = r5;
                            try {
                                Log.e(LocalMediaRecorder.TAG, "IOException", e);
                                if (r2 != 0) {
                                }
                                if (r52 != 0) {
                                }
                                this.mSrcFd.close();
                                synchronized (this.mRunningLatch) {
                                }
                            } catch (Throwable th) {
                                th = th;
                                r42 = r2;
                                r5 = r52;
                                r22 = r5;
                                r4 = r42;
                                if (r4 != 0) {
                                    try {
                                        r4.close();
                                    } catch (IOException e3) {
                                        Log.e(LocalMediaRecorder.TAG, "IOException", e3);
                                        synchronized (this.mRunningLatch) {
                                        }
                                        throw th;
                                    }
                                }
                                if (r22 != 0) {
                                    r22.close();
                                }
                                this.mSrcFd.close();
                                synchronized (this.mRunningLatch) {
                                    this.mRunningLatch.set(false);
                                    this.mRunningLatch.notify();
                                }
                                throw th;
                            }
                        } catch (Throwable th2) {
                            th = th2;
                            r42 = r4;
                            r5 = fileOutputStream;
                            r22 = r5;
                            r4 = r42;
                            if (r4 != 0) {
                            }
                            if (r22 != 0) {
                            }
                            this.mSrcFd.close();
                            synchronized (this.mRunningLatch) {
                            }
                            throw th;
                        }
                    } catch (IOException e4) {
                        e = e4;
                        ? r53 = 0;
                        r2 = r4;
                        r52 = r53;
                        Log.e(LocalMediaRecorder.TAG, "IOException", e);
                        if (r2 != 0) {
                            try {
                                r2.close();
                            } catch (IOException e5) {
                                Log.e(LocalMediaRecorder.TAG, "IOException", e5);
                                synchronized (this.mRunningLatch) {
                                    this.mRunningLatch.set(false);
                                    this.mRunningLatch.notify();
                                }
                            }
                        }
                        if (r52 != 0) {
                            r52.close();
                        }
                        this.mSrcFd.close();
                        synchronized (this.mRunningLatch) {
                        }
                    } catch (Throwable th3) {
                        th = th3;
                        if (r4 != 0) {
                        }
                        if (r22 != 0) {
                        }
                        this.mSrcFd.close();
                        synchronized (this.mRunningLatch) {
                        }
                        throw th;
                    }
                } catch (IOException e6) {
                    e = e6;
                    r52 = 0;
                    Log.e(LocalMediaRecorder.TAG, "IOException", e);
                    if (r2 != 0) {
                    }
                    if (r52 != 0) {
                    }
                    this.mSrcFd.close();
                    synchronized (this.mRunningLatch) {
                    }
                } catch (Throwable th4) {
                    th = th4;
                    r4 = 0;
                    r22 = r22;
                    if (r4 != 0) {
                    }
                    if (r22 != 0) {
                    }
                    this.mSrcFd.close();
                    synchronized (this.mRunningLatch) {
                    }
                    throw th;
                }
            }
        }
    }

    static {
        Method method;
        try {
            method = Method.of(MediaRecorder.class, "setParameter", "(Ljava/lang/String;)V");
        } catch (NoSuchMethodException e) {
            Log.e(TAG, "Could not get method:setParameter", e);
            method = null;
        }
        setParameter = method;
    }

    private void setExtraParametersLocal(String str) {
        if (setParameter != null && !TextUtils.isEmpty(str)) {
            setParameter.invoke(MediaRecorder.class, this.mMediaRecorder, str);
        }
    }

    private void waitingForAppendThread() {
        RecorderAppendThread recorderAppendThread = this.mRecorderAppendThread;
        if (recorderAppendThread != null) {
            synchronized (recorderAppendThread.mRunningLatch) {
                while (this.mRecorderAppendThread.mRunningLatch.get()) {
                    try {
                        this.mRecorderAppendThread.mRunningLatch.wait();
                    } catch (InterruptedException e) {
                        Log.e(TAG, "InterruptedException", e);
                    }
                }
            }
            this.mRecorderAppendThread = null;
        }
    }

    public boolean canPause() {
        synchronized (this.mSyncer) {
        }
        return false;
    }

    /* access modifiers changed from: protected */
    public int getHeaderLen() {
        return 0;
    }

    public int getMaxAmplitude() {
        int maxAmplitude;
        synchronized (this.mSyncer) {
            maxAmplitude = this.mIsPaused ? 0 : this.mMediaRecorder.getMaxAmplitude();
        }
        return maxAmplitude;
    }

    public long getRecordingTimeInMillis() {
        return isPaused() ? (long) this.mRecordingDuration : (SystemClock.elapsedRealtime() - this.mStartTime) + ((long) this.mRecordingDuration);
    }

    public boolean isPaused() {
        boolean z;
        synchronized (this.mSyncer) {
            z = this.mIsPaused;
        }
        return z;
    }

    public void pause() {
        synchronized (this.mSyncer) {
            this.mIsPaused = true;
            this.mMediaRecorder.stop();
            this.mMediaRecorder.release();
            this.mMediaRecorder = null;
            this.mRecordingDuration = (int) (((long) this.mRecordingDuration) + (SystemClock.elapsedRealtime() - this.mStartTime));
            if (this.mRecorderAppendWriteFd != null) {
                try {
                    this.mRecorderAppendWriteFd.close();
                    waitingForAppendThread();
                } catch (IOException e) {
                    throw new IllegalStateException(e);
                }
            }
            this.mSizeRecored = new File(this.mDestFilePath).length();
        }
    }

    public void prepare() {
        synchronized (this.mSyncer) {
            this.mMediaRecorder.prepare();
        }
    }

    public void release() {
        synchronized (this.mSyncer) {
            resetParameters();
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.release();
                this.mMediaRecorder = null;
            }
        }
    }

    public void reset() {
        synchronized (this.mSyncer) {
            resetParameters();
            this.mMediaRecorder.reset();
        }
    }

    /* access modifiers changed from: protected */
    public void resetParameters() {
        this.mDestFilePath = null;
        this.mExtraParameter = null;
        this.mAudioSource = -1;
        this.mSamplingRate = -1;
        this.mAudioEncoder = -1;
        this.mOutputFormat = -1;
        this.mBitRate = -1;
        this.mMaxSize = -1;
        this.mNumChannels = -1;
        this.mSizeRecored = 0;
        this.mOnErrorListener = null;
        this.mOnInfoListener = null;
        this.mRecorderOnErrorListener = null;
    }

    public void resume() {
        synchronized (this.mSyncer) {
            if (this.mIsPaused) {
                this.mMediaRecorder = new MediaRecorder();
                if (!TextUtils.isEmpty(this.mExtraParameter)) {
                    setExtraParameters(this.mExtraParameter);
                }
                if (this.mAudioSource != -1) {
                    this.mMediaRecorder.setAudioSource(this.mAudioSource);
                }
                if (this.mSamplingRate != -1) {
                    this.mMediaRecorder.setAudioSamplingRate(this.mSamplingRate);
                }
                if (this.mOutputFormat != -1) {
                    this.mMediaRecorder.setOutputFormat(this.mOutputFormat);
                }
                if (this.mAudioEncoder != -1) {
                    this.mMediaRecorder.setAudioEncoder(this.mAudioEncoder);
                }
                if (this.mBitRate != -1) {
                    this.mMediaRecorder.setAudioEncodingBitRate(this.mBitRate);
                }
                if (this.mNumChannels != -1) {
                    this.mMediaRecorder.setAudioChannels(this.mNumChannels);
                }
                if (this.mMaxSize != -1) {
                    this.mMediaRecorder.setMaxFileSize(this.mMaxSize - this.mSizeRecored);
                }
                if (this.mMaxDuration != -1) {
                    this.mMediaRecorder.setMaxDuration(this.mMaxDuration);
                }
                if (this.mOnErrorListener != null) {
                    this.mMediaRecorder.setOnErrorListener(this.mOnErrorListener);
                }
                if (this.mOnInfoListener != null) {
                    this.mMediaRecorder.setOnInfoListener(this.mOnInfoListener);
                }
                ParcelFileDescriptor[] createPipe = ParcelFileDescriptor.createPipe();
                this.mRecorderAppendWriteFd = createPipe[1];
                this.mRecorderAppendThread = new RecorderAppendThread(createPipe[0]);
                this.mMediaRecorder.setOutputFile(this.mRecorderAppendWriteFd.getFileDescriptor());
                this.mRecorderAppendThread.start();
                this.mMediaRecorder.prepare();
                this.mMediaRecorder.start();
                this.mIsPaused = false;
                this.mStartTime = SystemClock.elapsedRealtime();
            }
        }
    }

    public void setAudioChannel(int i) {
        synchronized (this.mSyncer) {
            if (i == 16) {
                this.mNumChannels = 1;
            } else {
                this.mNumChannels = 2;
            }
            this.mMediaRecorder.setAudioChannels(this.mNumChannels);
        }
    }

    public void setAudioEncoder(int i) {
        synchronized (this.mSyncer) {
            this.mAudioEncoder = i;
            this.mMediaRecorder.setAudioEncoder(i);
        }
    }

    public void setAudioEncodingBitRate(int i) {
        synchronized (this.mSyncer) {
            this.mBitRate = i;
            this.mMediaRecorder.setAudioEncodingBitRate(i);
        }
    }

    public void setAudioSamplingRate(int i) {
        synchronized (this.mSyncer) {
            this.mSamplingRate = i;
            this.mMediaRecorder.setAudioSamplingRate(i);
        }
    }

    public void setAudioSource(int i) {
        synchronized (this.mSyncer) {
            this.mAudioSource = i;
            this.mMediaRecorder.setAudioSource(i);
        }
    }

    public void setExtraParameters(String str) {
        synchronized (this.mSyncer) {
            this.mExtraParameter = str;
            setExtraParametersLocal(str);
        }
    }

    public void setMaxDuration(int i) {
        synchronized (this.mSyncer) {
            this.mMaxDuration = i;
            this.mMediaRecorder.setMaxDuration(i);
        }
    }

    public void setMaxFileSize(long j) {
        synchronized (this.mSyncer) {
            this.mMaxSize = j;
            this.mMediaRecorder.setMaxFileSize(j);
        }
    }

    public void setOnErrorListener(OnErrorListener onErrorListener) {
        synchronized (this.mSyncer) {
            this.mOnErrorListener = onErrorListener;
            this.mMediaRecorder.setOnErrorListener(onErrorListener);
        }
    }

    public void setOnErrorListener(Recorder.OnErrorListener onErrorListener) {
        this.mRecorderOnErrorListener = onErrorListener;
        setOnErrorListener((OnErrorListener) new OnErrorListener() {
            public void onError(MediaRecorder mediaRecorder, int i, int i2) {
                LocalMediaRecorder.this.mRecorderOnErrorListener.onError(LocalMediaRecorder.this, RecorderUtils.convertErrorCode(i, true));
            }
        });
        setOnInfoListener(new OnInfoListener() {
            public void onInfo(MediaRecorder mediaRecorder, int i, int i2) {
                LocalMediaRecorder.this.mRecorderOnErrorListener.onError(LocalMediaRecorder.this, RecorderUtils.convertErrorCode(i, true));
            }
        });
    }

    public void setOnInfoListener(OnInfoListener onInfoListener) {
        synchronized (this.mSyncer) {
            this.mOnInfoListener = onInfoListener;
            this.mMediaRecorder.setOnInfoListener(onInfoListener);
        }
    }

    public void setOutputFile(String str) {
        synchronized (this.mSyncer) {
            this.mDestFilePath = str;
            this.mMediaRecorder.setOutputFile(str);
        }
    }

    public void setOutputFormat(int i) {
        synchronized (this.mSyncer) {
            this.mOutputFormat = i;
            this.mMediaRecorder.setOutputFormat(i);
        }
    }

    public void setQuality(int i) {
    }

    public void start() {
        synchronized (this.mSyncer) {
            this.mMediaRecorder.start();
            this.mStartTime = SystemClock.elapsedRealtime();
            this.mRecordingDuration = 0;
        }
    }

    public void stop() {
        synchronized (this.mSyncer) {
            resetParameters();
            if (this.mMediaRecorder != null) {
                this.mMediaRecorder.stop();
                if (this.mRecorderAppendWriteFd != null) {
                    try {
                        this.mRecorderAppendWriteFd.close();
                        waitingForAppendThread();
                    } catch (IOException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
            this.mIsPaused = false;
        }
    }
}
