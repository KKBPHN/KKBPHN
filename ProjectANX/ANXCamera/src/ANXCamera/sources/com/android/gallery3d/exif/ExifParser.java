package com.android.gallery3d.exif;

import com.android.camera.log.Log;
import com.android.gallery3d.exif.ExifInterface.ColorSpace;
import java.io.IOException;
import java.io.InputStream;
import java.nio.ByteOrder;
import java.nio.charset.Charset;
import java.util.Locale;
import java.util.Map.Entry;
import java.util.TreeMap;

class ExifParser {
    protected static final short BIG_ENDIAN_TAG = 19789;
    protected static final int DEFAULT_IFD0_OFFSET = 8;
    public static final int EVENT_COMPRESSED_IMAGE = 3;
    public static final int EVENT_END = 5;
    public static final int EVENT_NEW_TAG = 1;
    public static final int EVENT_START_OF_IFD = 0;
    public static final int EVENT_UNCOMPRESSED_STRIP = 4;
    public static final int EVENT_VALUE_OF_REGISTERED_TAG = 2;
    protected static final int EXIF_HEADER = 1165519206;
    protected static final short EXIF_HEADER_TAIL = 0;
    protected static final short LITTLE_ENDIAN_TAG = 18761;
    private static final boolean LOGV = false;
    protected static final int OFFSET_SIZE = 2;
    public static final int OPTION_IFD_0 = 1;
    public static final int OPTION_IFD_1 = 2;
    public static final int OPTION_IFD_EXIF = 4;
    public static final int OPTION_IFD_GPS = 8;
    public static final int OPTION_IFD_INTEROPERABILITY = 16;
    public static final int OPTION_THUMBNAIL = 32;
    private static final String TAG = "ExifParser";
    private static final short TAG_EXIF_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_EXIF_IFD);
    private static final short TAG_GPS_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_GPS_IFD);
    private static final short TAG_INTEROPERABILITY_IFD = ExifInterface.getTrueTagKey(ExifInterface.TAG_INTEROPERABILITY_IFD);
    private static final short TAG_JPEG_INTERCHANGE_FORMAT = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT);
    private static final short TAG_JPEG_INTERCHANGE_FORMAT_LENGTH = ExifInterface.getTrueTagKey(ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH);
    protected static final int TAG_SIZE = 12;
    private static final short TAG_STRIP_BYTE_COUNTS = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_BYTE_COUNTS);
    private static final short TAG_STRIP_OFFSETS = ExifInterface.getTrueTagKey(ExifInterface.TAG_STRIP_OFFSETS);
    protected static final short TIFF_HEADER_TAIL = 42;
    private static final Charset US_ASCII = Charset.forName("US-ASCII");
    private int mApp1End;
    private boolean mContainExifData = false;
    private final TreeMap mCorrespondingEvent = new TreeMap();
    private byte[] mDataAboveIfd0;
    private int mIfd0Position;
    private int mIfdStartOffset = 0;
    private int mIfdType;
    private ImageEvent mImageEvent;
    private final ExifInterface mInterface;
    private ExifTag mJpegSizeTag;
    private boolean mNeedToParseOffsetsInCurrentIfd;
    private int mNumOfTagInIfd = 0;
    private int mOffsetToApp1EndFromSOF = 0;
    private final int mOptions;
    private int mStripCount;
    private ExifTag mStripSizeTag;
    private ExifTag mTag;
    private int mTiffStartPosition;
    private final CountedDataInputStream mTiffStream;

    class ExifTagEvent {
        boolean isRequested;
        ExifTag tag;

        ExifTagEvent(ExifTag exifTag, boolean z) {
            this.tag = exifTag;
            this.isRequested = z;
        }
    }

    class IfdEvent {
        int ifd;
        boolean isRequested;

        IfdEvent(int i, boolean z) {
            this.ifd = i;
            this.isRequested = z;
        }
    }

    class ImageEvent {
        int stripIndex;
        int type;

        ImageEvent(int i) {
            this.stripIndex = 0;
            this.type = i;
        }

        ImageEvent(int i, int i2) {
            this.type = i;
            this.stripIndex = i2;
        }
    }

    private ExifParser(InputStream inputStream, int i, ExifInterface exifInterface) {
        if (inputStream != null) {
            this.mInterface = exifInterface;
            this.mContainExifData = seekTiffData(inputStream);
            this.mTiffStream = new CountedDataInputStream(inputStream);
            this.mOptions = i;
            if (this.mContainExifData) {
                parseTiffHeader();
                long readUnsignedInt = this.mTiffStream.readUnsignedInt();
                if (readUnsignedInt <= 2147483647L) {
                    int i2 = (int) readUnsignedInt;
                    this.mIfd0Position = i2;
                    this.mIfdType = 0;
                    if (isIfdRequested(0) || needToParseOffsetsInCurrentIfd()) {
                        registerIfd(0, readUnsignedInt);
                        if (readUnsignedInt != 8) {
                            this.mDataAboveIfd0 = new byte[(i2 - 8)];
                            read(this.mDataAboveIfd0);
                        }
                    }
                    return;
                }
                StringBuilder sb = new StringBuilder();
                sb.append("Invalid offset ");
                sb.append(readUnsignedInt);
                throw new ExifInvalidFormatException(sb.toString());
            }
            return;
        }
        throw new IOException("Null argument inputStream to ExifParser");
    }

    private boolean checkAllowed(int i, int i2) {
        int i3 = this.mInterface.getTagInfo().get(i2);
        if (i3 == 0) {
            return false;
        }
        return ExifInterface.isIfdAllowed(i3, i);
    }

    /* JADX WARNING: Code restructure failed: missing block: B:10:0x0028, code lost:
        if (isIfdRequested(3) == false) goto L_0x00eb;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:17:0x0044, code lost:
        if (isIfdRequested(4) != false) goto L_0x002a;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private void checkOffsetOrImageTag(ExifTag exifTag) {
        int i;
        if (exifTag.getComponentCount() != 0) {
            short tagId = exifTag.getTagId();
            int ifd = exifTag.getIfd();
            if (tagId == TAG_EXIF_IFD && checkAllowed(ifd, ExifInterface.TAG_EXIF_IFD)) {
                i = 2;
                if (!isIfdRequested(2)) {
                }
            } else if (tagId == TAG_GPS_IFD && checkAllowed(ifd, ExifInterface.TAG_GPS_IFD)) {
                i = 4;
            } else if (tagId == TAG_INTEROPERABILITY_IFD && checkAllowed(ifd, ExifInterface.TAG_INTEROPERABILITY_IFD)) {
                if (isIfdRequested(3)) {
                    registerIfd(3, exifTag.getValueAt(0));
                }
            } else if (tagId == TAG_JPEG_INTERCHANGE_FORMAT && checkAllowed(ifd, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT)) {
                if (isThumbnailRequested()) {
                    registerCompressedImage(exifTag.getValueAt(0));
                }
            } else if (tagId == TAG_JPEG_INTERCHANGE_FORMAT_LENGTH && checkAllowed(ifd, ExifInterface.TAG_JPEG_INTERCHANGE_FORMAT_LENGTH)) {
                if (isThumbnailRequested()) {
                    this.mJpegSizeTag = exifTag;
                }
            } else if (tagId != TAG_STRIP_OFFSETS || !checkAllowed(ifd, ExifInterface.TAG_STRIP_OFFSETS)) {
                if (tagId == TAG_STRIP_BYTE_COUNTS && checkAllowed(ifd, ExifInterface.TAG_STRIP_BYTE_COUNTS) && isThumbnailRequested() && exifTag.hasValue()) {
                    this.mStripSizeTag = exifTag;
                }
            } else {
                if (isThumbnailRequested()) {
                    if (exifTag.hasValue()) {
                        for (int i2 = 0; i2 < exifTag.getComponentCount(); i2++) {
                            short dataType = exifTag.getDataType();
                            registerUncompressedStrip(i2, exifTag.getValueAt(i2));
                        }
                    } else {
                        this.mCorrespondingEvent.put(Integer.valueOf(exifTag.getOffset()), new ExifTagEvent(exifTag, false));
                    }
                }
            }
            registerIfd(i, exifTag.getValueAt(0));
        }
    }

    private boolean isIfdRequested(int i) {
        boolean z;
        boolean z2;
        boolean z3;
        boolean z4 = false;
        if (i == 0) {
            if ((this.mOptions & 1) != 0) {
                z = true;
            }
            return z;
        } else if (i == 1) {
            if ((this.mOptions & 2) != 0) {
                z2 = true;
            }
            return z2;
        } else if (i == 2) {
            if ((this.mOptions & 4) != 0) {
                z3 = true;
            }
            return z3;
        } else if (i == 3) {
            if ((this.mOptions & 16) != 0) {
                z4 = true;
            }
            return z4;
        } else if (i != 4) {
            return false;
        } else {
            if ((this.mOptions & 8) != 0) {
                z4 = true;
            }
            return z4;
        }
    }

    private boolean isThumbnailRequested() {
        return (this.mOptions & 32) != 0;
    }

    private boolean needToParseOffsetsInCurrentIfd() {
        int i = this.mIfdType;
        boolean z = false;
        if (i == 0) {
            if (isIfdRequested(2) || isIfdRequested(4) || isIfdRequested(3) || isIfdRequested(1)) {
                z = true;
            }
            return z;
        } else if (i == 1) {
            return isThumbnailRequested();
        } else {
            if (i != 2) {
                return false;
            }
            return isIfdRequested(3);
        }
    }

    protected static ExifParser parse(InputStream inputStream, int i, ExifInterface exifInterface) {
        return new ExifParser(inputStream, i, exifInterface);
    }

    protected static ExifParser parse(InputStream inputStream, ExifInterface exifInterface) {
        return new ExifParser(inputStream, 63, exifInterface);
    }

    private void parseTiffHeader() {
        CountedDataInputStream countedDataInputStream;
        ByteOrder byteOrder;
        short readShort = this.mTiffStream.readShort();
        String str = "Invalid TIFF header";
        if (18761 == readShort) {
            countedDataInputStream = this.mTiffStream;
            byteOrder = ByteOrder.LITTLE_ENDIAN;
        } else if (19789 == readShort) {
            countedDataInputStream = this.mTiffStream;
            byteOrder = ByteOrder.BIG_ENDIAN;
        } else {
            throw new ExifInvalidFormatException(str);
        }
        countedDataInputStream.setByteOrder(byteOrder);
        if (this.mTiffStream.readShort() != 42) {
            throw new ExifInvalidFormatException(str);
        }
    }

    private ExifTag readTag() {
        int i;
        short readShort = this.mTiffStream.readShort();
        short readShort2 = this.mTiffStream.readShort();
        long readUnsignedInt = this.mTiffStream.readUnsignedInt();
        if (readUnsignedInt > 2147483647L) {
            throw new ExifInvalidFormatException("Number of component is larger then Integer.MAX_VALUE");
        } else if (!ExifTag.isValidType(readShort2)) {
            Log.w(TAG, String.format(Locale.ENGLISH, "Tag %04x: Invalid data type %d", new Object[]{Short.valueOf(readShort), Short.valueOf(readShort2)}));
            this.mTiffStream.skip(4);
            return null;
        } else {
            int i2 = (int) readUnsignedInt;
            ExifTag exifTag = new ExifTag(readShort, readShort2, i2, this.mIfdType, i2 != 0);
            int dataSize = exifTag.getDataSize();
            if (dataSize > 4) {
                long readUnsignedInt2 = this.mTiffStream.readUnsignedInt();
                if (readUnsignedInt2 > 2147483647L) {
                    throw new ExifInvalidFormatException("offset is larger then Integer.MAX_VALUE");
                } else if (readUnsignedInt2 >= ((long) this.mIfd0Position) || readShort2 != 7) {
                    i = (int) readUnsignedInt2;
                } else {
                    byte[] bArr = new byte[i2];
                    System.arraycopy(this.mDataAboveIfd0, ((int) readUnsignedInt2) - 8, bArr, 0, i2);
                    exifTag.setValue(bArr);
                    return exifTag;
                }
            } else {
                boolean hasDefinedCount = exifTag.hasDefinedCount();
                exifTag.setHasDefinedCount(false);
                readFullTagValue(exifTag);
                exifTag.setHasDefinedCount(hasDefinedCount);
                this.mTiffStream.skip((long) (4 - dataSize));
                i = this.mTiffStream.getReadByteCount() - 4;
            }
            exifTag.setOffset(i);
            return exifTag;
        }
    }

    private void registerCompressedImage(long j) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) j), new ImageEvent(3));
    }

    private void registerIfd(int i, long j) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) j), new IfdEvent(i, isIfdRequested(i)));
    }

    private void registerUncompressedStrip(int i, long j) {
        this.mCorrespondingEvent.put(Integer.valueOf((int) j), new ImageEvent(4, i));
    }

    /* JADX WARNING: Code restructure failed: missing block: B:19:0x0058, code lost:
        com.android.camera.log.Log.w(TAG, "Invalid JPEG format.");
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    private boolean seekTiffData(InputStream inputStream) {
        CountedDataInputStream countedDataInputStream = new CountedDataInputStream(inputStream);
        if (countedDataInputStream.readShort() == -40) {
            while (true) {
                short readShort = countedDataInputStream.readShort();
                if (readShort == -39 || JpegHeader.isSofMarker(readShort)) {
                    break;
                }
                int readUnsignedShort = countedDataInputStream.readUnsignedShort();
                if (readShort == -31 && readUnsignedShort >= 8) {
                    int readInt = countedDataInputStream.readInt();
                    short readShort2 = countedDataInputStream.readShort();
                    readUnsignedShort -= 6;
                    if (readInt == EXIF_HEADER && readShort2 == 0) {
                        this.mTiffStartPosition = countedDataInputStream.getReadByteCount();
                        this.mApp1End = readUnsignedShort;
                        this.mOffsetToApp1EndFromSOF = this.mTiffStartPosition + this.mApp1End;
                        return true;
                    }
                }
                if (readUnsignedShort < 2) {
                    break;
                }
                long j = (long) (readUnsignedShort - 2);
                if (j != countedDataInputStream.skip(j)) {
                    break;
                }
            }
            return false;
        }
        throw new ExifInvalidFormatException("Invalid JPEG format");
    }

    private void skipTo(int i) {
        this.mTiffStream.skipTo((long) i);
        while (!this.mCorrespondingEvent.isEmpty() && ((Integer) this.mCorrespondingEvent.firstKey()).intValue() < i) {
            this.mCorrespondingEvent.pollFirstEntry();
        }
    }

    /* access modifiers changed from: protected */
    public ByteOrder getByteOrder() {
        return this.mTiffStream.getByteOrder();
    }

    /* access modifiers changed from: protected */
    public int getCompressedImageSize() {
        ExifTag exifTag = this.mJpegSizeTag;
        if (exifTag == null) {
            return 0;
        }
        return (int) exifTag.getValueAt(0);
    }

    /* access modifiers changed from: protected */
    public int getCurrentIfd() {
        return this.mIfdType;
    }

    /* access modifiers changed from: protected */
    public int getOffsetToExifEndFromSOF() {
        return this.mOffsetToApp1EndFromSOF;
    }

    /* access modifiers changed from: protected */
    public int getStripCount() {
        return this.mStripCount;
    }

    /* access modifiers changed from: protected */
    public int getStripIndex() {
        return this.mImageEvent.stripIndex;
    }

    /* access modifiers changed from: protected */
    public int getStripSize() {
        ExifTag exifTag = this.mStripSizeTag;
        if (exifTag == null) {
            return 0;
        }
        return (int) exifTag.getValueAt(0);
    }

    /* access modifiers changed from: protected */
    public ExifTag getTag() {
        return this.mTag;
    }

    /* access modifiers changed from: protected */
    public int getTagCountInCurrentIfd() {
        return this.mNumOfTagInIfd;
    }

    /* access modifiers changed from: protected */
    public int getTiffStartPosition() {
        return this.mTiffStartPosition;
    }

    /* access modifiers changed from: protected */
    public int next() {
        String sb;
        if (!this.mContainExifData) {
            return 5;
        }
        int readByteCount = this.mTiffStream.getReadByteCount();
        int i = this.mIfdStartOffset + 2 + (this.mNumOfTagInIfd * 12);
        if (readByteCount < i) {
            this.mTag = readTag();
            ExifTag exifTag = this.mTag;
            if (exifTag == null) {
                return next();
            }
            if (this.mNeedToParseOffsetsInCurrentIfd) {
                checkOffsetOrImageTag(exifTag);
            }
            return 1;
        }
        String str = TAG;
        if (readByteCount == i) {
            if (this.mIfdType == 0) {
                long readUnsignedLong = readUnsignedLong();
                if ((isIfdRequested(1) || isThumbnailRequested()) && readUnsignedLong != 0) {
                    registerIfd(1, readUnsignedLong);
                }
            } else {
                int intValue = this.mCorrespondingEvent.size() > 0 ? ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount() : 4;
                if (intValue < 4) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Invalid size of link to next IFD: ");
                    sb2.append(intValue);
                    sb = sb2.toString();
                } else {
                    long readUnsignedLong2 = readUnsignedLong();
                    if (readUnsignedLong2 != 0) {
                        StringBuilder sb3 = new StringBuilder();
                        sb3.append("Invalid link to next IFD: ");
                        sb3.append(readUnsignedLong2);
                        sb = sb3.toString();
                    }
                }
                Log.w(str, sb);
            }
        }
        while (this.mCorrespondingEvent.size() != 0) {
            Entry pollFirstEntry = this.mCorrespondingEvent.pollFirstEntry();
            Object value = pollFirstEntry.getValue();
            try {
                skipTo(((Integer) pollFirstEntry.getKey()).intValue());
            } catch (IOException unused) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append("Failed to skip to data at: ");
                sb4.append(pollFirstEntry.getKey());
                sb4.append(" for ");
                sb4.append(value.getClass().getName());
                sb4.append(", the file may be broken.");
                sb = sb4.toString();
            }
            if (value instanceof IfdEvent) {
                IfdEvent ifdEvent = (IfdEvent) value;
                this.mIfdType = ifdEvent.ifd;
                this.mNumOfTagInIfd = this.mTiffStream.readUnsignedShort();
                this.mIfdStartOffset = ((Integer) pollFirstEntry.getKey()).intValue();
                if ((this.mNumOfTagInIfd * 12) + this.mIfdStartOffset + 2 > this.mApp1End) {
                    StringBuilder sb5 = new StringBuilder();
                    sb5.append("Invalid size of IFD ");
                    sb5.append(this.mIfdType);
                    Log.w(str, sb5.toString());
                    return 5;
                }
                this.mNeedToParseOffsetsInCurrentIfd = needToParseOffsetsInCurrentIfd();
                if (ifdEvent.isRequested) {
                    return 0;
                }
                skipRemainingTagsInCurrentIfd();
            } else if (value instanceof ImageEvent) {
                this.mImageEvent = (ImageEvent) value;
                return this.mImageEvent.type;
            } else {
                ExifTagEvent exifTagEvent = (ExifTagEvent) value;
                this.mTag = exifTagEvent.tag;
                if (this.mTag.getDataType() != 7) {
                    readFullTagValue(this.mTag);
                    checkOffsetOrImageTag(this.mTag);
                }
                if (exifTagEvent.isRequested) {
                    return 2;
                }
            }
        }
        return 5;
    }

    /* access modifiers changed from: protected */
    public int read(byte[] bArr) {
        return this.mTiffStream.read(bArr);
    }

    /* access modifiers changed from: protected */
    public int read(byte[] bArr, int i, int i2) {
        return this.mTiffStream.read(bArr, i, i2);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:32:0x0137, code lost:
        r6.setValue(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:0x0163, code lost:
        r6.setValue(r0);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:?, code lost:
        return;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:?, code lost:
        return;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public void readFullTagValue(ExifTag exifTag) {
        int[] iArr;
        int i;
        int i2;
        Rational[] rationalArr;
        int i3;
        StringBuilder sb;
        short dataType = exifTag.getDataType();
        if (dataType == 2 || dataType == 7 || dataType == 1) {
            int componentCount = exifTag.getComponentCount();
            if (this.mCorrespondingEvent.size() > 0 && ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() < this.mTiffStream.getReadByteCount() + componentCount) {
                Object value = this.mCorrespondingEvent.firstEntry().getValue();
                boolean z = value instanceof ImageEvent;
                String str = TAG;
                if (z) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append("Thumbnail overlaps value for tag: \n");
                    sb2.append(exifTag.toString());
                    Log.w(str, sb2.toString());
                    Entry pollFirstEntry = this.mCorrespondingEvent.pollFirstEntry();
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append("Invalid thumbnail offset: ");
                    sb3.append(pollFirstEntry.getKey());
                    Log.w(str, sb3.toString());
                } else {
                    String str2 = " overlaps value for tag: \n";
                    if (value instanceof IfdEvent) {
                        sb = new StringBuilder();
                        sb.append("Ifd ");
                        sb.append(((IfdEvent) value).ifd);
                    } else {
                        if (value instanceof ExifTagEvent) {
                            sb = new StringBuilder();
                            sb.append("Tag value for tag: \n");
                            sb.append(((ExifTagEvent) value).tag.toString());
                        }
                        int intValue = ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount();
                        StringBuilder sb4 = new StringBuilder();
                        sb4.append("Invalid size of tag: \n");
                        sb4.append(exifTag.toString());
                        sb4.append(" setting count to: ");
                        sb4.append(intValue);
                        Log.w(str, sb4.toString());
                        exifTag.forceSetComponentCount(intValue);
                    }
                    sb.append(str2);
                    sb.append(exifTag.toString());
                    Log.w(str, sb.toString());
                    int intValue2 = ((Integer) this.mCorrespondingEvent.firstEntry().getKey()).intValue() - this.mTiffStream.getReadByteCount();
                    StringBuilder sb42 = new StringBuilder();
                    sb42.append("Invalid size of tag: \n");
                    sb42.append(exifTag.toString());
                    sb42.append(" setting count to: ");
                    sb42.append(intValue2);
                    Log.w(str, sb42.toString());
                    exifTag.forceSetComponentCount(intValue2);
                }
            }
        }
        int i4 = 0;
        switch (exifTag.getDataType()) {
            case 1:
            case 7:
                byte[] bArr = new byte[exifTag.getComponentCount()];
                read(bArr);
                exifTag.setValue(bArr);
                return;
            case 2:
                exifTag.setValue(readString(exifTag.getComponentCount()));
                return;
            case 3:
                iArr = new int[exifTag.getComponentCount()];
                int length = iArr.length;
                while (i < length) {
                    iArr[i] = readUnsignedShort();
                    i++;
                }
                break;
            case 4:
                long[] jArr = new long[exifTag.getComponentCount()];
                int length2 = jArr.length;
                while (i2 < length2) {
                    jArr[i2] = readUnsignedLong();
                    i2++;
                }
                exifTag.setValue(jArr);
                return;
            case 5:
                rationalArr = new Rational[exifTag.getComponentCount()];
                int length3 = rationalArr.length;
                while (i3 < length3) {
                    rationalArr[i3] = readUnsignedRational();
                    i3++;
                }
                break;
            case 9:
                iArr = new int[exifTag.getComponentCount()];
                int length4 = iArr.length;
                while (i4 < length4) {
                    iArr[i4] = readLong();
                    i4++;
                }
                break;
            case 10:
                rationalArr = new Rational[exifTag.getComponentCount()];
                int length5 = rationalArr.length;
                while (i4 < length5) {
                    rationalArr[i4] = readRational();
                    i4++;
                }
                break;
            default:
                return;
        }
    }

    /* access modifiers changed from: protected */
    public int readLong() {
        return this.mTiffStream.readInt();
    }

    /* access modifiers changed from: protected */
    public Rational readRational() {
        return new Rational((long) readLong(), (long) readLong());
    }

    /* access modifiers changed from: protected */
    public String readString(int i) {
        return readString(i, US_ASCII);
    }

    /* access modifiers changed from: protected */
    public String readString(int i, Charset charset) {
        return i > 0 ? this.mTiffStream.readString(i, charset) : "";
    }

    /* access modifiers changed from: protected */
    public long readUnsignedLong() {
        return ((long) readLong()) & 4294967295L;
    }

    /* access modifiers changed from: protected */
    public Rational readUnsignedRational() {
        return new Rational(readUnsignedLong(), readUnsignedLong());
    }

    /* access modifiers changed from: protected */
    public int readUnsignedShort() {
        return this.mTiffStream.readShort() & ColorSpace.UNCALIBRATED;
    }

    /* access modifiers changed from: protected */
    public void registerForTagValue(ExifTag exifTag) {
        if (exifTag.getOffset() >= this.mTiffStream.getReadByteCount()) {
            this.mCorrespondingEvent.put(Integer.valueOf(exifTag.getOffset()), new ExifTagEvent(exifTag, true));
        }
    }

    /* access modifiers changed from: protected */
    public void skipRemainingTagsInCurrentIfd() {
        int i = this.mIfdStartOffset + 2 + (this.mNumOfTagInIfd * 12);
        int readByteCount = this.mTiffStream.getReadByteCount();
        if (readByteCount <= i) {
            if (this.mNeedToParseOffsetsInCurrentIfd) {
                while (readByteCount < i) {
                    this.mTag = readTag();
                    readByteCount += 12;
                    ExifTag exifTag = this.mTag;
                    if (exifTag != null) {
                        checkOffsetOrImageTag(exifTag);
                    }
                }
            } else {
                skipTo(i);
            }
            long readUnsignedLong = readUnsignedLong();
            if (this.mIfdType == 0 && ((isIfdRequested(1) || isThumbnailRequested()) && readUnsignedLong > 0)) {
                registerIfd(1, readUnsignedLong);
            }
        }
    }
}
