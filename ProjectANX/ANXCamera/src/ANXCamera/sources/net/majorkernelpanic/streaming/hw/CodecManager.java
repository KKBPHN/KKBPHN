package net.majorkernelpanic.streaming.hw;

import android.annotation.SuppressLint;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecList;
import android.os.Build.VERSION;
import android.util.Log;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Locale;

@SuppressLint({"InlinedApi"})
public class CodecManager {
    public static final int[] SUPPORTED_COLOR_FORMATS = {21, 39, 19, 20, 2130706688};
    public static final String TAG = "CodecManager";
    private static Codec[] sDecoders = null;
    private static Codec[] sEncoders = null;

    class Codec {
        public Integer[] formats;
        public boolean isHardwareAccelerated;
        public String name;

        public Codec(String str, Integer[] numArr, boolean z) {
            this.name = str;
            this.formats = numArr;
            this.isHardwareAccelerated = z;
        }
    }

    @SuppressLint({"NewApi"})
    public static synchronized Codec[] findDecodersForMimeType(String str) {
        synchronized (CodecManager.class) {
            if (sDecoders != null) {
                Codec[] codecArr = sDecoders;
                return codecArr;
            }
            ArrayList arrayList = new ArrayList();
            for (int codecCount = MediaCodecList.getCodecCount() - 1; codecCount >= 0; codecCount--) {
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(codecCount);
                if (!codecInfoAt.isEncoder()) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    for (String equalsIgnoreCase : supportedTypes) {
                        if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                            try {
                                CodecCapabilities capabilitiesForType = codecInfoAt.getCapabilitiesForType(str);
                                HashSet hashSet = new HashSet();
                                for (int i : capabilitiesForType.colorFormats) {
                                    for (int i2 : SUPPORTED_COLOR_FORMATS) {
                                        if (i == i2) {
                                            hashSet.add(Integer.valueOf(i));
                                        }
                                    }
                                }
                                arrayList.add(new Codec(codecInfoAt.getName(), (Integer[]) hashSet.toArray(new Integer[hashSet.size()]), isHardwareAccelerated(codecInfoAt)));
                            } catch (Exception e) {
                                Log.wtf("CodecManager", e);
                            }
                        }
                    }
                    continue;
                }
            }
            sDecoders = (Codec[]) arrayList.toArray(new Codec[arrayList.size()]);
            for (int i3 = 0; i3 < sDecoders.length; i3++) {
                if (sDecoders[i3].name.equalsIgnoreCase("omx.google.h264.decoder")) {
                    Codec codec = sDecoders[0];
                    sDecoders[0] = sDecoders[i3];
                    sDecoders[i3] = codec;
                }
            }
            Codec[] codecArr2 = sDecoders;
            return codecArr2;
        }
    }

    @SuppressLint({"NewApi"})
    public static synchronized Codec[] findEncodersForMimeType(String str) {
        synchronized (CodecManager.class) {
            if (sEncoders != null) {
                Codec[] codecArr = sEncoders;
                return codecArr;
            }
            ArrayList arrayList = new ArrayList();
            for (int codecCount = MediaCodecList.getCodecCount() - 1; codecCount >= 0; codecCount--) {
                MediaCodecInfo codecInfoAt = MediaCodecList.getCodecInfoAt(codecCount);
                if (codecInfoAt.isEncoder()) {
                    String[] supportedTypes = codecInfoAt.getSupportedTypes();
                    for (String equalsIgnoreCase : supportedTypes) {
                        if (equalsIgnoreCase.equalsIgnoreCase(str)) {
                            try {
                                CodecCapabilities capabilitiesForType = codecInfoAt.getCapabilitiesForType(str);
                                HashSet hashSet = new HashSet();
                                for (int i : capabilitiesForType.colorFormats) {
                                    for (int i2 : SUPPORTED_COLOR_FORMATS) {
                                        if (i == i2) {
                                            hashSet.add(Integer.valueOf(i));
                                        }
                                    }
                                }
                                arrayList.add(new Codec(codecInfoAt.getName(), (Integer[]) hashSet.toArray(new Integer[hashSet.size()]), isHardwareAccelerated(codecInfoAt)));
                            } catch (Exception e) {
                                Log.wtf("CodecManager", e);
                            }
                        }
                    }
                    continue;
                }
            }
            sEncoders = (Codec[]) arrayList.toArray(new Codec[arrayList.size()]);
            Codec[] codecArr2 = sEncoders;
            return codecArr2;
        }
    }

    private static boolean isHardwareAccelerated(MediaCodecInfo mediaCodecInfo) {
        return VERSION.SDK_INT >= 29 ? mediaCodecInfo.isHardwareAccelerated() : !isSoftwareOnly(mediaCodecInfo);
    }

    private static boolean isSoftwareOnly(MediaCodecInfo mediaCodecInfo) {
        if (VERSION.SDK_INT >= 29) {
            return mediaCodecInfo.isSoftwareOnly();
        }
        String lowerCase = mediaCodecInfo.getName().toLowerCase(Locale.ENGLISH);
        boolean z = false;
        if (lowerCase.startsWith("arc.")) {
            return false;
        }
        if (lowerCase.startsWith("omx.google.") || lowerCase.startsWith("omx.ffmpeg.") || ((lowerCase.startsWith("omx.sec.") && lowerCase.contains(".sw.")) || lowerCase.equals("omx.qcom.video.decoder.hevcswvdec") || lowerCase.startsWith("c2.android.") || lowerCase.startsWith("c2.google.") || (!lowerCase.startsWith("omx.") && !lowerCase.startsWith("c2.")))) {
            z = true;
        }
        return z;
    }
}
