package com.android.camera.fragment.mimoji;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.ArrayList;

public class MimojiLevelBean implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public MimojiLevelBean createFromParcel(Parcel parcel) {
            return new MimojiLevelBean(parcel);
        }

        public MimojiLevelBean[] newArray(int i) {
            return new MimojiLevelBean[i];
        }
    };
    public volatile int configType;
    public volatile String configTypeName;
    public ArrayList thumnails = new ArrayList();

    public MimojiLevelBean() {
    }

    protected MimojiLevelBean(Parcel parcel) {
        this.configTypeName = parcel.readString();
        this.configType = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList getColorConfigInfos() {
        AvatarEngineManager instance = AvatarEngineManager.getInstance();
        if (instance == null) {
            return null;
        }
        return instance.getSubConfigColorList(this.configType);
    }

    public int getColorType() {
        AvatarEngineManager instance = AvatarEngineManager.getInstance();
        if (instance == null) {
            return -1;
        }
        return instance.getColorType(this.configType);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.configTypeName);
        parcel.writeInt(this.configType);
    }
}
