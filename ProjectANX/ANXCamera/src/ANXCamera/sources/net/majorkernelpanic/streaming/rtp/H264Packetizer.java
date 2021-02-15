package net.majorkernelpanic.streaming.rtp;

import android.annotation.SuppressLint;
import android.util.Log;
import java.io.IOException;
import net.majorkernelpanic.streaming.rtp.AbstractPacketizer.Statistics;

public class H264Packetizer extends AbstractPacketizer implements Runnable {
    public static final String TAG = "H264Packetizer";
    private int count = 0;
    private long delay = 0;
    byte[] header = new byte[5];
    private int naluLength = 0;
    private long oldtime = 0;
    private byte[] pps = null;
    private byte[] sps = null;
    private byte[] stapa = null;
    private Statistics stats = new Statistics();
    private int streamType = 1;
    private Thread t = null;

    public H264Packetizer() {
        this.socket.setClockFrequency(90000);
    }

    private int fill(byte[] bArr, int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            int read = this.is.read(bArr, i + i3, i2 - i3);
            if (read >= 0) {
                i3 += read;
            } else {
                throw new IOException("End of stream");
            }
        }
        return i3;
    }

    private void resync() {
        StringBuilder sb = new StringBuilder();
        sb.append("Packetizer out of sync ! Let's try to fix that...(NAL length: ");
        sb.append(this.naluLength);
        sb.append(")");
        String sb2 = sb.toString();
        String str = TAG;
        while (true) {
            Log.e(str, sb2);
            while (true) {
                byte[] bArr = this.header;
                bArr[0] = bArr[1];
                bArr[1] = bArr[2];
                bArr[2] = bArr[3];
                bArr[3] = bArr[4];
                bArr[4] = (byte) this.is.read();
                byte b = this.header[4] & 31;
                if (b == 5 || b == 1) {
                    byte[] bArr2 = this.header;
                    this.naluLength = ((bArr2[0] & -1) << 24) | (bArr2[3] & -1) | ((bArr2[2] & -1) << 8) | ((bArr2[1] & -1) << 16);
                    int i = this.naluLength;
                    if (i <= 0 || i >= 100000) {
                        if (this.naluLength != 0) {
                            byte[] bArr3 = this.header;
                            if (bArr3[3] == -1 && bArr3[2] == -1 && bArr3[1] == -1 && bArr3[0] == -1) {
                                sb2 = "NAL unit with 0xFFFFFFFF size found...";
                                break;
                            }
                        } else {
                            sb2 = "NAL unit with NULL size found...";
                            break;
                        }
                    } else {
                        this.oldtime = System.nanoTime();
                        Log.e(str, "A NAL unit may have been found in the bit stream !");
                        return;
                    }
                }
            }
        }
    }

    @SuppressLint({"NewApi"})
    private void send() {
        int i = this.streamType;
        String str = TAG;
        if (i == 0) {
            fill(this.header, 0, 5);
            this.ts += this.delay;
            byte[] bArr = this.header;
            this.naluLength = ((bArr[0] & -1) << 24) | (bArr[3] & -1) | ((bArr[2] & -1) << 8) | ((bArr[1] & -1) << 16);
            int i2 = this.naluLength;
            if (i2 > 100000 || i2 < 0) {
                resync();
            }
        } else if (i == 1) {
            fill(this.header, 0, 5);
            this.ts = ((MediaCodecInputStream) this.is).getLastBufferInfo().presentationTimeUs * 1000;
            this.naluLength = this.is.available() + 1;
            byte[] bArr2 = this.header;
            if (!(bArr2[0] == 0 && bArr2[1] == 0 && bArr2[2] == 0)) {
                Log.e(str, "NAL units are not preceeded by 0x00000001");
                this.streamType = 2;
                return;
            }
        } else {
            fill(this.header, 0, 1);
            byte[] bArr3 = this.header;
            bArr3[4] = bArr3[0];
            this.ts = ((MediaCodecInputStream) this.is).getLastBufferInfo().presentationTimeUs * 1000;
            this.naluLength = this.is.available() + 1;
        }
        byte b = this.header[4] & 31;
        if (b == 7 || b == 8) {
            Log.v(str, "SPS or PPS present in the stream.");
            this.count++;
            if (this.count > 4) {
                this.sps = null;
                this.pps = null;
            }
        }
        if (!(b != 5 || this.sps == null || this.pps == null)) {
            this.buffer = this.socket.requestBuffer();
            this.socket.markNextPacket();
            this.socket.updateTimestamp(this.ts);
            byte[] bArr4 = this.stapa;
            System.arraycopy(bArr4, 0, this.buffer, 12, bArr4.length);
            super.send(this.stapa.length + 12);
        }
        if (this.naluLength <= 1258) {
            this.buffer = this.socket.requestBuffer();
            byte[] bArr5 = this.buffer;
            bArr5[12] = this.header[4];
            fill(bArr5, 13, this.naluLength - 1);
            this.socket.updateTimestamp(this.ts);
            this.socket.markNextPacket();
            super.send(this.naluLength + 12);
        } else {
            byte[] bArr6 = this.header;
            bArr6[1] = (byte) (bArr6[4] & 31);
            bArr6[1] = (byte) (bArr6[1] + Byte.MIN_VALUE);
            bArr6[0] = (byte) (bArr6[4] & 96 & -1);
            bArr6[0] = (byte) (bArr6[0] + 28);
            int i3 = 1;
            while (i3 < this.naluLength) {
                this.buffer = this.socket.requestBuffer();
                byte[] bArr7 = this.buffer;
                byte[] bArr8 = this.header;
                bArr7[12] = bArr8[0];
                bArr7[13] = bArr8[1];
                this.socket.updateTimestamp(this.ts);
                byte[] bArr9 = this.buffer;
                int i4 = this.naluLength;
                int fill = fill(bArr9, 14, i4 - i3 > 1258 ? 1258 : i4 - i3);
                if (fill >= 0) {
                    i3 += fill;
                    if (i3 >= this.naluLength) {
                        byte[] bArr10 = this.buffer;
                        bArr10[13] = (byte) (bArr10[13] + 64);
                        this.socket.markNextPacket();
                    }
                    super.send(fill + 12 + 2);
                    byte[] bArr11 = this.header;
                    bArr11[1] = (byte) (bArr11[1] & Byte.MAX_VALUE);
                } else {
                    return;
                }
            }
        }
    }

    public void run() {
        long j;
        RtpSocket rtpSocket;
        String str = TAG;
        Log.d(str, "H264 packetizer started !");
        this.stats.reset();
        this.count = 0;
        if (this.is instanceof MediaCodecInputStream) {
            this.streamType = 1;
            rtpSocket = this.socket;
            j = 0;
        } else {
            this.streamType = 0;
            rtpSocket = this.socket;
            j = 400;
        }
        rtpSocket.setCacheSize(j);
        while (!Thread.interrupted()) {
            try {
                this.oldtime = System.nanoTime();
                send();
                this.stats.push(System.nanoTime() - this.oldtime);
                this.delay = this.stats.average();
            } catch (IOException | InterruptedException unused) {
            }
        }
        Log.d(str, "H264 packetizer stopped !");
    }

    public void setStreamParameters(byte[] bArr, byte[] bArr2) {
        this.pps = bArr;
        this.sps = bArr2;
        if (bArr != null && bArr2 != null) {
            this.stapa = new byte[(bArr2.length + bArr.length + 5)];
            byte[] bArr3 = this.stapa;
            bArr3[0] = 24;
            bArr3[1] = (byte) (bArr2.length >> 8);
            bArr3[2] = (byte) (bArr2.length & 255);
            bArr3[bArr2.length + 3] = (byte) (bArr.length >> 8);
            bArr3[bArr2.length + 4] = (byte) (bArr.length & 255);
            System.arraycopy(bArr2, 0, bArr3, 3, bArr2.length);
            System.arraycopy(bArr, 0, this.stapa, bArr2.length + 5, bArr.length);
        }
    }

    public void start() {
        if (this.t == null) {
            this.t = new Thread(this);
            this.t.start();
        }
    }

    public void stop() {
        if (this.t != null) {
            try {
                this.is.close();
            } catch (IOException unused) {
            }
            this.t.interrupt();
            try {
                this.t.join();
            } catch (InterruptedException unused2) {
            }
            this.t = null;
        }
    }
}
