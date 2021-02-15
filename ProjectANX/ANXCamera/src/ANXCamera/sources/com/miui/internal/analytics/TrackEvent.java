package com.miui.internal.analytics;

import android.database.Cursor;
import android.os.Parcel;
import android.text.TextUtils;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TrackEvent extends Event {
    private Map mParam;
    private long mValue;

    public TrackEvent() {
        this.mPackageName = "";
        this.mType = Integer.valueOf(2);
        this.mParam = null;
        this.mValue = 0;
    }

    public TrackEvent(String str, String str2, Map map, long j) {
        this.mPackageName = str;
        this.mType = Integer.valueOf(2);
        this.mEventId = str2;
        this.mParam = map;
        this.mValue = j;
    }

    private String buildParam(Map map) {
        if (map == null) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String str : map.keySet()) {
            sb.append(str);
            String str2 = "#ITEMSPLITTER#";
            sb.append(str2);
            sb.append((String) map.get(str));
            sb.append(str2);
        }
        return sb.toString();
    }

    private void parseParam(String str) {
        if (!TextUtils.isEmpty(str)) {
            this.mParam = new HashMap();
            String[] split = str.split("#ITEMSPLITTER#");
            for (int i = 0; i < split.length - 1; i += 2) {
                this.mParam.put(split[i], split[i + 1]);
            }
        }
    }

    public void dispatch() {
        List<Dispatchable> list = Event.sDispatcher;
        if (list != null) {
            for (Dispatchable dispatchEvent : list) {
                dispatchEvent.dispatchEvent(this);
            }
        }
    }

    public Map getParam() {
        return this.mParam;
    }

    public long getValue() {
        return this.mValue;
    }

    /* access modifiers changed from: 0000 */
    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
        parseParam(parcel.readString());
        this.mValue = parcel.readLong();
    }

    public void restore(Cursor cursor) {
        super.restore(cursor);
        if (cursor != null) {
            this.mValue = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("value")));
            parseParam(cursor.getString(cursor.getColumnIndexOrThrow(EventUtils.COLUMN_PARAM)));
        }
    }

    public void writeEvent(Storable storable) {
        if (storable != null) {
            String str = this.mPackageName;
            Integer num = this.mType;
            String str2 = this.mEventId;
            String buildParam = buildParam(this.mParam);
            StringBuilder sb = new StringBuilder();
            sb.append(this.mTrackTime);
            String str3 = "";
            sb.append(str3);
            String sb2 = sb.toString();
            StringBuilder sb3 = new StringBuilder();
            sb3.append(this.mValue);
            sb3.append(str3);
            storable.writeData(str, num, str2, buildParam, sb2, sb3.toString());
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
        parcel.writeString(buildParam(this.mParam));
        parcel.writeLong(this.mValue);
    }
}
