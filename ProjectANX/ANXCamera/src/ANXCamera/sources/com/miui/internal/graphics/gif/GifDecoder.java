package com.miui.internal.graphics.gif;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.support.v4.media.session.PlaybackStateCompat;
import java.io.BufferedInputStream;
import java.io.InputStream;
import java.util.Vector;

public class GifDecoder {
    public static final int MAX_DECODE_SIZE = 1048576;
    protected static final int MAX_STACK_SIZE = 4096;
    public static final int STATUS_DECODE_CANCEL = 3;
    public static final int STATUS_FORMAT_ERROR = 1;
    public static final int STATUS_OK = 0;
    public static final int STATUS_OPEN_ERROR = 2;
    protected int[] act;
    protected int bgColor;
    protected int bgIndex;
    protected byte[] block = new byte[256];
    protected int blockSize = 0;
    private boolean calledOnce = false;
    protected int delay = 0;
    private int[] dest;
    protected int dispose = 0;
    protected Vector frames;
    protected int[] gct;
    protected boolean gctFlag;
    protected int gctSize;
    private int height;
    protected int ih;
    protected Bitmap image;
    protected BufferedInputStream in;
    protected boolean interlace;
    protected int iw;
    protected int ix;
    protected int iy;
    protected int lastBgColor;
    protected Bitmap lastBitmap;
    protected int lastDispose = 0;
    protected int[] lct;
    protected boolean lctFlag;
    protected int lctSize;
    protected int loopCount = 1;
    protected int lrh;
    protected int lrw;
    protected int lrx;
    protected int lry;
    private boolean mCancel = false;
    private long mDecodeBmSize;
    private boolean mDecodeToTheEnd;
    private int mDecodedFrames;
    private long mMaxDecodeSize = PlaybackStateCompat.ACTION_SET_CAPTIONING_ENABLED;
    private int mStartFrame;
    protected int pixelAspect;
    protected byte[] pixelStack;
    protected byte[] pixels;
    protected short[] prefix;
    protected int status;
    protected byte[] suffix;
    protected int transIndex;
    protected boolean transparency = false;
    private int width;

    class GifFrame {
        public int delay;
        public Bitmap image;

        public GifFrame(Bitmap bitmap, int i) {
            this.image = bitmap;
            this.delay = i;
        }

        public void recycle() {
            Bitmap bitmap = this.image;
            if (bitmap != null && !bitmap.isRecycled()) {
                this.image.recycle();
            }
        }
    }

    public static boolean isGifStream(InputStream inputStream) {
        if (inputStream == null) {
            return false;
        }
        String str = "";
        for (int i = 0; i < 6; i++) {
            int readOneByte = readOneByte(inputStream);
            if (readOneByte == -1) {
                break;
            }
            StringBuilder sb = new StringBuilder();
            sb.append(str);
            sb.append((char) readOneByte);
            str = sb.toString();
        }
        return str.startsWith("GIF");
    }

    protected static int readOneByte(InputStream inputStream) {
        try {
            return inputStream.read();
        } catch (Exception unused) {
            return -1;
        }
    }

    private void requestCancel() {
    }

