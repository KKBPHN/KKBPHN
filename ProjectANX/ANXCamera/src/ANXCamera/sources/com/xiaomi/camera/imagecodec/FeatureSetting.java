package com.xiaomi.camera.imagecodec;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Arrays;

public class FeatureSetting implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public FeatureSetting createFromParcel(Parcel parcel) {
            return new FeatureSetting(parcel);
        }

        public FeatureSetting[] newArray(int i) {
            return new FeatureSetting[i];
        }
    };
    private int mFrameCount;
    private long[] mTuningIndexes;

    public FeatureSetting(int i, long[] jArr) {
        this.mFrameCount = i;
        this.mTuningIndexes = jArr;
    }

    protected FeatureSetting(Parcel parcel) {
        this.mFrameCount = parcel.readInt();
        this.mTuningIndexes = parcel.createLongArray();
    }

    public int describeContents() {
        return 0;
    }

    public int getFrameCount() {
        return this.mFrameCount;
    }

    public long[] getTuningIndexes() {
        return this.mTuningIndexes;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("FeatureSetting{frameCount=");
        sb.append(this.mFrameCount);
        sb.append(", tuningIndexes=");
        sb.append(Arrays.toString(this.mTuningIndexes));
        sb.append('}');
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mFrameCount);
        parcel.writeLongArray(this.mTuningIndexes);
    }
}
