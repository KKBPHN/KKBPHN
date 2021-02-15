package net.majorkernelpanic.streaming.rtcp;

import android.os.SystemClock;
import java.io.IOException;
import java.io.OutputStream;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import miui.text.ExtraTextUtils;
import tv.danmaku.ijk.media.player.IjkMediaMeta;

public class SenderReport {
    public static final int MTU = 1500;
    private static final int PACKET_LENGTH = 28;
    private long delta;
    private long interval;
    private byte[] mBuffer;
    private int mOctetCount;
    private OutputStream mOutputStream;
    private int mPacketCount;
    private int mPort;
    private int mSSRC;
    private byte[] mTcpHeader;
    private int mTransport;
    private long now;
    private long oldnow;
    private DatagramPacket upack;
    private MulticastSocket usock;

    public SenderReport() {
        this.mOutputStream = null;
        this.mBuffer = new byte[MTU];
        this.mPort = -1;
        this.mOctetCount = 0;
        this.mPacketCount = 0;
        this.mTransport = 0;
        this.mTcpHeader = new byte[]{36, 0, 0, 28};
        this.mBuffer[0] = (byte) Integer.parseInt("10000000", 2);
        this.mBuffer[1] = -56;
        setLong(6, 2, 4);
        try {
            this.usock = new MulticastSocket();
            this.upack = new DatagramPacket(this.mBuffer, 1);
            this.interval = 3000;
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

    public SenderReport(int i) {
        this.mOutputStream = null;
        this.mBuffer = new byte[MTU];
        this.mPort = -1;
        this.mOctetCount = 0;
        this.mPacketCount = 0;
        this.mSSRC = i;
    }

    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:8:0x0048 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void send(long j, long j2) {
        long j3 = j / ExtraTextUtils.GB;
        long j4 = ((j - (j3 * ExtraTextUtils.GB)) * IjkMediaMeta.AV_CH_WIDE_RIGHT) / ExtraTextUtils.GB;
        setLong(j3, 8, 12);
        setLong(j4, 12, 16);
        setLong(j2, 16, 20);
        if (this.mTransport == 0) {
            this.upack.setLength(28);
            this.usock.send(this.upack);
            return;
        }
        synchronized (this.mOutputStream) {
            this.mOutputStream.write(this.mTcpHeader);
            this.mOutputStream.write(this.mBuffer, 0, 28);
        }
    }

    private void setLong(long j, int i, int i2) {
        while (true) {
            i2--;
            if (i2 >= i) {
                this.mBuffer[i2] = (byte) ((int) (j % 256));
                j >>= 8;
            } else {
                return;
            }
        }
    }

    public void close() {
        this.usock.close();
    }

    public int getLocalPort() {
        return this.usock.getLocalPort();
    }

    public int getPort() {
        return this.mPort;
    }

    public int getSSRC() {
        return this.mSSRC;
    }

    public void reset() {
        this.mPacketCount = 0;
        this.mOctetCount = 0;
        setLong((long) this.mPacketCount, 20, 24);
        setLong((long) this.mOctetCount, 24, 28);
        this.oldnow = 0;
        this.now = 0;
        this.delta = 0;
    }

    public void setDestination(InetAddress inetAddress, int i) {
        this.mTransport = 0;
        this.mPort = i;
        this.upack.setPort(i);
        this.upack.setAddress(inetAddress);
    }

    public void setInterval(long j) {
        this.interval = j;
    }

    public void setOutputStream(OutputStream outputStream, byte b) {
        this.mTransport = 1;
        this.mOutputStream = outputStream;
        this.mTcpHeader[1] = b;
    }

    public void setSSRC(int i) {
        this.mSSRC = i;
        setLong((long) i, 4, 8);
        this.mPacketCount = 0;
        this.mOctetCount = 0;
        setLong((long) this.mPacketCount, 20, 24);
        setLong((long) this.mOctetCount, 24, 28);
    }

    public void update(int i, long j) {
        this.mPacketCount++;
        this.mOctetCount += i;
        setLong((long) this.mPacketCount, 20, 24);
        setLong((long) this.mOctetCount, 24, 28);
        this.now = SystemClock.elapsedRealtime();
        long j2 = this.delta;
        long j3 = this.oldnow;
        this.delta = j2 + (j3 != 0 ? this.now - j3 : 0);
        this.oldnow = this.now;
        long j4 = this.interval;
        if (j4 > 0 && this.delta >= j4) {
            send(System.nanoTime(), j);
            this.delta = 0;
        }
    }
}
