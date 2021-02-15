package net.majorkernelpanic.streaming.video;

import android.annotation.SuppressLint;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.os.Build.VERSION;
import android.util.Log;
import android.util.SparseArray;
import java.util.ArrayList;
import java.util.HashMap;

@SuppressLint({"InlinedApi"})
public class CodecManager {
    public static final String[] SOFTWARE_ENCODERS = {"OMX.google.h264.encoder"};
    public static final int[] SUPPORTED_COLOR_FORMATS = {19, 21};
    public static final String TAG = "CodecManager";

    class Codecs {
        public String hardwareCodec;
        public int hardwareColorFormat;
        public String softwareCodec;
        public int softwareColorFormat;

        Codecs() {
        }
    }

    class Selector {
        private static HashMap sHardwareCodecs = new HashMap();
        private static HashMap sSoftwareCodecs = new HashMap();

        Selector() {
        }

        /* JADX WARNING: Failed to process nested try/catch */
        /* JADX WARNING: Missing exception handler attribute for start block: B:6:0x0040 */
        /* JADX WARNING: Removed duplicated region for block: B:11:0x0054  */
        /* JADX WARNING: Removed duplicated region for block: B:12:0x0071  */
        /* JADX WARNING: Removed duplicated region for block: B:15:0x0078  */
        /* JADX WARNING: Removed duplicated region for block: B:16:0x0095  */
        /* Code decompiled incorrectly, please refer to instructions dump. */
        public static Codecs findCodecsFormMimeType(String str, boolean z) {
            findSupportedColorFormats(str);
            SparseArray sparseArray = (SparseArray) sHardwareCodecs.get(str);
            SparseArray sparseArray2 = (SparseArray) sSoftwareCodecs.get(str);
            Codecs codecs = new Codecs();
            String str2 = "Choosen secondary codec: ";
            String str3 = "No supported software codec found !";
            String str4 = "Choosen primary codec: ";
            String str5 = "No supported hardware codec found !";
            String str6 = " with color format: ";
            String str7 = "CodecManager";
            if (VERSION.SDK_INT < 18 || !z) {
                int i = 0;
                while (true) {
                    int[] iArr = CodecManager.SUPPORTED_COLOR_FORMATS;
                    if (i >= iArr.length) {
                        break;
                    }
                    try {
                        codecs.hardwareCodec = (String) ((ArrayList) sparseArray.get(iArr[i])).get(0);
                        codecs.hardwareColorFormat = CodecManager.SUPPORTED_COLOR_FORMATS[i];
                        break;
                    } catch (Exception unused) {
                        i++;
                    }
                }
                int i2 = 0;
                while (true) {
                    int[] iArr2 = CodecManager.SUPPORTED_COLOR_FORMATS;
                    if (i2 >= iArr2.length) {
                        break;
                    }
                    try {
                        codecs.softwareCodec = (String) ((ArrayList) sparseArray2.get(iArr2[i2])).get(0);
                        codecs.softwareColorFormat = CodecManager.SUPPORTED_COLOR_FORMATS[i2];
                        break;
                    } catch (Exception unused2) {
                        i2++;
                    }
                }
                if (codecs.hardwareCodec != null) {
                    StringBuilder sb = new StringBuilder();
                    sb.append(str4);
                    sb.append(codecs.hardwareCodec);
                    sb.append(str6);
                    sb.append(codecs.hardwareColorFormat);
                    Log.v(str7, sb.toString());
                } else {
                    Log.e(str7, str5);
                }
                if (codecs.softwareCodec != null) {
                    StringBuilder sb2 = new StringBuilder();
                    sb2.append(str2);
                    sb2.append(codecs.hardwareCodec);
                    sb2.append(str6);
                    sb2.append(codecs.softwareColorFormat);
                    Log.v(str7, sb2.toString());
                } else {
                    Log.e(str7, str3);
                }
                return codecs;
            }
            codecs.hardwareCodec = (String) ((ArrayList) sparseArray.get(2130708361)).get(0);
            codecs.hardwareColorFormat = 2130708361;
            try {
                codecs.softwareCodec = (String) ((ArrayList) sparseArray2.get(2130708361)).get(0);
                codecs.softwareColorFormat = 2130708361;
            } catch (Exception unused3) {
            }
            if (codecs.hardwareCodec == null) {
                StringBuilder sb3 = new StringBuilder();
                sb3.append(str4);
                sb3.append(codecs.hardwareCodec);
                sb3.append(str6);
                sb3.append(codecs.hardwareColorFormat);
                Log.v(str7, sb3.toString());
            } else {
                Log.e(str7, str5);
            }
            if (codecs.softwareCodec == null) {
                StringBuilder sb4 = new StringBuilder();
                sb4.append(str2);
                sb4.append(codecs.hardwareCodec);
                sb4.append(str6);
                sb4.append(codecs.hardwareColorFormat);
                Log.v(str7, sb4.toString());
            } else {
                Log.e(str7, str3);
            }
            return codecs;
        }

