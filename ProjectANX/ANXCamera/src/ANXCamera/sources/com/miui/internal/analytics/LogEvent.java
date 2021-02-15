package com.miui.internal.analytics;

import android.database.Cursor;
import android.os.Parcel;
import android.text.TextUtils;
import java.util.List;

public class LogEvent extends Event {
    private String mErrorClass;
    private String mMessage;

    public LogEvent() {
        String str = "";
        this.mPackageName = str;
        this.mMessage = str;
        this.mErrorClass = str;
    }

    public LogEvent(String str, String str2, String str3, String str4) {
        this.mPackageName = str;
        this.mEventId = str2;
        this.mMessage = str3;
        this.mErrorClass = str4;
    }

    private String buildParam() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.mMessage);
        sb.append("#ITEMSPLITTER#");
        sb.append(this.mErrorClass);
        return sb.toString();
    }

    private void parseParam(String str) {
        if (!TextUtils.isEmpty(str)) {
            String[] split = str.split("#ITEMSPLITTER#");
            if (split.length > 1) {
                this.mMessage = split[0];
                this.mErrorClass = split[1];
            }
        }
    }

    public void dispatch() {
        List<Dispatchable> list = Event.sDispatcher;
        if (list != null) {
            for (Dispatchable dispatchLog : list) {
                dispatchLog.dispatchLog(this);
            }
        }
    }

    public String getErrorClass() {
        return this.mErrorClass;
    }

    public String getMessage() {
        return this.mMessage;
    }

    /* access modifiers changed from: 0000 */
    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        this.mMessage = parcel.readString();
        this.mErrorClass = parcel.readString();
    }

    public void restore(Cursor cursor) {
        super.restore(cursor);
        if (cursor != null) {
            parseParam(cursor.getString(cursor.getColumnIndexOrThrow(EventUtils.COLUMN_PARAM)));
        }
    }

    public void writeEvent(Storable storable) {
        if (storable != null) {
            String str = this.mPackageName;
            Integer num = this.mType;
            String str2 = this.mEventId;
            String buildParam = buildParam();
            StringBuilder sb = new StringBuilder();
            sb.append(this.mTrackTime);
            sb.append("");
            storable.writeData(str, num, str2, buildParam, sb.toString(), Boolean.toString(false));
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(this.mMessage);
        parcel.writeString(this.mErrorClass);
    }
}
