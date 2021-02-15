package net.majorkernelpanic.streaming.rtp;

import android.annotation.SuppressLint;
import android.media.MediaCodec;
import android.media.MediaCodec.BufferInfo;
import android.media.MediaFormat;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;

@SuppressLint({"NewApi"})
public class MediaCodecInputStream extends InputStream {
    public final String TAG = "MediaCodecInputStream";
    private ByteBuffer mBuffer = null;
    private BufferInfo mBufferInfo = new BufferInfo();
    private ByteBuffer[] mBuffers = null;
    private boolean mClosed = false;
    private int mIndex = -1;
    private MediaCodec mMediaCodec = null;
    public MediaFormat mMediaFormat;

    public MediaCodecInputStream(MediaCodec mediaCodec) {
        this.mMediaCodec = mediaCodec;
        this.mBuffers = this.mMediaCodec.getOutputBuffers();
    }

    public int available() {
        ByteBuffer byteBuffer = this.mBuffer;
        if (byteBuffer != null) {
            return this.mBufferInfo.size - byteBuffer.position();
        }
        return 0;
    }

    public void close() {
        this.mClosed = true;
    }

    public BufferInfo getLastBufferInfo() {
        return this.mBufferInfo;
    }

    public int read() {
        return 0;
    }

    public int read(byte[] bArr, int i, int i2) {
        try {
            if (this.mBuffer == null) {
                while (true) {
                    if (Thread.interrupted() || this.mClosed) {
                        break;
                    }
                    this.mIndex = this.mMediaCodec.dequeueOutputBuffer(this.mBufferInfo, 500000);
                    if (this.mIndex >= 0) {
                        this.mBuffer = this.mBuffers[this.mIndex];
                        this.mBuffer.position(0);
                        break;
                    } else if (this.mIndex == -3) {
                        this.mBuffers = this.mMediaCodec.getOutputBuffers();
                    } else {
                        String str = "MediaCodecInputStream";
                        if (this.mIndex == -2) {
                            this.mMediaFormat = this.mMediaCodec.getOutputFormat();
                            Log.i(str, this.mMediaFormat.toString());
                        } else if (this.mIndex == -1) {
                            Log.v(str, "No buffer available...");
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("Message: ");
                            sb.append(this.mIndex);
                            Log.e(str, sb.toString());
                        }
                    }
                }
            }
            if (!this.mClosed) {
                if (i2 >= this.mBufferInfo.size - this.mBuffer.position()) {
                    i2 = this.mBufferInfo.size - this.mBuffer.position();
                }
                try {
                    this.mBuffer.get(bArr, i, r8);
                    if (this.mBuffer.position() >= this.mBufferInfo.size) {
                        this.mMediaCodec.releaseOutputBuffer(this.mIndex, false);
                        this.mBuffer = null;
                    }
                } catch (RuntimeException e) {
                    e = e;
                    e.printStackTrace();
                    return r8;
                }
                return r8;
            }
            throw new IOException("This InputStream was closed");
        } catch (RuntimeException e2) {
            e = e2;
            r8 = 0;
            e.printStackTrace();
            return r8;
        }
    }
}
