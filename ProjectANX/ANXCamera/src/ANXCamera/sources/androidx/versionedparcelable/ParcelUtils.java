package androidx.versionedparcelable;

import android.os.Bundle;
import android.os.Parcelable;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class ParcelUtils {
    private static final String INNER_BUNDLE_KEY = "a";

    private ParcelUtils() {
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static VersionedParcelable fromInputStream(InputStream inputStream) {
        return new VersionedParcelStream(inputStream, null).readVersionedParcelable();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static VersionedParcelable fromParcelable(Parcelable parcelable) {
        if (parcelable instanceof ParcelImpl) {
            return ((ParcelImpl) parcelable).getVersionedParcel();
        }
        throw new IllegalArgumentException("Invalid parcel");
    }

    @Nullable
    public static VersionedParcelable getVersionedParcelable(@NonNull Bundle bundle, @NonNull String str) {
        try {
            Bundle bundle2 = (Bundle) bundle.getParcelable(str);
            if (bundle2 == null) {
                return null;
            }
            bundle2.setClassLoader(ParcelUtils.class.getClassLoader());
            return fromParcelable(bundle2.getParcelable("a"));
        } catch (RuntimeException unused) {
            return null;
        }
    }

    @Nullable
    public static List getVersionedParcelableList(Bundle bundle, String str) {
        ArrayList arrayList = new ArrayList();
        try {
            Bundle bundle2 = (Bundle) bundle.getParcelable(str);
            bundle2.setClassLoader(ParcelUtils.class.getClassLoader());
            Iterator it = bundle2.getParcelableArrayList("a").iterator();
            while (it.hasNext()) {
                arrayList.add(fromParcelable((Parcelable) it.next()));
            }
            return arrayList;
        } catch (RuntimeException unused) {
            return null;
        }
    }

    public static void putVersionedParcelable(@NonNull Bundle bundle, @NonNull String str, @Nullable VersionedParcelable versionedParcelable) {
        if (versionedParcelable != null) {
            Bundle bundle2 = new Bundle();
            bundle2.putParcelable("a", toParcelable(versionedParcelable));
            bundle.putParcelable(str, bundle2);
        }
    }

    /* JADX WARNING: Incorrect type for immutable var: ssa=java.util.List, code=java.util.List<androidx.versionedparcelable.VersionedParcelable>, for r5v0, types: [java.util.List, java.util.List<androidx.versionedparcelable.VersionedParcelable>] */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void putVersionedParcelableList(@NonNull Bundle bundle, @NonNull String str, @NonNull List<VersionedParcelable> list) {
        Bundle bundle2 = new Bundle();
        ArrayList arrayList = new ArrayList();
        for (VersionedParcelable parcelable : list) {
            arrayList.add(toParcelable(parcelable));
        }
        bundle2.putParcelableArrayList("a", arrayList);
        bundle.putParcelable(str, bundle2);
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static void toOutputStream(VersionedParcelable versionedParcelable, OutputStream outputStream) {
        VersionedParcelStream versionedParcelStream = new VersionedParcelStream(null, outputStream);
        versionedParcelStream.writeVersionedParcelable(versionedParcelable);
        versionedParcelStream.closeField();
    }

    @RestrictTo({Scope.LIBRARY_GROUP_PREFIX})
    public static Parcelable toParcelable(VersionedParcelable versionedParcelable) {
        return new ParcelImpl(versionedParcelable);
    }
}
