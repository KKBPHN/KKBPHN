package androidx.core.view;

import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.WindowInsets;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import androidx.annotation.VisibleForTesting;
import androidx.core.graphics.Insets;
import androidx.core.util.ObjectsCompat;
import java.util.Objects;

public class WindowInsetsCompat {
    private final Object mInsets;

    public WindowInsetsCompat(WindowInsetsCompat windowInsetsCompat) {
        WindowInsets windowInsets = null;
        if (VERSION.SDK_INT >= 20 && windowInsetsCompat != null) {
            windowInsets = new WindowInsets((WindowInsets) windowInsetsCompat.mInsets);
        }
        this.mInsets = windowInsets;
    }

    @VisibleForTesting
    @RestrictTo({Scope.LIBRARY})
    WindowInsetsCompat(@Nullable Object obj) {
        this.mInsets = obj;
    }

    @RequiresApi(20)
    @Nullable
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static WindowInsets unwrap(@NonNull WindowInsetsCompat windowInsetsCompat) {
        return (WindowInsets) windowInsetsCompat.mInsets;
    }

    @RequiresApi(20)
    @NonNull
    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static WindowInsetsCompat wrap(@NonNull WindowInsets windowInsets) {
        return new WindowInsetsCompat(Objects.requireNonNull(windowInsets));
    }

    public WindowInsetsCompat consumeDisplayCutout() {
        return VERSION.SDK_INT >= 28 ? new WindowInsetsCompat((Object) ((WindowInsets) this.mInsets).consumeDisplayCutout()) : this;
    }

    public WindowInsetsCompat consumeStableInsets() {
        if (VERSION.SDK_INT >= 21) {
            return new WindowInsetsCompat((Object) ((WindowInsets) this.mInsets).consumeStableInsets());
        }
        return null;
    }

    public WindowInsetsCompat consumeSystemWindowInsets() {
        if (VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat((Object) ((WindowInsets) this.mInsets).consumeSystemWindowInsets());
        }
        return null;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || WindowInsetsCompat.class != obj.getClass()) {
            return false;
        }
        return ObjectsCompat.equals(this.mInsets, ((WindowInsetsCompat) obj).mInsets);
    }

    @Nullable
    public DisplayCutoutCompat getDisplayCutout() {
        if (VERSION.SDK_INT >= 28) {
            return DisplayCutoutCompat.wrap(((WindowInsets) this.mInsets).getDisplayCutout());
        }
        return null;
    }

    @NonNull
    public Insets getMandatorySystemGestureInsets() {
        return VERSION.SDK_INT >= 29 ? Insets.wrap(((WindowInsets) this.mInsets).getMandatorySystemGestureInsets()) : getSystemWindowInsets();
    }

    public int getStableInsetBottom() {
        if (VERSION.SDK_INT >= 21) {
            return ((WindowInsets) this.mInsets).getStableInsetBottom();
        }
        return 0;
    }

    public int getStableInsetLeft() {
        if (VERSION.SDK_INT >= 21) {
            return ((WindowInsets) this.mInsets).getStableInsetLeft();
        }
        return 0;
    }

    public int getStableInsetRight() {
        if (VERSION.SDK_INT >= 21) {
            return ((WindowInsets) this.mInsets).getStableInsetRight();
        }
        return 0;
    }

    public int getStableInsetTop() {
        if (VERSION.SDK_INT >= 21) {
            return ((WindowInsets) this.mInsets).getStableInsetTop();
        }
        return 0;
    }

    @NonNull
    public Insets getStableInsets() {
        return VERSION.SDK_INT >= 29 ? Insets.wrap(((WindowInsets) this.mInsets).getStableInsets()) : Insets.of(getStableInsetLeft(), getStableInsetTop(), getStableInsetRight(), getStableInsetBottom());
    }

    @NonNull
    public Insets getSystemGestureInsets() {
        return VERSION.SDK_INT >= 29 ? Insets.wrap(((WindowInsets) this.mInsets).getSystemGestureInsets()) : getSystemWindowInsets();
    }

    public int getSystemWindowInsetBottom() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).getSystemWindowInsetBottom();
        }
        return 0;
    }

    public int getSystemWindowInsetLeft() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).getSystemWindowInsetLeft();
        }
        return 0;
    }

    public int getSystemWindowInsetRight() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).getSystemWindowInsetRight();
        }
        return 0;
    }

    public int getSystemWindowInsetTop() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).getSystemWindowInsetTop();
        }
        return 0;
    }

    @NonNull
    public Insets getSystemWindowInsets() {
        return VERSION.SDK_INT >= 29 ? Insets.wrap(((WindowInsets) this.mInsets).getSystemWindowInsets()) : Insets.of(getSystemWindowInsetLeft(), getSystemWindowInsetTop(), getSystemWindowInsetRight(), getSystemWindowInsetBottom());
    }

    @NonNull
    public Insets getTappableElementInsets() {
        return VERSION.SDK_INT >= 29 ? Insets.wrap(((WindowInsets) this.mInsets).getTappableElementInsets()) : getSystemWindowInsets();
    }

    public boolean hasInsets() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).hasInsets();
        }
        return false;
    }

    public boolean hasStableInsets() {
        if (VERSION.SDK_INT >= 21) {
            return ((WindowInsets) this.mInsets).hasStableInsets();
        }
        return false;
    }

    public boolean hasSystemWindowInsets() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).hasSystemWindowInsets();
        }
        return false;
    }

    public int hashCode() {
        Object obj = this.mInsets;
        if (obj == null) {
            return 0;
        }
        return obj.hashCode();
    }

    public boolean isConsumed() {
        if (VERSION.SDK_INT >= 21) {
            return ((WindowInsets) this.mInsets).isConsumed();
        }
        return false;
    }

    public boolean isRound() {
        if (VERSION.SDK_INT >= 20) {
            return ((WindowInsets) this.mInsets).isRound();
        }
        return false;
    }

    public WindowInsetsCompat replaceSystemWindowInsets(int i, int i2, int i3, int i4) {
        if (VERSION.SDK_INT >= 20) {
            return new WindowInsetsCompat((Object) ((WindowInsets) this.mInsets).replaceSystemWindowInsets(i, i2, i3, i4));
        }
        return null;
    }

    public WindowInsetsCompat replaceSystemWindowInsets(Rect rect) {
        if (VERSION.SDK_INT >= 21) {
            return new WindowInsetsCompat((Object) ((WindowInsets) this.mInsets).replaceSystemWindowInsets(rect));
        }
        return null;
    }
}
