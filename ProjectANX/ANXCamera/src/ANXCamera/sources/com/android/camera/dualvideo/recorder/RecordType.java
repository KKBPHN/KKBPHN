package com.android.camera.dualvideo.recorder;

import androidx.annotation.NonNull;
import java.util.Arrays;

public enum RecordType {
    MERGED(r2, 1),
    STANDALONE(r4, 2);
    
    private int mIndex;
    private String mName;

    private RecordType(String str, int i) {
        this.mName = str;
        this.mIndex = i;
    }

    public static RecordType getTypeByName(String str) {
        return (RecordType) Arrays.stream(values()).filter(new O0000Oo0(str)).findAny().orElse(null);
    }

    public int getIndex() {
        return this.mIndex;
    }

    public String getName() {
        return this.mName;
    }

    @NonNull
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("RecordType_");
        sb.append(this.mName);
        return sb.toString();
    }
}
