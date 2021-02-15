package com.miui.internal.analytics;

import java.util.HashMap;
import java.util.Map;

public class LastPolicy extends Policy {
    public static final String TAG = "last";
    private Map mItems = new HashMap();

    public void end() {
        for (String str : this.mItems.keySet()) {
            ((Event) this.mItems.get(str)).dispatch();
        }
        this.mItems.clear();
    }

    public void execute(Event event) {
        this.mItems.put(event.getEventId(), event);
    }

    public void prepare() {
    }
}
