package com.bumptech.glide.gifdecoder;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.util.Log;
import androidx.annotation.ColorInt;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.gifdecoder.GifDecoder.BitmapProvider;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Arrays;
import java.util.Iterator;

public class StandardGifDecoder implements GifDecoder {
    private static final int BYTES_PER_INTEGER = 4;
    @ColorInt
    private static final int COLOR_TRANSPARENT_BLACK = 0;
    private static final int INITIAL_FRAME_POINTER = -1;
    private static final int MASK_INT_LOWEST_BYTE = 255;
    private static final int MAX_STACK_SIZE = 4096;
    private static final int NULL_CODE = -1;
    private static final String TAG = "StandardGifDecoder";
    @ColorInt
    private int[] act;
    @NonNull
    private Config bitmapConfig;
    private final BitmapProvider bitmapProvider;
    private byte[] block;
    private int downsampledHeight;
    private int downsampledWidth;
    private int framePointer;
    private GifHeader header;
    @Nullable
    private Boolean isFirstFrameTransparent;
    private byte[] mainPixels;
    @ColorInt
    private int[] mainScratch;
    private GifHeaderParser parser;
    @ColorInt
    private final int[] pct;
    private byte[] pixelStack;
    private short[] prefix;
    private Bitmap previousImage;
    private ByteBuffer rawData;
    private int sampleSize;
    private boolean savePrevious;
    private int status;
    private byte[] suffix;

    public StandardGifDecoder(@NonNull BitmapProvider bitmapProvider2) {
        this.pct = new int[256];
        this.bitmapConfig = Config.ARGB_8888;
        this.bitmapProvider = bitmapProvider2;
        this.header = new GifHeader();
    }

    public StandardGifDecoder(@NonNull BitmapProvider bitmapProvider2, GifHeader gifHeader, ByteBuffer byteBuffer) {
        this(bitmapProvider2, gifHeader, byteBuffer, 1);
    }

    public StandardGifDecoder(@NonNull BitmapProvider bitmapProvider2, GifHeader gifHeader, ByteBuffer byteBuffer, int i) {
        this(bitmapProvider2);
        setData(gifHeader, byteBuffer, i);
    }

    @ColorInt
    private int averageColorsNear(int i, int i2, int i3) {
        int i4 = i;
        int i5 = 0;
        int i6 = 0;
        int i7 = 0;
        int i8 = 0;
        int i9 = 0;
        while (i4 < this.sampleSize + i) {
            byte[] bArr = this.mainPixels;
            if (i4 >= bArr.length || i4 >= i2) {
                break;
            }
            int i10 = this.act[bArr[i4] & -1];
            if (i10 != 0) {
                i5 += (i10 >> 24) & 255;
                i6 += (i10 >> 16) & 255;
                i7 += (i10 >> 8) & 255;
                i8 += i10 & 255;
                i9++;
            }
            i4++;
        }
        int i11 = i + i3;
        int i12 = i11;
        while (i12 < this.sampleSize + i11) {
            byte[] bArr2 = this.mainPixels;
            if (i12 >= bArr2.length || i12 >= i2) {
                break;
            }
            int i13 = this.act[bArr2[i12] & -1];
            if (i13 != 0) {
                i5 += (i13 >> 24) & 255;
                i6 += (i13 >> 16) & 255;
                i7 += (i13 >> 8) & 255;
                i8 += i13 & 255;
                i9++;
            }
            i12++;
        }
        if (i9 == 0) {
            return 0;
        }
        return ((i5 / i9) << 24) | ((i6 / i9) << 16) | ((i7 / i9) << 8) | (i8 / i9);
    }

