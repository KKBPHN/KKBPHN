package com.android.gallery3d.exif;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.ArrayList;
import java.util.List;

class ExifModifier {
    public static final boolean DEBUG = false;
    public static final String TAG = "ExifModifier";
    private final ByteBuffer mByteBuffer;
    private final ExifInterface mInterface;
    private int mOffsetBase;
    private final List mTagOffsets = new ArrayList();
    private final ExifData mTagToModified;

    class TagOffset {
        final int mOffset;
        final ExifTag mTag;

        TagOffset(ExifTag exifTag, int i) {
            this.mTag = exifTag;
            this.mOffset = i;
        }
    }

    protected ExifModifier(ByteBuffer byteBuffer, ExifInterface exifInterface) {
        ByteBufferInputStream byteBufferInputStream;
        this.mByteBuffer = byteBuffer;
        this.mOffsetBase = byteBuffer.position();
        this.mInterface = exifInterface;
        try {
            byteBufferInputStream = new ByteBufferInputStream(byteBuffer);
            try {
                ExifParser parse = ExifParser.parse(byteBufferInputStream, this.mInterface);
                this.mTagToModified = new ExifData(parse.getByteOrder());
                this.mOffsetBase += parse.getTiffStartPosition();
                this.mByteBuffer.position(0);
                ExifInterface.closeSilently(byteBufferInputStream);
            } catch (Throwable th) {
                th = th;
                ExifInterface.closeSilently(byteBufferInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            byteBufferInputStream = null;
            ExifInterface.closeSilently(byteBufferInputStream);
            throw th;
        }
    }

    private void modify() {
        this.mByteBuffer.order(getByteOrder());
        for (TagOffset tagOffset : this.mTagOffsets) {
            writeTagValue(tagOffset.mTag, tagOffset.mOffset);
        }
    }

    private void writeTagValue(ExifTag exifTag, int i) {
        byte[] bArr;
        int i2;
        this.mByteBuffer.position(i + this.mOffsetBase);
        int i3 = 0;
        switch (exifTag.getDataType()) {
            case 1:
            case 7:
                bArr = new byte[exifTag.getComponentCount()];
                exifTag.getBytes(bArr);
                break;
            case 2:
                bArr = exifTag.getStringByte();
                if (bArr.length == exifTag.getComponentCount()) {
                    bArr[bArr.length - 1] = 0;
                    break;
                } else {
                    this.mByteBuffer.put(bArr);
                    this.mByteBuffer.put(0);
                    return;
                }
            case 3:
                int componentCount = exifTag.getComponentCount();
                while (i2 < componentCount) {
                    this.mByteBuffer.putShort((short) ((int) exifTag.getValueAt(i2)));
                    i2++;
                }
                return;
            case 4:
            case 9:
                int componentCount2 = exifTag.getComponentCount();
                while (i3 < componentCount2) {
                    this.mByteBuffer.putInt((int) exifTag.getValueAt(i3));
                    i3++;
                }
                return;
            case 5:
            case 10:
                int componentCount3 = exifTag.getComponentCount();
                while (i3 < componentCount3) {
                    Rational rational = exifTag.getRational(i3);
                    this.mByteBuffer.putInt((int) rational.getNumerator());
                    this.mByteBuffer.putInt((int) rational.getDenominator());
                    i3++;
                }
                return;
            default:
                return;
        }
        this.mByteBuffer.put(bArr);
    }

    /* access modifiers changed from: protected */
    /* JADX WARNING: Code restructure failed: missing block: B:44:0x00b7, code lost:
        r0 = r3.length;
        r2 = 0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:45:0x00b9, code lost:
        if (r2 >= r0) goto L_0x00cc;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:46:0x00bb, code lost:
        r4 = r3[r2];
     */
    /* JADX WARNING: Code restructure failed: missing block: B:47:0x00bd, code lost:
        if (r4 == null) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:49:0x00c3, code lost:
        if (r4.getTagCount() <= 0) goto L_0x00c9;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:50:0x00c5, code lost:
        com.android.gallery3d.exif.ExifInterface.closeSilently(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:51:0x00c8, code lost:
        return false;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:52:0x00c9, code lost:
        r2 = r2 + 1;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:54:?, code lost:
        modify();
     */
    /* JADX WARNING: Code restructure failed: missing block: B:55:0x00cf, code lost:
        com.android.gallery3d.exif.ExifInterface.closeSilently(r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:56:0x00d2, code lost:
        return true;
     */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public boolean commit() {
        ByteBufferInputStream byteBufferInputStream;
        IfdData ifdData = null;
        try {
            byteBufferInputStream = new ByteBufferInputStream(this.mByteBuffer);
            try {
                IfdData[] ifdDataArr = {this.mTagToModified.getIfdData(0), this.mTagToModified.getIfdData(1), this.mTagToModified.getIfdData(2), this.mTagToModified.getIfdData(3), this.mTagToModified.getIfdData(4)};
                int i = ifdDataArr[0] != null ? 1 : 0;
                if (ifdDataArr[1] != null) {
                    i |= 2;
                }
                if (ifdDataArr[2] != null) {
                    i |= 4;
                }
                if (ifdDataArr[4] != null) {
                    i |= 8;
                }
                if (ifdDataArr[3] != null) {
                    i |= 16;
                }
                ExifParser parse = ExifParser.parse(byteBufferInputStream, i, this.mInterface);
                while (true) {
                    int next = parse.next();
                    if (next == 5) {
                        break;
                    }
                    if (next == 0) {
                        ifdData = ifdDataArr[parse.getCurrentIfd()];
                        if (ifdData == null) {
                        }
                    } else if (next == 1) {
                        ExifTag tag = parse.getTag();
                        ExifTag tag2 = ifdData.getTag(tag.getTagId());
                        if (tag2 == null) {
                            continue;
                        } else if (tag2.getComponentCount() != tag.getComponentCount()) {
                            break;
                        } else if (tag2.getDataType() != tag.getDataType()) {
                            break;
                        } else {
                            this.mTagOffsets.add(new TagOffset(tag2, tag.getOffset()));
                            ifdData.removeTag(tag.getTagId());
                            if (ifdData.getTagCount() != 0) {
                            }
                        }
                    }
                    parse.skipRemainingTagsInCurrentIfd();
                }
                ExifInterface.closeSilently(byteBufferInputStream);
                return false;
            } catch (Throwable th) {
                th = th;
                ExifInterface.closeSilently(byteBufferInputStream);
                throw th;
            }
        } catch (Throwable th2) {
            th = th2;
            byteBufferInputStream = null;
            ExifInterface.closeSilently(byteBufferInputStream);
            throw th;
        }
    }

    /* access modifiers changed from: protected */
    public ByteOrder getByteOrder() {
        return this.mTagToModified.getByteOrder();
    }

    public void modifyTag(ExifTag exifTag) {
        this.mTagToModified.addTag(exifTag);
    }
}
