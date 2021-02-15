package net.majorkernelpanic.streaming.rtsp;

import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;
import java.io.PipedOutputStream;

class RtcpDeinterleaver extends InputStream implements Runnable {
    public static final String TAG = "RtcpDeinterleaver";
    private byte[] mBuffer;
    private IOException mIOException;
    private InputStream mInputStream;
    private PipedInputStream mPipedInputStream = new PipedInputStream(4096);
    private PipedOutputStream mPipedOutputStream;

    public RtcpDeinterleaver(InputStream inputStream) {
        this.mInputStream = inputStream;
        try {
            this.mPipedOutputStream = new PipedOutputStream(this.mPipedInputStream);
        } catch (IOException unused) {
        }
        this.mBuffer = new byte[1024];
        new Thread(this).start();
    }

    public void close() {
        this.mInputStream.close();
    }

    public int read() {
        IOException iOException = this.mIOException;
        if (iOException == null) {
            return this.mPipedInputStream.read();
        }
        throw iOException;
    }

    public int read(byte[] bArr) {
        IOException iOException = this.mIOException;
        if (iOException == null) {
            return this.mPipedInputStream.read(bArr);
        }
        throw iOException;
    }

    public int read(byte[] bArr, int i, int i2) {
        IOException iOException = this.mIOException;
        if (iOException == null) {
            return this.mPipedInputStream.read(bArr, i, i2);
        }
        throw iOException;
    }

    public void run() {
        while (true) {
            try {
                int read = this.mInputStream.read(this.mBuffer, 0, 1024);
                if (read >= 0) {
                    this.mPipedOutputStream.write(this.mBuffer, 0, read);
                } else {
                    return;
                }
            } catch (IOException e) {
                try {
                    this.mPipedInputStream.close();
                } catch (IOException unused) {
                }
                this.mIOException = e;
                return;
            }
        }
    }
}
