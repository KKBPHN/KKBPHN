package com.iqiyi.android.qigsaw.core.splitdownload;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;

public final class DownloadRequest implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public DownloadRequest createFromParcel(Parcel parcel) {
            return new DownloadRequest(parcel);
        }

        public DownloadRequest[] newArray(int i) {
            return new DownloadRequest[i];
        }
    };
    private final String fileDir;
    private final String fileMD5;
    private final String fileName;
    private final String moduleName;
    private final String url;

    public class Builder {
        /* access modifiers changed from: private */
        public String fileDir;
        /* access modifiers changed from: private */
        public String fileMD5;
        /* access modifiers changed from: private */
        public String fileName;
        /* access modifiers changed from: private */
        public String moduleName;
        /* access modifiers changed from: private */
        public String url;

        public DownloadRequest build() {
            return new DownloadRequest(this);
        }

        public Builder fileDir(String str) {
            this.fileDir = str;
            return this;
        }

        public Builder fileMD5(String str) {
            this.fileMD5 = str;
            return this;
        }

        public Builder fileName(String str) {
            this.fileName = str;
            return this;
        }

        public Builder moduleName(String str) {
            this.moduleName = str;
            return this;
        }

        public Builder url(String str) {
            this.url = str;
            return this;
        }
    }

    private DownloadRequest(Parcel parcel) {
        this.url = parcel.readString();
        this.fileDir = parcel.readString();
        this.fileName = parcel.readString();
        this.moduleName = parcel.readString();
        this.fileMD5 = parcel.readString();
    }

    private DownloadRequest(Builder builder) {
        this.fileDir = builder.fileDir;
        this.url = builder.url;
        this.fileName = builder.fileName;
        this.moduleName = builder.moduleName;
        this.fileMD5 = builder.fileMD5;
    }

    public static Builder newBuilder() {
        return new Builder();
    }

    public int describeContents() {
        return 0;
    }

    public String getFileDir() {
        return this.fileDir;
    }

    public String getFileMD5() {
        return this.fileMD5;
    }

    public String getFileName() {
        return this.fileName;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public String getUrl() {
        return this.url;
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(this.url);
        parcel.writeString(this.fileDir);
        parcel.writeString(this.fileName);
        parcel.writeString(this.moduleName);
    }
}
