package tv.danmaku.ijk.media.player;

import android.annotation.TargetApi;
import android.media.MediaCodecInfo;
import android.media.MediaCodecInfo.CodecCapabilities;
import android.media.MediaCodecInfo.CodecProfileLevel;
import android.os.Build.VERSION;
import android.text.TextUtils;
import android.util.Log;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;
import miui.telephony.phonenumber.CountryCodeConverter;

public class IjkMediaCodecInfo {
    public static final int RANK_ACCEPTABLE = 700;
    public static final int RANK_LAST_CHANCE = 600;
    public static final int RANK_MAX = 1000;
    public static final int RANK_NON_STANDARD = 100;
    public static final int RANK_NO_SENSE = 0;
    public static final int RANK_SECURE = 300;
    public static final int RANK_SOFTWARE = 200;
    public static final int RANK_TESTED = 800;
    private static final String TAG = "IjkMediaCodecInfo";
    private static Map sKnownCodecList;
    public MediaCodecInfo mCodecInfo;
    public String mMimeType;
    public int mRank = 0;

    private static synchronized Map getKnownCodecList() {
        synchronized (IjkMediaCodecInfo.class) {
            if (sKnownCodecList != null) {
                Map map = sKnownCodecList;
                return map;
            }
            sKnownCodecList = new TreeMap(String.CASE_INSENSITIVE_ORDER);
            sKnownCodecList.put("OMX.Nvidia.h264.decode", Integer.valueOf(800));
            sKnownCodecList.put("OMX.Nvidia.h264.decode.secure", Integer.valueOf(300));
            sKnownCodecList.put("OMX.Intel.hw_vd.h264", Integer.valueOf(IMediaPlayer.MEDIA_INFO_NOT_SEEKABLE));
            sKnownCodecList.put("OMX.Intel.VideoDecoder.AVC", Integer.valueOf(800));
            sKnownCodecList.put("OMX.qcom.video.decoder.avc", Integer.valueOf(800));
            sKnownCodecList.put("OMX.ittiam.video.decoder.avc", Integer.valueOf(0));
            sKnownCodecList.put("OMX.SEC.avc.dec", Integer.valueOf(800));
            sKnownCodecList.put("OMX.SEC.AVC.Decoder", Integer.valueOf(799));
            sKnownCodecList.put("OMX.SEC.avcdec", Integer.valueOf(798));
            sKnownCodecList.put("OMX.SEC.avc.sw.dec", Integer.valueOf(200));
            sKnownCodecList.put("OMX.Exynos.avc.dec", Integer.valueOf(800));
            sKnownCodecList.put("OMX.Exynos.AVC.Decoder", Integer.valueOf(799));
            sKnownCodecList.put("OMX.k3.video.decoder.avc", Integer.valueOf(800));
            sKnownCodecList.put("OMX.IMG.MSVDX.Decoder.AVC", Integer.valueOf(800));
            sKnownCodecList.put("OMX.TI.DUCATI1.VIDEO.DECODER", Integer.valueOf(800));
            sKnownCodecList.put("OMX.rk.video_decoder.avc", Integer.valueOf(800));
            sKnownCodecList.put("OMX.amlogic.avc.decoder.awesome", Integer.valueOf(800));
            sKnownCodecList.put("OMX.MARVELL.VIDEO.HW.CODA7542DECODER", Integer.valueOf(800));
            sKnownCodecList.put("OMX.MARVELL.VIDEO.H264DECODER", Integer.valueOf(200));
            sKnownCodecList.remove("OMX.Action.Video.Decoder");
            sKnownCodecList.remove("OMX.allwinner.video.decoder.avc");
            sKnownCodecList.remove("OMX.BRCM.vc4.decoder.avc");
            sKnownCodecList.remove("OMX.brcm.video.h264.hw.decoder");
            sKnownCodecList.remove("OMX.brcm.video.h264.decoder");
            sKnownCodecList.remove("OMX.cosmo.video.decoder.avc");
            sKnownCodecList.remove("OMX.duos.h264.decoder");
            sKnownCodecList.remove("OMX.hantro.81x0.video.decoder");
            sKnownCodecList.remove("OMX.hantro.G1.video.decoder");
            sKnownCodecList.remove("OMX.hisi.video.decoder");
            sKnownCodecList.remove("OMX.LG.decoder.video.avc");
            sKnownCodecList.remove("OMX.MS.AVC.Decoder");
            sKnownCodecList.remove("OMX.RENESAS.VIDEO.DECODER.H264");
            sKnownCodecList.remove("OMX.RTK.video.decoder");
            sKnownCodecList.remove("OMX.sprd.h264.decoder");
            sKnownCodecList.remove("OMX.ST.VFM.H264Dec");
            sKnownCodecList.remove("OMX.vpu.video_decoder.avc");
            sKnownCodecList.remove("OMX.WMT.decoder.avc");
            sKnownCodecList.remove("OMX.bluestacks.hw.decoder");
            sKnownCodecList.put("OMX.google.h264.decoder", Integer.valueOf(200));
            sKnownCodecList.put("OMX.google.h264.lc.decoder", Integer.valueOf(200));
            sKnownCodecList.put("OMX.k3.ffmpeg.decoder", Integer.valueOf(200));
            sKnownCodecList.put("OMX.ffmpeg.video.decoder", Integer.valueOf(200));
            sKnownCodecList.put("OMX.sprd.soft.h264.decoder", Integer.valueOf(200));
            Map map2 = sKnownCodecList;
            return map2;
        }
    }

