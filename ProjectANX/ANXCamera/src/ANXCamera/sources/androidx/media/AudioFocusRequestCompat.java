package androidx.media;

import android.media.AudioAttributes;
import android.media.AudioFocusRequest;
import android.media.AudioManager.OnAudioFocusChangeListener;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.Handler.Callback;
import android.os.Looper;
import android.os.Message;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.core.util.ObjectsCompat;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class AudioFocusRequestCompat {
    static final AudioAttributesCompat FOCUS_DEFAULT_ATTR = new androidx.media.AudioAttributesCompat.Builder().setUsage(1).build();
    private final AudioAttributesCompat mAudioAttributesCompat;
    private final Handler mFocusChangeHandler;
    private final int mFocusGain;
    private final Object mFrameworkAudioFocusRequest;
    private final OnAudioFocusChangeListener mOnAudioFocusChangeListener;
    private final boolean mPauseOnDuck;

    public final class Builder {
        private boolean mAcceptsDelayedFocusGain;
        private AudioAttributesCompat mAudioAttributesCompat = AudioFocusRequestCompat.FOCUS_DEFAULT_ATTR;
        private Handler mFocusChangeHandler;
        private int mFocusGain;
        private OnAudioFocusChangeListener mOnAudioFocusChangeListener;
        private boolean mPauseOnDuck;

        public Builder(int i) {
            setFocusGain(i);
        }

        public Builder(@NonNull AudioFocusRequestCompat audioFocusRequestCompat) {
            if (audioFocusRequestCompat != null) {
                this.mFocusGain = audioFocusRequestCompat.getFocusGain();
                this.mOnAudioFocusChangeListener = audioFocusRequestCompat.getOnAudioFocusChangeListener();
                this.mFocusChangeHandler = audioFocusRequestCompat.getFocusChangeHandler();
                this.mAudioAttributesCompat = audioFocusRequestCompat.getAudioAttributesCompat();
                this.mPauseOnDuck = audioFocusRequestCompat.willPauseWhenDucked();
                return;
            }
            throw new IllegalArgumentException("AudioFocusRequestCompat to copy must not be null");
        }

        @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
        static boolean isValidFocusGain(int i) {
            return i == 1 || i == 2 || i == 3 || i == 4;
        }

        public AudioFocusRequestCompat build() {
            OnAudioFocusChangeListener onAudioFocusChangeListener = this.mOnAudioFocusChangeListener;
            if (onAudioFocusChangeListener != null) {
                AudioFocusRequestCompat audioFocusRequestCompat = new AudioFocusRequestCompat(this.mFocusGain, onAudioFocusChangeListener, this.mFocusChangeHandler, this.mAudioAttributesCompat, this.mPauseOnDuck);
                return audioFocusRequestCompat;
            }
            throw new IllegalStateException("Can't build an AudioFocusRequestCompat instance without a listener");
        }

        @NonNull
        public Builder setAudioAttributes(@NonNull AudioAttributesCompat audioAttributesCompat) {
            if (audioAttributesCompat != null) {
                this.mAudioAttributesCompat = audioAttributesCompat;
                return this;
            }
            throw new NullPointerException("Illegal null AudioAttributes");
        }

        @NonNull
        public Builder setFocusGain(int i) {
            if (isValidFocusGain(i)) {
                if (VERSION.SDK_INT < 19 && i == 4) {
                    i = 2;
                }
                this.mFocusGain = i;
                return this;
            }
            StringBuilder sb = new StringBuilder();
            sb.append("Illegal audio focus gain type ");
            sb.append(i);
            throw new IllegalArgumentException(sb.toString());
        }

        @NonNull
        public Builder setOnAudioFocusChangeListener(@NonNull OnAudioFocusChangeListener onAudioFocusChangeListener) {
            return setOnAudioFocusChangeListener(onAudioFocusChangeListener, new Handler(Looper.getMainLooper()));
        }

        @NonNull
        public Builder setOnAudioFocusChangeListener(@NonNull OnAudioFocusChangeListener onAudioFocusChangeListener, @NonNull Handler handler) {
            if (onAudioFocusChangeListener == null) {
                throw new IllegalArgumentException("OnAudioFocusChangeListener must not be null");
            } else if (handler != null) {
                this.mOnAudioFocusChangeListener = onAudioFocusChangeListener;
                this.mFocusChangeHandler = handler;
                return this;
            } else {
                throw new IllegalArgumentException("Handler must not be null");
            }
        }

        @NonNull
        public Builder setWillPauseWhenDucked(boolean z) {
            this.mPauseOnDuck = z;
            return this;
        }
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    @Retention(RetentionPolicy.SOURCE)
    public @interface FocusGainType {
    }

    class OnAudioFocusChangeListenerHandlerCompat implements Callback, OnAudioFocusChangeListener {
        private static final int FOCUS_CHANGE = 2782386;
        private final Handler mHandler;
        private final OnAudioFocusChangeListener mListener;

        OnAudioFocusChangeListenerHandlerCompat(@NonNull OnAudioFocusChangeListener onAudioFocusChangeListener, @NonNull Handler handler) {
            this.mListener = onAudioFocusChangeListener;
            this.mHandler = new Handler(handler.getLooper(), this);
        }

        public boolean handleMessage(Message message) {
            if (message.what != FOCUS_CHANGE) {
                return false;
            }
            this.mListener.onAudioFocusChange(message.arg1);
            return true;
        }

        public void onAudioFocusChange(int i) {
            Handler handler = this.mHandler;
            handler.sendMessage(Message.obtain(handler, FOCUS_CHANGE, i, 0));
        }
    }

    AudioFocusRequestCompat(int i, OnAudioFocusChangeListener onAudioFocusChangeListener, Handler handler, AudioAttributesCompat audioAttributesCompat, boolean z) {
        this.mFocusGain = i;
        this.mFocusChangeHandler = handler;
        this.mAudioAttributesCompat = audioAttributesCompat;
        this.mPauseOnDuck = z;
        if (VERSION.SDK_INT >= 26 || this.mFocusChangeHandler.getLooper() == Looper.getMainLooper()) {
            this.mOnAudioFocusChangeListener = onAudioFocusChangeListener;
        } else {
            this.mOnAudioFocusChangeListener = new OnAudioFocusChangeListenerHandlerCompat(onAudioFocusChangeListener, handler);
        }
        this.mFrameworkAudioFocusRequest = VERSION.SDK_INT >= 26 ? new android.media.AudioFocusRequest.Builder(this.mFocusGain).setAudioAttributes(getAudioAttributes()).setWillPauseWhenDucked(this.mPauseOnDuck).setOnAudioFocusChangeListener(this.mOnAudioFocusChangeListener, this.mFocusChangeHandler).build() : null;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || AudioFocusRequestCompat.class != obj.getClass()) {
            return false;
        }
        AudioFocusRequestCompat audioFocusRequestCompat = (AudioFocusRequestCompat) obj;
        if (this.mFocusGain != audioFocusRequestCompat.mFocusGain || this.mPauseOnDuck != audioFocusRequestCompat.mPauseOnDuck || !ObjectsCompat.equals(this.mOnAudioFocusChangeListener, audioFocusRequestCompat.mOnAudioFocusChangeListener) || !ObjectsCompat.equals(this.mFocusChangeHandler, audioFocusRequestCompat.mFocusChangeHandler) || !ObjectsCompat.equals(this.mAudioAttributesCompat, audioFocusRequestCompat.mAudioAttributesCompat)) {
            z = false;
        }
        return z;
    }

    /* access modifiers changed from: 0000 */
    @RequiresApi(21)
    public AudioAttributes getAudioAttributes() {
        AudioAttributesCompat audioAttributesCompat = this.mAudioAttributesCompat;
        if (audioAttributesCompat != null) {
            return (AudioAttributes) audioAttributesCompat.unwrap();
        }
        return null;
    }

    @NonNull
    public AudioAttributesCompat getAudioAttributesCompat() {
        return this.mAudioAttributesCompat;
    }

    /* access modifiers changed from: 0000 */
    @RequiresApi(26)
    public AudioFocusRequest getAudioFocusRequest() {
        return (AudioFocusRequest) this.mFrameworkAudioFocusRequest;
    }

    @NonNull
    public Handler getFocusChangeHandler() {
        return this.mFocusChangeHandler;
    }

    public int getFocusGain() {
        return this.mFocusGain;
    }

    @NonNull
    public OnAudioFocusChangeListener getOnAudioFocusChangeListener() {
        return this.mOnAudioFocusChangeListener;
    }

    public int hashCode() {
        return ObjectsCompat.hash(Integer.valueOf(this.mFocusGain), this.mOnAudioFocusChangeListener, this.mFocusChangeHandler, this.mAudioAttributesCompat, Boolean.valueOf(this.mPauseOnDuck));
    }

    public boolean willPauseWhenDucked() {
        return this.mPauseOnDuck;
    }
}
