package net.majorkernelpanic.streaming.rtp;

import android.util.Log;
import java.io.IOException;

public class AMRNBPacketizer extends AbstractPacketizer implements Runnable {
    private static final int AMR_FRAME_HEADER_LENGTH = 1;
    public static final String TAG = "AMRNBPacketizer";
    private static final int[] sFrameBits = {95, 103, 118, 134, af.aC, 159, 204, 244};
    private final int AMR_HEADER_LENGTH = 6;
    private int samplingRate = 8000;
    private Thread t;

    public AMRNBPacketizer() {
        this.socket.setClockFrequency((long) this.samplingRate);
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

    public void run() {
        String str = TAG;
        System.nanoTime();
        byte[] bArr = new byte[6];
        try {
            fill(bArr, 0, 6);
            if (bArr[5] != 10) {
                Log.e(str, "Bad header ! AMR not correcty supported by the phone !");
                return;
            }
            while (!Thread.interrupted()) {
                this.buffer = this.socket.requestBuffer();
                this.buffer[12] = -16;
                fill(this.buffer, 13, 1);
                int i = (sFrameBits[(Math.abs(this.buffer[13]) >> 3) & 15] + 7) / 8;
                fill(this.buffer, 14, i);
                this.ts += 160000000000L / ((long) this.samplingRate);
                this.socket.updateTimestamp(this.ts);
                this.socket.markNextPacket();
                send(i + 14);
            }
            Log.d(str, "AMR packetizer stopped !");
        } catch (IOException | InterruptedException unused) {
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
