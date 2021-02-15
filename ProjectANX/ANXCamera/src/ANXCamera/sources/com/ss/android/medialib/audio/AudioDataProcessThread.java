package com.ss.android.medialib.audio;

import O00000o.O000000o.O000000o.O000000o;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import com.ss.android.medialib.common.LogUtil;
import com.ss.android.vesdk.VELogUtil;
import java.lang.ref.WeakReference;
import java.util.Arrays;
import java.util.concurrent.atomic.AtomicInteger;

public class AudioDataProcessThread implements Runnable {
    private static final int MSG_FEED = 3;
    private static final int MSG_START_FEEDING = 0;
    private static final int MSG_STOP = 2;
    private static final int MSG_STOP_FEEDING = 1;
    private static final String TAG = "AudioDataProcessThread";
    /* access modifiers changed from: private */
    public AtomicInteger mBufferCount = new AtomicInteger(0);
    private boolean mDiscard = false;
    private volatile ProcessHandler mHandler;
    /* access modifiers changed from: private */
    public OnProcessDataListener mListener;
    private final Object mLock;
    private boolean mReady;
    private final Object mReadyFence = new Object();
    private O000000o mRecorderInterface;
    private boolean mRunning;
    private boolean mStopped = true;

    public interface OnProcessDataListener {
        int onProcessData(byte[] bArr, int i);
    }

    class ProcessHandler extends Handler {
        private WeakReference mProcessor;

        public ProcessHandler(AudioDataProcessThread audioDataProcessThread) {
            this.mProcessor = new WeakReference(audioDataProcessThread);
        }

        public void handleMessage(Message message) {
            int i = message.what;
            AudioDataProcessThread audioDataProcessThread = (AudioDataProcessThread) this.mProcessor.get();
            String str = AudioDataProcessThread.TAG;
            if (audioDataProcessThread == null) {
                VELogUtil.e(str, "EncoderHandler.handleMessage: encoder is null");
                return;
            }
            if (i == 0) {
                audioDataProcessThread.handleStartFeeding(message.arg1, message.arg2, ((Double) message.obj).doubleValue());
            } else if (i == 1) {
                audioDataProcessThread.handleStopFeeding();
            } else if (i == 2) {
                VELogUtil.i(str, "Exit loop");
                audioDataProcessThread.handleStopFeeding();
                removeMessages(3);
                Looper.myLooper().quit();
            } else if (i == 3) {
                byte[] bArr = (byte[]) message.obj;
                int i2 = message.arg1;
                int decrementAndGet = audioDataProcessThread.mBufferCount.decrementAndGet();
                if (audioDataProcessThread.mListener != null) {
                    audioDataProcessThread.mListener.onProcessData(bArr, i2);
                    StringBuilder sb = new StringBuilder();
                    sb.append("Buffer processed, size=");
                    sb.append(i2);
                    sb.append(", ");
                    sb.append(decrementAndGet);
                    sb.append(" buffers remaining");
                    VELogUtil.d(str, sb.toString());
                }
            }
        }
    }

    public AudioDataProcessThread(O000000o o000000o, OnProcessDataListener onProcessDataListener) {
        this.mLock = o000000o != 0 ? o000000o : new Object();
        this.mRecorderInterface = o000000o;
        this.mListener = onProcessDataListener;
    }

    /* access modifiers changed from: private */
    public void handleStartFeeding(int i, int i2, double d) {
        if (this.mRecorderInterface != null) {
            StringBuilder sb = new StringBuilder();
            sb.append("handleStartFeeding() called with: sampleRateInHz = [");
            sb.append(i);
            sb.append("], channels = [");
            sb.append(i2);
            sb.append("], speed = [");
            sb.append(d);
            sb.append("]");
            String sb2 = sb.toString();
            String str = TAG;
            VELogUtil.i(str, sb2);
            if (this.mRecorderInterface.initWavFile(i, i2, d) != 0) {
                VELogUtil.e(str, "init wav file failed");
                return;
            }
            this.mStopped = false;
        }
    }

