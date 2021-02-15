package com.xiaomi.asr.engine.record;

import android.util.Log;
import com.xiaomi.asr.engine.utils.JSONCompliant;
import java.util.Arrays;
import org.json.JSONException;
import org.json.JSONObject;

public class AudioType implements JSONCompliant {
    public static final AudioType OPUS_NB = new AudioType(Encoding.OPUS, 8000);
    public static final AudioType OPUS_WB = new AudioType(Encoding.OPUS, 16000);
    public static final AudioType PCM_11k = new AudioType(Encoding.PCM_16, Frequency.FREQ_11KHZ);
    public static final AudioType PCM_16k = new AudioType(Encoding.PCM_16, 16000);
    public static final AudioType PCM_22k = new AudioType(Encoding.PCM_16, Frequency.FREQ_22KHZ);
    public static final AudioType PCM_44k = new AudioType(Encoding.PCM_16, 44100);
    public static final AudioType PCM_48k = new AudioType(Encoding.PCM_16, Frequency.FREQ_48KHZ);
    public static final AudioType PCM_8k = new AudioType(Encoding.PCM_16, 8000);
    public static final AudioType SPEEX_NB = new AudioType(Encoding.SPEEX, 8000);
    public static final AudioType SPEEX_WB = new AudioType(Encoding.SPEEX, 16000);
    public static final AudioType UNKNOWN = new AudioType(Encoding.UNKNOWN, 0);
    public final Encoding encoding;
    public final int frequency;
    public final byte[] sse;

    public enum Encoding {
        PCM_16,
        SPEEX,
        OPUS,
        UNKNOWN
    }

    public class Frequency {
        public static final int FREQ_11KHZ = 11025;
        public static final int FREQ_16KHZ = 16000;
        public static final int FREQ_22KHZ = 22050;
        public static final int FREQ_44KHZ = 44100;
        public static final int FREQ_48KHZ = 48000;
        public static final int FREQ_8KHZ = 8000;
    }

    public AudioType(Encoding encoding2, int i) {
        this(encoding2, i, null);
    }

    public AudioType(Encoding encoding2, int i, byte[] bArr) {
        this.frequency = i;
        this.encoding = encoding2;
        this.sse = bArr;
    }

    public static AudioType fromJSON(JSONObject jSONObject) {
        Encoding[] values;
        int i = jSONObject.getInt("freq");
        String string = jSONObject.getString("enc");
        for (Encoding encoding2 : Encoding.values()) {
            if (encoding2.name().equals(string)) {
                return new AudioType(encoding2, i);
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Encoding '");
        sb.append(string);
        sb.append("' not valid");
        throw new JSONException(sb.toString());
    }

    public final boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || AudioType.class != obj.getClass()) {
            return false;
        }
        AudioType audioType = (AudioType) obj;
        if (this.encoding == audioType.encoding && this.frequency == audioType.frequency) {
            return Arrays.equals(this.sse, audioType.sse);
        }
        return false;
    }

    public int getDuration(int i) {
        if (this.encoding == Encoding.PCM_16) {
            return (int) ((((long) i) * 1000) / ((long) this.frequency));
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to detect duration for encoding ");
        sb.append(this.encoding.name());
        Log.e("AudioType", sb.toString());
        return 0;
    }

    public int getDuration(short[] sArr) {
        return getDuration(sArr.length);
    }

    public int getSampleCount(int i) {
        return (this.frequency * i) / 1000;
    }

    public final int hashCode() {
        Encoding encoding2 = this.encoding;
        return (((((encoding2 == null ? 0 : encoding2.hashCode()) + 31) * 31) + this.frequency) * 31) + Arrays.hashCode(this.sse);
    }

    public JSONObject toJSON() {
        return new JSONObject();
    }

    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AudioType [frequency=");
        sb.append(this.frequency);
        sb.append(", encoding=");
        sb.append(this.encoding);
        sb.append(", sse=");
        sb.append(Arrays.toString(this.sse));
        sb.append("]");
        return sb.toString();
    }
}