    private void copyCopyIntoScratchRobust(GifFrame gifFrame) {
        Boolean bool;
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        GifFrame gifFrame2 = gifFrame;
        int[] iArr = this.mainScratch;
        int i6 = gifFrame2.ih;
        int i7 = this.sampleSize;
        int i8 = i6 / i7;
        int i9 = gifFrame2.iy / i7;
        int i10 = gifFrame2.iw / i7;
        int i11 = gifFrame2.ix / i7;
        int i12 = this.framePointer;
        Boolean valueOf = Boolean.valueOf(true);
        boolean z = i12 == 0;
        int i13 = this.sampleSize;
        int i14 = this.downsampledWidth;
        int i15 = this.downsampledHeight;
        byte[] bArr = this.mainPixels;
        int[] iArr2 = this.act;
        int i16 = 1;
        int i17 = 8;
        int i18 = 0;
        Boolean bool2 = this.isFirstFrameTransparent;
        int i19 = 0;
        while (i19 < i8) {
            Boolean bool3 = valueOf;
            if (gifFrame2.interlace) {
                if (i18 >= i8) {
                    i = i8;
                    i5 = i16 + 1;
                    if (i5 == 2) {
                        i18 = 4;
                    } else if (i5 == 3) {
                        i17 = 4;
                        i18 = 2;
                    } else if (i5 == 4) {
                        i18 = 1;
                        i17 = 2;
                    }
                } else {
                    i = i8;
                    i5 = i16;
                }
                i2 = i18 + i17;
                i16 = i5;
            } else {
                i = i8;
                i2 = i18;
                i18 = i19;
            }
            int i20 = i18 + i9;
            boolean z2 = i13 == 1;
            if (i20 < i15) {
                int i21 = i20 * i14;
                int i22 = i21 + i11;
                int i23 = i22 + i10;
                int i24 = i21 + i14;
                if (i24 < i23) {
                    i23 = i24;
                }
                i3 = i9;
                int i25 = i19 * i13 * gifFrame2.iw;
                if (z2) {
                    int i26 = i22;
                    while (true) {
                        i4 = i10;
                        if (i26 >= i23) {
                            break;
                        }
                        int i27 = iArr2[bArr[i25] & -1];
                        if (i27 != 0) {
                            iArr[i26] = i27;
                        } else if (z && bool == null) {
                            bool = bool3;
                        }
                        i25 += i13;
                        i26++;
                        i10 = i4;
                    }
                } else {
                    i4 = i10;
                    int i28 = ((i23 - i22) * i13) + i25;
                    int i29 = i22;
                    while (i29 < i23) {
                        int i30 = i23;
                        int averageColorsNear = averageColorsNear(i25, i28, gifFrame2.iw);
                        if (averageColorsNear != 0) {
                            iArr[i29] = averageColorsNear;
                        } else if (z && bool == null) {
                            bool = bool3;
                        }
                        i25 += i13;
                        i29++;
                        i23 = i30;
                    }
                }
            } else {
                i3 = i9;
                i4 = i10;
            }
            i19++;
            i18 = i2;
            i10 = i4;
            valueOf = bool3;
            i8 = i;
            i9 = i3;
        }
        if (this.isFirstFrameTransparent == null) {
            this.isFirstFrameTransparent = Boolean.valueOf(bool == null ? false : bool.booleanValue());
        }
    }

    private void copyIntoScratchFast(GifFrame gifFrame) {
        GifFrame gifFrame2 = gifFrame;
        int[] iArr = this.mainScratch;
        int i = gifFrame2.ih;
        int i2 = gifFrame2.iy;
        int i3 = gifFrame2.iw;
        int i4 = gifFrame2.ix;
        boolean z = this.framePointer == 0;
        int i5 = this.downsampledWidth;
        byte[] bArr = this.mainPixels;
        int[] iArr2 = this.act;
        int i6 = 0;
        byte b = -1;
        while (i6 < i) {
            int i7 = (i6 + i2) * i5;
            int i8 = i7 + i4;
            int i9 = i8 + i3;
            int i10 = i7 + i5;
            if (i10 < i9) {
                i9 = i10;
            }
            int i11 = gifFrame2.iw * i6;
            int i12 = i8;
            while (i12 < i9) {
                byte b2 = bArr[i11];
                byte b3 = b2 & -1;
                if (b3 != b) {
                    int i13 = iArr2[b3];
                    if (i13 != 0) {
                        iArr[i12] = i13;
                    } else {
                        b = b2;
                    }
                }
                i11++;
                i12++;
                GifFrame gifFrame3 = gifFrame;
            }
            i6++;
            gifFrame2 = gifFrame;
        }
        Boolean bool = this.isFirstFrameTransparent;
        boolean z2 = (bool != null && bool.booleanValue()) || (this.isFirstFrameTransparent == null && z && b != -1);
        this.isFirstFrameTransparent = Boolean.valueOf(z2);
    }

