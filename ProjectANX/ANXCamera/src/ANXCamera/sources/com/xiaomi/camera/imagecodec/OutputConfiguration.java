package com.xiaomi.camera.imagecodec;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.Locale;

public class OutputConfiguration implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public OutputConfiguration createFromParcel(Parcel parcel) {
            return new OutputConfiguration(parcel);
        }

        public OutputConfiguration[] newArray(int i) {
            return new OutputConfiguration[i];
        }
    };
    private int mFormat;
    private int mHeight;
    private int mWidth;

    public OutputConfiguration(int i, int i2, int i3) {
        this.mWidth = i;
        this.mHeight = i2;
        this.mFormat = i3;
    }

    public OutputConfiguration(Parcel parcel) {
        this.mWidth = parcel.readInt();
        this.mHeight = parcel.readInt();
        this.mFormat = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof OutputConfiguration)) {
            return false;
        }
        OutputConfiguration outputConfiguration = (OutputConfiguration) obj;
        if (!(this.mWidth == outputConfiguration.mWidth && this.mHeight == outputConfiguration.mHeight && this.mFormat == outputConfiguration.mFormat)) {
            z = false;
        }
        return z;
    }

    public int getFormat() {
        return this.mFormat;
    }

    public int getHeight() {
        return this.mHeight;
    }

    public int getWidth() {
        return this.mWidth;
    }

    public int hashCode() {
        return HashCodeHelpers.hashCode(this.mWidth, this.mHeight, this.mFormat);
    }

    public String toString() {
        return String.format(Locale.ENGLISH, "OutputConfiguration(w:%d, h:%d, format:%d)", new Object[]{Integer.valueOf(this.mWidth), Integer.valueOf(this.mHeight), Integer.valueOf(this.mFormat)});
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mWidth);
        parcel.writeInt(this.mHeight);
        parcel.writeInt(this.mFormat);
    }
}
