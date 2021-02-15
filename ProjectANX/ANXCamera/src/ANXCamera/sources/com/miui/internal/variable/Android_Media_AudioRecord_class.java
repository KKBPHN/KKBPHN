package com.miui.internal.variable;

import android.media.AudioRecord;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Media_AudioRecord_class {

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Media_AudioRecord_class Android_Media_AudioRecord_class;

        private Factory() {
            this.Android_Media_AudioRecord_class = (Android_Media_AudioRecord_class) create("Android_Media_AudioRecord_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Media_AudioRecord_class get() {
            return this.Android_Media_AudioRecord_class;
        }
    }

    public abstract boolean isExtraParamSupported();

    public abstract int setParameters(AudioRecord audioRecord, String str);
}
