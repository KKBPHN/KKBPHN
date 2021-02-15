package androidx.core.os;

import android.os.Parcel;
import android.os.Parcelable.ClassLoaderCreator;
import android.os.Parcelable.Creator;

@Deprecated
public final class ParcelableCompat {

    class ParcelableCompatCreatorHoneycombMR2 implements ClassLoaderCreator {
        private final ParcelableCompatCreatorCallbacks mCallbacks;

        ParcelableCompatCreatorHoneycombMR2(ParcelableCompatCreatorCallbacks parcelableCompatCreatorCallbacks) {
            this.mCallbacks = parcelableCompatCreatorCallbacks;
        }

        public Object createFromParcel(Parcel parcel) {
            return this.mCallbacks.createFromParcel(parcel, null);
        }

        public Object createFromParcel(Parcel parcel, ClassLoader classLoader) {
            return this.mCallbacks.createFromParcel(parcel, classLoader);
        }

        public Object[] newArray(int i) {
            return this.mCallbacks.newArray(i);
        }
    }

    private ParcelableCompat() {
    }

    @Deprecated
    public static Creator newCreator(ParcelableCompatCreatorCallbacks parcelableCompatCreatorCallbacks) {
        return new ParcelableCompatCreatorHoneycombMR2(parcelableCompatCreatorCallbacks);
    }
}
