package androidx.media;

import android.annotation.SuppressLint;
import android.media.AudioAttributes;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RequiresApi(21)
@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class AudioAttributesImplApi21 implements AudioAttributesImpl {
    private static final String TAG = "AudioAttributesCompat21";
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public AudioAttributes mAudioAttributes;
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public int mLegacyStreamType;

    class Builder implements androidx.media.AudioAttributesImpl.Builder {
        final android.media.AudioAttributes.Builder mFwkBuilder;

        Builder() {
            this.mFwkBuilder = new android.media.AudioAttributes.Builder();
        }

        Builder(Object obj) {
            this.mFwkBuilder = new android.media.AudioAttributes.Builder((AudioAttributes) obj);
        }

        public AudioAttributesImpl build() {
            return new AudioAttributesImplApi21(this.mFwkBuilder.build());
        }

        public Builder setContentType(int i) {
            this.mFwkBuilder.setContentType(i);
            return this;
        }

        public Builder setFlags(int i) {
            this.mFwkBuilder.setFlags(i);
            return this;
        }

        public Builder setLegacyStreamType(int i) {
            this.mFwkBuilder.setLegacyStreamType(i);
            return this;
        }

        public Builder setUsage(int i) {
            if (i == 16) {
                i = 12;
            }
            this.mFwkBuilder.setUsage(i);
            return this;
        }
    }

    AudioAttributesImplApi21() {
        this.mLegacyStreamType = -1;
    }

    AudioAttributesImplApi21(AudioAttributes audioAttributes) {
        this(audioAttributes, -1);
    }

    AudioAttributesImplApi21(AudioAttributes audioAttributes, int i) {
        this.mLegacyStreamType = -1;
        this.mAudioAttributes = audioAttributes;
        this.mLegacyStreamType = i;
    }

    public static AudioAttributesImpl fromBundle(Bundle bundle) {
        if (bundle == null) {
            return null;
        }
        AudioAttributes audioAttributes = (AudioAttributes) bundle.getParcelable("androidx.media.audio_attrs.FRAMEWORKS");
        if (audioAttributes == null) {
            return null;
        }
        return new AudioAttributesImplApi21(audioAttributes, bundle.getInt("androidx.media.audio_attrs.LEGACY_STREAM_TYPE", -1));
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof AudioAttributesImplApi21)) {
            return false;
        }
        return this.mAudioAttributes.equals(((AudioAttributesImplApi21) obj).mAudioAttributes);
    }

    public Object getAudioAttributes() {
        return this.mAudioAttributes;
    }

    public int getContentType() {
        return this.mAudioAttributes.getContentType();
    }

    public int getFlags() {
        return this.mAudioAttributes.getFlags();
    }

    public int getLegacyStreamType() {
        int i = this.mLegacyStreamType;
        return i != -1 ? i : AudioAttributesCompat.toVolumeStreamType(false, getFlags(), getUsage());
    }

    public int getRawLegacyStreamType() {
        return this.mLegacyStreamType;
    }

    public int getUsage() {
        return this.mAudioAttributes.getUsage();
    }

    @SuppressLint({"NewApi"})
    public int getVolumeControlStream() {
        return AudioAttributesCompat.toVolumeStreamType(true, getFlags(), getUsage());
    }

    public int hashCode() {
        return this.mAudioAttributes.hashCode();
    }

    @NonNull
    public Bundle toBundle() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("androidx.media.audio_attrs.FRAMEWORKS", this.mAudioAttributes);
        int i = this.mLegacyStreamType;
        if (i != -1) {
            bundle.putInt("androidx.media.audio_attrs.LEGACY_STREAM_TYPE", i);
        }
        return bundle;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AudioAttributesCompat: audioattributes=");
        sb.append(this.mAudioAttributes);
        return sb.toString();
    }
}