        @SuppressLint({"NewApi"})
        private static void findSupportedColorFormats(String str) {
            int i;
            String str2;
            Object obj;
            SparseArray sparseArray = new SparseArray();
            SparseArray sparseArray2 = new SparseArray();
            if (!sSoftwareCodecs.containsKey(str)) {
                StringBuilder sb = new StringBuilder();
                sb.append("Searching supported color formats for mime type \"");
                sb.append(str);
                sb.append("\"...");
                String str3 = "CodecManager";
                Log.v(str3, sb.toString());
                int codecCount = MediaCodecList.getCodecCount() - 1;
                while (true) {
                    i = 0;
                    if (codecCount < 0) {
                        break;
                    }
                    MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(codecCount);
                    if (codecInfoAt.isEncoder()) {
                        String[] supportedTypes = codecInfoAt.getSupportedTypes();
                        for (int i2 = 0; i2 < supportedTypes.length; i2++) {
                            if (supportedTypes[i2].equalsIgnoreCase(str)) {
                                CodecCapabilities capabilitiesForType = codecInfoAt.getCapabilitiesForType(str);
                                boolean z = false;
                                for (int i3 = 0; i3 < CodecManager.SOFTWARE_ENCODERS.length; i3++) {
                                    if (codecInfoAt.getName().equalsIgnoreCase(CodecManager.SOFTWARE_ENCODERS[i2])) {
                                        z = true;
                                    }
                                }
                                int i4 = 0;
                                while (true) {
                                    int[] iArr = capabilitiesForType.colorFormats;
                                    if (i4 >= iArr.length) {
                                        break;
                                    }
                                    int i5 = iArr[i4];
                                    if (z) {
                                        if (sparseArray.get(i5) == null) {
                                            sparseArray.put(i5, new ArrayList());
                                        }
                                        obj = sparseArray.get(i5);
                                    } else {
                                        if (sparseArray2.get(i5) == null) {
                                            sparseArray2.put(i5, new ArrayList());
                                        }
                                        obj = sparseArray2.get(i5);
                                    }
                                    ((ArrayList) obj).add(codecInfoAt.getName());
                                    i4++;
                                }
                            }
                        }
                    }
                    codecCount--;
                }
                StringBuilder sb2 = new StringBuilder();
                sb2.append("Supported color formats on this phone: ");
                int i6 = 0;
                while (true) {
                    str2 = ", ";
                    if (i6 >= sparseArray.size()) {
                        break;
                    }
                    StringBuilder sb3 = new StringBuilder();
                    sb3.append(sparseArray.keyAt(i6));
                    sb3.append(str2);
                    sb2.append(sb3.toString());
                    i6++;
                }
                while (i < sparseArray2.size()) {
                    StringBuilder sb4 = new StringBuilder();
                    sb4.append(sparseArray2.keyAt(i));
                    sb4.append(i == sparseArray2.size() - 1 ? "." : str2);
                    sb2.append(sb4.toString());
                    i++;
                }
                Log.v(str3, sb2.toString());
                sSoftwareCodecs.put(str, sparseArray);
                sHardwareCodecs.put(str, sparseArray2);
            }
        }
    }

    class Translator {
        private int bufferSize;
        private int i;
        private int mHeight;
        private int mOutputColorFormat;
        private int mUVSize;
        private int mUVStride = (((int) Math.ceil(((double) (this.mYStride / 2)) / 16.0d)) * 16);
        private int mWidth;
        private int mYSize;
        private int mYStride = (((int) Math.ceil(((double) this.mWidth) / 16.0d)) * 16);
        private byte[] tmp;

        public Translator(int i2, int i3, int i4) {
            this.mOutputColorFormat = i2;
            this.mWidth = i3;
            this.mHeight = i4;
            int i5 = this.mYStride;
            int i6 = this.mHeight;
            this.mYSize = i5 * i6;
            this.mUVSize = (this.mUVStride * i6) / 2;
            int i7 = this.mYSize;
            int i8 = this.mUVSize;
            this.bufferSize = i7 + (i8 * 2);
            this.tmp = new byte[(i8 * 2)];
        }

        public int getBufferSize() {
            return this.bufferSize;
        }

        public int getUVStride() {
            return this.mUVStride;
        }

        public int getYStride() {
            return this.mYStride;
        }

        public byte[] translate(byte[] bArr) {
            int i2 = this.mOutputColorFormat;
            if (i2 == 19) {
                int i3 = this.bufferSize / 6;
                int i4 = i3 * 4;
                while (true) {
                    this.i = i4;
                    int i5 = this.i;
                    if (i5 >= i3 * 5) {
                        break;
                    }
                    byte b = bArr[i5];
                    bArr[i5] = bArr[i5 + i3];
                    bArr[i5 + i3] = b;
                    i4 = i5 + 1;
                }
            } else if (i2 == 21) {
                System.arraycopy(bArr, this.mYSize, this.tmp, 0, this.mUVSize * 2);
                this.i = 0;
                while (true) {
                    int i6 = this.i;
                    int i7 = this.mUVSize;
                    if (i6 >= i7) {
                        break;
                    }
                    int i8 = this.mYSize;
                    int i9 = (i6 * 2) + i8;
                    byte[] bArr2 = this.tmp;
                    bArr[i9] = bArr2[i7 + i6];
                    bArr[i8 + (i6 * 2) + 1] = bArr2[i6];
                    this.i = i6 + 1;
                }
            }
            return bArr;
        }
    }
}