    public static String getLevelName(int i) {
        if (i == 1) {
            return "1";
        }
        if (i == 2) {
            return "1b";
        }
        switch (i) {
            case 4:
                return "11";
            case 8:
                return "12";
            case 16:
                return "13";
            case 32:
                return "2";
            case 64:
                return "21";
            case 128:
                return "22";
            case 256:
                return "3";
            case 512:
                return CountryCodeConverter.NL;
            case 1024:
                return CountryCodeConverter.BE;
            case 2048:
                return "4";
            case 4096:
                return CountryCodeConverter.CH;
            case 8192:
                return "42";
            case 16384:
                return "5";
            case 32768:
                return CountryCodeConverter.PE;
            case 65536:
                return CountryCodeConverter.MX;
            default:
                return "0";
        }
    }

    public static String getProfileLevelName(int i, int i2) {
        return String.format(Locale.US, " %s Profile Level %s (%d,%d)", new Object[]{getProfileName(i), getLevelName(i2), Integer.valueOf(i), Integer.valueOf(i2)});
    }

    public static String getProfileName(int i) {
        return i != 1 ? i != 2 ? i != 4 ? i != 8 ? i != 16 ? i != 32 ? i != 64 ? "Unknown" : "High444" : "High422" : "High10" : "High" : "Extends" : "Main" : "Baseline";
    }

    @TargetApi(16)
    public static IjkMediaCodecInfo setupCandidate(MediaCodecInfo mediaCodecInfo, String str) {
        IjkMediaCodecInfo ijkMediaCodecInfo = null;
        if (mediaCodecInfo != null && VERSION.SDK_INT >= 16) {
            String name = mediaCodecInfo.getName();
            if (TextUtils.isEmpty(name)) {
                return null;
            }
            String lowerCase = name.toLowerCase(Locale.US);
            boolean startsWith = lowerCase.startsWith("omx.");
            int i = RANK_LAST_CHANCE;
            if (!startsWith) {
                i = 100;
            } else if (!lowerCase.startsWith("omx.pv") && !lowerCase.startsWith("omx.google.") && !lowerCase.startsWith("omx.ffmpeg.") && !lowerCase.startsWith("omx.k3.ffmpeg.") && !lowerCase.startsWith("omx.avcodec.")) {
                if (!lowerCase.startsWith("omx.ittiam.")) {
                    if (!lowerCase.startsWith("omx.mtk.")) {
                        Integer num = (Integer) getKnownCodecList().get(lowerCase);
                        if (num != null) {
                            i = num.intValue();
                        } else {
                            try {
                                if (mediaCodecInfo.getCapabilitiesForType(str) != null) {
                                    i = 700;
                                }
                            } catch (Throwable unused) {
                            }
                        }
                    } else if (VERSION.SDK_INT >= 18) {
                        i = 800;
                    }
                }
                i = 0;
            } else {
                i = 200;
            }
            ijkMediaCodecInfo = new IjkMediaCodecInfo();
            ijkMediaCodecInfo.mCodecInfo = mediaCodecInfo;
            ijkMediaCodecInfo.mRank = i;
            ijkMediaCodecInfo.mMimeType = str;
        }
        return ijkMediaCodecInfo;
    }

    @TargetApi(16)
    public void dumpProfileLevels(String str) {
        int i;
        int i2;
        CodecProfileLevel[] codecProfileLevelArr;
        String str2 = TAG;
        if (VERSION.SDK_INT >= 16) {
            try {
                CodecCapabilities capabilitiesForType = this.mCodecInfo.getCapabilitiesForType(str);
                if (capabilitiesForType == null || capabilitiesForType.profileLevels == null) {
                    i2 = 0;
                    i = 0;
                } else {
                    i2 = 0;
                    i = 0;
                    for (CodecProfileLevel codecProfileLevel : capabilitiesForType.profileLevels) {
                        if (codecProfileLevel != null) {
                            i2 = Math.max(i2, codecProfileLevel.profile);
                            i = Math.max(i, codecProfileLevel.level);
                        }
                    }
                }
                Log.i(str2, String.format(Locale.US, "%s", new Object[]{getProfileLevelName(i2, i)}));
            } catch (Throwable unused) {
                Log.i(str2, "profile-level: exception");
            }
        }
    }
}
