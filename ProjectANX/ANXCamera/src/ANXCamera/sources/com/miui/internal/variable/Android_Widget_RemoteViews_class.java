package com.miui.internal.variable;

import android.widget.RemoteViews;
import com.miui.internal.util.ClassProxy;
import miui.R;
import miui.util.SoftReferenceSingleton;

public abstract class Android_Widget_RemoteViews_class extends ClassProxy implements IManagedClassProxy {
    protected static final String PROCESS_NAME_SYSTEM_UI = "com.android.systemui";
    protected static final int REMOTE_VIEWS_TEMPLATE_THEME = R.style.Theme_DayNight_RemoteViews;

    public class Factory extends AbsClassFactory {
        private static final SoftReferenceSingleton INSTANCE = new SoftReferenceSingleton() {
            /* access modifiers changed from: protected */
            public Factory createInstance() {
                return new Factory();
            }
        };
        private Android_Widget_RemoteViews_class Android_Widget_RemoteViews_class;

        private Factory() {
            this.Android_Widget_RemoteViews_class = (Android_Widget_RemoteViews_class) create("Android_Widget_RemoteViews_class");
        }

        public static Factory getInstance() {
            return (Factory) INSTANCE.get();
        }

        public Android_Widget_RemoteViews_class get() {
            return this.Android_Widget_RemoteViews_class;
        }
    }

    public Android_Widget_RemoteViews_class() {
        super(RemoteViews.class);
    }
}
