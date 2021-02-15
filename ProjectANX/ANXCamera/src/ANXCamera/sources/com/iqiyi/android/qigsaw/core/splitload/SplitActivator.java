package com.iqiyi.android.qigsaw.core.splitload;

import android.app.Application;
import android.content.Context;
import com.iqiyi.android.qigsaw.core.extension.AABExtension;
import com.iqiyi.android.qigsaw.core.extension.AABExtensionException;
import java.util.HashMap;
import java.util.Map;
import miui.extension.target.ActivityTarget;

final class SplitActivator {
    private static final Map sSplitApplicationMap = new HashMap();
    private final AABExtension aabExtension = AABExtension.getInstance();
    private final Context appContext;

    SplitActivator(Context context) {
        this.appContext = context;
    }

    private boolean debuggable() {
        try {
            return (this.appContext.getApplicationInfo().flags & 2) != 0;
        } catch (Throwable unused) {
            return false;
        }
    }

    /* access modifiers changed from: 0000 */
    public void activate(ClassLoader classLoader, String str) {
        try {
            Application createApplication = this.aabExtension.createApplication(classLoader, str);
            if (createApplication != null) {
                sSplitApplicationMap.put(str, createApplication);
            }
            this.aabExtension.activeApplication(createApplication, this.appContext);
            try {
                this.aabExtension.activateSplitProviders(classLoader, str);
                if (createApplication != null) {
                    try {
                        HiddenApiReflection.findMethod(Application.class, ActivityTarget.ACTION_ON_CREATE, new Class[0]).invoke(createApplication, new Object[0]);
                    } catch (Throwable th) {
                        if (debuggable()) {
                            throw new RuntimeException(th);
                        }
                        throw new SplitLoadException(-24, th);
                    }
                }
            } catch (AABExtensionException e) {
                throw new SplitLoadException(-25, e);
            }
        } catch (Throwable th2) {
            if (!debuggable() || (th2 instanceof AABExtensionException)) {
                throw new SplitLoadException(-24, th2);
            }
            throw new RuntimeException(th2);
        }
    }
}
