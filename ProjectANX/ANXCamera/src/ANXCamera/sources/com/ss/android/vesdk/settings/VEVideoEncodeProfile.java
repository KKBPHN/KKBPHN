package com.ss.android.vesdk.settings;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.text.TextUtils;
import com.xiaomi.stat.a;

public enum VEVideoEncodeProfile implements Parcelable {
    ENCODE_PROFILE_UNKNOWN,
    ENCODE_PROFILE_BASELINE,
    ENCODE_PROFILE_MAIN,
    ENCODE_PROFILE_HIGH;
    
    public static final Creator CREATOR = null;

    static {
        CREATOR = new Creator() {
            public VEVideoEncodeProfile createFromParcel(Parcel parcel) {
                return VEVideoEncodeProfile.values()[parcel.readInt()];
            }

            public VEVideoEncodeProfile[] newArray(int i) {
                return new VEVideoEncodeProfile[i];
            }
        };
    }

    public static VEVideoEncodeProfile valueOfString(String str) {
        VEVideoEncodeProfile vEVideoEncodeProfile = ENCODE_PROFILE_UNKNOWN;
        return !TextUtils.isEmpty(str) ? "baseline".equals(str) ? ENCODE_PROFILE_BASELINE : a.d.equals(str) ? ENCODE_PROFILE_MAIN : "high".equals(str) ? ENCODE_PROFILE_HIGH : vEVideoEncodeProfile : vEVideoEncodeProfile;
    }

    public int describeContents() {
        return 0;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(ordinal());
    }
}
