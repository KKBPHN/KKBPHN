package com.miui.internal.analytics;

import android.os.Parcel;
import java.util.List;

public class TrackPageViewEvent extends Event {
    private static final String PAGEVIEW_EVENT = "_pageview_event_";

    public TrackPageViewEvent() {
        this.mPackageName = "";
        this.mType = Integer.valueOf(3);
        this.mEventId = PAGEVIEW_EVENT;
    }

    public TrackPageViewEvent(String str) {
        this.mPackageName = str;
    }

    public void dispatch() {
        List<Dispatchable> list = Event.sDispatcher;
        if (list != null) {
            for (Dispatchable dispatchPageView : list) {
                dispatchPageView.dispatchPageView(this);
            }
        }
    }

    /* access modifiers changed from: 0000 */
    public void readFromParcel(Parcel parcel) {
        super.readFromParcel(parcel);
    }

    public void writeEvent(Storable storable) {
        if (storable != null) {
            String str = this.mPackageName;
            Integer num = this.mType;
            String str2 = this.mEventId;
            StringBuilder sb = new StringBuilder();
            sb.append(this.mTrackTime);
            sb.append("");
            Storable storable2 = storable;
            storable2.writeData(str, num, str2, "", sb.toString(), Boolean.toString(false));
        }
    }

    public void writeToParcel(Parcel parcel, int i) {
        super.writeToParcel(parcel, i);
    }
}
