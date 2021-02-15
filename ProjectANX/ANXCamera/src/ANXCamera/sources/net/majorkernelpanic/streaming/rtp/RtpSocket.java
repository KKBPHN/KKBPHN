package net.majorkernelpanic.streaming.rtp;

import android.os.SystemClock;
import android.util.Log;
import com.android.camera.module.loader.FunctionParseBeautyBodySlimCount;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import miui.text.ExtraTextUtils;
import net.majorkernelpanic.streaming.rtcp.SenderReport;

public class RtpSocket implements Runnable {
    public static final int MTU = 1300;
    public static final int RTP_HEADER_LENGTH = 12;
    public static final String TAG = "RtpSocket";
    public static final int TRANSPORT_TCP = 1;
    public static final int TRANSPORT_UDP = 0;
    private AverageBitrate mAverageBitrate;
    private Semaphore mBufferCommitted;
    private int mBufferCount = 300;
    private int mBufferIn;
    private int mBufferOut;
    private Semaphore mBufferRequested;
    private byte[][] mBuffers;
    private long mCacheSize = 0;
    private long mClock = 0;
    private int mCount = 0;
    private long mOldTimestamp = 0;
    protected OutputStream mOutputStream = null;
    private DatagramPacket[] mPackets;
    private int mPort = -1;
    private SenderReport mReport;
    private int mSeq = 0;
    private MulticastSocket mSocket;
    private int mSsrc;
    private byte[] mTcpHeader;
    private Thread mThread;
    private long[] mTimestamps;
    private int mTransport;

    public class AverageBitrate {
        private static final long RESOLUTION = 200;
        private int mCount;
        private long mDelta;
        private long[] mElapsed;
        private int mIndex;
        private long mNow;
        private long mOldNow;
        private int mSize;
        private long[] mSum;
        private int mTotal;

        public AverageBitrate() {
            this.mSize = 25;
            reset();
        }

        public AverageBitrate(int i) {
            this.mSize = i / 200;
            reset();
        }

        public int average() {
            long j = 0;
            long j2 = 0;
            long j3 = 0;
            for (int i = 0; i < this.mSize; i++) {
                j3 += this.mSum[i];
                j2 += this.mElapsed[i];
            }
            if (j2 > 0) {
                j = (j3 * 8000) / j2;
            }
            return (int) j;
        }

        public void push(int i) {
            this.mNow = SystemClock.elapsedRealtime();
            if (this.mCount > 0) {
                this.mDelta += this.mNow - this.mOldNow;
                this.mTotal += i;
                long j = this.mDelta;
                if (j > RESOLUTION) {
                    long[] jArr = this.mSum;
                    int i2 = this.mIndex;
                    jArr[i2] = (long) this.mTotal;
                    this.mTotal = 0;
                    this.mElapsed[i2] = j;
                    this.mDelta = 0;
                    this.mIndex = i2 + 1;
                    if (this.mIndex >= this.mSize) {
                        this.mIndex = 0;
                    }
                }
            }
            this.mOldNow = this.mNow;
            this.mCount++;
        }

        public void reset() {
            int i = this.mSize;
            this.mSum = new long[i];
            this.mElapsed = new long[i];
            this.mNow = SystemClock.elapsedRealtime();
            this.mOldNow = this.mNow;
            this.mCount = 0;
            this.mDelta = 0;
            this.mTotal = 0;
            this.mIndex = 0;
        }
    }

    public class Statistics {
        public static final String TAG = "Statistics";
        private int c = 0;
        private int count = 500;
        private long duration = 0;
        private long elapsed = 0;
        private boolean initoffset = false;
        private float m = 0.0f;
        private long period = 6000000000L;
        private float q = 0.0f;
        private long start = 0;

        public Statistics(int i, long j) {
            this.count = i;
            this.period = j * ExtraTextUtils.MB;
        }

        public long average() {
            long j = ((long) this.m) - 2000000;
            if (j > 0) {
                return j;
            }
            return 0;
        }

