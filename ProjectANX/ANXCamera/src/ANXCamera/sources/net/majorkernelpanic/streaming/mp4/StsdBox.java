package net.majorkernelpanic.streaming.mp4;

import android.util.Base64;
import java.io.IOException;
import java.io.RandomAccessFile;

class StsdBox {
    private byte[] buffer = new byte[4];
    private RandomAccessFile fis;
    private long pos = 0;
    private byte[] pps;
    private int ppsLength;
    private byte[] sps;
    private int spsLength;

    public StsdBox(RandomAccessFile randomAccessFile, long j) {
        this.fis = randomAccessFile;
        this.pos = j;
        findBoxAvcc();
        findSPSandPPS();
    }

    private boolean findBoxAvcc() {
        try {
            this.fis.seek(this.pos + 8);
            while (true) {
                if (this.fis.read() == 97) {
                    this.fis.read(this.buffer, 0, 3);
                    if (this.buffer[0] == 118 && this.buffer[1] == 99 && this.buffer[2] == 67) {
                        return true;
                    }
                }
            }
        } catch (IOException unused) {
            return false;
        }
    }

    private boolean findSPSandPPS() {
        try {
            this.fis.skipBytes(7);
            this.spsLength = this.fis.readByte() & -1;
            this.sps = new byte[this.spsLength];
            this.fis.read(this.sps, 0, this.spsLength);
            this.fis.skipBytes(2);
            this.ppsLength = this.fis.readByte() & -1;
            this.pps = new byte[this.ppsLength];
            this.fis.read(this.pps, 0, this.ppsLength);
            return true;
        } catch (IOException unused) {
            return false;
        }
    }

    public String getB64PPS() {
        return Base64.encodeToString(this.pps, 0, this.ppsLength, 2);
    }

    public String getB64SPS() {
        return Base64.encodeToString(this.sps, 0, this.spsLength, 2);
    }

    public String getProfileLevel() {
        return MP4Parser.toHexString(this.sps, 1, 3);
    }
}
