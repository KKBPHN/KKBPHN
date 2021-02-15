package com.xiaomi.mi_connect_service;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;

public class DpsCallbackData implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public DpsCallbackData createFromParcel(Parcel parcel) {
            return new DpsCallbackData(parcel);
        }

        public DpsCallbackData[] newArray(int i) {
            return new DpsCallbackData[i];
        }
    };
    public static final int INVALID_MESSAGE = 1;
    public static final String TAG = "DpsCallbackData";
    public static final int VALID_MESSAGE = 0;
    private int mCode;
    private byte[] mMessage;
    private String mPartition;
    private String mTopicName;

    public DpsCallbackData(int i, String str, String str2, byte[] bArr) {
        this.mCode = i;
        this.mTopicName = str;
        this.mPartition = str2;
        this.mMessage = bArr;
    }

    public DpsCallbackData(Parcel parcel) {
        readFromParcel(parcel);
    }

    private void readFromParcel(Parcel parcel) {
        this.mCode = parcel.readInt();
        this.mTopicName = parcel.readString();
        this.mPartition = parcel.readString();
        int readInt = parcel.readInt();
        if (readInt == 0) {
            this.mMessage = new byte[parcel.readInt()];
            parcel.readByteArray(this.mMessage);
        } else if (1 == readInt) {
            Log.w(TAG, "readFromParcel: invalid message ...");
        }
    }

    public int describeContents() {
        return 0;
    }

    public int getCode() {
        return this.mCode;
    }

    public byte[] getMessage() {
        return this.mMessage;
    }

    public String getPartition() {
        return this.mPartition;
    }

    public String getTopicName() {
        return this.mTopicName;
    }

    public void writeToParcel(Parcel parcel, int i) {
        if (parcel != null) {
            parcel.writeInt(this.mCode);
            parcel.writeString(this.mTopicName);
            parcel.writeString(this.mPartition);
            if (this.mMessage != null) {
                parcel.writeInt(0);
                parcel.writeInt(this.mMessage.length);
                parcel.writeByteArray(this.mMessage);
                return;
            }
            parcel.writeInt(1);
        }
    }
}