    /* JADX WARNING: type inference failed for: r3v4 */
    /* JADX WARNING: type inference failed for: r3v5 */
    /* JADX WARNING: type inference failed for: r3v11 */
    /* JADX WARNING: type inference failed for: r3v12 */
    /* JADX WARNING: type inference failed for: r3v13 */
    /* JADX WARNING: type inference failed for: r3v14 */
    /* JADX WARNING: type inference failed for: r3v15 */
    /* access modifiers changed from: protected */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r3v4
  assigns: []
  uses: []
  mth insns count: 155
    	at jadx.core.dex.visitors.typeinference.TypeSearch.fillTypeCandidates(TypeSearch.java:237)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.typeinference.TypeSearch.run(TypeSearch.java:53)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.runMultiVariableSearch(TypeInferenceVisitor.java:99)
    	at jadx.core.dex.visitors.typeinference.TypeInferenceVisitor.visit(TypeInferenceVisitor.java:92)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
    	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
    	at jadx.core.ProcessClass.process(ProcessClass.java:30)
    	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:49)
    	at java.util.ArrayList.forEach(ArrayList.java:1259)
    	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:49)
    	at jadx.core.ProcessClass.process(ProcessClass.java:35)
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 3 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void decodeBitmapData() {
        ? r3;
        int i;
        int i2;
        int i3;
        int i4;
        byte b;
        short s;
        int i5;
        short s2;
        byte b2;
        ? r32;
        int i6 = this.iw * this.ih;
        byte[] bArr = this.pixels;
        if (bArr == null || bArr.length < i6) {
            this.pixels = new byte[i6];
        }
        if (this.prefix == null) {
            this.prefix = new short[4096];
        }
        if (this.suffix == null) {
            this.suffix = new byte[4096];
        }
        if (this.pixelStack == null) {
            this.pixelStack = new byte[4097];
        }
        int read = read();
        int i7 = 1 << read;
        int i8 = i7 + 1;
        int i9 = i7 + 2;
        int i10 = read + 1;
        int i11 = (1 << i10) - 1;
        for (int i12 = 0; i12 < i7; i12++) {
            this.prefix[i12] = 0;
            this.suffix[i12] = (byte) i12;
        }
        int i13 = i10;
        int i14 = i11;
        int i15 = 0;
        int i16 = 0;
        int i17 = 0;
        int i18 = 0;
        int i19 = 0;
        int i20 = 0;
        byte b3 = 0;
        int i21 = 0;
        ? r33 = -1;
        int i22 = i9;
        while (i15 < i6) {
            if (i16 == 0) {
                if (i17 >= i13) {
                    byte b4 = i18 & i14;
                    i18 >>= i13;
                    i17 -= i13;
                    if (b4 > i22 || b4 == i8) {
                        break;
                    }
                    if (b4 == i7) {
                        i13 = i10;
                        i22 = i9;
                        i14 = i11;
                        b2 = -1;
                    } else if (r3 == -1) {
                        int i23 = i16 + 1;
                        int i24 = i10;
                        this.pixelStack[i16] = this.suffix[b4];
                        b2 = b4;
                        b3 = b2;
                        i16 = i23;
                        i10 = i24;
                    } else {
                        i2 = i10;
                        if (b4 == i22) {
                            i5 = i16 + 1;
                            s = b4;
                            this.pixelStack[i16] = (byte) b3;
                            s2 = r3;
                        } else {
                            s = b4;
                            i5 = i16;
                            s2 = s;
                        }
                        while (s2 > i7) {
                            int i25 = i5 + 1;
                            int i26 = i7;
                            this.pixelStack[i5] = this.suffix[s2];
                            s2 = this.prefix[s2];
                            i5 = i25;
                            i7 = i26;
                        }
                        i3 = i7;
                        byte[] bArr2 = this.suffix;
                        b = bArr2[s2] & -1;
                        if (i22 >= 4096) {
                            break;
                        }
                        i4 = i5 + 1;
                        i = i8;
                        byte b5 = (byte) b;
                        this.pixelStack[i5] = b5;
                        this.prefix[i22] = (short) r3;
                        bArr2[i22] = b5;
                        int i27 = i22 + 1;
                        if ((i27 & i14) == 0 && i27 < 4096) {
                            int i28 = i13 + 1;
                            int i29 = i14 + i27;
                        }
                        short s3 = s;
                    }
                    r3 = r32;
                    r33 = r3;
                } else {
                    if (i19 == 0) {
                        i19 = readBlock();
                        if (i19 <= 0) {
                            break;
                        }
                        i20 = 0;
                    }
                    i18 += (this.block[i20] & -1) << i17;
                    i17 += 8;
                    i20++;
                    i19--;
                    r33 = r3;
                }
            } else {
                i2 = i10;
                i = i8;
                byte b6 = b3;
                i3 = i7;
                b = b6;
            }
            i16 = i4 - 1;
            int i30 = i21 + 1;
            this.pixels[i21] = this.pixelStack[i16];
            i15++;
            i21 = i30;
            i7 = i3;
            i8 = i;
            b3 = b;
            i10 = i2;
            r3 = r33;
            r33 = r3;
        }
        for (int i31 = i21; i31 < i6; i31++) {
            this.pixels[i31] = 0;
        }
    }

    /* access modifiers changed from: protected */
    public boolean err() {
        return this.status != 0;
    }

    public Bitmap getBitmap() {
        return getFrame(0);
    }

    public int getDelay(int i) {
        this.delay = -1;
        int frameCount = getFrameCount();
        if (i >= 0 && i < frameCount) {
            this.delay = ((GifFrame) this.frames.elementAt(i)).delay;
        }
        return this.delay;
    }

    public Bitmap getFrame(int i) {
        int frameCount = getFrameCount();
        if (frameCount <= 0) {
            return null;
        }
        return ((GifFrame) this.frames.elementAt(i % frameCount)).image;
    }

    public int getFrameCount() {
        Vector vector = this.frames;
        if (vector == null) {
            return 0;
        }
        return vector.size();
    }

    public int getHeight() {
        return this.height;
    }

    public int getLoopCount() {
        return this.loopCount;
    }

    public int getRealFrameCount() {
        if (this.mDecodeToTheEnd) {
            return this.mDecodedFrames;
        }
        return 0;
    }

    public int getWidth() {
        return this.width;
    }

    /* access modifiers changed from: protected */
    public void init() {
        this.status = 0;
        this.frames = new Vector();
        this.gct = null;
        this.lct = null;
    }

    public boolean isDecodeToTheEnd() {
        return this.mDecodeToTheEnd;
    }

    /* access modifiers changed from: protected */
    public int read() {
        try {
            this = this;
            this = this.in.read();
            r1 = this;
            return this;
        } catch (Exception unused) {
            r1.status = 1;
            return 0;
        }
    }

    public int read(InputStream inputStream) {
        this.mDecodeToTheEnd = false;
        if (!this.calledOnce) {
            this.calledOnce = true;
            init();
            if (inputStream != null) {
                this.in = new BufferedInputStream(inputStream);
                try {
                    readHeader();
                    if (!this.mCancel && !err()) {
                        readContents();
                        if (getFrameCount() < 0) {
                            this.status = 1;
                        }
                    }
                } catch (OutOfMemoryError unused) {
                    this.status = 2;
                    recycle();
                }
            } else {
                this.status = 2;
            }
            if (this.mCancel) {
                recycle();
                this.status = 3;
            }
            return this.status;
        }
        throw new IllegalStateException("decoder cannot be called more than once");
    }

    /* access modifiers changed from: protected */
    public void readBitmap() {
        this.ix = readShort();
        this.iy = readShort();
        this.iw = readShort();
        this.ih = readShort();
        int read = read();
        int i = 0;
        this.lctFlag = (read & 128) != 0;
        this.lctSize = 2 << (read & 7);
        this.interlace = (read & 64) != 0;
        if (this.lctFlag) {
            this.lct = readColorTable(this.lctSize);
            this.act = this.lct;
        } else {
            this.act = this.gct;
            if (this.bgIndex == this.transIndex) {
                this.bgColor = 0;
            }
        }
        if (this.transparency) {
            int[] iArr = this.act;
            int i2 = this.transIndex;
            int i3 = iArr[i2];
            iArr[i2] = 0;
            i = i3;
        }
        if (this.act == null) {
            this.status = 1;
        }
        if (!err()) {
            decodeBitmapData();
            skip();
            if (!err() && !this.mCancel) {
                setPixels();
                if (this.mDecodedFrames >= this.mStartFrame) {
                    this.frames.addElement(new GifFrame(this.image, this.delay));
                }
                this.mDecodedFrames++;
                if (this.transparency) {
                    this.act[this.transIndex] = i;
                }
                resetFrame();
            }
        }
    }

    /* access modifiers changed from: protected */
    public int readBlock() {
        this.blockSize = read();
        int i = 0;
        if (this.blockSize > 0) {
            while (i < this.blockSize) {
                try {
                    int read = this.in.read(this.block, i, this.blockSize - i);
                    if (read == -1) {
                        break;
                    }
                    i += read;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (i < this.blockSize) {
                this.status = 1;
            }
        }
        return i;
    }

    /* access modifiers changed from: protected */
    public int[] readColorTable(int i) {
        int i2;
        int i3 = i * 3;
        byte[] bArr = new byte[i3];
        int i4 = 0;
        try {
            i2 = this.in.read(bArr, 0, bArr.length);
        } catch (Exception e) {
            e.printStackTrace();
            i2 = 0;
        }
        if (i2 < i3) {
            this.status = 1;
            return null;
        }
        int[] iArr = new int[256];
        int i5 = 0;
        while (i4 < i) {
            int i6 = i5 + 1;
            int i7 = i6 + 1;
            int i8 = i7 + 1;
            int i9 = i4 + 1;
            iArr[i4] = ((bArr[i5] & -1) << 16) | 0 | ((bArr[i6] & -1) << 8) | (bArr[i7] & -1);
            i5 = i8;
            i4 = i9;
        }
        return iArr;
    }

    /* access modifiers changed from: protected */
    public void readContents() {
        this.mDecodedFrames = 0;
        boolean z = false;
        while (!z && !err() && !this.mCancel) {
            int read = read();
            if (read != 33) {
                if (read == 44) {
                    int size = this.frames.size();
                    readBitmap();
                    if (this.frames.size() > size) {
                        this.mDecodeBmSize += (long) (this.image.getRowBytes() * this.image.getHeight());
                    }
                    if (this.mDecodeBmSize <= this.mMaxDecodeSize) {
                    }
                } else if (read != 59) {
                    this.status = 1;
                } else {
                    this.mDecodeToTheEnd = true;
                }
                z = true;
            } else {
                int read2 = read();
                if (read2 != 1) {
                    if (read2 == 249) {
                        readGraphicControlExt();
                    } else if (read2 != 254 && read2 == 255) {
                        readBlock();
                        String str = "";
                        for (int i = 0; i < 11; i++) {
                            StringBuilder sb = new StringBuilder();
                            sb.append(str);
                            sb.append((char) this.block[i]);
                            str = sb.toString();
                        }
                        if (str.equals("NETSCAPE2.0")) {
                            readNetscapeExt();
                        }
                    }
                }
                skip();
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readGraphicControlExt() {
        read();
        int read = read();
        this.dispose = (read & 28) >> 2;
        boolean z = true;
        if (this.dispose == 0) {
            this.dispose = 1;
        }
        if ((read & 1) == 0) {
            z = false;
        }
        this.transparency = z;
        this.delay = readShort() * 10;
        if (this.delay <= 0) {
            this.delay = 100;
        }
        this.transIndex = read();
        read();
    }

    /* access modifiers changed from: protected */
    public void readHeader() {
        if (!this.mCancel) {
            String str = "";
            for (int i = 0; i < 6; i++) {
                StringBuilder sb = new StringBuilder();
                sb.append(str);
                sb.append((char) read());
                str = sb.toString();
            }
            if (!str.startsWith("GIF")) {
                this.status = 1;
                return;
            }
            readLSD();
            if (this.gctFlag && !err()) {
                this.gct = readColorTable(this.gctSize);
                this.bgColor = this.gct[this.bgIndex];
            }
        }
    }

    /* access modifiers changed from: protected */
    public void readLSD() {
        this.width = readShort();
        this.height = readShort();
        int read = read();
        this.gctFlag = (read & 128) != 0;
        this.gctSize = 2 << (read & 7);
        this.bgIndex = read();
        this.pixelAspect = read();
    }

    /* access modifiers changed from: protected */
    public void readNetscapeExt() {
        do {
            readBlock();
            byte[] bArr = this.block;
            if (bArr[0] == 1) {
                this.loopCount = ((bArr[2] & -1) << 8) | (bArr[1] & -1);
            }
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }

    /* access modifiers changed from: protected */
    public int readShort() {
        return (read() << 8) | read();
    }

    public void recycle() {
        Vector vector = this.frames;
        if (vector != null) {
            int size = vector.size();
            for (int i = 0; i < size; i++) {
                ((GifFrame) this.frames.elementAt(i)).recycle();
            }
        }
    }

    public void requestCancelDecode() {
        this.mCancel = true;
        requestCancel();
    }

    /* access modifiers changed from: protected */
    public void resetFrame() {
        this.lastDispose = this.dispose;
        this.lrx = this.ix;
        this.lry = this.iy;
        this.lrw = this.iw;
        this.lrh = this.ih;
        this.lastBitmap = this.image;
        this.lastBgColor = this.bgColor;
        this.dispose = 0;
        this.transparency = false;
        this.delay = 0;
        this.lct = null;
    }

    public void setMaxDecodeSize(long j) {
        this.mMaxDecodeSize = j;
    }

    /* access modifiers changed from: protected */
    public void setPixels() {
        int i;
        if (this.dest == null) {
            this.dest = new int[(this.width * this.height)];
        }
        int i2 = this.lastDispose;
        int i3 = 0;
        if (i2 > 0) {
            if (i2 == 3) {
                int frameCount = getFrameCount() - 2;
                if (frameCount > 0) {
                    Bitmap frame = getFrame(frameCount - 1);
                    if (!frame.equals(this.lastBitmap)) {
                        this.lastBitmap = frame;
                        Bitmap bitmap = this.lastBitmap;
                        int[] iArr = this.dest;
                        int i4 = this.width;
                        bitmap.getPixels(iArr, 0, i4, 0, 0, i4, this.height);
                    }
                } else {
                    this.lastBitmap = null;
                    this.dest = new int[(this.width * this.height)];
                }
            }
            if (this.lastBitmap != null && this.lastDispose == 2) {
                int i5 = !this.transparency ? this.lastBgColor : 0;
                int i6 = (this.lry * this.width) + this.lrx;
                for (int i7 = 0; i7 < this.lrh; i7++) {
                    int i8 = this.lrw + i6;
                    for (int i9 = i6; i9 < i8; i9++) {
                        this.dest[i9] = i5;
                    }
                    i6 += this.width;
                }
            }
        }
        int i10 = 8;
        int i11 = 0;
        int i12 = 1;
        while (true) {
            int i13 = this.ih;
            if (i3 >= i13) {
                break;
            }
            if (this.interlace) {
                if (i11 >= i13) {
                    i12++;
                    if (i12 == 2) {
                        i11 = 4;
                    } else if (i12 == 3) {
                        i11 = 2;
                        i10 = 4;
                    } else if (i12 == 4) {
                        i10 = 2;
                        i11 = 1;
                    }
                }
                i = i11 + i10;
            } else {
                i = i11;
                i11 = i3;
            }
            int i14 = i11 + this.iy;
            if (i14 < this.height) {
                int i15 = this.width;
                int i16 = i14 * i15;
                int i17 = this.ix + i16;
                int i18 = this.iw + i17;
                if (i16 + i15 < i18) {
                    i18 = i16 + i15;
                }
                int i19 = this.iw * i3;
                while (i17 < i18) {
                    int i20 = i19 + 1;
                    int i21 = this.act[this.pixels[i19] & -1];
                    if (i21 != 0) {
                        this.dest[i17] = i21;
                    }
                    i17++;
                    i19 = i20;
                }
            }
            i3++;
            i11 = i;
        }
        if (this.mDecodedFrames <= this.mStartFrame) {
            Bitmap bitmap2 = this.image;
            if (bitmap2 != null && !bitmap2.isRecycled()) {
                this.image.recycle();
            }
        }
        this.image = Bitmap.createBitmap(this.dest, this.width, this.height, Config.ARGB_8888);
    }

    public void setStartFrame(int i) {
        this.mStartFrame = i;
    }

    /* access modifiers changed from: protected */
    public void skip() {
        do {
            readBlock();
            if (this.blockSize <= 0) {
                return;
            }
        } while (!err());
    }
}
