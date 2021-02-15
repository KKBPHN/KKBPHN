package net.majorkernelpanic.streaming.rtp;

import android.os.SystemClock;
import android.util.Log;
import java.io.IOException;
import net.majorkernelpanic.streaming.audio.AACStream;

public class AACADTSPacketizer extends AbstractPacketizer implements Runnable {
    private static final String TAG = "AACADTSPacketizer";
    private int samplingRate = 8000;
    private Thread t;

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

    /* JADX WARNING: Removed duplicated region for block: B:4:0x0014 A[Catch:{ IOException | InterruptedException -> 0x00f7, ArrayIndexOutOfBoundsException -> 0x00d2 }, LOOP:1: B:4:0x0014->B:39:0x0014, LOOP_START] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void run() {
        boolean z;
        String str = TAG;
        Log.d(str, "AAC ADTS packetizer started !");
        SystemClock.elapsedRealtime();
        byte[] bArr = new byte[8];
        while (Thread.interrupted()) {
            try {
                while (true) {
                    if ((this.is.read() & 255) == 255) {
                        z = true;
                        bArr[1] = (byte) this.is.read();
                        if ((bArr[1] & -16) == -16) {
                            break;
                        }
                    }
                }
                fill(bArr, 2, 5);
                if ((bArr[1] & 1) <= 0) {
                    z = false;
                }
                int i = (((-1 & bArr[5]) >> 5) | (((bArr[4] & -1) << 3) | ((bArr[3] & 3) << 11))) - (z ? 7 : 9);
                byte b = bArr[6];
                int i2 = i / 1272;
                if (!z) {
                    this.is.read(bArr, 0, 2);
                }
                this.samplingRate = AACStream.AUDIO_SAMPLING_RATES[(bArr[2] & 60) >> 2];
                byte b2 = bArr[2];
                this.ts += 1024000000000L / ((long) this.samplingRate);
                int i3 = 0;
                while (true) {
                    if (i3 < i) {
                        this.buffer = this.socket.requestBuffer();
                        this.socket.updateTimestamp(this.ts);
                        int i4 = i - i3;
                        if (i4 > 1256) {
                            i4 = 1256;
                        } else {
                            this.socket.markNextPacket();
                        }
                        i3 += i4;
                        fill(this.buffer, 16, i4);
                        this.buffer[12] = 0;
                        this.buffer[13] = 16;
                        this.buffer[14] = (byte) (i >> 5);
                        this.buffer[15] = (byte) (i << 3);
                        byte[] bArr2 = this.buffer;
                        bArr2[15] = (byte) (bArr2[15] & -8);
                        send(i4 + 16);
                    }
                }
                if (Thread.interrupted()) {
                }
                break;
            } catch (IOException | InterruptedException unused) {
            } catch (ArrayIndexOutOfBoundsException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("ArrayIndexOutOfBoundsException: ");
                sb.append(e.getMessage() != null ? e.getMessage() : "unknown error");
                Log.e(str, sb.toString());
                e.printStackTrace();
            }
        }
        Log.d(str, "AAC ADTS packetizer stopped !");
    }

    public void setSamplingRate(int i) {
        this.samplingRate = i;
        this.socket.setClockFrequency((long) i);
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
