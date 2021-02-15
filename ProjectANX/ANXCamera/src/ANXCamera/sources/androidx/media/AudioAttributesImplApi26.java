package androidx.media;

import android.media.AudioAttributes;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RequiresApi(26)
@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class AudioAttributesImplApi26 extends AudioAttributesImplApi21 {
    private static final String TAG = "AudioAttributesCompat26";

    class Builder extends Builder {
        Builder() {
        }

        Builder(Object obj) {
            super(obj);
        }

        public AudioAttributesImpl build() {
            return new AudioAttributesImplApi26(this.mFwkBuilder.build());
        }

        public Builder setUsage(int i) {
            this.mFwkBuilder.setUsage(i);
            return this;
        }
    }

    AudioAttributesImplApi26() {
    }

    AudioAttributesImplApi26(AudioAttributes audioAttributes) {
        super(audioAttributes, -1);
    }

    public int getVolumeControlStream() {
        return this.mAudioAttributes.getVolumeControlStream();
    }
}
