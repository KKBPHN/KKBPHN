package com.android.camera.features.mimoji2.bean;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import com.android.camera.features.mimoji2.widget.helper.AvatarEngineManager2;
import java.util.ArrayList;

public class MimojiLevelBean2 implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public MimojiLevelBean2 createFromParcel(Parcel parcel) {
            return new MimojiLevelBean2(parcel);
        }

        public MimojiLevelBean2[] newArray(int i) {
            return new MimojiLevelBean2[i];
        }
    };
    public volatile int mConfigType;
    public volatile String mConfigTypeName;
    public ArrayList mThumnails = new ArrayList();

    public MimojiLevelBean2() {
    }

    protected MimojiLevelBean2(Parcel parcel) {
        this.mConfigTypeName = parcel.readString();
        this.mConfigType = parcel.readInt();
    }

    public int describeContents() {
        return 0;
    }

    public ArrayList getColorConfigInfos() {
        AvatarEngineManager2 instance = AvatarEngineManager2.getInstance();
        if (instance == null) {
            return null;
        }
        return instance.getSubConfigColorList(this.mConfigType);
    }

    public int getColorType() {
        AvatarEngineManager2 instance = AvatarEngineManager2.getInstance();
        if (instance == null) {
            return -1;
        }
        return instance.getColorType(this.mConfigType);
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.mConfigTypeName);
        parcel.writeInt(this.mConfigType);
    }
}
