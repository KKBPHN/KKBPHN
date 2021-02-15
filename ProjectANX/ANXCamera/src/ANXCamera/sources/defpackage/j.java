package defpackage;

import android.annotation.SuppressLint;
import android.os.Parcel;
import android.os.Parcelable.Creator;

/* renamed from: j reason: default package */
final class j implements Creator {
    @SuppressLint({"ParcelClassLoader"})
    public final /* bridge */ /* synthetic */ Object createFromParcel(Parcel parcel) {
        return new k(parcel.readParcelable(null));
    }

    public final /* bridge */ /* synthetic */ Object[] newArray(int i) {
        return new k[i];
    }
}
