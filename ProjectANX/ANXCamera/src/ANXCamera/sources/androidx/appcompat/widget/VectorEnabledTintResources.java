package androidx.appcompat.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.lang.ref.WeakReference;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class VectorEnabledTintResources extends Resources {
    public static final int MAX_SDK_WHERE_REQUIRED = 20;
    private static boolean sCompatVectorFromResourcesEnabled;
    private final WeakReference mContextRef;

    public VectorEnabledTintResources(@NonNull Context context, @NonNull Resources resources) {
        super(resources.getAssets(), resources.getDisplayMetrics(), resources.getConfiguration());
        this.mContextRef = new WeakReference(context);
    }

    public static boolean isCompatVectorFromResourcesEnabled() {
        return sCompatVectorFromResourcesEnabled;
    }

    public static void setCompatVectorFromResourcesEnabled(boolean z) {
        sCompatVectorFromResourcesEnabled = z;
    }

    public static boolean shouldBeUsed() {
        return isCompatVectorFromResourcesEnabled() && VERSION.SDK_INT <= 20;
    }

    public Drawable getDrawable(int i) {
        Context context = (Context) this.mContextRef.get();
        return context != null ? ResourceManagerInternal.get().onDrawableLoadedFromResources(context, this, i) : super.getDrawable(i);
    }

    /* access modifiers changed from: 0000 */
    public final Drawable superGetDrawable(int i) {
        return super.getDrawable(i);
    }
}
