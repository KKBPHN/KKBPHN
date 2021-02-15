package defpackage;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

/* renamed from: k reason: default package */
public final class k implements Parcelable {
    public static final Creator CREATOR = new j();
    public final Parcelable a;

    public k(Parcelable parcelable) {
        String name = parcelable.getClass().getName();
        if (name.startsWith("android.os.") || name.equals("android.content.Intent") || name.equals("android.app.PendingIntent")) {
            this.a = parcelable;
            return;
        }
        throw new IllegalArgumentException("Only Android system classes can be passed in SystemParcelableWrapper.");
    }

    public final int describeContents() {
        return 0;
    }

    public final void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(this.a, i);
    }
}
