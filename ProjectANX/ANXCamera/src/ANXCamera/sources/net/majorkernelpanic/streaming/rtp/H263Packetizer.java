package net.majorkernelpanic.streaming.rtp;

import android.util.Log;
import java.io.IOException;
import net.majorkernelpanic.streaming.rtp.AbstractPacketizer.Statistics;

public class H263Packetizer extends AbstractPacketizer implements Runnable {
    public static final String TAG = "H263Packetizer";
    private Statistics stats = new Statistics();
    private Thread t;

    public H263Packetizer() {
        this.socket.setClockFrequency(90000);
    }

    private int fill(int i, int i2) {
        int i3 = 0;
        while (i3 < i2) {
            int read = this.is.read(this.buffer, i + i3, i2 - i3);
            if (read >= 0) {
                i3 += read;
            } else {
                throw new IOException("End of stream");
            }
        }
        return i3;
    }

    public void run() {
        int i;
        this.stats.reset();
        long j = 0;
        boolean z = true;
        int i2 = 0;
        while (!Thread.interrupted()) {
            try {
                if (i2 == 0) {
                    this.buffer = this.socket.requestBuffer();
                }
                this.socket.updateTimestamp(this.ts);
                this.buffer[12] = 0;
                this.buffer[13] = 0;
                long nanoTime = System.nanoTime();
                if (fill(i2 + 12 + 2, (1260 - i2) - 2) >= 0) {
                    j += System.nanoTime() - nanoTime;
                    int i3 = 14;
                    while (true) {
                        if (i3 < 1271) {
                            if (this.buffer[i3] == 0 && this.buffer[i3 + 1] == 0 && (this.buffer[i3 + 2] & -4) == Byte.MIN_VALUE) {
                                i = i3;
                                break;
                            }
                            i3++;
                        } else {
                            i = 0;
                            break;
                        }
                    }
                    byte b = this.buffer[i3 + 2];
                    byte b2 = this.buffer[i3 + 3];
                    if (z) {
                        this.buffer[12] = 4;
                        z = false;
                    } else {
                        this.buffer[12] = 0;
                    }
                    if (i > 0) {
                        this.stats.push(j);
                        this.ts += this.stats.average();
                        this.socket.markNextPacket();
                        send(i);
                        byte[] requestBuffer = this.socket.requestBuffer();
                        int i4 = (1272 - i) - 2;
                        System.arraycopy(this.buffer, i + 2, requestBuffer, 14, i4);
                        this.buffer = requestBuffer;
                        j = 0;
                        z = true;
                        i2 = i4;
                    } else {
                        send(1272);
                        i2 = i;
                    }
                } else {
                    return;
                }
            } catch (IOException | InterruptedException unused) {
            }
        }
        Log.d(TAG, "H263 Packetizer stopped !");
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
