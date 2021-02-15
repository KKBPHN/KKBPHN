package androidx.versionedparcelable;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import androidx.annotation.RestrictTo;
import androidx.annotation.RestrictTo.Scope;

@SuppressLint({"BanParcelableUsage"})
@RestrictTo({Scope.LIBRARY})
public class ParcelImpl implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public ParcelImpl createFromParcel(Parcel parcel) {
            return new ParcelImpl(parcel);
        }

        public ParcelImpl[] newArray(int i) {
            return new ParcelImpl[i];
        }
    };
    private final VersionedParcelable mParcel;

    protected ParcelImpl(Parcel parcel) {
        this.mParcel = new VersionedParcelParcel(parcel).readVersionedParcelable();
    }

    public ParcelImpl(VersionedParcelable versionedParcelable) {
        this.mParcel = versionedParcelable;
    }

    public int describeContents() {
        return 0;
    }

    public VersionedParcelable getVersionedParcel() {
        return this.mParcel;
    }

    public void writeToParcel(Parcel parcel, int i) {
        new VersionedParcelParcel(parcel).writeVersionedParcelable(this.mParcel);
    }
}