    /* access modifiers changed from: private */
    public void handleStopFeeding() {
        synchronized (this.mLock) {
            VELogUtil.i(TAG, "handleStopFeeding() called");
            if (!this.mStopped) {
                if (this.mRecorderInterface != null) {
                    this.mRecorderInterface.closeWavFile(this.mDiscard);
                } else {
                    VELogUtil.e(TAG, "handleStop: Discard wav file");
                }
                this.mStopped = true;
                this.mDiscard = false;
                this.mLock.notify();
            }
        }
    }

    public void discard() {
        synchronized (this.mReadyFence) {
            if (this.mReady) {
                this.mDiscard = true;
            }
        }
    }

    public void feed(byte[] bArr, int i) {
        synchronized (this.mReadyFence) {
            if (this.mReady) {
                this.mBufferCount.incrementAndGet();
                this.mHandler.sendMessage(this.mHandler.obtainMessage(3, i, 0, Arrays.copyOf(bArr, i)));
                VELogUtil.d(TAG, "feed audioData");
            }
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:12:0x0010, code lost:
        if (r3.mRunning == false) goto L_0x0017;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:14:0x0014, code lost:
        if (r3.mStopped != false) goto L_0x0017;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:15:0x0016, code lost:
        r2 = true;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:16:0x0017, code lost:
        monitor-exit(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0018, code lost:
        return r2;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:8:0x000b, code lost:
        r1 = r3.mLock;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:9:0x000d, code lost:
        monitor-enter(r1);
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean isProcessing() {
        synchronized (this.mReadyFence) {
            boolean z = false;
            if (!this.mReady) {
                return false;
            }
        }
    }

    public void run() {
        Looper.prepare();
        synchronized (this.mReadyFence) {
            this.mHandler = new ProcessHandler(this);
            this.mReady = true;
            this.mReadyFence.notify();
        }
        Looper.loop();
        VELogUtil.d(TAG, "Encoder thread exiting");
        synchronized (this.mReadyFence) {
            this.mRunning = false;
            this.mReady = false;
            this.mHandler = null;
        }
    }

    public void start() {
        StringBuilder sb = new StringBuilder();
        sb.append(VELogUtil.__FILE__());
        sb.append(": ");
        sb.append(VELogUtil.__FUNCTION__());
        VELogUtil.i(TAG, sb.toString());
        synchronized (this.mReadyFence) {
            if (this.mRunning) {
                VELogUtil.w(TAG, "thread already running");
                return;
            }
            this.mRunning = true;
            new Thread(this, TAG).start();
            while (!this.mReady) {
                try {
                    this.mReadyFence.wait();
                } catch (InterruptedException unused) {
                }
            }
        }
    }

    public void startFeeding(int i, double d) {
        VELogUtil.w(TAG, "startFeeding");
        this.mBufferCount.set(0);
        this.mHandler.sendMessage(this.mHandler.obtainMessage(0, i, 2, Double.valueOf(d)));
    }

    public void stop() {
        synchronized (this.mReadyFence) {
            if (this.mReady) {
                this.mHandler.sendMessage(this.mHandler.obtainMessage(2));
                VELogUtil.d(TAG, "stop()");
            }
        }
    }

    public void stopFeeding() {
        VELogUtil.w(TAG, "stopFeeding");
        this.mHandler.sendMessage(this.mHandler.obtainMessage(1));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:11:0x0012, code lost:
        if (r4.mStopped == false) goto L_0x0014;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void waitUtilAudioProcessDone() {
        boolean hasMessages;
        synchronized (this.mLock) {
            synchronized (this.mReadyFence) {
                hasMessages = this.mHandler.hasMessages(1);
            }
            if (!hasMessages) {
            }
            LogUtil.i(TAG, "waiting audio process start");
            try {
                this.mLock.wait(5000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            LogUtil.i(TAG, "waiting audio process done");
        }
    }
}
