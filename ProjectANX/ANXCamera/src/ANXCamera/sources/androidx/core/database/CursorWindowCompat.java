package androidx.core.database;

import android.database.CursorWindow;
import android.os.Build.VERSION;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public final class CursorWindowCompat {
    private CursorWindowCompat() {
    }

    @NonNull
    public static CursorWindow create(@Nullable String str, long j) {
        int i = VERSION.SDK_INT;
        return i >= 28 ? new CursorWindow(str, j) : i >= 15 ? new CursorWindow(str) : new CursorWindow(false);
    }
}
