package com.xiaomi.camera.imagecodec;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public class QueryFeatureSettingParameter implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public QueryFeatureSettingParameter createFromParcel(Parcel parcel) {
            return new QueryFeatureSettingParameter(parcel);
        }

        public QueryFeatureSettingParameter[] newArray(int i) {
            return new QueryFeatureSettingParameter[i];
        }
    };
    /* access modifiers changed from: private */
    public int mActiveCameraId;
    /* access modifiers changed from: private */
    public long mExposureTime;
    /* access modifiers changed from: private */
    public int mFeatureType;
    /* access modifiers changed from: private */
    public int mISO;

    public final class Builder {
        private final QueryFeatureSettingParameter mParameter = new QueryFeatureSettingParameter();

        public QueryFeatureSettingParameter build() {
            return this.mParameter;
        }

        public Builder setActiveCameraId(int i) {
            this.mParameter.mActiveCameraId = i;
            return this;
        }

        public Builder setExposureTime(long j) {
            this.mParameter.mExposureTime = j;
            return this;
        }

        public Builder setFeatureType(int i) {
            this.mParameter.mFeatureType = i;
            return this;
        }

        public Builder setISO(int i) {
            this.mParameter.mISO = i;
            return this;
        }
    }

    private QueryFeatureSettingParameter() {
        this.mFeatureType = 1;
    }

    protected QueryFeatureSettingParameter(Parcel parcel) {
        this.mActiveCameraId = parcel.readInt();
        this.mFeatureType = parcel.readInt();
        this.mISO = parcel.readInt();
        this.mExposureTime = parcel.readLong();
    }

    public int describeContents() {
        return 0;
    }

    public int getActiveCameraId() {
        return this.mActiveCameraId;
    }

    public long getExposureTime() {
        return this.mExposureTime;
    }

    public int getFeatureType() {
        return this.mFeatureType;
    }

    public int getISO() {
        return this.mISO;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.mActiveCameraId);
        parcel.writeInt(this.mFeatureType);
        parcel.writeInt(this.mISO);
        parcel.writeLong(this.mExposureTime);
    }
}
