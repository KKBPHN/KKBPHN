package com.bumptech.glide.load.resource.bitmap;

import android.util.Log;
import androidx.annotation.NonNull;
import com.bumptech.glide.load.ImageHeaderParser;
import com.bumptech.glide.load.ImageHeaderParser.ImageType;
import com.bumptech.glide.load.engine.bitmap_recycle.ArrayPool;
import com.bumptech.glide.util.Preconditions;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.charset.Charset;

public final class DefaultImageHeaderParser implements ImageHeaderParser {
    private static final int[] BYTES_PER_FORMAT = {0, 1, 1, 2, 4, 8, 1, 1, 2, 4, 8, 4, 8};
    static final int EXIF_MAGIC_NUMBER = 65496;
    static final int EXIF_SEGMENT_TYPE = 225;
    private static final int GIF_HEADER = 4671814;
    private static final int INTEL_TIFF_MAGIC_NUMBER = 18761;
    private static final String JPEG_EXIF_SEGMENT_PREAMBLE = "Exif\u0000\u0000";
    static final byte[] JPEG_EXIF_SEGMENT_PREAMBLE_BYTES = JPEG_EXIF_SEGMENT_PREAMBLE.getBytes(Charset.forName("UTF-8"));
    private static final int MARKER_EOI = 217;
    private static final int MOTOROLA_TIFF_MAGIC_NUMBER = 19789;
    private static final int ORIENTATION_TAG_TYPE = 274;
    private static final int PNG_HEADER = -1991225785;
    private static final int RIFF_HEADER = 1380533830;
    private static final int SEGMENT_SOS = 218;
    static final int SEGMENT_START_ID = 255;
    private static final String TAG = "DfltImageHeaderParser";
    private static final int VP8_HEADER = 1448097792;
    private static final int VP8_HEADER_MASK = -256;
    private static final int VP8_HEADER_TYPE_EXTENDED = 88;
    private static final int VP8_HEADER_TYPE_LOSSLESS = 76;
    private static final int VP8_HEADER_TYPE_MASK = 255;
    private static final int WEBP_EXTENDED_ALPHA_FLAG = 16;
    private static final int WEBP_HEADER = 1464156752;
    private static final int WEBP_LOSSLESS_ALPHA_FLAG = 8;

    final class ByteBufferReader implements Reader {
        private final ByteBuffer byteBuffer;

        ByteBufferReader(ByteBuffer byteBuffer2) {
            this.byteBuffer = byteBuffer2;
            byteBuffer2.order(ByteOrder.BIG_ENDIAN);
        }

        public int getUInt16() {
            return getUInt8() | (getUInt8() << 8);
        }

        public short getUInt8() {
            if (this.byteBuffer.remaining() >= 1) {
                return (short) (this.byteBuffer.get() & -1);
            }
            throw new EndOfFileException();
        }

        public int read(byte[] bArr, int i) {
            int min = Math.min(i, this.byteBuffer.remaining());
            if (min == 0) {
                return -1;
            }
            this.byteBuffer.get(bArr, 0, min);
            return min;
        }

        public long skip(long j) {
            int min = (int) Math.min((long) this.byteBuffer.remaining(), j);
            ByteBuffer byteBuffer2 = this.byteBuffer;
            byteBuffer2.position(byteBuffer2.position() + min);
            return (long) min;
        }
    }

    final class RandomAccessReader {
        private final ByteBuffer data;

        RandomAccessReader(byte[] bArr, int i) {
            this.data = (ByteBuffer) ByteBuffer.wrap(bArr).order(ByteOrder.BIG_ENDIAN).limit(i);
        }

        private boolean isAvailable(int i, int i2) {
            return this.data.remaining() - i >= i2;
        }

        /* access modifiers changed from: 0000 */
        public short getInt16(int i) {
            if (isAvailable(i, 2)) {
                return this.data.getShort(i);
            }
            return -1;
        }

        /* access modifiers changed from: 0000 */
        public int getInt32(int i) {
            if (isAvailable(i, 4)) {
                return this.data.getInt(i);
            }
            return -1;
        }

