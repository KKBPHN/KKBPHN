package net.majorkernelpanic.streaming.rtp;

import java.io.InputStream;
import java.net.InetAddress;
import java.util.Random;

public abstract class AbstractPacketizer {
    protected static final int MAXPACKETSIZE = 1272;
    protected static final int rtphl = 12;
    protected byte[] buffer;
    protected InputStream is = null;
    protected RtpSocket socket = null;
    protected long ts = 0;

    public class Statistics {
        public static final String TAG = "Statistics";
        private int c = 0;
        private int count = 700;
        private long duration = 0;
        private long elapsed = 0;
        private boolean initoffset = false;
        private float m = 0.0f;
        private long period = 10000000000L;
        private float q = 0.0f;
        private long start = 0;

        public Statistics() {
        }

        public Statistics(int i, int i2) {
            this.count = i;
            this.period = (long) i2;
        }

        public long average() {
            long j = (long) this.m;
            this.duration += j;
            return j;
        }

        public void push(long j) {
            this.elapsed += j;
            if (this.elapsed > this.period) {
                this.elapsed = 0;
                long nanoTime = System.nanoTime();
                if (!this.initoffset || nanoTime - this.start < 0) {
                    this.start = nanoTime;
                    this.duration = 0;
                    this.initoffset = true;
                }
                j += (nanoTime - this.start) - this.duration;
            }
            int i = this.c;
            if (i < 5) {
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

        public void reset() {
            this.initoffset = false;
            this.q = 0.0f;
            this.m = 0.0f;
            this.c = 0;
            this.elapsed = 0;
            this.start = 0;
            this.duration = 0;
        }
    }

    public AbstractPacketizer() {
        int nextInt = new Random().nextInt();
        this.ts = (long) new Random().nextInt();
        this.socket = new RtpSocket();
        this.socket.setSSRC(nextInt);
    }

    protected static String printBuffer(byte[] bArr, int i, int i2) {
        String str = "";
        while (i < i2) {
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append(",");
            sb.append(Integer.toHexString(bArr[i] & -1));
            str = sb.toString();
            i++;
        }
        return str;
    }

    public RtpSocket getRtpSocket() {
        return this.socket;
    }

    public int getSSRC() {
        return this.socket.getSSRC();
    }

    /* access modifiers changed from: protected */
    public void send(int i) {
        this.socket.commitBuffer(i);
    }

    public void setDestination(InetAddress inetAddress, int i, int i2) {
        this.socket.setDestination(inetAddress, i, i2);
    }

    public void setInputStream(InputStream inputStream) {
        this.is = inputStream;
    }

    public void setSSRC(int i) {
        this.socket.setSSRC(i);
    }

    public void setTimeToLive(int i) {
        this.socket.setTimeToLive(i);
    }

    public abstract void start();

    public abstract void stop();
}
