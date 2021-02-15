package com.ss.android.ugc.effectmanager.common.model;

import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import java.util.List;

public class UrlModel implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public UrlModel createFromParcel(Parcel parcel) {
            return new UrlModel(parcel);
        }

        public UrlModel[] newArray(int i) {
            return new UrlModel[i];
        }
    };
    private String uri;
    private List url_list;

    public UrlModel() {
    }

    private UrlModel(Parcel parcel) {
        this.url_list = parcel.createStringArrayList();
        this.uri = parcel.readString();
    }

    public int describeContents() {
        return 0;
    }

    public String getUri() {
        return this.uri;
    }

    public List getUrlList() {
        return this.url_list;
    }

    public void setUri(String str) {
        this.uri = str;
    }

    public void setUrlList(List list) {
        this.url_list = list;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        List<String> list = this.url_list;
        if (list != null) {
            for (String str : list) {
                sb.append(" [");
                sb.append(str);
                sb.append("] ");
            }
        }
        StringBuilder sb2 = new StringBuilder();
        sb2.append("UrlModel{url_list=");
        sb2.append(sb.toString());
        sb2.append(", uri='");
        sb2.append(this.uri);
        sb2.append('\'');
        sb2.append('}');
        return sb2.toString();
    }

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(this.url_list);
        parcel.writeString(this.uri);
    }
}
