package miui.external;

import android.content.Context;
import android.content.res.Configuration;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.Map;
import miui.external.SdkConstants.SdkError;

public class Application extends android.app.Application implements SdkConstants {
    private static final String PACKAGE_NAME = "com.miui.core";
    private ApplicationDelegate mApplicationDelegate;
    private boolean mInitialized;
    private boolean mStarted;

    public Application() {
        if (loadSdk() && initializeSdk()) {
            this.mInitialized = true;
        }
    }

    private void handleGenericError(Throwable th) {
        while (th != null && th.getCause() != null && ((th instanceof InvocationTargetException) || (th instanceof ExceptionInInitializerError))) {
            th = th.getCause();
        }
        Log.e("miuisdk", "MIUI SDK encounter errors, please contact miuisdk@xiaomi.com for support.", th);
        SdkErrorInstrumentation.handleSdkError(SdkError.GENERIC);
    }

    private void handleUnknownError(String str, int i) {
        StringBuilder sb = new StringBuilder();
        sb.append("MIUI SDK encounter errors, please contact miuisdk@xiaomi.com for support. phase: ");
        sb.append(str);
        sb.append(" code: ");
        sb.append(i);
        Log.e("miuisdk", sb.toString());
        SdkErrorInstrumentation.handleSdkError(SdkError.GENERIC);
    }

    private boolean initializeSdk() {
        String str = "initialize";
        try {
            HashMap hashMap = new HashMap();
            int intValue = ((Integer) SdkEntranceHelper.getSdkEntrance().getMethod(str, new Class[]{android.app.Application.class, Map.class}).invoke(null, new Object[]{this, hashMap})).intValue();
            if (intValue == 0) {
                return true;
            }
            handleUnknownError(str, intValue);
            return false;
        } catch (Throwable th) {
            handleGenericError(th);
            return false;
        }
    }

    private boolean loadSdk() {
        String str = "com.miui.core";
        try {
            if (SdkHelper.isMiuiSystem() || SdkLoader.load(SdkHelper.getApkPath(null, str, "miui"), null, SdkHelper.getLibPath(null, str), Application.class.getClassLoader())) {
                return true;
            }
            SdkErrorInstrumentation.handleSdkError(SdkError.NO_SDK);
            return false;
        } catch (Throwable th) {
            handleGenericError(th);
            return false;
        }
    }

    private boolean startSdk() {
        String str = "start";
        try {
            HashMap hashMap = new HashMap();
            int intValue = ((Integer) SdkEntranceHelper.getSdkEntrance().getMethod(str, new Class[]{Map.class}).invoke(null, new Object[]{hashMap})).intValue();
            if (intValue == 1) {
                SdkErrorInstrumentation.handleSdkError(SdkError.LOW_SDK_VERSION);
                return false;
            } else if (intValue == 0) {
                return true;
            } else {
                handleUnknownError(str, intValue);
                return false;
            }
        } catch (Throwable th) {
            handleGenericError(th);
            return false;
        }
    }

    /* access modifiers changed from: protected */
    public void attachBaseContext(Context context) {
        super.attachBaseContext(context);
        if (this.mInitialized && startSdk()) {
            this.mApplicationDelegate = onCreateApplicationDelegate();
            ApplicationDelegate applicationDelegate = this.mApplicationDelegate;
            if (applicationDelegate != null) {
                applicationDelegate.attach(this);
            }
            this.mStarted = true;
        }
    }

    public final ApplicationDelegate getApplicationDelegate() {
        return this.mApplicationDelegate;
    }

    public final void onConfigurationChanged(Configuration configuration) {
        ApplicationDelegate applicationDelegate = this.mApplicationDelegate;
        if (applicationDelegate != null) {
            applicationDelegate.onConfigurationChanged(configuration);
        } else {
            superOnConfigurationChanged(configuration);
        }
    }

    public final void onCreate() {
        if (this.mStarted) {
            ApplicationDelegate applicationDelegate = this.mApplicationDelegate;
            if (applicationDelegate != null) {
                applicationDelegate.onCreate();
            } else {
                superOnCreate();
            }
        }
    }

    public ApplicationDelegate onCreateApplicationDelegate() {
        return null;
    }

    public final void onLowMemory() {
        ApplicationDelegate applicationDelegate = this.mApplicationDelegate;
        if (applicationDelegate != null) {
            applicationDelegate.onLowMemory();
        } else {
            superOnLowMemory();
        }
    }

    public final void onTerminate() {
        ApplicationDelegate applicationDelegate = this.mApplicationDelegate;
        if (applicationDelegate != null) {
            applicationDelegate.onTerminate();
        } else {
            superOnTerminate();
        }
    }

    public final void onTrimMemory(int i) {
        ApplicationDelegate applicationDelegate = this.mApplicationDelegate;
        if (applicationDelegate != null) {
            applicationDelegate.onTrimMemory(i);
        } else {
            superOnTrimMemory(i);
        }
    }

    /* access modifiers changed from: 0000 */
    public final void superOnConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
    }

    /* access modifiers changed from: 0000 */
    public final void superOnCreate() {
        super.onCreate();
    }

    /* access modifiers changed from: 0000 */
    public final void superOnLowMemory() {
        super.onLowMemory();
    }

    /* access modifiers changed from: 0000 */
    public final void superOnTerminate() {
        super.onTerminate();
    }

    /* access modifiers changed from: 0000 */
    public final void superOnTrimMemory(int i) {
        super.onTrimMemory(i);
    }
}
