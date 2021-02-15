package net.majorkernelpanic.streaming.mp4;

import android.util.Log;
import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.util.HashMap;

public class MP4Parser {
    private static final String TAG = "MP4Parser";
    private HashMap mBoxes = new HashMap();
    private final RandomAccessFile mFile;
    private long mPos = 0;

    private MP4Parser(String str) {
        this.mFile = new RandomAccessFile(new File(str), "r");
        try {
            parse("", this.mFile.length());
        } catch (Exception e) {
            e.printStackTrace();
            throw new IOException("Parse error: malformed mp4 file");
        }
    }

    public static MP4Parser parse(String str) {
        return new MP4Parser(str);
    }

    private void parse(String str, long j) {
        long j2;
        long j3;
        String str2 = str;
        byte[] bArr = new byte[8];
        if (!str2.equals("")) {
            this.mBoxes.put(str2, Long.valueOf(this.mPos - 8));
        }
        long j4 = 0;
        while (j4 < j) {
            this.mFile.read(bArr, 0, 8);
            this.mPos += 8;
            long j5 = j4 + 8;
            if (validBoxName(bArr)) {
                String str3 = new String(bArr, 4, 4);
                if (bArr[3] == 1) {
                    this.mFile.read(bArr, 0, 8);
                    this.mPos += 8;
                    j5 += 8;
                    j3 = ByteBuffer.wrap(bArr, 0, 8).getLong() - 16;
                } else {
                    j3 = (long) (ByteBuffer.wrap(bArr, 0, 4).getInt() - 8);
                }
                if (j3 < 0 || j3 == 1061109559) {
                    throw new IOException();
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Atom -> name: ");
                sb.append(str3);
                sb.append(" position: ");
                sb.append(this.mPos);
                sb.append(", length: ");
                sb.append(j3);
                Log.d(TAG, sb.toString());
                j4 = j5 + j3;
                StringBuilder sb2 = new StringBuilder();
                sb2.append(str2);
                sb2.append('/');
                sb2.append(str3);
                parse(sb2.toString(), j3);
            } else {
                if (j < 8) {
                    RandomAccessFile randomAccessFile = this.mFile;
                    randomAccessFile.seek((randomAccessFile.getFilePointer() - 8) + j);
                    j2 = j - 8;
                } else {
                    j2 = j - 8;
                    int i = (int) j2;
                    if (this.mFile.skipBytes(i) >= i) {
                        this.mPos += j2;
                    } else {
                        throw new IOException();
                    }
                }
                j4 = j5 + j2;
            }
        }
    }

    static String toHexString(byte[] bArr, int i, int i2) {
        StringBuilder sb = new StringBuilder();
        for (int i3 = i; i3 < i + i2; i3++) {
            String hexString = Integer.toHexString(bArr[i3] & -1);
            if (hexString.length() < 2) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("0");
                sb2.append(hexString);
                hexString = sb2.toString();
            }
            sb.append(hexString);
        }
        return sb.toString();
    }

    private boolean validBoxName(byte[] bArr) {
        for (int i = 0; i < 4; i++) {
            int i2 = i + 4;
            if ((bArr[i2] < 97 || bArr[i2] > 122) && (bArr[i2] < 48 || bArr[i2] > 57)) {
                return false;
            }
        }
        return true;
    }

    public void close() {
        try {
            this.mFile.close();
        } catch (Exception unused) {
        }
    }

    public long getBoxPos(String str) {
        if (((Long) this.mBoxes.get(str)) != null) {
            return ((Long) this.mBoxes.get(str)).longValue();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Box not found: ");
        sb.append(str);
        throw new IOException(sb.toString());
    }

    public StsdBox getStsdBox() {
        try {
            return new StsdBox(this.mFile, getBoxPos("/moov/trak/mdia/minf/stbl/stsd"));
        } catch (IOException unused) {
            throw new IOException("stsd box could not be found");
        }
    }
}
