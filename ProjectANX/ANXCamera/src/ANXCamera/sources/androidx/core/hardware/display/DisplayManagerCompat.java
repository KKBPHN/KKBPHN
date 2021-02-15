package androidx.core.hardware.display;

import android.content.Context;
import android.hardware.display.DisplayManager;
import android.os.Build.VERSION;
import android.view.Display;
import android.view.WindowManager;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import java.util.WeakHashMap;

public final class DisplayManagerCompat {
    public static final String DISPLAY_CATEGORY_PRESENTATION = "android.hardware.display.category.PRESENTATION";
    private static final WeakHashMap sInstances = new WeakHashMap();
    private final Context mContext;

    private DisplayManagerCompat(Context context) {
        this.mContext = context;
    }

    @NonNull
    public static DisplayManagerCompat getInstance(@NonNull Context context) {
        DisplayManagerCompat displayManagerCompat;
        synchronized (sInstances) {
            displayManagerCompat = (DisplayManagerCompat) sInstances.get(context);
            if (displayManagerCompat == null) {
                displayManagerCompat = new DisplayManagerCompat(context);
                sInstances.put(context, displayManagerCompat);
            }
        }
        return displayManagerCompat;
    }

    @Nullable
    public Display getDisplay(int i) {
        int i2 = VERSION.SDK_INT;
        Context context = this.mContext;
        if (i2 >= 17) {
            return ((DisplayManager) context.getSystemService("display")).getDisplay(i);
        }
        Display defaultDisplay = ((WindowManager) context.getSystemService("window")).getDefaultDisplay();
        if (defaultDisplay.getDisplayId() == i) {
            return defaultDisplay;
        }
        return null;
    }

    @NonNull
    public Display[] getDisplays() {
        int i = VERSION.SDK_INT;
        Context context = this.mContext;
        if (i >= 17) {
            return ((DisplayManager) context.getSystemService("display")).getDisplays();
        }
        return new Display[]{((WindowManager) context.getSystemService("window")).getDefaultDisplay()};
    }

    @NonNull
    public Display[] getDisplays(@Nullable String str) {
        if (VERSION.SDK_INT >= 17) {
            return ((DisplayManager) this.mContext.getSystemService("display")).getDisplays(str);
        }
        if (str == null) {
            return new Display[0];
        }
        return new Display[]{((WindowManager) this.mContext.getSystemService("window")).getDefaultDisplay()};
    }
}
