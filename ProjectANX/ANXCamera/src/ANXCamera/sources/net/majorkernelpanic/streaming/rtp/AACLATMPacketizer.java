package net.majorkernelpanic.streaming.rtp;

import android.annotation.SuppressLint;
import android.media.MediaCodec.BufferInfo;
import android.util.Log;
import java.io.IOException;

@SuppressLint({"NewApi"})
public class AACLATMPacketizer extends AbstractPacketizer implements Runnable {
    private static final String TAG = "AACLATMPacketizer";
    private Thread t;

    public AACLATMPacketizer() {
        this.socket.setCacheSize(0);
    }

    @SuppressLint({"NewApi"})
    public void run() {
        RtpSocket rtpSocket;
        String str = TAG;
        Log.d(str, "AAC LATM packetizer started !");
        while (!Thread.interrupted()) {
            try {
                this.buffer = this.socket.requestBuffer();
                int read = this.is.read(this.buffer, 16, 1256);
                if (read > 0) {
                    BufferInfo lastBufferInfo = ((MediaCodecInputStream) this.is).getLastBufferInfo();
                    long j = this.ts;
                    this.ts = lastBufferInfo.presentationTimeUs * 1000;
                    if (j > this.ts) {
                        rtpSocket = this.socket;
                    } else {
                        this.socket.markNextPacket();
                        this.socket.updateTimestamp(this.ts);
                        this.buffer[12] = 0;
                        this.buffer[13] = 16;
                        this.buffer[14] = (byte) (read >> 5);
                        this.buffer[15] = (byte) (read << 3);
                        byte[] bArr = this.buffer;
                        bArr[15] = (byte) (bArr[15] & -8);
                        send(read + 12 + 4);
                    }
                } else {
                    rtpSocket = this.socket;
                }
                rtpSocket.commitBuffer();
            } catch (IOException | InterruptedException unused) {
            } catch (ArrayIndexOutOfBoundsException e) {
                StringBuilder sb = new StringBuilder();
                sb.append("ArrayIndexOutOfBoundsException: ");
                sb.append(e.getMessage() != null ? e.getMessage() : "unknown error");
                Log.e(str, sb.toString());
                e.printStackTrace();
            }
        }
        Log.d(str, "AAC LATM packetizer stopped !");
    }

    public void setSamplingRate(int i) {
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
