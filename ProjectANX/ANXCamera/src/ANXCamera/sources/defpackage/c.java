package defpackage;

import android.os.IInterface;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/* renamed from: c reason: default package */
public final class c {
    static {
        c.class.getClassLoader();
    }

    private c() {
    }

    public static Parcelable O000000o(Parcel parcel, Creator creator) {
        if (parcel.readInt() != 0) {
            return (Parcelable) creator.createFromParcel(parcel);
        }
        return null;
    }

    /* JADX WARNING: type inference failed for: r1v0, types: [android.os.IInterface, android.os.IBinder] */
    /* JADX WARNING: Incorrect type for immutable var: ssa=android.os.IInterface, code=null, for r1v0, types: [android.os.IInterface, android.os.IBinder] */
    /* JADX WARNING: Unknown variable types count: 1 */
    /* Code decompiled incorrectly, please refer to instructions dump. */
    public static void O000000o(Parcel parcel, IInterface r1) {
        parcel.writeStrongBinder(r1);
    }

    public static void O000000o(Parcel parcel, Parcelable parcelable) {
        parcel.writeInt(1);
        parcelable.writeToParcel(parcel, 0);
    }
}
