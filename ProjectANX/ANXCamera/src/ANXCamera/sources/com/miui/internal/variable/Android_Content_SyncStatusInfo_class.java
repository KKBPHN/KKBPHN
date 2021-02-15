package com.miui.internal.variable;

import miui.util.SoftReferenceSingleton;

public abstract class Android_Content_SyncStatusInfo_class {
    public static final String NAME = "android.content.SyncStatusInfo";

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Content_SyncStatusInfo_class Android_Content_SyncStatusInfo_class;

        private Factory() {
            this.Android_Content_SyncStatusInfo_class = (Android_Content_SyncStatusInfo_class) create("Android_Content_SyncStatusInfo_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Content_SyncStatusInfo_class get() {
            return this.Android_Content_SyncStatusInfo_class;
        }
    }

    public abstract boolean getInitialize(Object obj);

    public abstract int getLastFailureMesgAsInt(Object obj, int i);

    public abstract long getLastFailureTime(Object obj);

    public abstract long getLastSuccessTime(Object obj);

    public abstract boolean getPending(Object obj);
}