        /* access modifiers changed from: 0000 */
        public int length() {
            return this.data.remaining();
        }

        /* access modifiers changed from: 0000 */
        public void order(ByteOrder byteOrder) {
            this.data.order(byteOrder);
        }
    }

    interface Reader {

        public final class EndOfFileException extends IOException {
            private static final long serialVersionUID = 1;

            EndOfFileException() {
                super("Unexpectedly reached end of a file");
            }
        }

        int getUInt16();

        short getUInt8();

        int read(byte[] bArr, int i);

        long skip(long j);
    }

    final class StreamReader implements Reader {
        private final InputStream is;

        StreamReader(InputStream inputStream) {
            this.is = inputStream;
        }

        public int getUInt16() {
            return getUInt8() | (getUInt8() << 8);
        }

        public short getUInt8() {
            int read = this.is.read();
            if (read != -1) {
                return (short) read;
            }
            throw new EndOfFileException();
        }

        public int read(byte[] bArr, int i) {
            int i2 = 0;
            int i3 = 0;
            while (i2 < i) {
                i3 = this.is.read(bArr, i2, i - i2);
                if (i3 == -1) {
                    break;
                }
                i2 += i3;
            }
            if (i2 != 0 || i3 != -1) {
                return i2;
            }
            throw new EndOfFileException();
        }

        public long skip(long j) {
            if (j < 0) {
                return 0;
            }
            long j2 = j;
            while (j2 > 0) {
                long skip = this.is.skip(j2);
                if (skip <= 0) {
                    if (this.is.read() == -1) {
                        break;
                    }
                    skip = 1;
                }
                j2 -= skip;
            }
            return j - j2;
        }
    }

    private static int calcTagOffset(int i, int i2) {
        return i + 2 + (i2 * 12);
    }

    private int getOrientation(Reader reader, ArrayPool arrayPool) {
        byte[] bArr;
        try {
            int uInt16 = reader.getUInt16();
            boolean handles = handles(uInt16);
            String str = TAG;
            if (!handles) {
                if (Log.isLoggable(str, 3)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Parser doesn't handle magic number: ");
                    sb.append(uInt16);
                    Log.d(str, sb.toString());
                }
                return -1;
            }
            int moveToExifSegmentAndGetLength = moveToExifSegmentAndGetLength(reader);
            if (moveToExifSegmentAndGetLength == -1) {
                if (Log.isLoggable(str, 3)) {
                    Log.d(str, "Failed to parse exif segment length, or exif segment not found");
                }
                return -1;
            }
            bArr = (byte[]) arrayPool.get(moveToExifSegmentAndGetLength, byte[].class);
            int parseExifSegment = parseExifSegment(reader, bArr, moveToExifSegmentAndGetLength);
            arrayPool.put(bArr);
            return parseExifSegment;
        } catch (EndOfFileException unused) {
            return -1;
        } catch (Throwable th) {
            arrayPool.put(bArr);
            throw th;
        }
    }

