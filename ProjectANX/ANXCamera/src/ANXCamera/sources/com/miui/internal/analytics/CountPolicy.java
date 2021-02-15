package com.miui.internal.analytics;

import java.util.HashMap;
import java.util.Map;

public class CountPolicy extends Policy {
    private static final String COUNT_CATEGORY = "CountPolicyEvent";
    public static final String TAG = "count";
    private Map mCountEventItems = new HashMap();

    public void end() {
        for (String str : this.mCountEventItems.keySet()) {
            for (String str2 : ((Map) this.mCountEventItems.get(str)).keySet()) {
                HashMap hashMap = new HashMap();
                hashMap.put(str2, "counts");
                String str3 = str;
                TrackEvent trackEvent = new TrackEvent(str3, COUNT_CATEGORY, hashMap, (long) ((Integer) ((Map) this.mCountEventItems.get(str)).get(str2)).intValue());
                trackEvent.dispatch();
            }
        }
        this.mCountEventItems.clear();
    }

    public void execute(Event event) {
        Map map = (Map) this.mCountEventItems.get(event.getPackageName());
        if (map == null) {
            map = new HashMap();
            this.mCountEventItems.put(event.getPackageName(), map);
        }
        Integer num = (Integer) map.get(event.getEventId());
        map.put(event.getEventId(), num == null ? Integer.valueOf(1) : Integer.valueOf(num.intValue() + 1));
    }

    public void prepare() {
    }
}