        public void push(long j) {
            this.duration += j;
            this.elapsed += j;
            if (this.elapsed > this.period) {
                this.elapsed = 0;
                long nanoTime = System.nanoTime();
                if (!this.initoffset || nanoTime - this.start < 0) {
                    this.start = nanoTime;
                    this.duration = 0;
                    this.initoffset = true;
                }
                j -= (nanoTime - this.start) - this.duration;
            }
            int i = this.c;
            if (i < 40) {
                this.c = i + 1;
                this.m = (float) j;
                return;
            }
            float f = this.m;
            float f2 = this.q;
            this.m = ((f * f2) + ((float) j)) / (f2 + 1.0f);
            if (f2 < ((float) this.count)) {
                this.q = f2 + 1.0f;
            }
        }
    }

    public RtpSocket() {
        int i = this.mBufferCount;
        this.mBuffers = new byte[i][];
        this.mPackets = new DatagramPacket[i];
        this.mReport = new SenderReport();
        this.mAverageBitrate = new AverageBitrate();
        this.mTransport = 0;
        this.mTcpHeader = new byte[]{36, 0, 0, 0};
        resetFifo();
        for (int i2 = 0; i2 < this.mBufferCount; i2++) {
            byte[][] bArr = this.mBuffers;
            bArr[i2] = new byte[MTU];
            this.mPackets[i2] = new DatagramPacket(bArr[i2], 1);
            this.mBuffers[i2][0] = (byte) Integer.parseInt("10000000", 2);
            this.mBuffers[i2][1] = 96;
        }
        try {
            this.mSocket = new MulticastSocket();
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    private void resetFifo() {
        this.mCount = 0;
        this.mBufferIn = 0;
        this.mBufferOut = 0;
        int i = this.mBufferCount;
        this.mTimestamps = new long[i];
        this.mBufferRequested = new Semaphore(i);
        this.mBufferCommitted = new Semaphore(0);
        this.mReport.reset();
        this.mAverageBitrate.reset();
    }

    /* JADX WARNING: Can't wrap try/catch for region: R(6:2|3|4|5|6|7) */
    /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0046 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void sendTCP() {
        synchronized (this.mOutputStream) {
            int length = this.mPackets[this.mBufferOut].getLength();
            String str = TAG;
            StringBuilder sb = new StringBuilder();
            sb.append("sent ");
            sb.append(length);
            Log.d(str, sb.toString());
            this.mTcpHeader[2] = (byte) (length >> 8);
            this.mTcpHeader[3] = (byte) (length & 255);
            this.mOutputStream.write(this.mTcpHeader);
            this.mOutputStream.write(this.mBuffers[this.mBufferOut], 0, length);
        }
    }

    private void setLong(byte[] bArr, long j, int i, int i2) {
        while (true) {
            i2--;
            if (i2 >= i) {
                bArr[i2] = (byte) ((int) (j % 256));
                j >>= 8;
            } else {
                return;
            }
        }
    }

    private void updateSequence() {
        byte[] bArr = this.mBuffers[this.mBufferIn];
        int i = this.mSeq + 1;
        this.mSeq = i;
        setLong(bArr, (long) i, 2, 4);
    }

    public void close() {
        this.mSocket.close();
    }

    public void commitBuffer() {
        if (this.mThread == null) {
            this.mThread = new Thread(this);
            this.mThread.start();
        }
        int i = this.mBufferIn + 1;
        this.mBufferIn = i;
        if (i >= this.mBufferCount) {
            this.mBufferIn = 0;
        }
        this.mBufferCommitted.release();
    }

    public void commitBuffer(int i) {
        updateSequence();
        this.mPackets[this.mBufferIn].setLength(i);
        this.mAverageBitrate.push(i);
        int i2 = this.mBufferIn + 1;
        this.mBufferIn = i2;
        if (i2 >= this.mBufferCount) {
            this.mBufferIn = 0;
        }
        this.mBufferCommitted.release();
        if (this.mThread == null) {
            this.mThread = new Thread(this);
            this.mThread.start();
        }
    }

    public long getBitrate() {
        return (long) this.mAverageBitrate.average();
    }

    public int[] getLocalPorts() {
        return new int[]{this.mSocket.getLocalPort(), this.mReport.getLocalPort()};
    }

    public int getPort() {
        return this.mPort;
    }

    public int getSSRC() {
        return this.mSsrc;
    }

    public void markNextPacket() {
        byte[] bArr = this.mBuffers[this.mBufferIn];
        bArr[1] = (byte) (bArr[1] | Byte.MIN_VALUE);
    }

    public byte[] requestBuffer() {
        this.mBufferRequested.acquire();
        byte[][] bArr = this.mBuffers;
        int i = this.mBufferIn;
        byte[] bArr2 = bArr[i];
        bArr2[1] = (byte) (bArr2[1] & Byte.MAX_VALUE);
        return bArr[i];
    }

    public void run() {
        Statistics statistics = new Statistics(50, 3000);
        try {
            Thread.sleep(this.mCacheSize);
            long j = 0;
            while (this.mBufferCommitted.tryAcquire(4, TimeUnit.SECONDS)) {
                if (this.mOldTimestamp != 0) {
                    if (this.mTimestamps[this.mBufferOut] - this.mOldTimestamp > 0) {
                        statistics.push(this.mTimestamps[this.mBufferOut] - this.mOldTimestamp);
                        long average = statistics.average() / ExtraTextUtils.MB;
                        if (this.mCacheSize > 0) {
                            Thread.sleep(average);
                        }
                    } else if (this.mTimestamps[this.mBufferOut] - this.mOldTimestamp < 0) {
                        String str = TAG;
                        StringBuilder sb = new StringBuilder();
                        sb.append("TS: ");
                        sb.append(this.mTimestamps[this.mBufferOut]);
                        sb.append(" OLD: ");
                        sb.append(this.mOldTimestamp);
                        Log.e(str, sb.toString());
                    }
                    j += this.mTimestamps[this.mBufferOut] - this.mOldTimestamp;
                    if (j > 500000000 || j < 0) {
                        j = 0;
                    }
                }
                this.mReport.update(this.mPackets[this.mBufferOut].getLength(), ((this.mTimestamps[this.mBufferOut] / 100) * (this.mClock / 1000)) / FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME);
                this.mOldTimestamp = this.mTimestamps[this.mBufferOut];
                int i = this.mCount;
                this.mCount = i + 1;
                if (i > 30) {
                    if (this.mTransport == 0) {
                        this.mSocket.send(this.mPackets[this.mBufferOut]);
                    } else {
                        sendTCP();
                    }
                }
                int i2 = this.mBufferOut + 1;
                this.mBufferOut = i2;
                if (i2 >= this.mBufferCount) {
                    this.mBufferOut = 0;
                }
                this.mBufferRequested.release();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        this.mThread = null;
        resetFifo();
    }

    public void setCacheSize(long j) {
        this.mCacheSize = j;
    }

    public void setClockFrequency(long j) {
        this.mClock = j;
    }

    public void setDestination(InetAddress inetAddress, int i, int i2) {
        if (i != 0 && i2 != 0) {
            this.mTransport = 0;
            this.mPort = i;
            for (int i3 = 0; i3 < this.mBufferCount; i3++) {
                this.mPackets[i3].setPort(i);
                this.mPackets[i3].setAddress(inetAddress);
            }
            this.mReport.setDestination(inetAddress, i2);
        }
    }

    public void setOutputStream(OutputStream outputStream, byte b) {
        if (outputStream != null) {
            this.mTransport = 1;
            this.mOutputStream = outputStream;
            this.mTcpHeader[1] = b;
            this.mReport.setOutputStream(outputStream, (byte) (b + 1));
        }
    }

    public void setSSRC(int i) {
        this.mSsrc = i;
        for (int i2 = 0; i2 < this.mBufferCount; i2++) {
            setLong(this.mBuffers[i2], (long) i, 8, 12);
        }
        this.mReport.setSSRC(this.mSsrc);
    }

    public void setTimeToLive(int i) {
        this.mSocket.setTimeToLive(i);
    }

    public void updateTimestamp(long j) {
        long[] jArr = this.mTimestamps;
        int i = this.mBufferIn;
        jArr[i] = j;
        setLong(this.mBuffers[i], ((j / 100) * (this.mClock / 1000)) / FunctionParseBeautyBodySlimCount.TIP_INTERVAL_TIME, 4, 8);
    }
}
