package androidx.media;

import android.os.Bundle;
import android.support.v4.media.MediaBrowserCompat;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
public class MediaBrowserCompatUtils {
    private MediaBrowserCompatUtils() {
    }

    public static boolean areSameOptions(Bundle bundle, Bundle bundle2) {
        boolean z;
        boolean z2 = true;
        if (bundle == bundle2) {
            return true;
        }
        String str = MediaBrowserCompat.EXTRA_PAGE_SIZE;
        String str2 = MediaBrowserCompat.EXTRA_PAGE;
        if (bundle == null) {
            if (!(bundle2.getInt(str2, -1) == -1 && bundle2.getInt(str, -1) == -1)) {
                z2 = false;
            }
            return z2;
        } else if (bundle2 == null) {
            if (!(bundle.getInt(str2, -1) == -1 && bundle.getInt(str, -1) == -1)) {
                z2 = false;
            }
            return z2;
        } else {
            if (!(bundle.getInt(str2, -1) == bundle2.getInt(str2, -1) && bundle.getInt(str, -1) == bundle2.getInt(str, -1))) {
                z = false;
            }
            return z;
        }
    }

    public static boolean hasDuplicatedItems(Bundle bundle, Bundle bundle2) {
        int i;
        int i2;
        int i3;
        String str = MediaBrowserCompat.EXTRA_PAGE;
        int i4 = bundle == null ? -1 : bundle.getInt(str, -1);
        int i5 = bundle2 == null ? -1 : bundle2.getInt(str, -1);
        String str2 = MediaBrowserCompat.EXTRA_PAGE_SIZE;
        int i6 = bundle == null ? -1 : bundle.getInt(str2, -1);
        int i7 = bundle2 == null ? -1 : bundle2.getInt(str2, -1);
        int i8 = Integer.MAX_VALUE;
        if (i4 == -1 || i6 == -1) {
            i = Integer.MAX_VALUE;
            i2 = 0;
        } else {
            i2 = i4 * i6;
            i = (i6 + i2) - 1;
        }
        if (i5 == -1 || i7 == -1) {
            i3 = 0;
        } else {
            i3 = i5 * i7;
            i8 = (i7 + i3) - 1;
        }
        return i >= i3 && i8 >= i2;
    }
}
