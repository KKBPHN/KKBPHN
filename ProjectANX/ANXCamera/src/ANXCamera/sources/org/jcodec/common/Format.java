package org.jcodec.common;

import java.util.LinkedHashMap;
import java.util.Map;

public final class Format {
    public static final Format AVI;
    public static final Format FLV;
    public static final Format H264;
    public static final Format IMG;
    public static final Format IVF;
    public static final Format MJPEG;
    public static final Format MKV;
    public static final Format MOV;
    public static final Format MPEG_AUDIO = new Format("MPEG_AUDIO", false, true);
    public static final Format MPEG_PS;
    public static final Format MPEG_TS;
    public static final Format RAW;
    public static final Format WAV;
    public static final Format WEBP = new Format("WEBP", true, false);
    public static final Format Y4M;
    private static final Map _values = new LinkedHashMap();
    private final boolean audio;
    private final String name;
    private final boolean video;

    static {
        String str = "MOV";
        MOV = new Format(str, true, true);
        String str2 = "MPEG_PS";
        MPEG_PS = new Format(str2, true, true);
        String str3 = "MPEG_TS";
        MPEG_TS = new Format(str3, true, true);
        String str4 = "MKV";
        MKV = new Format(str4, true, true);
        String str5 = "H264";
        H264 = new Format(str5, true, false);
        String str6 = "RAW";
        RAW = new Format(str6, true, true);
        String str7 = "FLV";
        FLV = new Format(str7, true, true);
        String str8 = "AVI";
        AVI = new Format(str8, true, true);
        String str9 = "IMG";
        IMG = new Format(str9, true, false);
        String str10 = "IVF";
        IVF = new Format(str10, true, false);
        String str11 = "MJPEG";
        MJPEG = new Format(str11, true, false);
        String str12 = "Y4M";
        Y4M = new Format(str12, true, false);
        String str13 = "WAV";
        WAV = new Format(str13, false, true);
        String str14 = str13;
        _values.put(str, MOV);
        _values.put(str2, MPEG_PS);
        _values.put(str3, MPEG_TS);
        _values.put(str4, MKV);
        _values.put(str5, H264);
        _values.put(str6, RAW);
        _values.put(str7, FLV);
        _values.put(str8, AVI);
        _values.put(str9, IMG);
        _values.put(str10, IVF);
        _values.put(str11, MJPEG);
        _values.put(str12, Y4M);
        _values.put(str14, WAV);
        _values.put("WEBP", WEBP);
        _values.put("MPEG_AUDIO", MPEG_AUDIO);
    }

    private Format(String str, boolean z, boolean z2) {
        this.name = str;
        this.video = z;
        this.audio = z2;
    }

    public static Format valueOf(String str) {
        return (Format) _values.get(str);
    }

    public boolean isAudio() {
        return this.audio;
    }

    public boolean isVideo() {
        return this.video;
    }
}
