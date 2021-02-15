package com.android.camera2;

import O00000Oo.O00000oO.O000000o.C0122O00000o;
import O00000Oo.O00000oO.O000000o.C0124O00000oO;
import O00000Oo.O00000oO.O000000o.O0000Oo0;
import android.graphics.Point;
import android.util.Xml;
import com.adobe.xmp.XMPException;
import com.adobe.xmp.XMPMeta;
import com.android.camera.Util;
import com.android.camera.XmpHelper;
import com.android.camera.log.Log;
import com.android.gallery3d.exif.ExifInterface;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.io.StringWriter;
import miui.telephony.phonenumber.TelocationProvider.Contract.Params;
import org.xmlpull.v1.XmlSerializer;
import tv.danmaku.ijk.media.player.IjkMediaPlayer.OnNativeInvokeListener;

public class PortraitDepthMap {
    private static final int BLUR_LEVEL_START_ADDR = 16;
    private static final int DAPTH_MAP_START_ADDR = 152;
    private static final int DATA_LENGTH_4 = 4;
    private static final int DATA_LENGTH_START_ADDR = 148;
    private static final int HEADER_LENGTH_START_ADDR = 4;
    private static final int HEADER_START_ADDR = 0;
    private static final int POINT_X_START_ADDR = 8;
    private static final int POINT_Y_START_ADDR = 12;
    private static final String TAG = "PortraitDepthMap";
    @Deprecated
    public static final int TAG_DEPTH_MAP_BLUR_LEVEL = ExifInterface.defineTag(2, -30575);
    @Deprecated
    public static final int TAG_DEPTH_MAP_FOCUS_POINT = ExifInterface.defineTag(2, -30576);
    private byte[] mDepthMapHeader;
    private byte[] mDepthMapOriginalData;

    private static /* synthetic */ void $closeResource(Throwable th, AutoCloseable autoCloseable) {
        if (th != null) {
            try {
                autoCloseable.close();
            } catch (Throwable th2) {
                th.addSuppressed(th2);
            }
        } else {
            autoCloseable.close();
        }
    }

