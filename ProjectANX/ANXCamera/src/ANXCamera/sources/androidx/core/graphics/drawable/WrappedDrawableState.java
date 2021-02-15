package androidx.core.graphics.drawable;

import android.content.res.ColorStateList;
import android.content.res.Resources;
import android.graphics.PorterDuff.Mode;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.ConstantState;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

final class WrappedDrawableState extends ConstantState {
    int mChangingConfigurations;
    ConstantState mDrawableState;
    ColorStateList mTint = null;
    Mode mTintMode = WrappedDrawableApi14.DEFAULT_TINT_MODE;

    WrappedDrawableState(@Nullable WrappedDrawableState wrappedDrawableState) {
        if (wrappedDrawableState != null) {
            this.mChangingConfigurations = wrappedDrawableState.mChangingConfigurations;
            this.mDrawableState = wrappedDrawableState.mDrawableState;
            this.mTint = wrappedDrawableState.mTint;
            this.mTintMode = wrappedDrawableState.mTintMode;
        }
    }

    /* access modifiers changed from: 0000 */
    public boolean canConstantState() {
        return this.mDrawableState != null;
    }

    public int getChangingConfigurations() {
        int i = this.mChangingConfigurations;
        ConstantState constantState = this.mDrawableState;
        return (constantState != null ? constantState.getChangingConfigurations() : 0) | i;
    }

    @NonNull
    public Drawable newDrawable() {
        return newDrawable(null);
    }

    @NonNull
    public Drawable newDrawable(@Nullable Resources resources) {
        return VERSION.SDK_INT >= 21 ? new WrappedDrawableApi21(this, resources) : new WrappedDrawableApi14(this, resources);
    }
}
