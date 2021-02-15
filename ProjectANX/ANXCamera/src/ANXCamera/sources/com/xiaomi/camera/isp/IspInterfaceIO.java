package com.xiaomi.camera.isp;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Size;
import com.xiaomi.camera.imagecodec.HashCodeHelpers;
import com.xiaomi.camera.imagecodec.OutputConfiguration;
import java.util.Locale;

public class IspInterfaceIO implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public IspInterfaceIO createFromParcel(Parcel parcel) {
            return new IspInterfaceIO(parcel);
        }

        public IspInterfaceIO[] newArray(int i) {
            return new IspInterfaceIO[i];
        }
    };
    private OutputConfiguration mPicOutputConfiguration;
    private Size mRawInputSize;
    private Size mYuvInputSize;
    private OutputConfiguration mYuvOutputConfiguration;

    public IspInterfaceIO(Parcel parcel) {
        this.mYuvInputSize = new Size(parcel.readInt(), parcel.readInt());
        this.mPicOutputConfiguration = new OutputConfiguration(parcel);
        if (parcel.readInt() > 0) {
            this.mRawInputSize = new Size(parcel.readInt(), parcel.readInt());
            this.mYuvOutputConfiguration = new OutputConfiguration(parcel);
        }
    }

    public IspInterfaceIO(@NonNull Size size, @Nullable Size size2, @NonNull OutputConfiguration outputConfiguration) {
        this.mYuvInputSize = size;
        this.mPicOutputConfiguration = outputConfiguration;
        if (size2 != null) {
            this.mRawInputSize = size2;
            this.mYuvOutputConfiguration = new OutputConfiguration(size.getWidth(), size.getHeight(), 35);
        }
    }

    public int describeContents() {
        return 0;
    }

    public boolean equals(Object obj) {
        boolean z = true;
        if (this == obj) {
            return true;
        }
        if (obj == null || IspInterfaceIO.class != obj.getClass()) {
            return false;
        }
        IspInterfaceIO ispInterfaceIO = (IspInterfaceIO) obj;
        Size size = this.mRawInputSize;
        if (size == null) {
            if (!this.mYuvInputSize.equals(ispInterfaceIO.mYuvInputSize) || !this.mPicOutputConfiguration.equals(ispInterfaceIO.mPicOutputConfiguration)) {
                z = false;
            }
            return z;
        }
        if (!size.equals(ispInterfaceIO.mRawInputSize) || !this.mYuvInputSize.equals(ispInterfaceIO.mYuvInputSize) || !this.mPicOutputConfiguration.equals(ispInterfaceIO.mPicOutputConfiguration)) {
            z = false;
        }
        return z;
    }

    public OutputConfiguration getPicOutputConfiguration() {
        return this.mPicOutputConfiguration;
    }

    public Size getRawInputSize() {
        return this.mRawInputSize;
    }

    public Size getYuvInputSize() {
        return this.mYuvInputSize;
    }

    public OutputConfiguration getYuvOutputConfiguration() {
        return this.mYuvOutputConfiguration;
    }

    public int hashCode() {
        Size size = this.mRawInputSize;
        if (size == null) {
            return HashCodeHelpers.hashCode(this.mYuvInputSize.getWidth(), this.mYuvInputSize.getHeight(), this.mPicOutputConfiguration.hashCode());
        }
        return HashCodeHelpers.hashCode(size.getWidth(), this.mRawInputSize.getHeight(), this.mYuvInputSize.getWidth(), this.mYuvInputSize.getHeight(), this.mPicOutputConfiguration.hashCode());
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        Size size = this.mRawInputSize;
        if (size != null) {
            sb.append(String.format(Locale.ENGLISH, "RawInputSize: %dx%d ", new Object[]{Integer.valueOf(size.getWidth()), Integer.valueOf(this.mRawInputSize.getHeight())}));
        }
        sb.append(String.format(Locale.ENGLISH, "YuvInputSize: %dx%d ", new Object[]{Integer.valueOf(this.mYuvInputSize.getWidth()), Integer.valueOf(this.mYuvInputSize.getHeight())}));
        sb.append(this.mPicOutputConfiguration.toString());
        return sb.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeInt(this.mYuvInputSize.getWidth());
            parcel.writeInt(this.mYuvInputSize.getHeight());
            parcel.writeParcelable(this.mPicOutputConfiguration, 0);
            if (this.mRawInputSize != null) {
                parcel.writeInt(1);
                parcel.writeInt(this.mRawInputSize.getWidth());
                parcel.writeInt(this.mRawInputSize.getHeight());
                parcel.writeParcelable(this.mYuvOutputConfiguration, 0);
                return;
            }
            parcel.writeInt(0);
            return;
        }
        throw new IllegalArgumentException("dest must not be null");
    }
}
