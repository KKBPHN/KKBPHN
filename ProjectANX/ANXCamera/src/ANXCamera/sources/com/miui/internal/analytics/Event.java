package com.miui.internal.analytics;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;
import android.os.Parcelable.Creator;
import android.util.Log;
import java.util.List;

public abstract class Event implements Parcelable {
    public static final Creator CREATOR = new Creator() {
        public Event createFromParcel(Parcel parcel) {
            String str;
            String str2 = Event.TAG;
            try {
                Event event = (Event) Class.forName(parcel.readString()).newInstance();
                event.readFromParcel(parcel);
                return event;
            } catch (InstantiationException e) {
                e = e;
                str = "InstantiationException catched when create event from parcel";
                Log.e(str2, str, e);
                return null;
            } catch (IllegalAccessException e2) {
                e = e2;
                str = "IllegalAccessException catched when create event from parcel";
                Log.e(str2, str, e);
                return null;
            } catch (ClassNotFoundException e3) {
                e = e3;
                str = "ClassNotFoundException catched when create event from parcel";
                Log.e(str2, str, e);
                return null;
            }
        }

        public Event[] newArray(int i) {
            return new Event[i];
        }
    };
    protected static final String SPLITTER = "#ITEMSPLITTER#";
    private static final String TAG = "AnalyticsEvent";
    protected static List sDispatcher;
    protected String mEventId;
    protected String mPackageName;
    protected String mPolicy;
    protected long mTrackTime;
    protected Integer mType;

    public Event() {
        setTime(System.currentTimeMillis());
    }

    public static void setDispatcher(List list) {
        sDispatcher = list;
    }

    public int describeContents() {
        return 0;
    }

    public abstract void dispatch();

    public String getEventId() {
        return this.mEventId;
    }

    public String getPackageName() {
        return this.mPackageName;
    }

    public String getPolicy() {
        return this.mPolicy;
    }

    public long getTrackTime() {
        return this.mTrackTime;
    }

    public Integer getType() {
        return this.mType;
    }

    /* access modifiers changed from: 0000 */
    public void readFromParcel(Parcel parcel) {
        this.mPackageName = parcel.readString();
        this.mType = Integer.valueOf(parcel.readInt());
        this.mEventId = parcel.readString();
        this.mPolicy = parcel.readString();
        this.mTrackTime = parcel.readLong();
    }

    public void restore(Cursor cursor) {
        if (cursor != null) {
            this.mPackageName = cursor.getString(cursor.getColumnIndexOrThrow(EventUtils.COLUMN_PACKAGE_NAME));
            this.mEventId = cursor.getString(cursor.getColumnIndexOrThrow(EventUtils.COLUMN_ID));
            this.mTrackTime = Long.parseLong(cursor.getString(cursor.getColumnIndexOrThrow("time")));
        }
    }

    public void setPolicy(String str) {
        this.mPolicy = str;
    }

    public void setTime(long j) {
        this.mTrackTime = j;
    }

    public abstract void writeEvent(Storable storable);

    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(getClass().getName());
        parcel.writeString(this.mPackageName);
        parcel.writeInt(this.mType.intValue());
        parcel.writeString(this.mEventId);
        parcel.writeString(this.mPolicy);
        parcel.writeLong(this.mTrackTime);
    }
}
