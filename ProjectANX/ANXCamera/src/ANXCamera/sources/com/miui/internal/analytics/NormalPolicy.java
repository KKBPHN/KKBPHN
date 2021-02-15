package com.miui.internal.analytics;

public class NormalPolicy extends Policy {
    public static final String TAG = "normal";

    public void end() {
    }

    public void execute(Event event) {
        event.dispatch();
    }

    public void prepare() {
    }
}
