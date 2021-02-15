package com.airbnb.lottie.network;

import com.airbnb.lottie.O00000o.O00000o;
import com.iqiyi.android.qigsaw.core.common.SplitConstants;

public enum FileExtension {
    JSON(SplitConstants.DOT_JSON),
    ZIP(".zip");
    
    public final String extension;

    private FileExtension(String str) {
        this.extension = str;
    }

    public static FileExtension O000Oo0O(String str) {
        FileExtension[] values;
        for (FileExtension fileExtension : values()) {
            if (str.endsWith(fileExtension.extension)) {
                return fileExtension;
            }
        }
        StringBuilder sb = new StringBuilder();
        sb.append("Unable to find correct extension for ");
        sb.append(str);
        O00000o.warning(sb.toString());
        return JSON;
    }

    public String Oo0o() {
        StringBuilder sb = new StringBuilder();
        sb.append(".temp");
        sb.append(this.extension);
        return sb.toString();
    }

    public String toString() {
        return this.extension;
    }
}
