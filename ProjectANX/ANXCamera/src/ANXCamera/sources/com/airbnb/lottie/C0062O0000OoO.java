package com.airbnb.lottie;

import android.os.Parcel;
import android.os.Parcelable.Creator;

/* renamed from: com.airbnb.lottie.O0000OoO reason: case insensitive filesystem */
class C0062O0000OoO implements Creator {
    C0062O0000OoO() {
    }

    public SavedState createFromParcel(Parcel parcel) {
        return new SavedState(parcel, null);
    }

    public SavedState[] newArray(int i) {
        return new SavedState[i];
    }
}