    /* JADX WARNING: Code restructure failed: missing block: B:20:0x003b, code lost:
        return com.bumptech.glide.load.ImageHeaderParser.ImageType.PNG;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [] */
    /* JADX WARNING: Missing exception handler attribute for start block: B:18:0x0039 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    @NonNull
    private ImageType getType(Reader reader) {
        try {
            int uInt16 = reader.getUInt16();
            if (uInt16 == EXIF_MAGIC_NUMBER) {
                return ImageType.JPEG;
            }
            short uInt8 = (uInt16 << 8) | reader.getUInt8();
            if (uInt8 == GIF_HEADER) {
                return ImageType.GIF;
            }
            short uInt82 = (uInt8 << 8) | reader.getUInt8();
            if (uInt82 == PNG_HEADER) {
                reader.skip(21);
                return reader.getUInt8() >= 3 ? ImageType.PNG_A : ImageType.PNG;
            } else if (uInt82 != RIFF_HEADER) {
                return ImageType.UNKNOWN;
            } else {
                reader.skip(4);
                if (((reader.getUInt16() << 16) | reader.getUInt16()) != WEBP_HEADER) {
                    return ImageType.UNKNOWN;
                }
                int uInt162 = (reader.getUInt16() << 16) | reader.getUInt16();
                if ((uInt162 & -256) != VP8_HEADER) {
                    return ImageType.UNKNOWN;
                }
                int i = uInt162 & 255;
                if (i == 88) {
                    reader.skip(4);
                    return (reader.getUInt8() & 16) != 0 ? ImageType.WEBP_A : ImageType.WEBP;
                } else if (i != 76) {
                    return ImageType.WEBP;
                } else {
                    reader.skip(4);
                    return (reader.getUInt8() & 8) != 0 ? ImageType.WEBP_A : ImageType.WEBP;
                }
            }
        } catch (EndOfFileException unused) {
            return ImageType.UNKNOWN;
        }
    }

    private static boolean handles(int i) {
        return (i & EXIF_MAGIC_NUMBER) == EXIF_MAGIC_NUMBER || i == MOTOROLA_TIFF_MAGIC_NUMBER || i == INTEL_TIFF_MAGIC_NUMBER;
    }

    private boolean hasJpegExifPreamble(byte[] bArr, int i) {
        boolean z = bArr != null && i > JPEG_EXIF_SEGMENT_PREAMBLE_BYTES.length;
        if (z) {
            int i2 = 0;
            while (true) {
                byte[] bArr2 = JPEG_EXIF_SEGMENT_PREAMBLE_BYTES;
                if (i2 >= bArr2.length) {
                    break;
                } else if (bArr[i2] != bArr2[i2]) {
                    return false;
                } else {
                    i2++;
                }
            }
        }
        return z;
    }

    private int moveToExifSegmentAndGetLength(Reader reader) {
        String str;
        short uInt8;
        int uInt16;
        long j;
        long skip;
        do {
            short uInt82 = reader.getUInt8();
            str = TAG;
            if (uInt82 != 255) {
                if (Log.isLoggable(str, 3)) {
                    StringBuilder sb = new StringBuilder();
                    sb.append("Unknown segmentId=");
                    sb.append(uInt82);
                    Log.d(str, sb.toString());
                }
                return -1;
            }
            uInt8 = reader.getUInt8();
            if (uInt8 == 218) {
                return -1;
            }
            if (uInt8 == 217) {
                if (Log.isLoggable(str, 3)) {
                    Log.d(str, "Found MARKER_EOI in exif segment");
                }
                return -1;
            }
            uInt16 = reader.getUInt16() - 2;
            if (uInt8 == 225) {
                return uInt16;
            }
            j = (long) uInt16;
            skip = reader.skip(j);
        } while (skip == j);
        if (Log.isLoggable(str, 3)) {
            StringBuilder sb2 = new StringBuilder();
            sb2.append("Unable to skip enough data, type: ");
            sb2.append(uInt8);
            sb2.append(", wanted to skip: ");
            sb2.append(uInt16);
            sb2.append(", but actually skipped: ");
            sb2.append(skip);
            Log.d(str, sb2.toString());
        }
        return -1;
    }

    private static int parseExifSegment(RandomAccessReader randomAccessReader) {
        ByteOrder byteOrder;
        String str;
        StringBuilder sb;
        String str2;
        short int16 = randomAccessReader.getInt16(6);
        String str3 = TAG;
        if (int16 != INTEL_TIFF_MAGIC_NUMBER) {
            if (int16 != MOTOROLA_TIFF_MAGIC_NUMBER && Log.isLoggable(str3, 3)) {
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Unknown endianness = ");
                sb2.append(int16);
                Log.d(str3, sb2.toString());
            }
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else {
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        }
        randomAccessReader.order(byteOrder);
        int int32 = randomAccessReader.getInt32(10) + 6;
        short int162 = randomAccessReader.getInt16(int32);
        for (int i = 0; i < int162; i++) {
            int calcTagOffset = calcTagOffset(int32, i);
            short int163 = randomAccessReader.getInt16(calcTagOffset);
            if (int163 == 274) {
                short int164 = randomAccessReader.getInt16(calcTagOffset + 2);
                if (int164 >= 1 && int164 <= 12) {
                    int int322 = randomAccessReader.getInt32(calcTagOffset + 4);
                    if (int322 >= 0) {
                        String str4 = " tagType=";
                        if (Log.isLoggable(str3, 3)) {
                            StringBuilder sb3 = new StringBuilder();
                            sb3.append("Got tagIndex=");
                            sb3.append(i);
                            sb3.append(str4);
                            sb3.append(int163);
                            sb3.append(" formatCode=");
                            sb3.append(int164);
                            sb3.append(" componentCount=");
                            sb3.append(int322);
                            Log.d(str3, sb3.toString());
                        }
                        int i2 = int322 + BYTES_PER_FORMAT[int164];
                        if (i2 <= 4) {
                            int i3 = calcTagOffset + 8;
                            if (i3 < 0 || i3 > randomAccessReader.length()) {
                                if (Log.isLoggable(str3, 3)) {
                                    StringBuilder sb4 = new StringBuilder();
                                    sb4.append("Illegal tagValueOffset=");
                                    sb4.append(i3);
                                    sb4.append(str4);
                                    sb4.append(int163);
                                    str = sb4.toString();
                                    Log.d(str3, str);
                                }
                            } else if (i2 >= 0 && i2 + i3 <= randomAccessReader.length()) {
                                return randomAccessReader.getInt16(i3);
                            } else {
                                if (Log.isLoggable(str3, 3)) {
                                    sb = new StringBuilder();
                                    sb.append("Illegal number of bytes for TI tag data tagType=");
                                    sb.append(int163);
                                    str = sb.toString();
                                    Log.d(str3, str);
                                }
                            }
                        } else if (Log.isLoggable(str3, 3)) {
                            sb = new StringBuilder();
                            str2 = "Got byte count > 4, not orientation, continuing, formatCode=";
                        }
                    } else if (Log.isLoggable(str3, 3)) {
                        str = "Negative tiff component count";
                        Log.d(str3, str);
                    }
                } else if (Log.isLoggable(str3, 3)) {
                    sb = new StringBuilder();
                    str2 = "Got invalid format code = ";
                }
                sb.append(str2);
                sb.append(int164);
                str = sb.toString();
                Log.d(str3, str);
            }
        }
        return -1;
    }

    private int parseExifSegment(Reader reader, byte[] bArr, int i) {
        int read = reader.read(bArr, i);
        String str = TAG;
        if (read != i) {
            if (Log.isLoggable(str, 3)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Unable to read exif segment data, length: ");
                sb.append(i);
                sb.append(", actually read: ");
                sb.append(read);
                Log.d(str, sb.toString());
            }
            return -1;
        } else if (hasJpegExifPreamble(bArr, i)) {
            return parseExifSegment(new RandomAccessReader(bArr, i));
        } else {
            if (Log.isLoggable(str, 3)) {
                Log.d(str, "Missing jpeg exif preamble");
            }
            return -1;
        }
    }

    public int getOrientation(@NonNull InputStream inputStream, @NonNull ArrayPool arrayPool) {
        Preconditions.checkNotNull(inputStream);
        StreamReader streamReader = new StreamReader(inputStream);
        Preconditions.checkNotNull(arrayPool);
        return getOrientation((Reader) streamReader, arrayPool);
    }

    public int getOrientation(@NonNull ByteBuffer byteBuffer, @NonNull ArrayPool arrayPool) {
        Preconditions.checkNotNull(byteBuffer);
        ByteBufferReader byteBufferReader = new ByteBufferReader(byteBuffer);
        Preconditions.checkNotNull(arrayPool);
        return getOrientation((Reader) byteBufferReader, arrayPool);
    }

    @NonNull
    public ImageType getType(@NonNull InputStream inputStream) {
        Preconditions.checkNotNull(inputStream);
        return getType((Reader) new StreamReader(inputStream));
    }

    @NonNull
    public ImageType getType(@NonNull ByteBuffer byteBuffer) {
        Preconditions.checkNotNull(byteBuffer);
        return getType((Reader) new ByteBufferReader(byteBuffer));
    }
}