    /* JADX WARNING: type inference failed for: r3v1, types: [short[]] */
    /* JADX WARNING: type inference failed for: r22v0 */
    /* JADX WARNING: type inference failed for: r22v1 */
    /* JADX WARNING: type inference failed for: r28v0 */
    /* JADX WARNING: type inference failed for: r28v1 */
    /* JADX WARNING: type inference failed for: r15v1 */
    /* JADX WARNING: type inference failed for: r22v2 */
    /* JADX WARNING: type inference failed for: r22v3 */
    /* JADX WARNING: type inference failed for: r17v4 */
    /* JADX WARNING: type inference failed for: r28v2 */
    /* JADX WARNING: type inference failed for: r22v4 */
    /* JADX WARNING: type inference failed for: r4v16, types: [short] */
    /* JADX WARNING: type inference failed for: r4v18, types: [int] */
    /* JADX WARNING: type inference failed for: r28v4 */
    /* JADX WARNING: type inference failed for: r28v6 */
    /* JADX WARNING: type inference failed for: r22v5 */
    /* JADX WARNING: type inference failed for: r17v6 */
    /* JADX WARNING: type inference failed for: r28v7 */
    /* JADX WARNING: type inference failed for: r4v22 */
    /* JADX WARNING: type inference failed for: r28v8 */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short, code=int, for r4v16, types: [short] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=short[], code=null, for r3v1, types: [short[]] */
    /* JADX WARNING: Multi-variable type inference failed. Error: jadx.core.utils.exceptions.JadxRuntimeException: No candidate types for var: r22v3
  assigns: []
  uses: []
  mth insns count: 168
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
    	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:311)
    	at jadx.api.JavaClass.decompile(JavaClass.java:62)
    	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:217)
     */
    /* JADX WARNING: Unknown variable types count: 10 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void decodeBitmapData(GifFrame gifFrame) {
        int i;
        int i2;
        int i3;
        int i4;
        int i5;
        ? r22;
        int i6;
        ? r28;
        ? r4;
        int i7;
        int i8;
        ? r282;
        StandardGifDecoder standardGifDecoder = this;
        GifFrame gifFrame2 = gifFrame;
        if (gifFrame2 != null) {
            standardGifDecoder.rawData.position(gifFrame2.bufferFrameStart);
        }
        if (gifFrame2 == null) {
            GifHeader gifHeader = standardGifDecoder.header;
            i = gifHeader.width;
            i2 = gifHeader.height;
        } else {
            i = gifFrame2.iw;
            i2 = gifFrame2.ih;
        }
        int i9 = i * i2;
        byte[] bArr = standardGifDecoder.mainPixels;
        if (bArr == null || bArr.length < i9) {
            standardGifDecoder.mainPixels = standardGifDecoder.bitmapProvider.obtainByteArray(i9);
        }
        byte[] bArr2 = standardGifDecoder.mainPixels;
        if (standardGifDecoder.prefix == null) {
            standardGifDecoder.prefix = new short[4096];
        }
        ? r3 = standardGifDecoder.prefix;
        if (standardGifDecoder.suffix == null) {
            standardGifDecoder.suffix = new byte[4096];
        }
        byte[] bArr3 = standardGifDecoder.suffix;
        if (standardGifDecoder.pixelStack == null) {
            standardGifDecoder.pixelStack = new byte[4097];
        }
        byte[] bArr4 = standardGifDecoder.pixelStack;
        int readByte = readByte();
        int i10 = 1 << readByte;
        int i11 = i10 + 1;
        int i12 = i10 + 2;
        int i13 = readByte + 1;
        int i14 = (1 << i13) - 1;
        int i15 = 0;
        for (int i16 = 0; i16 < i10; i16++) {
            r3[i16] = 0;
            bArr3[i16] = (byte) i16;
        }
        byte[] bArr5 = standardGifDecoder.block;
        int i17 = i13;
        int i18 = i12;
        int i19 = i14;
        int i20 = 0;
        ? r17 = 0;
        int i21 = 0;
        int i22 = 0;
        int i23 = 0;
        ? r222 = 0;
        int i24 = 0;
        int i25 = -1;
        while (true) {
            if (i15 >= i9) {
                break;
            }
            if (i20 == 0) {
                i20 = readBlock();
                if (i20 <= 0) {
                    standardGifDecoder.status = 3;
                    break;
                }
                i22 = 0;
            }
            i21 += (bArr5[i22] & -1) << r17;
            i22++;
            i20--;
            int i26 = r17 + 8;
            int i27 = i25;
            ? r283 = r222;
            int i28 = i18;
            int i29 = i23;
            int i30 = i15;
            int i31 = i17;
            while (true) {
                if (i26 < i31) {
                    i17 = i31;
                    r22 = r283;
                    i15 = i30;
                    i23 = i29;
                    i6 = i26;
                    i18 = i28;
                    i25 = i27;
                    standardGifDecoder = this;
                    break;
                }
                byte b = i21 & i3;
                i21 >>= i31;
                i26 -= i31;
                if (b == i10) {
                    i31 = i5;
                    i28 = i12;
                    i3 = i14;
                    i27 = -1;
                    r28 = r283;
                } else if (b == i11) {
                    i6 = i26;
                    i17 = i31;
                    i15 = i30;
                    i23 = i29;
                    i18 = i28;
                    r22 = r283;
                    i25 = i27;
                    break;
                } else {
                    if (i27 == -1) {
                        bArr2[i29] = bArr3[b];
                        i29++;
                        i30++;
                        i27 = b;
                        i7 = i27;
                    } else {
                        int i32 = i28;
                        int i33 = i26;
                        if (b >= i32) {
                            bArr4[i4] = (byte) r283;
                            i4++;
                            i8 = i27;
                        } else {
                            i8 = b;
                        }
                        while (r4 >= i10) {
                            bArr4[i4] = bArr3[r4];
                            i4++;
                            r4 = r3[r4];
                        }
                        byte b2 = bArr3[r4] & -1;
                        int i34 = i5;
                        byte b3 = (byte) b2;
                        bArr2[i29] = b3;
                        while (true) {
                            i29++;
                            i30++;
                            if (i4 <= 0) {
                                break;
                            }
                            i4--;
                            bArr2[i29] = bArr4[i4];
                        }
                        byte b4 = b2;
                        if (i32 < 4096) {
                            r3[i32] = (short) i27;
                            bArr3[i32] = b3;
                            i32++;
                            if ((i32 & i3) == 0 && i32 < 4096) {
                                i31++;
                                i3 += i32;
                            }
                        }
                        i27 = b;
                        i26 = i33;
                        i5 = i34;
                        i7 = b4;
                        i28 = i32;
                    }
                    standardGifDecoder = this;
                    r28 = r282;
                }
                r283 = r28;
            }
            r222 = r22;
            r17 = i6;
        }
        Arrays.fill(bArr2, i23, i9, 0);
    }

    @NonNull
    private GifHeaderParser getHeaderParser() {
        if (this.parser == null) {
            this.parser = new GifHeaderParser();
        }
        return this.parser;
    }

    private Bitmap getNextBitmap() {
        Boolean bool = this.isFirstFrameTransparent;
        Config config = (bool == null || bool.booleanValue()) ? Config.ARGB_8888 : this.bitmapConfig;
        Bitmap obtain = this.bitmapProvider.obtain(this.downsampledWidth, this.downsampledHeight, config);
        obtain.setHasAlpha(true);
        return obtain;
    }

    private int readBlock() {
        int readByte = readByte();
        if (readByte <= 0) {
            return readByte;
        }
        ByteBuffer byteBuffer = this.rawData;
        byteBuffer.get(this.block, 0, Math.min(readByte, byteBuffer.remaining()));
        return readByte;
    }

    private int readByte() {
        return this.rawData.get() & -1;
    }

    private Bitmap setPixels(GifFrame gifFrame, GifFrame gifFrame2) {
        int[] iArr = this.mainScratch;
        int i = 0;
        if (gifFrame2 == null) {
            Bitmap bitmap = this.previousImage;
            if (bitmap != null) {
                this.bitmapProvider.release(bitmap);
            }
            this.previousImage = null;
            Arrays.fill(iArr, 0);
        }
        if (gifFrame2 != null && gifFrame2.dispose == 3 && this.previousImage == null) {
            Arrays.fill(iArr, 0);
        }
        if (gifFrame2 != null) {
            int i2 = gifFrame2.dispose;
            if (i2 > 0) {
                if (i2 == 2) {
                    if (!gifFrame.transparency) {
                        GifHeader gifHeader = this.header;
                        int i3 = gifHeader.bgColor;
                        if (gifFrame.lct == null || gifHeader.bgIndex != gifFrame.transIndex) {
                            i = i3;
                        }
                    }
                    int i4 = gifFrame2.ih;
                    int i5 = this.sampleSize;
                    int i6 = i4 / i5;
                    int i7 = gifFrame2.iy / i5;
                    int i8 = gifFrame2.iw / i5;
                    int i9 = gifFrame2.ix / i5;
                    int i10 = this.downsampledWidth;
                    int i11 = (i7 * i10) + i9;
                    int i12 = (i6 * i10) + i11;
                    while (i11 < i12) {
                        int i13 = i11 + i8;
                        for (int i14 = i11; i14 < i13; i14++) {
                            iArr[i14] = i;
                        }
                        i11 += this.downsampledWidth;
                    }
                } else if (i2 == 3) {
                    Bitmap bitmap2 = this.previousImage;
                    if (bitmap2 != null) {
                        int i15 = this.downsampledWidth;
                        bitmap2.getPixels(iArr, 0, i15, 0, 0, i15, this.downsampledHeight);
                    }
                }
            }
        }
        decodeBitmapData(gifFrame);
        if (gifFrame.interlace || this.sampleSize != 1) {
            copyCopyIntoScratchRobust(gifFrame);
        } else {
            copyIntoScratchFast(gifFrame);
        }
        if (this.savePrevious) {
            int i16 = gifFrame.dispose;
            if (i16 == 0 || i16 == 1) {
                if (this.previousImage == null) {
                    this.previousImage = getNextBitmap();
                }
                Bitmap bitmap3 = this.previousImage;
                int i17 = this.downsampledWidth;
                bitmap3.setPixels(iArr, 0, i17, 0, 0, i17, this.downsampledHeight);
            }
        }
        Bitmap nextBitmap = getNextBitmap();
        int i18 = this.downsampledWidth;
        nextBitmap.setPixels(iArr, 0, i18, 0, 0, i18, this.downsampledHeight);
        return nextBitmap;
    }

    public void advance() {
        this.framePointer = (this.framePointer + 1) % this.header.frameCount;
    }

    public void clear() {
        this.header = null;
        byte[] bArr = this.mainPixels;
        if (bArr != null) {
            this.bitmapProvider.release(bArr);
        }
        int[] iArr = this.mainScratch;
        if (iArr != null) {
            this.bitmapProvider.release(iArr);
        }
        Bitmap bitmap = this.previousImage;
        if (bitmap != null) {
            this.bitmapProvider.release(bitmap);
        }
        this.previousImage = null;
        this.rawData = null;
        this.isFirstFrameTransparent = null;
        byte[] bArr2 = this.block;
        if (bArr2 != null) {
            this.bitmapProvider.release(bArr2);
        }
    }

    public int getByteSize() {
        return this.rawData.limit() + this.mainPixels.length + (this.mainScratch.length * 4);
    }

    public int getCurrentFrameIndex() {
        return this.framePointer;
    }

    @NonNull
    public ByteBuffer getData() {
        return this.rawData;
    }

    public int getDelay(int i) {
        if (i >= 0) {
            GifHeader gifHeader = this.header;
            if (i < gifHeader.frameCount) {
                return ((GifFrame) gifHeader.frames.get(i)).delay;
            }
        }
        return -1;
    }

    public int getFrameCount() {
        return this.header.frameCount;
    }

    public int getHeight() {
        return this.header.height;
    }

    @Deprecated
    public int getLoopCount() {
        int i = this.header.loopCount;
        if (i == -1) {
            return 1;
        }
        return i;
    }

    public int getNetscapeLoopCount() {
        return this.header.loopCount;
    }

    public int getNextDelay() {
        if (this.header.frameCount > 0) {
            int i = this.framePointer;
            if (i >= 0) {
                return getDelay(i);
            }
        }
        return 0;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00f7, code lost:
        return null;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @Nullable
    public synchronized Bitmap getNextFrame() {
        if (this.header.frameCount <= 0 || this.framePointer < 0) {
            if (Log.isLoggable(TAG, 3)) {
                String str = TAG;
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to decode frame, frameCount=");
                sb.append(this.header.frameCount);
                sb.append(", framePointer=");
                sb.append(this.framePointer);
                Log.d(str, sb.toString());
            }
            this.status = 1;
        }
        if (this.status != 1) {
            if (this.status != 2) {
                this.status = 0;
                if (this.block == null) {
                    this.block = this.bitmapProvider.obtainByteArray(255);
                }
                GifFrame gifFrame = (GifFrame) this.header.frames.get(this.framePointer);
                int i = this.framePointer - 1;
                GifFrame gifFrame2 = i >= 0 ? (GifFrame) this.header.frames.get(i) : null;
                this.act = gifFrame.lct != null ? gifFrame.lct : this.header.gct;
                if (this.act == null) {
                    if (Log.isLoggable(TAG, 3)) {
                        String str2 = TAG;
                        StringBuilder sb2 = new StringBuilder();
                        sb2.append("No valid color table found for frame #");
                        sb2.append(this.framePointer);
                        Log.d(str2, sb2.toString());
                    }
                    this.status = 1;
                    return null;
                }
                if (gifFrame.transparency) {
                    System.arraycopy(this.act, 0, this.pct, 0, this.act.length);
                    this.act = this.pct;
                    this.act[gifFrame.transIndex] = 0;
                    if (gifFrame.dispose == 2 && this.framePointer == 0) {
                        this.isFirstFrameTransparent = Boolean.valueOf(true);
                    }
                }
                return setPixels(gifFrame, gifFrame2);
            }
        }
        if (Log.isLoggable(TAG, 3)) {
            String str3 = TAG;
            StringBuilder sb3 = new StringBuilder();
            sb3.append("Unable to decode frame, status=");
            sb3.append(this.status);
            Log.d(str3, sb3.toString());
        }
    }

    public int getStatus() {
        return this.status;
    }

    public int getTotalIterationCount() {
        int i = this.header.loopCount;
        if (i == -1) {
            return 1;
        }
        if (i == 0) {
            return 0;
        }
        return i + 1;
    }

    public int getWidth() {
        return this.header.width;
    }

    public int read(@Nullable InputStream inputStream, int i) {
        if (inputStream != null) {
            try {
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(i > 0 ? i + 4096 : 16384);
                byte[] bArr = new byte[16384];
                while (true) {
                    int read = inputStream.read(bArr, 0, bArr.length);
                    if (read == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, read);
                }
                byteArrayOutputStream.flush();
                read(byteArrayOutputStream.toByteArray());
            } catch (IOException e) {
                Log.w(TAG, "Error reading data from stream", e);
            }
        } else {
            this.status = 2;
        }
        if (inputStream != null) {
            try {
                inputStream.close();
            } catch (IOException e2) {
                Log.w(TAG, "Error closing stream", e2);
            }
        }
        return this.status;
    }

    public synchronized int read(@Nullable byte[] bArr) {
        this.header = getHeaderParser().setData(bArr).parseHeader();
        if (bArr != null) {
            setData(this.header, bArr);
        }
        return this.status;
    }

    public void resetFrameIndex() {
        this.framePointer = -1;
    }

    public synchronized void setData(@NonNull GifHeader gifHeader, @NonNull ByteBuffer byteBuffer) {
        setData(gifHeader, byteBuffer, 1);
    }

    public synchronized void setData(@NonNull GifHeader gifHeader, @NonNull ByteBuffer byteBuffer, int i) {
        if (i > 0) {
            int highestOneBit = Integer.highestOneBit(i);
            this.status = 0;
            this.header = gifHeader;
            this.framePointer = -1;
            this.rawData = byteBuffer.asReadOnlyBuffer();
            this.rawData.position(0);
            this.rawData.order(ByteOrder.LITTLE_ENDIAN);
            this.savePrevious = false;
            Iterator it = gifHeader.frames.iterator();
            while (true) {
                if (it.hasNext()) {
                    if (((GifFrame) it.next()).dispose == 3) {
                        this.savePrevious = true;
                        break;
                    }
                } else {
                    break;
                }
            }
            this.sampleSize = highestOneBit;
            this.downsampledWidth = gifHeader.width / highestOneBit;
            this.downsampledHeight = gifHeader.height / highestOneBit;
            this.mainPixels = this.bitmapProvider.obtainByteArray(gifHeader.width * gifHeader.height);
            this.mainScratch = this.bitmapProvider.obtainIntArray(this.downsampledWidth * this.downsampledHeight);
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Sample size must be >=0, not: ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }
    }

    public synchronized void setData(@NonNull GifHeader gifHeader, @NonNull byte[] bArr) {
        setData(gifHeader, ByteBuffer.wrap(bArr));
    }

    public void setDefaultBitmapConfig(@NonNull Config config) {
        if (config == Config.ARGB_8888 || config == Config.RGB_565) {
            this.bitmapConfig = config;
            return;
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unsupported format: ");
        sb.append(config);
        sb.append(", must be one of ");
        sb.append(Config.ARGB_8888);
        sb.append(" or ");
        sb.append(Config.RGB_565);
        throw new IllegalArgumentException(sb.toString());
    }
}