    public PortraitDepthMap(byte[] bArr) {
        if (bArr != null) {
            int headerTag = getHeaderTag(bArr);
            if (headerTag == 128) {
                this.mDepthMapOriginalData = bArr;
                this.mDepthMapHeader = getDepthMapHeader();
                return;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal depth format! 0x80 != ");
            sb.append(headerTag);
            throw new IllegalArgumentException(sb.toString());
        }
        throw new IllegalArgumentException("Null depth data!");
    }

    private static byte[] getBytes(byte[] bArr, int i, int i2) {
        if (i2 <= 0 || i < 0 || i2 > bArr.length - i) {
            StringBuilder sb = new StringBuilder();
            sb.append("WRONG ARGUMENT: from =");
            sb.append(i);
            sb.append(", length = ");
            sb.append(i2);
            throw new IllegalArgumentException(sb.toString());
        }
        byte[] bArr2 = new byte[i2];
        System.arraycopy(bArr, i, bArr2, 0, i2);
        return bArr2;
    }

    private static int getHeaderTag(byte[] bArr) {
        return getInteger(getBytes(bArr, 0, 4));
    }

    private static int getInteger(byte[] bArr) {
        if (bArr.length == 4) {
            int i = 0;
            for (int i2 = 0; i2 < 4; i2++) {
                i += (bArr[i2] & -1) << (i2 * 8);
            }
            return i;
        }
        throw new IllegalArgumentException("bytes can not covert to a integer value!");
    }

    public static boolean isDepthMapData(byte[] bArr) {
        boolean z = bArr != null && bArr.length > 4 && getHeaderTag(bArr) == 128;
        if (!z) {
            Log.e(TAG, "Illegal depthmap format");
        }
        return z;
    }

    public int getBlurLevel() {
        return getInteger(getBytes(this.mDepthMapHeader, 16, 4));
    }

    public byte[] getDepthMapData() {
        return getBytes(this.mDepthMapOriginalData, 152, getDepthMapLength());
    }

    public byte[] getDepthMapHeader() {
        return getBytes(this.mDepthMapOriginalData, 0, getInteger(getBytes(this.mDepthMapOriginalData, 4, 4)));
    }

    public int getDepthMapLength() {
        return getInteger(getBytes(this.mDepthMapHeader, 148, 4));
    }

    public Point getFocusPoint() {
        return new Point(getInteger(getBytes(this.mDepthMapHeader, 8, 4)), getInteger(getBytes(this.mDepthMapHeader, 12, 4)));
    }

    public int getVendor() {
        return 1;
    }

    /* JADX WARNING: Code restructure failed: missing block: B:189:0x06a0, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:190:0x06a1, code lost:
        r4 = r0;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:192:?, code lost:
        $closeResource(r3, r1);
     */
    /* JADX WARNING: Code restructure failed: missing block: B:193:0x06a5, code lost:
        throw r4;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:37:0x019f, code lost:
        r0 = move-exception;
     */
    /* JADX WARNING: Code restructure failed: missing block: B:40:?, code lost:
        throw r14;
     */
    /* JADX WARNING: Exception block dominator not found, dom blocks: [B:170:0x0688, B:187:0x069f] */
    /* JADX WARNING: Removed duplicated region for block: B:123:0x05ef  */
    /* JADX WARNING: Removed duplicated region for block: B:126:0x05fc A[SYNTHETIC, Splitter:B:126:0x05fc] */
    /* JADX WARNING: Removed duplicated region for block: B:201:0x06b4  */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public byte[] writePortraitExif(PortraitDepthMapExif portraitDepthMapExif) {
        byte[] bArr;
        byte[] bArr2;
        String str;
        String str2;
        File file;
        File file2;
        long j;
        long j2;
        int i;
        int i2;
        int i3;
        int i4;
        long j3;
        int i5;
        int i6;
        byte[] bArr3;
        File file3;
        String str3;
        byte[] bArr4;
        byte[] bArr5;
        Throwable th;
        byte[] bArr6;
        Throwable th2;
        StringWriter stringWriter;
        Throwable th3;
        int i7;
        Point focusPoint = getFocusPoint();
        int blurLevel = getBlurLevel();
        int portraitLightingVersioin = portraitDepthMapExif.getPortraitLightingVersioin();
        int cameraPreferredMode = portraitDepthMapExif.getCameraPreferredMode();
        boolean isFrontCamera = portraitDepthMapExif.getPictureInfo().isFrontCamera();
        int O0ooOO0 = portraitDepthMapExif.isSupportZeroDegreeOrientationImage() ? 2 : C0122O00000o.instance().O0ooOO0();
        int i8 = 10;
        int i9 = 5;
        if (O0ooOO0 <= 0) {
            i9 = -1;
            i8 = -1;
        } else if (isFrontCamera) {
            i8 = (!portraitDepthMapExif.getPictureInfo().isAiEnabled() || portraitDepthMapExif.getPictureInfo().getAiType() != 10) ? 40 : 70;
        } else if (portraitDepthMapExif.getPictureInfo().isAiEnabled() && portraitDepthMapExif.getPictureInfo().getAiType() == 10) {
            i8 = 30;
        }
        int vendor2 = O0ooOO0 > 1 ? getVendor() : 1;
        String str4 = TAG;
        StringBuilder sb = new StringBuilder();
        sb.append("writePortraitExif: focusPoint: ");
        sb.append(focusPoint);
        Log.d(str4, sb.toString());
        String str5 = TAG;
        StringBuilder sb2 = new StringBuilder();
        sb2.append("writePortraitExif: blurLevel: ");
        sb2.append(blurLevel);
        Log.d(str5, sb2.toString());
        String str6 = TAG;
        StringBuilder sb3 = new StringBuilder();
        sb3.append("writePortraitExif: shineThreshold: ");
        sb3.append(i9);
        Log.d(str6, sb3.toString());
        String str7 = TAG;
        StringBuilder sb4 = new StringBuilder();
        sb4.append("writePortraitExif: shineLevel: ");
        sb4.append(i8);
        Log.d(str7, sb4.toString());
        String str8 = TAG;
        StringBuilder sb5 = new StringBuilder();
        sb5.append("writePortraitExif: lightingPattern: ");
        sb5.append(portraitDepthMapExif.getLightingPattern());
        Log.d(str8, sb5.toString());
        String str9 = TAG;
        StringBuilder sb6 = new StringBuilder();
        sb6.append("writePortraitExif: isCinematicAspectRatio: ");
        sb6.append(portraitDepthMapExif.isCinematicAspectRatio());
        Log.d(str9, sb6.toString());
        String str10 = TAG;
        StringBuilder sb7 = new StringBuilder();
        sb7.append("writePortraitExif: rotation: ");
        sb7.append(portraitDepthMapExif.getRotation());
        Log.d(str10, sb7.toString());
        String str11 = TAG;
        StringBuilder sb8 = new StringBuilder();
        sb8.append("writePortraitExif: vendor: ");
        sb8.append(vendor2);
        Log.d(str11, sb8.toString());
        String str12 = TAG;
        StringBuilder sb9 = new StringBuilder();
        sb9.append("writePortraitExif: portraitLightingVersion: ");
        sb9.append(portraitLightingVersioin);
        Log.d(str12, sb9.toString());
        String str13 = TAG;
        StringBuilder sb10 = new StringBuilder();
        sb10.append("writePortraitExif: cameraPreferredMode: ");
        sb10.append(cameraPreferredMode);
        Log.d(str13, sb10.toString());
        th = 0;
        try {
            ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
            try {
                ExifInterface exifInterface = new ExifInterface();
                exifInterface.readExif(portraitDepthMapExif.getJpeg());
                exifInterface.addXiaomiDepthmapVersion(O0ooOO0);
                exifInterface.addDepthMapBlurLevel(blurLevel);
                exifInterface.addPortraitLighting(portraitDepthMapExif.getLightingPattern());
                if (portraitDepthMapExif.isBokehFrontCamera()) {
                    if (portraitDepthMapExif.isFrontMirror()) {
                        i7 = 1;
                    }
                    exifInterface.addFrontMirror(i7);
                }
                exifInterface.writeExif(portraitDepthMapExif.getJpeg(), (OutputStream) byteArrayOutputStream);
                bArr = byteArrayOutputStream.toByteArray();
                try {
                    $closeResource(null, byteArrayOutputStream);
                } catch (IOException unused) {
                }
            } finally {
                $closeResource(th3, byteArrayOutputStream);
            }
        } catch (IOException unused2) {
            bArr = null;
            Log.e(TAG, "writePortraitExif(): Failed to write depthmap associated exif metadata");
            bArr2 = bArr;
            if (bArr2 != null) {
            }
            str = TAG;
            str2 = "writePortraitExif(): #1: return original jpeg";
            Log.e(str, str2);
            return portraitDepthMapExif.getJpeg();
        }
        bArr2 = bArr;
        if (bArr2 != null || (bArr2.length <= portraitDepthMapExif.getJpeg().length && (!C0124O00000oO.isMTKPlatform() || !C0122O00000o.instance().OOo0oOO()))) {
            str = TAG;
            str2 = "writePortraitExif(): #1: return original jpeg";
        } else {
            StringBuilder sb11 = new StringBuilder();
            sb11.append("sdcard/DCIM/Camera/evZeroMainImage");
            sb11.append(portraitDepthMapExif.getTimeStamp());
            String str14 = ".yuv";
            sb11.append(str14);
            String sb12 = sb11.toString();
            StringBuilder sb13 = new StringBuilder();
            sb13.append("sdcard/DCIM/Camera/evZeroSubImage");
            byte[] bArr7 = bArr2;
            sb13.append(portraitDepthMapExif.getTimeStamp());
            sb13.append(str14);
            String sb14 = sb13.toString();
            StringBuilder sb15 = new StringBuilder();
            sb15.append("sdcard/DCIM/Camera/evMinusMainImage");
            int i10 = vendor2;
            sb15.append(portraitDepthMapExif.getTimeStamp());
            sb15.append(str14);
            String sb16 = sb15.toString();
            File file4 = new File(sb12);
            File file5 = new File(sb14);
            if (!file4.exists() || !file5.exists()) {
                file2 = file5;
                file = file4;
                j2 = 0;
                j = 0;
                i4 = 0;
                i3 = 0;
                i2 = 0;
                i = 0;
            } else {
                i3 = Util.getHeader2Int(file4, th);
                i4 = Util.getHeader2Int(file4, 4);
                j2 = file4.length() - 8;
                i = Util.getHeader2Int(file5, 0);
                i2 = Util.getHeader2Int(file5, 4);
                j = file5.length() - 8;
                String str15 = TAG;
                file2 = file5;
                StringBuilder sb17 = new StringBuilder();
                file = file4;
                sb17.append("main width = ");
                sb17.append(i3);
                sb17.append(", main height = ");
                sb17.append(i4);
                sb17.append(", sub width =");
                sb17.append(i);
                sb17.append(", sub height = ");
                sb17.append(i2);
                Log.d(str15, sb17.toString());
            }
            File file6 = new File(sb16);
            if (file6.exists()) {
                i6 = Util.getHeader2Int(file6, 0);
                i5 = Util.getHeader2Int(file6, 4);
                j3 = file6.length() - 8;
            } else {
                j3 = 0;
                i6 = 0;
                i5 = 0;
            }
            try {
                XmlSerializer newSerializer = Xml.newSerializer();
                file3 = file6;
                try {
                    StringWriter stringWriter2 = new StringWriter();
                    newSerializer.setOutput(stringWriter2);
                    bArr3 = bArr7;
                    stringWriter = stringWriter2;
                } catch (IOException unused3) {
                    bArr3 = bArr7;
                    Log.e(TAG, "writePortraitExif(): Failed to generate depthmap associated xmp metadata");
                    str3 = null;
                    if (str3 != null) {
                    }
                    Log.e(str, str2);
                    return portraitDepthMapExif.getJpeg();
                }
                try {
                    newSerializer.startDocument("UTF-8", Boolean.valueOf(true));
                    newSerializer.startTag(null, "depthmap");
                    newSerializer.attribute(null, "version", String.valueOf(O0ooOO0));
                    StringBuilder sb18 = new StringBuilder();
                    sb18.append(focusPoint.x);
                    sb18.append(",");
                    sb18.append(focusPoint.y);
                    newSerializer.attribute(null, "focuspoint", sb18.toString());
                    newSerializer.attribute(null, "blurlevel", String.valueOf(blurLevel));
                    newSerializer.attribute(null, "shinethreshold", String.valueOf(i9));
                    newSerializer.attribute(null, "shinelevel", String.valueOf(i8));
                    newSerializer.attribute(null, "rawlength", String.valueOf(portraitDepthMapExif.getRawLength()));
                    newSerializer.attribute(null, "depthlength", String.valueOf(portraitDepthMapExif.getDepthLength()));
                    newSerializer.attribute(null, "mimovie", String.valueOf(portraitDepthMapExif.isCinematicAspectRatio()));
                    newSerializer.attribute(null, "depthOrientation", String.valueOf(portraitDepthMapExif.getRotation()));
                    newSerializer.attribute(null, O0000Oo0.VENDOR, String.valueOf(i10));
                    newSerializer.attribute(null, "portraitLightingVersion", String.valueOf(portraitLightingVersioin));
                    newSerializer.attribute(null, "cameraPreferredMode", String.valueOf(cameraPreferredMode));
                    newSerializer.endTag(null, "depthmap");
                    long j4 = j2 + j + j3;
                    int i11 = (j4 > 0 ? 1 : (j4 == 0 ? 0 : -1));
                    String str16 = "height";
                    String str17 = "width";
                    String str18 = Params.LENGTH;
                    String str19 = OnNativeInvokeListener.ARG_OFFSET;
                    if (i11 != 0) {
                        newSerializer.startTag(null, "mainyuv");
                        newSerializer.attribute(null, str19, String.valueOf(((long) (portraitDepthMapExif.getRawLength() + portraitDepthMapExif.getDepthLength())) + j4));
                        newSerializer.attribute(null, str18, String.valueOf(j2));
                        newSerializer.attribute(null, str17, String.valueOf(i3));
                        newSerializer.attribute(null, str16, String.valueOf(i4));
                        newSerializer.endTag(null, "mainyuv");
                        newSerializer.startTag(null, "subyuv");
                        newSerializer.attribute(null, str19, String.valueOf(((long) (portraitDepthMapExif.getRawLength() + portraitDepthMapExif.getDepthLength())) + j + j3));
                        newSerializer.attribute(null, str18, String.valueOf(j));
                        newSerializer.attribute(null, str17, String.valueOf(i));
                        newSerializer.attribute(null, str16, String.valueOf(i2));
                        newSerializer.endTag(null, "subyuv");
                    }
                    if (j3 != 0) {
                        newSerializer.startTag(null, "evminusyuv");
                        newSerializer.attribute(null, str19, String.valueOf(((long) (portraitDepthMapExif.getRawLength() + portraitDepthMapExif.getDepthLength())) + j3));
                        newSerializer.attribute(null, str18, String.valueOf(j3));
                        newSerializer.attribute(null, str17, String.valueOf(i6));
                        newSerializer.attribute(null, str16, String.valueOf(i5));
                        newSerializer.endTag(null, "evminusyuv");
                    }
                    if (portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks() != null && portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks().length > 0 && portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks() != null && portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks().length >= 4) {
                        newSerializer.startTag(null, "subimage");
                        newSerializer.attribute(null, str19, String.valueOf(((long) (portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks().length + (portraitDepthMapExif.getDulWaterMark() != null ? portraitDepthMapExif.getDulWaterMark().length : 0) + (portraitDepthMapExif.getTimeWaterMark() != null ? portraitDepthMapExif.getTimeWaterMark().length : 0))) + j4 + ((long) portraitDepthMapExif.getRawLength()) + ((long) portraitDepthMapExif.getDepthLength())));
                        newSerializer.attribute(null, str18, String.valueOf(portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks().length));
                        newSerializer.attribute(null, "paddingx", String.valueOf(portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks()[0]));
                        newSerializer.attribute(null, "paddingy", String.valueOf(portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks()[1]));
                        newSerializer.attribute(null, str17, String.valueOf(portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks()[2]));
                        newSerializer.attribute(null, str16, String.valueOf(portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks()[3]));
                        newSerializer.attribute(null, "rotation", String.valueOf(portraitDepthMapExif.getRotation()));
                        newSerializer.endTag(null, "subimage");
                    }
                    if (portraitDepthMapExif.getDulWaterMark() != null && portraitDepthMapExif.getDulWaterMark().length > 0) {
                        newSerializer.startTag(null, "lenswatermark");
                        newSerializer.attribute(null, str19, String.valueOf(((long) (portraitDepthMapExif.getDulWaterMark().length + (portraitDepthMapExif.getTimeWaterMark() != null ? portraitDepthMapExif.getTimeWaterMark().length : 0))) + j4 + ((long) portraitDepthMapExif.getRawLength()) + ((long) portraitDepthMapExif.getDepthLength())));
                        newSerializer.attribute(null, str18, String.valueOf(portraitDepthMapExif.getDulWaterMark().length));
                        newSerializer.attribute(null, str17, String.valueOf(portraitDepthMapExif.getDulCameraWaterMarkLocation()[0]));
                        newSerializer.attribute(null, str16, String.valueOf(portraitDepthMapExif.getDulCameraWaterMarkLocation()[1]));
                        newSerializer.attribute(null, "paddingx", String.valueOf(portraitDepthMapExif.getDulCameraWaterMarkLocation()[2]));
                        newSerializer.attribute(null, "paddingy", String.valueOf(portraitDepthMapExif.getDulCameraWaterMarkLocation()[3]));
                        newSerializer.endTag(null, "lenswatermark");
                    }
                    if (portraitDepthMapExif.getTimeWaterMark() != null && portraitDepthMapExif.getTimeWaterMark().length > 0) {
                        newSerializer.startTag(null, "timewatermark");
                        newSerializer.attribute(null, str19, String.valueOf(((long) portraitDepthMapExif.getTimeWaterMark().length) + j4 + ((long) portraitDepthMapExif.getRawLength()) + ((long) portraitDepthMapExif.getDepthLength())));
                        newSerializer.attribute(null, str18, String.valueOf(portraitDepthMapExif.getTimeWaterMark().length));
                        newSerializer.attribute(null, str17, String.valueOf(portraitDepthMapExif.getTimeWaterMarkLocation()[0]));
                        newSerializer.attribute(null, str16, String.valueOf(portraitDepthMapExif.getTimeWaterMarkLocation()[1]));
                        newSerializer.attribute(null, "paddingx", String.valueOf(portraitDepthMapExif.getTimeWaterMarkLocation()[2]));
                        newSerializer.attribute(null, "paddingy", String.valueOf(portraitDepthMapExif.getTimeWaterMarkLocation()[3]));
                        newSerializer.endTag(null, "timewatermark");
                    }
                    newSerializer.endDocument();
                    str3 = stringWriter.toString();
                } catch (IOException unused4) {
                    Log.e(TAG, "writePortraitExif(): Failed to generate depthmap associated xmp metadata");
                    str3 = null;
                    if (str3 != null) {
                    }
                    Log.e(str, str2);
                    return portraitDepthMapExif.getJpeg();
                }
            } catch (IOException unused5) {
                file3 = file6;
                bArr3 = bArr7;
                Log.e(TAG, "writePortraitExif(): Failed to generate depthmap associated xmp metadata");
                str3 = null;
                if (str3 != null) {
                }
                Log.e(str, str2);
                return portraitDepthMapExif.getJpeg();
            }
            if (str3 != null) {
                str = TAG;
                str2 = "writePortraitExif(): #2: return original jpeg";
            } else {
                try {
                    bArr5 = bArr3;
                    try {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr5);
                        try {
                            ByteArrayOutputStream byteArrayOutputStream2 = new ByteArrayOutputStream();
                            XMPMeta createXMPMeta = XmpHelper.createXMPMeta();
                            createXMPMeta.setProperty(XmpHelper.XIAOMI_XMP_METADATA_NAMESPACE, XmpHelper.XIAOMI_XMP_METADATA_PROPERTY_NAME, str3);
                            XmpHelper.writeXMPMeta(byteArrayInputStream, byteArrayOutputStream2, createXMPMeta);
                            if (portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks() != null) {
                                try {
                                    if (portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks().length > 0 && portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks() != null && portraitDepthMapExif.getCoordinatesOfOfTheRegionUnderWatermarks().length >= 4) {
                                        byteArrayOutputStream2.write(portraitDepthMapExif.getJpegDataOfTheRegionUnderWatermarks());
                                    }
                                } catch (Throwable th4) {
                                    th2 = th4;
                                    bArr6 = null;
                                    try {
                                        throw th2;
                                    } catch (Throwable th5) {
                                        th = th5;
                                        th = th;
                                        bArr4 = bArr6;
                                        throw th;
                                    }
                                }
                            }
                            try {
                                if (portraitDepthMapExif.getDulWaterMark() != null) {
                                    byteArrayOutputStream2.write(portraitDepthMapExif.getDulWaterMark());
                                }
                                if (portraitDepthMapExif.getTimeWaterMark() != null) {
                                    byteArrayOutputStream2.write(portraitDepthMapExif.getTimeWaterMark());
                                }
                                if (j2 != 0) {
                                    File file7 = file;
                                    Util.writeFile2Stream(file7, byteArrayOutputStream2, 8);
                                    file7.delete();
                                }
                                if (j != 0) {
                                    File file8 = file2;
                                    Util.writeFile2Stream(file8, byteArrayOutputStream2, 8);
                                    file8.delete();
                                }
                                if (j3 != 0) {
                                    File file9 = file3;
                                    Util.writeFile2Stream(file9, byteArrayOutputStream2, 8);
                                    file9.delete();
                                }
                                byteArrayOutputStream2.flush();
                                bArr4 = byteArrayOutputStream2.toByteArray();
                            } catch (Throwable th6) {
                                bArr6 = null;
                                th2 = th6;
                                throw th2;
                            }
                            try {
                                $closeResource(null, byteArrayOutputStream2);
                                $closeResource(null, byteArrayInputStream);
                            } catch (Throwable th7) {
                                th = th7;
                                throw th;
                            }
                        } catch (Throwable th8) {
                            th = th8;
                            bArr6 = null;
                            th = th;
                            bArr4 = bArr6;
                            throw th;
                        }
                    } catch (XMPException | IOException unused6) {
                        bArr4 = null;
                        Log.d(TAG, "writePortraitExif(): Failed to insert depthmap associated xmp metadata");
                        if (bArr4 == null) {
                        }
                        str = TAG;
                        str2 = "writePortraitExif(): #3: return original jpeg";
                        Log.e(str, str2);
                        return portraitDepthMapExif.getJpeg();
                    }
                } catch (XMPException | IOException unused7) {
                    bArr5 = bArr3;
                    bArr4 = null;
                    Log.d(TAG, "writePortraitExif(): Failed to insert depthmap associated xmp metadata");
                    if (bArr4 == null) {
                    }
                    str = TAG;
                    str2 = "writePortraitExif(): #3: return original jpeg";
                    Log.e(str, str2);
                    return portraitDepthMapExif.getJpeg();
                }
                if (bArr4 == null && bArr4.length > bArr5.length) {
                    return bArr4;
                }
                str = TAG;
                str2 = "writePortraitExif(): #3: return original jpeg";
            }
        }
        Log.e(str, str2);
        return portraitDepthMapExif.getJpeg();
    }
}
