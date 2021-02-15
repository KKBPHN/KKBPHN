package com.android.gallery3d.exif;

import com.android.camera.log.Log;
import java.io.InputStream;

class ExifReader {
    private static final String TAG = "ExifReader";
    private final ExifInterface mInterface;

    ExifReader(ExifInterface exifInterface) {
        this.mInterface = exifInterface;
    }

    /* access modifiers changed from: protected */
    public ExifData read(InputStream inputStream) {
        ExifTag exifTag;
        String str;
        ExifParser parse = ExifParser.parse(inputStream, this.mInterface);
        ExifData exifData = new ExifData(parse.getByteOrder());
        while (true) {
            int next = parse.next();
            if (next == 5) {
                return exifData;
            }
            if (next != 0) {
                if (next == 1) {
                    exifTag = parse.getTag();
                    if (!exifTag.hasValue()) {
                        parse.registerForTagValue(exifTag);
                    }
                } else if (next != 2) {
                    String str2 = TAG;
                    if (next == 3) {
                        int compressedImageSize = parse.getCompressedImageSize();
                        if (compressedImageSize > 0) {
                            byte[] bArr = new byte[compressedImageSize];
                            if (bArr.length == parse.read(bArr)) {
                                exifData.setCompressedThumbnail(bArr);
                            } else {
                                str = "Failed to read the compressed thumbnail";
                            }
                        } else {
                            StringBuilder sb = new StringBuilder();
                            sb.append("compressedImageSize=");
                            sb.append(compressedImageSize);
                            Log.d(str2, sb.toString());
                        }
                    } else if (next == 4) {
                        byte[] bArr2 = new byte[parse.getStripSize()];
                        if (bArr2.length == parse.read(bArr2)) {
                            exifData.setStripBytes(parse.getStripIndex(), bArr2);
                        } else {
                            str = "Failed to read the strip bytes";
                        }
                    }
                    Log.w(str2, str);
                } else {
                    exifTag = parse.getTag();
                    if (exifTag.getDataType() == 7) {
                        parse.readFullTagValue(exifTag);
                    }
                }
                exifData.getIfdData(exifTag.getIfd()).setTag(exifTag);
            } else {
                exifData.addIfdData(new IfdData(parse.getCurrentIfd()));
            }
        }
    }
}
