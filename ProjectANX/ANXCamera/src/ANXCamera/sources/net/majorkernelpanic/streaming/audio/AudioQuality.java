package net.majorkernelpanic.streaming.audio;

public class AudioQuality {
    public static final AudioQuality DEFAULT_AUDIO_QUALITY = new AudioQuality(8000, 32000);
    public int bitRate = 0;
    public int samplingRate = 0;

    public AudioQuality() {
    }

    public AudioQuality(int i, int i2) {
        this.samplingRate = i;
        this.bitRate = i2;
    }

    public static AudioQuality copyOf(AudioQuality audioQuality) {
        return new AudioQuality(audioQuality.samplingRate, audioQuality.bitRate);
    }

    public static AudioQuality parseQuality(String str) {
        AudioQuality copyOf = copyOf(DEFAULT_AUDIO_QUALITY);
        if (str != null) {
            String[] split = str.split("-");
            try {
                copyOf.bitRate = Integer.parseInt(split[0]) * 1000;
                copyOf.samplingRate = Integer.parseInt(split[1]);
            } catch (IndexOutOfBoundsException unused) {
            }
        }
        return copyOf;
    }

    public boolean equals(AudioQuality audioQuality) {
        boolean z = false;
        if (audioQuality == null) {
            return false;
        }
        if (audioQuality.samplingRate == this.samplingRate && audioQuality.bitRate == this.bitRate) {
            z = true;
        }
        return z;
    }
}
